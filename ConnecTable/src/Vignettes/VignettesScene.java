package Vignettes;

import menu.MTSlideImage;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4jx.components.visibleComponents.widgets.MTPdf;
import org.mt4jx.components.visibleComponents.widgets.pdf.MTPDF;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.video.MTMovieClip;
import org.mt4j.input.gestureAction.DefaultXAction;
import org.mt4j.input.gestureAction.InertiaCircuDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.xProcessor.XProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;


public class VignettesScene extends AbstractScene {
	private String directory;

	public VignettesScene(AbstractMTApplication app, String name, String directory){
		super(app, name);
		this.directory = directory;
		CursorTracer c = new CursorTracer(app, this);
		MTRectangle background = new MTRectangle(0,0,app.width, app.height , app);
		PImage backImage = app.loadImage(directory + System.getProperty("file.separator" ) + "background.jpg");
		background.setTexture(backImage);
		background.setPickable(false);
		getCanvas().addChild(background);
		registerGlobalInputProcessor(c);


		File imageDirectory = new File(getPathToIcons());
		File videoDirectory = new File(getPathToVideos());
		File pdfDirectory = new File(getPathToPdfs());
		File sliderDirectory = new File(getPathToSliders());
		String[] imagesNames = null;
		String[] videosNames = null;
		String[] pdfsNames = null;
		String[] slidersNames = null;
		int count = 0;

		if(imageDirectory.exists()){
			//We get the images from the image directory
			imagesNames = imageDirectory.list(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String name) {
					return (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".JPG"));
				}
			});
			count += imagesNames.length;
		}

		if(videoDirectory.exists()){
			//We get the videos from the video directory
			videosNames = videoDirectory.list(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String name) {
					return (name.endsWith(".avi") || name.endsWith(".AVI"));
				}
			});
			count += videosNames.length;
		}

		if(pdfDirectory.exists()){
			//We get the PDF files from the pdf directory
			pdfsNames = pdfDirectory.list(new FilenameFilter() {

				@Override
				public boolean accept(File arg0, String name) {
					return (name.endsWith(".pdf"));
				}
			});
			count += pdfsNames.length;
		}

		if(sliderDirectory.exists()){
			//We look in the folders to create a slider for each subfolder
			slidersNames = sliderDirectory.list();
			count += slidersNames.length;
		}



		Vector3D center = new Vector3D(app.width/2,app.height/2);
		int currentNb = 0;

		if(imagesNames != null){
			for (int i = 0; i < imagesNames.length ; i++) {
				PImage image = app.loadImage(getPathToIcons() + imagesNames[i]);
				MTImage r = new MTImage(image,app);
				r.removeAllGestureEventListeners();
				r.addGestureListener(DragProcessor.class, new InertiaCircuDragAction(app));
				//r.addGestureListener(XProcessor.class, new DefaultXAction(r));
				getCanvas().addChild(r);
				r.setPositionGlobal(center);
				r.scaleGlobal(0.2f, 0.2f, 1, center);
				r.translate(new Vector3D(0, 200));
				r.rotateZ(center, (float)(-i*(360/count)));
				currentNb++;
			}
		}


		if(videosNames != null){
			for (int i = 0; i < videosNames.length; i++) {
				Vertex upperLeft = new Vertex(0,0,0,0,0);
				MTMovieClip vid = new MTMovieClip(getPathToVideos() + videosNames[i], upperLeft, app);
				vid.addGestureListener(DragProcessor.class, new InertiaCircuDragAction(app));
				getCanvas().addChild(vid);
				vid.setAnchor(PositionAnchor.CENTER);
				vid.setPositionGlobal(center);
				vid.translate(new Vector3D(0, 200));
				vid.rotateZ(center, (float)(-currentNb*(360/count)));
				currentNb++;
			}
		}

		if(slidersNames != null){
			for (int i = 0; i < slidersNames.length; i++) {
				try {
					MTSlideImage slider = new MTSlideImage(getPathToSliders() + slidersNames[i], app);
					slider.addGestureListener(DragProcessor.class, new InertiaCircuDragAction(app));
					getCanvas().addChild(slider);
					slider.setPositionGlobal(center);
					slider.translate(new Vector3D(0, 200));
					slider.rotateZ(center, (float)(-currentNb*(360/count)));
					currentNb++;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		if(pdfsNames != null){
			for (int i = 0; i < pdfsNames.length; i++) {
				//Chargement du PDF
				
				MTPdf pdf;
				try {
					pdf = new MTPdf(getPathToPdfs() + pdfsNames[i], app);
					pdf.addGestureListener(DragProcessor.class, new InertiaCircuDragAction(app));
					getCanvas().addChild(pdf);
					pdf.setPositionGlobal(center);
					pdf.scaleGlobal(0.6f, 0.6f, 1, center);
					pdf.translate(new Vector3D(0, 200));
					pdf.rotateZ(center, (float)(-currentNb*(360/count)));
					currentNb++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}	
				
	@Override
	public void init() {	}

	@Override
	public void shutDown() {	}

	private String getPathToIcons(){
		return  this.directory + System.getProperty("file.separator" ) + "images" + System.getProperty("file.separator" );
	}

	private String getPathToPdfs(){
		return  this.directory + System.getProperty("file.separator" ) + "pdf" + System.getProperty("file.separator" );
	}

	private String getPathToVideos(){
		return  this.directory + System.getProperty("file.separator" ) + "videos" + System.getProperty("file.separator" );
	}

	private String getPathToSliders(){
		return  this.directory + System.getProperty("file.separator" ) + "sliders" + System.getProperty("file.separator" );
	}


}