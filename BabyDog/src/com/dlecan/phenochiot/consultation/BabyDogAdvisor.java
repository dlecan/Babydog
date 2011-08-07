/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.dlecan.phenochiot.consultation;

import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * 
 * @author Dam
 *
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class BabyDogAdvisor extends WorkbenchAdvisor {

	/**
	 * Key to look up the action builder on the window configurer.
	 */
	private static final String BUILDER_KEY = "builder"; //$NON-NLS-1$

	/**
	 * Constructs a new <code>BabyDogAdvisor</code>.
	 */
	public BabyDogAdvisor() {
		// do nothing
	}

	/**
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer)
	 */
	public void initialize(IWorkbenchConfigurer configurer) {
		configurer.setSaveAndRestore(false);
		super.initialize(configurer);
	}

	/**
	 * @return
	 * @see org.eclipse.ui.application.WorkbenchAdvisor
	 */
	public String getInitialWindowPerspectiveId() {
		// Attention, le fichier plugin_customization.ini annule l'appel
		// à cette méthode
		return BabyDogApp.BABYDOG_PERSPECTIVE_ID; //$NON-NLS-1$
	}

	/**
	 * @param configurer
	 * @see org.eclipse.ui.application.WorkbenchAdvisor
	 */
	public void preWindowOpen(IWorkbenchWindowConfigurer configurer) {
		super.preWindowOpen(configurer);
        configurer.setShowStatusLine(false);
        configurer.setShowMenuBar(false);
	}

	/**
	 * @param window
	 * @param configurer
	 * @param flags
	 * @see org.eclipse.ui.application.WorkbenchAdvisor
	 */
	public void fillActionBars(	IWorkbenchWindow window,
								IActionBarConfigurer configurer,
								int flags) {
		Assert.isTrue((flags & WorkbenchAdvisor.FILL_PROXY) == 0);
		BabyDogActionBuilder builder = new BabyDogActionBuilder(window);
		getWorkbenchConfigurer().getWindowConfigurer(window)
				.setData(BUILDER_KEY, builder);
		builder.fillActionBars(configurer, flags);
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#postWindowClose(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
	 */
	public void postWindowClose(IWorkbenchWindowConfigurer configurer) {
		super.postWindowClose(configurer);
		BabyDogActionBuilder builder = (BabyDogActionBuilder) configurer
				.getData(BUILDER_KEY);
		if (builder != null) {
			builder.dispose();
		}
	}
}