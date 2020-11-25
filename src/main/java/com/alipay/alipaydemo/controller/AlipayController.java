package com.alipay.alipaydemo.controller;

import com.alipay.alipaydemo.config.AlipayConfig;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.models.AlipayTradeCloseResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class AlipayController {


    @Value("${com.alipay.return-url}")
    private String returnUrl;

    @PostMapping("alipay.trade.page.pay.action")
    @ResponseBody
    public String pay(String WIDsubject, String WIDout_trade_no, String WIDtotal_amount, String WIDbody, HttpServletResponse httpServletResponse) throws Exception {

        AlipayTradePagePayResponse response = Factory.Payment.Page()
                .optional("body",WIDbody)
                .optional("timeout_express","10m")
                .optional("product_code","FAST_INSTANT_TRADE_PAY")
                .pay(WIDsubject,
                WIDout_trade_no, WIDtotal_amount, returnUrl);
        httpServletResponse.setContentType("text/html");
        System.out.println(response.getBody());
        return response.getBody();
    }

    @PostMapping("alipay.trade.query.action")
    @ResponseBody
    public String query(String WIDTQout_trade_no,String WIDTQtrade_no) throws Exception {
//        AlipayTradeFastpayRefundQueryResponse response = Factory.Payment.Common().queryRefund(WIDTQout_trade_no, WIDTQtrade_no);
        AlipayTradeQueryResponse response = Factory.Payment.Common().query(WIDTQout_trade_no);
        return response.httpBody;
    }


    @PostMapping("alipay.trade.refund.action")
    @ResponseBody
    public String refund(String WIDTRout_trade_no,String WIDTRtrade_no,String WIDTRrefund_amount,String WIDTRrefund_reason,String WIDTRout_request_no) throws Exception {
        AlipayTradeRefundResponse response = Factory.Payment.Common().refund(WIDTRout_trade_no, WIDTRrefund_amount);
        return response.getHttpBody();
    }

    @PostMapping("alipay.trade.fastpay.refund.query.action")
    @ResponseBody
    public String refundQuery(String WIDRQout_trade_no,String WIDRQtrade_no,String WIDRQout_request_no) throws Exception {
        AlipayTradeFastpayRefundQueryResponse response = Factory.Payment.Common().queryRefund(WIDRQout_trade_no, WIDRQout_request_no);
        return response.getHttpBody();
    }

    @PostMapping("alipay.trade.close.action")
    @ResponseBody
    public String close(String WIDTCout_trade_no,String WIDTCtrade_no) throws Exception {
        AlipayTradeCloseResponse response = Factory.Payment.Common().close(WIDTCout_trade_no);
        return response.getHttpBody();
    }

    @GetMapping("return_url")
    public String returnPage(String out_trade_no,String total_amount,String trade_no,String sign,String timestamp,ModelMap model) {
        model.addAttribute("tradeNo", trade_no);
        model.addAttribute("outTradeNo", out_trade_no);
        model.addAttribute("totalAmount", total_amount);
        return "return_url";
    }

    @RequestMapping("notify_url")
    public void callback(HttpServletRequest request) throws Exception {
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params,AlipayConfig.alipay_public_key,AlipayConfig.charset,AlipayConfig.sign_type);
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(params.get("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(params.get("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易状态
            String trade_status = new String(params.get("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }
            System.out.println("callback notify_url");
        }
    }
}
