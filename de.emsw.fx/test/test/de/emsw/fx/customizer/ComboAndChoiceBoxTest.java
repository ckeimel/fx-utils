package test.de.emsw.fx.customizer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import de.emsw.fx.customizer.GraphCustomizer;

public class ComboAndChoiceBoxTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = createContents();
			GraphCustomizer.process(root);
			Scene scene = new Scene(root);
			primaryStage.setTitle("Customized ComboBox");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setX(primaryStage.getX() - 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Parent createContents() {
		HBox box = new HBox(6);
		box.getChildren().add(createComboBoxes());
		box.getChildren().add(createChoiceBoxes());
		return box;
	}

	private Parent createComboBoxes() {
		VBox box = new VBox(6);

		box.getChildren().add(new Label("ComboBox mit Strings"));
		ComboBox<String> combo1 = new ComboBox<String>(); {
			combo1.setEditable(false);
			ObservableList<String> list = FXCollections.observableArrayList("111", "222", "Aaaa", "Abbb", "Abcc", "Abcd", "Bbb", "Ccc", "Ddd", "Eee", "Fff", "ggg", "hhh");
			combo1.setItems(list);
		}
		box.getChildren().add(combo1);
		CheckBox cb1 = new CheckBox("Editable?");
		cb1.selectedProperty().bindBidirectional(combo1.editableProperty());
		box.getChildren().add(cb1);
		
		box.getChildren().add(new Separator());
		box.getChildren().add(new Label("ComboBox mit Personen ohne Converter"));
		ComboBox<Person> combo2 = new ComboBox<Person>(); {
			combo2.setEditable(false);
			ObservableList<Person> list = FXCollections.observableArrayList(new Person("Josef Duschinger"),
					new Person("Gabi Staudt"), 
					new Person("Georg Engl"), 
					new Person("Jaqueline Wunderlich"),
					new Person("Christoph Keimel"));
			combo2.setItems(list);
		}
		box.getChildren().add(combo2);
		CheckBox cb2 = new CheckBox("Editable?");
		cb2.selectedProperty().bindBidirectional(combo2.editableProperty());
		box.getChildren().add(cb2);
		
		box.getChildren().add(new Separator());
		box.getChildren().add(new Label("ComboBox mit Personen mit Converter"));
		ComboBox<Person> combo3 = new ComboBox<Person>(); {
			combo3.setEditable(false);
			ObservableList<Person> list = FXCollections.observableArrayList(new Person("Josef Duschinger"),
					new Person("Gabi Staudt"), 
					new Person("Georg Engl"), 
					new Person("Jaqueline Wunderlich"),
					new Person("Christoph Keimel"));
			combo3.setItems(list);
			combo3.setConverter(new StringConverter<ComboAndChoiceBoxTest.Person>() {
				@Override
				public String toString(Person object) {
					return object.name;
				}
				@Override
				public Person fromString(String string) {
					return null;
				}
			});
		}
		box.getChildren().add(combo3);
		CheckBox cb3 = new CheckBox("Editable?");
		cb3.selectedProperty().bindBidirectional(combo3.editableProperty());
		box.getChildren().add(cb3);
		
		TitledPane titledPane = new TitledPane("ComboBox", box);
		return titledPane;
	}
	
	private Node createChoiceBoxes() {
		VBox box = new VBox(6);

		box.getChildren().add(new Label("ChoiceBox mit Strings"));
		ChoiceBox<String> combo1 = new ChoiceBox<String>(); {
			ObservableList<String> list = FXCollections.observableArrayList("111", "222", "Aaaa", "Abbb", "Abcc", "Abcd", "Bbb", "Ccc", "Ddd", "Eee", "Fff", "ggg", "hhh");
			combo1.setItems(list);
		}
		box.getChildren().add(combo1);
		
		box.getChildren().add(new Separator());
		box.getChildren().add(new Label("ChoiceBox mit Personen ohne Converter"));
		ChoiceBox<Person> combo2 = new ChoiceBox<Person>(); {
			ObservableList<Person> list = FXCollections.observableArrayList(new Person("Josef Duschinger"),
					new Person("Gabi Staudt"), 
					new Person("Georg Engl"), 
					new Person("Jaqueline Wunderlich"),
					new Person("Christoph Keimel"));
			combo2.setItems(list);
		}
		box.getChildren().add(combo2);
		
		box.getChildren().add(new Separator());
		box.getChildren().add(new Label("ChoiceBox mit Personen mit Converter"));
		ChoiceBox<Person> combo3 = new ChoiceBox<Person>(); {
			ObservableList<Person> list = FXCollections.observableArrayList(new Person("Josef Duschinger"),
					new Person("Gabi Staudt"), 
					new Person("Georg Engl"), 
					new Person("Jaqueline Wunderlich"),
					new Person("Christoph Keimel"));
			combo3.setItems(list);
			combo3.setConverter(new StringConverter<ComboAndChoiceBoxTest.Person>() {
				@Override
				public String toString(Person object) {
					return object.name;
				}
				@Override
				public Person fromString(String string) {
					return null;
				}
			});
		}
		box.getChildren().add(combo3);
		
		TitledPane titledPane = new TitledPane("ChoiceBox", box);
		return titledPane;
	}

	private static class Person {
		public String name;
		public Person(String string) {
			name = string;
		}
		@Override
		public String toString() {
			return name;
		}
	}
}
