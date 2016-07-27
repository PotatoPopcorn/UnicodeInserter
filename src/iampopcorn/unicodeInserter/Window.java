package iampopcorn.unicodeInserter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Window extends Application {
	Parser parser = new Parser();

	// JavaFX components
	Scene scene;

	VBox root = new VBox();
	HBox unicode = new HBox();

	Button unicodeCodeGoButton;
	Label unicodeCodeLabel;
	TextField unicodeCode;
	TextField outputText;

	ScrollPane charSelector;
	VBox prevCharBox;

	FileHandler fileHand;
	String[] prevChars;

	public void start(Stage primaryStage) {
		// Init helper code
		fileHand = new FileHandler();

		// Init JavaFX components
		unicodeCodeGoButton = new Button("Insert");
		unicodeCodeLabel = new Label("Unicode Code: U+");
		unicodeCode = new TextField();
		outputText = new TextField();

		charSelector = new ScrollPane();
		prevCharBox = new VBox();
		prevCharBox.setMinWidth(400);
		prevCharBox.setMaxWidth(400);
		charSelector.setContent(prevCharBox);
		charSelector.setMinHeight(250);
		charSelector.setMaxHeight(250);

		// Use Filehandler to get previous characters
		prevChars = fileHand.loadCharacters(100);
		if (prevChars != null) {
			updatePrevChars();
		} else {
			Label unavaliableLabel = new Label("Prev Chars Unavaliable");
			prevCharBox.getChildren().add(unavaliableLabel);
			prevChars = new String[100];
		}
		// Set on actions
		unicodeCodeGoButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addCode("U+" + unicodeCode.getText());
			}
		});
		unicodeCode.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				addCode("U+" + unicodeCode.getText());
			}
		});

		// Setup JavaFX Window
		unicode.getChildren().addAll(unicodeCodeLabel, unicodeCode, unicodeCodeGoButton);
		root.getChildren().addAll(charSelector, unicode, outputText);
		scene = new Scene(root);
		primaryStage.setTitle("Unicode Inserter v2");
		primaryStage.getIcons().add(new Image("res/logo.png"));
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void addCode(String unicodeString1) {
		// Add U+ infront of the code and turn it to upper case
		if (unicodeString1 == "U+") {
			return;
		}
		unicodeString1 = unicodeString1.toUpperCase();
		fileHand.rememberCharacter(unicodeString1);
		unicodeCode.setText("");
		if (!validHex(unicodeString1)) {
			return;
		}
		addCharacterToList(unicodeString1);

		// Add text to the output text box
		String outputString = outputText.getText();
		outputString = outputString + parser.convertToString(unicodeString1);
		outputText.setText(outputString);

	}

	// init JavaFX
	public void begin(String[] args) {
		launch(args);
	}

	private void addCharacterToList(String s) {
		String[] d;
		if (prevChars.length < 99) {
			d = new String[prevChars.length + 1];
		} else {
			d = new String[100];
		}
		d[0] = s;
		for (int i = 1; i < d.length; i++) {
			d[i] = prevChars[i - 1];
		}
		prevChars = d;
		updatePrevChars();
	}

	private void updatePrevChars() {
		prevCharBox.getChildren().clear();
		for (int i = 0; i < prevChars.length; i++) {
			if (prevChars[i] == null) {
				break;
			}
			HBox hb = new HBox();
			hb.alignmentProperty();
			Label l1 = new Label(parser.convertToString(prevChars[i]));
			l1.setMinWidth(100);
			l1.setMaxWidth(100);
			Label l2 = new Label(prevChars[i]);
			l2.setMinWidth(150);
			l2.setMaxWidth(150);
			Button b = new Button("Insert");
			String bs = prevChars[i];
			b.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					String outputString = outputText.getText();
					outputString = outputString + parser.convertToString(bs);
					outputText.setText(outputString);
				}
			});
			hb.getChildren().addAll(l1, l2, b);
			prevCharBox.getChildren().add(hb);
		}
	}

	private boolean validHex(String p) {
		char[] arr = p.toCharArray();
		if (arr[0] != 'U' || arr[1] != '+') {
			return false;
		}
		for (int i = 2; i < arr.length; i++) {
			if (arr[i] != '0' && arr[i] != '1' && arr[i] != '2' && arr[i] != '3' && arr[i] != '4' && arr[i] != '5'
					&& arr[i] != '6' && arr[i] != '7' && arr[i] != '8' && arr[i] != '9' && arr[i] != 'A'
					&& arr[i] != 'B' && arr[i] != 'C' && arr[i] != 'D' && arr[i] != 'E' && arr[i] != 'F') {
				System.out.println("Invalid Char");
				return false;
			}
		}
		return true;
	}
}
