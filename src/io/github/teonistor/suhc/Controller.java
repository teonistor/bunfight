package io.github.teonistor.suhc;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Controller {
	
	private static final int PHOTO_DURATION_MILLI = 9696;
	static final double PHOTO_FADE_NANO = 696969696;

	private Random random;

	@FXML private TextField name, email;
	@FXML private Button register;
	@FXML private Pane photoFrame;
	@FXML private ImageView photos;
	@FXML private Text quotes, prompt;
	
	private Iterator<Image> photoCache;
	private AnimationTimer photoChange, photoTimer, quotesMove;
	
	@FXML private void initialize() {
		random = new Random();

		// Image position and size adjustments
		photoFrame.heightProperty().addListener(this::adjustPhotosHeight);
		photoFrame.widthProperty().addListener(this::adjustPhotosWidth);
		photos.imageProperty().addListener(this::adjustPhotosPosition);
		adjustPhotosHeight(null);
		adjustPhotosWidth(null);
		
		photoCache = new Iterator<Image>() {
			
			private List<File> files;
			private Queue<Integer> previous;
			private Image next;
			
			{
				files = new ArrayList<>(asList(new File("Photos").listFiles()));
				previous = new LinkedList<>();
				next = null;
				random = new Random();
				cacheNext();
			}
			
			public boolean hasNext() {
				return next != null;
			}

			public Image next() {
				Image next = this.next;
				cacheNext();
				return next;
			}
			
			private void cacheNext() {
				do {
					while (previous.size() > files.size() / 2)
						previous.remove();
					Integer index;
					do {
						index = random.nextInt(files.size());
					} while (previous.contains(index));
					try {
						next = new Image(new FileInputStream(files.get(index)));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} while (next == null);
			}
		};
		
//		AnimationTimer photo
		
		AnimationTimer quotesAnim = new AnimationTimer() {
			DoubleProperty y;
			
			{
				y = quotes.layoutYProperty();
				System.out.println(y.getValue());
			}
			
			public void handle(long now) {
				System.out.println(now);
				if (now>1000){
				y.setValue(y.intValue() + now/1000000000);
				System.out.println(y.getValue());
			}}
		};
		
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
		
//		quotesAnim.start();
		photoChangeTrigger();
	}
	
	/** TODO
	 * 
	 */
	@FXML private void onButtonRegister() {
		System.out.println("Not implemented");
	}
	
	@FXML private void photoChangeTrigger() {
		photoChange.start();
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
}
