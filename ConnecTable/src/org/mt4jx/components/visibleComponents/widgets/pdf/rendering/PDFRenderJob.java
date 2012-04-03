package org.mt4jx.components.visibleComponents.widgets.pdf.rendering;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.mt4jx.components.visibleComponents.widgets.pdf.PDFRenderer;
import org.mt4jx.components.visibleComponents.widgets.pdf.RenderedPDFPage;

public class PDFRenderJob {
	
	private File pdf;
	private int width=-1;
	private int height=-1;
	private double scale = -1.0;
	private int pageNumber;
	private int priority;
	private static final int PRIORITY_MAX = Integer.MAX_VALUE;
	private static final int PRIORITY_MIN = 0;
	private long millisCreated;
	private ArrayList<PDFRenderJobListener> listeners = new ArrayList<PDFRenderJobListener>();
	
	public PDFRenderJob(File pdf, int pageNumber, int width, int height, int priority){
		this.pdf = pdf;
		this.pageNumber = pageNumber;
		this.width = width;
		this.height = height;
		this.priority = priority;

		this.millisCreated = System.currentTimeMillis();
	}
	public PDFRenderJob(File pdf, int pageNumber, double scale, int priority){
		this.pdf = pdf;
		this.pageNumber = pageNumber;
		this.scale = scale;
		this.priority = priority;
		
		this.millisCreated = System.currentTimeMillis();
	}
	
	public void execute(){
		try {
			RenderedPDFPage result;
			if(this.isDefinedByScale()){
				System.out.println("render " + this.pdf.getName() + " p." + this.pageNumber);
				result = PDFRenderer.render(this.pdf, this.scale, this.pageNumber);
			}else{
				System.out.println("render, dimension");
				result = PDFRenderer.render(this.pdf, this.width, this.height, this.pageNumber);
			}
			for (int i = 0; i < this.listeners.size(); i++) {
				listeners.get(i).jobDone(result);
			}
		} catch (IOException e) {
			for (int i = 0; i < this.listeners.size(); i++) {
				listeners.get(i).jobFailed(this);
			}
		}
	}
	
	public void addListener(PDFRenderJobListener listener){
		this.listeners.add(listener);
	}
	public void removeListener(PDFRenderJobListener listener){
		this.listeners.remove(listener);
	}
	public File getPdf() {
		return pdf;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public int getPriority() {
		return priority;
	}
	public static int getPriorityMax() {
		return PRIORITY_MAX;
	}
	public static int getPriorityMin() {
		return PRIORITY_MIN;
	}
	public ArrayList<PDFRenderJobListener> getListeners() {
		return listeners;
	}
	public boolean isDefinedByScale(){
		return this.scale !=-1.0;
	}
	public boolean isDefinedByDimension(){
		return this.width !=-1.0 && this.height != -1.0;
	}
	public long getMillisCreated() {
		return millisCreated;
	}
	@Override
	public String toString() {
		return "JOB [" + this.pdf.getName() + ", prio=" + priority + ", created="+ new Timestamp(this.millisCreated).toLocaleString() + "]";
	}
}
