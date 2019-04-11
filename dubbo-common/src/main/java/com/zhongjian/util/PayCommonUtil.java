package com.zhongjian.util;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
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
     * @param trade_no    订单号
     * @param totalAmount 支付金额
     * @return -
     */
    public static SortedMap<String, Object> wxPublicPay(String trade_no, String totalAmount, String spbillCreateId,String wxAppAppId,String wxAppKey,String wxAppMchId,String wxAppNotifyUrl,String wxAppUrl) throws Exception {
        Map<String, String> map = weixinAppPrePay(trade_no, totalAmount, spbillCreateId,wxAppAppId,wxAppMchId,wxAppNotifyUrl,wxAppUrl,wxAppKey);
        SortedMap<String, Object> finalpackage = new TreeMap<>();
        finalpackage.put("appid", wxAppAppId);
        finalpackage.put("partnerid", wxAppMchId);
        finalpackage.put("prepayid", map.get("prepay_id"));
        finalpackage.put("timeStamp", System.currentTimeMillis() / 1000);
        finalpackage.put("nonceStr", getRandomString(32));
        finalpackage.put("package", "Sign=WXPay");
        String sign = PayCommonUtil.createSign("UTF-8", finalpackage,wxAppKey);
        finalpackage.put("sign", sign);
        return finalpackage;
    }

    /**
     * @param outTradeNo
     * @param totalAmount
     * @param spbillCreateId
     * @return
     */
    public static Map<String, String> weixinAppPrePay(String outTradeNo, String totalAmount, String spbillCreateId,
    		String wxAppAppId,String wxAppMchId,String wxAppNotifyUrl,String wxAppUrl,String wxAppKey) throws  Exception {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //应用ID
        parameterMap.put("appid", wxAppAppId);
        //商户号
        parameterMap.put("mch_id", wxAppMchId);
        //随机字符串
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));
        //商品描述
        parameterMap.put("body", "倪的菜商品订单支付");
        //商户订单号
        parameterMap.put("out_trade_no", outTradeNo);
        BigDecimal total = new BigDecimal(totalAmount).multiply(new BigDecimal(100));
        DecimalFormat df = new DecimalFormat("0");
        //总金额
        parameterMap.put("total_fee", df.format(total));
        //终端IP
        parameterMap.put("spbill_create_ip", spbillCreateId);
        //通知地址
        parameterMap.put("notify_url",wxAppNotifyUrl);
        //交易类型
        parameterMap.put("trade_type", "APP");
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap,wxAppKey);
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

    //请求xml组装
    public static String getRequestXml(SortedMap<String, Object> parameters) {
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

    //生成签名
    public static String createSign(String characterEncoding, SortedMap<String, Object> parameters,String wxAppKey) {
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

    

}
