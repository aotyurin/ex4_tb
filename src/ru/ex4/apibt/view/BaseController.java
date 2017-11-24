package ru.ex4.apibt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.dto.OpenOrderDto;
import ru.ex4.apibt.dto.OrderBookDto;
import ru.ex4.apibt.dto.TickerDto;
import ru.ex4.apibt.dto.UserBalanceDto;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.service.OrderBookService;
import ru.ex4.apibt.service.OrderService;
import ru.ex4.apibt.service.TickerService;
import ru.ex4.apibt.service.UserInfoService;
import ru.ex4.apibt.util.DecimalUtil;

import java.math.BigDecimal;
import java.util.List;

public class BaseController {
    private ObservableList<UserBalanceDto> userBalanceObservableList;
    private ObservableList<TickerDto> tickerPairObservableList;
    private ObservableList<OrderBookDto.Ask> askOrderBookObservableList;
    private ObservableList<OrderBookDto.Bid> bidOrderBookObservableList;
    private ObservableList<OpenOrderDto> openOrderObservableList;

    private UserInfoService userInfoService;
    //    private TickerService tickerService;
    private OrderBookService orderBookService;
    private OrderService orderService;

    private UserBalanceDto userBalanceDto;

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
    private TableView<OpenOrderDto> openOrderTable;
    @FXML
    private TableColumn<OpenOrderDto, String> createdOpenOrderTableColumn;
    @FXML
    private TableColumn<OpenOrderDto, String> typeOpenOrderTableColumn;
    @FXML
    private TableColumn<OpenOrderDto, Number> priceOpenOrderTableColumn;
    @FXML
    private TableColumn<OpenOrderDto, Number> quantityOpenOrderTableColumn;
    @FXML
    private TableColumn<OpenOrderDto, Number> amountOpenOrderTableColumn;

    @FXML
    private TextField quantityAskTextField;
    @FXML
    private TextField priceAskTextField;
    @FXML
    private TextField totalAskTextField;
    @FXML
    private TextField commissionAskTextField;
    @FXML
    private TextField balanceAskTextField;

    @FXML
    private TextField quantityBidTextField;
    @FXML
    private TextField priceBidTextField;
    @FXML
    private TextField totalBidTextField;
    @FXML
    private TextField commissionBidTextField;
    @FXML
    private TextField balanceBidTextField;


