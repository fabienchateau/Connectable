package org.mt4j.input.inputProcessors.componentProcessors.xProcessor;
/* Erik Paluka - June 2011
 * Vialab at the University of Ontario Institute of Technology
 * Summer Research Assistant with Dr. Collins (Summer 2011)
 * This class checks if two flicks occur within a second of each other, if their
 * lines are no collinear (using dot product), they intersect, and the length of the starting or ending point
 * of either input cursor to the intersection point is greater in size than 30% of half of the total length
 * of the input cursor
 */
import java.awt.geom.Line2D;

import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractCursorProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class XProcessor extends AbstractCursorProcessor{
	/** The applet. */
	private PApplet applet;
	
	private boolean startX;
	
	private double lastTime;
	
	private InputCursor lastCursor;

	/**
	 * Instantiates a new X processor.
	 */
	public XProcessor(PApplet app){
		applet = app;
		this.startX = false;
	}

	
	@Override
	public void cursorStarted(InputCursor cursor, AbstractCursorInputEvt currentEvent) {
		InputCursor[] theLockedCursors = getLockedCursorsArray();
		//if gesture isn't started and no other cursor on comp is locked by higher priority gesture -> start gesture
		if (theLockedCursors.length == 0 && this.canLock(getCurrentComponentCursorsArray())){ 
				//Lock this cursor with our priority
				this.getLock(cursor);
				this.fireGestureEvent(new XEvent(this, MTGestureEvent.GESTURE_STARTED, cursor.getCurrentTarget()));
		}
	}

	
	@Override
	public void cursorUpdated(InputCursor cursor, AbstractCursorInputEvt currentEvent) {
		if (getLockedCursors().contains(cursor)){
			this.fireGestureEvent(new XEvent(this, MTGestureEvent.GESTURE_UPDATED, cursor.getCurrentTarget()));
		}
	}

	
	@Override
	public void cursorEnded(InputCursor cursor, AbstractCursorInputEvt currentEvent) {
		if (getLockedCursors().contains(cursor)){ //Cursors was a actual gesture cursors
			
			//Check if we can resume the gesture with another cursor
			InputCursor[] availableCursors = getFreeComponentCursorsArray();
			
			if (availableCursors.length > 0 && this.canLock(getCurrentComponentCursorsArray())){ 
				InputCursor otherCursor = availableCursors[0]; 
				this.getLock(otherCursor);
			} else if (startX == true && System.currentTimeMillis()-lastTime < 900 ){
				//If the first flick of the 'X' has occurred less than
				//0.9 seconds ago 
				
				//Get the direction vector of the first 'X' line
				Vector3D dir1 = new Vector3D(
						lastCursor.getPosition().x - lastCursor.getStartPosX(),
						lastCursor.getPosition().y - lastCursor.getStartPosY(), 0);
				
				//Get the direction vector of the second 'X' line
				Vector3D dir2 = new Vector3D(
						cursor.getPosition().x - cursor.getStartPosX(),
						cursor.getPosition().y - cursor.getStartPosY(), 0);
				
				//Normalize them
				dir1.normalizeLocal();
				dir2.normalizeLocal();
				
				
				//Put the vectors into a line class to see if they intersect
				Line2D line1 = new Line2D.Double(lastCursor.getStartPosX(), lastCursor.getStartPosY(), 
						lastCursor.getPosition().x, lastCursor.getPosition().y);
				
				Line2D line2 = new Line2D.Double(cursor.getStartPosX(), cursor.getStartPosY(), 
						cursor.getPosition().x, cursor.getPosition().y);
				
				//Distance from each starting and ending point to the intersection point
				double dist1 = line2.ptLineDist(lastCursor.getStartPosX(), lastCursor.getStartPosY());
				double dist2 = line2.ptLineDist(lastCursor.getPosition().x, lastCursor.getPosition().y);
				double dist3 = line1.ptLineDist(cursor.getStartPosX(), cursor.getStartPosY());
				double dist4 = line1.ptLineDist(cursor.getPosition().x, cursor.getPosition().y);

				
				//If the lines are not co-linear, all the distances from the start
				// points and ends points to the intersection point is more than 30% of the length of each line
				//and they intersect then it triggers and XEvent with GESTURE_ENDED
				if (dir1.dot(dir2) >= -0.95 && dir1.dot(dir2) <= 0.95 && 
						line1.intersectsLine(line2) && dist1 > (0.3*(dist1+dist2)/2) && 
						dist2 > (0.3*(dist1+dist2)/2) && dist3 > (0.3*(dist3+dist4)/2) && 
						dist4 > (0.3*(dist3+dist4)/2)){
					this.fireGestureEvent(new XEvent(this, MTGestureEvent.GESTURE_ENDED, cursor.getCurrentTarget()));
					//test
					System.out.println("X action detected");
				}
				//An 'X' just finished so, set to false
				startX = false;	
						
			} else {
				//First line of an 'X', save when it happened, the cursor that produced it and say that we 
				//have started an 'X' by setting startX to true
				lastTime = System.currentTimeMillis();
				lastCursor = cursor;
				startX = true;
			}
		}
	}
	
	
	
	
	@Override
	public void cursorLocked(InputCursor cursor, IInputProcessor lockingprocessor) {
		this.unLockAllCursors();
		this.fireGestureEvent(new XEvent(this, MTGestureEvent.GESTURE_CANCELED, cursor.getCurrentTarget()));
	}

	
	@Override
	public void cursorUnlocked(InputCursor cursor) {
		//not resumable
	}
	

	@Override
	public String getName() {
		return "X Processor";
	}
	
}
