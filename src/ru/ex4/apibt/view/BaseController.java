package ru.ex4.apibt.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.UserBalanceDto;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.UserInfoService;

import java.math.BigDecimal;
import java.util.List;

public class BaseController {
    private ObservableList<UserBalanceDto> userBalanceObservableList;
    private ObservableList<TickerDto> tickerPairObservableList;
    private UserInfoService userInfoService;

    @FXML
    private TableView<UserBalanceDto> userBalanceTable;
    @FXML
    private TableColumn<UserBalanceDto, String> currencyColumn;
    @FXML
    private TableColumn<UserBalanceDto, String> amountBalanceColumn;
    @FXML
    private TableColumn<UserBalanceDto, String> amountReservedColumn;

    @FXML
    private TableView<TickerDto> tickerPairTable;
    @FXML
    private TableColumn<TickerDto, String> pairColumn;
    @FXML
    private TableColumn<TickerDto, Number> buyPriceColumn;
    @FXML
    private TableColumn<TickerDto, Number> sellPriceColumn;
    @FXML
    private TableColumn<TickerDto, Number> volatPresentColumn;
    @FXML
    private TableColumn<TickerDto, Number> changePricePresentColumn;
    @FXML
    private TableColumn<TickerDto, Boolean> changePriceFactorColumn;
    @FXML
    private TableColumn<TickerDto, String> volumeColumn;

    @FXML
    private Label realLabel;


    public BaseController() {
        this.userBalanceObservableList = FXCollections.observableArrayList();
        this.tickerPairObservableList = FXCollections.observableArrayList();
        this.userInfoService = new UserInfoService();
    }

    @FXML
    private void initialize() {
        Logs.info("initialize baseView");

        fillBalanceTable();

        fillBalanceTableColumn();

//        fillLabel(null);

        addListener();
    }

    private void fillBalanceTable() {
        List<UserBalanceDto> balanceDtoList = userInfoService.getBalanceDtoList();
        this.userBalanceObservableList.addAll(balanceDtoList);
        this.userBalanceTable.setItems(userBalanceObservableList);
    }

    private void fillBalanceTableColumn() {
        this.currencyColumn.setCellValueFactory(param -> param.getValue().currencyProperty());
        this.amountBalanceColumn.setCellValueFactory(param -> param.getValue().amountBalanceProperty());
        this.amountReservedColumn.setCellValueFactory(param -> param.getValue().amountReservedProperty());
    }

//    private void fillLabel(Object o) {
//
//    }

    private void addListener() {
        this.userBalanceTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillPairTable(newValue.currencyProperty().get());
            fillTickerPairTableColumn();
        });
    }

    private void fillPairTable(String currency) {
        System.out.println(currency);

        this.tickerPairObservableList.clear();
        List<TickerDto> tickerDtoList = TickerService.getTickerListByCurrency(currency);
        this.tickerPairObservableList.addAll(tickerDtoList);
        this.tickerPairTable.setItems(tickerPairObservableList);
    }

    private void fillTickerPairTableColumn() {
        this.pairColumn.setCellValueFactory(param -> param.getValue().pairProperty());
        this.buyPriceColumn.setCellValueFactory(param -> param.getValue().buyPriceProperty());
        this.sellPriceColumn.setCellValueFactory(param -> param.getValue().sellPriceProperty());
        this.volatPresentColumn.setCellValueFactory(param -> param.getValue().volatPresentProperty());
        this.changePriceFactorColumn.setCellValueFactory(param -> param.getValue().changePriceFactorProperty());
        this.changePricePresentColumn.setCellValueFactory(param -> param.getValue().changePricePresentProperty());
        this.volumeColumn.setCellValueFactory(param -> param.getValue().volumeProperty());
    }

}
