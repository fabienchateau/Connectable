package org.mt4jx.components.visibleComponents.widgets.pdf.rendering;

public class JobExecutionThread extends Thread {
	private PDFRenderQueue queue;
	
	public JobExecutionThread(PDFRenderQueue queue){
		this.queue = queue;
	}
	@Override
	public void run() {
		while(true){
			PDFRenderJob job = queue.pop();
			if(job==null){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				job.execute();
			}
		}
	}
}
