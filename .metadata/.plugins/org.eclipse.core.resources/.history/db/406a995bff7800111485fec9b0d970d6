package org.mt4jx.components.visibleComponents.widgets.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import processing.core.PApplet;
import processing.core.PImage;

public class CachedPDFPageLoader {
	
	private Hashtable<String, RenderedPDFPage> renderedPDFPageTable = new Hashtable<String, RenderedPDFPage>();
	private Hashtable<String, Long> lastAccessTable = new Hashtable<String, Long>();
	
	private File storageRoot = new File("./imgcache");
	private double scaleFactor;
	
	private boolean useDisk = true;
	int maxEntries = 5;
	
	public CachedPDFPageLoader(double scaleFactor, int maxEntries){
		this.scaleFactor = scaleFactor;
		this.maxEntries = maxEntries;
		storageRoot.mkdirs();
	}
	public void add(File pdf, int page, RenderedPDFPage image){
		String key = encodeKey(pdf, page);
		System.out.println("CACHE: ADD: " + key);
		this.renderedPDFPageTable.put(key, image);
		if(useDisk){
			String saveFilePath = getStoragePath(key);
			new File(saveFilePath).getParentFile().mkdirs();
			System.out.println("this.storageRoot.getAbsolutePath():" + this.storageRoot.getAbsolutePath());
			System.out.println("key:" + key);
			try {
				if(!new File(saveFilePath).exists()){
					System.out.println("TRY SAVE: " + toValidAbsolutePath(saveFilePath));
					try {
						this.savePNG(image.getPImage(), new File(toValidAbsolutePath(saveFilePath)));
					} catch (Exception e) {
						e.printStackTrace();
					}
//					image.getPImage().save(toValidAbsolutePath(saveFilePath));
					System.out.println("SAVED: " + toValidAbsolutePath(saveFilePath));
				}else{
//					System.err.println("ALREADY EXISTS: " +toValidAbsolutePath(saveFilePath));
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		lastAccessTable.put(key, System.currentTimeMillis());
		cleanup();
	}
	private void remove(String key){
		System.out.println("CACHE: REMOVE: " + key);
		this.lastAccessTable.remove(key);
		this.renderedPDFPageTable.remove(key);
	}
	/**
	 * @return recently used page or null if cache is empty
	 */
	public RenderedPDFPage getRecentlyUsed(){
		String[] lruList = this.getKeysLRUOrder();
		if(lruList.length>0){
			return this.renderedPDFPageTable.get(lruList[0]);
		}else{
			return null;
		}
	}
	
	public RenderedPDFPage get(File pdf, int page){
		String key = encodeKey(pdf, page);
		lastAccessTable.put(key, System.currentTimeMillis());
		RenderedPDFPage img = renderedPDFPageTable.get(key);
		if(img ==null){
			// from file
			String storagePath = getStoragePath(key);
			if(new File(storagePath).exists() && new File(storagePath).isFile()){
				PApplet p = new PApplet();
				PImage image = p.loadImage(storagePath);
				RenderedPDFPage rpdf = new RenderedPDFPage(pdf, image, page, image.width*image.height);
				System.out.println("CACHE: LOAD (DISK)" + key);
				this.add(pdf, page, rpdf);
				return rpdf;
			}else{
				// render
				try {
					System.out.println("CACHE: RENDER" + key);
					img = PDFRenderer.render(pdf, scaleFactor, page);
					if(useDisk){
						String saveFilePath = this.storageRoot.getAbsolutePath() + key;
						new File(saveFilePath).getParentFile().mkdirs();
						System.out.println("CACHE: STORE: " + this.storageRoot.getAbsolutePath());
						System.out.println("key:" + key);
						this.add(pdf, page, img);
						try {
							if(!new File(saveFilePath).exists()){
								System.out.println("TRY SAVE: " + toValidAbsolutePath(saveFilePath));
								try {
									this.savePNG(img.getPImage(), new File(toValidAbsolutePath(saveFilePath)));
								} catch (Exception e) {
									e.printStackTrace();
								}
//								img.getPImage().save(toValidAbsolutePath(saveFilePath));
								System.out.println("SAVED: " + toValidAbsolutePath(saveFilePath));
							}else{
//								System.err.println("ALREADY EXISTS: " +toValidAbsolutePath(saveFilePath));
							}
						} catch (Exception e) {
							// best effort, maybe in use
						}
					}
					System.out.println("CACHE: ADDED PAGE " + key);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("CACHE: GET (RAM): " + key);
		}
		return renderedPDFPageTable.get(key);
	}
	private String encodeKey(File pdf, int page){
		return PathUtil.toRelativePathString(storageRoot, new File(storageRoot.getAbsolutePath() + "/" + pdf.getName() + "/" + toValidAbsolutePath(pdf.getPath()).hashCode() + "" + pdf.lastModified() + "/" + page + ".png"));
	}
	private String getStoragePath(String key){
		return this.storageRoot.getAbsolutePath() + key;
	}
	private synchronized void cleanup(){
		int removeCnt = this.renderedPDFPageTable.size() - maxEntries;
		if(removeCnt>0){
			String[] lru = getKeysLRUOrder();
			System.out.println("removeCnt:" + removeCnt);
			System.out.println("maxEntries:" + maxEntries);
			System.out.println("size:" + this.renderedPDFPageTable.size());
			for (int i = 0; i < removeCnt; i++) {
				this.remove(lru[i]);
			}
		}
	}
	private String[] getKeysLRUOrder(){
		String[] keys = this.lastAccessTable.keySet().toArray(new String[this.lastAccessTable.size()]);
		Arrays.sort(keys, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				// TODO Auto-generated method stub
				long l1 = lastAccessTable.get(s1);
				long l2 = lastAccessTable.get(s2);
				if(l1>l2){
					return 1;
				}else if(l1<l2){
					return -1;
				}else{
					return 0;
				}
			}
		});
		return keys;
	}
	public synchronized void clear(){
		this.renderedPDFPageTable.clear();
		this.lastAccessTable.clear();
	}
	private String toValidAbsolutePath(String p){
			String path = p;
			path = path.replace("/", File.separator);
			path = path.replace("\\", File.separator);
			path = path.replace(File.separator+".", "");
			System.out.println("FIXED PATH:" + path);
			return path;

	}
	private void savePNG(PImage pi, File target) throws IOException{
		BufferedImage bi = (BufferedImage)pi.getImage(); // retrieve image
	    ImageIO.write(bi, "png", target);
	}
////	TODO:Test
//	private String[] getKeysDistanceOrder(){
//		String[] keys = this.lastAccessTable.keySet().toArray(new String[this.lastAccessTable.size()]);
//		Arrays.sort(keys, new Comparator<String>() {
//			@Override
//			public int compare(String s1, String s2) {
//				// TODO Auto-generated method stub
//				long l1 = lastAccessTable.get(s1);
//				long l2 = lastAccessTable.get(s2);
//				if(l1<l2){
//					return 1;
//				}else if(l1>l2){
//					return -1;
//				}else{
//					return 0;
//				}
//			}
//		});
//		for (int i = 0; i < keys.length; i++) {
//			System.out.println(i + ": " + keys[i]);
//		}
//		return keys;
//	}
}
