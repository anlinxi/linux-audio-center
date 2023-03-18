package com.faker.audioStation.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.faker.audioStation.model.domain.Music;
import com.faker.audioStation.model.vo.LayuiColVo;
import com.faker.audioStation.wrapper.WrapMapper;
import io.swagger.annotations.ApiModelProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>Title: 常用的工具类 - ToolsUtil</p>
 *
 * <p>Description:常用工具放这儿</p>
 *
 * <p>Copyright: Copyright Faker(c) 2018</p>
 *
 * <p>Company: 无</p>
 *
 * @author Anlinxi
 * @version 1.0
 */
public class ToolsUtil {

    /**
     * 工具类实例化对象
     */
    private static ToolsUtil toolsUtil = null;

    /**
     * 私有化构造函数防止new对象消耗内存
     */
    private ToolsUtil() {

    }

    /**
     * 静态实例化方法获取实例化对象
     * 使用同一个lCsinginUtil，减少内存占用
     *
     * @return LCsinginUtil
     */
    public static ToolsUtil getInstence() {
        if (toolsUtil == null) {
            toolsUtil = new ToolsUtil();
        }
        return toolsUtil;
    }

    /**
     * 日志
     */
    private final static Logger logger = LoggerFactory.getLogger(ToolsUtil.class);

