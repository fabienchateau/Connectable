package org.mt4jx.components.visibleComponents.widgets.pdf.rendering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class PDFRenderQueue {
	private ArrayList<PDFRenderJob> jobs = new ArrayList<PDFRenderJob>();
	
	public PDFRenderQueue(){
		
	}

	public synchronized void enque(PDFRenderJob job){
		this.jobs.add(job);
	}
	public synchronized PDFRenderJob[] getJobs(){
		PDFRenderJob[] jobArray = jobs.toArray(new PDFRenderJob[jobs.size()]);
		Arrays.sort(jobArray, new Comparator<PDFRenderJob>() {
			@Override
			public int compare(PDFRenderJob j1, PDFRenderJob j2) {
				if(j1.getPriority()!=j2.getPriority()){
					return j2.getPriority()-j1.getPriority();
				}else{
					return (int)(j1.getMillisCreated()-j2.getMillisCreated());
				}
			}
		});
		return jobArray;
	}
	public synchronized PDFRenderJob pop(){
		PDFRenderJob[] jobs = getJobs();
		if(jobs.length>0){
			this.jobs.remove(jobs[0]);
			return jobs[0];
		}else{
			return null;
		}
	}
	public void dump(){
		PDFRenderJob[] jobArray = this.getJobs();
		System.out.println("Queue size: " + this.jobs.size());
		for (int i = 0; i < jobArray.length; i++) {
			System.out.println(i + ": " + jobArray[i] + "-" + jobArray[i].getMillisCreated());
		}
	}
}
