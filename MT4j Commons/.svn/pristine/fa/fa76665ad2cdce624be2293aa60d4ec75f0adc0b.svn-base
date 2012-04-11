package org.mt4jx.components.visibleComponents.widgets.tabbedPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;

import processing.core.PApplet;
import processing.core.PImage;

public class MTToggleImageButton extends MTImageButton {
	private MTImageButton overlayButton;
	private boolean selected = false;
	private ArrayList<MTToggleImageButtonListener> toggleListeners = new ArrayList<MTToggleImageButtonListener>();
	private MTToggleImageButton selfRef;
	public MTToggleImageButton(PImage texture, PApplet pApplet){
		super(texture, pApplet);
		this.setName("Unnamed Toggle Button");
		this.selfRef = this;
		this.overlayButton = new MTImageButton(texture, pApplet);
		this.addChild(overlayButton);
		this.overlayButton.setFillColor(new MTColor(0,0,0,128));
		ActionListener al = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (e.getID()) {
				case TapEvent.BUTTON_CLICKED:
					for (int i = 0; i < toggleListeners.size(); i++) {
						toggleListeners.get(i).buttonClicked(selfRef);
					}
					break;

				default:
					break;
				}
			}
		};
		this.overlayButton.addActionListener(al);
		this.addActionListener(al);
		this.setNoStroke(true);
		this.overlayButton.setNoStroke(true);
		this.toggle();
	}
	public void toggle(){
		System.out.println("TOGGLE");
		if(selected){
			this.overlayButton.setVisible(true);
			this.selected = false;
		}else{
			this.overlayButton.setVisible(false);
			this.selected = true;
		}
	}
	public void setSelected(boolean selected){
		if(this.selected!=selected){
			this.toggle();
		}
	}
	public void addToggleListener(MTToggleImageButtonListener listener){
		if(!this.toggleListeners.contains(listener)){
			this.toggleListeners.add(listener);
		}
	}
	public void removeToggleListener(MTToggleImageButtonListener listener){
		this.toggleListeners.remove(listener);
	}
	@Override
	public synchronized void addActionListener(ActionListener listener) {
		super.addActionListener(listener);
		this.overlayButton.addActionListener(listener);
	}
}
