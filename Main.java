package personalexpensetracker;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        loginScreen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
