package org.mt4j.input.gestureAction;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.interfaces.IMTController;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;

public class InertiaCircuDragAction implements IGestureEventListener {

	private float limit;
	private float damping;
	private int integrationTime;
	private AbstractMTApplication app;
	float screenHeight;
	float screenWidth;
	Vector3D center;
	Vector3D org;
	float maxDist;
	float startDist = 200;
	float minDist = 50;
	float scale;
	
	public InertiaCircuDragAction(AbstractMTApplication app2){
		// this(120, 0.85f, 17);
		this(120, 0.9f, 200);
		app = app2;
		screenHeight = app.height;
		screenWidth = app.width;
		center = new Vector3D(screenWidth / 2, screenHeight / 2);
		org = new Vector3D(screenWidth / 2, screenHeight);
		maxDist = center.distance(new Vector3D(screenWidth / 2, screenHeight));
	}

	public InertiaCircuDragAction(int integrationTime, float damping, float maxVelocityLength){
		this.integrationTime = integrationTime;
		this.limit = maxVelocityLength;
		this.damping = damping;
	}

	public boolean processGestureEvent(MTGestureEvent ge) {
		IMTComponent3D t = ge.getTargetComponent();
		if (t instanceof MTComponent) {
			MTComponent comp = (MTComponent) t;
			DragEvent de = (DragEvent)ge;
			IMTController oldController;
		   
			Vector3D orgTo = de.getTo();
			Vector3D orgFrom = de.getFrom();
			Vector3D mvTo = new Vector3D(orgTo.x - center.x, (orgTo.y-center.y));
			Vector3D mvFrom = new Vector3D(orgFrom.x - center.x, (orgFrom.y-center.y));
			float distFrom = center.distance(orgFrom);
			//System.out.println("\n valeur du from"+mvFrom.x+" || "+mvFrom.y+"\n");
			float distTo = center.distance(orgTo);
			float scale = distTo/distFrom;
			
			//System.out.println("mvTo.x = " + mvTo.x + "mvTo.y = "+mvTo.y + "mvTo.z = "+mvTo.z +"distance = " + center.distance2D(new Vector3D(screenWidth/2, screenHeight)));
			/*System.out.println("orgTo.x = "+orgTo.x+" orgTo.y = "+orgTo.y);
			System.out.println("mvfrom.x = " + mvFrom.x + "mvFrom.y = "+mvFrom.y);
			System.out.println("orgFrom.x = "+orgFrom.x+" orgFrom.y = "+orgFrom.y);
			*/
		   
			switch (de.getId()) {
				case DragEvent.GESTURE_DETECTED:
					//System.out.println("1");
				break;
				case DragEvent.GESTURE_UPDATED:
					//System.out.println("2");

					//System.out.println("dans update\n");
					if(distTo < maxDist && distTo > 50)
					{
						comp.scaleGlobal(scale, scale, 1, orgTo);
					}
				
					double angle = Math.atan2(mvTo.y,mvTo.x)-Math.atan2(mvFrom.y,mvFrom.x);
					double angleDeg = Math.toDegrees(angle);
					//System.out.print("valeur de l'angle "+angleDeg+"et l'angle norm" +angle+"\n");
					
					comp.rotateZGlobal(orgTo, (float)angleDeg);
				break;
				case DragEvent.GESTURE_ENDED:
					//System.out.println("3");
					//System.out.println("dans le Ended\n");
					Vector3D vel = de.getDragCursor().getVelocityVector(integrationTime);
					vel.scaleLocal(0.9f); //Test - integrate over longer time but scale down velocity vec
					vel = vel.getLimited(limit);
					oldController = comp.getController();
					comp.setController(new InertiaCircuController(comp, vel, orgTo, mvFrom,oldController));
				break;
				default:
				break;
			}
		}
	return false;
	}


	private class InertiaCircuController implements IMTController{
		private MTComponent target;
		private Vector3D startVelocityVec;
		private Vector3D To;
		private Vector3D mvFrom;
		private Vector3D mvTo;
		private Vector3D vectTmp;
		// private float dampingValue = 0.90f;
		// private float dampingValue = 0.80f;
		// private float dampingValue = 0.45f;
		private IMTController oldController;
		//TODO use animation instead and ease out?
		private float dist;

		private Vector3D zeros = new Vector3D(0,0,0);
		
		public InertiaCircuController(MTComponent target, Vector3D startVelocityVec,
				Vector3D To, Vector3D mvFrom, IMTController oldController) {
			super();
			this.target = target;
			this.To = new Vector3D(To.x,To.y);
			this.mvFrom = new Vector3D(mvFrom.x,mvFrom.y);
			this.mvTo = new Vector3D(mvFrom.x,mvFrom.y);
			this.startVelocityVec = startVelocityVec;
			this.oldController = oldController;
			this.vectTmp = new Vector3D(0,0,0);
			//Animation inertiaAnim = new Animation("Inertia anim for " + target, new MultiPurposeInterpolator(startVelocityVec.length(), 0, 100, 0.0f, 0.5f, 1), target);
			/*
			currentAnimationTime = 0;
			movePerMilli = startVelocityVec.length()/animationTime;
			moveVectNorm = startVelocityVec.getNormalized();
			moveVect = new Vector3D();
			*/
		}
		
