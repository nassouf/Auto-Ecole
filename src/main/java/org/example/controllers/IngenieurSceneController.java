package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.example.entities.Vehicule;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.IngenieurService;
import org.example.service.VehiculeServices;
import org.example.service.verification.InputCheckServices;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class IngenieurSceneController {
    @FXML
    private ChoiceBox<String> choiceSearch=new ChoiceBox<>();
    @FXML
    private TextField fieldSearch;
    public TableView<Employee> ingenieurTable;
    public TableColumn<Employee,Integer> idIngenieur;
    public TableColumn<Employee,String> nom;
    public TableColumn<Employee,String> prenom;
    public TableColumn<Employee, LocalDateTime> dateNaissance;
    public TableColumn<Employee,String> email;
    public TableColumn<Employee,Integer> telephone;
    public TableColumn<Employee,String> Adresse;
    public TableColumn<Employee,VehiculeType> quatification;
    public TableColumn<Employee,Float> Salaire;
    public DatePicker date;
    public TextField fieldPrenom;
    public TextField fieldNom;
    public TextField fieldid;
    public TextField fieldemail;
    public ChoiceBox<VehiculeType> vehiculeTypeChoiceBox;
    public TextField fieldAdresse;
    public TextField fieldTelephone;
    public Label errorLabel;
    public TextField fieldSalaire;
    private final IngenieurService ingenieurService = new IngenieurService();
    @FXML
    private Button btajouter;
    @FXML
    private Button btmodifier;
    @FXML
    private Button btannuler;

    @FXML
    private Label errorCin;
    @FXML
    private Label errorNom;
    @FXML
    private Label errorPrenom;
    @FXML
    private Label errorDateNaissance;
    @FXML
    private Label errorEmail;
    @FXML
    private Label errorAdresse;
    @FXML
    private Label errorTelephone;
    @FXML
    private Label errorSalaire;
    @FXML
    private Label errorSpecialisation;

    @FXML
    public void initialize() {
        hideErrors();
        choiceSearch.getItems().addAll("Nom","Prenom","Type");
        vehiculeTypeChoiceBox.setItems(FXCollections.observableArrayList(VehiculeType.values()));
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        btannuler.setVisible(false);
        setupTable();
        loadIngenieurs();
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
    private void hideErrors(){
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
    @FXML
    private void annulerEdit(ActionEvent event) {
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        btannuler.setVisible(false);
        resetInputs();
    }
    private boolean checkInputs(){
        hideErrors();
        boolean check=true;
        if (!InputCheckServices.isValidCIN(fieldid.getText(),errorCin)){
            check=false;
        }
        if (!InputCheckServices.isValidname(fieldNom.getText(),errorNom)){
            check=false;
        }
        if (!InputCheckServices.isValidname(fieldPrenom.getText(),errorPrenom)){
            check=false;
        }
        if(!InputCheckServices.isValidEmail(fieldemail.getText(),errorEmail)){
            check=false;
        }
        if (!InputCheckServices.isValidPrice(fieldSalaire.getText(),errorSalaire)){
            check=false;
        }
        if (!InputCheckServices.isValidPhoneNumber(fieldTelephone.getText(),errorTelephone)){
            System.out.println("phone problem");
            check=false;
        }
        if(fieldAdresse.getText().isEmpty()){
            check=false;
            errorAdresse.setText("field is empty");
            errorAdresse.setVisible(true);
        }
        if (date.getValue()==null){
            check=false;
            errorDateNaissance.setText("Il faut choisir une date");
            errorDateNaissance.setVisible(true);
        }
        if (vehiculeTypeChoiceBox.getValue()==null){
            check=false;
            errorSpecialisation.setText("Il faut choisir une type");
            errorSpecialisation.setVisible(true);
        }
        return check;
    }

    private void resetInputs() {
        fieldAdresse.clear();
        fieldTelephone.clear();
        fieldSalaire.clear();
        fieldid.clear();
        fieldNom.clear();
        fieldPrenom.clear();
        fieldemail.clear();
        date.setValue(null);
        vehiculeTypeChoiceBox.setValue(null);
    }
    @FXML
    private void resetTable() {
        IngenieurService ingenieurService = new IngenieurService();
        ObservableList<Employee> ingenieurs = FXCollections.observableArrayList(ingenieurService.getAll());
        ingenieurTable.setItems(ingenieurs);
        ingenieurTable.refresh();
    }

    private void setupTable() {
        idIngenieur.setCellValueFactory(new PropertyValueFactory<>("idMoniteur"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        Adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        quatification.setCellValueFactory(new PropertyValueFactory<>("vehiculeType"));
        Salaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));
    }

    private void loadIngenieurs() {
        List<Employee> ingenieurs = ingenieurService.getAll();
        ObservableList<Employee> data = FXCollections.observableArrayList(ingenieurs);
        ingenieurTable.setItems(data);
        ingenieurTable.refresh();
    }


    public void ingenieurDeleteOption(ActionEvent actionEvent) {
        Employee selectedIngenieur = (Employee) ingenieurTable.getSelectionModel().getSelectedItem();
        if (selectedIngenieur != null) {
            if (ingenieurService.delete(selectedIngenieur.getIdMoniteur())) {
                errorLabel.setText("Ingenieur supprimé avec succès!");
                loadIngenieurs();
            } else {
                errorLabel.setText("Échec de la suppression du Ingenieur.");
            }
        } else {
            errorLabel.setText("Veuillez sélectionner un Ingenieur.");
        }
    }


    public void ajoutIngenieur(ActionEvent actionEvent) {
        if (checkInputs()) {
            try {
                Employee ingenieur = new Employee(
                        Integer.parseInt(fieldid.getText().trim()),
                        fieldNom.getText(),
                        fieldPrenom.getText(),
                        date.getValue(),
                        fieldemail.getText(),
                        fieldAdresse.getText(),
                        vehiculeTypeChoiceBox.getValue(),
                        Float.parseFloat(fieldSalaire.getText().trim()),
                        Integer.parseInt(fieldTelephone.getText().trim())
                );
                System.out.println(ingenieur);
                if (ingenieurService.create(ingenieur)) {
                    errorLabel.setText("Ingenieur ajouté avec succès!");
                    loadIngenieurs();
                    resetInputs();
                    hideErrors();
                } else {
                    errorLabel.setText("Échec de l'ajout du ingenieur.");
                }
            } catch (Exception e) {
                System.out.println(e);
                errorLabel.setText(e.getMessage());
            }
        }

    }
    @FXML
    private void searchByChoice(ActionEvent event){
        resetTable();
        switch(choiceSearch.getValue()){
            case "Nom":
                if (fieldSearch.getText() == null || fieldSearch.getText().trim().isEmpty()||choiceSearch.getValue().isEmpty()) {
                    resetTable();
                    System.out.println("Search field is empty.");
                    break;
                }

                try {
                    String nom = fieldSearch.getText().trim().toLowerCase();
                    ingenieurTable.setItems(ingenieurTable.getItems().filtered(i->i.getNom().toLowerCase().contains(nom)));
                    ingenieurTable.refresh();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid nom: " + fieldSearch.getText());
                }
                break;
            case "Type":
                if (fieldSearch.getText() == null || fieldSearch.getText().trim().isEmpty()||choiceSearch.getValue().isEmpty()) {
                    resetTable();
                    System.out.println("Search field is empty.");
                    break;
                }

                try {
                    String type = fieldSearch.getText().trim().toLowerCase();
                    ingenieurTable.setItems(
                            ingenieurTable.getItems().filtered(v -> v.getVehiculeType().toString().toLowerCase().contains(type))
                    );
                    ingenieurTable.refresh();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid type: " + fieldSearch.getText());
                }
                break;
            case "Prenom":
                if (fieldSearch.getText() == null || fieldSearch.getText().trim().isEmpty()||choiceSearch.getValue().isEmpty()) {
                    resetTable();
                    System.out.println("Search field is empty.");
                    break;
                }

                try {
                    String prenom = fieldSearch.getText().trim().toLowerCase();
                    ingenieurTable.setItems(
                            ingenieurTable.getItems().filtered(v -> v.getPrenom().toLowerCase().contains(prenom))
                    );
                    ingenieurTable.refresh();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid prenom: " + fieldSearch.getText());
                }
                break;
        }
    }
    @FXML
    private void editerIngenieur() {
        Employee selectedIngenieur = (Employee) ingenieurTable.getSelectionModel().getSelectedItem();
        if (selectedIngenieur != null) {
            // Remplir les champs avec les informations du Ingenieur sélectionné
            fieldid.setText(String.valueOf(selectedIngenieur.getIdMoniteur()));
            fieldNom.setText(selectedIngenieur.getNom());
            fieldPrenom.setText(selectedIngenieur.getPrenom());
            date.setValue(selectedIngenieur.getDateNaissance());
            fieldemail.setText(selectedIngenieur.getEmail());
            fieldTelephone.setText(String.valueOf(selectedIngenieur.getTelephone()));
            fieldAdresse.setText(selectedIngenieur.getAdresse());
            vehiculeTypeChoiceBox.setValue(selectedIngenieur.getVehiculeType());
            fieldSalaire.setText(String.valueOf(selectedIngenieur.getSalaire()));

            // Afficher un message pour confirmer la modification
            errorLabel.setText("Modification du ingenieur en cours...");
        } else {
            errorLabel.setText("Veuillez sélectionner un ingenieur !");
        }
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
    }
    @FXML
    public void modifierIngenieur(ActionEvent event) {
        if (checkInputs()) {
            try {

                Employee selectedIngenieur = (Employee) ingenieurTable.getSelectionModel().getSelectedItem();
                if (selectedIngenieur == null) {
                    errorLabel.setText("Veuillez d'abord sélectionner un ingenieur !");
                    return;
                }

                // Créer un objet Employee mis à jour
                Employee ingenieurModifie = new Employee(
                        selectedIngenieur.getIdMoniteur(),
                        fieldNom.getText(),
                        fieldPrenom.getText(),
                        date.getValue(),
                        fieldemail.getText(),
                        fieldAdresse.getText(),
                        vehiculeTypeChoiceBox.getValue(),
                        Float.parseFloat(fieldSalaire.getText()),
                        Integer.parseInt(fieldTelephone.getText())
                );

                // Mettre à jour dans la base de données
                if (ingenieurService.update(selectedIngenieur.getIdMoniteur(), ingenieurModifie)) {
                    errorLabel.setText("ingenieur modifié avec succès !");
                    loadIngenieurs();// Rafraîchir la table
                    annulerEdit(event);
                    hideErrors();
                } else {
                    errorLabel.setText("Échec de la modification.");
                }
            } catch (Exception e) {
                errorLabel.setText("Erreur : " + e.getMessage());
            }
        }
    }
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
    private void switchToMoniteur(ActionEvent event) throws IOException {
        App.setRoot("moniteur");
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
    private void switchToCondidat(ActionEvent event) throws IOException {

        App.setRoot("condidat");
    }
    @FXML
    private void switchToHome() throws IOException{
        App.setRoot("Home");
    }
    @FXML
    private ImageView mainLogo;
    private AutoEcoleInfoServices autoEcoleInfoServices =new AutoEcoleInfoServices();;

}
