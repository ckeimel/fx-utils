package test.de.emsw.fx;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import de.emsw.fx.NodeAdapter;

public class TestNodeAdapter {
	
	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

	@Test
	public void test_getChildrenUnmodifiable() {
		Pair<? extends Node> root = createGraph();
		NodeAdapter adapter = new NodeAdapter(root.node);
		compare(root.element, adapter);
	}

	private void compare(TestModelElement<?> modelElement, NodeAdapter adapter) {
		// compare ui element
		Assert.assertEquals("Inkompatible child found in adapter: " + adapter, modelElement.fxObject, adapter.getFXObject());
		// compare number of children
		Assert.assertEquals("Number of children does not match: " + adapter, modelElement.children.size(), adapter.getChildrenUnmodifiable().size());
		// compare children rekursiv
		for (TestModelElement<?> modelChild : modelElement.children) {
			NodeAdapter childAdapter = adapter.getAdapterForChild(modelChild.fxObject);
			Assert.assertNotNull("Child not found in adapter: " + adapter, childAdapter);
			compare(modelChild, childAdapter);
		}
	}

	private Pair<? extends Node> createGraph() {
		BorderPane root = new BorderPane();
		TestModelElement<BorderPane> rootElement = new TestModelElement<>(root);
		
		Pair<? extends Node> menuBar = createMenuBar();
		rootElement.children.add(menuBar.element);
		root.setTop(menuBar.node);
		
		Pair<? extends Node> contents = createContents();
		rootElement.children.add(contents.element);
		root.setCenter(contents.node);
		
		Pair<ToolBar> toolBar = createToolbar();
		rootElement.children.add(toolBar.element);
		root.setLeft(toolBar.node);
		
		return new Pair<BorderPane>(root, rootElement);
	}
	
	private Pair<ToolBar> createToolbar() {
		ToolBar bar = new ToolBar();
		TestModelElement<ToolBar> barElement = new TestModelElement<>(bar);
		
		Button button1 = new Button("Tool 1");
		bar.getItems().add(button1);
		barElement.children.add(new TestModelElement<>(button1));
		
		Separator separator = new Separator();
		bar.getItems().add(separator);
		barElement.children.add(new TestModelElement<>(separator));
		
		Button button2 = new Button("Tool 2");
		bar.getItems().add(button2);
		barElement.children.add(new TestModelElement<>(button2));
		
		return new Pair<ToolBar>(bar, barElement);
	}

	private Pair<? extends Node> createMenuBar() {
		MenuBar menuBar = new MenuBar();
		TestModelElement<MenuBar> menuBarElement = new TestModelElement<>(menuBar);
		
		Menu menu01 = new Menu("Menu 01");
		TestModelElement<Menu> menuElement01 = new TestModelElement<>(menu01);
		menuBar.getMenus().add(menu01);
		menuBarElement.children.add(menuElement01);
		populateMenu(menu01, menuElement01);
		
		Menu menu02 = new Menu("Menu 01");
		TestModelElement<Menu> menuElement02 = new TestModelElement<>(menu02);
		menuBar.getMenus().add(menu02);
		menuBarElement.children.add(menuElement02);
		populateMenu(menu02, menuElement02);
		
		return new Pair<MenuBar>(menuBar, menuBarElement);
	}

	private void populateMenu(Menu menu, TestModelElement<Menu> menuElement) {
		Menu subMenu = new Menu("SubMenu");
		TestModelElement<Menu> subMenuElement = new TestModelElement<>(subMenu);
		menu.getItems().add(subMenu);
		menuElement.children.add(subMenuElement);
		populateSubMenu(subMenu, subMenuElement);
		
		MenuItem item01 = new MenuItem("MenuItem 01"); 
		menu.getItems().add(item01);
		menuElement.children.add(new TestModelElement<>(item01));
		
		MenuItem item02 = new MenuItem("MenuItem 02"); 
		menu.getItems().add(item02);
		menuElement.children.add(new TestModelElement<>(item02));
		
		SeparatorMenuItem separator = new SeparatorMenuItem();
		menu.getItems().add(separator);
		menuElement.children.add(new TestModelElement<>(separator));
		
		MenuItem item03 = new MenuItem("MenuItem 03"); 
		menu.getItems().add(item03);
		menuElement.children.add(new TestModelElement<>(item03));
	}

