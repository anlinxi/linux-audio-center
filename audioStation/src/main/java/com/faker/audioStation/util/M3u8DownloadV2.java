package com.faker.audioStation.util;

import com.faker.audioStation.exception.M3u8Exception;
import com.faker.audioStation.listener.DownloadListener;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * <p>M3u8Download</p>
 *
 * <p>项目名称：haveFunCenter</p>
 *
 * <p>注释:带转码功能</p>
 *
 * <p>Copyright: Copyright Faker(c) 2022/10/7</p>
 *
 * <p>公司: Faker</p>
 *
 * @author 淡梦如烟
 * @version 1.0
 * @date 2022/10/7 7:18
 */
@Slf4j
public class M3u8DownloadV2 {

    //要下载的m3u8链接
    private String downloadUrl;

    //优化内存占用
    private final BlockingQueue<byte[]> BLOCKING_QUEUE = new LinkedBlockingQueue<>();

    //线程数
    private int threadCount = 1;

    //重试次数
    private int retryCount = 30;

    //链接连接超时时间（单位：毫秒）
    private long timeoutMillisecond = 1000L;

    //合并后的文件存储目录
    private String dir;

    //合并前的ts片段临时存储目录
    private String tempDir;

    //合并后的视频文件名称
    private String fileName;

    /**
     * ffmpeg转码工具地址
     */
    private String ffmpegPath;

    //已完成ts片段个数
    private int finishedCount = 0;

    //解密算法名称
    private String method;

    //密钥
    private String key = "";

    //密钥字节
    private byte[] keyBytes = new byte[16];

    //key是否为字节
    private boolean isByte = false;

    //IV
    private String iv = "";

    //所有ts片段下载链接
    private Set<String> tsSet = new LinkedHashSet<>();

    //所有ts片段数量
    private int totalCount;

    //解密后的片段
    private Set<File> finishedFiles = new ConcurrentSkipListSet<>(Comparator.comparingInt(o -> Integer.parseInt(o.getName().replace(".xyz", ""))));

    //已经下载的文件大小
    private BigDecimal downloadBytes = new BigDecimal(0);

    //监听间隔
    private volatile long interval = 0L;

    //监听事件
    private DownloadListener listener = null;

    //当前步骤 1：解析视频地址 2：下载视频 3：下载完成，正在合并视频 4：结束
    private int step = 1;

