package io.github.teonistor.suhc;

import static io.github.teonistor.suhc.Controller.complain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch();
	}
	
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			Scene scene = new Scene((Parent)(loader.load()));
//			scene.setOnKeyTyped(e -> {
//				if (e.getCode().equals(KeyCode.ESCAPE))
//					stage.hide();
//			});
			stage.fullScreenProperty().addListener(e -> {
				if (!stage.isFullScreen())
					stage.hide();
			});
			
			stage.setScene(scene);
			stage.setMaximized(true);
//			stage.setFullScreen(true);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("Icon.png")));
			stage.setTitle("SUHC Bunfight App");
			stage.show();
		} catch (Exception e) {
			complain("Fatal Error", "Could not read FXML specification for main window.", e);
		}
	}
}
