package org.mt4jx.components.visibleComponents.shapes;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import org.mt4j.components.css.style.CSSStyle;
import org.mt4j.components.visibleComponents.shapes.MTCSSStylableShape;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GL10;
import org.mt4j.util.opengl.GL11;
import org.mt4j.util.opengl.GL11Plus;
import processing.core.PApplet;
import processing.core.PGraphics;

import org.mt4j.components.bounds.BoundsArbitraryPlanarPolygon;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.css.style.CSSStyle;
import org.mt4j.components.visibleComponents.StyleInfo;
import org.mt4j.util.PlatformUtil;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Ray;
import org.mt4j.util.math.Tools3D;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4j.util.opengl.GL10;
import org.mt4j.util.opengl.GL11;
import org.mt4j.util.opengl.GL11Plus;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Represents a section of an annulus.
 * 
 * @author Randy Scarberry
 */
public class MTAnnularSegment extends MTCSSStylableShape {
	
	/** The p context. */
	private PApplet pContext;
	

    // Trapezoidal polygons used to draw the segment.
    private MTPolygon[] polygons;
    // Outer lines so that the outline can be drawn.
    private MTLine[] outlines;
    // The center of the ring.
    private Vector3D centerPoint;
    // Inner and outer radius.
    private float innerRadius, outerRadius;
    // Start and end angles in degrees.
    private float startAngle, endAngle;
    
    /**
     * Constructor.
     * 
     * @param pApplet       - the parent PApplet
     * @param centerX       - the center x coordinate of the annular ring, that is the 
     *                        point in the center of
     *                        the annulus if it were complete.
     * @param centerY       - the center y coordinate of the ring.
     * @param innerRadius   - the inner radius of the annular band.
     * @param outerRadius   - the outer radius.
     * @param startAngle    - the starting angle in degrees.
     * @param endAngle      - the ending angle in degrees.
     * @param segments      - the number of line segments to use in simulating arcs for 
     *                        every PI/2 radians of arc.  The number of segments actually
     *                        used will generally be different.
     */
    public MTAnnularSegment(PApplet pApplet, 
            float centerX, float centerY,
            float innerRadius, float outerRadius,
            float startAngle, float endAngle, int segments) {
        
        super(pApplet, computeVertices(
                new Vector3D(centerX, centerY, 0f),
                innerRadius, outerRadius,
                startAngle, endAngle, segments));
        
        this.centerPoint = new Vector3D(centerX, centerY, 0f);
        
        // Ensure they're not swapped by accident.
        this.innerRadius = Math.min(innerRadius, outerRadius);
        this.outerRadius = Math.max(innerRadius, outerRadius);
        
        // Ensure that these angles are in the range [0 - 360].
        this.startAngle = fixAngle(startAngle);
        this.endAngle = fixAngle(endAngle);      
        
        // Get the vertices in order to generate polygons and lines.
        Vertex[] vertices = this.getGeometryInfo().getVertices();
        
        // The segments argument is the number of segments per 90 degrees.
        // The actual number is probably different.
        segments = (vertices.length - 2)/2;
        
        // Generate a trapezoid for each segment.
        polygons = new MTPolygon[segments];
        for (int i=0; i<segments; i++) {
            polygons[i] = new MTPolygon(pApplet, new Vertex[] {
               vertices[i], vertices[i + 1],
               vertices[vertices.length - i - 2], vertices[vertices.length - i - 1]
            });
            polygons[i].setPickable(false);
            polygons[i].setNoStroke(true);
            polygons[i].setFillColor(super.getFillColor());
            this.addChild(polygons[i]);
        }
        
        // Is it a complete ring?
        boolean fullCircle = this.startAngle == this.endAngle;
        List<MTLine> lines = new ArrayList<MTLine> (fullCircle ? vertices.length : vertices.length - 2);
        
        // The outer lines.
        for (int i=0; i<segments; i++) {
            Vertex start = vertices[i];
            Vertex end = vertices[i+1];
            MTLine line = new MTLine(pApplet, start, end);
            line.setStrokeColor(super.getStrokeColor());
            line.setStrokeWeight(super.getStrokeWeight());
            line.setPickable(false);
            this.addChild(line);
            lines.add(line);
        }
        
        // If not a complete ring, generate the line on one end.
        if (!fullCircle) {
            Vertex start = vertices[segments];
            Vertex end = vertices[segments+1];
            MTLine line = new MTLine(pApplet, start, end);
            line.setStrokeColor(super.getStrokeColor());
            line.setStrokeWeight(super.getStrokeWeight());
            line.setPickable(false);
            this.addChild(line);
            lines.add(line);
        }
        
        // Now the lines on the inner ring.
        int lim = 2*segments + 1;
        for (int i=segments+1; i<lim; i++) {
            Vertex start = vertices[i];
            Vertex end = vertices[i+1];
            MTLine line = new MTLine(pApplet, start, end);
            line.setStrokeColor(super.getStrokeColor());
            line.setStrokeWeight(super.getStrokeWeight());
            line.setPickable(false);
            this.addChild(line);
            lines.add(line);
        }
        
        // The final end line if not a complete ring.
        if (!fullCircle) {
            Vertex start = vertices[vertices.length - 1];
            Vertex end = vertices[0];
            MTLine line = new MTLine(pApplet, start, end);
            line.setStrokeColor(super.getStrokeColor());
            line.setStrokeWeight(super.getStrokeWeight());
            line.setPickable(false);
            this.addChild(line);
            lines.add(line);
        }

        outlines = lines.toArray(new MTLine[lines.size()]);
    }
    
