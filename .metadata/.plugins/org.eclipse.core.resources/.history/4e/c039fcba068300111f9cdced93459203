/**
 * This material was prepared as an account of work sponsored by an agency of the United States Government.<br>
 * Neither the United States Government nor the United States Department of Energy, nor any of their employees,<br> 
 * nor any of their contractors, subcontractors or their employees, makes any warranty, express or implied, or<br>
 * assumes any legal liability or responsibility for the accuracy, completeness, or usefulness or any information,<br> 
 * apparatus, product, or process disclosed, or represents that its use would not infringe privately owned rights.
 */
package gov.pnnl.components.visibleComponents.widgets.radialMenu;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.util.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Matrix;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;
import org.mt4jx.util.animation.AnimationUtil;

import processing.core.PApplet;

/**
 * The menu item polygon internal class for use in the {@link MTRadialMenu}
 * 
 * @author Ryan LaMothe
 * 
 */
public class MTRadialPoly extends MTPolygon {

  // Logger for this class
  private static final Logger LOG = Logger.getLogger(MTRadialPoly.class);

  private Vector3D center;

  private MTColor selectedColor = new MTColor(MTColor.BLUE);

  private MTColor unSelectedColor = new MTColor(MTColor.GREY);

  private boolean subMenuActive = false;

  private float subMenuLevel = 1;

  private List<MTRadialPoly> subMenuItems = new CopyOnWriteArrayList<MTRadialPoly>();

  /**
   * Full constructor
   * 
   * @param pApplet
   * @param vertices
   * @param center
   * @param buttonHeight
   * @param radius
   * @param menuText
   * @param font
   * @param selectedColor
   * @param unSelectedColor
   */
  public MTRadialPoly(final PApplet pApplet, final Vertex[] vertices,
      final Vector3D center,
      final float buttonHeight,
      final float radius,
      final String menuText, final IFont font,
      final MTColor selectedColor, final MTColor unSelectedColor) {

    super(pApplet, vertices);

    // Set colors
    if (selectedColor != null) {
      this.setSelectedColor(selectedColor);
    }

    if (unSelectedColor != null) {
      this.setUnSelectedColor(unSelectedColor);
    }

    // Set center
    this.setCenter(center);

    // Create intial menu item
    this.createMenuItem(pApplet, font, menuText);

    // Add inputEventListener
    final IMTInputEventListener inputListener = new IMTInputEventListener() {
      @Override
      public boolean processInputEvent(final MTInputEvent inEvt) {

        // Most input events in MT4j are an instance of AbstractCursorInputEvt (mouse, multi-touch..)
        if (inEvt instanceof AbstractCursorInputEvt) {
          final AbstractCursorInputEvt cursorInputEvt = (AbstractCursorInputEvt) inEvt;
          final IMTComponent3D target = cursorInputEvt.getTarget();
          switch (cursorInputEvt.getId()) {
            case AbstractCursorInputEvt.INPUT_STARTED:

              // Change colors
              if (MTRadialPoly.this.getFillColor().equals(MTRadialPoly.this.getUnSelectedColor()))
            {
              MTRadialPoly.this.setFillColor(MTRadialPoly.this.getSelectedColor());
            }
            else
            {
              MTRadialPoly.this.setFillColor(MTRadialPoly.this.getUnSelectedColor());
            }

            break;
          case AbstractCursorInputEvt.INPUT_ENDED:

            LOG.debug("Target: " + target.getName());

            // Enable/disable sub-menu items
            if ((MTRadialPoly.this.isSubMenuActive() != true) && (MTRadialPoly.this.getSubMenuItems().size() > 0)) {

              // Remove all other active menu items
              ((MTRadialMenu) MTRadialPoly.this.getParent()).removeAllActiveMenuItems(MTRadialPoly.this.getSubMenuLevel());

              MTRadialPoly.this.setSubMenuActive(true);

              LOG.debug("Sub-menu activated...Adding " + MTRadialPoly.this.getSubMenuItems().size() + " menu items");

              // Note: Math.PI can be divided by different numbers to produce different button configurations
              final float angularDiv = ((float) Math.PI) / 5;

              for (int i = 0; i < MTRadialPoly.this.getSubMenuItems().size(); i++)
              {
                final MTRadialPoly menuItem = MTRadialPoly.this.getSubMenuItems().get(i);

                final float angle = angularDiv * (5 + i + ((6 - MTRadialPoly.this.getSubMenuItems().size()) / 2f));

                if (menuItem.getSubMenuLevel() <= MTRadialPoly.this.getSubMenuLevel())
                {
                  menuItem.setSubMenuLevel(menuItem.getSubMenuLevel() + 1);
                }

                final float adjRadius = radius + ((buttonHeight * MTRadialPoly.this.getSubMenuLevel()) + (40 * (1 / MTRadialPoly.this.getSubMenuLevel())));

                /*
                 * X coordinate: radius * cos(angle) where angle is in radians
                 * Y coordinate: radius * sin(angle) where angle is in radians
                 */
                final float xCoord = MTRadialPoly.this.getCenter().x + (adjRadius * (float) Math.cos(angle));
                final float yCoord = MTRadialPoly.this.getCenter().y + (adjRadius * (float) Math.sin(angle));

                final float degrees = ((float) Math.toDegrees(angle)) - 270f;

                menuItem.setLocalMatrix(Matrix.getZRotationMatrix(MTRadialPoly.this.getCenter(), degrees));

                menuItem.setPositionRelativeToParent(new Vector3D(xCoord, yCoord, 0));

                menuItem.setVisible(true);

                AnimationUtil.scaleIn(menuItem);

                if (LOG.isTraceEnabled())
                {
                  LOG.trace("ButtonHeight: " + buttonHeight);
                  LOG.trace("SubMenuLevel: " + menuItem.getSubMenuLevel());
                  LOG.trace("Adjusted Radius: " + adjRadius);
                  LOG.trace("Sub-menu degrees: " + degrees);
                }

                // Add sub-menu items to root MTRadialMenu
                MTRadialPoly.this.getParent().addChild(menuItem);
              }
            }
            else
            {
              MTRadialPoly.this.setSubMenuActive(false);

              LOG.debug("Sub-menu de-activated...Removing " + MTRadialPoly.this.getSubMenuItems().size() + " menu items");

              if (MTRadialPoly.this.getSubMenuItems().size() == 0)
              {
                // This means a menu item was selected at the end of a menu item tree so close the entire menu
                if (MTRadialPoly.this.getParent().getName().equals(MTRadialMenu.NAME))
                {
                  MTRadialPoly.this.setFillColor(MTRadialPoly.this.getUnSelectedColor());

                  // Remove all the buttons from the parent
                  ((MTRadialMenu) MTRadialPoly.this.getParent()).removeAllMenuItems(1);

                  // Default close animation
                  AnimationUtil.bounceOut((MTRadialMenu) MTRadialPoly.this.getParent(), true);
                }
              }
              else
              {
                // Remove all the sub-menu items of only this menu item
                MTRadialPoly.this.removeAllMenuItems();
              }
            }

            break;
          default:
            break;
        }
      } else {
        LOG.warn("An unexpected event occured:" + inEvt);
      }
      return false;
    }
    };

    this.addInputListener(inputListener);
  }

