package io.github.teonistor.suhc;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch();
	}
	
	public Main() {}
	

	public void start(Stage stage) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		try {
			Scene scene = new Scene((Parent)(loader.load()));
			scene.setOnKeyTyped(e -> {
				if (e.getCode().equals(KeyCode.ESCAPE))
					stage.hide();
			});
			
			stage.setScene(scene);
			stage.setFullScreen(true);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AnimationTimer t; // TODO
	}


}