		public ArrayList<Vector3D> InTheCenter(Vector3D a, Vector3D b, Vector3D u, Vector3D c, float r){
			
			float dx = (b.x - a.x);
			float dy = (b.y - a.y);
			float dcx = (a.x - c.x);
			float dcy = (a.y - c.y);
			float A = (float) (Math.pow(dx,2) + Math.pow(dy,2));
			float B = 2 * (dx*dcx + dy*dcy);
			float C = (float) (Math.pow(dcx,2) + Math.pow(dcy, 2) - Math.pow(r, 2));
			float delta = (float) (Math.pow(B, 2) - 4*A*C);
			ArrayList<Vector3D> resu = new ArrayList<Vector3D>();
			if (delta > 0) {
				float t1 = (float) ((- B + Math.sqrt(delta))/(2*A));
				float t2 = (float) ((- B - Math.sqrt(delta))/(2*A));
				//Les deux points d'inter appartiennent au vecteur
				if (t1 < 1 && t1 >0 && t2 < 1 && t2 > 0){			
					resu.add(new Vector3D(a.x + dx*t1,a.y+dy*t1));
					resu.add(new Vector3D(a.x + dx*t2,a.y+dy*t2));
					//System.out.println("2 solutions");
				}
				else {
					if (t1 < 1 && t1 > 0){
						resu.add(new Vector3D(a.x + dx*t1,a.y+dy*t1));
						//System.out.println("1 solutions");
					}
					else {
						if (t2 < 1 && t2 > 0){
							resu.add(new Vector3D(a.x + dx*t2,a.y+dy*t2));
							//System.out.println("1 solutions");
						}
						else {
							resu = null;
						}
					}	
				}	
			}
			else {
				//System.out.println("pas de sol");
				resu = null;
			}
			return resu;		
		}
		
		//TODO ? inertia animation is frame based, not time - so framerate decides how long it goes..
		////@Override
		public void update(long timeDelta) {

			startVelocityVec.scaleLocal(damping);
			
			this.vectTmp.setXYZ(To.x, To.y, To.z);
			this.vectTmp.addLocal(startVelocityVec);
			int i =0;
			while (center.distance(this.vectTmp)> maxDist && i<20){
				startVelocityVec.scaleLocal(damping);
				this.vectTmp.setXYZ(To.x, To.y, To.z);
				this.vectTmp.addLocal(startVelocityVec);
				i++;
			}
//			System.out.println("Valeur du oldPos : x = "+oldPos.x+" || y = "+oldPos.y+" z = "+oldPos.z);
//			System.out.println("Valeur du newPos : x = "+newPos.x+" || y = "+newPos.y+" z = "+newPos.z);
//			System.out.println("Valeur du startVelocityVec : x = "+startVelocityVec.x+" || y = "+startVelocityVec.y+" z = "+startVelocityVec.z);

			
			
			float distFrom = center.distance(this.To);
			Vector3D save = this.To.getCopy();
			this.To.addLocal(startVelocityVec);
			float distTo = center.distance(this.To);
			
			this.dist = center.distance(this.To);
			

			
			//this.d = zeros.distance(this.newPos);
			
			
			
			//System.out.println("valeur de d = "+center.distance(this.To)+" maxdist "+maxDist);
			

			

			
			if ((Math.abs(startVelocityVec.x) < 0.05f && Math.abs(startVelocityVec.y) < 0.05f)
					|| this.dist > maxDist){
				startVelocityVec.setValues(Vector3D.ZERO_VECTOR);
				target.setController(oldController);
				return;
			}
			/*
			ArrayList<Vector3D> pts = this.InTheCenter(save, this.To, startVelocityVec, center, 50);
			
			float scale;
			
			//pas d'inter
			if(pts == null){
				scale = Math.abs(distTo/distFrom);
			}
			else {
				//Un pt d'inter
				if (pts.size() == 1){
					//Sortie du centre
					if (distFrom < 50){
						scale = distTo/minDist;
					}
					//Entree dans le centre
					else {
						scale = minDist/distFrom;
					}
				}
				//2 pts d'inter, ca change rien !!
				else {
					scale = Math.abs(distTo/distFrom);
				}
			}
			*/
			
			float scale;
			if (distFrom < 50 && distTo > 50){
				scale = distTo/minDist;
			}
			else {
				if (distTo< 50 && distFrom > 50){
					scale = minDist/distFrom;
				}
				else {
					scale = distTo/distFrom;
				}
			}
			target.translateGlobal(startVelocityVec);
			/*
			if((distTo < maxDist && distTo > 50) || (distFrom < maxDist && distFrom > 50))
			{
				target.scaleGlobal(scale, scale, 1, save);
				System.out.println("valeur de scale  = "+scale);
			}*/
			
			target.scaleGlobal(scale,scale, 1, save);
			
			this.mvTo.addLocal(startVelocityVec);
			
			double angle = Math.atan2(this.mvTo.y,this.mvTo.x)-Math.atan2(mvFrom.y,mvFrom.x);
			double angleDeg = Math.toDegrees(angle);
			
			this.target.rotateZGlobal(this.To, (float)angleDeg);
			this.mvFrom.setXYZ(this.mvTo.x,this.mvTo.y,0);

			
			if (oldController != null){
				oldController.update(timeDelta);
			}
		}
	}
}

