package org.mt4jx.components.visibleComponents.widgets;

import java.util.HashSet;
import java.util.Set;

import org.mt4j.components.TransformSpace;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.font.IFontCharacter;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.shapes.MTAnnularSegment;

import processing.core.PApplet;

public class MTAnnularTextArea extends MTAnnularSegment {

    private IFont font;
    private String text;
    private MTTextArea[] letters;
    
    public MTAnnularTextArea(PApplet pApplet, 
            float centerX, float centerY,
            float innerRadius, float outerRadius, float startAngle,
            float endAngle, int segments) {

        super(pApplet, centerX, centerY, innerRadius, outerRadius, startAngle,
                endAngle, segments);
    
        setFont(FontManager.getInstance().getDefaultFont(pApplet));
    }
    
    public static IFont findAppropriateFont(PApplet pApplet, 
            float innerRadius, float outerRadius, float angle,
            String fontName, MTColor fontColor, boolean antiAliased,
            String[] strings) {
        
        FontManager fm = FontManager.getInstance();
        
        final float annularThickness = Math.abs(outerRadius - innerRadius);
        final float centerLineRadius = (outerRadius + innerRadius)/2f;
        final float desiredFontHeight = 0.6f * annularThickness;
        final float padding = (annularThickness - desiredFontHeight)/2f;
        
        final float desiredFontLength = centerLineRadius * (float) Math.toRadians(fixAngle(angle)) - 2f*padding;
            
        int fontSize = 20;
        IFont font = fm.createFont(pApplet, fontName, 20, fontColor, antiAliased);
        
        float maxWidth = 0f;
        String longestString = null;
        for (String s : strings) {
            s = cleanupString(s);
            float w = stringWidth(font, s);
            if (w > maxWidth) {
                maxWidth = w;
                longestString = s;
            }
        }
        
        Set<Integer> tried = new HashSet<Integer> ();
        tried.add(fontSize);
        
        float width = maxWidth;
        float height = font.getFontAbsoluteHeight();
        
        int tooLargeFontSize = Integer.MAX_VALUE;
        
        boolean ok = false;
        
        while(!ok) {
            float widthRatio = width/desiredFontLength;
            float heightRatio = height/desiredFontHeight;
            boolean tooBig = false;
            if (widthRatio > 1f || heightRatio > 1f) {
                tooBig = true;
                if (fontSize < tooLargeFontSize) {
                    tooLargeFontSize = fontSize;
                }
                fontSize *= 1f/Math.max(widthRatio, heightRatio);
            } else {
                float maxRatio = Math.max(widthRatio, heightRatio);
                float minRatio = Math.min(widthRatio, heightRatio);
                // If the max ratio is almost right, making the font 
                // bigger will probably making it to large again.
                // And if they're both > 0.9f, that's pretty good.
                if (maxRatio > 0.95f || minRatio > 0.9f) {
                    ok = true;
                } else {
                    int newFontSize = Math.round(((float)fontSize)/minRatio);
                    if (newFontSize < tooLargeFontSize) {
                        fontSize = newFontSize;
                    } else {
                        newFontSize = (tooLargeFontSize + fontSize)/2;
                        if (newFontSize > fontSize) {
                            fontSize = newFontSize;
                        } else {
                            ok = true;
                        }
                    }
                }
            }
            if (!ok) {
                while(tried.contains(fontSize)) {
                    if (tooBig) {
                        fontSize--;
                    } else {
                        fontSize++;
                    }
                }
                
                if (fontSize > 0) {
                    
                    IFont newFont = null;
                    int tries = 0;
                    while(newFont == null && tries < 3) {
                        tries++;
                        newFont = fm.createFont(pApplet, fontName, fontSize, fontColor, antiAliased);
                        if (newFont == null) {
                            tried.add(fontSize);
                            while(tried.contains(fontSize)) {
                                if (tooBig) {
                                    fontSize--;
                                } else {
                                    fontSize++;
                                }
                            }
                        }
                        if (fontSize == 0) {
                            break;
                        }
                    }
                    
                    if (newFont == null) {
                        return font;
                    } else {
                        font = newFont;
                    }
                    
                    width = stringWidth(font, longestString);
                    height = font.getFontAbsoluteHeight();
                    tried.add(fontSize);
                
                } else {
                    break;
                }
            }
        }
        
        return font;
    }
    
