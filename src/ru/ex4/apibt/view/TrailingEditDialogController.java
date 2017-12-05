package ru.ex4.apibt.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.ex4.apibt.dto.TrailingDto;
import ru.ex4.apibt.model.TrendType;
import ru.ex4.apibt.util.DateUtil;
import ru.ex4.apibt.util.DecimalUtil;
import ru.ex4.apibt.view.fxmlManager.IFxmlController;
import ru.ex4.apibt.view.fxmlManager.IFxmlDto;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class TrailingEditDialogController implements IFxmlController {
    private TrailingDto trailingDto;
    private Stage dialogStage;

    @FXML
    private Label pairTrailingLabel;
    @FXML
    private ComboBox<String> trendTypeTrailingComboBox;
    @FXML
    private TextField priceTrailingTextField;
    @FXML
    private Label dateCreatedTrailingLabel;
    @FXML
    private Label dateNotifyTrailingLabel;
    @FXML
    private Label priceTrailingErrorTextField;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @Override
    public void initCtrl(IFxmlDto trailingDto) {
        if (trailingDto != null && trailingDto instanceof TrailingDto) {
            this.trailingDto = (TrailingDto) trailingDto;

            fillComboBox();
            fillViewControls(this.trailingDto);
        }
    }

    @Override
    public IFxmlDto getResult() {
        return this.trailingDto;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void btnOk() {
        if (isInputValid() && this.trailingDto != null) {
            TrailingDto tmp = this.trailingDto;
            this.trailingDto = new TrailingDto(tmp.getPair(),
                    TrendType.valueOf(this.trendTypeTrailingComboBox.getSelectionModel().getSelectedItem()),
                    DecimalUtil.parse(this.priceTrailingTextField.getText()),
                    tmp.getDateCreated(),
                    tmp.getDateNotify());

            dialogStage.close();
        }
    }

    @FXML
    private void btnCancel() {
        this.trailingDto = null;
        dialogStage.close();
    }


    private void fillComboBox() {
        List<TrendType> trendTypes = TrendType.getList();
        for (TrendType trendType : trendTypes) {
            this.trendTypeTrailingComboBox.getItems().add(trendType.name());
        }
    }

    private void fillViewControls(TrailingDto trailingDto) {
        if (trailingDto != null) {
            this.pairTrailingLabel.setText(trailingDto.pairProperty().get());
            this.trendTypeTrailingComboBox.getSelectionModel().select(trailingDto.trendTypeProperty().get());
            this.priceTrailingTextField.setText(String.valueOf(trailingDto.priceProperty().get()));
            this.dateCreatedTrailingLabel.setText(trailingDto.dateCreatedProperty().get());
            this.dateNotifyTrailingLabel.setText(trailingDto.dateNotifyProperty().get());
        } else {
            this.pairTrailingLabel.setText("");
            this.priceTrailingTextField.setText("");
            this.dateCreatedTrailingLabel.setText(DateUtil.format(new Date()));
            this.dateNotifyTrailingLabel.setText("");
        }
    }

    private boolean isInputValid() {
        StringBuilder msg = new StringBuilder();
        String text = this.priceTrailingTextField.getText();
        try {
            if (text.equals("")) {
                msg.append("поле 'price' не заполнено! \n");
            }
            BigDecimal i = new BigDecimal(text);
        } catch (NumberFormatException ignore) {
            msg.append("в поле 'price' значение ").append(text).append(" не является числом! \n");
        }

        this.priceTrailingErrorTextField.setText(msg.toString());

        return msg.length() == 0;
    }


}
