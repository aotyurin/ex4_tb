package ru.ex4.apibt.view.fxmlManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ex4.apibt.Main;

import java.io.IOException;
import java.net.URL;

public class LoaderFxmlController<T extends IFxmlController> {

    public void trailingDialog(IFxmlDto userBalanceDto, String templateName) {
        fxmlLoader(userBalanceDto, templateName, "tr title");
    }

    public IFxmlDto trailingEditDialog(IFxmlDto trailingDto, String templateName) {
        return fxmlLoader(trailingDto, templateName, "tr  edit title ");
    }

    public IFxmlDto lossOrderDialog(String templateName) {
        return fxmlLoader(null, templateName, "lo title");
    }

    public IFxmlDto lossOrderEditDialog(IFxmlDto lossOrderDto, String templateName) {
        return fxmlLoader(lossOrderDto, templateName, "lo edit title");
    }


    private IFxmlDto fxmlLoader(IFxmlDto t, String templateName, String titleName) {
        try {
            URL resource = Main.class.getResource("view/" + templateName + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            Parent parent = (Parent) fxmlLoader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle(titleName);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(parent);
            modalStage.setScene(scene);

            T fxmlController = fxmlLoader.getController();
            fxmlController.initCtrl(t);
            fxmlController.setDialogStage(modalStage);

            modalStage.showAndWait();

            return fxmlController.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
