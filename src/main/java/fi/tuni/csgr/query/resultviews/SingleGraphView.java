package fi.tuni.csgr.query.resultviews;

import fi.tuni.csgr.components.BarChartView;
import fi.tuni.csgr.components.ChartView;
import fi.tuni.csgr.managers.graphs.BarGraphDataManager;
import fi.tuni.csgr.smearAndStatfi.SMEAR.timeAndVariablesFromSmear.PredefinedStationsInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingleGraphView implements ResultView{
    private BarGraphDataManager graphDataManager;
    private ObservableMap<String, ObservableList<XYChart.Series<String, Double>>> gases;
    private HashMap<String, ChartView> charts;
    private VBox resultView;
    private HBox buttonView;
    private VBox graphView;
    private ToggleGroup toggleGroup;
    private RadioButton radioButton1;

    public SingleGraphView(BarGraphDataManager dataManager) {
        graphDataManager = dataManager;
        charts = new HashMap<>();
        resultView = new VBox();
        buttonView = new HBox();
        graphView = new VBox();
        resultView.getChildren().add(buttonView);
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

                    var buttonName = PredefinedStationsInfo.statfiValues
                            .entrySet()
                            .stream()
                            .filter(entry -> Objects.equals(entry.getValue(), name))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .map(Object::toString)
                            .orElse("");

                    radioButton1 = new RadioButton(buttonName);
                    radioButton1.setToggleGroup(toggleGroup);
                    buttonView.getChildren().add(radioButton1);
                }
                radioButton1.setSelected(true);
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
                String name = PredefinedStationsInfo.statfiValues.get(rb.getText());
                charts.put(name, new BarChartView(rb.getText(), gases.get(name)));
                graphView.getChildren().add(charts.get(name).getChartBox());
            }
        });
    }

    @Override
    public Pane getResultView() {
        return resultView;
    }

}
