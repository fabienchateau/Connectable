package org.mt4j.input.gestureAction;
/* Erik Paluka - June 2011
 * Vialab at the University of Ontario Institute of Technology
 * Summer Research Assistant with Dr. Collins (Summer 2011)
 * This class performs the default action for the 'X' gesture.
 * It is currently set to exit the program.
 */
import org.mt4j.components.MTComponent;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.xProcessor.XEvent;


public class DefaultXAction implements IGestureEventListener, ICollisionAction{
	
	/** The target. */
	private IMTComponent3D target;
	
	/** The last event. */
	private MTGestureEvent lastEvent;
	
	/** The gesture aborted. */
	private boolean gestureAborted = false;	
	
	public DefaultXAction(IMTComponent3D customTarget){
		this.target = null;

	}
	
	@Override
	public boolean gestureAborted() {
		return this.gestureAborted;
	}

	@Override
	public void setGestureAborted(boolean aborted) {
		this.gestureAborted = aborted;
	}

	@Override
	public MTGestureEvent getLastEvent() {
		return this.lastEvent;
	}

	@Override
	public boolean processGestureEvent(MTGestureEvent g) {
		if (g instanceof XEvent){
			XEvent xEvent = (XEvent)g;
			this.lastEvent = xEvent;
			
			if (target == null)
				target = xEvent.getTarget(); 
			
			switch (xEvent.getId()) {
			case MTGestureEvent.GESTURE_STARTED:
				break;
			case MTGestureEvent.GESTURE_RESUMED:
				if (target instanceof MTComponent){
					((MTComponent)target).sendToFront();
				}
				break;
			case MTGestureEvent.GESTURE_UPDATED:
				break;
			case MTGestureEvent.GESTURE_CANCELED:
				break;
			case MTGestureEvent.GESTURE_ENDED:
				doAction(target, xEvent);
				break;			
			default:
				break;
			}
		}
		return false;
	}
	
	protected void doAction(IMTComponent3D comp, XEvent xe){
		if(!gestureAborted() )
		{	
			System.exit(0);
		} 
	}

}
