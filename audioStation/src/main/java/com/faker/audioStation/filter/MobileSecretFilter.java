package com.faker.audioStation.filter;


import com.faker.audioStation.util.DesUtils;
import com.faker.audioStation.util.QEncodeUtil;
import com.faker.audioStation.util.RsaUtils;
import com.faker.audioStation.util.ToolsUtil;
import com.faker.audioStation.wrapper.AesWrapMapper;
import com.faker.audioStation.wrapper.DesWrapMapper;
import com.faker.audioStation.wrapper.ParameterRequestWrapper;
import com.faker.audioStation.wrapper.RsaWrapMapper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 移动端参数解密过滤器
 *
 * @author ThinkGem
 * @version 2020-4-13
 */
public class MobileSecretFilter implements Filter {

    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(MobileSecretFilter.class);

    public MobileSecretFilter() {
//        logger.info("初始化移动端权限过滤器[" + stringRedisTemplate + "]--------------------------------------------------------------");
    }


    /**
     * 初始化
     * spring容器初始化bean对象的顺序是listener-->filter-->servlet
     * 所以需要spring需在这里手动注入
     *
     * @param filterConfig 过滤器设置
     * @throws ServletException servlet异常
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 执行过滤方法
     *
     * @param servletRequest  servlet请求
     * @param servletResponse servlet响应
     * @param filterChain     过滤器设置
     * @throws IOException      读写异常
     * @throws ServletException servlet异常
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("[" + this.getClass().getName() + "]执行过滤方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if ("POST".equals(request.getMethod())) {
            String requestBody = this.getRequestBody((HttpServletRequest) request);
            JSONObject jsonBody = JSONObject.fromObject(requestBody);
            String secretType = jsonBody.optString("secretType");
            if (ToolsUtil.isNotNull(secretType)) {
                String uri = request.getRequestURI();
                //todo aes 加密
                if (AesWrapMapper.ENCODE_TYPE.equalsIgnoreCase(secretType)) {
                    String aes = jsonBody.optString("secretData");
                    logger.debug("接收到加密内容：" + aes);
                    try {
                        String jsonStr = null;
                        JSONObject jsonObject = null;
                        synchronized (this) {
                            jsonStr = QEncodeUtil.aesDecrypt(aes, "abcdefgabcdefg12");
                            jsonObject = JSONObject.fromObject(jsonStr);
                            logger.info("app请求[" + uri + "]密参数:" + jsonObject.toString());
                        }
                        request = this.getNewHttpServletRequest(request, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //todo des加密
                if (DesWrapMapper.ENCODE_TYPE.equalsIgnoreCase(secretType)) {
                    String des = jsonBody.optString("secretData");
                    logger.debug("接收到加密内容：" + des);
                    try {
                        String jsonStr = null;
                        JSONObject jsonObject = null;
                        synchronized (this) {
                            jsonStr = DesUtils.decode(des, "tx,anlinxi,top");
                            jsonObject = JSONObject.fromObject(jsonStr);
                            logger.info("app请求[" + uri + "]密参数:" + jsonObject.toString());
                        }
                        request = this.getNewHttpServletRequest(request, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //todo rsa加密
                if (RsaWrapMapper.ENCODE_TYPE.equalsIgnoreCase(secretType)) {
                    String rsa = jsonBody.optString("secretData");
                    logger.debug("接收到加密内容：" + rsa);
                    try {
                        String jsonStr = null;
                        JSONObject jsonObject = null;
                        synchronized (this) {
                            jsonStr = RsaUtils.decryptPub(rsa, RsaUtils.PUBLIC_KEY);
                            jsonObject = JSONObject.fromObject(jsonStr);
                            logger.info("app请求[" + uri + "]密参数:" + jsonObject.toString());
                        }
                        request = this.getNewHttpServletRequest(request, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                try {
                    request = this.getNewHttpServletRequest(request, jsonBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            //一旦获取了值。strus的表单就会中文乱码...
            String secretType = request.getParameter("secretType");
            if (ToolsUtil.isNotNull(secretType)) {
                String uri = request.getRequestURI();
                //todo aes 加密
                if (AesWrapMapper.ENCODE_TYPE.equalsIgnoreCase(secretType)) {
                    String aes = request.getParameter("secretData");
                    logger.debug("接收到加密内容：" + aes);
                    try {
                        String jsonStr = null;
                        JSONObject jsonObject = null;
                        synchronized (this) {
                            jsonStr = QEncodeUtil.aesDecrypt(aes, "abcdefgabcdefg12");
                            jsonObject = JSONObject.fromObject(jsonStr);
                            logger.info("app请求[" + uri + "]密参数:" + jsonObject.toString());
                        }
                        request = this.getNewHttpServletRequest(request, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //todo des加密
                if (DesWrapMapper.ENCODE_TYPE.equalsIgnoreCase(secretType)) {
                    String des = request.getParameter("secretData");
                    logger.debug("接收到加密内容：" + des);
                    try {
                        String jsonStr = null;
                        JSONObject jsonObject = null;
                        synchronized (this) {
                            jsonStr = DesUtils.decode(des, "tx,anlinxi,top");
                            jsonObject = JSONObject.fromObject(jsonStr);
                            logger.info("app请求[" + uri + "]密参数:" + jsonObject.toString());
                        }
                        request = this.getNewHttpServletRequest(request, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //todo rsa加密
                if (RsaWrapMapper.ENCODE_TYPE.equalsIgnoreCase(secretType)) {
                    String rsa = request.getParameter("secretData");
                    logger.debug("接收到加密内容：" + rsa);
                    try {
                        String jsonStr = null;
                        JSONObject jsonObject = null;
                        synchronized (this) {
                            jsonStr = RsaUtils.decryptPub(rsa, RsaUtils.PUBLIC_KEY);
                            jsonObject = JSONObject.fromObject(jsonStr);
                            logger.info("app请求[" + uri + "]密参数:" + jsonObject.toString());
                        }
                        request = this.getNewHttpServletRequest(request, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                //todo 老的未加密的情况 重写复写
                HashMap<String, String[]> map = new HashMap<String, String[]>();
                request.setCharacterEncoding("UTF-8");
                Enumeration<String> parms = request.getParameterNames();
                while (parms.hasMoreElements()) {
                    String parameterName = (String) parms.nextElement();// 参数名称
                    if (!"secretType".equals(parameterName)) {
                        String[] parameterValue = request.getParameterValues(parameterName);
                        String[] parameterValueNew = new String[parameterValue.length];
                        for (int i = 0; i < parameterValue.length; i++) {
                            parameterValueNew[i] = new String(parameterValue[i].getBytes("iso-8859-1"), "UTF-8");
                        }
                        map.put(parameterName, parameterValueNew);
                    }
                }
                request = new ParameterRequestWrapper((HttpServletRequest) request, map, JSONObject.fromObject(map).toString());
            }
        }

        filterChain.doFilter(request, servletResponse);
    }

    /**
     * 回复重新登录的信息
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws IOException
     */
    private void toLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        //认证过了
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        OutputStream out = httpServletResponse.getOutputStream();
        out.write("{\"result\":\"login\",\"message\":\"请重新登录!\",\"errorCode\":-10000}".getBytes("UTF-8"));
    }


    /**
     * 通过解密参数获取一个新的HttpServletRequest对象
     *
     * @param request    老HttpServletRequest对象
     * @param jsonObject 解密参数
     * @return 新HttpServletRequest对象
     * @throws Exception
     */
    private HttpServletRequest getNewHttpServletRequest(HttpServletRequest request, JSONObject jsonObject) throws Exception {
        HashMap<String, String[]> map = new HashMap<String, String[]>();
        Iterator iter = jsonObject.entrySet().iterator();
        //校验参数
        String userId = null;
        String loginToken = null;
        String method = null;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            Object object = entry.getValue();
            if (null != object) {
                if (object instanceof String) {
                    String text = (String) object;
                    map.put(key, new String[]{text});
                    if ("userId".equals(key)) {
                        userId = text;
                    } else if ("loginToken".equals(key)) {
                        loginToken = text;
                    }
                } else if (object instanceof List) {
                    List list = (List) object;
                    String[] parameterValue = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        parameterValue[i] = null == object ? "" : String.valueOf(object);
                    }
                    map.put(key, parameterValue);
                } else {
                    //什么int date类型都强转成string了 时间类型可以单独转换
                    map.put(key, new String[]{String.valueOf(object)});
                }
            }
        }
        request.setCharacterEncoding("UTF-8");
        Enumeration<String> parms = request.getParameterNames();
        while (parms.hasMoreElements()) {
            String parameterName = (String) parms.nextElement();// 参数名称
            if (!"secretType".equals(parameterName)) {
                String[] parameterValue = request.getParameterValues(parameterName);
                String[] parameterValueNew = new String[parameterValue.length];
                for (int i = 0; i < parameterValue.length; i++) {
                    parameterValueNew[i] = new String(parameterValue[i].getBytes("iso-8859-1"), "UTF-8");
                }
                map.put(parameterName, parameterValueNew);
            }
        }
//        logger.debug("所有参数:" + JSONObject.fromObject(map).toString());
        request = new ParameterRequestWrapper((HttpServletRequest) request, map, jsonObject.toString());
        return request;
    }

    /**
     * 获取POST请求体
     *
     * @param req
     * @return
     */
    private String getRequestBody(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            return json;
        } catch (IOException e) {
            logger.info("请求体读取失败", e);
        }
        return "";
    }
}
