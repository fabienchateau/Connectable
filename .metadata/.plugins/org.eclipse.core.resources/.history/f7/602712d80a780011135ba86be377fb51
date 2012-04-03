package org.mt4j.components.visibleComponents.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.GestureEventSupport;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.InertiaCircuDragAction;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.IdragClusterable;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.SwingTextureRenderer;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GLTexture;
import org.mt4j.util.opengl.GLTextureSettings;
import org.mt4j.util.opengl.GLTexture.WRAP_MODE;

import processing.core.PApplet;
import processing.core.PImage;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class MTPdfold extends MTRectangle implements IdragClusterable{

	/** The selected. */
	private boolean selected;


	private MTRectangle image;
	private int currentPage = 0;
	private ArrayList<PDFPage> pdfPages;
	private ArrayList<PImage> pages;
	private PagePanel panel;
	private PApplet pApplet;
	private PImage nextImage;
	private PImage previousImage;
	private MTImageButton nextButton;
	private MTImageButton previousButton;
	private float sizeX;
	private float sizeY;

	private int res = 3;

	private static int pdfSizeX = 420/2;
	private static int pdfSizeY = 540/2;

	private static float topBarHeight = 20;

	private static float sideBarWidth = 7;

	private static float bottomBarHeight = 7;

	public MTPdfold(String pdfLink, PApplet pApplet) throws IOException{
		super(-sideBarWidth, -topBarHeight, pdfSizeX + 2*sideBarWidth , pdfSizeY + topBarHeight + bottomBarHeight , pApplet);
		this.setVisible(false);
		this.pApplet = pApplet;
		this.sizeX = pdfSizeX + 2*sideBarWidth;
		this.sizeY = pdfSizeY + topBarHeight + bottomBarHeight;
		this.setSizeLocal(sizeX, sizeY);
		this.setStrokeColor(new MTColor(0,0,0));
		this.setFillColor(new MTColor(50,50,50,200));
		//load a pdf from a byte buffer
		File file = new File(pdfLink);
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);

		// show the first page

		this.pdfPages = new ArrayList<PDFPage>();
		this.pages = new  ArrayList<PImage>();
		this.panel = new PagePanel();

		panel.setSize(pdfSizeX*res, pdfSizeY*res);
		//panel.setSize((420 + 20) * pdffile.getNumPages() , 540);

		for(int i = 1 ; i <= pdffile.getNumPages()+1; i++){
			pdfPages.add(pdffile.getPage(i));            
		}


		panel.showPage(pdfPages.get(currentPage));
		final SwingTextureRenderer str = new SwingTextureRenderer((MTApplication) pApplet, panel);

		//pages.add(str.getTextureToRenderTo());
		//pages[i] = new MTImage(p,pApplet);
		PImage page = str.getTextureToRenderTo();
		str.startTimedRefresh(4000); //give the textpane a little time to fetch the data before drawing
		str.addPaintOcurredListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				str.stopTimedRefresh(); //stop repainting after it was painted once
			}
		});
		this.pages.add(page);

		//we launch the thread...
		LoadingTh th = new LoadingTh(pdfPages, pages, pApplet);
		th.start();


		Vertex pdfUpperLeft = new Vertex(topBarHeight, sideBarWidth, 0 ,0);
		image = new MTRectangle(page, pApplet);
		image.setStrokeColor(new MTColor(255,255,255,255));
		image.setPickable(false);
		image.setSizeLocal(pdfSizeX, pdfSizeY);
		this.addChild(image);

		//Draw this component and its children above 
		//everything previously drawn and avoid z-fighting
		this.setDepthBufferDisabled(true);

		nextImage = pApplet.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() + "nextButton64.png");
		nextButton = new MTImageButton(nextImage, pApplet);

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent r) {
				switch (r.getID()) {
				case TapEvent.BUTTON_CLICKED:
					nextPage(); 
					break;
				default:
					break;
				}
			}
		});
		this.addChild(nextButton);
		nextButton.setNoStroke(true);
		nextButton.setSizeLocal(20, 20);
		nextButton.setPositionRelativeToParent(new Vector3D(this.sizeX - 20, -10));
		//this.setAnchor(PositionAnchor.UPPER_LEFT);
		//Vector3D upperRight = new Vector3D(upperLeft.x + this.getWidthXY(TransformSpace.LOCAL), upperLeft.y);
		//Vector3D closeButtonPos = new Vector3D(upperRight.x - nextButton.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), upperRight.y);
		//nextButton.setAnchor(PositionAnchor.UPPER_LEFT);
		//nextButton.setPositionRelativeToParent(closeButtonPos);

		previousImage = pApplet.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() + "previousButton64.png");
		previousButton = new MTImageButton(previousImage, pApplet);

		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent r) {
				switch (r.getID()) {
				case TapEvent.BUTTON_CLICKED:
					previousPage(); 
					break;
				default:
					break;
				}
			}
		});
		this.addChild(previousButton);
		previousButton.setNoStroke(true);
		previousButton.setSizeLocal(20, 20);
		previousButton.setPositionRelativeToParent(new Vector3D(5, -10));

		//        this.registerInputProcessor(new TapProcessor(pApplet, 30));
		//		this.addGestureListener(GestureEventSupport.class, new IGestureEventListener() {
		//			public boolean processGestureEvent(MTGestureEvent ge) {
		//				TapEvent te = (TapEvent)ge;
		//				if (te.is()){
		//					if (movieClip != null && movieClip.movie != null){
		////						if (movieClip.getMovie().isPlaying()){
		//						if (!playSymbol.isVisible()){
		////							System.out.println("Pause!");
		//							if (playSymbol != null){
		//								playSymbol.setVisible(true);
		//							}
		//							if (slider != null){
		//								slider.setVisible(false);
		//							}
		//							movieClip.loop(); //FIXME TEST
		//							movieClip.pause();
		//						}else{
		////							System.out.println("Play!");
		//							if (playSymbol != null){
		//								playSymbol.setVisible(false);
		//							}
		//							if (slider != null){
		//								slider.setVisible(true);
		//							}	
		//							movieClip.loop();
		//						}
		//						
		//					}
		//				}
		//				return false;
		//			}
		//		});

		this.setVisible(true);
	}

	public void changePage(int nb){
		System.out.println("page " + (nb+1) + " / " + this.pages.size());
		this.removeAllChildren();
		image = new MTRectangle(this.pages.get(nb), pApplet);
		image.setStrokeColor(new MTColor(255,255,255,255));
		image.setPickable(false);
		image.setSizeLocal(pdfSizeX, pdfSizeY);
		this.addChild(image);
		this.addChild(nextButton);
		this.addChild(previousButton);
	}


	public MTRectangle getImage(){
		return this.image;
	}

	public void nextPage(){
		if(currentPage < pages.size() - 1)
			changePage(++currentPage);

	}

	public void previousPage(){
		if(currentPage > 0)
			changePage(--currentPage);
	}

	public void firstPage(){
		changePage(0);
	}

	public void lastPage(){
		changePage(pages.size());
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}




	/*internal class Thread*/
	public class LoadingTh extends Thread {

		private ArrayList<PDFPage> pdfPagesTh;
		private PagePanel panelTh;
		private ArrayList<PImage> pagesTh;
		private PApplet pAppletTh;
		private SwingTextureRenderer strTh;
		private PImage imgTh;

		public LoadingTh(ArrayList<PDFPage> pdfPages, ArrayList<PImage> pages, PApplet pApplet) {
			this.pdfPagesTh = pdfPages;
			this.pAppletTh = pApplet;
			this.panelTh = new PagePanel();
			this.pagesTh = pages;

		}
		public void run() {
			for (int i = 1 ; i< this.pdfPagesTh.size() ; i++){
				this.panelTh = new PagePanel();
				this.panelTh.setSize(pdfSizeX*res, pdfSizeY*res);
				panelTh.showPage(this.pdfPagesTh.get(i));
				this.strTh = new SwingTextureRenderer((MTApplication) this.pAppletTh, this.panelTh);
				System.out.println("Thread Début");
				this.imgTh = this.strTh.getTextureToRenderTo();
				this.strTh.startTimedRefresh(4000); //give the textpane a little time to fetch the data before drawing
				this.strTh.addPaintOcurredListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						strTh.stopTimedRefresh(); //stop repainting after it was painted once
					}
				});
				System.out.println("Thread fin");
				this.pagesTh.add(this.imgTh);
			}
			this.pagesTh.remove(this.pagesTh.size()-1);
		}

	}

}