    /**
     * Get the center point of the annular ring.
     * 
     * @return a copy of the center vector.
     */
    public Vector3D getCenter() {
        return new Vector3D(this.centerPoint);
    }
    
    /**
     * Get the inner radius.
     * 
     * @return
     */
    public float getInnerRadius() {
        return this.innerRadius;
    }
    
    /**
     * Get the outer radius.
     * 
     * @return
     */
    public float getOuterRadius() {
        return this.outerRadius;
    }
    
    /**
     * Get the start angle in degrees.
     * 
     * @return
     */
    public float getStartAngle() {
        return this.startAngle;
    }
    
    /**
     * Get the end angle in degrees.
     * 
     * @return
     */
    public float getEndAngle() {
        return this.endAngle;
    }
    
    @Override
    public MTColor getFillColor() {
        return this.polygons[0].getFillColor();
    }
    
    @Override
    public void setFillColor(MTColor color) {
        for (MTPolygon p: this.polygons) {
            p.setFillColor(color);
        }
    }
    
    @Override
    public MTColor getStrokeColor() {
        return this.outlines[0].getStrokeColor();
    }
    
    @Override
    public void setStrokeColor(MTColor color) {
        for (MTLine line : outlines) {
            line.setStrokeColor(color);
        }
    }
    
    @Override
    public boolean isNoFill() {
        return polygons[0].isNoFill();
    }
    
    @Override
    public void setNoFill(boolean b) {
        for (MTPolygon p : polygons) {
            p.setNoFill(b);
        }
    }
    
    @Override
    public boolean isNoStroke() {
        return outlines[0].isNoStroke();
    }
    
    @Override
    public void setNoStroke(boolean b) {
        for (MTLine line : outlines) {
            line.setVisible(!b);
        }
    }
    
    /**
     * Returns an angle equivalent to the argument in the
     * range [0 - 360].
     * 
     * @param angle
     * @return
     */
    public static float fixAngle(float angle) {
        boolean negative = angle < 0;
        angle = Math.abs(angle);
        if (angle > 360f) {
            float f = angle/360f;
            angle = (f - (float) Math.floor(f)) * 360f; 
        }
        if (negative) {
            angle = 360f - angle;
        }
        return angle;
    }
    
    /**
     * Returns the degrees of arc.
     * 
     * @return
     */
    public float getArcDegrees() {
        return computeArcDegrees(this.startAngle, this.endAngle);
    }
    
    // Computes the arc degrees for the given start and end angles.
    public static float computeArcDegrees(float startDegrees, float endDegrees) {
        startDegrees = fixAngle(startDegrees);
        endDegrees = fixAngle(endDegrees);
        float arcDegrees = endDegrees - startDegrees;
        if (arcDegrees <= 0f) {
            arcDegrees += 360f;
        }
        return arcDegrees;
    }
    