	private void populateSubMenu(Menu menu, TestModelElement<Menu> menuElement) {
		MenuItem item01 = new MenuItem("SubMenuItem 01"); 
		TestModelElement<MenuItem> item01Element = new TestModelElement<>(item01);
		menu.getItems().add(item01);
		menuElement.children.add(item01Element);
		
		MenuItem item02 = new MenuItem("SubMenuItem 02"); 
		TestModelElement<MenuItem> item02Element = new TestModelElement<>(item02);
		menu.getItems().add(item02);
		menuElement.children.add(item02Element);
	}

	private Pair<? extends Node> createContents() {
		TabPane tabPane = new TabPane();
		TestModelElement<TabPane> tabPaneElement = new TestModelElement<>(tabPane);
		
		Pair<Tab> titledTab = createTitledPaneTab();
		tabPane.getTabs().add(titledTab.node);
		tabPaneElement.children.add(titledTab.element);
		
		Pair<Tab> anchorTab = createAnchorPaneTab();
		tabPane.getTabs().add(anchorTab.node);
		tabPaneElement.children.add(anchorTab.element);
		
		Pair<Tab> stackTab = createStackPaneTab();
		tabPane.getTabs().add(stackTab.node);
		tabPaneElement.children.add(stackTab.element);
		
		Pair<Tab> hboxTab = createHBoxTab();
		tabPane.getTabs().add(hboxTab.node);
		tabPaneElement.children.add(hboxTab.element);
		
		Pair<Tab> vboxTab = createVBoxTab();
		tabPane.getTabs().add(vboxTab.node);
		tabPaneElement.children.add(vboxTab.element);
		
		Pair<Tab> gridTab = createGridPaneTab();
		tabPane.getTabs().add(gridTab.node);
		tabPaneElement.children.add(gridTab.element);
		
		Pair<Tab> flowTab = createFlowPaneTab();
		tabPane.getTabs().add(flowTab.node);
		tabPaneElement.children.add(flowTab.element);
		
		Pair<Tab> accordionTab = createAccordionTab();
		tabPane.getTabs().add(accordionTab.node);
		tabPaneElement.children.add(accordionTab.element);
		
		Pair<Tab> paneTab = createPaneTab();
		tabPane.getTabs().add(paneTab.node);
		tabPaneElement.children.add(paneTab.element);
		
		Pair<Tab> scrollPaneTab = createScrollPaneTab();
		tabPane.getTabs().add(scrollPaneTab.node);
		tabPaneElement.children.add(scrollPaneTab.element);
		
		Pair<Tab> splitPaneTab = createSplitPaneTab();
		tabPane.getTabs().add(splitPaneTab.node);
		tabPaneElement.children.add(splitPaneTab.element);
		
		Pair<Tab> contextMenuTab = createContextMenuTab();
		tabPane.getTabs().add(contextMenuTab.node);
		tabPaneElement.children.add(contextMenuTab.element);
		
		Pair<Tab> toolBarTab = createToolBarTab();
		tabPane.getTabs().add(toolBarTab.node);
		tabPaneElement.children.add(toolBarTab.element);
		
		Pair<Tab> buttonBarTab = createButtonBarTab();
		tabPane.getTabs().add(buttonBarTab.node);
		tabPaneElement.children.add(buttonBarTab.element);
		
		return new Pair<TabPane>(tabPane, tabPaneElement);
	}
	
