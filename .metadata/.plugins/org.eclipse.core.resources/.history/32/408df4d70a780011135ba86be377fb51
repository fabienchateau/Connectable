/***********************************************************************
 * mt4j Copyright (c) 2008 - 2009 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package org.mt4j.components.visibleComponents.widgets;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.interfaces.IMTController;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

/**
 * The Class MTList. A list component to add MTListCell objects to.
 * The layout will be done automatically. The list should be (created) vertically but
 * by rotating it, it can also be used horizontally.
 * 
 * @author Christopher Ruff
 */
public class MTCircularList extends MTClipRectangle {
	
	private float width;
	private float height;
	private Vector3D center;
	private float preferredCellWidth;
	private float preferredCellHeight;
	
	private MTListCellContainer listCellContainer;
	
	private float cellYPadding;
	
	//TODO dont paint listcells that are clipped entirely
	//TODO horizontal/vertical list
	//TODO padding, border between list cells etc
	//TODO deal with setWIdth/Height preferredWIdth etc changes
	//FIXME this is stupid, we got tapID and getID...
	//TODO getSelected, select()
	
	//TODO scrollbar in list and/or other indicator that we can scroll in a direction
	
	public MTCircularList(float x, float y, float width, float height,  PApplet applet) {
		this(x, y, width, height, 2, applet);
	}
	
	public MTCircularList(float x, float y, float width, float height, float cellPaddingY, PApplet applet) {
		super(x, y, 0, width, height, applet);
		
		this.width = width;
		this.height = height;
		this.center = this.getCenterPointLocal();
		
		this.preferredCellWidth = this.width;
		this.preferredCellHeight = this.height;
		
		this.cellYPadding = cellPaddingY;
		
		this.listCellContainer = new MTListCellContainer(x,y,1,1, applet);
		this.addChild(listCellContainer);
		
		//So we can drag the cell container when dragging on the list 
		this.registerInputProcessor(new DragProcessor(applet));
		this.removeAllGestureEventListeners(DragProcessor.class);
		this.addGestureListener(DragProcessor.class, new ListCellDragListener(listCellContainer));
		
	}
	
	@Override
	protected void setDefaultGestureActions() {
		//No gestures
	}
	
	
	@Override
	public void addChild(MTComponent tangibleComp) {
		this.addChild(this.getChildCount(), tangibleComp);
	}

		
	@Override
	public void addChild(int i, MTComponent tangibleComp) {
		if (tangibleComp instanceof MTListCell) {
			MTListCell cell = (MTListCell) tangibleComp;
			this.addListElement(cell);
		}else{
			super.addChild(i, tangibleComp);
		}
	}
	
	
	/**
	 * Adds a list element.
	 * 
	 * @param item the item
	 */
	public void addListElement(MTListCell item){
		this.listCellContainer.addCell(listCellContainer.cells.size(), item);
	}
	
	/**
	 * Adds a list element.
	 * 
	 * @param index the index
	 * @param item the item
	 */
	public void addListElement(int index, MTListCell item){
		this.listCellContainer.addCell(index, item);
	}
	
	/**
	 * Removes a list element.
	 * 
	 * @param item the item
	 */
	public void removeListElement(MTListCell item){
		this.listCellContainer.removeCell(item);
	}
	
	/**
	 * The Class MTListCellContainer. Container for all the MTListCell's.
	 * 
	 * @author Christopher Ruff
	 */
	private class MTListCellContainer extends MTRectangle{
		private PApplet app;
		private List<MTListCell> cells;
		private List<MTListCell> selectedCells; //TODO!
		
		private boolean isDragging;
		

		public MTListCellContainer(float x, float y, float width, float height,	PApplet applet) {
			super(x, y, width, height, applet);
			this.app = applet;
			
			this.setNoFill(true); //ENABLE LATER!
			this.setNoStroke(true);
			this.setPickable(false);
			
			this.cells = new ArrayList<MTListCell>();
			this.selectedCells = new ArrayList<MTListCell>();
			
			isDragging = false;
		}
		
		
		public void addCell(int index, MTListCell item){
			if (cells.contains(item)){
				return;
			}
			
			this.addChild(index, item);
			this.cells.add(index, item);
			this.updateLayout();
			
			//Add drag listener which drags the cells parent (listcontainer) restriced to one axis 
			if (!hasDragProcessor(item)){
				item.registerInputProcessor(new DragProcessor(app));
			}
			
			//Remove the default drag listener from the cell for safety
			IGestureEventListener[] l = item.getGestureListeners();
	    	for (int j = 0; j < l.length; j++) {
				IGestureEventListener gestureEventListener = l[j];
				if (gestureEventListener.getClass().equals(DefaultDragAction.class)){
					item.removeGestureEventListener(DragProcessor.class, gestureEventListener);
				}
			}
	    	
			item.addGestureListener(DragProcessor.class, new ListCellDragListener(this));
			
		}
		
		private boolean hasDragProcessor(MTComponent comp){
			AbstractComponentProcessor[] ps = comp.getInputProcessors();
			for (int i = 0; i < ps.length; i++) {
				AbstractComponentProcessor p = ps[i];
				if (p instanceof DragProcessor){
					return true;
				}
			}
			return false;
		}
		
