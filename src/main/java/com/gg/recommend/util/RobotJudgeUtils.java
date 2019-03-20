package com.gg.recommend.util;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author guowei
 * @date 2018/5/24
 */
public class RobotJudgeUtils {
    // 爬虫机器人列表
    private final static String[] ROBOT_SPIDER =
            {".*appie.*", ".*architext.*", ".*jeeves.*", ".*bjaaland.*", ".*contentmatch.*", ".*ferret.*",
                    ".*googlebot.*", ".*google\\-sitemaps.*", ".*gulliver.*", ".*virus[_+ ]detector.*", ".*harvest.*",
                    ".*htdig.*", ".*linkwalker.*", ".*lilina.*", ".*lycos[_+ ].*", ".*moget.*", ".*muscatferret.*",
                    ".*myweb.*", ".*nomad.*", ".*scooter.*", ".*slurp.*", ".*^voyager\\/.*", ".*weblayers.*",
                    ".*antibot.*", ".*bruinbot.*", ".*digout4u.*", ".*echo!.*", ".*fast\\-webcrawler.*",
                    ".*ia_archiver\\-web\\.archive\\.org.*", ".*ia_archiver.*", ".*jennybot.*", ".*mercator.*",
                    ".*netcraft.*", ".*msnbot\\-media.*", ".*msnbot.*", ".*petersnews.*", ".*relevantnoise\\.com.*",
                    ".*unlost_web_crawler.*", ".*voila.*", ".*webbase.*", ".*webcollage.*", ".*cfetch.*", ".*zyborg.*",
                    ".*wisenutbot.*", ".*ask jeeves.*", ".*winhttp robot.*", ".*exabot.*", ".*php monitor.*",
                    ".*speedy spider.*", ".*baiduspider.*", ".*pingdom_gigrib_bot.*", ".*java.*", ".*twiceler.*",
                    ".*htmlparser.*"};
    // 浏览器列表
    private final static String[] BROWSER_LIST =
            {"msie", "netscape", "firefox", "svn", "chrome", "firebird", "go!zilla", "icab", "konqueror", "links",
                    "lynx", "omniweb", "opera", "22acidownload", "aol\\-iweng", "amaya", "amigavoyager", "aweb",
                    "bonecho", "bpftp", "camino", "chimera", "cyberdog", "dillo", "doris", "dreamcast", "xbox",
                    "downloadagent", "ecatch", "emailsiphon", "encompass", "epiphany", "friendlyspider", "fresco",
                    "galeon", "flashget", "freshdownload", "getright", "leechget", "netants", "headdump", "hotjava",
                    "ibrowse", "intergo", "k\\-meleon", "linemodebrowser", "lotus\\-notes", "macweb", "multizilla",
                    "ncsa_mosaic", "netcaptor", "netpositive", "nutscrape", "msfrontpageexpress", "phoenix", "shiira",
                    "safari", "tzgeturl", "viking", "webfetcher", "webexplorer", "webmirror", "webvcr"};
    // 搜索引擎列表
    private final static String[] SEARCH_ENGINES =
            {".*google\\.[\\w.]+/products.*", ".*base\\.google\\..*", ".*froogle\\.google\\..*",
                    ".*groups\\.google\\..*", ".*images\\.google\\..*", ".*google\\..*", ".*googlee\\..*",
                    ".*googlecom\\.com.*", ".*goggle\\.co\\.hu.*", ".*216\\.239\\.(35|37|39|51)\\.100.*",
                    ".*216\\.239\\.(35|37|39|51)\\.101.*", ".*216\\.239\\.5[0-9]\\.104.*",
                    ".*64\\.233\\.1[0-9]{2}\\.104.*", ".*66\\.102\\.[1-9]\\.104.*", ".*66\\.249\\.93\\.104.*",
                    ".*72\\.14\\.2[0-9]{2}\\.104.*", ".*msn\\..*", ".*live\\.com.*", ".*voila\\..*",
                    ".*mindset\\.research\\.yahoo.*", ".*yahoo\\..*",
                    ".*(66\\.218\\.71\\.225|216\\.109\\.117\\.135|216\\.109\\.125\\.130|66\\.218\\.69\\.11).*",
                    ".*search\\.aol\\.co.*", ".*tiscali\\..*", ".*lycos\\..*", ".*alexa\\.com.*", ".*alltheweb\\.com.*",
                    ".*altavista\\..*", ".*a9\\.com.*", ".*dmoz\\.org.*", ".*netscape\\..*", ".*search\\.terra\\..*",
                    ".*www\\.search\\.com.*", ".*search\\.sli\\.sympatico\\.ca.*", ".*excite\\..*"};

    public static boolean isRobotRequest(String userAgent) {
        if (StringUtils.isEmpty(userAgent)) {
            return true;
        }
        for (String tempStr : ROBOT_SPIDER) {
            if (userAgent.matches(tempStr)) {
                return true;
            }
        }
        for (String tempStr : SEARCH_ENGINES) {
            if (userAgent.matches(tempStr)) {
                return true;
            }
        }
        return false;
    }
}
