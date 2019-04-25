package com.zhongjian.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;

import javax.net.ssl.SSLContext;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.*;

/**
 * Http客户端工具类<br/>
 * 这是内部调用类，请不要在外部调用。
 *
 * @author miklchen
 */
public class HttpClientUtil {

    public static final String  SUNX509 = "SunX509";
    public static final String  JKS     = "JKS";
    public static final String  PKCS12  = "PKCS12";
    public static final String  TLS     = "TLS";

    private static final Logger logger  = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 获取不带查询串的url
     *
     * @param strUrl
     * @return String
     */
    public static String getURL(String strUrl) {
        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }
            return strUrl;
        }
        return strUrl;

    }

    /**
     * 获取查询串
     *
     * @param strUrl
     * @return String
     */
    public static String getQueryString(String strUrl) {
        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(indexOf + 1, strUrl.length());
            }
            return "";
        }
        return strUrl;
    }

    /**
     * 查询字符串转换成Map<br/>
     * name1=key1&name2=key2&...
     *
     * @param queryString
     * @return
     */
    public static Map<String, Object> queryString2Map(String queryString) {
        if (null == queryString || "".equals(queryString)) {
            return null;
        }
        Map<String, Object> m = new HashMap<>();
        String[] strArray = queryString.split("&");
        for (int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            HttpClientUtil.putMapByPair(pair, m);
        }
        return m;

    }

    /**
     * 把键值添加至Map<br/>
     * pair:name=value
     *
     * @param pair name=value
     * @param m
     */
    public static void putMapByPair(String pair, Map<String, Object> m) {
        if (null == pair || "".equals(pair)) {
            return;
        }
        int indexOf = pair.indexOf("=");
        if (-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf + 1, pair.length());
            if (null != k && !"".equals(k)) {
                m.put(k, v);
            }
        } else {
            m.put(pair, "");
        }
    }

    /**
     * BufferedReader转换成String<br/>
     * 注意:流关闭需要自行处理
     *
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    /**
     * 处理输出<br/>
     * 注意:流关闭需要自行处理
     *
     * @param out
     * @param data
     * @param len
     * @throws IOException
     */
    public static void doOutput(OutputStream out, byte[] data, int len) throws IOException {
        int dataLen = data.length;
        int off = 0;
        while (off < dataLen) {
            if (len >= dataLen) {
                out.write(data, off, dataLen);
            } else {
                out.write(data, off, len);
            }

            //刷新缓冲区
            out.flush();

            off += len;

            dataLen -= len;
        }

    }

    /**
     * 字符串转换成char数组
     *
     * @param str
     * @return char[]
     */
    public static char[] str2CharArray(String str) {
        if (null == str) {
            return null;
        }

        return str.toCharArray();
    }

    public static InputStream string2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static String getResponseResultByGet(String url) throws Exception {
        String result = null;
        Map<String, String> headerMap = new HashMap<String, String>(2);
        headerMap.put("Content-Type", "text/html;charset=UTF-8");
        result = getResponseResultByGet(url, "UTF-8", headerMap);
        return result;
    }

    // 第一车网数据同步专用，包含了一次失败自动重试，无日志打印
    public static String getResponseResultByGet4Cariauto(String url) throws Exception {
        String result = null;
        Map<String, String> headerMap = new HashMap<String, String>(2);
        headerMap.put("Content-Type", "text/html;charset=UTF-8");
        try {
            result = getResponseResultByGet4Cariauto(url, "UTF-8", headerMap);
        } catch (Exception e) {
            LogUtil.info("HttpClient请求出错,自动重试一遍",e.getMessage());
            result = getResponseResultByGet4Cariauto(url, "UTF-8", headerMap);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public static String getResponseResultByGet(String url, String charset, Map<String, String> headerMap)
            throws Exception {
        String result = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            for (Map.Entry header : headerMap.entrySet()) {
                httpGet.setHeader((String) header.getKey(), (String) header.getValue());
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            logger.info("网络请求状态："+httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    result = new String(EntityUtils.toByteArray(httpEntity), charset);
                }
            }
        } catch (Exception e) {
            logger.error(" HttpClientUtil getResponseResultByGet information error : " + e.fillInStackTrace()
                    + " ; param : url:" + url + " headerMap : " + JSON.toJSONString(headerMap) + " charset : "
                    + charset, e);
            throw new Exception(e);
        } finally {
            httpClient.close();
        }
        return result;
    }

    /**
     * 第一车网数据同步专用http请求方法
     * @param url
     * @param charset
     * @param headerMap
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static String getResponseResultByGet4Cariauto(String url, String charset, Map<String, String> headerMap)
            throws Exception {
        String result = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            for (Map.Entry header : headerMap.entrySet()) {
                httpGet.setHeader((String) header.getKey(), (String) header.getValue());
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
//            logger.info("网络请求状态："+httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    result = new String(EntityUtils.toByteArray(httpEntity), charset);
                }
            }
        } catch (SocketTimeoutException e) {
            throw new Exception(e);
        }catch (Exception e) {
            throw new Exception(e);
        } finally {
            httpClient.close();
        }
        return result;
    }
    @SuppressWarnings("rawtypes")
    public static byte[] getByteArrayByGet(String url, Map<String, String> headerMap)
            throws Exception {
        byte[] result = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            for (Map.Entry header : headerMap.entrySet()) {
                httpGet.setHeader((String) header.getKey(), (String) header.getValue());
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            logger.info("网络请求状态："+httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toByteArray(httpEntity);
                }
            }
        } catch (Exception e) {
            logger.error(" HttpClientUtil getResponseResultByGet information error : " + e.fillInStackTrace()
                    + " ; param : url:" + url + " headerMap : " + JSON.toJSONString(headerMap) , e);
            throw new Exception(e);
        } finally {
            httpClient.close();
        }
        return result;
    }

    /**
     * @param path 地址
     * @param params 参数
     * @param encoding 编码
     * @param requestType 请求方式
     * @return
     * @throws Exception
     */
    public static String getResponseResultForBase(String path, String params, String encoding,
                                                  RequestTypeEnum requestType)
            throws Exception {

        return getResponseResultForBaseWithHander(path,params,encoding,requestType,null);
    }
    /**
     * @param path 地址
     * @param params 参数
     * @param encoding 编码
     * @param requestType 请求方式
     * @return
     * @throws Exception
     */
    public static String getResponseResultForBaseWithHander(String path, String params, String encoding,
                                                  RequestTypeEnum requestType,Map<String,String> handerMap)
            throws Exception {
        String resultJsonArray = null;
        StringEntity entity = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            entity = new StringEntity(params, encoding);
            HttpPost httpPost = new HttpPost(path);
            switch (requestType) {
                case XML:
                    entity.setContentType("application/xml");
                    break;
                case JSON:
                    entity.setContentType("application/json");
                    break;
                default:
            }
            httpPost.setEntity(entity);
            if(!CollectionUtils.isEmpty(handerMap)) {
                Set<String> handerSet = handerMap.keySet();
                for (String handerkey : handerSet) {
                    httpPost.addHeader(handerkey, handerMap.get(handerkey));
                }
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            logger.info("网络请求状态："+httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    resultJsonArray = getResponseResultForStream(httpEntity.getContent());
                }
            }
        } catch (Exception e) {
            logger.error(" HttpClientUtil getResponseResultForJson information error : " + e.fillInStackTrace()
                    + " ; param : path:" + path + " jsonData : " + params + " encoding : " + encoding, e);
            throw new Exception(e);
        } finally {
            httpClient.close();
        }
        return resultJsonArray;
    }


    public static String getResponseResultForBaseWithHander(String path, List<NameValuePair> nameValuePairs , String encoding,
                                                            Map<String,String> handerMap)
            throws Exception {
        String resultJsonArray = null;
        StringEntity entity = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost httpPost = new HttpPost(path);
            StringEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs, "utf-8");
            httpPost.setEntity(entity);
            if(!CollectionUtils.isEmpty(handerMap)) {
                Set<String> handerSet = handerMap.keySet();
                for (String handerkey : handerSet) {
                    httpPost.addHeader(handerkey, handerMap.get(handerkey));
                }
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            logger.info("网络请求状态："+httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    resultJsonArray = getResponseResultForStream(httpEntity.getContent());
                }
            }
        } catch (Exception e) {
            logger.error(" HttpClientUtil getResponseResultForJson information error : " + e.fillInStackTrace()
                    + " ; param : path:" + path + " jsonData : "+ " encoding : " + encoding, e);
            throw new Exception(e);
        } finally {
            httpClient.close();
        }
        return resultJsonArray;
    }
    public static String getResponseResultForJson(String path, String jsonData, String encoding) throws Exception {
        String resultJsonArray = getResponseResultForBase(path, jsonData, encoding, RequestTypeEnum.JSON);
        return resultJsonArray;
    }



    public static String getResponseResultForXml(String path, String xml, String encoding) throws Exception {
        String resultJsonArray = getResponseResultForBase(path, xml, encoding, RequestTypeEnum.XML);
        return resultJsonArray;
    }

    public static String getResponseResultForParam(String path, List<NameValuePair> nameValuePairArrayList,
                                                   String encoding)
            throws Exception {
        String resultJsonArray = null;
        UrlEncodedFormEntity entity = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            entity = new UrlEncodedFormEntity(nameValuePairArrayList, encoding);
            HttpPost httpPost = new HttpPost(path);
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            logger.info("网络请求状态："+httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    resultJsonArray = getResponseResultForStream(httpEntity.getContent());
                }
            }
        } catch (Exception e) {
            logger.error(" HttpClientUtil getResponseResultForParam information error : " + e.fillInStackTrace()
                    + " ; param : path:" + path + " nameValuePairArrayList : "
                    + JSON.toJSONString(nameValuePairArrayList) + " encoding : " + encoding, e);
            throw new Exception(e);
        } finally {
            httpClient.close();
        }
        return resultJsonArray;
    }

    public static String getResponseResultForStream(InputStream input) throws Exception {
        if (null == input) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        try {
            for (String line = null; null != (line = reader.readLine());) {
                builder.append(line).append("\n");
            }
            if (0 == builder.length()) {
                return null;
            }
        } catch (Exception e) {
            logger.error(" HttpClientUtil getResponseResultForStream information error : " + e.fillInStackTrace(), e);
            throw new Exception(e);
        }
        return builder.toString();
    }

    public static CloseableHttpClient getHttpClient() {
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000)
                .setConnectionRequestTimeout(20000).build();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig).build();
        return httpClient;
    }

    public static String getResDataForXmlByHttps(String certFilePath, String keyPasswd, String urlPath, String xml,
                                                 String encoding)
            throws Exception {
        File certFile = new File(certFilePath);
        CloseableHttpClient httpClient = getHttpsClient(certFile, keyPasswd);
        String resultJsonArray = null;
        StringEntity entity = null;
        try {
            entity = new StringEntity(xml, encoding);
            entity.setContentType("application/xml");
            HttpPost httpPost = new HttpPost(urlPath);
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            logger.info("网络请求状态："+httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    resultJsonArray = getResponseResultForStream(httpEntity.getContent());
                }
            }
        } catch (Exception e) {
            logger.error(" HttpClientUtil getResponseResultForParam information error : " + e.fillInStackTrace()
                    + " ; param : urlPath:" + urlPath + " certFilePath : " + certFilePath + " keyPasswd : " + keyPasswd
                    + " encoding : " + encoding, e);
            throw new Exception(e);
        } finally {
            httpClient.close();
        }
        return resultJsonArray;
    }

    /**
     * 获取SSLContext
     *
     * @param keyPasswd
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static CloseableHttpClient getHttpsClient(File certFilePath, String keyPasswd)
            throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException,
            UnrecoverableKeyException, KeyManagementException {
        FileInputStream instream = new FileInputStream(certFilePath);
        KeyStore keyStore = KeyStore.getInstance(PKCS12);
        try {
            keyStore.load(instream, keyPasswd.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, keyPasswd.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.1" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        return httpclient;
    }

    public static String getContentCharSet(HttpEntity entity) throws ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        String charset = null;
        if (entity.getContentType() != null) {
            HeaderElement[] values = entity.getContentType().getElements();
            if (values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }
        if (StringUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }
        return charset;
    }
    public static List<NameValuePair> paramsConverter(Map<String, String> params){
        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        Set<Map.Entry<String, String>> paramsSet= params.entrySet();
        for (Map.Entry<String, String> paramEntry : paramsSet) {
            nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()));
        }
        return nvps;
    }

    public static String genGetParams(Object o) throws Exception{
        StringBuffer strBuffer = new StringBuffer("?");
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!"class".equals(key)) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(o);
                    if (null != value && StringUtil.isNotBlank(String.valueOf(value))) {
                        strBuffer.append(key).append("=").append(value).append("&");
                    }
                }
            }
        }catch(Exception e){
            throw e;
        }
        return strBuffer.substring(0,strBuffer.length()-1).toString() ;
    }
}
