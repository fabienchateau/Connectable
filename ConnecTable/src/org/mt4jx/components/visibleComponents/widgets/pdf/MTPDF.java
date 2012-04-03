/***********************************************************************
 *   MT4j Extension: PDF
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
package org.mt4jx.components.visibleComponents.widgets.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

import com.sun.pdfview.PDFFile;

/**
 * @author Uwe Laufs
 *
 */
public class MTPDF extends MTRectangle {
	private File pdf;
	private int pageNumber;
	private int numberOfPages=1;
	private RenderedPDFPage currentPage;
	private boolean rendering = false;
	private MTRectangle infoLayer;
		private MTTextArea pageLabel;
	
	private static CachedPDFPageLoader previewLoader = new CachedPDFPageLoader(1.0, 10);
	/**
	 * limit in pixels for pdf rendering result to avoid memory problems
	 */
	private int sizeLimitX=1920;
	private boolean autoUpdate = true;
	
	private PApplet app;
	
	public MTPDF(PApplet app, File pdf){
		this(app, pdf, 1);
		this.app = app;
	}
	public MTPDF(PApplet pApplet, File pdf, int pageNumber){
		super(pApplet,0,0);
		this.pdf = pdf;
		this.app = pApplet;
		this.pageNumber = pageNumber;
		
        PDFFile pdffile;
		try {
			pdffile = null;
			File file = pdf;
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			pdffile = new PDFFile(buf);
			this.numberOfPages = pdffile.getNumPages();
			buf.clear();
			channel.close();
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.currentPage = previewLoader.get(pdf, pageNumber);
		this.setTexture(this.currentPage.getPImage());
//		updateTexture();
		
		this.setWidthLocal(this.currentPage.getPImage().width);
		this.setHeightLocal(this.currentPage.getPImage().height);
		
		final PApplet pa = pApplet;
		this.addGestureListener(ScaleProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				if(ge instanceof ScaleEvent && autoUpdate){
					switch (ge.getId()) {
					case ScaleEvent.GESTURE_ENDED:
						((MTApplication)pa).invokeLater(
								new Thread(){
								public void run(){
									updateTexture();
								}
							}
						);
						updateTexture();
						break;
					default:
						break;
					}
				}
				return false;
			}
		});
		addListener();
		this.infoLayer = new MTRectangle(app, this.getWidthXYGlobal(), this.getHeightXYGlobal());
		this.infoLayer.setPickable(false);
		this.infoLayer.setNoFill(true);
		this.infoLayer.setNoFill(true);
		this.infoLayer.setVisible(false);
		pageLabel = new MTTextArea(app, createFont());
			pageLabel.setNoFill(true);
			pageLabel.setNoStroke(true);
			updateInfoLayer();
		{
			float posX = this.getCenterPointGlobal().x;
			float posy = this.getCenterPointGlobal().y + (this.getHeightXYGlobal()/2f) - pageLabel.getHeightXY(TransformSpace.GLOBAL);
			pageLabel.setPositionGlobal(new Vector3D(posX,posy));
		}
		this.infoLayer.addChild(pageLabel);
		this.addChild(infoLayer);
	}
	
	public void updateInfoLayer(){
		pageLabel.setText(getPageNumber() + "/" + getNumberOfPages());
	}
	public void setShowInfoLayer(boolean show){
		this.infoLayer.setVisible(show);
	}
	
	public void setPageNumber(int pn){
		int pnumber;
		if(pn<1){
			pnumber = 1;
		}else{
			pnumber = pn;
		}
		if(pn>this.numberOfPages){
			pnumber = this.numberOfPages;
		}
		RenderedPDFPage page= null;
//		try {
			setRenderingFlag(true);
			page = previewLoader.get(pdf, pnumber);
			this.currentPage = page;
			setRenderingFlag(false);
			updateTexture();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
			System.out.println("page==null: " + (page==null));
		this.setTexture(page.getPImage());
		this.pageNumber = pnumber;
		this.updateInfoLayer();
		if(pn<this.numberOfPages){
			new PrefetchThread(previewLoader, pdf, new int[]{pageNumber-1, pageNumber+1}).start();
		}else{
			new PrefetchThread(previewLoader, pdf, new int[]{pageNumber-1}).start();
		}

	}

	public int getPageNumber(){
		return this.pageNumber;
	}
	
	public boolean isAutoUpdate() {
		return autoUpdate;
	}
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	public synchronized void updateTexture(){
		if(!isRendering()){
			System.out.println("RE-RENDER TEXTURE");
			setRenderingFlag(true);
			new ThreadAndPreDrawAction(((MTApplication)app).getCurrentScene()) {
				private RenderedPDFPage page = null;
				@Override
				public void doFirstThreaded() {
					this.setPriority(Thread.MIN_PRIORITY);
					// round to full pixels
					int width = Math.round(getWidthXYGlobal());
					int height = Math.round(getHeightXYGlobal());
					int textureWidth,textureHeight;
					
					if(width>sizeLimitX){
						double ratio = ((double)width)/((double)height);
						textureWidth = sizeLimitX;
						textureHeight = (int)Math.round((double)sizeLimitX*(double)ratio);
					}else{
						textureWidth = width;
						textureHeight = height;
					}
					// resize to full pixels
					setSizeXYGlobal(width, height);
					try {
						// TODO: use double image size as texture (better while scaling up) ?
						setRenderingFlag(true);
						page = PDFRenderer.render(pdf, textureWidth, textureHeight, pageNumber);
						setRenderingFlag(false);
					} catch (IOException e) {
						setRenderingFlag(false);
						throw new RuntimeException(e);
					}
				}
				@Override
				public void doSecondPreDraw() {
					// drop if outdated page
					if(page.getPageNumber()==getPageNumber()){
						setTexture(page.getPImage());
					}else{
						maybeRender();
					}
				}
			};
		}else{
			System.out.println("IGNORE Page change");
		}
	}
	private synchronized void setRenderingFlag(boolean flag){
		this.rendering = flag;
	}
	private synchronized boolean isRendering(){
		return rendering;
	}
	private synchronized void maybeRender(){
		if(!isRendering()){
			// retry if outdated and all threads have finished. last one will render (!?)
			updateTexture();
		}
	}
	public int getNumberOfPages() {
		return numberOfPages;
	}
	@Override
	public void destroy() {
		this.previewLoader.clear();
		super.destroy();
	}
	/**
	 * Sets maximum width for rendering (size is limited by memory restrictions)
	 * @param sizeLimitX Maximum with for rendering
	 */
	public void setSizeLimitX(int sizeLimitX) {
		this.sizeLimitX = sizeLimitX;
	}
	private void addListener(){
		System.err.println("register TapAndHoldProcessor");
		//Add tap&hold gesture to clear all tails
		TapAndHoldProcessor tapAndHold = new TapAndHoldProcessor((MTApplication)app);
		tapAndHold.setLockPriority(1.1f);
		tapAndHold.setMaxFingerUpDist(10);
		tapAndHold.setHoldTime(500);
		this.registerInputProcessor(tapAndHold);
		this.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {

				TapAndHoldEvent t = (TapAndHoldEvent)ge;

				if (t.getId() == TapAndHoldEvent.GESTURE_DETECTED){
//					System.out.println("STARTED");
					setShowInfoLayer(true);
				}else if (t.getId() == TapAndHoldEvent.GESTURE_ENDED){
					setShowInfoLayer(false);
				}
//				// funzt leider nicht...
//				if (t.getId() == TapAndHoldEvent.GESTURE_ENDED && t.isHoldComplete()){
//					System.out.println("HOLD COMPLETE");
//				}
				return false;
			}
		});
	}
	private IFont createFont() {
//		return FontManager.getInstance().createFont(getRenderer(), "Arial.bold", 22);
		return FontManager.getInstance().createFont(getRenderer(), "Arial.bold", 20, new MTColor(0,0,0,150));
	}
	
}
