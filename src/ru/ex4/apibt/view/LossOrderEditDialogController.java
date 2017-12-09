package ru.ex4.apibt.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ex4.apibt.dto.LossOrderDto;
import ru.ex4.apibt.view.fxmlManager.IFxmlController;
import ru.ex4.apibt.view.fxmlManager.IFxmlDto;

import java.math.BigDecimal;

public class LossOrderEditDialogController implements IFxmlController {
    private Stage dialogStage;
    private LossOrderDto lossOrderDto;

    @FXML
    public Label pairLossOrderLabel;
    @FXML
    public Label quantityLossOrderLabel;
    @FXML
    public Label priceLossOrderLabel;
    @FXML
    public Label typeOrderLossOrderLabel;
    @FXML
    public TextField priceStepLossOrderTextField;
    @FXML
    public TextField priceLossLossOrderTextField;
    @FXML
    public Label errorLossOrderTextField;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @Override
    public void initCtrl(IFxmlDto lossOrderDto) {
        if (lossOrderDto != null && lossOrderDto instanceof LossOrderDto) {
            this.lossOrderDto = (LossOrderDto) lossOrderDto;

            fillViewControls(this.lossOrderDto);
        }
    }

    @Override
    public LossOrderDto getResult() {
        return this.lossOrderDto;
    }

    @FXML
    public void btnOk(Event actionEvent) {
        if (isInputValid() && this.lossOrderDto != null) {
            LossOrderDto tmp = this.lossOrderDto;
            this.lossOrderDto = new LossOrderDto(tmp.getPair(),
                    tmp.getQuantity(),
                    tmp.getPrice(),
                    tmp.getType(),
                    new BigDecimal(this.priceStepLossOrderTextField.getText()),
                    new BigDecimal(this.priceLossLossOrderTextField.getText()));
            dialogStage.close();
        }
    }

    @FXML
    public void btnCancel(Event actionEvent) {
        this.lossOrderDto = null;
        dialogStage.close();
    }

    private void fillViewControls(LossOrderDto lossOrderDto) {
        this.pairLossOrderLabel.setText(lossOrderDto.pairProperty().get());
        this.quantityLossOrderLabel.setText(String.valueOf(lossOrderDto.quantityProperty().get()));
        this.priceLossOrderLabel.setText(lossOrderDto.getPrice().toPlainString());
        this.typeOrderLossOrderLabel.setText(lossOrderDto.getType().name());
        this.priceStepLossOrderTextField.setText(lossOrderDto.getPriceStep().toPlainString());
        this.priceLossLossOrderTextField.setText(lossOrderDto.getPriceLoss().toPlainString());
    }

    private boolean isInputValid() {
        StringBuilder msg = new StringBuilder();
        String priceStep = this.priceStepLossOrderTextField.getText();
        String priceLoss = this.priceLossLossOrderTextField.getText();

        checkNumberField(msg, priceStep, "priceStep");
        checkNumberField(msg, priceLoss, "priceLoss");

        this.errorLossOrderTextField.setText(msg.toString());

        return msg.length() == 0;
    }

    private void checkNumberField(StringBuilder msg, String field, String nameField) {
        if (field.equals("")) {
            msg.append("поле '").append(nameField).append("' не заполнено! \n");
        }
        try {
            BigDecimal i = new BigDecimal(field);
        } catch (NumberFormatException ignore) {
            msg.append("в поле '").append(nameField).append("' значение ").append(field).append(" не является числом! \n");
        }
    }


}
