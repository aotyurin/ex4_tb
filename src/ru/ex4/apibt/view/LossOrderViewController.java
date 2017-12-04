package ru.ex4.apibt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ex4.apibt.Main;
import ru.ex4.apibt.dto.LossOrderDto;
import ru.ex4.apibt.service.LossOrderService;
import ru.ex4.apibt.view.fxmlManager.IFxmlController;
import ru.ex4.apibt.view.fxmlManager.IFxmlDto;
import ru.ex4.apibt.view.fxmlManager.LoaderFxmlController;

import java.io.IOException;
import java.util.List;

public class LossOrderViewController implements IFxmlController {
    private ObservableList<LossOrderDto> lossOrderObservableList;
    private Stage dialogStage;
    private LossOrderService lossOrderService;

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


    @Override
    public void initCtrl(IFxmlDto t) {

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @Override
    public IFxmlDto getResult() {
        return null;
    }

    public LossOrderViewController() {
        this.lossOrderObservableList = FXCollections.observableArrayList();

        this.lossOrderService = new LossOrderService();
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

    public void initCtrl() {
        fillLossOrderTable();
        fillLossOrderTableColumn();
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
        LossOrderDto lossOrderDtoRezult = showEditDialog(lossOrderDto);
        if (lossOrderDtoRezult != null) {
            lossOrderService.save(lossOrderDtoRezult);
        }
    }


    private LossOrderDto showEditDialog(LossOrderDto lossOrderDto) {
        LoaderFxmlController fxmlController = new LoaderFxmlController<LossOrderEditDialogController>();
        return (LossOrderDto) fxmlController.lossOrderEditDialog(lossOrderDto, "LossOrderEditDialog");

//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/LossOrderEditDialogController.fxml"));
//            Parent parent = (Parent) fxmlLoader.load();
//
//            Stage modalStage = new Stage();
//            modalStage.setTitle("new title");
//            modalStage.initModality(Modality.APPLICATION_MODAL);
//            Scene scene = new Scene(parent);
//            modalStage.setScene(scene);
//
//            LossOrderEditDialogController editDialogController = (LossOrderEditDialogController) fxmlLoader.getController();
//            editDialogController.initCtrl(lossOrderDto);
//            editDialogController.setDialogStage(modalStage);
//
//            modalStage.showAndWait();
//
//            return editDialogController.getResult();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }


}
