package com.javafx3.javafx3;

import com.javafx3.javafx3.impl.Moniter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Data;

import java.awt.*;
import java.net.URI;
import java.util.Date;

@Data
public class HelloController {
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

    // 控制监控的逻辑单元
    private Moniter moniter;
    private boolean isMonitoringActive = false;
    private Integer logCount = 0;

    @FXML
    protected void onForwordButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

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
        //timeParam.setText("10m");
        price.setText("118.3-130.5");
        token.setText("XMRUSDT");
    }

    @FXML
    private void handleOpenWebPage() {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                // 设置要打开的网址
                String baseUrl = "www.binance.com/zh-CN/futures/";
                if(token.getText() !=null && token.getText().length()>0){
                    baseUrl += token.getText();
                }else {
                    baseUrl += "XMRUSDT";
                }
                URI uri = new URI(baseUrl);
                desktop.browse(uri);
                setLog("打开网页: "+baseUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 在这里处理错误情况，例如弹出一个对话框提示用户错误信息
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