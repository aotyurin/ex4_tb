package ru.ex4.apibt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ru.ex4.apibt.dto.LossOrderDto;
import ru.ex4.apibt.service.LossOrderService;
import ru.ex4.apibt.view.fxmlManager.IFxmlController;
import ru.ex4.apibt.view.fxmlManager.IFxmlDto;
import ru.ex4.apibt.view.fxmlManager.LoaderFxmlController;

import java.util.List;

public class LossOrderViewController implements IFxmlController {
    private ObservableList<LossOrderDto> lossOrderObservableList;
    private LossOrderService lossOrderService;
    private Stage dialogStage;

    @FXML
    public TableView<LossOrderDto> lossOrderTable;

    @FXML
    public TableColumn<LossOrderDto, String> pairLossOrderTableColumn;
    @FXML
    public TableColumn<LossOrderDto, Number> priceLossOrderTableColumn;
    @FXML
    public TableColumn<LossOrderDto, Number> quantityLossOrderTableColumn;
    @FXML
    public TableColumn<LossOrderDto, String> typeLossOrderTableColumn;
    @FXML
    public TableColumn<LossOrderDto, Number> priceStepLossOrderTableColumn;
    @FXML
    public TableColumn<LossOrderDto, Number> priceLossLossOrderTableColumn;
    @FXML
    public TableColumn<LossOrderDto, String> createdLossOrderTableColumn;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public LossOrderViewController() {
        this.lossOrderObservableList = FXCollections.observableArrayList();

        this.lossOrderService = new LossOrderService();
    }

    @Override
    public IFxmlDto getResult() {
        return null;
    }

    @Override
    public void initCtrl(IFxmlDto fxmlDto) {
        fillLossOrderTable();
        fillLossOrderTableColumn();
    }

    @FXML
    private void initialize() {
    }

    @FXML
    public void OnChangeLossOrderDialogShow(ActionEvent actionEvent) {
        LossOrderDto selectedItem = this.lossOrderTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            handleLossOrderEditDialog(selectedItem);
        }
    }

    @FXML
    public void OnDeleteLossOrder(ActionEvent actionEvent) {
        LossOrderDto selectedItem = this.lossOrderTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lossOrderService.delete(selectedItem);
            this.lossOrderTable.getItems().remove(selectedItem);
        }
    }

    @FXML
    public void btnOk(ActionEvent actionEvent) {
        dialogStage.close();
    }

    private void fillLossOrderTable() {
        List<LossOrderDto> trailingDtoList = lossOrderService.getTrailingDtoList();
        this.lossOrderObservableList.addAll(trailingDtoList);
        this.lossOrderTable.setItems(lossOrderObservableList);
    }

    private void fillLossOrderTableColumn() {
        this.pairLossOrderTableColumn.setCellValueFactory(param -> param.getValue().pairProperty());
        this.priceLossOrderTableColumn.setCellValueFactory(param -> param.getValue().priceProperty());
        this.quantityLossOrderTableColumn.setCellValueFactory(param -> param.getValue().quantityProperty());
        this.typeLossOrderTableColumn.setCellValueFactory(param -> param.getValue().typeProperty());
        this.priceStepLossOrderTableColumn.setCellValueFactory(param -> param.getValue().priceStepProperty());
        this.priceLossLossOrderTableColumn.setCellValueFactory(param -> param.getValue().priceLossProperty());
        this.createdLossOrderTableColumn.setCellValueFactory(param -> param.getValue().createdProperty());
    }

    private void handleLossOrderEditDialog(LossOrderDto lossOrderDto) {
        LoaderFxmlController fxmlController = new LoaderFxmlController<LossOrderEditDialogController>();
        LossOrderDto result = (LossOrderDto) fxmlController.lossOrderEditDialog(lossOrderDto, "LossOrderEditDialog");
        if (result != null) {
            lossOrderService.save(result);
        }
    }


}
