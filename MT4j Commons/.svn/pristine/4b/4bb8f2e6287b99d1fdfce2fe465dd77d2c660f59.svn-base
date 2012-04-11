package org.mt4jx.components.visibleComponents.widgets.tabbedPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.layout.MTColumnLayout2D;
import org.mt4jx.components.visibleComponents.layout.MTRowLayout2D;

import processing.core.PApplet;

public class MTTabbedPanel extends MTColumnLayout2D {
	private MTRowLayout2D tabContainer;
	private MTRowLayout2D contentContainer;
	private Hashtable<MTComponent, MTComponent> tabAndContent = new Hashtable<MTComponent, MTComponent>();
	private MTToggleImageButtonGroup toggleGroup = new MTToggleImageButtonGroup();
	public MTTabbedPanel(PApplet pApplet){
		super(pApplet);
		this.tabContainer = new MTRowLayout2D(pApplet);
		this.tabContainer.setPickable(false);
		this.tabContainer.setFillColor(new MTColor(255,0,0));
		this.contentContainer = new MTRowLayout2D(pApplet);
		this.contentContainer.setPickable(false);
		this.addChild(tabContainer);
		this.addChild(contentContainer);
		this.contentContainer.translate(new Vector3D(0,-10));
		this.setNoFill(true);
		this.setNoStroke(true);
	}

	public void addTab(MTToggleImageButton tabButton, MTComponent tabContent){
		this.tabAndContent.put(tabButton, tabContent);
		tabButton.setNoStroke(true);
		this.toggleGroup.add(tabButton);
		createTab(tabButton, tabContent);
		if(tabAndContent.size()==1){
			this.setVisibleContent(tabButton);
		}
	}
	private void createTab(final MTImageButton tabButton, MTComponent tabContent){
		tabButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("MTTabbedPanel.actionPerformed(ActionEvent e)");
				switch (e.getID()) {
				case TapEvent.BUTTON_CLICKED:
					setVisibleContent(tabButton);
					break;
				default:
					break;
				}
			}
		});
		this.tabContainer.addChild(tabButton);
		this.contentContainer.addChild(tabContent);
	}
	private synchronized void setVisibleContent(final MTImageButton tabButton){
		MTComponent[] content = this.tabAndContent.values().toArray(new MTComponent[this.tabAndContent.size()]);
		for (int i = 0; i < content.length; i++) {
			content[i].setVisible(false);
		}
		MTImageButton[] tabs = this.tabAndContent.keySet().toArray(new MTImageButton[this.tabAndContent.size()]);
		this.setVisibleContent(this.tabAndContent.get(tabButton));
	}
	public synchronized void setVisibleContent(final MTComponent tabContent){
		System.out.println("setVisibleContent(final MTComponent tabContent)");
		MTComponent[] content = this.tabAndContent.values().toArray(new MTComponent[this.tabAndContent.size()]);
		for (int i = 0; i < content.length; i++) {
			if(tabContent.equals(content[i])){
				for (int j = 0; j < content.length; j++) {
					content[j].setVisible(false);
				}
				content[i].setVisible(true);
			}
		}
	}

	public MTRowLayout2D getTabContainer() {
		return tabContainer;
	}

	public MTRowLayout2D getContentContainer() {
		return contentContainer;
	}
	
}