		public void removeCell(MTListCell item){
			if (!this.containsDirectChild(item)){
				return;
			}
			this.removeChild(item);
			this.cells.remove(item);
			this.updateLayout();
		}
		
		
		public void updateLayout(){
			int nbCell = this.cells.size();
//			Vector3D centerApp = new Vector3D(height/2, height/2);
			for (int i = 0; i < nbCell; i++) {
				MTListCell cell = this.cells.get(i);
				if(i != nbCell -1)
					cell.rotateZ(center, (float)(i*(360/(nbCell-1))));
					
				cell.setPositionGlobal(center);
				cell.translate(new Vector3D((float) (-height/1.5), 0, 0));
				cell.rotateZ(center, (float)(-i*(360/nbCell)));
			}
		}
		
		
		
		public synchronized boolean isDragging() {
			return isDragging;
		}

	}

	/**
	 * The Class ListCellDragListener.
	 * Drag gestue listener which restricts dragging/scrolling of the cell container to its y-axis.
	 * Also stops if the list end is reached.
	 * 
	 * @author Thibault Roucou based on the work of Christopher Ruff
	 */
	private class ListCellDragListener implements IGestureEventListener{
		private MTListCellContainer theListCellContainer;
		private double angle = 1;
		
		private boolean canDrag;
		
		public ListCellDragListener(MTListCellContainer cont){
			this.theListCellContainer = cont;
			//this.canDrag = false;
		}
		
		public boolean processGestureEvent(MTGestureEvent ge) {
			DragEvent de = (DragEvent)ge;
			
			Vector3D dir = de.getTranslationVect();
			//Transform the global direction vector into listCellContainer local coordiante space
			dir.transformDirectionVector(theListCellContainer.getGlobalInverseMatrix());
			Vector3D to = new Vector3D(de.getTo().x -center.x, de.getTo().y - center.y, 0);
			Vector3D from = new Vector3D(de.getFrom().x - center.x, de.getFrom().y - center.y, 0);
			
			if(from.length() > 200)
			{
				switch (de.getId()) {
				case MTGestureEvent.GESTURE_DETECTED:
				case MTGestureEvent.GESTURE_UPDATED:
						angle = Math.atan2(to.y,to.x) - Math.atan2(from.y,from.x);
						theListCellContainer.rotateZ(center, (float) (ToolsMath.RAD_TO_DEG * angle));
					break;
				case MTGestureEvent.GESTURE_ENDED:
					Vector3D vel = de.getDragCursor().getVelocityVector(140);
					vel.scaleLocal(0.8f);
					vel = vel.getLimited(15);
					IMTController oldController = theListCellContainer.getController();
					theListCellContainer.setController(new InertiaListController(theListCellContainer, vel, oldController, (float) (Math.abs(angle)/angle)));
					break;
				default:
					break;
				}
			}
			return false;
		}
		
		
		
		
		
		/**
		 * The Class InertiaListController.
		 * Controller to add an inertia scrolling after scrolling/dragging the list content.
		 * 
		 * @author Christopher Ruff
		 */
		private class InertiaListController implements IMTController{
			private MTComponent target;
			private Vector3D startVelocityVec;
			private float dampingValue = 0.95f;
			private float rotationDir;
//			private float dampingValue = 0.80f;
			
			private IMTController oldController;
			
			public InertiaListController(MTComponent target, Vector3D startVelocityVec, IMTController oldController, float dir) {
				super();
				this.target = target;
				this.startVelocityVec = startVelocityVec;
				this.oldController = oldController;
				this.rotationDir = dir;
				
//				System.out.println(startVelocityVec);
				//Animation inertiaAnim = new Animation("Inertia anim for " + target, new MultiPurposeInterpolator(startVelocityVec.length(), 0, 100, 0.0f, 0.5f, 1), target);
			}
			
			public void update(long timeDelta) {
				if (theListCellContainer.isDragging()){
					startVelocityVec.setValues(Vector3D.ZERO_VECTOR);
					target.setController(oldController);
					return;
				}
				
				if (Math.abs(startVelocityVec.x) < 0.05f && Math.abs(startVelocityVec.y) < 0.05f){
					startVelocityVec.setValues(Vector3D.ZERO_VECTOR);
					target.setController(oldController);
					return;
				}
				startVelocityVec.scaleLocal(dampingValue);
				
				Vector3D transVect = new Vector3D(startVelocityVec);
				transVect.transformDirectionVector(listCellContainer.getGlobalInverseMatrix());
				//System.out.println(rotationDir);
				//theListCellContainer.translate(new Vector3D(0, transVect.y), TransformSpace.LOCAL);
				theListCellContainer.rotateZGlobal(center, (rotationDir * transVect.length())/2);
				if (oldController != null){
					oldController.update(timeDelta);
				}
			}
		}
		
	}
	
	
	public void scrollY(float amount){
		listCellContainer.rotateZ(center, amount);
	}
	
	
	
	
}
