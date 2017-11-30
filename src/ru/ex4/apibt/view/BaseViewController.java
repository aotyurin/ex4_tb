package ru.ex4.apibt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ex4.apibt.IExConst;
import ru.ex4.apibt.Main;
import ru.ex4.apibt.dto.*;
import ru.ex4.apibt.log.Logs;
import ru.ex4.apibt.logic.TrailingProcess;
import ru.ex4.apibt.model.OrderCreate;
import ru.ex4.apibt.model.OrderCreateResult;
import ru.ex4.apibt.model.TypeOrder;
import ru.ex4.apibt.service.*;
import ru.ex4.apibt.util.DecimalUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class BaseViewController {
    private ObservableList<UserBalanceDto> userBalanceMasterObservableList;
    private ObservableList<UserBalanceDto> userBalanceFilterObservableList;
    private ObservableList<TickerDto> tickerPairObservableList;
    private ObservableList<OrderBookDto.Ask> askOrderBookObservableList;
    private ObservableList<OrderBookDto.Bid> bidOrderBookObservableList;
    private ObservableList<OpenOrderDto> openOrderObservableList;

    private UserInfoService userInfoService;
    private TickerService tickerService;
    private OrderBookService orderBookService;
    private OrderService orderService;
    private LossOrderService lossOrderService;

    private UserBalanceDto userBalanceDto;
    private TrailingProcess trailingProcess;


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
    private Label pairLabel;

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
    private TextField quantitySellTextField;
    @FXML
    private TextField priceSellTextField;
    @FXML
    private TextField totalSellTextField;
    @FXML
    private TextField commissionSellTextField;
    @FXML
    private TextField balanceSellTextField;

    @FXML
    private TextField quantityBuyTextField;
    @FXML
    private TextField priceBuyTextField;
    @FXML
    private TextField totalBuyTextField;
    @FXML
    private TextField commissionBuyTextField;
    @FXML
    private TextField balanceBuyTextField;

    @FXML
    private CheckBox allCurrencyCheckBox;

    @FXML
    private MenuItem trailingStartMenuItem;
    @FXML
    private MenuItem lossOrderStartMenuItem;



    public BaseViewController() {
        this.userBalanceMasterObservableList = FXCollections.observableArrayList();
        this.userBalanceFilterObservableList = FXCollections.observableArrayList();
        this.tickerPairObservableList = FXCollections.observableArrayList();
        this.askOrderBookObservableList = FXCollections.observableArrayList();
        this.bidOrderBookObservableList = FXCollections.observableArrayList();
        this.openOrderObservableList = FXCollections.observableArrayList();

        this.userInfoService = new UserInfoService();
        this.tickerService = new TickerService();
        this.orderBookService = new OrderBookService();
        this.orderService = new OrderService();
        this.lossOrderService = new LossOrderService();
    }

    @FXML
    private void initialize() {
        Logs.info("initialize baseView");

        initControls();

        fillBalanceTableObservableList();
        filterBalanceTable();
        fillBalanceTableColumn();

        addListener();
    }

    @FXML
    private void OnTrailingShow(ActionEvent actionEvent) {
        UserBalanceDto selectedItem = this.userBalanceTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (this.tickerPairObservableList.size() > 0) {
                handleTrailingDialog(tickerPairObservableList);
            }
        }
    }

    @FXML
    private void OnTrailingStart(ActionEvent actionEvent) {
        if (trailingProcess != null) {
            trailingProcess.interrupt();
        }
        trailingProcess = new TrailingProcess();
        trailingProcess.start();

        this.trailingStartMenuItem.setText("running");
    }

    @FXML
    private void OnTrailingStop(ActionEvent actionEvent) {
        if (trailingProcess != null) {
            trailingProcess.interrupt();

            this.trailingStartMenuItem.setText("start");
        }
    }

    @FXML
    public void OnLossOrderShow(ActionEvent actionEvent) {

    }

    @FXML
    public void OnLossOrderStart(ActionEvent actionEvent) {

    }

    @FXML
    public void OnLossOrderStop(ActionEvent actionEvent) {

    }

    @FXML
    private void OnClickBuyNow() {
        String balanceText = this.balanceBuyTextField.getText();
        if (balanceText != null) {
            BigDecimal balance = new BigDecimal(balanceText);
            if (balance.compareTo(BigDecimal.ZERO) != -1) {
                System.out.println("OnClickBuyNow");

                String pair = this.pairLabel.getText();
                String quantityText = this.quantityBuyTextField.getText();
                String priceText = this.priceBuyTextField.getText();
                try {
                    OrderCreateResult createResult = orderService.orderCreate(new OrderCreate(pair, new BigDecimal(quantityText), new BigDecimal(priceText), TypeOrder.buy));
                    if (!createResult.getResult()) {
                        Logs.error("Ордер не создан: " + createResult.getError());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void OnClickBuyLoss() {
        String balanceText = this.balanceBuyTextField.getText();
        if (balanceText != null) {
            BigDecimal balance = new BigDecimal(balanceText);
            if (balance.compareTo(BigDecimal.ZERO) != -1) {
                System.out.println("OnClickBuyLoss");

                String pair = this.pairLabel.getText();
                String quantityText = this.quantityBuyTextField.getText();
                String priceText = this.priceBuyTextField.getText();

                lossOrderService.save(new LossOrderDto(pair, new BigDecimal(quantityText), new BigDecimal(priceText), TypeOrder.buy_loss, BigDecimal.ZERO));
            }
        }
    }

    private void initControls() {
        this.allCurrencyCheckBox.setSelected(true);
        this.totalSellTextField.setEditable(false);
        this.commissionSellTextField.setEditable(false);
        this.balanceSellTextField.setEditable(false);
        this.totalBuyTextField.setEditable(false);
        this.commissionBuyTextField.setEditable(false);
        this.balanceBuyTextField.setEditable(false);
        this.trailingStartMenuItem.setText("start");
        this.pairLabel.setText("");
    }

    private void fillBalanceTableObservableList() {
        List<UserBalanceDto> balanceDtoList = userInfoService.getBalanceDtoList();
        this.userBalanceMasterObservableList.addAll(balanceDtoList);
    }

    private void filterBalanceTable() {
        this.userBalanceFilterObservableList.clear();

        if (!allCurrencyCheckBox.isSelected()) {
            userBalanceFilterObservableList.addAll(userBalanceMasterObservableList);
        } else {
            userBalanceMasterObservableList.forEach(userBalanceDto -> {
                if (userBalanceDto.getAmountBalance().compareTo(BigDecimal.ZERO) != 0 || userBalanceDto.getAmountReserved().compareTo(BigDecimal.ZERO) != 0) {
                    userBalanceFilterObservableList.add(userBalanceDto);
                }
            });
        }
        this.userBalanceTable.setItems(userBalanceFilterObservableList);
    }

    private void fillBalanceTableColumn() {
        this.currencyColumn.setCellValueFactory(param -> param.getValue().currencyProperty());
        this.amountBalanceColumn.setCellValueFactory(param -> param.getValue().amountBalanceProperty());
        this.amountReservedColumn.setCellValueFactory(param -> param.getValue().amountReservedProperty());
    }

    private void addListener() {
        this.allCurrencyCheckBox.selectedProperty().addListener((observable1, oldValue1, newValue1) -> {
            filterBalanceTable();
        });
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

        this.quantityBuyTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                calcBidTextField();
            }
        });
        this.priceBuyTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                calcBidTextField();
            }
        });
        this.quantitySellTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                calcAskTextField();
            }
        });
        this.priceSellTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                calcAskTextField();
            }
        });
        this.balanceBuyTextField.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                calcBidByBalance();
            }
        });
        this.balanceSellTextField.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                calcAskByBalance();
            }
        });
    }

    private void fillTickerPairTable(UserBalanceDto userBalanceDto) {
        this.tickerPairObservableList.clear();

        if (userBalanceDto != null) {
            List<TickerDto> tickerDtoList = tickerService.getTickerListByCurrency(userBalanceDto.currencyProperty().get());
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

            this.pairLabel.setText(pair);
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
            this.quantityBuyTextField.setText(value.getAmount().toPlainString());
            this.priceBuyTextField.setText(value.getPrice().toPlainString());
            this.priceSellTextField.setText(value.getPrice().toPlainString());
        } else {
            this.quantityBuyTextField.setText("");
            this.priceBuyTextField.setText("");
        }
    }

    private void fillAskTextField(OrderBookDto.Bid value) {
        if (value != null) {
            this.quantitySellTextField.setText(value.getAmount().toPlainString());
            this.priceSellTextField.setText(value.getPrice().toPlainString());
            this.priceBuyTextField.setText(value.getPrice().toPlainString());
        } else {
            this.quantitySellTextField.setText("");
            this.priceSellTextField.setText("");
        }
    }


    private void calcBidTextField() {
        BigDecimal quantity = DecimalUtil.parse(this.quantityBuyTextField.getText());
        BigDecimal price = DecimalUtil.parse(this.priceBuyTextField.getText());

        BigDecimal total = quantity.multiply(price);
        BigDecimal commission = quantity.multiply(BigDecimal.valueOf(IExConst.STOCK_FEE));
        BigDecimal balance = userBalanceDto.getAmountBalance().subtract(total.add(commission));

        this.totalBuyTextField.setText(DecimalUtil.round(total).toPlainString());
        this.commissionBuyTextField.setText(DecimalUtil.round(commission).toPlainString());
        this.balanceBuyTextField.setText(DecimalUtil.round(balance).toPlainString());
    }

    private void calcAskTextField() {
        BigDecimal quantity = DecimalUtil.parse(this.quantitySellTextField.getText());
        BigDecimal price = DecimalUtil.parse(this.priceSellTextField.getText());

        BigDecimal total = quantity.multiply(price);
        BigDecimal commission = quantity.multiply(BigDecimal.valueOf(IExConst.STOCK_FEE));
        BigDecimal balance = userBalanceDto.getAmountBalance().subtract(total.add(commission));

        this.totalSellTextField.setText(DecimalUtil.round(total).toPlainString());
        this.commissionSellTextField.setText(DecimalUtil.round(commission).toPlainString());
        this.balanceSellTextField.setText(DecimalUtil.round(balance).toPlainString());
    }

    private void calcBidByBalance() {
        if (this.priceBuyTextField.getText() != null) {
            BigDecimal price = DecimalUtil.parse(this.priceBuyTextField.getText());
            BigDecimal quantity = new BigDecimal(userBalanceDto.getAmountBalance().doubleValue() / price.doubleValue());

            this.quantityBuyTextField.setText(DecimalUtil.round(quantity).toPlainString());
        }
    }

    private void calcAskByBalance() {
        if (this.priceSellTextField.getText() != null) {
            BigDecimal price = DecimalUtil.parse(this.priceSellTextField.getText());
            BigDecimal quantity = new BigDecimal(userBalanceDto.getAmountBalance().doubleValue() / price.doubleValue());

            this.quantitySellTextField.setText(DecimalUtil.round(quantity).toPlainString());
        }
    }

    private void handleTrailingDialog(ObservableList<TickerDto> tickerPairObservableList) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/TrailingView.fxml"));

            Parent parent = (Parent) fxmlLoader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle("Trailing Stop");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(parent);
            modalStage.setScene(scene);

            TrailingViewController trailingViewController = (TrailingViewController) fxmlLoader.getController();
            trailingViewController.initCtrl(tickerPairObservableList);
            trailingViewController.setDialogStage(modalStage);

            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
