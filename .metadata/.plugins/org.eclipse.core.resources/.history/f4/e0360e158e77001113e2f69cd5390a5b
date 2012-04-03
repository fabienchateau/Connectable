package org.mt4j.components.visibleComponents.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.IdragClusterable;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * @author VirtualSens
 * This class allows to create a simple slider for a directory full of images.
 * The images must be all the same size in order to be well displayed or at least smaller than the first image
 * All images are sorted by alphabetically by name
 *
 */
public class MTSlideImage extends MTRectangle implements IdragClusterable{

	private boolean selected;
	private int currentImage = 0;
	private PImage nextImage;
	private PImage previousImage;
	private MTImageButton nextButton;
	private MTImageButton previousButton;
	private MTImage image;
	private ArrayList<MTImage> images;
	private float sizeX;
	private float sizeY;
	private static float maxSize = 640;
	private static float imageSizeX = 640;
	private static float imageSizeY = 480;
	private static float topBarHeight = 20;
	private static float sideBarWidth = 7;
	private static float bottomBarHeight = 7;
	
	/**
	 * @param directory The directory where the images are
	 * @param pApplet The parent Applet
	 * @throws IOException
	 */
	public MTSlideImage(String directory, final PApplet pApplet) throws IOException{
		super(-sideBarWidth, -topBarHeight, imageSizeX + 2*sideBarWidth , imageSizeY + topBarHeight + bottomBarHeight , pApplet);
		this.setVisible(false);
		this.images = new ArrayList<MTImage>();
		File imageDirectory = new File(directory);

		/*We get the images from the image directory */
		String imagesNames[] = imageDirectory.list(new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String name) {
				return (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".JPG"));
			}
		});

		Arrays.sort(imagesNames, String.CASE_INSENSITIVE_ORDER);
		
		

		for(int i = 0; i < imagesNames.length; i++){
			PImage img = pApplet.loadImage(directory + System.getProperty("file.separator" ) + imagesNames[i]);
			image = new MTImage(img, pApplet);
			float h = image.getHeightXY(TransformSpace.LOCAL);
			if(h > maxSize){
				image.scale(maxSize/h, maxSize/h, 1, image.getNormal());
			}
			float w = image.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
			if(w > maxSize){
				image.scale(maxSize/w, maxSize/w, 1, image.getNormal());
			}

			images.add(image);
		}

		image = images.get(currentImage);
		imageSizeX = image.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		imageSizeY = image.getHeightXY(TransformSpace.RELATIVE_TO_PARENT);
		this.sizeX = imageSizeX + 2*sideBarWidth;
		this.sizeY = imageSizeY + topBarHeight + bottomBarHeight;
		this.setSizeLocal(sizeX, sizeY);
		this.setStrokeColor(new MTColor(0,0,0));
		this.setFillColor(new MTColor(50,50,50,200));



		image.setSizeLocal(imageSizeX, imageSizeY);
		image.setPickable(false);
		this.addChild(image);

		this.setDepthBufferDisabled(true);

		nextImage = pApplet.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() + "nextButton64.png");
		nextButton = new MTImageButton(nextImage, pApplet);

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent r) {
				switch (r.getID()) {
				case TapEvent.BUTTON_CLICKED:
					nextImage(); 
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

		previousImage = pApplet.loadImage(MT4jSettings.getInstance().getDefaultImagesPath() + "previousButton64.png");
		previousButton = new MTImageButton(previousImage, pApplet);

		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent r) {
				switch (r.getID()) {
				case TapEvent.BUTTON_CLICKED:
					previousImage(); 
					break;
				default:
					break;
				}
			}
		});
		previousButton.setNoStroke(true);
		previousButton.setSizeLocal(20, 20);
		previousButton.setPositionRelativeToParent(new Vector3D(5, -10));

		this.setVisible(true);
	}
	
	public void changeImage(int nb){
		this.removeAllChildren();
		image = this.images.get(nb);
		image.setStrokeColor(new MTColor(255,255,255,255));
		image.setPickable(false);
		this.addChild(image);
		if(currentImage < this.images.size() -1)
			this.addChild(nextButton);
		if(currentImage > 0)
			this.addChild(previousButton);
	}

	public void nextImage(){
		if(currentImage < this.images.size() -1)
			changeImage(++currentImage);
	}

	public void previousImage(){
		if(currentImage > 0)
			changeImage(--currentImage);
	}

	public void firstImage(){
		changeImage(0);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


}
