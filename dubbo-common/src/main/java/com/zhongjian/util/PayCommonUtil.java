package com.zhongjian.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Author: ldd
 */
public class PayCommonUtil {

    //随机字符串生成
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 微信公众号支付
     *
     * @param tradeNo    订单号
     * @param totalPrice 支付金额
     * @return -
     * @Param type 0微信支付 1小程序支付 2.银行微信支付
     */
    public static SortedMap<String, String> wxPublicPay(String tradeNo, String totalPrice, String spbillCreateId, String wxAppAppletsId, String wxAppAppId, String wxAppKey, String WxAppletsKey, String wxAppMchId, String wxAppNotifyUrl, String wxAppletsNotifyUrl, String wxAppUrl, String body, String openid, Integer type) throws Exception {
        Map<String, String> map = weixinAppPrePay(tradeNo, totalPrice, spbillCreateId, wxAppAppId, wxAppAppletsId, wxAppMchId, wxAppNotifyUrl, wxAppletsNotifyUrl, wxAppUrl, wxAppKey, WxAppletsKey, body, type, openid);
        SortedMap<String, String> finalpackage = new TreeMap<>();
        if (0 == type) {
            finalpackage.put("appid", wxAppAppId);
            finalpackage.put("partnerid", wxAppMchId);
            finalpackage.put("prepayid", map.get("prepay_id"));
            finalpackage.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            finalpackage.put("noncestr", getRandomString(32));
            finalpackage.put("package", "Sign=WXPay");
            String sign = PayCommonUtil.createSign("UTF-8", finalpackage, wxAppKey);
            finalpackage.put("sign", sign);
        } else if (1 == type) {
            finalpackage.put("appId", wxAppAppletsId);
            finalpackage.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            finalpackage.put("nonceStr", getRandomString(32));
            finalpackage.put("package", "prepay_id=" + map.get("prepay_id"));
            finalpackage.put("signType", "MD5");
            String sign = PayCommonUtil.createSign("UTF-8", finalpackage, WxAppletsKey);
            finalpackage.put("paySign", sign);
        }
        return finalpackage;
    }

    /**
     * @param outTradeNo
     * @param totalPrice
     * @param spbillCreateId
     * @return
     */
    public static Map<String, String> weixinAppPrePay(String outTradeNo, String totalPrice, String spbillCreateId,
                                                      String wxAppAppId, String wxAppAppletsId, String wxAppMchId, String wxAppNotifyUrl, String wxAppletsNotifyUrl, String wxAppUrl, String wxAppKey, String wxAppLetsKey, String body, Integer type, String openid) throws Exception {
        SortedMap<String, String> parameterMap = new TreeMap<String, String>();
        //应用ID
        if (0 == type) {
            parameterMap.put("appid", wxAppAppId);
        } else if (1 == type) {
            parameterMap.put("appid", wxAppAppletsId);
        }
        //商户号
        parameterMap.put("mch_id", wxAppMchId);
        //随机字符串
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));
        //商品描述
        parameterMap.put("body", body);
        //商户订单号
        parameterMap.put("out_trade_no", outTradeNo);
        BigDecimal total = new BigDecimal(totalPrice).multiply(new BigDecimal(100));
        DecimalFormat df = new DecimalFormat("0");
        //总金额
        parameterMap.put("total_fee", df.format(total));
        //终端IP
        parameterMap.put("spbill_create_ip", spbillCreateId);
        //交易类型 type = 0 为微信, type=1为小程序
        String sign = null;
        if (0 == type) {
            //通知地址
            parameterMap.put("notify_url", wxAppletsNotifyUrl);
            parameterMap.put("trade_type", "APP");
            sign = PayCommonUtil.createSign("UTF-8", parameterMap, wxAppKey);
        } else if (1 == type) {
            //通知地址
            parameterMap.put("notify_url", wxAppNotifyUrl);
            parameterMap.put("trade_type", "JSAPI");
        }
        if (parameterMap.get("trade_type").equals("JSAPI")) {
            parameterMap.put("openid", openid);
            sign = PayCommonUtil.createSign("UTF-8", parameterMap, wxAppLetsKey);
        }
        //签名
        parameterMap.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        String result = PayCommonUtil.httpsRequest(
                wxAppUrl, "POST",
                requestXML);
        Map<String, String> map = null;
        map = PayCommonUtil.xmlToMap(result);
        return map;
    }

    //请求方法
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuilder buffer = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}" + ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}" + e);
        }
        return null;
    }

    /**
     * 过滤参数
     *
     * @param sArray
     * @return
     * @author
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>(sArray.size());
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    //请求xml组装
    public static String getRequestXml(SortedMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)) {
                sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
            } else {
                sb.append("<" + key + ">" + value + "</" + key + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * @param payParams
     * @return
     * @author
     */
    public static void buildPayParams(StringBuilder sb, Map<String, String> payParams, boolean encoding) {
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            sb.append(key).append("=");
            if (encoding) {
                sb.append(urlEncode(payParams.get(key)));
            } else {
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        }
    }

    //生成签名
    public static String createSign(String characterEncoding, SortedMap<String, String> parameters, String wxAppKey) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + wxAppKey);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }


    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }

    public static String inputStream2String(InputStream in, String encoding) throws Exception {
        StringBuffer out = new StringBuffer();
        InputStreamReader inread = new InputStreamReader(in, encoding);

        char[] b = new char[4096];
        for (int n; (n = inread.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }


    //请求时根据不同签名方式去生成不同的sign
    public static String getSign(String signType, String preStr,String mchPrivateKey) {
        if ("RSA_1_256".equals(signType)) {
            try {
                return sign(preStr, "RSA_1_256",mchPrivateKey);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    //preStr,"RSA_1_256","mchPrivateKey==??????????#测试RSA签名私钥，商户需改为自己的"
    public static String sign(String preStr, String signType, String mchPrivateKey) throws Exception {
        String suite = "SHA256WithRSA";
        byte[] signBuf = sign(suite, preStr.getBytes("UTF8"),
                mchPrivateKey);
        return new String(Base64.encodeBase64(signBuf), "UTF8");
    }

    public static byte[] sign(String suite, byte[] msgBuf, String privateKeyStr) {
        Signature signature = null;
        try {
            signature = Signature.getInstance(suite);
        } catch (Exception e) {
            // 上线运行时套件一定存在
            // 异常不往外抛
        }
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
            signature.initSign(privateKey);
        } catch (Exception e) {
            //logger.warn("解析私钥失败：{}", e.getMessage());
            throw new RuntimeException("INVALID_PRIKEY");
        }
        try {
            signature.update(msgBuf);
            return signature.sign();
        } catch (SignatureException e) {
            // 一般不会出现
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


}
