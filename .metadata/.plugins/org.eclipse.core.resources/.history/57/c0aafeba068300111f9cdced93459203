/**
 * This material was prepared as an account of work sponsored by an agency of the United States Government.<br>
 * Neither the United States Government nor the United States Department of Energy, nor any of their employees,<br> 
 * nor any of their contractors, subcontractors or their employees, makes any warranty, express or implied, or<br>
 * assumes any legal liability or responsibility for the accuracy, completeness, or usefulness or any information,<br> 
 * apparatus, product, or process disclosed, or represents that its use would not infringe privately owned rights.
 */
package gov.pnnl.components.visibleComponents.widgets.radialMenu;

import gov.pnnl.components.visibleComponents.widgets.radialMenu.item.MTMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.mt4j.util.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.ToolsGeometry;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;

/**
 * A radial menu
 * <p>
 * This menu is intended to be a proof-of-concept of a full-featured radial menu using MT4j<br>
 * Many of the default values defined in this class are used to determine arched button calculations<br>
 * These default values are approximations and should be replaced by general equations in the future
 * <p>
 * <b>Note: The default radius is 200. Scale to the size needed using the scale factor.</b>
 * 
 * @author Ryan LaMothe
 * 
 */
public class MTRadialMenu extends MTEllipse {

  // Logger for this class
  private static final Logger LOG = Logger.getLogger(MTRadialMenu.class);

  // Public Constants
  public static final String NAME = "MTRadialMenu";

  // Default values
  private final float radius = 200f;

  private final float buttonXCoordAdj = 10f;

  private final float buttonTopWidthAdj = 47.5f;

  private final float buttonBottomWidthAdj = 47.5f;

  private final float topArcHeight = 80f;

  private final float topArcHeightAdj = 20f;

  // Fields
  private final PApplet pApplet;

  private final Vector3D center;

  private final IFont font;

  private float buttonHeight = 0;

  private float buttonTopWidth = 0;

  private float buttonBottomWidth = 0;

  private String closeButtonText = "Close";

  // Default MTColor is RED
  private MTColor centerButtonFillColor = new MTColor(255, 0, 0, MTColor.ALPHA_HIGH_TRANSPARENCY);

  private List<MTRadialPoly> menuItems = new CopyOnWriteArrayList<MTRadialPoly>();

  /**
   * Full constructor
   * 
   * @param pApplet
   *          The pApplet
   * @param centerPoint
   *          The center point of the circle
   * @param font
   *          The font to use for the menu items
   * @param scaleFactor
   *          The scale factor of the entire menu. Default radius is 200.
   * @param menuItems
   *          The list of menu items and sub-menu items to display in the radial menu
   */
  public MTRadialMenu(final PApplet pApplet, final Vector3D centerPoint, final IFont font, final float scaleFactor, final List<MTMenuItem> menuItems) {
    super(pApplet, centerPoint, 200, 200);

    this.pApplet = pApplet;

    this.setName(NAME);

    this.center = this.getCenterPointLocal();

    this.font = font;

    // Default MTColor is GREY
    this.setFillColor(new MTColor(128, 128, 128, MTColor.ALPHA_HALF_TRANSPARENCY));

    this.setNoStroke(true);

    // Starts out small and then zooms out to its actual size
    AnimationUtil.scaleIn(this);

    this.createCloseButton();

    this.createMainMenuItems(menuItems);

    // Allow the user to scale the menu to the desired size
    this.scaleGlobal(scaleFactor, scaleFactor, scaleFactor, centerPoint);
  }

  /**
   * Retrieve the text used for the Close button
   * <p>
   * <b>Note: The default text is "Close"</b>
   * 
   * @return the closeButtonText
   */
  public String getCloseButtonText() {
    return this.closeButtonText;
  }

  /**
   * Set the text used for the Close button
   * 
   * @param closeButtonText
   *          the closeButtonText to set
   */
  public void setCloseButtonText(final String closeButtonText) {
    this.closeButtonText = closeButtonText;
  }

