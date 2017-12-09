package ru.ex4.apibt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.TrailingDto;
import ru.ex4.apibt.dto.UserBalanceDto;
import ru.ex4.apibt.model.TrendType;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.TrailingService;
import ru.ex4.apibt.view.fxmlManager.IFxmlController;
import ru.ex4.apibt.view.fxmlManager.IFxmlDto;
import ru.ex4.apibt.view.fxmlManager.LoaderFxmlController;

import java.util.Date;
import java.util.List;

public class TrailingViewController implements IFxmlController {
    private ObservableList<TickerDto> tickerPairObservableList;
    private ObservableList<TrailingDto> trailingObservableList;

    private TickerService tickerService;
    private TrailingService trailingService;

    private Stage dialogStage;

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
        this.tickerPairObservableList = FXCollections.observableArrayList();
        this.trailingObservableList = FXCollections.observableArrayList();

        this.tickerService = new TickerService();
        this.trailingService = new TrailingService();
    }

    @Override
    public void initCtrl(IFxmlDto userBalanceDto) {
        if (userBalanceDto != null && userBalanceDto instanceof UserBalanceDto) {
            fillTickerPairTable((UserBalanceDto) userBalanceDto);
            fillTickerPairTableColumn();

            addListener();

            fillStopSignalTable();
            fillStopSignalTableColumn();
        }
    }

    @Override
    public IFxmlDto getResult() {
        return null;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void OnAddTrailingDialogShow() {
        TickerDto selectedItem = this.tickerPairTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            handleTrailingEditDialog(new TrailingDto(selectedItem.getPair(), TrendType.flat, selectedItem.getSellPrice(), new Date(), null));
        }
    }

    @FXML
    private void OnEditTrailingDialogShow() {
        TrailingDto selectedItem = this.trailingTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            handleTrailingEditDialog(selectedItem);
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


    private void fillTickerPairTable(UserBalanceDto userBalanceDto) {
        List<TickerDto> tickerDtoList = tickerService.getTickerListByCurrency(userBalanceDto.currencyProperty().get());

        this.tickerPairObservableList.addAll(tickerDtoList);
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

    private void handleTrailingEditDialog(TrailingDto trailingDto) {
        LoaderFxmlController fxmlController = new LoaderFxmlController<TrailingEditDialogController>();
        TrailingDto result = (TrailingDto) fxmlController.trailingEditDialog(trailingDto, "TrailingEditDialog");

        if (result != null) {
            trailingService.save(result);

            fillStopSignalTable();
        }
    }

}
