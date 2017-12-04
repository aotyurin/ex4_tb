package ru.ex4.apibt.view.fxmlManager;

import javafx.stage.Stage;

public interface IFxmlController {

    void initCtrl(IFxmlDto t);

    void setDialogStage(Stage modalStage);

    IFxmlDto getResult();
}
