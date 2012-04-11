package org.mt4jx.components.visibleComponents.widgets.examples;

import java.util.Random;

import org.mt4j.MTApplication;
import org.mt4j.util.font.IFont;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4jx.components.visibleComponents.shapes.MTAnnularSegment;
import org.mt4jx.components.visibleComponents.widgets.MTAnnularTextArea;

public class AnnularTextExampleScene extends AbstractScene {

    public AnnularTextExampleScene(MTApplication mtApplication, String name) {
        
        super(mtApplication, name);
        
        this.setClearColor(MTColor.randomColor());
        
        MTColor[] annularFills = new MTColor[] { MTColor.randomColor(), MTColor.randomColor() };
        MTColor[] annularFontColors = new MTColor[] { MTColor.randomColor(), MTColor.randomColor() };
        
        float radius = 50;
        float annularThickness = 70;
        float gapThickness = 25;
        
        String theMessage = "This is an annular text component.  How do you like it?";
        
        long seed = System.currentTimeMillis()%10000L;
        
        System.err.printf("........................... SEED = %d\n", seed);
        
        Random r = new Random(seed);
        
        float centerX = mtApplication.width/2f;
        float centerY = mtApplication.height/2f;
        
        int ringCount = 0;
        while (radius + annularThickness <= centerY) {
            
            MTColor fillColor = annularFills[ringCount%2];
            MTColor strokeColor = annularFills[(ringCount + 1)%2];
            MTColor fontColor = annularFontColors[ringCount%2];
            
            float startAngle = r.nextFloat()*360f;
            float endAngle = startAngle + 50f + r.nextFloat()*310f;
            
            float arcDegrees = MTAnnularSegment.computeArcDegrees(startAngle, endAngle);
            
            float outerRadius = radius + annularThickness;
        
            IFont font = MTAnnularTextArea.findAppropriateFont(mtApplication, radius, 
                    outerRadius, arcDegrees, "Arial", fontColor, true, 
                    new String[] { theMessage });
            
            MTAnnularTextArea ta = new MTAnnularTextArea(mtApplication, centerX, centerY,
                    radius, outerRadius, startAngle, endAngle, 20);
            ta.setFillColor(fillColor);
            ta.setStrokeColor(strokeColor);
            ta.setFont(font);
            ta.setText(theMessage);
            
            this.getCanvas().addChild(ta);
        
            radius = outerRadius + gapThickness;
        }
    }

}
