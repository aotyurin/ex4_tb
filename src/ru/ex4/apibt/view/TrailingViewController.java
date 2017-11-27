package ru.ex4.apibt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.TrailingDto;
import ru.ex4.apibt.service.TrailingService;

import java.io.IOException;
import java.util.List;

public class TrailingViewController {
    private ObservableList<TrailingDto> trailingObservableList;
    private Stage dialogStage;

    private TrailingService trailingService;

    @FXML
    private TableView<TickerDto> tickerPairTable;
    @FXML
    private TableColumn<TickerDto, String> pairTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Number> highTradeTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Number> lastTradeTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Number> lowTradeTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Number> avgTradeTickerPairTableColumn;

    @FXML
    private TableView<TrailingDto> trailingTable;
    @FXML
    private TableColumn<TrailingDto, String> pairTrailingTableColumn;
    @FXML
    private TableColumn<TrailingDto, String> trendTypeTrailingTableColumn;
    @FXML
    private TableColumn<TrailingDto, Number> priceTrailingTableColumn;
    @FXML
    private TableColumn<TrailingDto, String> dateCreatedTrailingTableColumn;
    @FXML
    private TableColumn<TrailingDto, String> dateNotifyTrailingTableColumn;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public TrailingViewController() {
        this.trailingObservableList = FXCollections.observableArrayList();

        this.trailingService = new TrailingService();
    }

    @FXML
    private void initialize() {
    }

    public void initCtrl(ObservableList<TickerDto> tickerPairObservableList) throws IOException {
        if (tickerPairObservableList == null) {
            throw new IOException("error initialize controller");
        }
        fillTickerPairTable(tickerPairObservableList);
        fillTickerPairTableColumn();

        fillStopSignalTable();
        fillStopSignalTableColumn();

//        addListener();
    }

    private void fillTickerPairTable(ObservableList<TickerDto> tickerPairObservableList) {
        this.tickerPairTable.setItems(tickerPairObservableList);
    }

    private void fillTickerPairTableColumn() {
        this.pairTickerPairTableColumn.setCellValueFactory(param -> param.getValue().pairProperty());
        this.highTradeTickerPairTableColumn.setCellValueFactory(param -> param.getValue().highTradeProperty());
        this.lastTradeTickerPairTableColumn.setCellValueFactory(param -> param.getValue().lastTradeProperty());
        this.lowTradeTickerPairTableColumn.setCellValueFactory(param -> param.getValue().lowTradeProperty());
        this.avgTradeTickerPairTableColumn.setCellValueFactory(param -> param.getValue().avgTradeProperty());
    }

//    private void addListener() {
//        this.tickerPairTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            fillStopSignalTable(newValue);
//            fillStopSignalTableColumn();
//        });
//    }

    private void fillStopSignalTable() {
        List<TrailingDto> trailingDtoList = trailingService.getTrailingDtoList();
        this.trailingObservableList.addAll(trailingDtoList);
        this.trailingTable.setItems(trailingObservableList);
    }

    private void fillStopSignalTableColumn() {
        this.pairTrailingTableColumn.setCellValueFactory(param -> param.getValue().pairProperty());
        this.trendTypeTrailingTableColumn.setCellValueFactory(param -> param.getValue().trendTypeProperty());
        this.priceTrailingTableColumn.setCellValueFactory(param -> param.getValue().priceProperty());
        this.dateCreatedTrailingTableColumn.setCellValueFactory(param -> param.getValue().dateCreatedProperty());
        this.dateNotifyTrailingTableColumn.setCellValueFactory(param -> param.getValue().dateNotifyProperty());
    }


}
