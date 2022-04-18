package fi.tuni.csgr.query.resultviews;

import fi.tuni.csgr.components.ChartView;
import fi.tuni.csgr.managers.graphs.GraphDataManager;
import fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear.PredefinedStationsInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class StatfiResultsView implements ResultView{
    private GraphDataManager graphDataManager;
    private ObservableMap<String, ObservableList<XYChart.Series<Long, Double>>> gases;
    private HashMap<String, ChartView> charts;
    private VBox resultView;
    private HBox newHBox;
    private VBox graphView;
    private ToggleGroup toggleGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;

    public StatfiResultsView(GraphDataManager dataManager) {
        graphDataManager = dataManager;
        charts = new HashMap<>();
        resultView = new VBox();
        newHBox = new HBox();
        graphView = new VBox();
        resultView.getChildren().add(newHBox);
        resultView.getChildren().add(graphView);
        toggleGroup = new ToggleGroup();
        gases = dataManager.getGases();
        addToggleGroupListener();
        addGraphListeners();
    }

    /**
     * Adds listeners to graphDataManager, to add/remove charts when graphDataManager updated.
     *
     */
    private void addGraphListeners() {

        MapChangeListener mainListener = new MapChangeListener() {
            @Override
            public void onChanged(Change change) {

                String name = change.getKey().toString();

                if(change.wasAdded()) {
                    radioButton1 = new RadioButton(name);
                    radioButton1.setToggleGroup(toggleGroup);
                    newHBox.getChildren().add(radioButton1);
                }
            }
        };
        graphDataManager.addMainListener(mainListener);

    }

    private void addToggleGroupListener(){
        // add a change listener
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> ob,
                                Toggle o, Toggle n)
            {
                graphView.getChildren().clear();
                RadioButton rb = (RadioButton)toggleGroup.getSelectedToggle();
                String name = rb.getText();
                charts.put(name, new ChartView(name, gases.get(name)));
                graphView.getChildren().add(charts.get(name).getChartBox());

            }
        });
    }

    @Override
    public Pane getResultView() {
        return resultView;
    }

}
