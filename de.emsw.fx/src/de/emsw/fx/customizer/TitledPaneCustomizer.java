package de.emsw.fx.customizer;

import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

public class TitledPaneCustomizer {

	public static void customize(TitledPane titledPane) {
		/*
		 * TODO Mit Java 8u60 zu entfernen, wenn RT-40166 integriert wurde
		 * Siehe: https://javafx-jira.kenai.com/browse/RT-40166
		 */
		titledPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			keyPressed(event, titledPane);
		});
		
		/*
		 * TODO Mit Java 9 zu entfernen, wenn RT-40570 integriert wurde
		 * Siehe: https://javafx-jira.kenai.com/browse/RT-40570
		 */
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
