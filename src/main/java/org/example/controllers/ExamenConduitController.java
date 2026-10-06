package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.entities.*;
import org.example.entities.myEnums.VehiculeType;
import org.example.service.*;
import org.example.service.verification.TimeCheckServices;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExamenConduitController {
    private static Float locationX=null;
    private static Float locationY=null;
    private static String locationName=null;
    private static LocalDate selectedDate;
    private MapController mapController;

    @FXML
    private TableView<ExamenConduit> table;
    @FXML
    private TableColumn<ExamenConduit, Integer> cin;
    @FXML
    private TableColumn<ExamenConduit, VehiculeType> type;
    @FXML
    private TableColumn<ExamenConduit, LocalDateTime> date;
    @FXML
    private TableColumn<ExamenConduit, Duration> duree;
    @FXML
    private TableColumn<ExamenConduit, Integer> idMoniteur;
    @FXML
    private TableColumn<ExamenConduit, Integer> matricule;
    @FXML
    private TableColumn<ExamenConduit, Float> price;
    @FXML
    private TableColumn<ExamenConduit, String> locationColumn;
    @FXML
    private TableColumn<ExamenConduit, Boolean> status;
    @FXML
    private Label currentDay;
    @FXML
    private ChoiceBox<VehiculeType> typeChoice;
    @FXML
    private ChoiceBox<Candidat> condidatList;
    @FXML
    private ChoiceBox<Employee> moniteurList ;
    @FXML
    private ChoiceBox<Vehicule> vehiculeList ;
    @FXML
    private TextField hrTemp;
    @FXML
    private TextField minTemp;
    @FXML
    private TextField hrDuree;
    @FXML
    private TextField minDuree;
    @FXML
    private Label errorType;
    @FXML
    private Label errorTemp;
    @FXML
    private Label errorDuree;
    @FXML
    private Label errorMoniteur;
    @FXML
    private Label errorVehicule;
    @FXML
    private Label errorCondidat;
    @FXML
    private Label errorLocation;
    @FXML
    private Label errorPast;
    @FXML
    private Button locationButton;
    @FXML
    private Button btajouter;
    @FXML
    private Button btannuler;
    @FXML
    private Button btmodifier;
    @FXML
    private Label locationLabel;
    @FXML
    private RadioButton isSuccess;
    @FXML
    private void initialize() {
        System.out.println(locationName);
        System.out.println(locationX);
        System.out.println(locationY);
        btajouter.setVisible(true);
        btannuler.setVisible(false);
        btmodifier.setVisible(false);
        isSuccess.setVisible(false);
        condidatList.setDisable(true);
        moniteurList.setDisable(true);
        vehiculeList.setDisable(true);
        hideErrors();
        if (selectedDate.isBefore(LocalDate.now())) {
            disableInputs();
            errorPast.setText("vous ne pouvez pas\najouter un événement\ndans le passé!!!");
            errorPast.setVisible(true);
        }
        typeChoice.getItems().addAll(VehiculeType.values());
        typeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                condidatList.setDisable(false);
                moniteurList.setDisable(false);
                vehiculeList.setDisable(false);
                updateLists(newVal);
            }
        });

        CandidatService candidatService=new CandidatService();
        condidatList.getItems().addAll(candidatService.getAll());

        VehiculeServices vehiculeServices=new VehiculeServices();
        vehiculeList.getItems().addAll(vehiculeServices.getAll());

        MoniteurService moniteurService=new MoniteurService();
        moniteurList.getItems().addAll(moniteurService.getAll());

        currentDay.setText(selectedDate.getYear()+" "+selectedDate.getMonth()+" "+selectedDate.getDayOfMonth());
        System.out.println(selectedDate);
        cin.setCellValueFactory(new PropertyValueFactory<ExamenConduit, Integer>("idCandidat"));
        type.setCellValueFactory(new PropertyValueFactory<ExamenConduit, VehiculeType>("vehiculeType"));
        date.setCellValueFactory(new PropertyValueFactory<ExamenConduit, LocalDateTime>("dateExamen"));
        duree.setCellValueFactory(new PropertyValueFactory<ExamenConduit, Duration>("durationExamen"));
        idMoniteur.setCellValueFactory(new PropertyValueFactory<ExamenConduit, Integer>("idMoniteur"));
        price.setCellValueFactory(new PropertyValueFactory<ExamenConduit, Float>("prix"));
        matricule.setCellValueFactory(new PropertyValueFactory<ExamenConduit, Integer>("matriculeVehicule"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<ExamenConduit, String>("locationName"));
        status.setCellValueFactory(new PropertyValueFactory<ExamenConduit, Boolean>("status"));


        ExamenConduiteServices service = new ExamenConduiteServices();
        ObservableList<ExamenConduit> examenConduits= FXCollections.observableArrayList(service.getAll());
        table.setItems(examenConduits.stream()
                .filter(sc -> sc.getDateExamen().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );

        table.setRowFactory(tv -> {
            TableRow<ExamenConduit> row = new TableRow<>() {
                @Override
                protected void updateItem(ExamenConduit sc, boolean empty) {
                    VehiculeServices vehiculeServices1=new VehiculeServices();
                    super.updateItem(sc, empty);
                    if (empty || sc == null) {
                        setStyle("");
                    } else {
                        if (vehiculeServices1.getById(sc.getMatriculeVehicule()).getStatus()==0) {
                            setStyle("-fx-background-color: #e8d20c;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
            return row;
        });

        table.refresh();
    }
    private List<SceanceConduite> seancesConduiteToday() {
        SceanceConduiteServices sceanceConduiteServices = new SceanceConduiteServices();
        return sceanceConduiteServices.getAll().stream()
                .filter(sc -> sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
    }
    private void updateLists(VehiculeType selectedType) {
        CandidatService candidatService = new CandidatService();
        MoniteurService moniteurService = new MoniteurService();
        VehiculeServices vehiculeServices = new VehiculeServices();

        // Filter and update condidatList
        condidatList.getItems().setAll(
                candidatService.getAll().stream()
                        .filter(c -> c.getTypePermis() == selectedType)
                        .collect(Collectors.toList())
        );

        // Filter and update ingenieurList
        moniteurList.getItems().setAll(
                moniteurService.getAll().stream()
                        .filter(i -> i.getVehiculeType() == selectedType)
                        .collect(Collectors.toList())
        );
        vehiculeList.getItems().setAll(
                vehiculeServices.getAll().stream()
                        .filter(v->v.getVehiculeType()==selectedType && v.getStatus()==1)
                        .collect(Collectors.toList())
        );
    }

    private List<SceanceCode> seancesCodeToday() {
        SceanceCodeServices sceanceCodeServices = new SceanceCodeServices();
        return sceanceCodeServices.getAll().stream()
                .filter(sc -> sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
    }

    private List<ExamenConduit> examenConduitToday() {
        ExamenConduiteServices examenConduiteServices = new ExamenConduiteServices();
        return examenConduiteServices.getAll().stream()
                .filter(ex -> ex.getDateExamen().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
    }

    private List<ExamenCode> examenCodeToday() {
        ExamenCodeServices examenCodeServices = new ExamenCodeServices();
        return examenCodeServices.getAll().stream()
                .filter(ex -> ex.getDateExamen().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
    }
    @FXML
    private void annulerEdit(ActionEvent event) {
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        btannuler.setVisible(false);
        isSuccess.setVisible(false);
        resetInputs();
        locationX=null;
        locationY=null;
        locationName=null;
        hideErrors();
    }
    private void disableInputs(){
        typeChoice.setDisable(true);
        condidatList.setDisable(true);
        moniteurList.setDisable(true);
        vehiculeList.setDisable(true);
        hrTemp.setDisable(true);
        minTemp.setDisable(true);
        hrDuree.setDisable(true);
        minDuree.setDisable(true);
        locationButton.setDisable(true);
        btajouter.setDisable(true);
        isSuccess.setDisable(true);

    }
    private void resetInputs(){
        typeChoice.getItems().clear();
        condidatList.setValue(null);
        moniteurList.setValue(null);
        vehiculeList.setValue(null);
        hrTemp.setText("");
        minTemp.setText("");
        hrDuree.setText("");
        minDuree.setText("");
        locationLabel.setVisible(false);
    }
    private void hideErrors(){
        errorType.setVisible(false);
        errorTemp.setVisible(false);
        errorDuree.setVisible(false);
        errorMoniteur.setVisible(false);
        errorVehicule.setVisible(false);
        errorCondidat.setVisible(false);
        errorPast.setVisible(false);
        errorLocation.setVisible(false);
    }
    private boolean testInputs() {
        boolean result = true;

        // Check if type is selected.
        if (typeChoice.getSelectionModel().getSelectedItem() == null) {
            result = false;
            errorType.setText("Il faut spécifier le type");
            errorType.setVisible(true);
        }

        // Validate time input: hours and minutes.
        String hrTempStr = hrTemp.getText().trim();
        String minTempStr = minTemp.getText().trim();
        if (!hrTempStr.matches("\\d+") || !minTempStr.matches("\\d+") || Integer.parseInt(hrTempStr) < 0 || Integer.parseInt(minTempStr) < 0 || Integer.parseInt(hrTempStr) > 24 || Integer.parseInt(minTempStr) >= 60) {
            result = false;
            errorTemp.setText("Temps invalide");
            errorTemp.setVisible(true);
        }

        // Validate duration input: hours and minutes.
        String hrDureeStr = hrDuree.getText().trim();
        String minDureeStr = minDuree.getText().trim();
        if (!hrDureeStr.matches("\\d+") || !minDureeStr.matches("\\d+")) {
            result = false;
            errorDuree.setText("Duree invalide");
            errorDuree.setVisible(true);
        }

        // If time and duration are valid, parse them and perform checks.
        try {
            int hour = Integer.parseInt(hrTempStr);
            int minute = Integer.parseInt(minTempStr);
            int durationHours = Integer.parseInt(hrDureeStr);
            int durationMinutes = Integer.parseInt(minDureeStr);

            LocalDateTime sessionStart = LocalDateTime.of(
                    selectedDate.getYear(),
                    selectedDate.getMonth(),
                    selectedDate.getDayOfMonth(),
                    hour,
                    minute
            );
            Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

            // Check if the session is within working time.
            if (!TimeCheckServices.isWorkingTime(sessionStart, sessionDuration)) {
                result = false;
                errorTemp.setText("La séance doit être entre 8:00 et 15:00, hors dimanche");
                errorTemp.setVisible(true);
            }
        } catch (NumberFormatException e) {
            result = false;
            // Error labels have been set already.
        } catch (RuntimeException e) {
            result = false;
        }

        // Check Moniteur availability.
        try {
            if (moniteurList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

                if (!TimeCheckServices.isMoniteurSeanceValable(
                        seancesConduiteToday(),
                        moniteurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorMoniteur
                ) || !TimeCheckServices.isMoniteurExamenValable(
                        examenConduitToday(),
                        moniteurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorMoniteur
                )) {
                    result = false;
                }
            } else {
                result = false;
                errorMoniteur.setText("Il faut choisir un moniteur");
                errorMoniteur.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }

        // Check Candidat availability.
        try {
            if (condidatList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

                List<? extends Sceance> combinedSceances = Stream.concat(
                        seancesConduiteToday().stream(),
                        seancesCodeToday().stream()
                ).collect(Collectors.toList());

                List<? extends Examen> combinedExamens = Stream.concat(
                        examenConduitToday().stream(),
                        examenCodeToday().stream()
                ).collect(Collectors.toList());

                if (!TimeCheckServices.isCandidatSeanceValable(
                        combinedSceances,
                        condidatList.getValue().getCIN(),
                        sessionStart,
                        sessionDuration,
                        errorCondidat
                ) || !TimeCheckServices.isCandidatExamenValable(
                        combinedExamens,
                        condidatList.getValue().getCIN(),
                        sessionStart,
                        sessionDuration,
                        errorCondidat
                )) {
                    result = false;
                }
            } else {
                result = false;
                errorCondidat.setText("Il faut choisir un candidat");
                errorCondidat.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }

        // Check Vehicule availability.
        try {
            if (vehiculeList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

                if (!TimeCheckServices.isVehiculeSeanceAvailable(
                        seancesConduiteToday(),
                        vehiculeList.getValue().getMatricule(),
                        sessionStart,
                        sessionDuration,
                        errorVehicule
                ) || !TimeCheckServices.isVehiculeExamenAvailable(
                        examenConduitToday(),
                        vehiculeList.getValue().getMatricule(),
                        sessionStart,
                        sessionDuration,
                        errorVehicule
                )) {
                    result = false;
                }
            } else {
                result = false;
                errorVehicule.setText("Il faut choisir un véhicule");
                errorVehicule.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }
        if(locationX==null||locationY==null||locationName==null){
            errorLocation.setText("Il faut choisir une localisation");
            errorLocation.setVisible(true);
            result = false;
        }else {
            locationLabel.setText(locationName);
            locationLabel.setVisible(true);
        }

        return result;
    }
    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }
    @FXML
    private void chooseLocation(ActionEvent event) throws IOException {
        // Load the map FXML
        FXMLLoader loader = new FXMLLoader(App.class.getResource("map.fxml"));
        Parent mapRoot = loader.load();

        // Get the controller
        MapController mapController = loader.getController();
        this.setMapController(mapController);

        // Create a new stage for the map
        Stage mapStage = new Stage();
        mapStage.setTitle("Select Location");
        mapStage.initModality(Modality.APPLICATION_MODAL); // Makes it modal (blocks input to other windows)
        mapStage.setScene(new Scene(mapRoot));

        // Show the map and wait for it to close
        mapStage.showAndWait();

        // After map closes, update location label if location was selected
        if (locationX != null && locationY != null && locationName != null) {
            locationLabel.setText(locationName);
            locationLabel.setVisible(true);
        }
    }
    private boolean testInputsUpdate(ExamenConduit selectedExam) {
        hideErrors();
        boolean result = true;
        if (typeChoice.getSelectionModel().getSelectedItem() == null) {
            result = false;
            errorType.setText("Il faut spécifier le type");
            errorType.setVisible(true);
        }

        String hrTempStr = hrTemp.getText().trim();
        String minTempStr = minTemp.getText().trim();
        if (!hrTempStr.matches("\\d+") || !minTempStr.matches("\\d+") || Integer.parseInt(hrTempStr)<0 || Integer.parseInt(minTempStr)<0 || Integer.parseInt(hrTempStr)>24 || Integer.parseInt(minTempStr)>=60) {
            result = false;
            errorTemp.setText("Temps invalide");
            errorTemp.setVisible(true);
        }

        String hrDureeStr = hrDuree.getText().trim();
        String minDureeStr = minDuree.getText().trim();
        if (!hrDureeStr.matches("\\d+") || !minDureeStr.matches("\\d+")) {
            result = false;
            errorDuree.setText("Duree invalide");
            errorDuree.setVisible(true);
        }

        try {
            int hour = Integer.parseInt(hrTempStr);
            int minute = Integer.parseInt(minTempStr);
            int durationHours = Integer.parseInt(hrDureeStr);
            int durationMinutes = Integer.parseInt(minDureeStr);

            LocalDateTime sessionStart = LocalDateTime.of(
                    selectedDate.getYear(),
                    selectedDate.getMonth(),
                    selectedDate.getDayOfMonth(),
                    hour,
                    minute
            );
            Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);
            // Check if the session is within working time.
            if (!TimeCheckServices.isWorkingTime(sessionStart, sessionDuration)) {
                result = false;
                errorTemp.setText("La séance doit être entre 8:00 et 15:00, hors dimanche");
                errorTemp.setVisible(true);
            }
        } catch (NumberFormatException e) {
            result = false;
            // Error labels have been set already.
        } catch (RuntimeException e) {
            result = false;
        }



        try {
            if (moniteurList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);

                // Get all seances except the one being updated
                List<ExamenConduit> filteredExamens = examenConduitToday().stream()
                        .filter(sceance -> !Objects.equals(sceance.getId(), selectedExam.getId()))
                        .collect(Collectors.toList());

                if (!TimeCheckServices.isMoniteurSeanceValable(
                        seancesConduiteToday(),
                        moniteurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorMoniteur
                ) || !TimeCheckServices.isMoniteurExamenValable(
                        filteredExamens,
                        moniteurList.getValue().getIdMoniteur(),
                        sessionStart,
                        sessionDuration,
                        errorMoniteur
                )) {
                    result = false;
                }
            } else {
                result = false;
                errorMoniteur.setText("Il faut choisir un moniteur");
                errorMoniteur.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }


        try {
            if (condidatList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);
                List<ExamenConduit> filteredExamens = examenConduitToday().stream()
                        .filter(sceanceConduite -> !Objects.equals(sceanceConduite.getId(), selectedExam.getId()))
                        .collect(Collectors.toList());
                List<? extends Sceance> combinedSceances = Stream.concat(
                        seancesConduiteToday().stream(),
                        seancesCodeToday().stream()
                ).collect(Collectors.toList());
                List<? extends Examen> combinedExamens =Stream.concat(
                        filteredExamens.stream(),
                        examenCodeToday().stream()
                ).collect(Collectors.toList());

                if (!TimeCheckServices.isCandidatSeanceValable(
                        combinedSceances,
                        condidatList.getValue().getCIN(),
                        sessionStart,
                        sessionDuration,
                        errorCondidat
                )||!TimeCheckServices.isCandidatExamenValable(
                        combinedExamens,
                        condidatList.getValue().getCIN(),
                        sessionStart,
                        sessionDuration,
                        errorCondidat
                )) {
                    result = false;
                }
            } else {
                result = false;
                errorCondidat.setText("Il faut choisir un candidat");
                errorCondidat.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }

        try {
            if (vehiculeList.getValue() != null) {
                int hour = Integer.parseInt(hrTempStr);
                int minute = Integer.parseInt(minTempStr);
                int durationHours = Integer.parseInt(hrDureeStr);
                int durationMinutes = Integer.parseInt(minDureeStr);

                LocalDateTime sessionStart = LocalDateTime.of(
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDayOfMonth(),
                        hour,
                        minute
                );
                Duration sessionDuration = Duration.ofMinutes(durationHours * 60L + durationMinutes);
                List<ExamenConduit> filteredExamens = examenConduitToday().stream()
                        .filter(sceanceConduite -> !Objects.equals(sceanceConduite.getId(), selectedExam.getId()))
                        .collect(Collectors.toList());
                if (!TimeCheckServices.isVehiculeSeanceAvailable(
                        seancesConduiteToday(),
                        vehiculeList.getValue().getMatricule(),
                        sessionStart,
                        sessionDuration,
                        errorVehicule
                )||!TimeCheckServices.isVehiculeExamenAvailable(
                        filteredExamens,
                        vehiculeList.getValue().getMatricule(),
                        sessionStart,
                        sessionDuration,
                        errorVehicule
                )) {
                    result = false;
                }
            } else {
                result = false;
                errorVehicule.setText("Il faut choisir une vehicule");
                errorVehicule.setVisible(true);
            }
        } catch (RuntimeException e) {
            result = false;
        }

        if(locationX==null||locationY==null||locationName==null){
            errorLocation.setText("Il faut choisir une localisation");
            errorLocation.setVisible(true);
            result = false;
        }else {
            locationLabel.setText(locationName);
            locationLabel.setVisible(true);
        }

        return result;
    }
    @FXML
    public void modifierExamen(ActionEvent event) {
        ExamenConduit examenConduit = table.getSelectionModel().getSelectedItem();
        System.out.println(examenConduit);
        if (testInputsUpdate(examenConduit)) {
            try {
                if (examenConduit == null) {
                    System.out.println("Veuillez d'abord sélectionner une séance de conduite !");
                    return;
                }
                TarifDAO tarifDAO = new TarifDAO();
                int prix = 0;
                if (typeChoice.getValue() == VehiculeType.CAMION) {
                    prix = tarifDAO.getTarifs().getDrivingSessionPricecamion();
                } else if (typeChoice.getValue() == VehiculeType.MOTO) {
                    prix = tarifDAO.getTarifs().getDrivingSessionPricemoto();
                } else if (typeChoice.getValue() == VehiculeType.VOITURE) {
                    prix = tarifDAO.getTarifs().getDrivingSessionPricevoiture();
                }

                ExamenConduit examenConduit1=new ExamenConduit();
                examenConduit1.setIdCandidat(condidatList.getValue().getCIN());
                examenConduit1.setVehiculeType(typeChoice.getValue());
                examenConduit1.setIdMoniteur(moniteurList.getValue().getIdMoniteur());
                examenConduit1.setPrix((float)prix);
                examenConduit1.setDateExamen(LocalDateTime.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDayOfMonth(), Integer.parseInt(hrTemp.getText()), Integer.parseInt(minTemp.getText())));
                examenConduit1.setDurationExamen(Duration.ofMinutes(Integer.parseInt(hrDuree.getText()) * 60 + Integer.parseInt(minDuree.getText())));
                examenConduit1.setMatriculeVehicule(vehiculeList.getValue().getMatricule());
                examenConduit1.setLocationX(locationX);
                examenConduit1.setLocationY(locationY);
                examenConduit1.setLocationName(locationName);
                examenConduit1.setStatus(isSuccess.isSelected());
                // Mettre à jour dans la base de données
                ExamenConduiteServices examenConduiteServices = new ExamenConduiteServices();
                if (examenConduiteServices.update(examenConduit.getId(), examenConduit1)) {
                    loadExamTable(); // Rafraîchir la table
                } else {
                    System.out.println("Échec de la modification.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            annulerEdit(event);
            loadExamTable();
        }
    }
    private void loadExamTable() {
        ExamenConduiteServices examenConduiteServices=new ExamenConduiteServices();
        List<ExamenConduit> examenConduits = examenConduiteServices.getAll().stream()
                .filter(sc -> sc.getDateExamen().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<ExamenConduit> data = FXCollections.observableArrayList(examenConduits);
        table.setItems(data);
        table.refresh();
    }

    public static void setLocationX(Float locationX) {
        ExamenConduitController.locationX = locationX;
    }

    public static void setLocationY(Float locationY) {
        ExamenConduitController.locationY = locationY;
    }

    public static void setLocationName(String locationName) {
        ExamenConduitController.locationName = locationName;
    }

    @FXML
    private void ajoutExConduit(ActionEvent event) {
        hideErrors();
        if (testInputs()){
            ExamenConduiteServices examenConduiteServices=new ExamenConduiteServices();
            ExamenConduit examenConduit=new ExamenConduit();
            examenConduit.setIdCandidat(condidatList.getValue().getCIN());
            examenConduit.setVehiculeType(typeChoice.getValue());
            examenConduit.setIdMoniteur(moniteurList.getValue().getIdMoniteur());
            TarifDAO tarifDAO = new TarifDAO();
            int prix = 0;
            if (typeChoice.getValue() == VehiculeType.CAMION) {
                prix = tarifDAO.getTarifs().getDrivingSessionPricecamion();
            } else if (typeChoice.getValue() == VehiculeType.MOTO) {
                prix = tarifDAO.getTarifs().getDrivingSessionPricemoto();
            } else if (typeChoice.getValue() == VehiculeType.VOITURE) {
                prix = tarifDAO.getTarifs().getDrivingSessionPricevoiture();
            }
            examenConduit.setPrix((float)prix);
            examenConduit.setDateExamen(LocalDateTime.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDayOfMonth(), Integer.parseInt(hrTemp.getText()), Integer.parseInt(minTemp.getText())));
            examenConduit.setDurationExamen(Duration.ofMinutes(Integer.parseInt(hrDuree.getText()) * 60 + Integer.parseInt(minDuree.getText())));
            examenConduit.setMatriculeVehicule(vehiculeList.getValue().getMatricule());
            examenConduit.setLocationX(locationX);
            examenConduit.setLocationY(locationY);
            examenConduit.setLocationName(locationName);
            examenConduit.setStatus(false);
            SceanceConduiteServices sceanceConduiteServices=new SceanceConduiteServices();
            ExamenCodeServices examenCodeServices=new ExamenCodeServices();
            if(sceanceConduiteServices.getLastSeanceDateTime(examenConduit.getIdCandidat(),3)!=null) {
                if(examenCodeServices.getExamensByCandidat(examenConduit.getIdCandidat()).stream().anyMatch(examenCode -> examenCode.getStatus())) {
                    if(sceanceConduiteServices.getLastSeanceDateTime(examenConduit.getIdCandidat(),3).toLocalDate().isBefore(selectedDate)) {
                        if (examenConduiteServices.create(examenConduit)) {
                            resetInputs();
                            hideErrors();
                            ObservableList<ExamenConduit> examenConduits = table.getItems();
                            examenConduits.add(examenConduit);
                            table.setItems(examenConduits);
                            table.refresh();
                            locationX = null;
                            locationY = null;
                            locationName = null;
                            btajouter.setVisible(true);
                            btannuler.setVisible(false);
                            btmodifier.setVisible(false);
                            isSuccess.setVisible(false);
                        } else {
                            System.out.println("Erreur ajoutScConduit");
                        }
                    }else{
                        errorCondidat.setText("la date d'examen pour ce candidat doit\netre au minimum le "+sceanceConduiteServices.getLastSeanceDateTime(examenConduit.getIdCandidat(),3).toLocalDate());
                    }
                }else {
                    errorCondidat.setText("ce condidat n'a pas reussite accun\n examen code");
                    errorCondidat.setVisible(true);
                }
            }else{
                errorCondidat.setText("Cet Candidat n'a pas atteind le\nnbr minimum de seance codes");
                errorCondidat.setVisible(true);
            }
        }
    }
    //************** the methods for the "right click" menu *******************

    //1)delete
    @FXML
    private void exCodeDeleteOption(ActionEvent event) {
        ExamenConduit examenConduit=table.getSelectionModel().getSelectedItem();
        if(examenConduit!=null) {

            //initialize alert and pops up a confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("supprimer examen :"+examenConduit.getId()+" ?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get()==ButtonType.OK) {
                alert.close();
                ExamenConduiteServices examenConduiteServices=new ExamenConduiteServices();
                examenConduiteServices.delete(examenConduit.getId());
                table.getItems().remove(examenConduit);
                table.setItems(table.getItems());
                table.refresh();
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
    private void examenEditOption(ActionEvent event) {
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
        isSuccess.setVisible(true);
        ExamenConduit examenConduit=table.getSelectionModel().getSelectedItem();
        CandidatService candidatService=new CandidatService();
        MoniteurService moniteurService=new MoniteurService();
        VehiculeServices vehiculeService=new VehiculeServices();
        if (examenConduit != null) {
            typeChoice.setValue(examenConduit.getVehiculeType());
            hrTemp.setText(String.valueOf(examenConduit.getDateExamen().getHour()));
            minTemp.setText(String.valueOf(examenConduit.getDateExamen().getMinute()));
            hrDuree.setText(String.valueOf(examenConduit.getDurationExamen().toMinutes()/60));
            minDuree.setText(String.valueOf(examenConduit.getDurationExamen().toMinutes()%60));
            condidatList.setValue(candidatService.getById(examenConduit.getIdCandidat()));
            moniteurList.setValue(moniteurService.getById(examenConduit.getIdMoniteur()));
            vehiculeList.setValue(vehiculeService.getById(examenConduit.getMatriculeVehicule()));
            locationX=examenConduit.getLocationX();
            locationY=examenConduit.getLocationY();
            locationName=examenConduit.getLocationName();
            locationLabel.setText(locationName);
            //***********
        } else {
            System.out.println("Veuillez sélectionner un ingenieur !");
        }
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
        isSuccess.setVisible(true);
    }
    //3)See location
    @FXML
    private void goToLocation(ActionEvent event) {
        ExamenConduit examenConduit = table.getSelectionModel().getSelectedItem();

        if (examenConduit == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Exam Selected");
            alert.setContentText("Please select a driving session to view its location.");
            alert.showAndWait();
            return;
        }

        Float x = examenConduit.getLocationX();
        Float y = examenConduit.getLocationY();
        String name = examenConduit.getLocationName();

        if (x == null || y == null || name == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Location Data");
            alert.setContentText("The selected session does not have valid location information.");
            alert.showAndWait();
            return;
        }

        try {
            // Load the specific view-only map FXML
            FXMLLoader loader = new FXMLLoader(App.class.getResource("viewLocationMap.fxml"));
            Parent mapRoot = loader.load();

            // Get the controller and set the location data
            ViewLocationMapController controller = loader.getController();
            controller.setLocationData(x, y, name);

            // Create a new stage for the map
            Stage mapStage = new Stage();
            mapStage.setTitle("Location: " + name);
            mapStage.initModality(Modality.APPLICATION_MODAL);
            mapStage.setScene(new Scene(mapRoot));

            // Show the map
            mapStage.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Load Map");
            alert.setContentText("An error occurred while trying to load the map: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    //************************


    public static void setSelectedDate(LocalDate selectedDate) {
        ExamenConduitController.selectedDate = selectedDate;
    }
    @FXML
    private void switchToScConduite(ActionEvent event){
        SingleDayConduiteController.setSelectedDate(selectedDate);
        try {
            App.setRoot("sceanceDayMenuConduit");
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void switchToCalendar(ActionEvent event) {
        try {
            App.setRoot("calender");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void switchToScCode(ActionEvent event){
        SingleDayCodeController.setSelectedDate(selectedDate);
        try {
            App.setRoot("sceanceDayMenuCode");
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void switchToExamenCode(ActionEvent event){
        ExamenCodeController.setSelectedDate(selectedDate);
        try {
            App.setRoot("examenDayMenuCode");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
