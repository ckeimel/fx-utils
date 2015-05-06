package test.de.emsw.fx.customizer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import de.emsw.fx.customizer.GraphCustomizer;

public class TitledPaneTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = createContents();
			GraphCustomizer.process(root);
			Scene scene = new Scene(root);
			primaryStage.setTitle("Customized TitledPanes");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setX(primaryStage.getX() - 300);
			
			Parent root2 = createContents();
			Scene scene2 = new Scene(root2);
			Stage stage2 = new Stage();
			stage2.setTitle("Original");
			stage2.setScene(scene2);
			stage2.show();
			stage2.setX(primaryStage.getX() + 300);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Parent createContents() {
		VBox root = new VBox(6);
		root.setPadding(new Insets(6));
		root.getChildren().add(createInputPane());
		root.getChildren().add(createButtonPane());
		return root; 
	}

	private Node createInputPane() {
		VBox box = new VBox(6);
		box.getChildren().add(new TextField("Input 1"));
		box.getChildren().add(new TextField("Input 2"));
		
		TitledPane nestedCollapsible = new TitledPane("Nested", new TextField("Input 3"));
		box.getChildren().add(nestedCollapsible);
		
		TitledPane nested = new TitledPane("Nested", new TextField("Input 4"));
		nested.setCollapsible(false);
		box.getChildren().add(nested);
		
		TitledPane titledPane = new TitledPane("Buttons", box);
		return titledPane;
	}

	private Node createButtonPane() {
		HBox buttonPane = new HBox(6);
		buttonPane.setPadding(new Insets(6));
		buttonPane.setAlignment(Pos.CENTER_RIGHT);
		
		final Button defaultButton = new Button();
		defaultButton.setText("Default (ENTER)");
		defaultButton.setDefaultButton(true);
		defaultButton.setOnAction(event -> {
			System.out.println("Default button action fired");
		});
		buttonPane.getChildren().add(defaultButton);
		
		final Button cancelButton = new Button();
		cancelButton.setText("Cancel (ESC)");
		cancelButton.setCancelButton(true);
		cancelButton.setOnAction(event -> {
			System.out.println("Cancel button action fired");
		});
		buttonPane.getChildren().add(cancelButton);
		
		return buttonPane;
	}
}
