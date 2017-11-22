package ru.ex4.apibt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ru.ex4.apibt.dto.OrderBookDto;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.UserBalanceDto;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.OrderBookService;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.UserInfoService;

import java.util.List;

public class BaseController {
    private ObservableList<UserBalanceDto> userBalanceObservableList;
    private ObservableList<TickerDto> tickerPairObservableList;
    private ObservableList<OrderBookDto.Ask> askOrderBookObservableList;
    private ObservableList<OrderBookDto.Bid> bidOrderBookObservableList;
    private UserInfoService userInfoService;
    private OrderBookService orderBookService;

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
    private TableColumn<TickerDto, String> pairTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Number> buyPriceTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Number> sellPriceTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Number> volatPresentTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Number> changePricePresentTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, Boolean> changePriceFactorTickerPairTableColumn;
    @FXML
    private TableColumn<TickerDto, String> volumeTickerPairTableColumn;

    @FXML
    private TableView<OrderBookDto.Ask> askOrderBookTable;
    @FXML
    private TableColumn<OrderBookDto.Ask, Number> amountAskOrderBookTableColumn;
    @FXML
    private TableColumn<OrderBookDto.Ask, Number> priceAskOrderBookTableColumn;
    @FXML
    private TableColumn<OrderBookDto.Ask, Number> quantityAskOrderBookTableColumn;
    @FXML
    private TableView<OrderBookDto.Bid> bidOrderBookTable;
    @FXML
    private TableColumn<OrderBookDto.Bid, Number> amountBidOrderBookTableColumn;
    @FXML
    private TableColumn<OrderBookDto.Bid, Number> priceBidOrderBookTableColumn;
    @FXML
    private TableColumn<OrderBookDto.Bid, Number> quantityBidOrderBookTableColumn;

    @FXML
    private Label realLabel;


    public BaseController() {
        this.userBalanceObservableList = FXCollections.observableArrayList();
        this.tickerPairObservableList = FXCollections.observableArrayList();
        this.askOrderBookObservableList = FXCollections.observableArrayList();
        this.bidOrderBookObservableList = FXCollections.observableArrayList();

        this.userInfoService = new UserInfoService();
        this.orderBookService = new OrderBookService();
    }

    @FXML
    private void initialize() {
        Logs.info("initialize baseView");

        fillBalanceTable();
        fillBalanceTableColumn();

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

    private void addListener() {
        this.userBalanceTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillTickerPairTable(newValue.currencyProperty().get());
            fillTickerPairTableColumn();
        });
        this.tickerPairTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String pair = newValue.pairProperty().get();
            fillOrderBookTable(pair);
            fillOrderBookTableColumn();
            fillOpenOrderTable(pair);
            fillOpenOrderTableColumn();

        });
    }

    private void fillTickerPairTable(String currency) {
        System.out.println(currency);

        this.tickerPairObservableList.clear();
        List<TickerDto> tickerDtoList = TickerService.getTickerDtoListByCurrency(currency);
        this.tickerPairObservableList.addAll(tickerDtoList);
        this.tickerPairTable.setItems(tickerPairObservableList);
    }

    private void fillTickerPairTableColumn() {
        this.pairTickerPairTableColumn.setCellValueFactory(param -> param.getValue().pairProperty());
        this.buyPriceTickerPairTableColumn.setCellValueFactory(param -> param.getValue().buyPriceProperty());
        this.sellPriceTickerPairTableColumn.setCellValueFactory(param -> param.getValue().sellPriceProperty());
        this.volatPresentTickerPairTableColumn.setCellValueFactory(param -> param.getValue().volatPresentProperty());
        this.changePriceFactorTickerPairTableColumn.setCellValueFactory(param -> param.getValue().changePriceFactorProperty());
        this.changePricePresentTickerPairTableColumn.setCellValueFactory(param -> param.getValue().changePricePresentProperty());
        this.volumeTickerPairTableColumn.setCellValueFactory(param -> param.getValue().volumeProperty());
    }


    private void fillOrderBookTable(String pair) {
        this.askOrderBookObservableList.clear();
        this.bidOrderBookObservableList.clear();

        OrderBookDto orderBookDto = orderBookService.getOrderBookDtoByPair(pair);
        this.askOrderBookObservableList.addAll(orderBookDto.getAskList());
        this.askOrderBookTable.setItems(askOrderBookObservableList);

        this.bidOrderBookObservableList.addAll(orderBookDto.getBidList());
        this.bidOrderBookTable.setItems(bidOrderBookObservableList);
    }

    private void fillOrderBookTableColumn() {
        this.amountAskOrderBookTableColumn.setCellValueFactory(param -> param.getValue().amountProperty());
        this.priceAskOrderBookTableColumn.setCellValueFactory(param -> param.getValue().priceProperty());
        this.quantityAskOrderBookTableColumn.setCellValueFactory(param -> param.getValue().quantityProperty());

        this.amountBidOrderBookTableColumn.setCellValueFactory(param -> param.getValue().amountProperty());
        this.priceBidOrderBookTableColumn.setCellValueFactory(param -> param.getValue().priceProperty());
        this.quantityBidOrderBookTableColumn.setCellValueFactory(param -> param.getValue().quantityProperty());
    }


    private void fillOpenOrderTable(String pair) {

    }

    private void fillOpenOrderTableColumn() {

    }

}
