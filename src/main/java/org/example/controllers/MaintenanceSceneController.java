package org.example.controllers;

import com.sun.tools.javac.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.App;
import org.example.entities.Employee;
import org.example.entities.Facture;
import org.example.entities.Maintenance;
import org.example.entities.Vehicule;
import org.example.entities.exceptions.ExistingVehiculeMatriculeException;
import org.example.entities.exceptions.InvalideInputTypeException;
import org.example.entities.exceptions.MissingDataException;
import org.example.service.FactureDAO;
import org.example.service.MaintenanceServices;
import org.example.service.verification.InputCheckServices;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class MaintenanceSceneController {
    @FXML
    private Label errorDescription;
    @FXML
    private Label errorDate;
    @FXML
    private Label errorPrix;
    @FXML
    private Button btmodifier;
    @FXML
    private Button btajouter;
    @FXML
    private Button btannuler;
    private static Vehicule vehicule;
    @FXML
    private TableView<Maintenance> maintenanceTable;
    @FXML
    private TableColumn<Maintenance, String> maintenanceId;
    @FXML
    private TableColumn<Maintenance, String> maintenanceDescription;
    @FXML
    private TableColumn<Maintenance, LocalDate> maintenanceDate;
    @FXML
    private TableColumn<Maintenance, String> maintenancePrice;
    @FXML
    private Label vehiculeLabel;
    @FXML
    private ChoiceBox<String> choiceSearch;
    @FXML
    private TextField fieldSearch;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label errorLabel;
    @FXML
    private CheckBox isServiced;

    @FXML
    private void initialize() {
        hideErrors();
        btannuler.setVisible(false);
        btmodifier.setVisible(false);
        choiceSearch.getItems().addAll("Description");

        //************ initializing the table ************
        maintenanceDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        maintenanceDate.setCellValueFactory(new PropertyValueFactory<>("dateMaintenance"));
        maintenancePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        MaintenanceServices maintenanceServices = new MaintenanceServices();
        ObservableList<Maintenance> maintenances= FXCollections.observableArrayList(maintenanceServices.getAll());

        //stream() ;)
        maintenanceTable.setItems(FXCollections.observableArrayList(maintenances.stream()
                .filter(maintenance -> maintenance.getMatricule().equals(vehicule.getMatricule()))
                .collect(Collectors.toList())
        ));

        maintenanceTable.setRowFactory(tv -> new TableRow<Maintenance>() {
            @Override
            protected void updateItem(Maintenance maintenance, boolean empty) {
                super.updateItem(maintenance, empty);
                if (empty || maintenance == null) {
                    setStyle(""); // Reset style for empty rows
                } else {
                    Float price = maintenance.getPrice();
                    if (price == null || price == 0F || maintenance.getDateMaintenance() == null) {
                        setStyle("-fx-background-color: #FF7F7F;"); // Light red
                    } else {
                        setStyle("-fx-background-color: lightgreen;"); // Light green
                    }
                }
            }
        });

        maintenanceDate.setCellFactory(column -> new TableCell<Maintenance, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Maintenance maintenance = getTableRow().getItem();
                    if (maintenance.getDateMaintenance() == null || maintenance.getPrice() == 0) {
                        setText("Not serviced yet");
                    } else {
                        setText(date.toString());
                    }
                }
            }
        });
        System.out.println(vehicule+"mmmmmmmmmmmmmmmm");

        maintenanceTable.refresh();
        vehiculeLabel.setText("Matricule: "+vehicule.getMatricule()+
                "\nMarque: "+vehicule.getMarque()+
                "\nType: "+vehicule.getVehiculeType().toString());
        vehiculeLabel.setStyle("-fx-text-alignment: center;-fx-border-color: black;-fx-border-radius: 10px ;-fx-border-width: 5px");
        vehiculeLabel.setVisible(true);

        //**********Check box setup********************

        isServiced.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean disabled = !newValue; // Fields are disabled when the checkbox is UNCHECKED
            priceField.setDisable(disabled);
            datePicker.setDisable(disabled);

            // Change opacity for visual effect
            double opacity = disabled ? 0.5 : 1;
            priceField.setStyle("-fx-opacity: " + opacity + ";");
            datePicker.setStyle("-fx-opacity: " + opacity + ";");
        });
        // Initialize the state right after the listener, to handle the default case
        boolean initialDisabled = !isServiced.isSelected();
        priceField.setDisable(initialDisabled);
        datePicker.setDisable(initialDisabled);
        priceField.setStyle("-fx-opacity: " + (initialDisabled ? 0.5 : 1) + ";");
        datePicker.setStyle("-fx-opacity: " + (initialDisabled ? 0.5 : 1) + ";");
    }
    @FXML
    private void switchToVehicule(ActionEvent event) throws IOException {
        maintenanceTable.getSelectionModel().clearSelection();
        maintenanceTable.setItems(FXCollections.observableArrayList());
        App.setRoot("vehiculeMenu");
    }
    public static void setVehicule(Vehicule v) {
        vehicule=v;
    }
    //used for searching Maintenance by the specific column selected in the choicelist
    @FXML
    private void searchByChoice(ActionEvent event) {
        resetTable();

        if (fieldSearch.getText()==null|| fieldSearch.getText().isEmpty() || choiceSearch.getValue().isEmpty()) {
            resetTable();
            System.out.println("Search field is empty.");
            return;
        }

        switch (choiceSearch.getValue()) {
            case "Description":
                String description = fieldSearch.getText().toLowerCase();
                maintenanceTable.setItems(
                        maintenanceTable.getItems().filtered(d -> d.getDescription().toLowerCase().contains(description)) // Allows partial matching
                );
                maintenanceTable.refresh();
                break;

            default:
                System.out.println("Invalid search criteria.");
                break;
        }
    }
    @FXML
    private void addMaintenance(ActionEvent event) {
        hideErrors();
        try {
            errorLabel.setVisible(false);

            MaintenanceServices maintenanceServices=new MaintenanceServices();
            Maintenance maintenance=new Maintenance();
            //data checks....
            if(checkInputs()) {
                //************************
                //storing the values to an object then to the DB
                System.out.println("checks done");
                maintenance.setMatricule(vehicule.getMatricule());
                System.out.println(maintenance.getMatricule());
                maintenance.setDateMaintenance(datePicker.getValue());
                maintenance.setPrice((!priceField.getText().isEmpty()) ? Float.parseFloat(priceField.getText()) : null);
                maintenance.setDescription(descriptionField.getText());
                System.out.println(maintenance);
                if (maintenanceServices.create(maintenance)) {
                    errorLabel.setText("ajouter avec succes");
                    //if added successfully then the table refreshes with the new added object
                    if (isServiced.isSelected() && maintenance.getPrice() != null && maintenance.getPrice() > 0) {
                        createFactureForMaintenance(maintenance);
                    }
                    hideErrors();
                    ObservableList<Maintenance> maintenances = maintenanceTable.getItems();
                    maintenances.add(maintenance);
                    maintenanceTable.setItems(maintenances);
                    maintenanceTable.refresh();
                    hideErrors();
                    isServiced.setSelected(false);
                    priceField.clear();
                    datePicker.setValue(null);
                    descriptionField.clear();

                } else {
                    throw new Exception();
                }
            }

            //if vehicule.km ==0 .....

        }catch (ExistingVehiculeMatriculeException | InvalideInputTypeException | MissingDataException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void resetTable() {
        MaintenanceServices maintenanceServices=new MaintenanceServices();
        ObservableList<Maintenance> maintenances = FXCollections.observableArrayList(maintenanceServices.getAll());
        maintenanceTable.setItems(FXCollections.observableArrayList(maintenances.stream()
                .filter(maintenance -> maintenance.getMatricule().equals(vehicule.getMatricule()))
                .collect(Collectors.toList())
        ));
        maintenanceTable.refresh();
    }
    //************** the methods for the "right click" menu *******************

    //1)delete
    @FXML
    private void maintenanceDeleteOption(ActionEvent event) {
        Maintenance maintenance=maintenanceTable.getSelectionModel().getSelectedItem();
        if(maintenance!=null) {

            //initialize alert and pops up a confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("supprimer maintenance: "+maintenance.getDescription()+" ?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get()==ButtonType.OK) {
                alert.close();
                MaintenanceServices maintenanceServices=new MaintenanceServices();
                maintenanceServices.delete(maintenance.getId());
                maintenanceTable.getItems().remove(maintenance);
                maintenanceTable.setItems(maintenanceTable.getItems());
                maintenanceTable.refresh();
            }else {
                alert.close();
            }
        }else{

            errorLabel.setText("Rien est selectionné");
        }
    }
    //2)edit
    @FXML
    private void maintenanceEditOption(ActionEvent event) {
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
        isServiced.setSelected(true);
        Maintenance selectedItem = maintenanceTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Remplir les champs avec les informations du Maintenance sélectionné
            priceField.setText(selectedItem.getPrice().toString());
            descriptionField.setText(selectedItem.getDescription());
            datePicker.setValue(selectedItem.getDateMaintenance());

            // Afficher un message pour confirmer la modification
            errorLabel.setText("Modification du maintenance en cours...");

        } else {
            errorLabel.setText("Veuillez sélectionner  !");
        }
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
    }
    @FXML
    public void modifierMaintenance(ActionEvent event) {
        try {

            Maintenance selectedMaintenance=maintenanceTable.getSelectionModel().getSelectedItem();
            if (selectedMaintenance == null) {
                errorLabel.setText("Veuillez d'abord sélectionner maintenance !");
                return;
            }

            // Créer un objet mis à jour
            Maintenance maintenance=new Maintenance(
                    selectedMaintenance.getId(),
                    selectedMaintenance.getMatricule(),
                    descriptionField.getText(),
                    datePicker.getValue(),
                    Float.parseFloat(priceField.getText())
            );

            // Mettre à jour dans la base de données
            MaintenanceServices maintenanceServices=new MaintenanceServices();
            if (maintenanceServices.update(selectedMaintenance.getId(),maintenance)) {
                errorLabel.setText("maintenance modifié avec succès !");
                loadMaintenance(); // Rafraîchir la table
            } else {
                errorLabel.setText("Échec de la modification.");
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur : " + e.getMessage());
        }
        annulerEdit(event);
        loadMaintenance();
    }
    private void createFactureForMaintenance(Maintenance maintenance) {
        try {
            // Création d'un objet Facture
            Facture facture = new Facture();
            facture.setNumeroFacture("MAINT-" + maintenance.getMatricule() + "-" + maintenance.getId());
            facture.setDateFacture(maintenance.getDateMaintenance());
            facture.setDescription("Maintenance véhicule: " + vehicule.getMarque() + " (" + vehicule.getMatricule() + ") - " + maintenance.getDescription());
            facture.setMontant(maintenance.getPrice());
            facture.setCategorie("Maintenance");
            facture.setStatut("Payée"); // On suppose que la maintenance est payée immédiatement
            facture.setDatePaiement(maintenance.getDateMaintenance());

            // Création du répertoire pour les fichiers du véhicule si nécessaire
            String vehicleDir = "data/vehicles/" + vehicule.getMatricule();
            File dir = new File(vehicleDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String receiptPath = vehicleDir + "/maintenance_" + maintenance.getId() + "_receipt.pdf";
            facture.setCheminScan(receiptPath);

            FactureDAO factureDAO = new FactureDAO();
            if (factureDAO.create(facture)) {
                System.out.println("Facture créée automatiquement pour la maintenance: " + facture.getNumeroFacture());
            } else {
                System.err.println("Erreur lors de la création de la facture pour la maintenance");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la création de la facture: " + e.getMessage());
        }
    }
    @FXML
    private void annulerEdit(ActionEvent event) {
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        btannuler.setVisible(false);
        priceField.clear();
        descriptionField.clear();
        isServiced.setSelected(false);
        datePicker.setValue(null);

    }

    //**********************
    private boolean checkInputs(){
        boolean test=true;

        if (isServiced.isSelected()) {
            if (!InputCheckServices.isValidPrice(priceField.getText(),errorPrix)){
                test=false;
            }
            if (datePicker.getValue()==null){
                errorDate.setText("choisir une date");
                errorDate.setVisible(true);
                test=false;
            }
        }
        return test;
    }
    private void hideErrors(){
        errorDescription.setVisible(false);
        errorDate.setVisible(false);
        errorDescription.setVisible(false);
        errorPrix.setVisible(false);
    }
    private void loadMaintenance() {
        MaintenanceServices maintenanceServices=new MaintenanceServices();
        List<Maintenance> maintenances = maintenanceServices.getAll().stream().filter(maintenance -> maintenance.getMatricule().equals(vehicule.getMatricule()))
                .collect(Collectors.toList());
        ObservableList<Maintenance> data = FXCollections.observableArrayList(maintenances);
        maintenanceTable.setItems(data);
        maintenanceTable.refresh();
    }



}
