package org.example.controllers;

import javafx.fxml.FXML;
import org.example.App;

import java.io.IOException;

public class StartupSceneController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("login");
    }
    @FXML
    private void switchToSignup() throws IOException {
        App.setRoot("signup");
    }
}
