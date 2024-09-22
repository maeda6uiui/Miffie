module com.github.maeda6uiui.miffie {
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires com.github.dabasan.jxm.mif;
    requires java.desktop;

    opens com.github.maeda6uiui.miffie to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.github.maeda6uiui.miffie;
}