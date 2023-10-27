module com.github.maeda6uiui.miffie {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.maeda6uiui.miffie to javafx.fxml;
    exports com.github.maeda6uiui.miffie;
}