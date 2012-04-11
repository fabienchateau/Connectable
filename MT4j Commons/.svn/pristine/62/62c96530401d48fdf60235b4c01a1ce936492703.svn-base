package org.mt4jx.components.visibleComponents.layout;

import java.util.ArrayList;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

/**
 * Implementation of a simple layout manager. When adding other components as
 * children gestures will be removed and their positioning handled.

 * @author Alexander Phleps
 */
public class AbstractLayout2D extends MTRectangle {
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	private int layout = VERTICAL;
	private ArrayList<AbstractShape> components;
	private float cellOffset = 2f;

	public AbstractLayout2D(PApplet app, int layout) {
		super(app, 100, 100);
		this.layout = layout;
		this.setNoFill(true);
		this.setNoStroke(true);
		components = new ArrayList<AbstractShape>();
	}
	public AbstractLayout2D(PApplet app) {
		this(app, VERTICAL);
	}

	public void addChild(AbstractShape comp) {
		super.addChild(comp);
		components.add(comp);
		layout();
	}

	@Override
	public void removeAllChildren() {
		super.removeAllChildren();
		MTComponent[] children = this.getChildren();
		for (int i = 0; i < children.length; i++) {
			this.components.remove(children[i]);
		}
		layout();
	}

	@Override
	public void removeChild(int i) {
		MTComponent child = this.getChildByIndex(i);
		super.removeChild(i);
		this.components.remove(child);
		layout();
	}

	@Override
	public void removeChild(MTComponent comp) {
		super.removeChild(comp);
		this.components.remove(comp);
		layout();
	}
	public float getCellOffset() {
		return cellOffset;
	}
	public void setCellOffset(float cellOffset) {
		this.cellOffset = cellOffset;
		this.layout();
	}
	protected void layout() {
		AbstractShape current = null;
		float currentWidth = 0f;
		float currentHeight = 0f;
		float sum = 0f;
		float maxThickness = 0f;

		for(int i=0; i<this.components.size(); i++){
			current = components.get(i);
			currentWidth = current.getWidthXY(TransformSpace.LOCAL);
			currentHeight = current.getHeightXY(TransformSpace.LOCAL);

			if (layout == HORIZONTAL) {
				if(i>0){
					sum +=cellOffset;
				}
				current.setPositionRelativeToParent(new Vector3D(sum
						+ currentWidth / 2, currentHeight / 2));
				sum += currentWidth;
				if (maxThickness < currentHeight)
					maxThickness = currentHeight;
			} else if (layout == VERTICAL) {
				if(i>0){
					sum +=cellOffset;
				}
				current.setPositionRelativeToParent(new Vector3D(
						currentWidth / 2, sum + currentHeight / 2));
				sum += currentHeight;
				if (maxThickness < currentWidth)
					maxThickness = currentWidth;
			}
		}
		if (layout == HORIZONTAL) {
			this.setWidthLocal(sum);
			this.setHeightLocal(maxThickness);
		} else if (layout == VERTICAL) {
			this.setWidthLocal(maxThickness);
			this.setHeightLocal(sum);
		}
	}
}
