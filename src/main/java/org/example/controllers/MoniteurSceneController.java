package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.App;
import org.example.entities.AutoEcoleInfo;
import org.example.entities.Employee;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.MoniteurService;
import org.example.service.verification.InputCheckServices;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MoniteurSceneController {
    @FXML public TableView<Employee> moniteursTable;
    @FXML public TableColumn<Employee, Integer> idMoniteur;
    @FXML public TableColumn<Employee, String> nom;
    @FXML public TableColumn<Employee, String> prenom;
    @FXML public TableColumn<Employee, String> dateNaissance;
    @FXML public TableColumn<Employee, String> email;
    @FXML public TableColumn<Employee, Integer> telephone;
    @FXML public TableColumn<Employee, String> Adresse;
    @FXML public TableColumn<Employee, VehiculeType> quatification;
    @FXML public TableColumn<Employee, Float> Salaire;
    @FXML public DatePicker date;
    @FXML public TextField fieldPrenom;
    @FXML public TextField fieldNom;
    @FXML public TextField fieldid;
    @FXML public TextField fieldemail;
    @FXML public ChoiceBox<VehiculeType> vehiculeType;
    @FXML public TextField fieldAdresse;
    @FXML public TextField fieldTelephone;
    @FXML public Label errorLabel;
    @FXML public TextField fieldSalaire;
    @FXML public Button btmodifier;
    @FXML public Button btajouter;
    @FXML public Button btaAnnuler;
    @FXML public TextField fieldSearch;
    @FXML public ChoiceBox<String> choiceSearch;

    @FXML private Label errorCin;
    @FXML private Label errorNom;
    @FXML private Label errorPrenom;
    @FXML private Label errorDateNaissance;
    @FXML private Label errorEmail;
    @FXML private Label errorAdresse;
    @FXML private Label errorTelephone;
    @FXML private Label errorSalaire;
    @FXML private Label errorSpecialisation;

    private final MoniteurService moniteurService = new MoniteurService();
    private FilteredList<Employee> filteredMoniteurs;

    @FXML
    public void initialize() {
        hideErrors();
        setupChoiceBoxes();
        setupButtonVisibility();
        setupTable();
        loadMoniteurs();
        setupSearchFunctionality();
        AutoEcoleInfo info = autoEcoleInfoServices.getAutoEcoleInfo(null);
        String logoPath = info.getAutoEcolelogo();
        if (logoPath != null && !logoPath.isEmpty()) {
            try {
                File file = new File(logoPath);
                if (file.exists()) {
                    Image logo = new Image(file.toURI().toString());
                    mainLogo.setImage(logo);
                }
            } catch (Exception e) {
                System.out.println("Error loading logo: " + e.getMessage());
            }
        }
    }

    private void setupChoiceBoxes() {
        vehiculeType.getItems().addAll(VehiculeType.values());

        // Setup search choice box
        choiceSearch.getItems().addAll("ID", "Nom", "Prénom", "Email", "Qualification");
        choiceSearch.setValue("Nom"); // Default search criteria
    }

    private void setupButtonVisibility() {
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        btaAnnuler.setVisible(true);
    }

    private void setupTable() {
        idMoniteur.setCellValueFactory(new PropertyValueFactory<>("idMoniteur"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        Adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        quatification.setCellValueFactory(new PropertyValueFactory<>("vehiculeType"));
        Salaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));
    }

    private void loadMoniteurs() {
        List<Employee> moniteurs = moniteurService.getAll();
        ObservableList<Employee> data = FXCollections.observableArrayList(moniteurs);
        filteredMoniteurs = new FilteredList<>(data, p -> true);
        moniteursTable.setItems(filteredMoniteurs);
        moniteursTable.refresh();
    }

    private void setupSearchFunctionality() {
        fieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMoniteurs.setPredicate(moniteur -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String searchCriteria = choiceSearch.getValue();

                switch (searchCriteria) {
                    case "ID":
                        return String.valueOf(moniteur.getIdMoniteur()).contains(lowerCaseFilter);
                    case "Nom":
                        return moniteur.getNom().toLowerCase().contains(lowerCaseFilter);
                    case "Prénom":
                        return moniteur.getPrenom().toLowerCase().contains(lowerCaseFilter);
                    case "Email":
                        return moniteur.getEmail().toLowerCase().contains(lowerCaseFilter);
                    case "Qualification":
                        return moniteur.getVehiculeType().toString().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });
    }

    @FXML
    public void searchByChoice(ActionEvent actionEvent) {
        // This will use the already set up listener to filter the results
        String searchText = fieldSearch.getText();
        fieldSearch.setText(searchText + " "); // Force the listener to trigger
        fieldSearch.setText(searchText);
    }

    @FXML
    public void MoniteurDeleteOption(ActionEvent actionEvent) {
        Employee selectedMoniteur = moniteursTable.getSelectionModel().getSelectedItem();
        if (selectedMoniteur != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Supprimer le moniteur");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce moniteur ?");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (moniteurService.delete(selectedMoniteur.getIdMoniteur())) {
                        showAlert("Succès", "Moniteur supprimé avec succès !", Alert.AlertType.INFORMATION);
                        loadMoniteurs();
                    } else {
                        showAlert("Erreur", "Échec de la suppression du moniteur.", Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            showAlert("Erreur", "Veuillez sélectionner un moniteur.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void ajoutMoniture(ActionEvent actionEvent) {
        if (checkInputs()) {
            try {
                Employee moniteur = createEmployeeFromFields();

                if (moniteurService.create(moniteur)) {
                    showAlert("Succès", "Moniteur ajouté avec succès!", Alert.AlertType.INFORMATION);
                    loadMoniteurs();
                    clear();
                } else {
                    showAlert("Erreur", "Échec de l'ajout du moniteur.", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                showAlert("Erreur", "Erreur: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private Employee createEmployeeFromFields() {
        return new Employee(
                Integer.parseInt(fieldid.getText()),
                fieldNom.getText(),
                fieldPrenom.getText(),
                date.getValue(),
                fieldemail.getText(),
                fieldAdresse.getText(),
                vehiculeType.getValue(),
                Float.parseFloat(fieldSalaire.getText()),
                Integer.parseInt(fieldTelephone.getText())
        );
    }

    @FXML
    private void editerMoniteur() {
        Employee selectedMoniteur = moniteursTable.getSelectionModel().getSelectedItem();
        if (selectedMoniteur != null) {
            // Fill fields with selected monitor's information
            fieldid.setText(String.valueOf(selectedMoniteur.getIdMoniteur()));
            fieldNom.setText(selectedMoniteur.getNom());
            fieldPrenom.setText(selectedMoniteur.getPrenom());
            date.setValue(selectedMoniteur.getDateNaissance());
            fieldemail.setText(selectedMoniteur.getEmail());
            fieldTelephone.setText(String.valueOf(selectedMoniteur.getTelephone()));
            fieldAdresse.setText(selectedMoniteur.getAdresse());
            vehiculeType.setValue(selectedMoniteur.getVehiculeType());
            fieldSalaire.setText(String.valueOf(selectedMoniteur.getSalaire()));

            fieldid.setDisable(false);
            btajouter.setVisible(false);
            btmodifier.setVisible(true);
            btaAnnuler.setVisible(true);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un moniteur!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void modifierMoniteur(ActionEvent event) {
        if (checkInputs()) {
            try {
                Employee selectedMoniteur = moniteursTable.getSelectionModel().getSelectedItem();
                if (selectedMoniteur == null) {
                    showAlert("Erreur", "Veuillez d'abord sélectionner un moniteur !", Alert.AlertType.WARNING);
                    return;
                }

                Employee moniteurModifie = createEmployeeFromFields();

                if (moniteurService.update(selectedMoniteur.getIdMoniteur(), moniteurModifie)) {
                    showAlert("Succès", "Moniteur modifié avec succès !", Alert.AlertType.INFORMATION);
                    loadMoniteurs();
                    annulerEdit();
                } else {
                    showAlert("Erreur", "Échec de la modification du moniteur.", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                showAlert("Erreur", "Erreur : " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void annulerEdit() {
        fieldid.setDisable(false);
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        btaAnnuler.setVisible(true);
        clear();
    }

    @FXML
    public void clear() {
        fieldid.clear();
        fieldNom.clear();
        fieldPrenom.clear();
        date.setValue(null);
        fieldemail.clear();
        fieldAdresse.clear();
        fieldSalaire.clear();
        fieldTelephone.clear();
        vehiculeType.getSelectionModel().clearSelection();
        fieldid.setDisable(false);
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        hideErrors();
    }

    @FXML
    private void switchToIngenenieur() throws IOException {
        App.setRoot("ingenieur");
    }

    @FXML
    private void switchToVehicule() throws IOException {
        App.setRoot("vehiculeMenu");
    }

    @FXML
    private void switchToCalendar() throws IOException {
        App.setRoot("calender");
    }

    @FXML
    private void switchToCondidat() throws IOException {
        App.setRoot("condidat");
    }

    private boolean checkInputs() {
        hideErrors();
        boolean check = true;

        if (!InputCheckServices.isValidCIN(fieldid.getText(), errorCin)) {
            check = false;
        }
        if (!InputCheckServices.isValidname(fieldNom.getText(), errorNom)) {
            check = false;
        }
        if (!InputCheckServices.isValidname(fieldPrenom.getText(), errorPrenom)) {
            check = false;
        }
        if (!InputCheckServices.isValidEmail(fieldemail.getText(), errorEmail)) {
            check = false;
        }
        if (!InputCheckServices.isValidPrice(fieldSalaire.getText(), errorSalaire)) {
            check = false;
        }
        if (!InputCheckServices.isValidPhoneNumber(fieldTelephone.getText(), errorTelephone)) {
            check = false;
        }
        if (fieldAdresse.getText().isEmpty()) {
            check = false;
            errorAdresse.setText("Ce champ ne peut pas être vide");
            errorAdresse.setVisible(true);
        }
        if (date.getValue() == null) {
            check = false;
            errorDateNaissance.setText("Il faut choisir une date");
            errorDateNaissance.setVisible(true);
        }
        if (vehiculeType.getValue() == null) {
            check = false;
            errorSpecialisation.setText("Il faut choisir une qualification");
            errorSpecialisation.setVisible(true);
        }

        return check;
    }

    private void hideErrors() {
        errorTelephone.setVisible(false);
        errorCin.setVisible(false);
        errorNom.setVisible(false);
        errorPrenom.setVisible(false);
        errorDateNaissance.setVisible(false);
        errorEmail.setVisible(false);
        errorAdresse.setVisible(false);
        errorSalaire.setVisible(false);
        errorSpecialisation.setVisible(false);
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void switchToHome() throws IOException{
        App.setRoot("Home");
    }
    @FXML
    public void switchToSetting(ActionEvent actionEvent)throws IOException{
        App.setRoot("autoecoleinfo");
    }
    @FXML
    private VBox comptabiliteSubMenu;

    @FXML
    private Button comptabiliteButton;
    @FXML
    public void toggleComptabiliteMenu() {
        comptabiliteSubMenu.setVisible(!comptabiliteSubMenu.isVisible());
        comptabiliteSubMenu.setManaged(!comptabiliteSubMenu.isManaged());
    }

    @FXML
    public void switchToPaiement() throws java.io.IOException {
        App.setRoot("paiement");
    }

    @FXML
    public void switchToFactures() throws java.io.IOException {
        App.setRoot("factures");
    }
    public void switchToStatique(ActionEvent actionEvent)throws java.io.IOException {
        App.setRoot("tableaudebord");
    }
    @FXML
    private ImageView mainLogo;
    private AutoEcoleInfoServices autoEcoleInfoServices =new AutoEcoleInfoServices();;

}