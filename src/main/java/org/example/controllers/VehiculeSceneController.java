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
import org.example.entities.Maintenance;
import org.example.entities.Vehicule;
import org.example.entities.exceptions.ExistingVehiculeMatriculeException;
import org.example.entities.exceptions.InvalideInputTypeException;
import org.example.entities.exceptions.MissingDataException;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.AutoEcoleInfoServices;
import org.example.service.MaintenanceServices;
import org.example.service.VehiculeServices;
import org.example.service.verification.InputCheckServices;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class VehiculeSceneController {
    @FXML
    private TableView<Vehicule> vehiculeTable;
    @FXML
    private TableColumn<Vehicule, Integer> matricule;
    @FXML
    private TableColumn<Vehicule, String> marque;
    @FXML
    private TableColumn<Vehicule, Float> kilometer;
    @FXML
    private TableColumn<Vehicule, Float> KilometerRestant;
    @FXML
    private TableColumn<Vehicule, Integer> status;
    @FXML
    private TableColumn<Vehicule, VehiculeType> type;
    @FXML
    private TableColumn<Vehicule, LocalDate> dateAssurance;
    @FXML
    private TableColumn<Vehicule, Float> prixAssurance;
    @FXML
    private TextField fieldMatricule1;
    @FXML
    private TextField fieldMatricule2;
    @FXML
    private TextField fieldMarque;
    @FXML
    private TextField fieldKilometer;
    @FXML
    private TextField fieldKilometerRestant;
    @FXML
    private ChoiceBox<VehiculeType> choiceType;
    @FXML
    private DatePicker datePickerAssuranceDate;
    @FXML
    private TextField fieldPrixAssurance;
    @FXML
    private Label errorAjout;
    @FXML
    private Label errorMatricule;
    @FXML
    private Label errorMarque;
    @FXML
    private Label errorKilometrage;
    @FXML
    private Label errorKilometerRestant;
    @FXML
    private Label errorDateAssurance;
    @FXML
    private Label errorPrixAssurance;
    @FXML
    private Label errorType;
    @FXML
    private Button btajouter;
    @FXML
    private Button btmodifier;
    @FXML
    private Button btannuler;
    @FXML
    private ImageView mainLogo;
    private AutoEcoleInfoServices autoEcoleInfoServices =new AutoEcoleInfoServices(); ;


    @FXML
    private ChoiceBox<String> choiceSearch=new ChoiceBox<>();
    @FXML
    private TextField fieldSearch;

    @FXML
    private void initialize() {
        btajouter.setVisible(true);
        btannuler.setVisible(false);
        btmodifier.setVisible(false);

        hideErrors();
        //setting the values for the choicelist
        choiceSearch.getItems().addAll("Matricule","Marque","Status","Type");
        //initializing the value types for each column
        matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        marque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        kilometer.setCellValueFactory(new PropertyValueFactory<>("kilometer"));
        KilometerRestant.setCellValueFactory(new PropertyValueFactory<>("nbrKilometerRestant"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        type.setCellValueFactory(new PropertyValueFactory<>("vehiculeType"));
        dateAssurance.setCellValueFactory(new PropertyValueFactory<>("dateAssurance"));
        prixAssurance.setCellValueFactory(new PropertyValueFactory<>("prixAssurance"));

        VehiculeServices vehiculeServices = new VehiculeServices();
        //getting the Data from the DB to a list
        ObservableList<Vehicule> vehicules = FXCollections.observableArrayList(vehiculeServices.getAll());

        vehiculeTable.setItems(vehicules);//filling the table with the previous list

        //cosmetics.........
        vehiculeTable.setRowFactory(tv -> {
            TableRow<Vehicule> row = new TableRow<>() {
                @Override
                protected void updateItem(Vehicule vehicule, boolean empty) {
                    super.updateItem(vehicule, empty);
                    if (empty || vehicule == null) {
                        setStyle("");
                    } else {
                        if (vehicule.getStatus() == 0) {
                            setStyle("-fx-background-color: #FF7F7F;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
            //******************************* mte3 insaf
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    Vehicule clickedVehicule = row.getItem();
                    //*************** ikteb lehna
                    System.out.println("Clicked on: " + clickedVehicule);
                }
            });
            //**************************
            return row;
        });

        vehiculeTable.refresh();

        choiceType.setItems(FXCollections.observableArrayList(VehiculeType.values()));
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
    @FXML
    private void ajoutVoiture(ActionEvent event) {
        hideErrors();
        try {
            errorAjout.setVisible(false);

            VehiculeServices vehiculeServices = new VehiculeServices();
            Vehicule vehicule = new Vehicule();
            //data checks....
            //************************
            //storing the values to an object then to the DB
            if(testInputs()) {
                vehicule.setMatricule(Integer.parseInt(fieldMatricule1.getText()) * 10000 + Integer.parseInt(fieldMatricule2.getText()));
                System.out.println(vehicule.getMatricule());
                vehicule.setKilometer(Float.parseFloat(fieldKilometer.getText()));
                System.out.println(vehicule.getKilometer());
                vehicule.setDateAssurance(datePickerAssuranceDate.getValue());
                System.out.println(vehicule.getDateAssurance());
                vehicule.setPrixAssurance(Float.parseFloat(fieldPrixAssurance.getText()));
                System.out.println(vehicule.getPrixAssurance());
                vehicule.setVehiculeType(VehiculeType.valueOf(choiceType.getSelectionModel().getSelectedItem().toString()));
                System.out.println(vehicule.getVehiculeType());
                vehicule.setNbrKilometerRestant(Float.parseFloat(fieldKilometerRestant.getText()));
                System.out.println(vehicule.getNbrKilometerRestant());
                vehicule.setMarque(fieldMarque.getText());
                System.out.println(vehicule.getMarque());

                vehicule.setStatus((vehicule.getNbrKilometerRestant() <= 0) ? 0 : 1);

                if (vehiculeServices.create(vehicule)) {
                    //if added successfully then the table refreshes with the new added object
                    ObservableList<Vehicule> vehicules = vehiculeTable.getItems();
                    vehicules.add(vehicule);
                    vehiculeTable.setItems(vehicules);
                    vehiculeTable.refresh();
                    resetInputs();
                } else {
                    throw new Exception();
                }
            }



        }catch (ExistingVehiculeMatriculeException | InvalideInputTypeException | MissingDataException e) {
            errorAjout.setText(e.getMessage());
            errorAjout.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    private void hideErrors(){
        errorAjout.setVisible(false);
        errorMatricule.setVisible(false);
        errorMarque.setVisible(false);
        errorKilometrage.setVisible(false);
        errorKilometerRestant.setVisible(false);
        errorDateAssurance.setVisible(false);
        errorPrixAssurance.setVisible(false);
        errorType.setVisible(false);
    }
    private boolean testInputs(){
        boolean test=true;
            if(!InputCheckServices.isValidMatricule(fieldMatricule1.getText()+"TUN"+fieldMatricule2.getText(),errorMatricule)) {
                test=false;
            }
            if (!InputCheckServices.isValidname(marque.getText(),errorMarque)){
                test=false;
            }
            if (!InputCheckServices.isValidKilometre(fieldKilometer.getText(),errorKilometrage)){
                test=false;
            }
            if (!InputCheckServices.isValidKilometre(fieldKilometerRestant.getText(),errorKilometerRestant)){
                test=false;
            }
            if (!InputCheckServices.isValidDate(datePickerAssuranceDate.getValue(),errorDateAssurance)){
                test=false;
            }
            if (!InputCheckServices.isValidPrice(fieldPrixAssurance.getText(),errorPrixAssurance)){
                test=false;
            }
            return test;
    }
    private void resetInputs(){
        fieldMatricule1.setText("");
        fieldMatricule2.setText("");
        fieldKilometer.setText("");
        fieldKilometerRestant.setText("");
        fieldPrixAssurance.setText("");
        fieldMarque.setText("");
        datePickerAssuranceDate.setValue(null);
        choiceType.setValue(null);
    }

    //************** the methods for the "right click" menu *******************

    //1)delete
    @FXML
    private void vehiculeDeleteOption(ActionEvent event) {
        Vehicule vehicule=vehiculeTable.getSelectionModel().getSelectedItem();
        if(vehicule!=null) {

            //initialize alert and pops up a confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("supprimer la vehicule :"+vehicule.getMatricule()+" ?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get()==ButtonType.OK) {
                alert.close();
                VehiculeServices vehiculeServices = new VehiculeServices();
                vehiculeServices.delete(vehicule.getMatricule());
                vehiculeTable.getItems().remove(vehicule);
                vehiculeTable.setItems(vehiculeTable.getItems());
                vehiculeTable.refresh();
            }else {
                alert.close();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Rien est selectionné");
        }
    }
    //2)edit
    @FXML
    private void vehiculeEditOption(ActionEvent event) {
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
        Vehicule selectedVehicule=vehiculeTable.getSelectionModel().getSelectedItem();
        if (selectedVehicule != null) {
        fieldMatricule1.setText(String.valueOf(selectedVehicule.getMatricule()/10000));
        fieldMatricule2.setText(String.valueOf(selectedVehicule.getMatricule()%10000));
        fieldMarque.setText(selectedVehicule.getMarque());
        fieldKilometer.setText(String.valueOf(selectedVehicule.getKilometer()));
        fieldKilometerRestant.setText(String.valueOf(selectedVehicule.getNbrKilometerRestant()));
        fieldPrixAssurance.setText(String.valueOf(selectedVehicule.getPrixAssurance()));
        choiceType.setValue(selectedVehicule.getVehiculeType());
        datePickerAssuranceDate.setValue(selectedVehicule.getDateAssurance());

            // Afficher un message pour confirmer la modification
            errorAjout.setText("Modification du ingenieur en cours...");

        } else {
            errorAjout.setText("Veuillez sélectionner un ingenieur !");
        }
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
    }

    //3)historique maintenance
    @FXML
    private void vehiculeMaintenanceOption(ActionEvent event) throws IOException{
        Vehicule selectedVehicule =vehiculeTable.getSelectionModel().getSelectedItem();

        //clearing the table
        //***
        vehiculeTable.getSelectionModel().clearSelection();
        vehiculeTable.setItems(FXCollections.observableArrayList());
        //***

        MaintenanceSceneController.setVehicule(selectedVehicule);
        App.setRoot("maintenanceMenu");
    }
    @FXML
    private void annulerEdit(ActionEvent event) {
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        btannuler.setVisible(false);
        resetInputs();

    }
    private void loadVehiculeTable() {
        VehiculeServices vehiculeServices = new VehiculeServices();
        List<Vehicule> vehicules = vehiculeServices.getAll();
        ObservableList<Vehicule> vehiculeObservableList = FXCollections.observableArrayList(vehicules);
        vehiculeTable.setItems(vehiculeObservableList);
        vehiculeTable.refresh();
    }
    @FXML
    public void modifierMaintenance(ActionEvent event) {
        try {

            Vehicule selectedVehicule =vehiculeTable.getSelectionModel().getSelectedItem();
            if (selectedVehicule == null) {
                errorAjout.setText("Veuillez d'abord sélectionner maintenance !");
                return;
            }
            Vehicule vehicule=new Vehicule(
                    selectedVehicule.getMatricule(),
                    fieldMarque.getText(),
                    datePickerAssuranceDate.getValue(),
                    Float.parseFloat(fieldPrixAssurance.getText()),
                    Float.parseFloat(fieldKilometer.getText()),
                    Float.parseFloat(fieldKilometerRestant.getText()),
                    1,
                    choiceType.getValue(),
                    null,
                    null
            );

            // Mettre à jour dans la base de données
            VehiculeServices vehiculeServices = new VehiculeServices();
            if (testInputs()) {
                if (vehiculeServices.update(selectedVehicule.getMatricule(), vehicule)) {
                    errorAjout.setText("maintenance modifié avec succès !");
                    loadVehiculeTable(); // Rafraîchir la table
                } else {
                    errorAjout.setText("Échec de la modification.");
                }
            }
        } catch (Exception e) {
            errorAjout.setText("Erreur : " + e.getMessage());
        }
        annulerEdit(event);
        loadVehiculeTable();
    }

    //**************************************************************************


    @FXML
    private void resetTable() {
        VehiculeServices vehiculeServices = new VehiculeServices();
        ObservableList<Vehicule> vehicules = FXCollections.observableArrayList(vehiculeServices.getAll());
        vehiculeTable.setItems(vehicules);
        vehiculeTable.refresh();
    }


    //used for searching vehicules by the specific column selected in the choicelist
    @FXML
    private void searchByChoice(ActionEvent event){
            resetTable();
            switch(choiceSearch.getValue()){
                case "Matricule":
                    if (fieldSearch.getText() == null || fieldSearch.getText().trim().isEmpty()||choiceSearch.getValue().isEmpty()) {
                        resetTable();
                        System.out.println("Search field is empty.");
                        break;
                    }

                    try {
                        int matricule = Integer.parseInt(fieldSearch.getText().trim());
                        vehiculeTable.setItems(
                                vehiculeTable.getItems().filtered(v -> v.getMatricule() == matricule)
                        );
                        vehiculeTable.refresh();
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid matricule: " + fieldSearch.getText());
                    }
                    break;
                case "Marque":
                    if (fieldSearch.getText() == null || fieldSearch.getText().trim().isEmpty()||choiceSearch.getValue().isEmpty()) {
                        resetTable();
                        System.out.println("Search field is empty.");
                        break;
                    }

                    try {
                        String marque = fieldSearch.getText().trim().toLowerCase();
                        vehiculeTable.setItems(
                                vehiculeTable.getItems().filtered(v -> v.getMarque().toLowerCase().contains(marque))
                        );
                        vehiculeTable.refresh();
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid marque: " + fieldSearch.getText());
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
                        vehiculeTable.setItems(
                                vehiculeTable.getItems().filtered(v -> v.getVehiculeType().toString().toLowerCase().contains(type))
                        );
                        vehiculeTable.refresh();
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid type: " + fieldSearch.getText());
                    }
                    break;
                case "Status":
                    if (fieldSearch.getText() == null || fieldSearch.getText().trim().isEmpty()||choiceSearch.getValue().isEmpty()) {
                        resetTable();
                        System.out.println("Search field is empty.");
                        break;
                    }

                    try {
                        int status = Integer.parseInt(fieldSearch.getText().trim());
                        vehiculeTable.setItems(
                                vehiculeTable.getItems().filtered(v -> v.getStatus() == status)
                        );
                        vehiculeTable.refresh();
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid status: " + fieldSearch.getText());
                    }
                    break;
            }
        }
    @FXML
    private void switchToCondidat(ActionEvent event) throws IOException {
        vehiculeTable.getSelectionModel().clearSelection();
        vehiculeTable.setItems(FXCollections.observableArrayList());
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
    private void switchToCalendar() throws IOException{
        App.setRoot("calender");
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
}
