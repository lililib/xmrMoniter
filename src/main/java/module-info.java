module com.javafx3.javafx3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires binance.futures.connector.java;
    requires cn.hutool.json;
    requires java.desktop;
    requires static lombok;
    requires org.yaml.snakeyaml;


    opens com.javafx3.javafx3 to javafx.fxml;
    exports com.javafx3.javafx3;
}