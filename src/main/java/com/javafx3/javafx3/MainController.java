package com.javafx3.javafx3;

import com.javafx3.javafx3.impl.Moniter;
import com.javafx3.javafx3.utils.YamlReader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Data;

import java.awt.*;
import java.net.URI;
import java.util.Date;

@Data
public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button forword;

    @FXML
    private Button start;

    @FXML
    private TextField timeParam;
    @FXML
    private TextField price;
    @FXML
    private TextField token;
    @FXML
    private TextArea logTextArea;

    private Stage stage;

    private String forwordUrl = "www.binance.com/zh-CN/futures/";

    // 控制监控的逻辑单元
    private Moniter moniter;
    private boolean isMonitoringActive = false;
    private Integer logCount = 0;

    @FXML
    protected void onStartButtonClick() {
        if (!isMonitoringActive) {
            setLog("开始监控");
            //setLog("时间参数: "+timeParam.getText());
            setLog("监控价格: "+price.getText());
            setLog("监控币种: "+token.getText());
            moniter = new Moniter(this);
            moniter.start();
            start.setText("stop");
        }
        else {
            moniter.stop();
            setLog("停止监控");
            start.setText("start");
        }
        isMonitoringActive = !isMonitoringActive;

    }

    @FXML
    public void parperInsert() {
        if(price.getText()==null || price.getText().length()==0){
            String token = (String)YamlReader.instance.getValueByKey("token");
            String price = (String)YamlReader.instance.getValueByKey("priceRange");

            this.price.setText(price);
            this.token.setText(token);
            setLog(token+"++"+price);
        }

    }

    public static void main(String[] args) {
        String aaaa =(String) YamlReader.instance.getValueByKey("moniter");
        System.out.println(aaaa);
    }


    /**
     * 点击跳转网页
     */
    @FXML
    private void handleOpenWebPage() {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                if(! forwordUrl.contains("usdt")){
                }else {
                    forwordUrl += "XMRUSDT";
                }
                URI uri = new URI(forwordUrl);
                desktop.browse(uri);
                setLog("打开网页: "+forwordUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setLog(String log) {
        logCount++;
        Platform.runLater(() -> logTextArea.appendText(new Date() +" "+log+"\n"));
        if(logCount > 50000) {
            logTextArea.clear();
            logCount = 0;
        }
    }

    public void stopMoniterIfNeeded() {
        if (moniter != null || isMonitoringActive) {
            moniter.stop();
            isMonitoringActive = false; // 更新标志来反映监控已停止
        }
    }
}