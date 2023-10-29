module com.github.maeda6uiui.miffie {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.github.dabasan.jxm.mif;

    opens com.github.maeda6uiui.miffie to javafx.fxml;
    exports com.github.maeda6uiui.miffie;
}