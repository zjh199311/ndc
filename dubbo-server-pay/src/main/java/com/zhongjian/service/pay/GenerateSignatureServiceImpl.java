package com.zhongjian.service.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.zhongjian.commoncomponent.PropUtil;
import com.zhongjian.dto.common.ResultDTO;
import com.zhongjian.dto.common.ResultUtil;
import com.zhongjian.util.LogUtil;
import com.zhongjian.util.PayCommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

@Service
public class GenerateSignatureServiceImpl implements GenerateSignatureService {

    @Resource
    private PropUtil propUtil;

    @Override
    public String getAliSignature(String business, String orderId) {
        String out_trade_no = "";
        String orderTypeString = "";
        String totalAmount = "";
        //普通订单
        if (business.equals("RO")) {
            orderTypeString = "订单总价";
            Map<String, String> result = getOutRradeNoAndAmount(orderId);
            out_trade_no = result.get("out_trade_no");
            totalAmount = result.get("totalAmount");
        } else {
            return null;
        }
        //生成签名
        Map<String, String> orderMap = new LinkedHashMap<String, String>(); // 订单实体
        /****** 2.商品参数封装开始 *****/ // 手机端用
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        orderMap.put("out_trade_no", out_trade_no);
        orderMap.put("subject", orderTypeString);
        // 付款金额，必填
        orderMap.put("total_amount", totalAmount);
        // 商品描述，可空
        orderMap.put("body", "本次订单花费" + totalAmount + "元");
        // 超时时间 可空
        orderMap.put("timeout_express", "15m");
        // 销售产品码 必填
        orderMap.put("product_code", "QUICK_MSECURITY_PAY");
        // 实例化客户端
        AlipayClient client = new DefaultAlipayClient(propUtil.getAliUrl(), propUtil.getAliAppId(),
                propUtil.getAliRSAPrivateKey(), propUtil.getAliFormat(), propUtil.getAliCharset(), propUtil.getAliPayPublicKey(),
                propUtil.getAliSignType());
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest ali_request = new AlipayTradeAppPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setPassbackParams(URLEncoder.encode((String) orderMap.get("body").toString()));
        // 描述信息 添加附加数据
        model.setBody(orderMap.get("body")); // 商品信息
        model.setSubject(orderMap.get("subject")); // 商品名称
        model.setOutTradeNo(orderMap.get("out_trade_no")); // 商户订单号(自动生成)
        model.setTimeoutExpress(orderMap.get("timeout_express")); // 交易超时时间
        model.setTotalAmount(orderMap.get("total_amount")); // 支付金额
        model.setProductCode(orderMap.get("product_code")); // 销售产品码
        ali_request.setBizModel(model);
        ali_request.setNotifyUrl(propUtil.getAliNotifyUrl()); // 回调地址
        AlipayTradeAppPayResponse response = null;
        String orderString = null;
        try {
            response = client.sdkExecute(ali_request);
            orderString = response.getBody();
        } catch (AlipayApiException e) {
            return null;
        }
        return orderString;
    }

    @Override
    public ResultDTO<Object> getWxAppSignature(String business, String orderId, String spbillCreateIp) {
        String out_trade_no = "";
        String totalAmount = "";
        //普通订单
        if (business.equals("RO")) {
            Map<String, String> result = getOutRradeNoAndAmount(orderId);
            out_trade_no = result.get("out_trade_no");
            totalAmount = result.get("totalAmount");
        } else {
            return null;
        }
        SortedMap<String, Object> stringObjectSortedMap = null;
        try {
            stringObjectSortedMap = PayCommonUtil.wxPublicPay(out_trade_no, totalAmount, spbillCreateIp, propUtil.getWxAppAppId(), propUtil.getWxAppKey(), propUtil.getWxAppMchId(), propUtil.getWxAppNotifyUrl(), propUtil.getWxAppUrl(),"倪的菜商品订单支付");
        } catch (Exception e) {
            LogUtil.info("获取签名异常", "e:" + e.getMessage());
            e.getMessage();
        }
        return ResultUtil.getSuccess(stringObjectSortedMap);
    }

    @Transactional
    private Map<String, String> getOutRradeNoAndAmount(String orderId) {
        //生成out_trade_no
        return null;

    }
}
