package org.mt4jx.components.visibleComponents.widgets.examples;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4jx.components.visibleComponents.shapes.widgets.imageinfo.MTInfoPanel;

public class CommonsExampleScene extends AbstractScene {
	private MTApplication app;
	
	public CommonsExampleScene(final MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.app = mtApplication;
		MTRectangle rect = new MTRectangle(mtApplication, mtApplication.loadImage("./data/logo.gif"));
		MTInfoPanel foo = new MTInfoPanel(mtApplication, "LabelText LabelText LabelText LabelText LabelText", rect, "Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. Text. ", 500, 200);
		this.getCanvas().addChild(foo);
	}
}
