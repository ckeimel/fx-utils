package de.emsw.fx.customizer;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import de.emsw.fx.NodeAdapter;

public class GraphCustomizer {
	
	public static void process(Node root) {
		NodeAdapter adapter = new NodeAdapter(root);
		adapter.accept(nodeAdapter -> {
			if (nodeAdapter.getFXObject() instanceof ComboBox) {
				ComboAndChoiceBoxCustomizer.customize((ComboBox<?>)nodeAdapter.getFXObject());
			} else if (nodeAdapter.getFXObject() instanceof ChoiceBox) {
				ComboAndChoiceBoxCustomizer.customize((ChoiceBox<?>)nodeAdapter.getFXObject());
			} else if (nodeAdapter.getFXObject() instanceof TitledPane) {
				TitledPaneCustomizer.customize((TitledPane)nodeAdapter.getFXObject());
			}
			return true;
		});
	}
	
}
