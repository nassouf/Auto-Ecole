package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.service.verification.SignupLoginServices;

import java.io.IOException;

public class LoginSceneController {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Label errorText;

    @FXML
    private void switchToStartup() throws IOException {
        App.setRoot("startup");
    }
    @FXML
    private void switchToCondidat() throws IOException {
        String username = this.username.getText();
        System.out.println(username);
        String password = this.password.getText();
        System.out.println(password);
        if (!SignupLoginServices.login(username, password)){
            System.out.println("Login failed");
            errorText.setText("Wrong username or password");
            errorText.setVisible(true);
        }else {
            errorText.setText("Login successful");
            errorText.setVisible(false);
            App.setRoot("Home");
        }
    }

}