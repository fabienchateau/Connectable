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
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import processing.core.PImage;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * @author Uwe Laufs
 *
 */
public class PDFRenderer {
	public static RenderedPDFPage render(File pdf, double scaleFactor, int pageNumber) throws IOException {
        PDFFile pdffile = null;
		File file = pdf;
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		pdffile = new PDFFile(buf);

        PDFPage page = pdffile.getPage(pageNumber);
		int w = (int)(page.getBBox().getWidth());
		int h = (int)(page.getBBox().getHeight());
		System.out.println("dimension pdfFile: " + w + "/" + h);
		
		int c1,c2; // for clipping
		if(w>h){
			c1 = h;
			c2 = w;
		}else{
			c1 = w;
			c2 = h;
		}
        Rectangle clip = new Rectangle(0,0,(int)(c1*scaleFactor),(int) (c2*scaleFactor) );
        
        Image img = page.getImage(
        		(int)(w*scaleFactor),(int)(h*scaleFactor), //width & height
                null, // disable clipping
//              clip, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true  // block until drawing is done
                );
        RenderedPDFPage result = new RenderedPDFPage(pdf, new PImage(img), pageNumber, (int)(w*scaleFactor*h*scaleFactor) );
        System.out.println("Rendered dimension:" + result.getPImage().width + "/" + result.getPImage().height);
        System.gc(); 
        return result;
	}
	
	public static RenderedPDFPage render(File pdf, int width, int height, int pageNumber) throws IOException {
        PDFFile pdffile = null;
		File file = pdf;
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		pdffile = new PDFFile(buf);

        PDFPage page = pdffile.getPage(pageNumber);
		int w = (int)(page.getBBox().getWidth());
		int h = (int)(page.getBBox().getHeight());
		
//		int c1,c2; // for clipping
//		if(w>h){
//			c1 = (int)page.getBBox().getHeight();
//			c2 = (int)page.getBBox().getWidth();
//		}else{
//			c1 = (int)page.getBBox().getWidth();
//			c2 = (int)page.getBBox().getHeight();
//		}
//        Rectangle clip = new Rectangle(0,0,c1,c2);
        Image img = page.getImage(
        		width, height, //width & height
//                clip, // clip rect
                null, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true  // block until drawing is done
                );
        RenderedPDFPage result = new RenderedPDFPage(pdf, new PImage(img), pageNumber, w*h);
        System.gc(); 
        return result;
	}
	public static RenderedPDFPage loadImage(File pdf) throws IOException {
		return render(pdf, 1, 0);
	}
}
