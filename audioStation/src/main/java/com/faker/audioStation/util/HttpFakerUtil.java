package com.faker.audioStation.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;

/**
 * @program: myProject
 * @description: java模拟Http请求的工具类
 * @author: 淡梦如烟
 * @create: 2019-07-30 16:38
 */
public class HttpFakerUtil {
    /**
     * 请求类型： GET
     */
    public final static String GET = "GET";
    /**
     * 请求类型： POST
     */
    public final static String POST = "POST";

    /**
     * 模拟请求头
     */
    public final static String[] USER_AGENT = new String[]{
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"
            , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1"
            , "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0"
            , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50"
            , "Opera/9.80 (Windows NT 6.1; U; zh-cn) Presto/2.9.168 Version/11.50"
            , "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; Tablet PC 2.0; .NET4.0E)"
            , "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; GTB7.0)"
            , "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3)"
            , "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
            "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)",
            "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"
    };


    /**
     * 模拟Http Get请求
     *
     * @param urlStr   请求路径
     * @param paramMap 请求参数
     * @return
     * @throws Exception
     */
    public static String get(String urlStr, Map<String, String> paramMap) throws Exception {
        return get(urlStr, paramMap, new HashMap<>(0));
    }

    /**
     * 模拟Http Get请求
     *
     * @param urlStr   请求路径
     * @param paramMap 请求参数
     * @param headers  请求头列表
     * @return
     * @throws Exception
     */
    public static String get(String urlStr, Map<String, String> paramMap, Map<String, String> headers) throws Exception {
        urlStr = urlStr + "?" + getParamString(paramMap);
        HttpURLConnection conn = null;
        try {
            //创建URL对象
            URL url = new URL(urlStr);
            //获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            //设置通用的请求属性
            setHttpUrlConnection(conn, GET);
            //将请求头遍历添加进去
            if (null != headers) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    if (ToolsUtil.isNotNull(header.getKey()) && ToolsUtil.isNotNull(header.getValue())) {
                        conn.setRequestProperty(header.getKey(), header.getValue());
                    }
                }
            }
            //建立实际的连接
            conn.connect();
            //获取响应的内容
            return readResponseContent(conn.getInputStream());
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
    }

    /**
     * 模拟Http Post请求
     *
     * @param urlStr   请求路径
     * @param paramMap 请求参数
     * @return
     * @throws Exception
     */
    public static String post(String urlStr, Map<String, String> paramMap) throws Exception {
        return post(urlStr, paramMap, null);
    }

    /**
     * 模拟Http Post请求
     *
     * @param urlStr   请求路径
     * @param paramMap 请求参数
     * @param headers  请求头列表
     * @return
     * @throws Exception
     */
    public static String post(String urlStr, Map<String, String> paramMap, Map<String, String> headers) throws Exception {
        HttpURLConnection conn = null;
        PrintWriter writer = null;
        try {
            //创建URL对象
            URL url = new URL(urlStr);
            //获取请求参数
            String param = getParamString(paramMap);
            //获取URL连接
            conn = (HttpURLConnection) url.openConnection();
            //设置通用请求属性
            setHttpUrlConnection(conn, POST);
            //将请求头遍历添加进去
            if (null != headers) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    if (ToolsUtil.isNotNull(header.getKey()) && ToolsUtil.isNotNull(header.getValue())) {
                        conn.setRequestProperty(header.getKey(), header.getValue());
                    }
                }
            }

            //建立实际的连接
            conn.connect();
            //将请求参数写入请求字符流中
            writer = new PrintWriter(conn.getOutputStream());
            writer.print(param);
            writer.flush();
            //获取响应头
            Map<String, List<String>> map = conn.getHeaderFields();
            //比较是否是压缩的gzip标记
            List<String> list = map.get("Content-Encoding");
            String ce = null;
            if (list != null && list.size() > 0) {
                ce = list.get(0);
            }
            InputStream in = conn.getInputStream();
            if (null != ce && ce.toLowerCase().contains("gzip")) {
                //读取压缩流响应的内容
                return readResponseZipContent(in);
            } else {
                //读取响应的内容
                return readResponseContent(in);
            }
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
            if (null != writer) {
                writer.close();
            }
        }
    }

    /**
     * 压缩流解析
     *
     * @param in 输入流
     * @return
     * @throws IOException
     */
    private static String readResponseZipContent(InputStream in) throws IOException {
        Reader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            GZIPInputStream gzip = new GZIPInputStream(in);
            reader = new InputStreamReader(gzip, "utf-8");
            char[] buffer = new char[1024];
            int head = 0;
            while ((head = reader.read(buffer)) > 0) {
                content.append(new String(buffer, 0, head));
            }
            return content.toString();
        } finally {
            if (null != in) {
                in.close();
            }
            if (null != reader) {
                reader.close();
            }
        }
    }

    /**
     * 读取响应字节流并将之转为字符串
     *
     * @param in 要读取的字节流
     * @return
     * @throws IOException
     */
    private static String readResponseContent(InputStream in) throws IOException {
        Reader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            reader = new InputStreamReader(in, "utf-8");
            char[] buffer = new char[1024];
            int head = 0;
            while ((head = reader.read(buffer)) > 0) {
                content.append(new String(buffer, 0, head));
            }
            return content.toString();
        } finally {
            if (null != in) {
                in.close();
            }
            if (null != reader) {
                reader.close();
            }
        }
    }

    /**
     * 设置Http连接属性
     *
     * @param conn http连接
     * @return
     * @throws ProtocolException
     * @throws Exception
     */
    private static void setHttpUrlConnection(HttpURLConnection conn, String requestMethod) throws ProtocolException {
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        Random rand = new Random();
        int i = rand.nextInt(USER_AGENT.length);
        String userAgent = USER_AGENT[i];
        conn.setRequestProperty("User-Agent", userAgent);
        conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
        if (null != requestMethod && POST.equals(requestMethod)) {
            conn.setDoOutput(true);
            conn.setDoInput(true);
        }
    }

    /**
     * 随机获取一个请求头
     *
     * @return 请求头
     */
    public static String getUserAgent() {
        Random rand = new Random();
        int i = rand.nextInt(USER_AGENT.length);
        String userAgent = USER_AGENT[i];
        return userAgent;
    }

    /**
     * 将参数转为路径字符串
     *
     * @param paramMap 参数
     * @return 结果
     */
    private static String getParamString(Map<String, String> paramMap) {
        if (null == paramMap || paramMap.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String key : paramMap.keySet()) {
            builder.append("&")
                    .append(key).append("=").append(paramMap.get(key));
        }
        return builder.deleteCharAt(0).toString();
    }

    /**
     * unicode编码转中文
     *
     * @param dataStr unicode编码
     * @return
     */
    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    /**
     * 中文转unicode编码
     *
     * @param gbString 中文
     * @return
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * get请求获取内容
     *
     * @param url       请求地址
     * @param paramMap  参数
     * @param defeValue 默认值
     * @return 返回值
     */
    public static String get(String url, Map<String, String> paramMap, String defeValue) {
        String back = null;
        try {
            back = get(url, paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != back && "".equals(back)) {
            back = defeValue;
        }
        return back;
    }

    public static void main(String[] args) {
        String url = "https://api.zhuishushenqi.com/book/by-categories?gender=male&type=hot&major=%E7%8E%84%E5%B9%BB&minor=&start=1&limit=30&session_token=";
        try {
            System.out.println(get(url, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}