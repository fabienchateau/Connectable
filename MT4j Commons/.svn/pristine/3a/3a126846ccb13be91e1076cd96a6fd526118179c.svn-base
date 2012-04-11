package org.mt4jx.components.visibleComponents.widgets.tabbedPanel;

import java.util.ArrayList;

public class MTToggleImageButtonGroup implements MTToggleImageButtonListener {
	private ArrayList<MTToggleImageButton> toggleButtons = new ArrayList<MTToggleImageButton>();
	public MTToggleImageButtonGroup(){
	}
	public void add(MTToggleImageButton tib){
		if(!this.toggleButtons.contains(tib)){
			this.toggleButtons.add(tib);
			tib.addToggleListener(this);
		}
		if(this.toggleButtons.size()==1){
			setSelectedButton(tib);
		}else{
			tib.setSelected(false);
		}
	}
	public void remove(MTToggleImageButton tib){
		if(this.toggleButtons.contains(tib)){
			this.toggleButtons.remove(tib);
			tib.removeToggleListener(this);
		}
	}
	@Override
	public void buttonClicked(MTToggleImageButton button) {
		System.out.println("MTToggleImageButtonGroup.buttonClicked(MTToggleImageButton button)");
		this.setSelectedButton(button);
	}
	private void setSelectedButton(MTToggleImageButton button){
		for (int i = 0; i < this.toggleButtons.size(); i++) {
			MTToggleImageButton current = this.toggleButtons.get(i);
			System.out.println("DESELECT: " + current);
			current.setSelected(false);
		}
		if(!button.isSelected()){
			button.setSelected(true);
		}
	}
	
}
