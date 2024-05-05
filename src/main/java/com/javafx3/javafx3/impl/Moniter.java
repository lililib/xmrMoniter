package com.javafx3.javafx3.impl;

import cn.hutool.json.JSONUtil;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import com.binance.connector.futures.client.utils.ProxyAuth;
import com.javafx3.javafx3.HelloController;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * Hello world!
 */
public class Moniter {

    public static volatile boolean havaShow = false;

    private HelloController mainController;

    private volatile boolean monitoring;
    private Thread monitorThread;

    private String symbol;
    private String price;
    private String timeParam;

    public Moniter(HelloController mainController) {
        this.mainController = mainController;
        this.symbol = mainController.getToken().getText();
        this.price = mainController.getPrice().getText();
      //  this.timeParam = mainController.getTimeParam().getText();
    }

    public void start() {
        String apiKey = "p8LgRALO4jf85z9UqohFz";
        String secretKey = "j7fmW4F8VzJQCsARl";
        UMFuturesClientImpl client = new UMFuturesClientImpl(apiKey, secretKey);
        Proxy proxyConn = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809));
        ProxyAuth proxy = new ProxyAuth(proxyConn, null);
        client.setProxy(proxy);
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("symbol", symbol);


        monitorThread = new Thread(() -> {
            monitoring = true;
            while (monitoring) {
                String bookTickerStr = client.market().bookTicker(parameters);
                BigDecimal bidPrice = new BigDecimal((String) JSONUtil.parseObj(bookTickerStr).get("bidPrice"));
                mainController.setLog("bidPrice:" + bidPrice);
                if (bidPrice.compareTo(new BigDecimal(price.split("-")[0])) <= 0 || bidPrice.compareTo(new BigDecimal(price.split("-")[1])) >= 0) {
                    mainController.setLog("抄底抄底！目前价格:" + bidPrice);
                    Random random = new Random();
                    int i = random.nextInt(100);
                    if(i!=0 && i%2==0){
                        java.awt.Toolkit.getDefaultToolkit().beep();
                    }
                    if (havaShow == false) {

                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        });
        monitorThread.start();

    }

    public void stop() {
        monitoring = false;
        if (monitorThread != null) {
            monitorThread.interrupt();
        }
    }
}
