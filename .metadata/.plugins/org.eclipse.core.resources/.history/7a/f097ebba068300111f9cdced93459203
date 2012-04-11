/**
 * This material was prepared as an account of work sponsored by an agency of the United States Government.<br>
 * Neither the United States Government nor the United States Department of Energy, nor any of their employees,<br> 
 * nor any of their contractors, subcontractors or their employees, makes any warranty, express or implied, or<br>
 * assumes any legal liability or responsibility for the accuracy, completeness, or usefulness or any information,<br> 
 * apparatus, product, or process disclosed, or represents that its use would not infringe privately owned rights.
 */
package gov.pnnl.components.visibleComponents.widgets.radialMenu.item;

import gov.pnnl.components.visibleComponents.widgets.radialMenu.MTRadialMenu;
import gov.pnnl.components.visibleComponents.widgets.radialMenu.MTRadialPoly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.IInputProcessor;

/**
 * This is the value object intended to contain all the information necessary to build<br>
 * an object graph of {@link MTRadialPoly} inside {@link MTRadialMenu}
 * 
 * @author Ryan LaMothe
 * 
 */
public class MTMenuItem implements Serializable {

  private static final long serialVersionUID = 1L;

  private String menuText;

  private List<IMTInputEventListener> inputEventListeners = new ArrayList<IMTInputEventListener>();

  private Map<Class<? extends IInputProcessor>, IGestureEventListener> gestureEventListeners = new ConcurrentHashMap<Class<? extends IInputProcessor>, IGestureEventListener>();

  private List<MTMenuItem> subMenuItems = new ArrayList<MTMenuItem>();

  /**
   * Full constructor
   * 
   * @param menuText
   *          The text to appear on the button
   */
  public MTMenuItem(final String menuText, final Map<Class<? extends IInputProcessor>, IGestureEventListener> gestureEventListeners) {
    if (menuText != null) {
      this.setMenuText(menuText);
    }

    if (gestureEventListeners != null) {
      this.setGestureEventListeners(gestureEventListeners);
    }
  }

  /**
   * Retrieve the menu text
   * 
   * @return the menuText
   */
  public String getMenuText() {
    return this.menuText;
  }

  /**
   * Set the menu text
   * 
   * @param menuText
   *          the menuText to set
   */
  public void setMenuText(final String menuText) {
    this.menuText = menuText;
  }

  /**
   * Retrieve the list of IMTInputEventListeners
   * 
   * @return the inputEventListeners
   */
  public List<IMTInputEventListener> getInputEventListeners() {
    return this.inputEventListeners;
  }

  /**
   * Set the IMTInputEventListeners
   * 
   * @param inputEventListeners
   *          the inputEventListeners to set
   */
  public void setInputEventListeners(final List<IMTInputEventListener> inputEventListeners) {
    this.inputEventListeners = inputEventListeners;
  }

  /**
   * Add an IMTInputEventListener to an existing list of IMTInputEventListeners
   * 
   * @param inputEventListener
   *          the inputEventListener to add
   */
  public void addInputEventListener(final IMTInputEventListener inputEventListener) {
    if (this.getInputEventListeners() != null) {
      this.getInputEventListeners().add(inputEventListener);
    }
  }

  /**
   * Retrieve the GestureEventListeners
   * 
   * @return the gestureEventListeners
   */
  public Map<Class<? extends IInputProcessor>, IGestureEventListener> getGestureEventListeners() {
    return this.gestureEventListeners;
  }

  /**
   * Set the GestureEventListeners
   * 
   * @param gestureEventListeners
   *          the gestureEventListeners to set
   */
  public void setGestureEventListeners(final Map<Class<? extends IInputProcessor>, IGestureEventListener> gestureEventListeners) {
    this.gestureEventListeners = gestureEventListeners;
  }

  /**
   * Add a GestureEventListener to an existing Map of GestureEventListeners
   * 
   * @param gestureEventListener
   */
  public void addGestureEventListener(final Map<Class<? extends IInputProcessor>, IGestureEventListener> gestureEventListener) {
    this.getGestureEventListeners().putAll(gestureEventListener);
  }

  /**
   * Retrieve the list of sub-menu items
   * 
   * @return the subMenuItems
   */
  public List<MTMenuItem> getSubMenuItems() {
    return this.subMenuItems;
  }

  /**
   * Set the list of sub-menu items
   * 
   * @param subMenuItems
   *          the subMenuItems to set
   */
  public void setSubMenuItems(final List<MTMenuItem> subMenuItems) {
    this.subMenuItems = subMenuItems;
  }

  /**
   * Add a sub-menu item to an existing list of sub-menu items
   * 
   * @param mtMenuItem
   *          the mtMenuItem to add
   */
  public void addSubMenuItem(final MTMenuItem mtMenuItem) {
    if (this.getSubMenuItems() != null) {
      this.getSubMenuItems().add(mtMenuItem);
    }
  }
}