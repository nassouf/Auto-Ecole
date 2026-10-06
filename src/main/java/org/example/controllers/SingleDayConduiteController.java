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
import org.example.service.verification.InputCheckServices;
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

public class SingleDayConduiteController {
    private static Float locationX=null;
    private static Float locationY=null;
    private static String locationName=null;
    private static LocalDate selectedDate;
    private MapController mapController;

    @FXML
    private TableView<SceanceConduite> table;
    @FXML
    private TableColumn<SceanceConduite, Integer> cin;
    @FXML
    private TableColumn<SceanceConduite, VehiculeType> type;
    @FXML
    private TableColumn<SceanceConduite, LocalDateTime> date;
    @FXML
    private TableColumn<SceanceConduite, Duration> duree;
    @FXML
    private TableColumn<SceanceConduite, Integer> idMoniteur;
    @FXML
    private TableColumn<SceanceConduite, Integer> matricule;
    @FXML
    private TableColumn<SceanceConduite, Float> price;
    @FXML
    private TableColumn<SceanceConduite, String> locationColumn;
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
    private void initialize() {
        System.out.println(locationName);
        System.out.println(locationX);
        System.out.println(locationY);
        btajouter.setVisible(true);
        btannuler.setVisible(false);
        btmodifier.setVisible(false);
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
        cin.setCellValueFactory(new PropertyValueFactory<SceanceConduite, Integer>("CondidatId"));
        type.setCellValueFactory(new PropertyValueFactory<SceanceConduite, VehiculeType>("vehiculeType"));
        date.setCellValueFactory(new PropertyValueFactory<SceanceConduite, LocalDateTime>("sceanceDate"));
        duree.setCellValueFactory(new PropertyValueFactory<SceanceConduite, Duration>("duration"));
        idMoniteur.setCellValueFactory(new PropertyValueFactory<SceanceConduite, Integer>("moniteurId"));
        price.setCellValueFactory(new PropertyValueFactory<SceanceConduite, Float>("price"));
        matricule.setCellValueFactory(new PropertyValueFactory<SceanceConduite, Integer>("matriculeVehicule"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<SceanceConduite, String>("locationName"));


        SceanceConduiteServices service = new SceanceConduiteServices();
        ObservableList<SceanceConduite> sceanceConduite= FXCollections.observableArrayList(service.getAll());
        table.setItems(sceanceConduite.stream()
                .filter(sc -> sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList))
        );

        table.setRowFactory(tv -> {
            TableRow<SceanceConduite> row = new TableRow<>() {
                @Override
                protected void updateItem(SceanceConduite sc, boolean empty) {
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
    @FXML
    private void annulerEdit(ActionEvent event) {
        btajouter.setVisible(true);
        btmodifier.setVisible(false);
        btannuler.setVisible(false);
        resetInputs();
        locationX=null;
        locationY=null;
        locationName=null;
        hideErrors();
    }
    private List<SceanceConduite> seancesConduiteToday() {
        SceanceConduiteServices sceanceConduiteServices = new SceanceConduiteServices();
        return sceanceConduiteServices.getAll().stream()
                .filter(sc -> sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toList());
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
    private List<SceanceConduite> seancesConduitsToday(){
        SceanceConduiteServices sceanceConduiteServices=new SceanceConduiteServices();
        return sceanceConduiteServices.getAll().stream()
                .filter(sc->sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
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
    @FXML
    public void modifierSeance(ActionEvent event) {
        SceanceConduite selectedSeance = table.getSelectionModel().getSelectedItem();
        System.out.println(selectedSeance);
        if (testInputsUpdate(selectedSeance)) {
            try {
                if (selectedSeance == null) {
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

                SceanceConduite sceanceConduite=new SceanceConduite();
                sceanceConduite.setCondidatId(condidatList.getValue().getCIN());
                sceanceConduite.setVehiculeType(typeChoice.getValue());
                sceanceConduite.setMoniteurId(moniteurList.getValue().getIdMoniteur());
                sceanceConduite.setPrice((float)prix);
                sceanceConduite.setSceanceDate(LocalDateTime.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDayOfMonth(), Integer.parseInt(hrTemp.getText()), Integer.parseInt(minTemp.getText())));
                sceanceConduite.setDuration(Duration.ofMinutes(Integer.parseInt(hrDuree.getText()) * 60 + Integer.parseInt(minDuree.getText())));
                sceanceConduite.setMatriculeVehicule(vehiculeList.getValue().getMatricule());
                sceanceConduite.setLocationX(locationX);
                sceanceConduite.setLocationY(locationY);
                sceanceConduite.setLocationName(locationName);
                // Mettre à jour dans la base de données
                SceanceConduiteServices sceanceConduiteServices = new SceanceConduiteServices();
                if (sceanceConduiteServices.update(selectedSeance.getSceanceId(), sceanceConduite)) {
                    loadSeanceTable(); // Rafraîchir la table
                } else {
                    System.out.println("Échec de la modification.");
                }
            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            annulerEdit(event);
            loadSeanceTable();
        }
    }

    private boolean testInputsUpdate(SceanceConduite selectedSeance) {
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
                List<SceanceConduite> filteredSceances = seancesConduiteToday().stream()
                        .filter(sceance -> !Objects.equals(sceance.getSceanceId(), selectedSeance.getSceanceId()))
                        .collect(Collectors.toList());

                if (!TimeCheckServices.isMoniteurSeanceValable(
                        filteredSceances,
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
                List<SceanceConduite> filteredSceances = seancesConduitsToday().stream()
                        .filter(sceanceConduite -> !Objects.equals(sceanceConduite.getSceanceId(), selectedSeance.getSceanceId()))
                        .collect(Collectors.toList());
                List<? extends Sceance> combinedSceances = Stream.concat(
                        filteredSceances.stream(),
                        seancesCodeToday().stream()
                ).collect(Collectors.toList());
                List<? extends Examen> combinedExamens =Stream.concat(
                        examenConduitToday().stream(),
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
                List<SceanceConduite> filteredSceances = seancesConduitsToday().stream()
                        .filter(sceanceConduite -> !Objects.equals(sceanceConduite.getSceanceId(), selectedSeance.getSceanceId()))
                        .collect(Collectors.toList());
                if (!TimeCheckServices.isVehiculeSeanceAvailable(
                        filteredSceances,
                        vehiculeList.getValue().getMatricule(),
                        sessionStart,
                        sessionDuration,
                        errorVehicule
                )||!TimeCheckServices.isVehiculeExamenAvailable(
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

    private void loadSeanceTable() {
        SceanceConduiteServices sceanceConduiteServices=new SceanceConduiteServices();
        List<SceanceConduite> sceanceConduites = sceanceConduiteServices.getAll().stream()
                .filter(sc -> sc.getSceanceDate().toLocalDate().isEqual(selectedDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        ObservableList<SceanceConduite> data = FXCollections.observableArrayList(sceanceConduites);
        table.setItems(data);
        table.refresh();
    }

    @FXML
    private void ajoutScConduit(ActionEvent event) {
        hideErrors();
        if (testInputs()){
            SceanceConduiteServices sceanceConduiteServices=new SceanceConduiteServices();
            SceanceConduite sceanceConduite=new SceanceConduite();
            sceanceConduite.setCondidatId(condidatList.getValue().getCIN());
            sceanceConduite.setVehiculeType(typeChoice.getValue());
            sceanceConduite.setMoniteurId(moniteurList.getValue().getIdMoniteur());
            TarifDAO tarifDAO = new TarifDAO();
            int prix = 0;
            if (typeChoice.getValue() == VehiculeType.CAMION) {
                prix = tarifDAO.getTarifs().getDrivingSessionPricecamion();
            } else if (typeChoice.getValue() == VehiculeType.MOTO) {
                prix = tarifDAO.getTarifs().getDrivingSessionPricemoto();
            } else if (typeChoice.getValue() == VehiculeType.VOITURE) {
                prix = tarifDAO.getTarifs().getDrivingSessionPricevoiture();
            }
            sceanceConduite.setPrice((float)prix);
            sceanceConduite.setSceanceDate(LocalDateTime.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDayOfMonth(), Integer.parseInt(hrTemp.getText()), Integer.parseInt(minTemp.getText())));
            sceanceConduite.setDuration(Duration.ofMinutes(Integer.parseInt(hrDuree.getText()) * 60 + Integer.parseInt(minDuree.getText())));
            sceanceConduite.setMatriculeVehicule(vehiculeList.getValue().getMatricule());
            sceanceConduite.setLocationX(locationX);
            sceanceConduite.setLocationY(locationY);
            sceanceConduite.setLocationName(locationName);
            if (sceanceConduiteServices.create(sceanceConduite)){
                resetInputs();
                hideErrors();
                ObservableList<SceanceConduite> sceanceConduites=table.getItems();
                sceanceConduites.add(sceanceConduite);
                table.setItems(sceanceConduites);
                table.refresh();
                locationX=null;
                locationY=null;
                locationName=null;
                btajouter.setVisible(true);
                btannuler.setVisible(false);
                btmodifier.setVisible(false);
            }else {
                System.out.println("Erreur ajoutScConduit");
            }
        }
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

    //************** the methods for the "right click" menu *******************

    //1)delete
    @FXML
    private void scCodeDeleteOption(ActionEvent event) {
        SceanceConduite sceanceConduite=table.getSelectionModel().getSelectedItem();
        if(sceanceConduite!=null) {

            //initialize alert and pops up a confirmation message
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("supprimer la seance :"+sceanceConduite.getSceanceId()+" ?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get()==ButtonType.OK) {
                alert.close();
                SceanceConduiteServices sceanceConduiteServices=new SceanceConduiteServices();
                sceanceConduiteServices.delete(sceanceConduite.getSceanceId());
                table.getItems().remove(sceanceConduite);
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
    private void seanceEditOption(ActionEvent event) {
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
        SceanceConduite sceanceConduite=table.getSelectionModel().getSelectedItem();
        CandidatService candidatService=new CandidatService();
        MoniteurService moniteurService=new MoniteurService();
        VehiculeServices vehiculeService=new VehiculeServices();
        if (sceanceConduite != null) {
            typeChoice.setValue(sceanceConduite.getVehiculeType());
            hrTemp.setText(String.valueOf(sceanceConduite.getSceanceDate().getHour()));
            minTemp.setText(String.valueOf(sceanceConduite.getSceanceDate().getMinute()));
            hrDuree.setText(String.valueOf(sceanceConduite.getDuration().toMinutes()/60));
            minDuree.setText(String.valueOf(sceanceConduite.getDuration().toMinutes()%60));
            condidatList.setValue(candidatService.getById(sceanceConduite.getCondidatId()));
            moniteurList.setValue(moniteurService.getById(sceanceConduite.getMoniteurId()));
            vehiculeList.setValue(vehiculeService.getById(sceanceConduite.getMatriculeVehicule()));
            locationX=sceanceConduite.getLocationX();
            locationY=sceanceConduite.getLocationY();
            locationName=sceanceConduite.getLocationName();
            locationLabel.setText(locationName);
            //***********
        } else {
            System.out.println("Veuillez sélectionner un ingenieur !");
        }
        btajouter.setVisible(false);
        btmodifier.setVisible(true);
        btannuler.setVisible(true);
    }
    //3)See location
    @FXML
    private void goToLocation(ActionEvent event) {
        SceanceConduite selectedSeance = table.getSelectionModel().getSelectedItem();

        if (selectedSeance == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Session Selected");
            alert.setContentText("Please select a driving session to view its location.");
            alert.showAndWait();
            return;
        }

        Float x = selectedSeance.getLocationX();
        Float y = selectedSeance.getLocationY();
        String name = selectedSeance.getLocationName();

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

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }



    public static void setSelectedDate(LocalDate selectedDate) {
        SingleDayConduiteController.selectedDate = selectedDate;
    }
    public static LocalDate getSelectedDate(){
        return selectedDate;
    }
    public static Float getLocationX(){return locationX;}
    public static Float getLocationY(){return locationY;}
    public static void setLocationX(Float x){locationX = x;}
    public static void setLocationY(Float y){locationY = y;}
    public static void setLocationName(String s){locationName = s;}
    public static String getLocationName(){return locationName;}
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
    @FXML
    private void switchToExamenConduit(ActionEvent event){
        ExamenConduitController.setSelectedDate(selectedDate);
        try {
            App.setRoot("examenDayMenuConduit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

