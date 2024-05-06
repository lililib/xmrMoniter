package com.javafx3.javafx3.impl;

import cn.hutool.json.JSONUtil;
import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import com.binance.connector.futures.client.utils.ProxyAuth;
import com.javafx3.javafx3.MainController;
import com.javafx3.javafx3.utils.YamlReader;
import javafx.application.Platform;
import javafx.stage.Stage;

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

    private MainController mainController;

    private volatile boolean monitoring;
    private Thread monitorThread;

    private String symbol;
    private String price;

    public Moniter(MainController mainController) {
        this.mainController = mainController;
        this.symbol = mainController.getToken().getText();
        this.price = mainController.getPrice().getText();
    }

    public void start() {
        String apiKey = (String) YamlReader.instance.getValueByKey("apiKey");
        String secretKey = (String) YamlReader.instance.getValueByKey("secretKey");
        mainController.setLog(apiKey + "-----" + secretKey);
        UMFuturesClientImpl client = new UMFuturesClientImpl(apiKey, secretKey);
        Proxy proxyConn = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809));
        ProxyAuth proxy = new ProxyAuth(proxyConn, null);
        client.setProxy(proxy);
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
        String[] tokens = symbol.split(",");
        String[] tokenPrice = price.split(",");

        monitorThread = new Thread(() -> {
            monitoring = true;
            while (monitoring) {
                for (int i = 0; i < tokens.length; i++) {
                    parameters.clear();
                    parameters.put("symbol",tokens[i]);
                    String lowPrice =tokenPrice[i].split("-")[0];
                    String highPrice = tokenPrice[i].split("-")[1];


                    String bookTickerStr = client.market().bookTicker(parameters);
                    BigDecimal bidPrice = new BigDecimal((String) JSONUtil.parseObj(bookTickerStr).get("bidPrice"));
                    mainController.setLog(tokens[i]+" bidPrice:" + bidPrice);
                    if (bidPrice.compareTo(new BigDecimal(lowPrice)) <= 0 || bidPrice.compareTo(new BigDecimal(highPrice)) >= 0) {
                        Random random = new Random();
                        int num = random.nextInt(100);
                        if (num != 0 && num % 2 == 0) {
                            java.awt.Toolkit.getDefaultToolkit().beep();
                            mainController.setLog("trigger token:"+tokens[i]   +"   price:"+bidPrice);
                            mainController.setForwordUrl(mainController.getForwordUrl()+tokens[i]);
                            Platform.runLater(() -> {
                                Stage stage = mainController.getStage();
                                if (!stage.isFocused()) {
                                    stage.setIconified(false);
                                    stage.show();
                                    stage.toFront();
                                }
                            });
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
