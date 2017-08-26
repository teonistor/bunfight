package io.github.teonistor.suhc;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ButtonType.CLOSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Controller {
	
	static final String PHOTOS_DIR = "Photos",
						QUOTES_FILE = "Quotes.txt",
						PROMPT_THANKS = "Thank you!",
					    PROMPT_REGISTER = "Register for mailing list:",
					    REGISTRAR_FILE = "members.csv";
	static final int	PHOTO_DURATION_MILLI = 9696;
	static final double PHOTO_FADE_NANO = 696969696;
	static final double PROMPT_FADE_NANO = 333333333;

	private Random random;
	private Registrar registrar;

	@FXML private TextField name, email;
	@FXML private Text prompt, nameT, emailT;
	@FXML private Button register;
	@FXML private Pane photoFrame, quotesFrame;
	@FXML private ImageView photos;
//	@FXML private AnchorPane quotesFrame;
	
	private Iterator<Image> photoCache;
	private Iterator<Text> quotesCache;
	private AnimationTimer photoChange, photoTimer, registrationAnimation, quotesChange;
	
	@FXML private void initialize() {
		random = new Random();
		registrar = new Registrar(REGISTRAR_FILE);

		// Image position and size adjustments
		photoFrame.heightProperty().addListener(this::adjustPhotosHeight);
		photoFrame.widthProperty().addListener(this::adjustPhotosWidth);
		photos.imageProperty().addListener(this::adjustPhotosPosition);
		adjustPhotosHeight(null);
		adjustPhotosWidth(null);
		
		photoCache = new CacheIterator<>(new File(PHOTOS_DIR).listFiles(),
				file -> { try {
					return new Image(new FileInputStream(file));
				} catch (IOException e){
					e.printStackTrace();
					return null;
				}}
			);
		/**
		 * 
		 */
		quotesCache = new CacheIterator<String, Text>(() -> {
				Collection<String> result = new ArrayList<>();
				try (BufferedReader r = new BufferedReader(new FileReader(QUOTES_FILE))) {
					String line, q = "";
					while ((line = r.readLine()) != null) {
						if (line.isEmpty()) {
							if (!q.isEmpty()) {
								result.add(q);
								q = "";
							}
							continue;
						}
						if (q.isEmpty())
							q = line;
						else
							q = String.format("%s\n%s", q, line);
					}
					if (!q.isEmpty())
						result.add(q);
				} catch (IOException e) {
					complain("", "Error reading quotes from file", e);
				}
//				result.forEach(q -> {
//					System.out.println(q);
//					System.out.println("----------");
//				});
				return result;
			}, Text::new);
		
//		quotesCache = new CacheIterator<>(((Function<String, String[]>) filename -> {
//				return new String[4];
//			}).apply(QUOTES_FILE),
//				Text::new);
		
		/*...
		 * ...
		 * This is a fundamental and user-triggered animation
		 */
		photoChange = new AnimationTimer() {
			
			static final short STOP = 1, DESCENT = 2, ASCENT = 4;
			
			private long beginning;
			private short status = STOP;
			
			@Override public void handle(long now) {
				double opacity;
				
				switch (status) {
					case STOP:
						beginning = now;
						status = DESCENT;
						return;
					case DESCENT:
						opacity = (PHOTO_FADE_NANO - now + beginning) / PHOTO_FADE_NANO;
						photos.setOpacity(opacity);
						if (opacity <= 0.0) {
							photos.setImage(photoCache.next());
							photos.setRotate(random.nextDouble() * 10 - 5);
							beginning = now;
							status = ASCENT;
						}
						return;
					case ASCENT:
						opacity = ((double) now - beginning) / PHOTO_FADE_NANO;
						photos.setOpacity(opacity);
						if (opacity >= 1.0) {
							status = STOP;
							stop();
							photoTimer.start();
						}
				}
			}
		};
		
		/* Invisible animation that acts like a timer to trigger the displayed picture to
		 * be changed at fixed intervals
		 * This is a non-fundamental animation (it is triggered from within another animation)
		 */
		photoTimer = new AnimationTimer() {
			
			private long target;
			
			@Override public void start() {
				target = System.currentTimeMillis() + PHOTO_DURATION_MILLI;
				super.start();
			}
			
			public void handle(long now) {
				if (System.currentTimeMillis() > target) {
					photoChangeTrigger();
					stop();
				}
			}
		};
		
		/* Animation to fade in and out the registration form and the "Thank you" message
		 * ....
		 * This is a non-fundamental, user-triggered animation
		 */
		registrationAnimation = new AnimationTimer() {
			
			static final short STOP = 1, DESCENT_ALL = 2, ASCENT_THANK = 4,
							   DESCENT_THANK = 8, ASCENT_ALL = 16;
			
			private long beginning;
			private short status = STOP;
			
			@Override public void handle(long now) {
				double opacity;
				
				switch (status) {
					case STOP:
						beginning = now;
						register.setDisable(true);
						status = DESCENT_ALL;
						return;
					case DESCENT_ALL:
						opacity = (PROMPT_FADE_NANO - now + beginning) / PROMPT_FADE_NANO;
						setRegistrationBlockOpacity(opacity);
						if (opacity <= 0.0) {
							prompt.setText(PROMPT_THANKS);
							beginning = now;
							status = ASCENT_THANK;
						}
						return;
					case ASCENT_THANK:
						opacity = ((double) now - beginning) / PROMPT_FADE_NANO;
						prompt.setOpacity(opacity);
						if (opacity >= 1.0) {
							name.setText("");
							email.setText("");
							beginning = now;
							status = DESCENT_THANK;
						}
						return;
					case DESCENT_THANK:
						opacity = (PROMPT_FADE_NANO - now + beginning) / PROMPT_FADE_NANO;
						prompt.setOpacity(opacity);
						if (opacity <= 0.0) {
							prompt.setText(PROMPT_REGISTER);
							beginning = now;
							status = ASCENT_ALL;
						}
						return;
					case ASCENT_ALL:
						opacity = ((double) now - beginning) / PROMPT_FADE_NANO;
						setRegistrationBlockOpacity(opacity);
						if (opacity >= 1.0) {
							register.setDisable(false);
							status = STOP;
							stop();
						}
				}
			}
		};
		
		/* Animation to create and display a new, randomly-picked, quote, on screen,
		 * at regular intervals.
		 * This is a fundamental animation (it is started directly during setup)
		 */
		quotesChange = new AnimationTimer() {
			final long spawnInterval = 4000000000l;
			
			private long lastMove = Long.MIN_VALUE;
			
//			{
//				long n
//			}
			
			public void handle(long now) {
//				System.out.println(now + "  " + lastMove);
				if (now > spawnInterval + lastMove) {
					lateSetupQuote(quotesCache.next());
					lastMove = now;
//					System.out.println("Handled!");
				}
			}
		};
		
		// Launch the two fundamental animations
		quotesChange.start();
		photoChangeTrigger();
	}
	
	@FXML private void photoChangeTrigger() {
		photoChange.start();
	}

	@FXML private void onButtonRegister() {
		String name = this.name.getText(),
			   email = this.email.getText();
		if (name.isEmpty() || email.isEmpty())
			return;
		registrationAnimation.start();
		try {
			registrar.register(name, email);
		}
		catch (RuntimeException e) {
			complain("Error", "Could not save registration info", e);
		}
	}
	
	/**
	 * Perform setup on a Text element representing a quote, which must be done immediately
	 * before being displayed rather than at instantiation time. This includes setting its
	 * color and width; listening for its vertical position so that it's removed when going
	 * off-screen; adding a translation animation to scroll the text up the screen; and
	 * adding the text to its actual container.
	 * @param quote The Text element to be set up
	 */
	private void lateSetupQuote (Text quote) {
		quote.setStyle("-fx-fill: white");
		quote.setWrappingWidth(quotesFrame.getWidth());
		quote.translateYProperty().addListener(o -> {
			if (quote.getTranslateY() < -80)
				quotesFrame.getChildren().remove(quote);
		});
		
		TranslateTransition tt = new TranslateTransition(Duration.seconds(20), quote);
		tt.setFromY(quotesFrame.getHeight() - 100);
		tt.setInterpolator(Interpolator.LINEAR);
		tt.setToY(-100);
		tt.play();
		
		quotesFrame.getChildren().add(quote);
	}

	/**
	 * Set the opacity of all UI elements that form the registration box in one go
	 * @param opacity The new opacity of registration box UI elements
	 */
	private void setRegistrationBlockOpacity(double opacity) {
		prompt.setOpacity(opacity);
		nameT.setOpacity(opacity);
		emailT.setOpacity(opacity);
		name.setOpacity(opacity);
		email.setOpacity(opacity);
		register.setOpacity(opacity);
	}

	/**
	 * Auto-triggered upon photo frame height change, this method ensures the image view matches the height of its parent frame.
	 * @param o Ignored
	 */
	private void adjustPhotosHeight (Observable o) {
		photos.setFitHeight(photoFrame.getHeight());
		adjustPhotosPosition(o);
	}
	
	/**
	 * Auto-triggered upon photo frame width change, this method ensures the image view matches the width of its parent frame.
	 * @param o Ignored
	 */
	private void adjustPhotosWidth (Observable o) {
		photos.setFitWidth(photoFrame.getWidth());
		adjustPhotosPosition(o);
	}
	
	/**
	 * Auto-triggered when the image updates, this method ensures the image view is centered within its frame. It does soby comparing the aspect ratio of the image to that of the frame and adding half the slack space to either X or Y position, whichever exceeds the requirements of the image.
	 * @param o Ignored
	 */
	private void adjustPhotosPosition (Observable o) {
		Image image = photos.getImage();
		if (image != null) {
			double imageRatio = image.getWidth() / image.getHeight();
			double frameRatio = photos.getFitWidth() / photos.getFitHeight();
			if (frameRatio > imageRatio) {
				photos.setLayoutX((frameRatio - imageRatio) / 2 * photos.getFitHeight());
				photos.setLayoutY(0.0);
			}
			else {
				photos.setLayoutX(0.0);
				photos.setLayoutY((imageRatio - frameRatio) / (2 * imageRatio * frameRatio) * photos.getFitWidth());
			}
		}
	}
	
	/**
	 * Utility method to show a blocking error message with the given information. The summary will be shown on the first line, followed by the given exception's string representation on the second line. The stack trace will be printed to stderr
	 * @param title The title of the error dialog
	 * @param summary The first line of the error summary
	 * @param cause The exception that occurred
	 */
	static void complain (String title, String summary, Exception cause) {
		cause.printStackTrace();
		Alert a = new Alert(ERROR, String.format("%s\n(%s)", summary, cause), CLOSE);
		a.setTitle(title);
		a.showAndWait();
	}
}