    /**
     * 开始执行任务
     */
    public void runTask() {
        setThreadCount(30);
        if (tempDir == null) {
            tempDir = dir + "temp";
        }
        finishedCount = 0;
        method = "";
        key = "";
        isByte = false;
        iv = "";
        tsSet.clear();
        finishedFiles.clear();
        downloadBytes = new BigDecimal(0);

        //监视视频地址是否解析完成
        new Thread(() -> {
            while (step == 1) {
                try {
                    Thread.sleep(1000);
                    listener.prepare(step, "正在解析视频地址，请稍后...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            String tsUrl = getTsUrl();
            if (StringUtils.isEmpty(tsUrl)) {
                log.info("不需要解密");
            }
            startDownload();
        }).start();
    }

    /**
     * 下载视频
     */
    private void startDownload() {
        this.step = 2;
        //线程池
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadCount);
        int i = 0;
        //如果生成目录不存在，则创建
        File file1 = new File(tempDir);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        totalCount = tsSet.size();
        //执行多线程下载
        for (String s : tsSet) {
            i++;
            fixedThreadPool.execute(getThread(s, i));
        }
        fixedThreadPool.shutdown();
        startListener(fixedThreadPool);
    }

    private void startListener(ExecutorService fixedThreadPool) {
        new Thread(() -> {
            listener.start(totalCount);

            //轮询是否下载成功
            while (!fixedThreadPool.isTerminated()) {
                try {
                    Thread.sleep(interval);
                    listener.process(downloadUrl, finishedCount, totalCount, new BigDecimal(finishedCount).divide(new BigDecimal(totalCount), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("下载完成，正在合并文件！共" + finishedFiles.size() + "个！" + StringUtils.convertToDownloadSpeed(downloadBytes, 3));
            try {
                //开始合并视频
                File tmpFile1 = new File(tempDir + "/" + fileName + ".ts");
                mergeTs(tmpFile1);
                //开始转码
                transcoding(tmpFile1);
                //删除多余的ts片段
                deleteFiles();
                this.step = 4;
                log.info("视频合并完成，欢迎使用");
                listener.end();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            while (!fixedThreadPool.isTerminated()) {
                try {
                    BigDecimal bigDecimal = new BigDecimal(downloadBytes.toString());
                    Thread.sleep(1000L);
                    listener.speed(StringUtils.convertToDownloadSpeed(new BigDecimal(downloadBytes.toString()).subtract(bigDecimal), 2) + "/s");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 开始转码
     *
     * @param tmpFile1
     */
    private void transcoding(File tmpFile1) throws IOException {
        File smbFile = new File(dir + fileName + ".mp4");
        //ffmpeg -i M:\xxx.mp4 -acodec copy -vcodec copy -f mp4 M:\xxx2.mp4
        String classPath = this.getClass().getResource("/").getPath();
        String ffmpegPathRun = classPath + "ffmpeg\\ffmpeg";
        if (null != this.ffmpegPath && !"".equals(this.ffmpegPath)) {
            ffmpegPathRun = this.ffmpegPath;
        }
        log.info("开始转码:" + tmpFile1.getPath());
        String cmd = ffmpegPathRun + " -y -i " + tmpFile1.getAbsolutePath() + " -acodec copy -vcodec copy -f mp4 " + smbFile.getAbsolutePath();
        log.info("命令:" + cmd);
        Process process = Runtime.getRuntime().exec(cmd);

        try {
            // 通过读取进程的流信息，可以看到视频转码的相关执行信息，并且使得下面的视频转码时间贴近实际的情况
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                log.info(line);
            }
            br.close();
            process.getOutputStream().close();
            process.getInputStream().close();
            process.getErrorStream().close();
            process.waitFor();
            process.destroy();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("转码完毕:" + tmpFile1.getPath());
    }

    /**
     * 合并下载好的ts片段
     */
    private void mergeTs(File file) throws IOException {
        this.step = 3;

        System.gc();
        if (file.exists()) {
            file.delete();
        } else {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] b = new byte[4096];
        int i = 0;
        int error = 0;
        for (File f : finishedFiles) {
            try {
                FileInputStream fileInputStream = new FileInputStream(f);
                int len;
                while ((len = fileInputStream.read(b)) != -1) {
                    fileOutputStream.write(b, 0, len);
                }
                fileInputStream.close();
                fileOutputStream.flush();
                log.info("合并视频[" + fileName + "],已完成[" + i++ + "],共计[" + finishedFiles.size() + "]");
            } catch (IOException e) {
                e.printStackTrace();
                if (error++ > 5) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        fileOutputStream.close();
    }

    /**
     * 删除下载好的片段
     */
    private void deleteFiles() {
        File file = new File(tempDir);
        for (File f : file.listFiles()) {
//            if (f.getName().endsWith(".xy") || f.getName().endsWith(".xyz") || f.getName().endsWith(".tz")) {
//                f.delete();
//            }
            f.delete();
        }
        file.delete();
    }

    /**
     * 开启下载线程
     *
     * @param urls ts片段链接
     * @param i    ts片段序号
     * @return 线程
     */
    private Thread getThread(String urls, int i) {
        return new Thread(() -> {
            int count = 1;
            HttpURLConnection httpURLConnection = null;
            //xy为未解密的ts片段，如果存在，则删除
            File file2 = new File(tempDir + "/" + i + ".xy");
            if (file2.exists()) {
                file2.delete();
            }

            OutputStream outputStream = null;
            InputStream inputStream1 = null;
            FileOutputStream outputStream1 = null;
            byte[] bytes;
            try {
                bytes = BLOCKING_QUEUE.take();
            } catch (InterruptedException e) {
                bytes = new byte[Constants.BYTE_COUNT];
            }
            //重试次数判断
            while (count <= retryCount) {
                try {
                    //模拟http请求获取ts片段文件
                    URL url = new URL(urls);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout((int) timeoutMillisecond);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setReadTimeout((int) timeoutMillisecond);
                    httpURLConnection.setDoInput(true);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    try {
                        outputStream = new FileOutputStream(file2);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        continue;
                    }
                    int len;
                    //将未解密的ts片段写入文件
                    while ((len = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, len);
                        synchronized (this) {
                            downloadBytes = downloadBytes.add(new BigDecimal(len));
                        }
                    }
                    outputStream.flush();
                    inputStream.close();
                    inputStream1 = new FileInputStream(file2);
                    int available = inputStream1.available();
                    if (bytes.length < available) {
                        bytes = new byte[available];
                    }
                    inputStream1.read(bytes);
                    File file = new File(tempDir + "/" + i + ".xyz");
                    outputStream1 = new FileOutputStream(file);
                    //开始解密ts片段，这里我们把ts后缀改为了xyz，改不改都一样
                    byte[] decrypt = decrypt(bytes, available, key, iv, method);
                    if (decrypt == null) {
                        outputStream1.write(bytes, 0, available);
                    } else {
                        outputStream1.write(decrypt);
                    }
                    finishedFiles.add(file);
                    break;
                } catch (Exception e) {
                    if (e instanceof InvalidKeyException || e instanceof InvalidAlgorithmParameterException) {
                        log.error("解密失败！");
                        break;
                    }
                    log.debug("第" + count + "获取链接重试！\t" + urls);
                    count++;
                } finally {
                    try {
                        if (inputStream1 != null) {
                            inputStream1.close();
                        }
                        if (outputStream1 != null) {
                            outputStream1.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        BLOCKING_QUEUE.put(bytes);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
            if (count > retryCount) {
                throw new M3u8Exception("连接超时！");
            }
            finishedCount++;
        });
    }

    /**
     * 获取所有的ts片段下载链接
     *
     * @return 链接是否被加密，null为非加密
     */
    private String getTsUrl() {
        StringBuilder content = getUrlContent(downloadUrl, false);
        //判断是否是m3u8链接
        if (!content.toString().contains("#EXTM3U")) {
            throw new M3u8Exception(downloadUrl + "不是m3u8链接！");
        }

        String[] split = content.toString().split("\\n");
        String keyUrl = "";
        boolean isKey = false;
        for (String s : split) {
            //如果含有此字段，则说明只有一层m3u8链接
            if (s.contains("#EXT-X-KEY") || s.contains("#EXTINF")) {
                isKey = true;
                keyUrl = downloadUrl;
                break;
            }
            //如果含有此字段，则说明ts片段链接需要从第二个m3u8链接获取
            if (s.contains(".m3u8")) {
                if (StringUtils.isUrl(s)) {
                    return s;
                }
                String relativeUrl = downloadUrl.substring(0, downloadUrl.lastIndexOf("/") + 1);
                keyUrl = relativeUrl + s;
                break;
            }
        }
        if (StringUtils.isEmpty(keyUrl)) {
            throw new M3u8Exception("未发现key链接！");
        }

        //获取密钥
        String key1 = isKey ? getKey(keyUrl, content) : getKey(keyUrl, null);
        if (StringUtils.isNotEmpty(key1)) {
            key = key1;
        } else {
            key = null;
        }
        return key;
    }

    /**
     * 获取ts解密的密钥，并把ts片段加入set集合
     *
     * @param url     密钥链接，如果无密钥的m3u8，则此字段可为空
     * @param content 内容，如果有密钥，则此字段可以为空
     * @return ts是否需要解密，null为不解密
     */
    private String getKey(String url, StringBuilder content) {
        StringBuilder urlContent;
        if (content == null || StringUtils.isEmpty(content.toString())) {
            urlContent = getUrlContent(url, false);
        } else {
            urlContent = content;
        }
        if (!urlContent.toString().contains("#EXTM3U")) {
            throw new M3u8Exception(downloadUrl + "不是m3u8链接！");
        }

        String[] split = urlContent.toString().split("\\n");
        for (String s : split) {
            //如果含有此字段，则获取加密算法以及获取密钥的链接
            if (s.contains("EXT-X-KEY")) {
                String[] split1 = s.split(",");
                for (String s1 : split1) {
                    if (s1.contains("METHOD")) {
                        method = s1.split("=", 2)[1];
                        continue;
                    }
                    if (s1.contains("URI")) {
                        key = s1.split("=", 2)[1];
                        continue;
                    }
                    if (s1.contains("IV")) {
                        iv = s1.split("=", 2)[1];
                    }
                }
            }
        }
        String relativeUrl = url.substring(0, url.lastIndexOf("/") + 1);
        //将ts片段链接加入set集合
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("#EXTINF")) {
                String s1 = split[++i];
                tsSet.add(StringUtils.isUrl(s1) ? s1 : relativeUrl + s1);
            }
        }
        if (!StringUtils.isEmpty(key)) {
            key = key.replace("\"", "");
            return getUrlContent(StringUtils.isUrl(key) ? key : relativeUrl + key, true).toString().replaceAll("\\s+", "");
        }
        return null;
    }

    /**
     * 模拟http请求获取内容
     *
     * @param urls  http链接
     * @param isKey 这个url链接是否用于获取key
     * @return 内容
     */
    private StringBuilder getUrlContent(String urls, boolean isKey) {
        int count = 1;
        HttpURLConnection httpURLConnection = null;
        StringBuilder content = new StringBuilder();
        while (count <= retryCount) {
            try {
                URL url = new URL(urls);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout((int) timeoutMillisecond);
                httpURLConnection.setReadTimeout((int) timeoutMillisecond);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
                String line;
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                if (isKey) {
                    byte[] bytes = new byte[128];
                    int len;
                    len = inputStream.read(bytes);
                    isByte = true;
                    if (len == 1 << 4) {
                        keyBytes = Arrays.copyOf(bytes, 16);
                        content.append("isByte");
                    } else {
                        content.append(new String(Arrays.copyOf(bytes, len)));
                    }
                    return content;
                }
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();
                log.info("地址解析成功！");
                break;
            } catch (Exception e) {
                log.debug("第" + count + "获取链接重试！\t" + urls);
                count++;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
        if (count > retryCount) {
            throw new M3u8Exception("连接超时！");
        }
        return content;
    }

    /**
     * 解密ts
     *
     * @param sSrc   ts文件字节数组
     * @param length
     * @param sKey   密钥
     * @return 解密后的字节数组
     */
    private byte[] decrypt(byte[] sSrc, int length, String sKey, String iv, String method) throws Exception {
        if (StringUtils.isNotEmpty(method) && !method.contains("AES")) {
            throw new M3u8Exception("未知的算法！");
        }
        // 判断Key是否正确
        if (StringUtils.isEmpty(sKey)) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16 && !isByte) {
            throw new M3u8Exception("Key长度不是16位！");
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec keySpec = new SecretKeySpec(isByte ? keyBytes : sKey.getBytes(StandardCharsets.UTF_8), "AES");
        byte[] ivByte;
        if (iv.startsWith("0x")) {
            ivByte = StringUtils.hexStringToByteArray(iv.substring(2));
        } else {
            ivByte = iv.getBytes();
        }
        if (ivByte.length != 16) {
            ivByte = new byte[16];
        }
        //如果m3u8有IV标签，那么IvParameterSpec构造函数就把IV标签后的内容转成字节数组传进去
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivByte);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
        return cipher.doFinal(sSrc, 0, length);
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        if (BLOCKING_QUEUE.size() < threadCount) {
            for (int i = BLOCKING_QUEUE.size(); i < threadCount * Constants.FACTOR; i++) {
                try {
                    BLOCKING_QUEUE.put(new byte[Constants.BYTE_COUNT]);
                } catch (InterruptedException ignored) {
                }
            }
        }
        this.threadCount = threadCount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getTimeoutMillisecond() {
        return timeoutMillisecond;
    }

    public void setTimeoutMillisecond(long timeoutMillisecond) {
        this.timeoutMillisecond = timeoutMillisecond;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFinishedCount() {
        return finishedCount;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void addListener(DownloadListener downloadListener) {
        this.listener = downloadListener;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    public void setFfmpegPath(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
    }

}
