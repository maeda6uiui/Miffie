module com.github.maeda6uiui.miffie {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires org.slf4j;
    requires com.github.dabasan.jxm.mif;

    opens com.github.maeda6uiui.miffie to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.github.maeda6uiui.miffie;
}