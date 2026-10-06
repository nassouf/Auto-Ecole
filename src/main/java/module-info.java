module org.example {
    requires javafx.fxml;
    requires java.sql;
    requires javafx.controls;
    requires javafx.web;
    requires jdk.jsobject;
    requires java.desktop;
    requires io;
    requires layout;
    requires kernel;
    requires jdk.compiler;

    // Open necessary packages
    opens org.example to javafx.fxml;
    exports org.example;

    opens org.example.controllers to javafx.fxml;
    exports org.example.controllers;

    opens org.example.entities to javafx.base, javafx.fxml;
    exports org.example.entities;

    opens org.example.service to javafx.fxml;
    exports org.example.service;
    exports org.example.service.verification;
    opens org.example.service.verification to javafx.fxml;
    exports org.example.entities.exceptions;
    opens org.example.entities.exceptions to javafx.base, javafx.fxml;
    exports org.example.entities.interfaces;
    opens org.example.entities.interfaces to javafx.base, javafx.fxml;
    exports org.example.entities.myEnums;
    opens org.example.entities.myEnums to javafx.base, javafx.fxml;
    exports org.example.service.myInterfaces;
    opens org.example.service.myInterfaces to javafx.fxml;
}