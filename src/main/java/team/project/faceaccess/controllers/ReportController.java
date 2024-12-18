package team.project.faceaccess.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import team.project.faceaccess.utils.Constants;
import team.project.faceaccess.utils.Helpers;
import team.project.faceaccess.metier.IMetier;
import team.project.faceaccess.metier.IMetierImp;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    @FXML
    private PieChart pieChart;
    @FXML
    private Button pieChartDBtn;
    @FXML
    private Button pieChartMBtn;
    @FXML
    private Button pieChartWBtn;
    private int nbrResidentsPieChart;
    private int nbrUnknowsPieChart;
    IMetier metier = new IMetierImp();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pieChartMBtn.fire();
        buildBarChart();
    }

    private void buildPieChart() {
        //Preparing ObservbleList object
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Residents", nbrResidentsPieChart),
                new PieChart.Data("Unknowns", nbrUnknowsPieChart));
        pieChart.getData().clear();
        pieChart.getData().setAll(pieChartData); //Creating a Pie chart

        //attach tooltips
        createToolTips(pieChart);

        pieChart.setTitle("Number of active users %"); //Setting the title of the Pie chart
        pieChart.setClockwise(true); //setting the direction to arrange the data
        pieChart.setLabelLineLength(20); //Setting the length of the label line
        pieChart.setLabelsVisible(true); //Setting the labels of the pie chart visible
        pieChart.setLegendVisible(false);
        pieChart.setStartAngle(180);
        pieChart.setAnimated(false);

        //bind value and label on each pie slice to reflect changes
        pieChartData.forEach(data ->
                data.nameProperty().bind(Bindings.concat(data.getName(), " ", data.pieValueProperty(), " ")
                ));
    }

    private void createToolTips(PieChart pc) {

        for (PieChart.Data data : pc.getData()) {
            String msg = Double.toString(data.getPieValue());

            Tooltip tp = new Tooltip(msg);
            tp.setShowDelay(Duration.seconds(0));

            Tooltip.install(data.getNode(), tp);

            //update tooltip data when changed
            data.pieValueProperty().addListener((observable, oldValue, newValue) ->
            {
                tp.setText(newValue.toString());
            });
        }
    }

    @FXML
    void onPieChartDBtnClicked(ActionEvent event) {
        nbrResidentsPieChart = metier.getTotalUsers(Constants.DURATION_DAY, true);
        nbrUnknowsPieChart = metier.getTotalUsers(Constants.DURATION_DAY, false);
        buildPieChart();
    }

    @FXML
    void onPieChartWBtnClicked(ActionEvent event) {
        this.pieChartWBtn.setDisable(true);
        nbrResidentsPieChart = metier.getTotalUsers(Constants.DURATION_WEEK, true);
        nbrUnknowsPieChart = metier.getTotalUsers(Constants.DURATION_WEEK, false);
        buildPieChart();
        this.pieChartWBtn.setDisable(false);
    }

    @FXML
    void onPieChartMBtnClicked(ActionEvent event) {
        nbrResidentsPieChart = metier.getTotalUsers(Constants.DURATION_MONTH, true);
        nbrUnknowsPieChart = metier.getTotalUsers(Constants.DURATION_MONTH, false);
        buildPieChart();
    }

    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private void buildBarChart() {

        XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<String, Integer>();
        dataSeries1.setName("Residents");

        Map<String, Integer> accessCounts = metier.getTotalVisits(true);

        Map<String, String> daysNames = Helpers.getLastDaysNames();

        dataSeries1.getData().add(new XYChart.Data<>(daysNames.get("today"), accessCounts.get("today")));
        dataSeries1.getData().add(new XYChart.Data<>(daysNames.get("today1"), accessCounts.get("today1")));
        dataSeries1.getData().add(new XYChart.Data<>(daysNames.get("today2"), accessCounts.get("today2")));
        dataSeries1.getData().add(new XYChart.Data<>(daysNames.get("today3"), accessCounts.get("today3")));
        dataSeries1.getData().add(new XYChart.Data<>(daysNames.get("today4"), accessCounts.get("today4")));
        dataSeries1.getData().add(new XYChart.Data<>(daysNames.get("today5"), accessCounts.get("today5")));
        dataSeries1.getData().add(new XYChart.Data<>(daysNames.get("today6"), accessCounts.get("today6")));

        barChart.getData().add(dataSeries1);

        XYChart.Series<String, Integer> dataSeries2 = new XYChart.Series<>();
        dataSeries2.setName("Unknowns");

        accessCounts = metier.getTotalVisits(false);

        dataSeries2.getData().add(new XYChart.Data<>(daysNames.get("today"), accessCounts.get("today")));
        dataSeries2.getData().add(new XYChart.Data<>(daysNames.get("today1"), accessCounts.get("today1")));
        dataSeries2.getData().add(new XYChart.Data<>(daysNames.get("today2"), accessCounts.get("today2")));
        dataSeries2.getData().add(new XYChart.Data<>(daysNames.get("today3"), accessCounts.get("today3")));
        dataSeries2.getData().add(new XYChart.Data<>(daysNames.get("today4"), accessCounts.get("today4")));
        dataSeries2.getData().add(new XYChart.Data<>(daysNames.get("today5"), accessCounts.get("today5")));
        dataSeries2.getData().add(new XYChart.Data<>(daysNames.get("today6"), accessCounts.get("today6")));

        barChart.getData().add(dataSeries2);

    }


}
