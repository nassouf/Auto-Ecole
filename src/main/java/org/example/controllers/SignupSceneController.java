package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.service.verification.SignupLoginServices;

import java.io.IOException;
public class SignupSceneController {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    PasswordField confirmpassword;
    @FXML
    PasswordField securityCode;
    @FXML
    Label errorPassword;
    @FXML
    Label errorCode;
    @FXML
    private void switchToStartup() throws IOException {
        App.setRoot("startup");
    }
    @FXML
    private void signup() throws IOException {
        if (!password.getText().equals(confirmpassword.getText())) {
            errorPassword.setText("Passwords do not match");
            errorPassword.setVisible(true);
        }else{
            errorPassword.setVisible(false);
            if (!(SignupLoginServices.signup(username.getText(), password.getText(), securityCode.getText()))) {
                errorCode.setText("Invalid Security Code");
                errorCode.setVisible(true);
            }else {
                errorCode.setText("Added Successfully");
                errorCode.setVisible(true);
            }

        }
    }
}
