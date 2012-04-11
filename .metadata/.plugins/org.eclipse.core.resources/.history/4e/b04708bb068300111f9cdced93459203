/**
 * This material was prepared as an account of work sponsored by an agency of the United States Government.<br>
 * Neither the United States Government nor the United States Department of Energy, nor any of their employees,<br> 
 * nor any of their contractors, subcontractors or their employees, makes any warranty, express or implied, or<br>
 * assumes any legal liability or responsibility for the accuracy, completeness, or usefulness or any information,<br> 
 * apparatus, product, or process disclosed, or represents that its use would not infringe privately owned rights.
 */
package gov.pnnl.components.visibleComponents.shapes;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.components.bounds.BoundsZPlaneRectangle;
import org.mt4j.components.bounds.IBoundingShape;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.ToolsGeometry;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;

/**
 * This class can be used to display an arched rectangle shape<br>
 * Based on code from {@link MTRoundRectangle}
 * 
 * @author Ryan LaMothe
 * 
 */
public class MTArchedRectangle extends MTPolygon {

  /**
   * Instantiates a new mT arched rectangle
   * 
   * @param pApplet
   *          The pApplet
   * @param x
   *          The X coordinate of the rectangle
   * @param y
   *          The Y coordinate of the rectangle
   * @param z
   *          The Z coordinate of the rectangle
   * @param height
   *          The height of the rectangle
   * @param topArcWidth
   *          The top arc width
   * @param topArcHeight
   *          The top arc height
   * @param bottomArcWidth
   *          The bottom arc width
   * @param bottomArcHeight
   *          The bottom arc height
   * @param createTexCoords
   *          Create texture coordinates
   * @param arcSegments
   *          The number of segments to use to create the arcs
   */
  public MTArchedRectangle(final PApplet pApplet, final float x, final float y, final float z,
      final float height,
      final float topArcWidth, final float topArcHeight,
      final float bottomArcWidth, final float bottomArcHeight,
      final boolean createTexCoords,
      final int arcSegments) {

    super(pApplet, new Vertex[] {});

    // Arc Width may not be greater than the rectangles width
    // and Arc height may not be greater than rectangles height!
    this.setVertices(this.calculateArchedRectangleVertices(x, y, z, height, topArcWidth, topArcHeight, bottomArcWidth, bottomArcHeight, createTexCoords, arcSegments));

    this.setBoundsBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
  }

  @Override
  protected IBoundingShape computeDefaultBounds() {
    return new BoundsZPlaneRectangle(this);
  }

  /**
   * Calculates the arched rectangle vertices
   * 
   * @param x
   * @param y
   * @param z
   * @param height
   * @param topArcWidth
   * @param topArcHeight
   * @param bottomArcWidth
   * @param bottomArcHeight
   * @param createTexCoords
   * @param arcSegments
   * @return vertices
   */
  private Vertex[] calculateArchedRectangleVertices(final float x, final float y, final float z,
      final float height,
      final float topArcWidth, final float topArcHeight,
      final float bottomArcWidth, final float bottomArcHeight,
      final boolean createTexCoords,
      final int arcSegments) {

    final MTColor currentFillColor = this.getFillColor();

    final Vertex upperLeftPoint = new Vertex(x, y + height - topArcHeight, 0);

    final Vertex upperRightPoint = new Vertex(x + topArcWidth, y + height - topArcHeight, 0);

    // This is done to help center the bottom arc
    final float bottomArcAdjustment = (topArcWidth - bottomArcWidth) / 2;

    final Vertex lowerRightPoint = new Vertex(x + bottomArcAdjustment + bottomArcWidth, y + height - bottomArcHeight, 0);

    final Vertex lowerLeftPoint = new Vertex(x + bottomArcAdjustment, y + height - bottomArcHeight, 0);

    // Calculates the arcs
    final List<Vertex> upperArc = ToolsGeometry.arcTo(upperRightPoint.x, upperRightPoint.y, topArcWidth, -topArcHeight, 0, false, true, upperLeftPoint.x, upperLeftPoint.y, arcSegments);

    final List<Vertex> lowerArc = ToolsGeometry.arcTo(lowerLeftPoint.x, lowerLeftPoint.y, bottomArcWidth / 1.5f, bottomArcHeight, 0, false, true, lowerRightPoint.x, lowerRightPoint.y, arcSegments);

    // Add vertices to list
    final ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    vertices.addAll(upperArc);
    vertices.addAll(lowerArc);

    final Vertex[] newVertices = vertices.toArray(new Vertex[vertices.size()]);

    // Set texture coordinates
    for (final Vertex vertex : newVertices) {
      if (createTexCoords) {
        // FIXME Which width to use?
        vertex.setTexCoordU((vertex.x - x) / topArcWidth);
        vertex.setTexCoordV((vertex.y - y) / height);
      }

      vertex.setRGBA(currentFillColor.getR(), currentFillColor.getG(), currentFillColor.getB(), currentFillColor.getAlpha());
    }

    return newVertices;
  }
}