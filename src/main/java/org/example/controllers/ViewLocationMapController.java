package org.example.controllers;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.net.URL;

public class ViewLocationMapController {
    @FXML
    private WebView webView;
    @FXML
    private Label coordinatesLabel;
    @FXML
    private Label locationNameLabel;

    private float locationX;
    private float locationY;
    private String locationName;

    public void setLocationData(float x, float y, String name) {
        this.locationX = x;
        this.locationY = y;
        this.locationName = name;
    }

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        URL url = getClass().getResource("/org/example/map.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.err.println("Error: map.html not found!");
        }

        // Wait for the page to load before adding the marker
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // JavaScript bridge setup
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", this);

                // Display location if available
                if (locationX != 0 && locationY != 0 && locationName != null) {
                    displayMarker(webEngine);
                }
            }
        });
    }

    private void displayMarker(WebEngine webEngine) {
        try {
            // Update labels with location info
            if (coordinatesLabel != null) {
                coordinatesLabel.setText(String.format("Coordinates: %.5f, %.5f", locationX, locationY));
            }

            if (locationNameLabel != null) {
                locationNameLabel.setText("Location: " + locationName);
            }

            // Execute JavaScript to add a marker and center the map
            String script = String.format(
                    "var lat = %f; var lng = %f; " +
                            "map.setView([lat, lng], 15); " +
                            "if (currentMarker) { map.removeLayer(currentMarker); } " +
                            "currentMarker = L.marker([lat, lng]).addTo(map)" +
                            ".bindPopup('%s').openPopup();",
                    locationX, locationY, locationName.replace("'", "\\'"));

            webEngine.executeScript(script);
        } catch (Exception e) {
            System.err.println("Error displaying marker: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void closeMap(ActionEvent event) {
        Stage stage = (Stage) webView.getScene().getWindow();
        stage.close();
    }
}