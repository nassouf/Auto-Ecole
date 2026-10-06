package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.example.App;
import org.example.service.DashboardDAO;

public class DashboardController  {
    @FXML
    private ImageView mainLogo;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button filterButton;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label totalCandidatesLabel;

    @FXML
    private Label successRateLabel;

    @FXML
    private Label totalSessionsLabel;

    @FXML
    private PieChart revenusByLicenseTypeChart;

    @FXML
    private BarChart<String, Number> successRateByLicenseTypeChart;

    @FXML
    private ComboBox<String> vehicleTypeComboBox;

    @FXML
    private BarChart<String, Number> vehicleUsageChart;

    @FXML
    private BarChart<String, Number> paymentsChart;

    @FXML
    private Button comptabiliteButton;

    @FXML
    private VBox comptabiliteSubMenu;

    private DashboardDAO dashboardService;
    private LocalDate startDate;
    private LocalDate endDate;

    @FXML
    public void initialize() {
        // Initialize service
        dashboardService = new DashboardDAO();

        // Set default dates (last month)
        endDate = LocalDate.now();
        startDate = endDate.minusMonths(1);
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);

        // Load logo
        loadLogo();

        // Initialize vehicle types in ComboBox
        vehicleTypeComboBox.setItems(FXCollections.observableArrayList("voiture", "moto", "camion"));
        vehicleTypeComboBox.getSelectionModel().selectFirst();

        // Load initial dashboard data
        loadDashboardData();
    }

    private void loadLogo() {
        try {
            String logoPath = dashboardService.getLogoPath();
            if (logoPath != null && !logoPath.isEmpty()) {
                File file = new File(logoPath);
                if (file.exists()) {
                    Image logo = new Image(file.toURI().toString());
                    mainLogo.setImage(logo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void applyDateFilter(ActionEvent event) {
        startDate = startDatePicker.getValue();
        endDate = endDatePicker.getValue();
        loadDashboardData();
    }

    @FXML
    private void updateVehicleUsageChart(ActionEvent event) {
        String selectedVehicleType = vehicleTypeComboBox.getValue();
        loadVehicleUsageData(selectedVehicleType);
    }

    private void loadDashboardData() {
        loadTotalRevenue();
        loadTotalCandidates();
        loadSuccessRate();
        loadTotalSessions();
        loadRevenusByLicenseType();
        loadSuccessRateByLicenseType();
        loadVehicleUsageData(vehicleTypeComboBox.getValue());
        loadPaymentsData();
    }

    private void loadTotalRevenue() {
        double totalRevenue = dashboardService.getTotalRevenue(startDate, endDate);
        totalRevenueLabel.setText(String.format("%.2f DT", totalRevenue));
    }

    private void loadTotalCandidates() {
        int totalCandidates = dashboardService.getTotalCandidates();
        totalCandidatesLabel.setText(String.valueOf(totalCandidates));
    }

    private void loadSuccessRate() {
        double successRate = dashboardService.getSuccessRate(startDate, endDate);
        successRateLabel.setText(String.format("%.1f%%", successRate));
    }

    private void loadTotalSessions() {
        int totalSessions = dashboardService.getTotalSessions(startDate, endDate);
        totalSessionsLabel.setText(String.valueOf(totalSessions));
    }

    private void loadRevenusByLicenseType() {
        ObservableList<PieChart.Data> pieChartData = dashboardService.getRevenusByLicenseType(startDate, endDate);
        revenusByLicenseTypeChart.setData(pieChartData);
    }

    private void loadSuccessRateByLicenseType() {
        XYChart.Series<String, Number> series = dashboardService.getSuccessRateByLicenseType(startDate, endDate);
        successRateByLicenseTypeChart.getData().clear();
        successRateByLicenseTypeChart.getData().add(series);
    }

    private void loadVehicleUsageData(String vehicleType) {
        XYChart.Series<String, Number> series = dashboardService.getVehicleUsageData(vehicleType, startDate, endDate);
        vehicleUsageChart.getData().clear();
        vehicleUsageChart.getData().add(series);
    }

    private void loadPaymentsData() {
        XYChart.Series<String, Number> series = dashboardService.getPaymentsData(startDate, endDate);
        paymentsChart.getData().clear();
        paymentsChart.getData().add(series);
    }

    @FXML
    public void switchToCandidat(ActionEvent event) throws IOException {
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
    public void toggleComptabiliteMenu() {
        comptabiliteSubMenu.setVisible(!comptabiliteSubMenu.isVisible());
        comptabiliteSubMenu.setManaged(!comptabiliteSubMenu.isManaged());
    }

    @FXML
    public void switchToPaiement(ActionEvent event) throws IOException{
        App.setRoot("paiement");
    }
    @FXML
    public void switchToFactures(ActionEvent event) throws IOException{
        App.setRoot("factures");
    }
    @FXML
    private void switchToHome() throws IOException{
        App.setRoot("Home");
    }
    @FXML
    public void switchToSetting(ActionEvent actionEvent)throws IOException{
        App.setRoot("autoecoleinfo");
    }
}