    public BaseController() {
        this.userBalanceObservableList = FXCollections.observableArrayList();
        this.tickerPairObservableList = FXCollections.observableArrayList();
        this.askOrderBookObservableList = FXCollections.observableArrayList();
        this.bidOrderBookObservableList = FXCollections.observableArrayList();
        this.openOrderObservableList = FXCollections.observableArrayList();

        this.userInfoService = new UserInfoService();
//        this.tickerService = new TickerService();
        this.orderBookService = new OrderBookService();
        this.orderService = new OrderService();
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
            userBalanceDto = newValue;

            fillTickerPairTable(newValue);
            fillTickerPairTableColumn();
        });
        this.tickerPairTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillOrderBookTable(newValue);
            fillOrderBookTableColumn();
            fillOpenOrderTable(newValue);
            fillOpenOrderTableColumn();
        });
        this.askOrderBookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillBidTextField(newValue);
        });
        this.bidOrderBookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillAskTextField(newValue);
        });

        this.quantityBidTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                calcBidTextField();
            }
        });
        this.priceBidTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                calcBidTextField();
            }
        });
        this.quantityAskTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                calcAskTextField();
            }
        });
        this.priceAskTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                calcAskTextField();
            }
        });

    }

    private void fillTickerPairTable(UserBalanceDto userBalanceDto) {
        this.tickerPairObservableList.clear();

        if (userBalanceDto != null) {
            List<TickerDto> tickerDtoList = TickerService.getTickerDtoListByCurrency(userBalanceDto.currencyProperty().get());
            this.tickerPairObservableList.addAll(tickerDtoList);
            this.tickerPairTable.setItems(tickerPairObservableList);
        }
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


    private void fillOrderBookTable(TickerDto tickerDto) {
        this.askOrderBookObservableList.clear();
        this.bidOrderBookObservableList.clear();

        if (tickerDto != null) {
            String pair = tickerDto.pairProperty().get();
            OrderBookDto orderBookDto = orderBookService.getOrderBookDtoByPair(pair);
            this.askOrderBookObservableList.addAll(orderBookDto.getAskList());
            this.askOrderBookTable.setItems(askOrderBookObservableList);

            this.bidOrderBookObservableList.addAll(orderBookDto.getBidList());
            this.bidOrderBookTable.setItems(bidOrderBookObservableList);
        }
    }

    private void fillOrderBookTableColumn() {
        this.amountAskOrderBookTableColumn.setCellValueFactory(param -> param.getValue().amountProperty());
        this.priceAskOrderBookTableColumn.setCellValueFactory(param -> param.getValue().priceProperty());
        this.quantityAskOrderBookTableColumn.setCellValueFactory(param -> param.getValue().quantityProperty());

        this.amountBidOrderBookTableColumn.setCellValueFactory(param -> param.getValue().amountProperty());
        this.priceBidOrderBookTableColumn.setCellValueFactory(param -> param.getValue().priceProperty());
        this.quantityBidOrderBookTableColumn.setCellValueFactory(param -> param.getValue().quantityProperty());
    }


    private void fillOpenOrderTable(TickerDto tickerDto) {
        this.openOrderObservableList.clear();

        if (tickerDto != null) {
            String pair = tickerDto.pairProperty().get();
            List<OpenOrderDto> openOrderDtoList = orderService.getOrderByPair(pair);
            this.openOrderObservableList.addAll(openOrderDtoList);
            this.openOrderTable.setItems(openOrderObservableList);
        }
    }

    private void fillOpenOrderTableColumn() {
        this.createdOpenOrderTableColumn.setCellValueFactory(param -> param.getValue().createdProperty());
        this.typeOpenOrderTableColumn.setCellValueFactory(param -> param.getValue().typeProperty());
        this.priceOpenOrderTableColumn.setCellValueFactory(param -> param.getValue().priceProperty());
        this.quantityOpenOrderTableColumn.setCellValueFactory(param -> param.getValue().quantityProperty());
        this.amountOpenOrderTableColumn.setCellValueFactory(param -> param.getValue().amountProperty());
    }


    private void fillBidTextField(OrderBookDto.Ask value) {
        if (value != null) {
            this.quantityBidTextField.setText(value.getAmount().toPlainString());
            this.priceBidTextField.setText(value.getPrice().toPlainString());
            this.priceAskTextField.setText(value.getPrice().toPlainString());
        } else {
            this.quantityBidTextField.setText("");
            this.priceBidTextField.setText("");
        }
    }

    private void fillAskTextField(OrderBookDto.Bid value) {
        if (value != null) {
            this.quantityAskTextField.setText(value.getAmount().toPlainString());
            this.priceAskTextField.setText(value.getPrice().toPlainString());
            this.priceBidTextField.setText(value.getPrice().toPlainString());
        } else {
            this.quantityAskTextField.setText("");
            this.priceAskTextField.setText("");
        }
    }


    private void calcBidTextField() {
        BigDecimal quantity = DecimalUtil.parse(this.quantityBidTextField.getText());
        BigDecimal price = DecimalUtil.parse(this.priceBidTextField.getText());

        BigDecimal total = quantity.multiply(price).add(quantity.multiply(price).multiply(BigDecimal.valueOf(IExConst.STOCK_FEE)));
        BigDecimal commission = quantity.multiply(price).multiply(BigDecimal.valueOf(IExConst.STOCK_FEE));
        BigDecimal balance = userBalanceDto.getAmountBalance().subtract(total);

        this.totalBidTextField.setText(DecimalUtil.round(total).toPlainString());
        this.commissionBidTextField.setText(DecimalUtil.round(commission).toPlainString());
        this.balanceBidTextField.setText(DecimalUtil.round(balance).toPlainString());
    }

    private void calcAskTextField() {
        BigDecimal quantity = DecimalUtil.parse(this.quantityAskTextField.getText());
        BigDecimal price = DecimalUtil.parse(this.priceAskTextField.getText());

        BigDecimal total = quantity.multiply(price).add(quantity.multiply(price).multiply(BigDecimal.valueOf(IExConst.STOCK_FEE)));
        BigDecimal commission = quantity.multiply(price).multiply(BigDecimal.valueOf(IExConst.STOCK_FEE));
        BigDecimal balance = userBalanceDto.getAmountBalance().subtract(total);

        this.totalAskTextField.setText(DecimalUtil.round(total).toPlainString());
        this.commissionAskTextField.setText(DecimalUtil.round(commission).toPlainString());
        this.balanceAskTextField.setText(DecimalUtil.round(balance).toPlainString());
    }


}