  /**
   * Retrieve the MTColor used for the center button fillColor
   * 
   * @return the centerButtonFillColor
   */
  protected MTColor getCenterButtonFillColor() {
    return this.centerButtonFillColor;
  }

  /**
   * Set the MTColor used for the center button fillColor
   * 
   * @param centerButtonFillColor
   *          the centerButtonFillColor to set
   */
  protected void setCenterButtonFillColor(final MTColor centerButtonFillColor) {
    this.centerButtonFillColor = centerButtonFillColor;
  }

  /**
   * Retrieve the list of menu items
   * 
   * @return the menuItems
   */
  protected List<MTRadialPoly> getMenuItems() {
    return this.menuItems;
  }

  /**
   * Set the list of menu items
   * 
   * @param menuItems
   *          the menuItems to set
   */
  protected void setMenuItems(final List<MTRadialPoly> menuItems) {
    this.menuItems = menuItems;
  }

  /**
   * Add a menu item to an existing list of menu items
   * 
   * @param menuItem
   */
  protected void addMenuItem(final MTRadialPoly menuItem) {
    if (this.getMenuItems() != null) {
      this.getMenuItems().add(menuItem);
    }
  }

  /**
   * Removes all of the sub-menu items at or above a specified menu level
   * 
   * @param subMenuLevel
   */
  protected void removeAllMenuItems(final float subMenuLevel) {
    // Retrieve all menu items at or above the specified menu level
    for (final MTRadialPoly item : this.getMenuItems()) {
      if (item.getSubMenuLevel() >= subMenuLevel) {
        item.setFillColor(item.getUnSelectedColor());
        item.setSubMenuActive(false);
        item.removeAllMenuItems();

        // Default animation for removing menu items
        AnimationUtil.scaleOut(item, false);
      }
    }
  }

  /**
   * Removes all of the active sub-menu items at a specified menu level
   * 
   * @param subMenuLevel
   */
  protected void removeAllActiveMenuItems(final float subMenuLevel) {
    // Retrieve all menu items at the specified menu level
    for (final MTRadialPoly item : this.getMenuItems()) {
      if ((item.getSubMenuLevel() == subMenuLevel) && item.isSubMenuActive()) {
        item.setFillColor(item.getUnSelectedColor());
        item.setSubMenuActive(false);

        item.removeAllMenuItems();
      }
    }
  }

