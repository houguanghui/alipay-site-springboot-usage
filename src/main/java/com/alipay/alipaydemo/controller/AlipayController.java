package com.alipay.alipaydemo.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

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

    @GetMapping("notify_url")
    public void callback() {
        System.out.println("callback notify_url");
    }
}
