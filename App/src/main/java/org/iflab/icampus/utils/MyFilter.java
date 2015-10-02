package org.iflab.icampus.utils;

/**
 * 用来处理字符串的工具类
 * Created at 2015/10/2 13:33.
 */
public class MyFilter {
    /**
     * 替换<br/>为\n
     *
     * @param s 要替换的string
     * @return 替换后的String
     */
    public static String replaceBr(String s) {
        return s.replaceAll("<br>", "\n").replaceAll("<br/>", "\n");
    }

    /**
     * 处理新闻标题
     *
     * @param s 要处理字符串
     * @return 处理后的字符串
     */
    public static String handleNewsTitle(String s) {
        return s.replaceAll("<br/>", "").replaceAll("<br>", "").replaceAll("\\u3000", "");
    }

    /**
     * 处理新闻详情URL
     *
     * @param URL
     * @return
     */
    public static String handleNewsURL(String URL) {
        int a = URL.indexOf(".cn/") + 4;
        int b = URL.indexOf(".xml");
        return URL.substring(a, b);
    }
}
