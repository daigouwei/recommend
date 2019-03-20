package com.gg.recommend.util;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author guowei
 * @date 2019/1/13
 */
public class HBaseUtils {
    //    private Connection hConnection = ConnectionFactory.createConnection(hConf);
    //    private TableName hTableName = TableName.valueOf("hbase_recommend");

    public static void createTable(Connection connection, TableName tableName, String... families) throws Exception {
        Admin admin = null;
        try {
            admin = connection.getAdmin();
            if (!admin.tableExists(tableName)) {
                HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
                for (String family : families) {
                    tableDescriptor.addFamily(new HColumnDescriptor(family));
                }
                admin.createTable(tableDescriptor);
            }
        }
        finally {
            if (null != admin) {
                admin.close();
            }
        }
    }

    public static void deleteTable(Connection connection, TableName tableName) throws Exception {
        Admin admin = null;
        try {
            admin = connection.getAdmin();
            if (admin.tableExists(tableName)) {
                //delete之前必须disable
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            }
        }
        finally {
            if (null != admin) {
                admin.close();
            }
        }
    }

    public static void put(Connection connection, TableName tableName, String rowKey, String family, String qualifier,
            String value) throws Exception {
        Table table = null;
        try {
            table = connection.getTable(tableName);
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);
        }
        finally {
            if (null != table) {
                table.close();
            }
        }
    }

    public static void put(Connection connection, TableName tableName, List<String[]> datas) throws Exception {
        Table table = null;
        try {
            table = connection.getTable(tableName);
            List<Put> puts = datas.stream().map(data -> {
                Put put = new Put(Bytes.toBytes(data[0]));
                put.addColumn(Bytes.toBytes(data[1]), Bytes.toBytes(data[2]), Bytes.toBytes(data[3]));
                return put;
            }).collect(Collectors.toList());
            table.put(puts);
        }
        finally {
            if (null != table) {
                table.close();
            }
        }
    }

    public static byte[] get(Connection connection, TableName tableName, String rowKey, String family, String qualifier)
            throws Exception {
        Table table = null;
        try {
            table = connection.getTable(tableName);
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);
            Cell latestCell = result.getColumnLatestCell(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            return latestCell.getValueArray();
        }
        finally {
            if (null != table) {
                table.close();
            }
        }
    }
}
