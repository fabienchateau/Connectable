/***********************************************************************
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
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
package org.mt4j.components.visibleComponents.widgets.video;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.ILassoable;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;
import codeanticode.gsvideo.GSMovie;

/**
 * The Class MTMovieClip. 
 * A widget which can be used as a video player.
 * <br>NOTE: Needs to have the GStreamer framework to be installed on the system.
 * 
 * @author Chris
 */
public class MTMovieClip extends MTRectangle implements ILassoable {
	
	/** The app. */
	private PApplet app;
	
	/** The selected. */
	private boolean selected;
	
	/** The font. */
	private IFont font;
	
	/** The slider. */
	private MTSlider slider;
	
	/** The dragging. */
	private boolean stopSliderAdvance;
	
	private MovieClip movieClip;
	
	private float topBarHeight = 30;
	
	private float sideBarWidth = 15;
	
	private float bottomBarHeight = 30;
	
	private PlaySymbol playSymbol;

	private MTImageButton closeButton;

	private MTSlider volumeSlider;
	
	private float firstSizeHeight;
	private float firstSizeWidth;
	
	//TODO tap on slider -> amount settable?
	//TODO delete button svg's from SVN?
	//TODO (volume control icon)

	
	//TODO we actually would need some sort of command queue since this
	//has to work asynchronously -> read from queue after the next frame
	//FIXME error at pause sometimes when using gplayer.isPlaying() -> report bug
	
