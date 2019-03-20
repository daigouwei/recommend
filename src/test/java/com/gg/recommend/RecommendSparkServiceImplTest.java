package com.gg.recommend;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author guowei
 * @date 2019/1/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommendApplication.class)
@WebAppConfiguration
public class RecommendSparkServiceImplTest {
    public static void main(String[] args) {
        try {
            new RecommendSparkServiceImplTest().testSpark();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//    @Test
    public void testSpark() throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("test").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaPairRDD<String, String> newProfile = sc.parallelize(
                Arrays.asList("a 1,2,3", "b 11,12,13", "c 21,22,23", "d 3,5,7", "e 12,17", "f 23,25,27,29", "g 23,25"))
                .mapToPair(line -> {
                    String[] lineArr = line.split(" ");
                    return new Tuple2<String, String>(lineArr[0], lineArr[1]);
                });
        //生成用户向量
        JavaPairRDD<String, List<String>> userVectorRDD =
                newProfile.mapValues(items -> Stream.of(items.split(",")).distinct().collect(Collectors.toList()));
        //生成共现矩阵
        JavaPairRDD<String, String> userAndSingleItemRDD = userVectorRDD.flatMapValues(items -> items);
        JavaPairRDD<Tuple2<String, String>, Integer> similarItemRDD = userAndSingleItemRDD.join(userAndSingleItemRDD)
                .mapToPair(tuple -> new Tuple2<Tuple2<String, String>, Integer>(tuple._2(), 1))
                .reduceByKey((sum1, sum2) -> sum1 + sum2);
        JavaPairRDD<String, String> concurrenceMatrixRDD = similarItemRDD.mapToPair(tuple -> {
            return new Tuple2<String, String>(tuple._1()._1(), tuple._1()._2() + ":" + tuple._2());
        }).reduceByKey((similarItem1, similarItem2) -> similarItem1 + "," + similarItem2);
        //广播变量可以用于缓存
        final Broadcast<Map<String, String>> broadCastMap = sc.broadcast(concurrenceMatrixRDD.collectAsMap());
        //用户推荐结果
        JavaPairRDD<String, String> userRecommendRDD = userVectorRDD.mapValues(items -> {
            final Map<String, Integer> similarItemScoreMap = new HashMap<>();
            items.forEach(item -> {
                String similarItems = broadCastMap.value().getOrDefault(item, null);
                if (null == similarItems) {
                    return;
                }
                Stream.of(similarItems.split(",")).forEach(similarItem -> {
                    String[] similarItemArr = similarItem.split(":");
                    similarItemScoreMap.merge(similarItemArr[0], Integer.valueOf(similarItemArr[1]),
                            (score1, score2) -> score1 + score2);
                });
            });
            return similarItemScoreMap.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                    .filter(e -> !items.contains(e.getKey())).map(map -> map.getKey() + ":" + map.getValue())
                    .collect(Collectors.joining(","));
        });
        List<Tuple2<String, String>> results = userRecommendRDD.collect();
        Thread.sleep(1000000);
        System.out.println();
    }
}