  /**
   * Creates the main menu items
   * 
   * @param menuItems
   *          The list of menu items to create
   */
  private void createMainMenuItems(final List<MTMenuItem> menuItems) {

    // Note: Math.PI can be divided by different numbers to produce different button configurations
    final float angularDiv = ((float) Math.PI) / 5;

    this.buttonHeight = this.radius / 2;
    final float innerRadius = this.radius + 60f;
    final float outerRadius = innerRadius + this.buttonHeight;
    final float outerCircumference = (float) (2 * Math.PI * outerRadius);
    final float innerCircumference = (float) (2 * Math.PI * innerRadius);
    final float halfOuterCircumference = (float) (Math.PI * outerRadius);
    final float halfInnerCircumference = (float) (Math.PI * innerRadius);
    this.buttonTopWidth = halfOuterCircumference / 7f;
    this.buttonBottomWidth = halfInnerCircumference / 7f;

    if (LOG.isTraceEnabled()) {
      LOG.trace("Number of menuItems: " + menuItems.size());
      LOG.trace("Circle Radius: " + this.radius);
      LOG.trace("Inner Radius: " + innerRadius);
      LOG.trace("Outer Radius: " + outerRadius);
      LOG.trace("Outer Circumference: " + outerCircumference);
      LOG.trace("Inner Circumference: " + innerCircumference);
      LOG.trace("Half Outer Circumference: " + halfOuterCircumference);
      LOG.trace("Half Inner Circumference: " + halfInnerCircumference);
      LOG.trace("AngularDiv: " + angularDiv);
      LOG.trace("Button Height: " + this.buttonHeight);
      LOG.trace("Button Top Width: " + this.buttonTopWidth);
      LOG.trace("Button Bottom Width: " + this.buttonBottomWidth);
      LOG.trace("Top Arc Height: " + this.topArcHeight);
    }

    for (int i = 0; i < menuItems.size(); i++) {

      final MTMenuItem mtMenuItem = menuItems.get(i);

      // Calculate the proper angle
      final float angle = angularDiv * (5 + i + ((6 - menuItems.size()) / 2f));

      /*
       * X coordinate: radius * cos(angle) where angle is in radians
       * Y coordinate: radius * sin(angle) where angle is in radians
       */
      final float xCoord = this.center.x + (innerRadius * (float) Math.cos(angle));
      final float yCoord = this.center.y + (innerRadius * (float) Math.sin(angle));

      MTRadialPoly mainMenuItem = null;

      // Main menu item
      final Vertex[] newVertices = this.calculateArchedRectangleVertices(this.center.x, this.center.y, this.center.z, 0,
           this.buttonTopWidth + (this.buttonTopWidthAdj * 0),
           this.topArcHeight + (this.topArcHeightAdj * 0),
           this.buttonBottomWidth + (this.buttonBottomWidth * .05f) + (this.buttonBottomWidthAdj * 0),
           20f + (this.buttonXCoordAdj * 0), true, 50);

      mainMenuItem = new MTRadialPoly(this.pApplet, newVertices, this.center, this.buttonHeight, this.radius, mtMenuItem.getMenuText(), this.font, null, null);

      // Add all inputEventListeners
      for (final IMTInputEventListener inputEventListener : mtMenuItem.getInputEventListeners()) {
        mainMenuItem.addInputListener(inputEventListener);
      }

      // Add all gestureEventListeners
      for (final Entry<Class<? extends IInputProcessor>, IGestureEventListener> entry : mtMenuItem.getGestureEventListeners().entrySet()) {
        mainMenuItem.addGestureListener(entry.getKey(), entry.getValue());
      }

      // Rotate the menu item
      final float degrees = ((float) Math.toDegrees(angle)) - 270f;
      mainMenuItem.setLocalMatrix(Matrix.getZRotationMatrix(this.center, degrees));

      // Sub-menu items
      if (mtMenuItem.getSubMenuItems() != null) {

        this.createSubMenuItems(mtMenuItem.getSubMenuItems(), mainMenuItem, 1);
      }

      // Position the main menu button
      mainMenuItem.setPositionRelativeToParent(new Vector3D(xCoord, yCoord, 0));

      // Add to the root object
      this.addChild(mainMenuItem);

      // Add to the global menu item list
      this.addMenuItem(mainMenuItem);
    }
  }

  /**
   * Creates the sub-menu items
   * 
   * @param subMenuItems
   *          The list of sub-menu items
   * @param menuItemPolygon
   *          The menu item to add the created sub-menu items
   * @param subMenuLevel
   *          The sub-menu level
   */
  protected void createSubMenuItems(final List<MTMenuItem> subMenuItems, final MTRadialPoly menuItemPolygon, final int subMenuLevel) {

    for (final MTMenuItem subMenuItem : subMenuItems) {
      final Vertex[] newVertices = this.calculateArchedRectangleVertices(this.center.x, this.center.y, this.center.z, 0,
          this.buttonTopWidth + (this.buttonTopWidthAdj * subMenuLevel),
          this.topArcHeight + (this.topArcHeightAdj * subMenuLevel),
          this.buttonBottomWidth + (this.buttonBottomWidth * .05f) + (this.buttonBottomWidthAdj * subMenuLevel),
          30f + (this.buttonXCoordAdj * subMenuLevel), true, 50);

      final MTRadialPoly smi = new MTRadialPoly(this.pApplet, newVertices, this.center, this.buttonHeight, this.radius, subMenuItem.getMenuText(), this.font, null, null);

      // Add all inputEventListeners
      for (final IMTInputEventListener inputEventListener : subMenuItem.getInputEventListeners()) {
        smi.addInputListener(inputEventListener);
      }

      // Add all gestureEventListeners
      for (final Entry<Class<? extends IInputProcessor>, IGestureEventListener> entry : subMenuItem.getGestureEventListeners().entrySet()) {
        smi.addGestureListener(entry.getKey(), entry.getValue());
      }

      if (subMenuItem.getSubMenuItems() != null) {
        this.createSubMenuItems(subMenuItem.getSubMenuItems(), smi, subMenuLevel + 1);
      }

      // Add to the parent menu item
      menuItemPolygon.addSubMenuItem(smi);

      // Add to the global menu item list
      this.addMenuItem(smi);
    }
  }

