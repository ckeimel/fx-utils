package de.emsw.fx.customizer;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import de.emsw.fx.NodeAdapter;

public class TitledPaneCustomizer {

	public static void process(Node root) {
		NodeAdapter adapter = new NodeAdapter(root);
		adapter.accept(nodeAdapter -> {
			if (nodeAdapter.getFXObject() instanceof TitledPane)
				customize((TitledPane)nodeAdapter.getFXObject());
			return true;
		});
	}

	public static void customize(TitledPane titledPane) {
		titledPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			keyPressed(event, titledPane);
		});
		titledPane.focusTraversableProperty().bind(titledPane.collapsibleProperty());
	}

	private static void keyPressed(KeyEvent event, TitledPane titledPane) {
		if (event.getCode() != KeyCode.ENTER)
			return;
		if (event.isConsumed())
			return;
		if (event.getTarget() instanceof TitledPane) // Toggle can be activated
			return;
		fireDefaultButton(titledPane.getScene());
		event.consume();
	}

	private static void fireDefaultButton(Scene scene) {
		KeyCodeCombination keyCodeCombination = new KeyCodeCombination(KeyCode.ENTER);
		Runnable runnable = scene.getAccelerators().get(keyCodeCombination);
		if (runnable != null) {
			runnable.run();
		}
	}

}
