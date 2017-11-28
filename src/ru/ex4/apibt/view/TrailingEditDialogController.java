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

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class TrailingEditDialogController {
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

    @FXML
    private void initialize() {
    }

    @FXML
    private void btnOk() {
        if (isInputValid()) {
            try {
                this.trailingDto = new TrailingDto(this.pairTrailingLabel.getText(),
                        TrendType.valueOf(this.trendTypeTrailingComboBox.getSelectionModel().getSelectedItem()),
                        DecimalUtil.parse(this.priceTrailingTextField.getText()),
                        DateUtil.parse(this.dateCreatedTrailingLabel.getText()),
                        DateUtil.parse(this.dateNotifyTrailingLabel.getText()));

                dialogStage.close();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnCancel() {
        this.trailingDto = null;
        dialogStage.close();
    }


    public void initCtrl(String pair, TrailingDto trailingDto) {
        this.trailingDto = trailingDto;

        fillComboBox();
        fillViewControls(pair, trailingDto);
    }

    private void fillComboBox() {
        List<TrendType> trendTypes = TrendType.getList();
        for (TrendType trendType : trendTypes) {
            this.trendTypeTrailingComboBox.getItems().add(trendType.name());
        }
    }

    private void fillViewControls(String pair, TrailingDto trailingDto) {
        if (trailingDto != null) {
            this.pairTrailingLabel.setText(trailingDto.pairProperty().get());
            this.trendTypeTrailingComboBox.getSelectionModel().select(trailingDto.trendTypeProperty().get());
            this.priceTrailingTextField.setText(String.valueOf(trailingDto.priceProperty().get()));
            this.dateCreatedTrailingLabel.setText(trailingDto.dateCreatedProperty().get());
            this.dateNotifyTrailingLabel.setText(trailingDto.dateNotifyProperty().get());
        } else {
            this.pairTrailingLabel.setText(pair);
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

    public TrailingDto getResult() {
        return this.trailingDto;
    }

}
