/**
 * This material was prepared as an account of work sponsored by an agency of the United States Government.<br>
 * Neither the United States Government nor the United States Department of Energy, nor any of their employees,<br> 
 * nor any of their contractors, subcontractors or their employees, makes any warranty, express or implied, or<br>
 * assumes any legal liability or responsibility for the accuracy, completeness, or usefulness or any information,<br> 
 * apparatus, product, or process disclosed, or represents that its use would not infringe privately owned rights.
 */
package gov.pnnl.components.visibleComponents.widgets.radialMenu.examples;


import org.mt4j.MTApplication;
import org.mt4j.input.inputSources.MacTrackpadSource;
import org.mt4j.util.MT4jSettings;

/**
 * This is the entry-point into the application
 * 
 * @author Ryan LaMothe
 * 
 */
public class StartMTRadialMenuExample extends MTApplication {

  private static final long serialVersionUID = 1L;

  public static void main(final String args[]) {
    MT4jSettings.getInstance().renderer = MT4jSettings.OPENGL_MODE;
    initialize();
  }

  @Override
  public void startUp() {
    // Enable Mac OS X Multi-touch trackpads
    if (this.isMacOSX()) {
      this.getInputManager().registerInputSource(new MacTrackpadSource(this));
    }

    this.addScene(new MTRadialMenuExampleScene(this, "MTRadialMenu Examples"));
  }

  /*
   * http://developer.apple.com/library/mac/#technotes/tn2002/tn2110.html
   */
  public boolean isMacOSX() {
    final String osName = System.getProperty("os.name");
    return osName.startsWith("Mac OS X");
  }
}