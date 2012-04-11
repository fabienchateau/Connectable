package org.mt4jx.components.visibleComponents.shapes.widgets.imageinfo;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.layout.MTColumnLayout2D;

import processing.core.PApplet;

public class MTInfoPanel extends MTRoundRectangle {
	private MTColumnLayout2D rows;
	
	private IFont headlineFont,textFont;
	private float strokeWeight = 2.5f;
	private MTColor textColor = new MTColor(255,255,255);
	private MTColor fillColor = new MTColor(0,0,0, 192);
	private MTColor strokeColor = new MTColor(255,255,255, 128);
	private float maxWidth, maxHeight;
	private String text,labelText;
	private PApplet pa;
	private AbstractShape image;
	
	public MTInfoPanel(PApplet pa, String labelText, AbstractShape image, String text, float maxWidth, float maxHeight){
		this(pa, labelText, image, text, maxWidth, maxHeight, null, null);
	}
	public MTInfoPanel(PApplet pa, String labelText, AbstractShape image, String text, float maxWidth, float maxHeight, IFont headlineFont, IFont textFont){
		super(pa,0,0,0,400,300,12,12);
		this.setFillColor(fillColor);
		this.setStrokeColor(strokeColor);
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.headlineFont = headlineFont;
		this.textFont = textFont;
		this.text = text;
		this.labelText = labelText;
		this.pa = pa;
		this.image = image;
		this.init();
	}
	
	private void init(){
		this.rows = new MTColumnLayout2D(pa);
		this.rows.setPickable(false);
		this.addChild(rows);
		this.rows.translate(new Vector3D(1,1));
		
		// setting default font if missing
		if(this.headlineFont==null){
			this.headlineFont =  FontManager.getInstance().createFont(pa, "arial", 
					32, 	//Font size
					textColor);	//Font outline color
		}
		if(this.textFont==null){
			this.textFont =  FontManager.getInstance().createFont(pa, "arial", 
					16, 	//Font size
					textColor);	//Font outline color
		}
		MTTextArea ta_label = new MTTextArea(pa,0,0,maxWidth, 40,headlineFont);
			ta_label.setNoFill(true);
			ta_label.setNoStroke(true);
			ta_label.setPickable(false);
			if(labelText!=null){
				ta_label.setText(labelText);
			}
			this.rows.addChild(ta_label);
			MTLine line = new MTLine(pa, 0,0,maxWidth,0);
			line.setStrokeColor(strokeColor);
			line.setStrokeWeight(strokeWeight);
			line.setPickable(false);
			this.rows.addChild(line);
			
			image.setNoStroke(true);
			image.setPickable(false);
			this.rows.addChild(image);
			
			float textHeight = maxHeight-rows.getHeightXY(TransformSpace.GLOBAL);
			if(textHeight<90){
				textHeight=90;
			}
			
			line = new MTLine(pa, 0,0,maxWidth,0);
			line.setStrokeColor(strokeColor);
			line.setStrokeWeight(strokeWeight);
			line.setPickable(false);
			this.rows.addChild(line);
			
			MTTextArea ta_text = new MTTextArea(pa,0,0,maxWidth, textHeight,textFont);
			ta_text.setNoFill(true);
			ta_text.setNoStroke(true);
			ta_text.setPickable(false);
			if(text!=null){
				ta_text.setText(text);
			}
			this.rows.addChild(ta_text);
			this.setSizeLocal(this.rows.getWidthXY(TransformSpace.LOCAL), this.rows.getHeightXY(TransformSpace.LOCAL));
			{
			float w = this.getWidthXY(TransformSpace.LOCAL);
			float h = this.getHeightXY(TransformSpace.LOCAL);
//			System.out.println("w/h" + w + "/" + h);
			if(w>maxWidth || h>maxHeight){
				float fw = maxWidth/w;
				float fh = maxHeight/h;
//				System.out.println("fw/fh" + fw + "/" + fh);
				if(fw<fh){
					scale(fw, fw, fw, this.getCenterPointLocal());
				}else{
					scale(fh, fh, fh, this.getCenterPointLocal());
				}
			}
	}
			float w = this.getWidthXY(TransformSpace.GLOBAL);
			float h = this.getHeightXY(TransformSpace.GLOBAL);
			System.out.println("w/h" + w + "/" + h);
	}
}