	public MTMovieClip(String movieFile, Vertex upperLeft, PApplet pApplet) {
		this(movieFile, upperLeft, 30, pApplet);
	}
	
	
	public MTMovieClip(String movieFile, Vertex upperLeft, int ifps, PApplet pApplet) {
		super(upperLeft, 320, 180, pApplet);
		this.app = pApplet;
		this.selected = false;
		stopSliderAdvance = false;
		this.firstSizeHeight = 320 + 2*sideBarWidth;
		this.firstSizeWidth = 180 + topBarHeight + bottomBarHeight;
		this.setSizeLocal(firstSizeHeight, firstSizeWidth);
		this.setStrokeColor(new MTColor(0,0,0));
		this.setFillColor(new MTColor(50,50,50,200));
//		this.setNoFill(true);
		
		//Create movieclip child
		Vertex movieClipUpperLeft = new Vertex(upperLeft);
		movieClipUpperLeft.y += topBarHeight;
		movieClipUpperLeft.x += sideBarWidth;
		this.movieClip = new MovieClip(movieFile, movieClipUpperLeft, ifps, pApplet);
		this.movieClip.setStrokeColor(new MTColor(0,0,0));
		this.movieClip.setStrokeWeight(0.5f);
		this.movieClip.setNoFill(true);
		this.movieClip.setNoStroke(true);
//		this.movieClip.setSizeXYGlobal(320, 180);
		this.addChild(movieClip);
		
//		MTSvgButton playButton = new MTSvgButton(MT4jSettings.getInstance().getDefaultSVGPath() 
//				+ "play.svg" , pApplet);
//		playButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				switch (arg0.getID()) {
//				case TapEvent.BUTTON_CLICKED:
//					if (movieClip != null && movieClip.movie != null){
//						movieClip.loop();
//						if (slider != null){
//							slider.setVisible(true);
//						}
//					}
//					break;
//				default:
//					break;
//				}
//			}
//		});
//		playButton.scale(0.5f, 0.5f, 1, new Vector3D(0,0,0));
//		playButton.translate(upperLeft);
//		this.addChild(playButton);

//		MTSvgButton stopButton = new MTSvgButton(MT4jSettings.getInstance().getDefaultSVGPath() 
//				+ "stop.svg" , pApplet);
//		stopButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				switch (arg0.getID()) {
//				case TapEvent.BUTTON_CLICKED:
//					if (movieClip != null && movieClip.movie != null){
////						movie.stop();
//////						movie.pause();
////						movie.goToBeginning();
//						
////						if (getRenderer() instanceof MTApplication) {
////							final MTApplication app = (MTApplication) getRenderer() ;
////							movie.goToBeginning();
////							app.invokeLater(new Runnable() {
////								public void run() {
////									app.invokeLater(new Runnable() {
////										public void run() {
////											movie.pause();
////										}
////									});
////								}
////							});
////						}else{
//							movieClip.goToBeginning();
//							movieClip.pause();	
////						}
////						movie.stop();
//					}
//					if (slider != null){
//						slider.setVisible(false);	
//					}
//					break;
//				default:
//					break;
//				}
//			}
//		});
//		//m�sste eigentlich gr�sste comp aus svg holen, dann center an die stelle positionieren
//		this.addChild(stopButton);
//		stopButton.scale(0.5f, 0.5f, 1, new Vector3D(0,0,0));
//		stopButton.translate(new Vector3D(upperLeft.x + 30 , upperLeft.y, upperLeft.z));
		
		float movieClipWidth = movieClip.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		playSymbol = new PlaySymbol(app,  this.movieClip.getCenterPointRelativeToParent(), movieClipWidth/16f, movieClipWidth/16f, 35);
		this.addChild(playSymbol);
		
		
		this.registerInputProcessor(new TapProcessor(app, 30));
		this.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					if (movieClip != null && movieClip.movie != null){
//						if (movieClip.getMovie().isPlaying()){
						if (!playSymbol.isVisible()){
//							System.out.println("Pause!");
							if (playSymbol != null){
								playSymbol.setVisible(true);
							}
							if (slider != null){
								slider.setVisible(false);
							}
							movieClip.loop(); //FIXME TEST
							movieClip.pause();
						}else{
//							System.out.println("Play!");
							if (playSymbol != null){
								playSymbol.setVisible(false);
							}
							if (slider != null){
								slider.setVisible(true);
							}	
							movieClip.loop();
						}
						
					}
				}
				return false;
			}
		});
		
		//Title bar
		font = FontManager.getInstance().createFont(app, "Calibri", 16, MTColor.BLACK);
		
		MTTextArea titleBar = new MTTextArea(app, font);
		titleBar.setFillColor(new MTColor(150,150,150,255));
		titleBar.setNoFill(false);
		titleBar.setStrokeColor (MTColor.BLACK);
		titleBar.setNoStroke(false);
		String movieUrl = movieClip.getName();
		String movieName = movieUrl.substring(movieUrl.lastIndexOf("\\")+1, movieUrl.length() - 4);
		titleBar.setText(movieName);
		this.addChild(titleBar);	
		Vector3D titleBarPos = new Vector3D(this.getCenterPointLocal().x,topBarHeight -15 , 0);
		titleBar.setPositionRelativeToParent(titleBarPos);
		
		
		
		//Close button
		PImage closeButtonImage = app.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() +
		"closeButton64.png");
		closeButton = new MTImageButton(app, closeButtonImage);
		closeButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					close(); 
				}
				return false;
			}
		});
		this.addChild(closeButton);
		closeButton.setNoStroke(true);
		closeButton.setSizeXYRelativeToParent(topBarHeight - 0, topBarHeight - 0);
		this.setAnchor(PositionAnchor.UPPER_LEFT);
		Vector3D upperRight = new Vector3D(upperLeft.x + this.getWidthXY(TransformSpace.LOCAL), upperLeft.y);
		Vector3D closeButtonPos = new Vector3D(upperRight.x - closeButton.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), upperRight.y);
		closeButton.setAnchor(PositionAnchor.UPPER_LEFT);
		closeButton.setPositionRelativeToParent(closeButtonPos);
		
		
	}
	
	
	
	private class PlaySymbol extends MTEllipse{

		public PlaySymbol(PApplet pApplet, Vector3D centerPoint, float radiusX,	float radiusY, int segments) {
			super(pApplet, centerPoint, radiusX, radiusY, segments);
			
			Vertex[] vertices = new Vertex[]{
				new Vertex(centerPoint.x - radiusX/3f, centerPoint.y - radiusY/3f, centerPoint.z),
				new Vertex(centerPoint.x + radiusX/2.5f, centerPoint.y, centerPoint.z),
				new Vertex(centerPoint.x - radiusX/3f, centerPoint.y + radiusY/3f, centerPoint.z),
				new Vertex(centerPoint.x - radiusX/3f, centerPoint.y - radiusY/3f, centerPoint.z),
			};
			MTPolygon triangle = new MTPolygon(pApplet, vertices);
			triangle.setPickable(false);
			triangle.setNoFill(true);
			triangle.setStrokeColor(new MTColor(0,0,0,255));
			triangle.setStrokeWeight(1.5f);
			this.addChild(triangle);
			
			this.setComposite(true);
			this.setPickable(false); //We tap on the movie itself instead
			this.setFillColor(new MTColor(150,150,150,255));
			this.setStrokeColor(new MTColor(0,0,0,255));
			this.setStrokeWeight(1.5f);
		}
		
		
		@Override
		protected void setDefaultGestureActions() {
		}
		
		@Override
		protected IBoundingShape computeDefaultBounds() {
			return null;
		}
		
	}

	
	private class MovieClip extends MTVideoTexture{
		public MovieClip(String movieFile, Vertex upperLeft, int ifps, PApplet pApplet) {
			super(movieFile, upperLeft, ifps, pApplet);
			
			this.setPickable(false);
		}

		@Override
		protected void onFirstFrame() {
			super.onFirstFrame();

			GSMovie m = getMovie();
			if (m != null){
				this.setNoFill(false);
				this.setNoStroke(false);
				
				//Resize MTMovieClip
//				movieClip.setSizeXYGlobal(320, 180);
				MTMovieClip.this.setSizeLocal(320 + 2*sideBarWidth, 180 + topBarHeight + bottomBarHeight);
//				movieClip.setSizeXYGlobal(320, 180);
				System.out.println(movieClip.getWidthXY(TransformSpace.RELATIVE_TO_PARENT));
//				 MTMovieClip.this.setSizeLocal(movieClip.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) + 2*sideBarWidth, movieClip.getHeightXY(TransformSpace.RELATIVE_TO_PARENT) + topBarHeight + bottomBarHeight);

//				float sizeWidth = movieClip.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) + 2*sideBarWidth;
//				float sizeHeight = movieClip.getHeightXY(TransformSpace.RELATIVE_TO_PARENT) + topBarHeight + bottomBarHeight;
//				MTMovieClip.this.setSizeLocal(sizeWidth, sizeHeight);
//				MTMovieClip.this.translate(new Vector3D(-(sizeWidth/2f - firstSizeWidth/2f), -(sizeHeight/2f - firstSizeHeight/2f), 0), TransformSpace.LOCAL);
				//Reposition movie
				PositionAnchor oldAnchor = MTMovieClip.this.getAnchor();
				MTMovieClip.this.setAnchor(PositionAnchor.LOWER_LEFT);
				Vector3D lowerLeft = MTMovieClip.this.getPosition(TransformSpace.LOCAL);
				MTMovieClip.this.setAnchor(oldAnchor);
				
				//Reposition close button
				MTMovieClip.this.setAnchor(PositionAnchor.UPPER_LEFT);
				Vector3D upperLeft = MTMovieClip.this.getPosition(TransformSpace.LOCAL);
				this.setAnchor(PositionAnchor.UPPER_LEFT);
				Vector3D upperRight = new Vector3D(upperLeft.x + MTMovieClip.this.getWidthXY(TransformSpace.LOCAL), upperLeft.y);
				Vector3D closeButtonPos = new Vector3D(upperRight.x - closeButton.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) - sideBarWidth, upperRight.y);
				closeButton.setPositionRelativeToParent(closeButtonPos);
				MTMovieClip.this.setAnchor(oldAnchor);
				//Reposition play symbol
				if (playSymbol != null){
//					playSymbol.setSizeXYRelativeToParent(this.getHeightXY(TransformSpace.LOCAL), this.getHeightXY(TransformSpace.LOCAL));
					playSymbol.setPositionRelativeToParent(new Vector3D(35,190,0));
				}
				
				//Create movie seek Slider
				float sliderXPadding = 10;
				float sliderYPadding = 5;
				float sliderHeight = bottomBarHeight - 2*sliderYPadding;
				slider = new MTSlider(lowerLeft.x + sliderXPadding, lowerLeft.y - sliderHeight - sliderYPadding, MTMovieClip.this.getWidthXY(TransformSpace.LOCAL) - sliderXPadding*2, sliderHeight, 0, 10, app);
				slider.getOuterShape().setFillColor(new MTColor(0, 0, 0, 80));
				slider.getOuterShape().setStrokeColor(new MTColor(0, 0, 0, 80));
				slider.getKnob().setFillColor(new MTColor(100, 100, 100, 80));
				slider.getOuterShape().setStrokeColor(new MTColor(100, 100, 100, 80));
				slider.getKnob().addGestureListener(DragProcessor.class, new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						DragEvent de = (DragEvent)ge;
						switch (de.getId()) {
						case MTGestureEvent.GESTURE_DETECTED:
							stopSliderAdvance = true;
							break;
						case MTGestureEvent.GESTURE_UPDATED:
							break;
						case MTGestureEvent.GESTURE_ENDED:
							if (movieClip != null && movieClip.getMovie() != null /*&& movieClip.getMovie().isPlaying()*/){
								float currValue = slider.getValue();
								movieClip.jump(currValue);
							}
							stopSliderAdvance = false;
							break;
						default:
							break;
						}
						return false;
					}
				});
				//Dont do every frame! Duration is only valid if playing..
				slider.setValueRange(0, m.duration());
				
				slider.getOuterShape().addGestureListener(TapProcessor.class, new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent)ge;
						switch (te.getTapID()) {
						case TapEvent.BUTTON_DOWN:
							stopSliderAdvance = true;
							break;
						case TapEvent.BUTTON_UP:
							stopSliderAdvance = false;
							break;
						case TapEvent.BUTTON_CLICKED:
							if (movieClip != null && movieClip.getMovie() != null /*&& movieClip.getMovie().isPlaying()*/){
								float currValue = slider.getValue();
								movieClip.jump(currValue);
							}
							stopSliderAdvance = false;
							break;
						default:
							break;
						}
						return false;
					}
				});
				if (app instanceof MTApplication) {
					MTApplication mtApp = (MTApplication) app;
					mtApp.invokeLater(new Runnable() {
						public void run() {
							MTMovieClip.this.addChild(slider);
						}
					});
				}else{
					this.addChild(slider);
				}
				slider.setVisible(true);
				
				//Create volume slider
				float volSliderWidth = this.getWidthXY(TransformSpace.LOCAL)/7f;
				float volSliderHeight = topBarHeight - 2*sliderYPadding;
				this.setAnchor(PositionAnchor.UPPER_LEFT);
				Vector3D movieUpperLeft = this.getPosition(TransformSpace.RELATIVE_TO_PARENT);
				volumeSlider = new MTSlider(movieUpperLeft.x + 1.5f, movieUpperLeft.y - volSliderHeight - 1.5f, volSliderWidth, volSliderHeight, 0, 1, app);
				volumeSlider.getOuterShape().setFillColor(new MTColor(0, 0, 0, 80));
				volumeSlider.getOuterShape().setStrokeColor(new MTColor(0, 0, 0, 80));
				volumeSlider.getKnob().setFillColor(new MTColor(100, 100, 100, 80));
				volumeSlider.getOuterShape().setStrokeColor(new MTColor(100, 100, 100, 80));
				if (app instanceof MTApplication) {
					MTApplication mtApp = (MTApplication) app;
					mtApp.invokeLater(new Runnable() {
						public void run() {
							MTMovieClip.this.addChild(volumeSlider);
						}
					});
				}else{
					this.addChild(volumeSlider);
				}
				volumeSlider.setVisible(true);
				volumeSlider.setValue(1);
				volumeSlider.addPropertyChangeListener("value", new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent e) {
							if (movie != null){
								movie.volume(((Float)e.getNewValue()).doubleValue());
							}
					}
				});
			}
		}
		
		@Override
		protected void onNewFrame() {
			super.onNewFrame();
			
			GSMovie m = getMovie();
			if (!stopSliderAdvance && m != null && slider != null){
				slider.setValue(m.time()); //ONLY UPDATE the slider position WHEN NOT DRAGGING THE SLIDER
			}
		}
		
		@Override
		protected void setDefaultGestureActions() {
			
		}
		
		@Override
		public void play() {
			super.play();
			if (slider != null){
				slider.setVisible(true);
			}
		}
		
		@Override
		public void loop() {
			super.loop();
			if (slider != null){
				slider.setVisible(true);
			}
		}
		
		
	}
	
	
	/**
	 * Stops and Closes the movieclip with an animation.
	 */
	public void close(){
		float width = this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		IAnimation closeAnim = new Animation("Window Fade", new MultiPurposeInterpolator(width, 1, 350, 0.2f, 0.5f, 1), this);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					setWidthXYRelativeToParent(currentVal);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					setVisible(false);
					destroy();
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}

	
	@Override
	protected void destroyComponent() {
		super.destroyComponent();
		
		this.movieClip.noLoop();
//		if (this.movieClip.getMovie().getGplayer().isPlaying()){ //gplayer.isPlaying still hangs the program sometimes..
//		if (this.movieClip.getMovie().isPlaying()){ //not as accurate..
//			this.movieClip.getMovie().dispose(); //DONE IN MTVideoTexture child..
//		}
	}
	
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Gets the video texture.
	 * 
	 * @return the video texture
	 */
	public MTVideoTexture getVideoTexture(){
		return this.movieClip;
	}

}
