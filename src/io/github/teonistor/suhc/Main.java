package io.github.teonistor.suhc;

import static io.github.teonistor.suhc.Controller.complain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Entry point for SUHC Bunfight app
 * @author Copyright (C) 2017 Teodor Gherasim Nistor
 * <p>
 * This sotware is distributed under the terms of the GNU General Public License, version 3 or later; see www.gnu.org/licenses
 */
public class Main extends Application {

	/**
	 * Entry point for SUHC Bunfight app
	 * @param a Ignored
	 */
	public static void main(String[] a) {
		launch();
	}

	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			Scene scene = new Scene((Parent)(loader.load()));

			// This app is meant for full-screen operation only.
			stage.fullScreenProperty().addListener(e -> {
				if (!stage.isFullScreen())
					stage.hide();
			});
			
			// Setup and display window
			stage.setScene(scene);
			stage.setFullScreen(true);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("Icon.png")));
			stage.setTitle("SUHC Bunfight App");
			stage.show();

		} catch (Exception e) {
			complain("Fatal Error", "Could not read FXML specification for main window.", e);
		}
	}
}
