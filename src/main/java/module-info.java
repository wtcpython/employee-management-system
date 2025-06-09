module com.company {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires transitive javafx.graphics;
    requires lombok;
    requires com.jfoenix;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires javafx.web;

    opens com.company to javafx.fxml;
    opens com.company.pojo to javafx.base;

    exports com.company;
    exports com.company.pojo;
    exports com.company.service;
}
