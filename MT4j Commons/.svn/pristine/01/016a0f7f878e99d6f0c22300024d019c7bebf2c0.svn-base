/***********************************************************************
 *   MT4j Extension: MTCircularMenu
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License (LGPL)
 *   as published by the Free Software Foundation, either version 3
 *   of the License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the LGPL
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/
package org.mt4jx.util.animation;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;
import org.mt4j.util.math.ToolsGeometry;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

/**
 * @author Uwe Laufs
 *
 */
public class AnimationUtil {
	public static void rotateOut(final MTPolygon as, final boolean destroyWhenCompleted){
		float width = as.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		IAnimation closeAnim = new AniAnimation(width, 1, 350, AniAnimation.SINE_IN, as);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getValue();
					as.setWidthXYRelativeToParent(currentVal);
					as.rotateZ(as.getCenterPointRelativeToParent(), -ae.getDelta()*0.4f);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					as.setVisible(false);
					if(destroyWhenCompleted){
						as.destroy();
					}
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}
	public static void rotate2D(final MTPolygon as, float degrees){
		IAnimation closeAnim = new AniAnimation(0, -1*degrees, 500, AniAnimation.SINE_IN, as);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					as.rotateZ(as.getCenterPointRelativeToParent(), -ae.getDelta());
					break;
				case AnimationEvent.ANIMATION_ENDED:
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}
	public static void bounceOut(final MTPolygon as, final boolean destroyWhenCompleted){
		float width = as.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		IAnimation closeAnim = new AniAnimation(width, 1, 1000, AniAnimation.BOUNCE_OUT, as);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getValue();
					as.setWidthXYRelativeToParent(currentVal);
//					as.rotateZ(as.getCenterPointRelativeToParent(), -ae.getCurrentStepDelta()*0.4f);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					as.setVisible(false);
					if(destroyWhenCompleted){
						as.destroy();
					}
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}
	
	public static void scaleOut(final MTPolygon as, final boolean destroyWhenCompleted){
		float width = as.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		IAnimation closeAnim = new AniAnimation(width, 1, 300, AniAnimation.LINEAR, as);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getValue();
					as.setWidthXYRelativeToParent(currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					as.setVisible(false);
					if(destroyWhenCompleted){
						as.destroy();
					}
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}
	
	
	public static void scaleIn(final MTPolygon as){
		float width = as.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		IAnimation closeAnim = new AniAnimation(1, width, 300, AniAnimation.LINEAR, as);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getValue();
					as.setWidthXYRelativeToParent(currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}

	public static void translate(final MTComponent as, final float x,
			final float y) {
		{
			IAnimation ani = new AniAnimation(0, 1, 400, AniAnimation.SINE_IN,
					as);
			ani.addAnimationListener(new IAnimationListener() {
				public void processAnimationEvent(AnimationEvent ae) {
					switch (ae.getId()) {
					case AnimationEvent.ANIMATION_STARTED:
					case AnimationEvent.ANIMATION_UPDATED:
						float delta = ae.getDelta();
						as.translate(new Vector3D(delta*x, delta*y));
						break;
					case AnimationEvent.ANIMATION_ENDED:
						break;
					default:
						break;
					}// switch
				}// processanimation
			});
			ani.start();
		}
	}
	public static void moveIntoScreen(AbstractShape as, PApplet pa){
		{
			Vector3D posEvent = as.getCenterPointGlobal();
			
			// move menu if it exits screen
			float width = as.getWidthXY(TransformSpace.LOCAL);
			float height = as.getHeightXY(TransformSpace.LOCAL);
			
			float deltaX1 = posEvent.x-width;
			float deltaX2 = posEvent.x+width-pa.width;
			float deltaY1 = posEvent.y-height;
			float deltaY2 = posEvent.y+height-pa.height;

//			System.out.println("deltaX:"+deltaX1);
//			System.out.println("deltaY2:"+deltaY2);
			
			float translateX=0f;
			float translateY=0f;
			if(deltaX1<0){
				translateX=-deltaX1/2f;
			}else if(deltaX2>0){
				translateX=-deltaX2/2f;
			}
			if(deltaY1<0){
				translateY=-deltaY1/2f;
			}else if(deltaY2>0){
				translateY=-deltaY2/2f;
			}
			if(translateX!=0||translateY!=0){
				AnimationUtil.translate(as, translateX, translateY);
			}
		}
	}
	public static Vector3D getTranslationVector(MTComponent as, MTComponent container){
		// Parent Geometry
		
//		Vector3D[] boundingShapeParent = container.getBounds().getVectorsGlobal();
		float[] minMaxParent = ToolsGeometry.getMinXYMaxXY(as.getBounds().getVectorsGlobal());
		float xMinParent = minMaxParent[0];
		float yMinParent = minMaxParent[1];
		float xMaxParent = minMaxParent[2];
		float yMaxParent = minMaxParent[3];

		Vector3D[] boundingShape = as.getBounds().getVectorsGlobal();
		float[] minMax = ToolsGeometry.getMinXYMaxXY(boundingShape);
		float xMin = minMax[0];
		float yMin = minMax[1];
		float xMax = minMax[2];
		float yMax = minMax[3];

		float deltaX = 0f;
		float deltaY = 0f;
		
		if(xMin<xMinParent){
			deltaX = xMin-xMinParent;
		}
		if(xMax>xMaxParent){
			deltaX = xMax-xMaxParent;
		}
		if(yMin<yMinParent){
			deltaY = yMin-yMinParent;
		}
		if(yMax>yMaxParent){
			deltaY = yMax-yMaxParent;
		}
		System.out.println("delta:" + deltaX + "/" + deltaY);
		
		return new Vector3D(-1*deltaX, -1*deltaY);
	}
//	private static float[] minMaxWithChildren(MTComponent as){
//		MTComponent[] children = as.getChildren();
//		
//		float[] minXYMaxXY = ToolsGeometry.getMinXYMaxXY(as.getBounds().getVectorsGlobal());
//		for (int i = 0; i < children.length; i++) {
//			float[] childMinMax = ToolsGeometry.getMinXYMaxXY(children[i].getBounds().getVectorsGlobal());
//			if(childMinMax[0]<minXYMaxXY[0]){
//				minXYMaxXY[0]=childMinMax[0];
//			}
//			if(childMinMax[1]<minXYMaxXY[1]){
//				minXYMaxXY[1]=childMinMax[1];
//			}
//			if(childMinMax[2]>minXYMaxXY[2]){
//				minXYMaxXY[2]=childMinMax[2];
//			}
//			if(childMinMax[3]>minXYMaxXY[3]){
//				minXYMaxXY[3]=childMinMax[3];
//			}
//		}
//		System.out.println("minMax: " + minXYMaxXY[0] + " " + minXYMaxXY[1] + " " + minXYMaxXY[2] + " " + minXYMaxXY[3]);
//		return minXYMaxXY;
//	}
}

