package com.faker.audioStation.wrapper;

import org.apache.commons.fileupload.FileItem;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * 更改request Parameters中的值
 *
 * @author hxp
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 请求参数
     */
    private Map<String, String[]> params;

    /**
     * 上传文件
     */
    private Map<String, FileItem> fields;

    /**
     * POST的JSON参数
     */
    private String json = null;

    /**
     * 原始请求
     */
    private HttpServletRequest request = null;

    public ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> newParams, String json) {
        super(request);
        this.params = newParams;
        this.json = json;
        this.request = request;
    }

    public ParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> newParams, Map<String, FileItem> fields) {
        super(request);
        this.params = newParams;
        this.fields = fields;
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        Object v = params.get(name);
        if (v == null) {
            return null;
        } else if (v instanceof String[]) {
            String[] strArr = (String[]) v;
            if (strArr.length > 0) {
                return strArr[0];
            } else {
                return null;
            }
        } else if (v instanceof String) {
            return (String) v;
        } else {
            return v.toString();
        }
    }

    @Override
    public Map getParameterMap() {
        return params;
    }

    @Override
    public Enumeration getParameterNames() {
        return new Vector(params.keySet()).elements();
    }

    @Override
    public String[] getParameterValues(String name) {
        Object v = params.get(name);
        if (v == null) {
            return null;
        } else if (v instanceof String[]) {
            return (String[]) v;
        } else if (v instanceof String) {
            return new String[]{(String) v};
        } else {
            return new String[]{v.toString()};
        }
    }

    /**
     * 上传文件流
     */
//    SequenceInputStream sequenceInputStream = null;

//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//
//        Vector<InputStream> streams = new Vector<InputStream>();
//        if (null != fields) {
//            for (FileItem fileItem : fields.values()) {
//                if (null != fileItem) {
//                    streams.add(fileItem.getInputStream());
//                }
//            }
//        }
//        sequenceInputStream = new SequenceInputStream(streams.elements());
//
//        return new ServletInputStream() {
//            @Override
//            public int read() throws IOException {
//                return sequenceInputStream.read();
//            }
//        };
//    }

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletRequestWrapper#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new StringReader(json));
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {
            private InputStream in = new ByteArrayInputStream(
                    json.getBytes(request.getCharacterEncoding()));

            @Override
            public int read() throws IOException {
                return in.read();
            }

            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // TODO Auto-generated method stub

            }
        };
    }
}