package org.mt4jx.components.visibleComponents.layout.test;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.layout.MTColumnLayout2D;
import org.mt4jx.components.visibleComponents.layout.MTRowLayout2D;

public class SimpleLayoutTestSceneScene extends AbstractScene {

	public SimpleLayoutTestSceneScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		MTColor black = new MTColor(0,0,0);
		this.setClearColor(new MTColor(146, 150, 188, 255));
		//Show touches
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		IFont fontArial = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
				24, 	//Font size
				black);	//Font color
		//Create a textfield
		
		MTTextArea t1 = new MTTextArea(mtApplication, fontArial); 
		t1.setText("TF 01");
		MTTextArea t2 = new MTTextArea(mtApplication, fontArial); 
		t2.setText("TF 02");
		MTTextArea t3 = new MTTextArea(mtApplication, fontArial); 
		t3.setText("TF 03");
		
		MTTextArea t4 = new MTTextArea(mtApplication, fontArial); 
		t4.setText("TF 04");
		MTTextArea t5 = new MTTextArea(mtApplication, fontArial); 
		t5.setText("TF 05");
		MTTextArea t6 = new MTTextArea(mtApplication, fontArial); 
		t6.setText("TF 06");

		MTColumnLayout2D colLayout = new MTColumnLayout2D(mtApplication);
		colLayout.addChild(t1);
		colLayout.addChild(t2);
		colLayout.addChild(t3);
		
		MTRowLayout2D rowLayout = new MTRowLayout2D(mtApplication);
		rowLayout.addChild(t4);
		rowLayout.addChild(t5);
		rowLayout.addChild(t6);
		rowLayout.translate(new Vector3D(100,0));
		this.getCanvas().addChild(colLayout);
		this.getCanvas().addChild(rowLayout);
	}
	
	public void onEnter() {}
	
	public void onLeave() {}
}
