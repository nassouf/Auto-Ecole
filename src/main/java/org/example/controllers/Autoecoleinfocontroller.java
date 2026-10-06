package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import org.example.App;
import org.example.entities.AutoEcoleInfo;
import org.example.entities.Tarifs;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.TarifDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Autoecoleinfocontroller {

    @FXML  public Label logoPathLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField websiteField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;

    // Logo fields
    @FXML
    private ImageView logoPreview;
    @FXML
    private ImageView sidebarLogoPreview;
    @FXML
    private ImageView mainLogo;
    @FXML
    private Button chooseLogo;


    // Tarifs fields - séances de code
    @FXML
    private TextField codeSessionPriceField; // voiture
    @FXML
    private TextField codeSessionPriceField1; // moto
    @FXML
    private TextField codeSessionPriceField11; // camion

    // Tarifs fields - séances de conduite
    @FXML
    private TextField drivingSessionPriceField; // voiture
    @FXML
    private TextField drivingSessionPriceField1; // moto
    @FXML
    private TextField drivingSessionPriceField11; // camion

    // Tarifs fields - examen code
    @FXML
    private TextField codeExamPriceField; // voiture
    @FXML
    private TextField codeExamPriceField1; // moto
    @FXML
    private TextField codeExamPriceField11; // camion

    // Tarifs fields - examen conduite
    @FXML
    private TextField drivingExamPriceField; // voiture
    @FXML
    private TextField drivingExamPriceField1; // moto
    @FXML
    private TextField drivingExamPriceField2; // camion

    // Comptabilité submenu
    @FXML
    private Button comptabiliteButton;
    @FXML
    private VBox comptabiliteSubMenu;

    private String selectedLogoPath;
    private AutoEcoleInfoServices autoEcoleInfoServices;
    private TarifDAO tarifDAO;

    public void initialize() {
        autoEcoleInfoServices = new AutoEcoleInfoServices();
        tarifDAO = new TarifDAO();

        // Initialize the comptabilité submenu as hidden
        comptabiliteSubMenu.setVisible(false);
        comptabiliteSubMenu.setManaged(false);

        // Load existing data
        loadAutoEcoleInfo();
        loadTarifs();
    }

    private void loadAutoEcoleInfo() {
        // Get auto-école info from database
        AutoEcoleInfo info = autoEcoleInfoServices.getAutoEcoleInfo(null);

        if (info != null) {
            nameField.setText(info.getAutoEcoleName());
            descriptionField.setText(info.getAutoEcoleDescription());
            websiteField.setText(info.getAutoEcoleWebsite());
            phoneField.setText(info.getAutoEcolePhone() != null ? info.getAutoEcolePhone().toString() : "");
            emailField.setText(info.getAutoEcoleEmail());
            addressField.setText(info.getAutoEcoleAddress());

            // Load logo if available
            String logoPath = info.getAutoEcolelogo();
            if (logoPath != null && !logoPath.isEmpty()) {
                try {
                    File file = new File(logoPath);
                    if (file.exists()) {
                        Image logo = new Image(file.toURI().toString());
                        logoPreview.setImage(logo);
                        sidebarLogoPreview.setImage(logo);
                        mainLogo.setImage(logo);
                        logoPathLabel.setText(file.getName());
                        selectedLogoPath = logoPath;
                    }
                } catch (Exception e) {
                    System.out.println("Error loading logo: " + e.getMessage());
                }
            }
        }
    }

    private void loadTarifs() {
        try {
            // Try to get actual tariffs from the database (if method exists)
            Tarifs tarifs = tarifDAO.getTarifs();

            if (tarifs != null) {
                codeSessionPriceField.setText(String.valueOf(tarifs.getCodeSessionPricevoiture()));
                codeSessionPriceField1.setText(String.valueOf(tarifs.getCodeSessionPricemoto()));
                codeSessionPriceField11.setText(String.valueOf(tarifs.getCodeSessionPricecamion()));

                drivingSessionPriceField.setText(String.valueOf(tarifs.getDrivingSessionPricevoiture()));
                drivingSessionPriceField1.setText(String.valueOf(tarifs.getDrivingSessionPricemoto()));
                drivingSessionPriceField11.setText(String.valueOf(tarifs.getDrivingSessionPricecamion()));
            } else {
                // Set default values if no tariffs found
                codeSessionPriceField.setText("20");
                codeSessionPriceField1.setText("18");
                codeSessionPriceField11.setText("25");

                drivingSessionPriceField.setText("40");
                drivingSessionPriceField1.setText("35");
                drivingSessionPriceField11.setText("50");
            }

            // Default values for exam prices (not in the Tarifs entity yet)
            codeExamPriceField.setText("30");
            codeExamPriceField1.setText("25");
            codeExamPriceField11.setText("40");

            drivingExamPriceField.setText("60");
            drivingExamPriceField1.setText("55");
            drivingExamPriceField2.setText("75");
        } catch (Exception e) {
            System.out.println("Error loading tariffs: " + e.getMessage());
            e.printStackTrace();

            // Set default values in case of error
            codeSessionPriceField.setText("20");
            codeSessionPriceField1.setText("18");
            codeSessionPriceField11.setText("25");

            drivingSessionPriceField.setText("40");
            drivingSessionPriceField1.setText("35");
            drivingSessionPriceField11.setText("50");

            codeExamPriceField.setText("30");
            codeExamPriceField1.setText("25");
            codeExamPriceField11.setText("40");

            drivingExamPriceField.setText("60");
            drivingExamPriceField1.setText("55");
            drivingExamPriceField2.setText("75");
        }
    }

    @FXML
    public void handleLogoSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un logo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(logoPreview.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Create a resource directory if it doesn't exist
                Path resourceDir = Paths.get("ProjectJava/src/main/resources/logos");
                if (!Files.exists(resourceDir)) {
                    Files.createDirectories(resourceDir);
                }

                // Copy the file to the resource directory
                String fileName = selectedFile.getName();
                Path targetPath = resourceDir.resolve(fileName);
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Update the UI with the new logo
                Image logo = new Image(targetPath.toUri().toString());
                logoPreview.setImage(logo);
                sidebarLogoPreview.setImage(logo);
                mainLogo.setImage(logo);
                logoPathLabel.setText(fileName);

                // Store the path for saving later
                selectedLogoPath = targetPath.toString();

            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger le logo",
                        "Une erreur s'est produite: " + e.getMessage());
            }
        }
    }

    @FXML
    public void saveSettings() {
        try {
            // Save Auto-École Info
            AutoEcoleInfo info = new AutoEcoleInfo();
            info.setAutoEcoleName(nameField.getText());
            info.setAutoEcoleDescription(descriptionField.getText());
            info.setAutoEcoleWebsite(websiteField.getText());

            try {
                info.setAutoEcolePhone(Integer.parseInt(phoneField.getText()));
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Format de numéro invalide",
                        "Le numéro de téléphone doit être un nombre entier.");
                return;
            }

            info.setAutoEcoleEmail(emailField.getText());
            info.setAutoEcoleAddress(addressField.getText());

            if (selectedLogoPath != null && !selectedLogoPath.isEmpty()) {
                info.setAutoEcolelogo(selectedLogoPath);
            }

            boolean infoSaved = autoEcoleInfoServices.modify(info);

            // Save Tarifs
            Tarifs tarifs = new Tarifs(
                    getIntValue(codeSessionPriceField),
                    getIntValue(codeSessionPriceField1),
                    getIntValue(codeSessionPriceField11),
                    getIntValue(drivingSessionPriceField),
                    getIntValue(drivingSessionPriceField1),
                    getIntValue(drivingSessionPriceField11)
            );

            boolean tarifsSaved = tarifDAO.update(tarifs);

            if (infoSaved && tarifsSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Paramètres enregistrés",
                        "Les paramètres de l'auto-école ont été enregistrés avec succès.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Attention", "Enregistrement partiel",
                        "Certains paramètres n'ont pas pu être enregistrés.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur d'enregistrement",
                    "Une erreur s'est produite lors de l'enregistrement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int getIntValue(TextField field) {
        try {
            return Integer.parseInt(field.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void toggleComptabiliteMenu() {
        comptabiliteSubMenu.setVisible(!comptabiliteSubMenu.isVisible());
        comptabiliteSubMenu.setManaged(!comptabiliteSubMenu.isManaged());
    }



    @FXML
    public void switchToCandidat(ActionEvent event) throws IOException{
        App.setRoot("condidat");
    }

    @FXML
    private void switchToMoniteur(ActionEvent event) throws IOException {
        App.setRoot("moniteur");
    }
    @FXML
    private void switchToIngenenieur(ActionEvent event) throws IOException{
        App.setRoot("ingenieur");
    }
    @FXML
    private void switchToVehicule() throws IOException {
        App.setRoot("vehiculeMenu");
    }
    @FXML
    private void switchToCalendar() throws IOException{
        App.setRoot("calender");
    }
    @FXML
    private void switchToHome() throws IOException{
        App.setRoot("Home");
    }

    @FXML
    public void switchToPaiement() throws IOException {
        App.setRoot("paiement");
    }

    @FXML
    public void switchToFactures() throws IOException {
        App.setRoot("factures");
    }
    @FXML
    public void switchToStatique(ActionEvent actionEvent)throws java.io.IOException {
        App.setRoot("tableaudebord");
    }
}