	private Pair<Tab> createButtonBarTab() {
		Tab tab = new Tab("ButtonBar Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<ButtonBar> buttonbar = createButtonBar();
		tab.setContent(buttonbar.node);
		tabElement.children.add(buttonbar.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<ButtonBar> createButtonBar() {
		ButtonBar pane = new ButtonBar();
		TestModelElement<ButtonBar> paneElement = new TestModelElement<>(pane);
		
		Button button1 = new Button("Button 01 in ButtonBar");
		pane.getButtons().add(button1);
		TestModelElement<Button> button1Element = new TestModelElement<>(button1);
		paneElement.children.add(button1Element);
		
		Button button2 = new Button("Button 02 in ButtonBar");
		pane.getButtons().add(button2);
		TestModelElement<Button> button2Element = new TestModelElement<>(button2);
		paneElement.children.add(button2Element);
		
		return new Pair<ButtonBar>(pane, paneElement);
	}

	private Pair<Tab> createToolBarTab() {
		Tab tab = new Tab("ToolBar Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<ToolBar> toolbar = createToolBar();
		tab.setContent(toolbar.node);
		tabElement.children.add(toolbar.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<ToolBar> createToolBar() {
		ToolBar pane = new ToolBar();
		TestModelElement<ToolBar> paneElement = new TestModelElement<>(pane);
		
		Button button1 = new Button("Button 01 in ToolBar");
		pane.getItems().add(button1);
		TestModelElement<Button> button1Element = new TestModelElement<>(button1);
		paneElement.children.add(button1Element);
		createContextMenu(button1, button1Element);
		
		Separator sep = new Separator();
		pane.getItems().add(sep);
		TestModelElement<Separator> sepElement = new TestModelElement<>(sep);
		paneElement.children.add(sepElement);
		createContextMenu(sep, sepElement);
		
		Button button2 = new Button("Button 02 in ToolBar");
		pane.getItems().add(button2);
		TestModelElement<Button> button2Element = new TestModelElement<>(button2);
		paneElement.children.add(button2Element);
		createContextMenu(button2, button2Element);
		
		return new Pair<ToolBar>(pane, paneElement);
	}

	private Pair<Tab> createContextMenuTab() {
		Tab tab = new Tab("SplitPane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<VBox> vbox = createContextMenuVBox();
		tab.setContent(vbox.node);
		tabElement.children.add(vbox.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<VBox> createContextMenuVBox() {
		VBox pane = new VBox();
		TestModelElement<VBox> paneElement = new TestModelElement<>(pane);
		
		Button button1 = new Button("Button 01 in VBox");
		pane.getChildren().add(button1);
		TestModelElement<Button> button1Element = new TestModelElement<>(button1);
		paneElement.children.add(button1Element);
		createContextMenu(button1, button1Element);
		
		Button button2 = new Button("Button 02 in VBox");
		pane.getChildren().add(button2);
		TestModelElement<Button> button2Element = new TestModelElement<>(button2);
		paneElement.children.add(button2Element);
		createContextMenu(button2, button2Element);
		
		return new Pair<VBox>(pane, paneElement);
	}

	private void createContextMenu(Control control, TestModelElement<? extends Control> controlElement) {
		ContextMenu contextMenu = new ContextMenu(); {
			MenuItem item01 = new MenuItem("MenuItem 01"); 
			contextMenu.getItems().add(item01);
			controlElement.children.add(new TestModelElement<>(item01));
			
			MenuItem item02 = new MenuItem("MenuItem 02"); 
			contextMenu.getItems().add(item02);
			controlElement.children.add(new TestModelElement<>(item02));
			
			SeparatorMenuItem separator = new SeparatorMenuItem();
			contextMenu.getItems().add(separator);
			controlElement.children.add(new TestModelElement<>(separator));
			
			MenuItem item03 = new MenuItem("MenuItem 03"); 
			contextMenu.getItems().add(item03);
			controlElement.children.add(new TestModelElement<>(item03));
		}
		control.setContextMenu(contextMenu);
	}

	private Pair<Tab> createSplitPaneTab() {
		Tab tab = new Tab("SplitPane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<SplitPane> pane = createSplitPane();
		tab.setContent(pane.node);
		tabElement.children.add(pane.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<SplitPane> createSplitPane() {
		SplitPane pane = new SplitPane();
		TestModelElement<SplitPane> paneElement = new TestModelElement<>(pane);
		
		Pair<HBox> hBox = createHBox();
		pane.getItems().add(hBox.node);
		paneElement.children.add(hBox.element);
		
		Pair<VBox> vBox = createVBox();
		pane.getItems().add(vBox.node);
		paneElement.children.add(vBox.element);
		
		return new Pair<SplitPane>(pane, paneElement);
	}

	private Pair<Tab> createScrollPaneTab() {
		Tab tab = new Tab("ScrollPane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<ScrollPane> pane = createScrollPane();
		tab.setContent(pane.node);
		tabElement.children.add(pane.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<ScrollPane> createScrollPane() {
		ScrollPane pane = new ScrollPane();
		TestModelElement<ScrollPane> paneElement = new TestModelElement<>(pane);
		
		TextArea textArea = new TextArea("ScrollPane TextArea");
		pane.setContent(textArea);
		paneElement.children.add(new TestModelElement<>(textArea));
		
		return new Pair<ScrollPane>(pane, paneElement);
	}

	private Pair<Tab> createPaneTab() {
		Tab tab = new Tab("Pane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<Pane> flow = createPane();
		tab.setContent(flow.node);
		tabElement.children.add(flow.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<Pane> createPane() {
		Pane pane = new Pane();
		TestModelElement<Pane> paneElement = new TestModelElement<>(pane);
		
		Label label1 = new Label("Label 01 in Pane");
		pane.getChildren().add(label1);
		paneElement.children.add(new TestModelElement<>(label1));
		
		Label label2 = new Label("Label 02 in Pane");
		pane.getChildren().add(label2);
		paneElement.children.add(new TestModelElement<>(label2));
		
		return new Pair<Pane>(pane, paneElement);
	}

	private Pair<Tab> createAccordionTab() {
		Tab tab = new Tab("Accordion Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<Accordion> flow = createAccordion();
		tab.setContent(flow.node);
		tabElement.children.add(flow.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<Accordion> createAccordion() {
		Accordion pane = new Accordion();
		TestModelElement<Accordion> paneElement = new TestModelElement<>(pane);
		
		Pair<TitledPane> titledPane1 = createTitledPane();
		pane.getPanes().add(titledPane1.node);
		paneElement.children.add(titledPane1.element);
		
		Pair<TitledPane> titledPane2 = createTitledPane();
		pane.getPanes().add(titledPane2.node);
		paneElement.children.add(titledPane2.element);
		
		Pair<TitledPane> titledPane3 = createTitledPane();
		pane.getPanes().add(titledPane3.node);
		paneElement.children.add(titledPane3.element);
		
		return new Pair<Accordion>(pane, paneElement);
	}

	private Pair<Tab> createFlowPaneTab() {
		Tab tab = new Tab("FlowPane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<FlowPane> flow = createFlowPane();
		tab.setContent(flow.node);
		tabElement.children.add(flow.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<FlowPane> createFlowPane() {
		FlowPane pane = new FlowPane();
		TestModelElement<FlowPane> paneElement = new TestModelElement<>(pane);
		
		ColorPicker colorPicker = new ColorPicker();
		pane.getChildren().add(colorPicker);
		paneElement.children.add(new TestModelElement<>(colorPicker));
		
		DatePicker datePicker = new DatePicker();
		pane.getChildren().add(datePicker);
		paneElement.children.add(new TestModelElement<>(datePicker));
		
		return new Pair<FlowPane>(pane, paneElement);
	}

	private Pair<Tab> createStackPaneTab() {
		Tab tab = new Tab("StackPane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<StackPane> stack = createStackPane();
		tab.setContent(stack.node);
		tabElement.children.add(stack.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<StackPane> createStackPane() {
		StackPane pane = new StackPane();
		TestModelElement<StackPane> paneElement = new TestModelElement<>(pane);
		
		Button button1 = new Button("Button 01 in StackPane");
		pane.getChildren().add(button1);
		paneElement.children.add(new TestModelElement<>(button1));
		
		Button button2 = new Button("Button 02 in StackPane");
		pane.getChildren().add(button2);
		paneElement.children.add(new TestModelElement<>(button2));
		
		return new Pair<StackPane>(pane, paneElement);
	}

	private Pair<Tab> createGridPaneTab() {
		Tab tab = new Tab("GridPane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<GridPane> grid = createGridPane();
		tab.setContent(grid.node);
		tabElement.children.add(grid.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<GridPane> createGridPane() {
		GridPane pane = new GridPane();
		TestModelElement<GridPane> paneElement = new TestModelElement<>(pane);
		
		CheckBox checkBox = new CheckBox("CheckBox in GridPane");
		pane.add(checkBox, 0, 0);
		paneElement.children.add(new TestModelElement<>(checkBox));
		
		ComboBox<String> comboBox = new ComboBox<String>();
		pane.add(comboBox, 1, 0);
		paneElement.children.add(new TestModelElement<>(comboBox));
		
		RadioButton radioButton = new RadioButton("RadioButton in GridPane");
		pane.add(radioButton, 1, 0);
		paneElement.children.add(new TestModelElement<>(radioButton));
		
		ChoiceBox<String> choiceBox = new ChoiceBox<String>();
		pane.add(choiceBox, 1, 1);
		paneElement.children.add(new TestModelElement<>(choiceBox));
		
		return new Pair<GridPane>(pane, paneElement);
	}

	private Pair<Tab> createVBoxTab() {
		Tab tab = new Tab("VBox Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<VBox> hbox = createVBox();
		tab.setContent(hbox.node);
		tabElement.children.add(hbox.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<VBox> createVBox() {
		VBox pane = new VBox();
		TestModelElement<VBox> paneElement = new TestModelElement<>(pane);
		
		Button button1 = new Button("Button 01 in VBox");
		pane.getChildren().add(button1);
		paneElement.children.add(new TestModelElement<>(button1));
		
		Button button2 = new Button("Button 02 in VBox");
		pane.getChildren().add(button2);
		paneElement.children.add(new TestModelElement<>(button2));
		
		return new Pair<VBox>(pane, paneElement);
	}

	private Pair<Tab> createHBoxTab() {
		Tab tab = new Tab("HBox Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<HBox> hbox = createHBox();
		tab.setContent(hbox.node);
		tabElement.children.add(hbox.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<HBox> createHBox() {
		HBox pane = new HBox();
		TestModelElement<HBox> paneElement = new TestModelElement<>(pane);
		
		Button button1 = new Button("Button 01 in HBox");
		pane.getChildren().add(button1);
		paneElement.children.add(new TestModelElement<>(button1));
		
		Button button2 = new Button("Button 02 in HBox");
		pane.getChildren().add(button2);
		paneElement.children.add(new TestModelElement<>(button2));
		
		return new Pair<HBox>(pane, paneElement);
	}

	private Pair<Tab> createTitledPaneTab() {
		Tab tab = new Tab("TitledPane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<TitledPane> titeledPane = createTitledPane();
		tab.setContent(titeledPane.node);
		tabElement.children.add(titeledPane.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<TitledPane> createTitledPane() {
		TitledPane titledPane = new TitledPane();
		TestModelElement<TitledPane> titledPaneElement = new TestModelElement<>(titledPane);
		
		TextArea textArea = new TextArea("TitledPane TextArea");
		titledPane.setContent(textArea);
		titledPaneElement.children.add(new TestModelElement<>(textArea));
		
		return new Pair<TitledPane>(titledPane, titledPaneElement);
	}

	private Pair<Tab> createAnchorPaneTab() {
		Tab tab = new Tab("AnchorPane Test");
		TestModelElement<Tab> tabElement = new TestModelElement<>(tab);
		
		Pair<AnchorPane> anchorPane = createAnchorPane();
		tab.setContent(anchorPane.node);
		tabElement.children.add(anchorPane.element);
		
		return new Pair<Tab>(tab, tabElement);
	}

	private Pair<AnchorPane> createAnchorPane() {
		AnchorPane pane = new AnchorPane();
		TestModelElement<AnchorPane> paneElement = new TestModelElement<>(pane);
		
		TextField textField1 = new TextField("TextField 01 in AnchorPane");
		pane.getChildren().add(textField1);
		paneElement.children.add(new TestModelElement<>(textField1));
		
		TextField textField2 = new TextField("TextField 02 in AnchorPane");
		pane.getChildren().add(textField2);
		paneElement.children.add(new TestModelElement<>(textField2));
		
		return new Pair<AnchorPane>(pane, paneElement);
	}

}

class Pair<T> {
	T node;
	TestModelElement<T> element;
	
	public Pair(T node, TestModelElement<T> element) {
		this.node = node;
		this.element = element;
	}
}

class TestModelElement<T> {
	T fxObject;
	List<TestModelElement<?>> children = new ArrayList<>();
	
	public TestModelElement(T fxObject) {
		this.fxObject = fxObject;
	}
}