  /**
   * Creates a Close button and adds it to the root menu
   */
  private void createCloseButton() {

    // Create a new Ellipse to be used for closing this menu
    final MTEllipse closeButton = new MTEllipse(this.pApplet, this.center, this.radius * .25f, this.radius * .25f);

    // Configure this menu item
    closeButton.unregisterAllInputProcessors();
    closeButton.setName(this.getCloseButtonText());

    // Color is RED
    closeButton.setFillColor(this.getCenterButtonFillColor());
    closeButton.setNoStroke(false);

    // Configure the text area
    final MTTextArea mtTextArea = new MTTextArea(this.pApplet);
    mtTextArea.setName(this.getCloseButtonText());
    mtTextArea.setText(this.getCloseButtonText());
    mtTextArea.setFont(this.font);
    mtTextArea.setPositionGlobal(new Vector3D(closeButton.getCenterPointLocal().x, closeButton.getCenterPointLocal().y));
    mtTextArea.setNoFill(true);
    mtTextArea.setNoStroke(true);
    mtTextArea.removeAllGestureEventListeners();

    closeButton.addChild(mtTextArea);

    // Add default inputEventListener
    final IMTInputEventListener inputListener = new IMTInputEventListener() {
      @Override
      public boolean processInputEvent(final MTInputEvent inEvt) {

        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
        if (inEvt instanceof AbstractCursorInputEvt) {
          final AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
          switch (cursorInputEvt.getId()) {
            case AbstractCursorInputEvt.INPUT_STARTED:

              // Remove all the buttons from the parent
              MTRadialMenu.this.removeAllMenuItems(1);

              // Default close animation
              AnimationUtil.bounceOut(MTRadialMenu.this, true);

              break;
            case AbstractCursorInputEvt.INPUT_UPDATED:
            case AbstractCursorInputEvt.INPUT_ENDED:
            default:
              break;
          }
        } else {
          LOG.warn("An unexpected event occured:" + inEvt);
        }
        return false;
      }
    };

    closeButton.addInputListener(inputListener);

    this.addChild(closeButton);
  }

  /**
   * Calculates the arched rectangle vertices<br>
   * Based on code from {@link MTRoundRectangle}
   * 
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
   * @return Vertex[]
   *         The vertices needed to create the arched rectangle
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

    if (LOG.isTraceEnabled()) {
      LOG.trace("topArcWidth: " + topArcWidth);
      LOG.trace("bottomArcWidth: " + bottomArcWidth);
      LOG.trace("bottomArcHeight: " + bottomArcHeight);
      LOG.trace("upperLeftPoint: " + upperLeftPoint);
      LOG.trace("upperRightPoint: " + upperRightPoint);
      LOG.trace("bottomArcAdjustment: " + bottomArcAdjustment);
      LOG.trace("lowerRightPoint: " + lowerRightPoint);
      LOG.trace("lowerLeftPoint: " + lowerLeftPoint);
    }

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