    public IFont getFont() {
        return font;
    }

    public void setFont(IFont font) {
        if (font == null) throw new NullPointerException();
        this.font = font;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String s) {
        text = null;
        appendText(s);
        generateLetters();
    }

    private static String cleanupString(String s) {
        StringBuilder sb = new StringBuilder();
        final int len = s.length();
        for (int i=0; i<len; i++) {
            String unicode = s.substring(i, i+1);
            if (!unicode.equals("\n")) {
                sb.append(unicode);
            }
        }
        return sb.toString();
    }
    
    public void appendText(String s) {
        s = cleanupString(s);
        if (text != null) {
            text += s;
        } else {
            text = s;
        }
        generateLetters();
    }
    
    private void generateLetters() {
        if (this.letters != null) {
            for (MTTextArea ta : this.letters) {
                removeChild(ta);
            }
        }
        final int len = text != null ? text.length() : 0;
        this.letters = new MTTextArea[len];
        float[][] locsAndRots = computeLocationsAndRotations();
        
        PApplet pa = this.getRenderer();
        
        for (int i=0; i<len; i++) {
            MTTextArea ta = new MTTextArea(pa, font);
            ta.setFont(font);
            ta.setNoFill(true);
            ta.setNoStroke(true);
            ta.setPickable(false);
            ta.setText(this.text.substring(i, i+1));
            float[] f = locsAndRots[i];
            Vector3D pos = new Vector3D(f[0], f[1], 0f);
            ta.setPositionGlobal(pos);
            ta.rotateZ(pos, (float) Math.toDegrees(f[2]) - 90f, TransformSpace.GLOBAL);
            this.addChild(ta);
            this.letters[i] = ta;
        }
    }

    private float[][] computeLocationsAndRotations() {
  
        final int charCount = this.text != null ? this.text.length() : 0;
        final float[][] results = new float[charCount][3];
  
        final float[] charWidths = new float[charCount];
        float totalWidth = 0f;
        
        for (int i=0; i<charCount; i++) {
            IFontCharacter c = font.getFontCharacterByUnicode(this.text.substring(i, i+1));
            float w = c.getHorizontalDist();
            charWidths[i] = w;
            totalWidth += w;
        }
        
        final Vector3D center = this.getCenter();
  
        final float startRadians = (float) Math.toRadians(this.getStartAngle());  
        final float arcRadians = (float) Math.toRadians(this.getArcDegrees());  
        
        final float centerLineRadius = (this.getInnerRadius() + this.getOuterRadius())/2f;

        float textRadians = totalWidth > 0f ? totalWidth/centerLineRadius : 0f; 
        
        float angle = startRadians + arcRadians/2f - textRadians/2f;
        //float angle = startRadians + (arcRadians - textRadians)/2f - (float) (Math.PI/2.0);
  
        for (int i=0; i<charCount; i++) {
      
            float w = charWidths[i];
            float halfCharInc = w/(centerLineRadius*2f);
            
            angle += halfCharInc;
            float x = - centerLineRadius * (float) Math.cos(angle) + center.x;
            float y = - centerLineRadius * (float) Math.sin(angle) + center.y;
      
            results[i][0] = x;
            results[i][1] = y;
            results[i][2] = angle;
            
            angle += halfCharInc;
        }
  
        return results;
    }

    public static float stringWidth(IFont font, String s) {
        float maxLineWidth = 0f;
        float width = 0f;
        final int len = s.length();
        for (int i=0; i<len; i++) {
          IFontCharacter c = font.getFontCharacterByUnicode(s.substring(i, i+1));
          // Have to consider the possibility of multiple lines.
          if (!c.getUnicode().equals("\n")) {
            width += c.getHorizontalDist();
          } else {
            if (width > maxLineWidth) {
              maxLineWidth = width;
            }
            width = 0f;
          }
        }
        if (width > maxLineWidth) {
          maxLineWidth = width;
        }
        return maxLineWidth;
      }
}