    /**
     * 发送json数据
     *
     * @param response 响应
     * @param object   发送的对象
     */
    public static void WriterJson(HttpServletResponse response, Object object) {
        try {
            String backJson = null;
            if (object instanceof List) {
                backJson = JSONArray.toJSONString(object);
            } else {
                backJson = JSONObject.toJSONString(object);
            }
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("text/json;charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            logger.info("发送json数据>>>>>>>>>>>>>>>>" + backJson);
            printWriter.write(backJson);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            logger.error("发送json数据失败");
            e.printStackTrace();
        }
    }


    /**
     * 判断一个字符串是否为null或空字符，是则返回false，否为true
     *
     * @param str 字符串对象
     * @return 是否为null或空字符
     */
    public static boolean isNotNull(Object str) {
        return !isNullOrEmpty(str);
    }

    /**
     * 判断一个字符串是否为null或空字符，是则返回true，否为false
     *
     * @param str 字符串对象
     * @return 是否为null或空字符
     */
    public static boolean isNullOrEmpty(Object str) {
        String t = trim(str);
        if (t.equals("") || t == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串对象去空
     *
     * @param str 字符串对象
     * @return 替换空
     */
    public static String trim(Object str) {
        return trim(str, "");
    }

    /**
     * 将对象转换为字符串类型
     *
     * @param obj          对象
     * @param nullToString 如果为空就返回该默认值
     * @return 字符串类型
     */
    public static String trim(Object obj, String nullToString) {
        return obj == null ? nullToString : obj.toString().trim();
    }

    /**
     * 创建新文件，如果不存在路径则创建目录
     *
     * @param tempImg
     */
    public static void createNewFile(File tempImg) {
        try {
            if (!tempImg.getParentFile().exists()) {
                tempImg.getParentFile().mkdirs();
            }
            if (!tempImg.exists()) {
                tempImg.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 时间格式化
     */
    public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取字符串
     *
     * @param object 对象
     * @return
     */
    public static String getString(Object object) {
        if (null == object) {
            return "";
        } else {
            if (object instanceof Date) {
                return sdf1.format((Date) object);
            } else {
                return String.valueOf(object);
            }
        }
    }

    /**
     * 读取python文件代码
     *
     * @param fileName 文件名(含后缀)
     * @param path     文件夹路径
     * @return 读取的字符串
     */
    public Boolean download(HttpServletResponse response, String fileName, String path) {
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(path + fileName);
            //通过available方法取得流的最大字符数
            byte[] inOutb = new byte[inStream.available()];
            //读入流,保存在byte数组
            inStream.read(inOutb);
            //设置文件类型
            response.setContentType(Files.probeContentType(Paths.get(path + fileName)));
            //将response提取流
            OutputStream out = response.getOutputStream();
            //将byte数组写入response中
            out.write(inOutb);
            //刷新流
            out.flush();
            out.close();
            inStream.close();
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取系统信息
     */
    Properties prop = System.getProperties();

    /**
     * 获取当前服务器所在系统的路径
     *
     * @return 服务器的路径
     */
    public String classRootPath() {
        //确定jython目录的路径
        String classRootPath = this.getClass().getResource("/").toString();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return classRootPath.replace("file:", "");
        } else {
            return classRootPath.replace("file:/", "");
        }
    }

    /**
     * 获取properties文件配置信息
     *
     * @param fileName 文件名(不含后缀)
     * @param key      取值对应的键名
     * @return 值
     * @throws Exception 异常
     */
    public String getProperties(String fileName, String key) throws Exception {
        //文件名不为空
        if (fileName != null && !"".equals(fileName)) {
            //不需要后缀
            if (fileName.contains(".properties")) {
                fileName.replace(".properties", "");
            }
            if (key != null && !"".equals(key)) {
                //properties文件放置的位置
                String fileAddr = "config/" + fileName;
                ResourceBundle resource = ResourceBundle.getBundle(fileAddr);
                String text = new String(resource.getString(key).getBytes("ISO-8859-1"), "UTF8");
                return text;
            } else {
                throw new Exception("取值键名为空！");
            }
        } else {
            throw new Exception("文件名为空！");
        }
    }

    /**
     * 获取请求里的所有参数并封装到Map里
     *
     * @param request 请求
     * @return 参数Map
     */
    public static Map getAllParameterMap(HttpServletRequest request) {
        Map map = request.getParameterMap();
        Map result = new HashMap();
        Set keSet = map.entrySet();
        for (Iterator itr = keSet.iterator(); itr.hasNext(); ) {
            Map.Entry me = (Map.Entry) itr.next();
            Object ok = me.getKey();
            Object ov = me.getValue();
            String[] value = new String[1];
            if (ov instanceof String[]) {
                value = (String[]) ov;
            } else {
                value[0] = ov.toString();
            }
            for (int k = 0; k < value.length; k++) {
//				System.out.println(ok + "=" + value[k]);
                if (ok != null && !"".equals(ok) && value[k] != null) {
                    result.put(ok, value[k]);
                }
                ;
            }
        }
        return result;
    }

    /**
     * 通过Sigar获取系统名称
     *
     * @return 系统名称
     */
    public static String getSystemName() {
        String sysName = System.getProperty("os.name");
        logger.info("获取到系统名称[" + sysName + "]");
        return sysName;
    }

    /**
     * 获取对象的属性
     *
     * @param c
     * @return
     */
    public static List<LayuiColVo> getApiModelProperty(Class<?> c) {
        List<LayuiColVo> layuiColVoList = new ArrayList<LayuiColVo>();
        try {
            // 2.获取类的属性
            Field[] declaredFields = c.getDeclaredFields();
            // 3.遍历属性，获取属性上ApiModelProperty的值，属性的名，存入Properties
            if (declaredFields.length != 0) {
                for (Field field : declaredFields) {
                    //序列化属性去掉
                    if ("serialVersionUID".equals(field.getName())) {
                        continue;
                    }
                    LayuiColVo layuiColVo = new LayuiColVo();
                    layuiColVo.setField(field.getName());
                    if (field.getAnnotation(ApiModelProperty.class) != null) {
                        layuiColVo.setTitle(field.getAnnotation(ApiModelProperty.class).value());
                    }
                    if (field.getAnnotation(TableId.class) != null) {
                        layuiColVo.setTableField(field.getAnnotation(TableId.class).value());
                        layuiColVo.setPk(true);
                        layuiColVo.setWidth(80);
                    }
                    if (field.getAnnotation(TableField.class) != null) {
                        layuiColVo.setTableField(field.getAnnotation(TableField.class).value());
                        layuiColVo.setWidth(150);
                    }
                    layuiColVoList.add(layuiColVo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layuiColVoList;
    }

    /**
     * 根据属性获取表字段
     *
     * @param c
     * @return
     */
    public static String getTableField(Class<?> c, String name) {
        try {
            // 2.获取类的属性
            Field[] declaredFields = c.getDeclaredFields();
            // 3.遍历属性，获取属性上ApiModelProperty的值，属性的名，存入Properties
            if (declaredFields.length != 0) {
                for (Field field : declaredFields) {
                    //序列化属性去掉
                    if ("serialVersionUID".equals(field.getName())) {
                        continue;
                    }
                    if (field.getAnnotation(TableId.class) != null) {
                        if (field.getName().equals(name)) {
                            return field.getAnnotation(TableId.class).value();
                        }
                    }
                    if (field.getAnnotation(TableField.class) != null) {
                        if (field.getName().equals(name)) {
                            return field.getAnnotation(TableField.class).value();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件名不包含以下任何字符：
     * “（双引号）、*（星号）、<（小于）、>（大于）、？（问号）、\（反斜杠）、/（正斜杠）、|（竖线）、：（冒号）
     *
     * @param name
     * @return
     */
    public static String getFileName(String name) {
        return name.replaceAll("\"", "").replaceAll("\\*", "").replaceAll("\\<", "").replaceAll("\\>", "")
                .replaceAll("\\?", "").replaceAll("\\\\", "").replaceAll("/", "").replaceAll("|", "")
                .replaceAll(":", "");
    }

    /**
     * 转换文件名
     *
     * @param music
     * @return
     */
    public static String getFileName(Music music) {
        return getFileName(music.getArtist() + " - " + music.getTitle());
    }

    /**
     * 设置返回码状态
     *
     * @param response 响应
     * @param code     响应编码
     * @param msg      响应信息
     */
    public static void setStateInfo(HttpServletResponse response, int code, String msg) {
        try {
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(code);
            OutputStream out = response.getOutputStream();
            out.write(JSONObject.toJSONString(WrapMapper.wrap(code, msg)).getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件到响应流
     *
     * @param response
     * @param file
     */
    public static void downloadFile(HttpServletResponse response, File file) {
        OutputStream toClient = null;

        try {
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != toClient) {
                try {
                    toClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析url的查询参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> parseUrlQuery(String url) {
        Map<String, String> entity = new HashMap<>();
        if (url == null) {
            return entity;
        }
        url = url.trim();
        if (url.equals("")) {
            return entity;
        }
        String[] urlParts = url.split("\\?");
        //没有参数
        if (urlParts.length == 1) {
            return entity;
        }
        //有参数
        String[] params = urlParts[1].split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            entity.put(keyValue[0], keyValue[1]);
        }
        return entity;
    }
}
