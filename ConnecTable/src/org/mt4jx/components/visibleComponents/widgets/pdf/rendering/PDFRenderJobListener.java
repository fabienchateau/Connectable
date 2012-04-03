package org.mt4jx.components.visibleComponents.widgets.pdf.rendering;

import org.mt4jx.components.visibleComponents.widgets.pdf.RenderedPDFPage;

public interface PDFRenderJobListener {
	public void jobDone(RenderedPDFPage pdf);
	public boolean stillRequired(PDFRenderJob job);
	public void jobFailed(PDFRenderJob job);
}
