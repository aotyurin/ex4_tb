package ru.ex4.apibt.view;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import ru.ex4.apibt.dto.LossOrderDto;
import ru.ex4.apibt.view.fxmlManager.IFxmlController;
import ru.ex4.apibt.view.fxmlManager.IFxmlDto;

public class LossOrderEditDialogController implements IFxmlController {
    private Stage dialogStage;
    private LossOrderDto lossOrderDto;

    public void initCtrl(LossOrderDto lossOrderDto) {
        this.lossOrderDto = lossOrderDto;
    }

    @Override
    public void initCtrl(IFxmlDto t) {

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public LossOrderDto getResult() {
        return this.lossOrderDto;
    }

    public void btnOk(ActionEvent actionEvent) {

    }

    public void btnCancel(ActionEvent actionEvent) {

    }
}
