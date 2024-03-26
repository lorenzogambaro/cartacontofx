module com.conto.cartacontogui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;
    requires java.sql;

    opens com.conto.cartacontogui to javafx.fxml;
    opens model to com.google.gson;
    exports com.conto.cartacontogui;
    exports applicationControllers;
    exports userControllers;
    exports adminControllers;
    opens userControllers;
    opens applicationControllers;
    opens adminControllers;
}
