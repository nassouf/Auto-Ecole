package org.example.controllers;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.net.URL;

public class MapController {
    private Float locationX;
    private Float locationY;
    private String locationName;

    @FXML
    private WebView webView;

    @FXML
    private Label coordinatesLabel;
    @FXML
    private Label locationNameLabel;
    @FXML
    private Button confirmButton;

    private boolean isViewOnly = false;

    @FXML
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        URL url = getClass().getResource("/org/example/map.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            System.err.println("Error: map.html not found!");
        }

        // Once the page loads, set up the JavaScript bridge
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // Get the window object of the page
                JSObject window = (JSObject) webEngine.executeScript("window");
                // Expose this Java controller to JavaScript as 'java'
                window.setMember("java", this);

                // If we have pre-defined location coordinates (for viewing only)
                // set them on the map
                checkAndDisplayPredefinedLocation(webEngine);
            }
        });
    }

    private void checkAndDisplayPredefinedLocation(WebEngine webEngine) {
        // Check if we have location data from SingleDayConduiteController
        Float x = SingleDayConduiteController.getLocationX();
        Float y = SingleDayConduiteController.getLocationY();
        String name = SingleDayConduiteController.getLocationName();

        if (x != null && y != null && name != null) {
            // This is view-only mode
            isViewOnly = true;

            // If in view-only mode, change the confirm button text
            confirmButton.setText("Close");

            // Update our local values
            this.locationX = x;
            this.locationY = y;
            this.locationName = name;

            // Update labels
            coordinatesLabel.setText(String.format("Coordinates: %.5f, %.5f", x, y));
            locationNameLabel.setText("Location: " + name);

            // Call JavaScript to set the marker at these coordinates
            String script = String.format(
                    "var lat = %f; var lng = %f; " +
                            "map.setView([lat, lng], 15); " +
                            "if (currentMarker) { map.removeLayer(currentMarker); } " +
                            "currentMarker = L.marker([lat, lng]).addTo(map)" +
                            ".bindPopup('%s').openPopup();",
                    x, y, name.replace("'", "\\'"));

            webEngine.executeScript(script);
        }
    }

    public void receiveCoordinates(double lat, double lng, String locationName) {
        // Only update if we're not in view-only mode
        if (!isViewOnly) {
            // Update the instance variables
            this.locationX = (float) lat;
            this.locationY = (float) lng;
            this.locationName = locationName;

            // Update the label with the received coordinates
            String text = String.format("Coordinates: %.5f, %.5f", lat, lng);
            coordinatesLabel.setText(text);

            // Update the location name label
            locationNameLabel.setText("Location: " + locationName);
        }
    }

    @FXML
    private void confirm(ActionEvent event) {
        if (!isViewOnly) {
            // Set the location information in the static variables
            SingleDayConduiteController.setLocationX(locationX);
            ExamenConduitController.setLocationX(locationX);
            SingleDayConduiteController.setLocationY(locationY);
            ExamenConduitController.setLocationY(locationY);
            SingleDayConduiteController.setLocationName(locationName);
            ExamenConduitController.setLocationName(locationName);
        }

        // Close the map window
        closeMapWindow();
    }

    @FXML
    private void switchToScConduite(ActionEvent event) {
        // Since we're now using a dialog, this should just close the map window
        closeMapWindow();
    }

    // Method to close the map window
    private void closeMapWindow() {
        // Get the current stage and close it
        Stage stage = (Stage) webView.getScene().getWindow();
        stage.close();
    }
}