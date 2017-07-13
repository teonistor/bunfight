package io.github.teonistor.suhc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {

	@FXML private TextField name, email;
	@FXML private Button register;
	@FXML private ImageView photos;
	@FXML private Text quotes, prompt;
	
	@FXML private void initialize() {
		System.out.println("Initializing controller...");
		
	}
	
	@FXML private void onButtonRegister() {
		
	}
}
