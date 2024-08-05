import TouchGrassModel.TouchGrassModel;
import javafx.application.Application;
import javafx.stage.Stage;
import views.TouchGrassView;
import java.io.IOException;

/**
 * Class TouchGrassApp.
 */
public class TouchGrassApp extends  Application {

    TouchGrassModel model;
    TouchGrassView view;

    public static void main(String[] args) {
        launch(args);
    }

    /*
     * JavaFX is a Framework, and to use it we will have to
     * respect its control flow!  To start the game, we need
     * to call "launch" which will in turn call "start" ...
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.model = new TouchGrassModel();
        this.view = new TouchGrassView(model, primaryStage);
    }
}
