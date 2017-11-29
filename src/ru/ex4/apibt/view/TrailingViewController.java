package ru.ex4.apibt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ex4.apibt.Main;
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

    @FXML
    private void OnAddTrailingDialogShow() {
        TickerDto selectedItem = this.tickerPairTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            handleTrailingDialog(selectedItem.getPair(), null);
        }
    }

    @FXML
    private void OnEditTrailingDialogShow() {
        TrailingDto selectedItem = this.trailingTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            handleTrailingDialog(selectedItem.getPair(), selectedItem);
        }
    }

    @FXML
    private void OnDeleteTrailing() {
        TrailingDto selectedItem = this.trailingTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            trailingService.delete(selectedItem);

            this.trailingTable.getItems().remove(selectedItem);
        }
    }

    @FXML
    private void btnOk() {
        dialogStage.close();
    }

    public void initCtrl(ObservableList<TickerDto> tickerPairObservableList) throws IOException {
        if (tickerPairObservableList == null) {
            throw new IOException("error initialize controller");
        }
        fillTickerPairTable(tickerPairObservableList);
        fillTickerPairTableColumn();

        addListener();

        fillStopSignalTable();
        fillStopSignalTableColumn();
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

    private void addListener() {
        this.tickerPairTable.setRowFactory(param -> {
            TableRow<TickerDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    OnAddTrailingDialogShow();
                }
            });
            return row;
        });
        this.trailingTable.setRowFactory(param -> {
            TableRow<TrailingDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    OnEditTrailingDialogShow();
                }
            });
            return row;
        });
    }

    private void fillStopSignalTable() {
        this.trailingObservableList.clear();
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

    private void handleTrailingDialog(String pair, TrailingDto trailingDto) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/TrailingEditDialog.fxml"));

            Parent parent = (Parent) fxmlLoader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle("stop..tr..");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(parent);
            modalStage.setScene(scene);

            TrailingEditDialogController trailingViewController = (TrailingEditDialogController) fxmlLoader.getController();
            trailingViewController.initCtrl(pair, trailingDto);
            trailingViewController.setDialogStage(modalStage);

            modalStage.showAndWait();

            TrailingDto result = trailingViewController.getResult();
            if (result != null) {
                trailingService.save(result);

                fillStopSignalTable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
