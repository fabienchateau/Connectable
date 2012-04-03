/***********************************************************************
 *   MT4j Extension: MTCircularMenu
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

import org.mt4j.components.visibleComponents.widgets.progressBar.AbstractProgressThread;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.sceneManagement.Iscene;

/**
 * @author Uwe Laufs
 *
 */
public abstract class ThreadAndPreDrawAction extends AbstractProgressThread {
	private Iscene scene;
	private int priority = Thread.MIN_PRIORITY;

	public ThreadAndPreDrawAction(Iscene scene){
		super(100);
		this.scene = scene;
		this.setPriority(priority);
		this.start();
	}
	@Override
	public void run() {
		System.out.println("DO IN THREAD FIRST");
		doFirstThreaded();
		IPreDrawAction preDraw = new IPreDrawAction(){
			@Override
			public boolean isLoop() {
				return false;
			}
			@Override
			public void processAction() {
				System.out.println("DO IN PREDRAW ACTION SECOND");
				doSecondPreDraw();
			}
		};
		this.scene.registerPreDrawAction(preDraw);
	}
	public abstract void doFirstThreaded();
	public abstract void doSecondPreDraw();
}
