package pl.maciejbiel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pl.maciejbiel.utils.HibernateUtil;
import pl.maciejbiel.utils.InitData;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        InitData init = new InitData();
        FXMLLoader loader = App.loadFXML("Home");
        Parent root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        setCss(scene, "main");
        setIcon(stage);
        setTitle(stage, "Home");
        stage.setResizable(false);
        stage.show();
    }

    public static void setCss(Scene scene, String css) {
        scene.getStylesheets().add(App.class.getResource("css/" + css + ".css").toExternalForm());
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&display=swap");
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml).load());
    }

    public static void setIcon(Stage stage) {
        stage.getIcons().add(new Image(App.class.getResourceAsStream("img/icon.png")));
    }

    public static void setTitle(Stage stage, String title) {
        stage.setTitle("FX-Komp - " + title);
    }

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource("fxml/" + fxml + ".fxml"));
    }

    public static void main(String[] args) {
        launch();
    }
}
