package org.mt4jx.components.visibleComponents.widgets.pdf;

import java.io.File;

public class PrefetchThread extends Thread {
	private boolean locked = false;
	private Class lock = this.getClass();
	private boolean stopFlag = false;
	private long sleepMillis = 1000;
	private File pdf;
	private int[] pageNumbers;

	private CachedPDFPageLoader loader;
	public PrefetchThread(CachedPDFPageLoader loader, File pdf, int[] pageNumbers){
		this.loader = loader;
		this.pdf = pdf;
		this.pageNumbers = pageNumbers;
	}
	
	public boolean isLocked() {
		synchronized (lock) {
			return locked;
		}
	}
	public synchronized void setLocked(boolean locked) {
		synchronized (lock) {
			this.locked = locked;
		}
	}
	@Override
	public void run() {
		try {
			Thread.sleep(sleepMillis);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		
		while(!stopFlag&&isLocked()){
			try {
				Thread.sleep(sleepMillis);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		if(stopFlag)return;
		
		for (int i = 0; i < pageNumbers.length; i++) {
			while(!stopFlag&&isLocked()){
				try {
					Thread.sleep(sleepMillis);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
			if(stopFlag)return;
			loader.get(pdf, pageNumbers[i]);
		}
	}
	public void terminate(){
		this.stopFlag = true;
	}
	
}