    /**
     * Static method for computing vertices of an annular segment.
     * 
     * @param center
     * @param innerRadius
     * @param outerRadius
     * @param startAngle
     * @param endAngle
     * @param segments
     * @return
     */
    public static Vertex[] computeVertices(
            Vector3D center,
            float innerRadius, float outerRadius,
            float startAngle, float endAngle, int segments) {
        
        if (segments <= 0) {
            throw new IllegalArgumentException("segments must be positive: " + segments);
        }
        
        List<Vertex> vertices = new ArrayList<Vertex> ();
        
        final float rmin = Math.min(innerRadius, outerRadius);
        final float rmax = Math.max(innerRadius, outerRadius);
        
        startAngle = fixAngle(startAngle);
        endAngle = fixAngle(endAngle);
        
        final float startRadians = (float) Math.toRadians(startAngle);
        final float endRadians = (float) Math.toRadians(endAngle);
        
        float arc = (float) Math.toRadians(computeArcDegrees(startAngle, endAngle));
        
        segments = (int) Math.round(segments * arc/(Math.PI/2.0));
        if (segments == 0) {
            segments = 1;
        }
        
        final float radInc = arc/segments;

        float currentRadians = endRadians;
    
        float minX = Float.MAX_VALUE;
        float maxX = 0f;
        float minY = Float.MAX_VALUE;
        float maxY = 0f;
        
        for (int i=0; i<=segments; i++, currentRadians -= radInc) {
            float x = - rmax * (float) Math.cos(currentRadians) + center.x;
            float y = - rmax * (float) Math.sin(currentRadians) + center.y;
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
            vertices.add(new Vertex(x, y, center.z));
        }
        
        currentRadians = startRadians;
        
        for (int i=0; i<=segments; i++, currentRadians += radInc) {
            float x = - rmin * (float) Math.cos(currentRadians) + center.x;
            float y = - rmin * (float) Math.sin(currentRadians) + center.y;
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
            vertices.add(new Vertex(x, y, center.z));
        }

        Vertex[] varray = vertices.toArray(new Vertex[vertices.size()]);
        
        float xRange = maxX - minX;
        float yRange = maxY - minY;
        
        // Set the texture X and Y
        for (Vertex v : varray) {
            float texU = 0f, texV = 0f;
            if (xRange > 0) {
                texU = (v.x - minX)/xRange;
            }
            if (yRange > 0) {
                texV = (v.y - minY)/yRange;
            }
            v.setTexCoordU(texU);
            v.setTexCoordV(texV);
        }
        
        return varray;
    }

    @Override
    protected void applyStyleSheetCustom(CSSStyle virtualStyleSheet) {
    }

    @Override
    public void drawPureGl(GL10 gl) {
    }

    @Override
    public Vector3D getGeometryIntersectionLocal(Ray ray) {
        final int n = this.polygons.length;
        for (int i=0; i<n; i++) {
            Vector3D intersection = this.polygons[i].getGeometryIntersectionLocal(ray);
            if (intersection != null) {
                return intersection;
            }
        }
        return null;
    }

    @Override
    public boolean isGeometryContainsPointLocal(Vector3D testPoint) {
        final int n = this.polygons.length;
        for (int i=0; i<n; i++) {
            if (this.polygons[i].isGeometryContainsPointLocal(testPoint)) return true;
        }
        return false;
    }

    @Override
    public Vector3D getCenterPointLocal() {
        final int n = this.polygons.length;
        int mid = n/2;
        
        MTPolygon middle = this.polygons[mid];
        Vector3D center = middle.getCenterPointLocal();
        
        if (n%2 == 0) {
            Vector3D center2 = this.polygons[mid-1].getCenterPointLocal();
            center.x = (center.x + center2.x)/2f;
            center.y = (center.y + center2.y)/2f;
            center.z = (center.z + center2.z)/2f;
        }
        
        return center;
    }

    @Override
    public void drawComponent(PGraphics g) {
        // Noop
    }
    
    @Override
    protected void destroyComponent() {
    }

    public boolean setWidthXYRelativeToParent(float width){
        if (width > 0){
            Vector3D centerPoint = this.getCenterPointRelativeToParent(); 
            float factor = (1f/this.getWidthXYRelativeToParent()) * width;
            this.scale(factor, factor, 1, centerPoint);
            return true;
        }else
            return false;
    }
}