  /**
   * Add a sub-menu item to this menu item
   * 
   * @param mtMenuItem
   */
  public void addSubMenuItem(final MTRadialPoly mtMenuItem) {
    this.subMenuItems.add(mtMenuItem);
  }

  /**
   * Retrieve the center vector of this menu item
   * 
   * @return Vector3D
   */
  protected Vector3D getCenter() {
    return this.center;
  }

  /**
   * Set the center vector of this menu item
   * 
   * @param center
   */
  protected void setCenter(final Vector3D center) {
    this.center = center;
  }

  /**
   * Retrieve the "Selected" color of this menu item
   * 
   * @return MTColor
   */
  public MTColor getSelectedColor() {
    return this.selectedColor;
  }

  /**
   * Set the "Selected" color of this menu item
   * 
   * @param selectedColor
   */
  public void setSelectedColor(final MTColor selectedColor) {
    this.selectedColor = selectedColor;
  }

  /**
   * Retrieve the "Unselected" color of this menu item
   * 
   * @return MTColor
   */
  public MTColor getUnSelectedColor() {
    return this.unSelectedColor;
  }

  /**
   * Set the "Unselected" color of this menu item
   * 
   * @param unSelectedColor
   */
  public void setUnSelectedColor(final MTColor unSelectedColor) {
    this.unSelectedColor = unSelectedColor;
  }

  /**
   * Retrieve the sub-menu level of this menu item
   * 
   * @return float
   *         The sub-menu level
   */
  public float getSubMenuLevel() {
    return this.subMenuLevel;
  }

  /**
   * Set the sub-menu level of this menu item
   * 
   * @param subMenuLevel
   */
  protected void setSubMenuLevel(final float subMenuLevel) {
    this.subMenuLevel = subMenuLevel;
  }

  /**
   * Retrieve whether this menu item is "Active"
   * 
   * @return
   *         Whether the sub-menu is active
   */
  protected boolean isSubMenuActive() {
    return this.subMenuActive;
  }

  /**
   * Set whether this menu item is "Active"
   * 
   * @param subMenuActive
   */
  protected void setSubMenuActive(final boolean subMenuActive) {
    this.subMenuActive = subMenuActive;
  }

  /**
   * Retrieve a list of sub-menu items
   * 
   * @return the subMenuItems
   */
  protected List<MTRadialPoly> getSubMenuItems() {
    return this.subMenuItems;
  }

  /**
   * Set the list of sub-menu items
   * 
   * @param subMenuItems
   *          the subMenuItems to set
   */
  protected void setSubMenuItems(final List<MTRadialPoly> subMenuItems) {
    this.subMenuItems = subMenuItems;
  }

  /**
   * Removes all of the sub-menu items that are children of this menu item
   */
  protected void removeAllMenuItems() {
    for (final MTRadialPoly item : this.subMenuItems) {

      item.setFillColor(this.getUnSelectedColor());
      item.setSubMenuActive(false);
      item.removeAllMenuItems();

      // Default animation for removing menu items
      AnimationUtil.scaleOut(item, false);
    }
  }

  /**
   * This method creates the menu button
   * 
   * @param pApplet
   * @param font
   * @param angle
   * @param menuText
   */
  private void createMenuItem(final PApplet pApplet, final IFont font, final String menuText) {

    // Configure this menu item
    this.unregisterAllInputProcessors();
    this.setName(menuText);
    this.setFillColor(this.getUnSelectedColor());
    this.setNoStroke(true);

    // Configure the text area
    final MTTextArea mtTextArea = new MTTextArea(pApplet);
    mtTextArea.setName(menuText);
    mtTextArea.setText(menuText);
    mtTextArea.setFont(font);

    // Add font height to y coordinate to center text in middle of button
    mtTextArea.setPositionGlobal(new Vector3D(this.getCenterPointLocal().x, this.getCenterPointLocal().y + font.getOriginalFontSize()));
    mtTextArea.setNoFill(true);
    mtTextArea.setNoStroke(true);
    mtTextArea.removeAllGestureEventListeners();

    // Default animation for creating menu items
    AnimationUtil.scaleIn(this);

    this.addChild(mtTextArea);
  }
}