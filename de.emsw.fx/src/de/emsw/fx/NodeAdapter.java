package de.emsw.fx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;

public class NodeAdapter {
	
	static enum RootType {
		NODE, MENU_ITEM, TAB
	}
	
	public static NodeAdapter adapt(Object fx) {
		if (fx instanceof Node) {
			return new NodeAdapter((Node)fx);
		} else if (fx instanceof MenuItem) {
			return new NodeAdapter((MenuItem)fx);
		} else if (fx instanceof Tab) {
			return new NodeAdapter((Tab)fx);
		}
		return null;
	}
	
	private Object fxObject;
	private RootType type;

	public NodeAdapter(Node node) {
		this.fxObject = node;
		this.type = RootType.NODE;
	}
	
	public NodeAdapter(MenuItem menuItem) {
		this.fxObject = menuItem;
		this.type = RootType.MENU_ITEM;
	}
	
	public NodeAdapter(Tab tab) {
		this.fxObject = tab;
		this.type = RootType.TAB;
	}
	
	public Object getFXObject() {
		return fxObject;
	}
	
	public RootType getRootType() {
		return type;
	}
	
	public Node getNode() {
		if (type != RootType.NODE)
			return null;
		return (Node)fxObject;
	}
	
	public MenuItem getMenuItem() {
		if (type != RootType.MENU_ITEM)
			return null;
		return (MenuItem)fxObject;
	}
	
	public Tab getTab() {
		if (type != RootType.TAB)
			return null;
		return (Tab)fxObject;
	}
	
	public StringProperty idProperty() {
		switch (type) {
		case NODE:
			return getNode().idProperty();
		case MENU_ITEM:
			return getMenuItem().idProperty();
		case TAB:
			return getTab().idProperty();
		}
		return null;
	}
	
	public ObservableList<String> getStyleClass() {
		switch (type) {
		case NODE:
			return getNode().getStyleClass();
		case MENU_ITEM:
			return getMenuItem().getStyleClass();
		case TAB:
			return getTab().getStyleClass();
		}
		return null;
	}

	public Object getUserData() {
		switch (type) {
		case NODE:
			return getNode().getUserData();
		case MENU_ITEM:
			return getMenuItem().getUserData();
		case TAB:
			return getTab().getUserData();
		}
		return null;
	}
	
	public void setUserData(Object data) {
		switch (type) {
		case NODE:
			getNode().setUserData(data);
			break;
		case MENU_ITEM:
			getMenuItem().setUserData(data);
			break;
		case TAB:
			getTab().setUserData(data);
			break;
		}
	}
	
	public List<NodeAdapter> getChildrenUnmodifiable() {
		List<NodeAdapter> result = new ArrayList<NodeAdapter>();
		
		// This returns the same thing as Parent.getChildrenUnmodifiable()
//		if (fxObject instanceof Node) {
//			((Node)fxObject).lookupAll("*").forEach(node -> {
//				if (!node.equals(fxObject)) {
//					result.add(new NodeAdapter(node));
//				}
//			});
//		}
		
		// primary parent type derived from the root type
		if (fxObject instanceof Parent) {
			((Parent)fxObject).getChildrenUnmodifiable().forEach(node -> result.add(new NodeAdapter(node)));
		} else if (fxObject instanceof Menu) {
			((Menu)fxObject).getItems().forEach(item -> result.add(new NodeAdapter(item)));
		} else if (fxObject instanceof Tab) {
			Node content = ((Tab)fxObject).getContent();
			if (content != null)
				result.add(new NodeAdapter(content));
		} 
		
		// extended parent types
		if (fxObject instanceof MenuBar) {
			((MenuBar)fxObject).getMenus().forEach(menu -> result.add(new NodeAdapter(menu)));
		} else if (fxObject instanceof TabPane) {
			((TabPane)fxObject).getTabs().forEach(tab -> result.add(new NodeAdapter(tab)));
		} else if (fxObject instanceof TitledPane) {
			Node content = ((TitledPane)fxObject).getContent();
			if (content != null)
				result.add(new NodeAdapter(content));
		} else if (fxObject instanceof ScrollPane) {
			Node content = ((ScrollPane)fxObject).getContent();
			if (content != null)
				result.add(new NodeAdapter(content));
		} else if (fxObject instanceof Accordion) {
			((Accordion)fxObject).getPanes().forEach(pane -> result.add(new NodeAdapter(pane)));
		} else if (fxObject instanceof SplitPane) {
			((SplitPane)fxObject).getItems().forEach(node -> result.add(new NodeAdapter(node)));
		} else if (fxObject instanceof ToolBar) {
			((ToolBar)fxObject).getItems().forEach(node -> result.add(new NodeAdapter(node)));
		} else if (fxObject instanceof ButtonBar) {
			((ButtonBar)fxObject).getButtons().forEach(button -> result.add(new NodeAdapter(button)));
		}
		
		// context menu
		if (fxObject instanceof Control) {
			ContextMenu contextMenu = ((Control)fxObject).getContextMenu();
			if (contextMenu != null) {
				contextMenu.getItems().forEach(item -> result.add(new NodeAdapter(item)));
			}
		}
		
		return Collections.unmodifiableList(result);
	}
	
	public NodeAdapter getAdapterForChild(Object node) {
		List<NodeAdapter> result = getChildrenUnmodifiable()
				.stream()
				.filter(child -> child.fxObject.equals(node))
				.collect(Collectors.toList());
		if (result.size() == 0)
			return null;
		return result.get(0);
	}
	
	public void accept(INodeVisitor visitor) {
		boolean result = visitor.visit(this);
		if (!result)
			return;
		getChildrenUnmodifiable().forEach(nodeAdapter -> nodeAdapter.accept(visitor));
	}

	@Override
	public String toString() {
		return "NodeAdapter [type=" + type + ", fxObject=" + fxObject + "]";
	}

}
