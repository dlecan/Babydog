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

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Application BabyDog
 */
public class BabyDogApp implements IPlatformRunnable {

	/**
	 * ID général du plugin
	 */
	public static final String PLUGIN_ID = "com.dlecan.phenochiot.consultation"; //$NON-NLS-1$

	/**
	 * ID de la perspective générale
	 */
	public static final String BABYDOG_PERSPECTIVE_ID = PLUGIN_ID + ".babydogPerspective"; //$NON-NLS-1$

	/**
	 * ID de la vue accueil
	 */
	public static final String ACCUEIL_VIEW_ID = PLUGIN_ID + ".vueAccueil"; //$NON-NLS-1$

	/**
	 * ID de la vue recherche
	 */
	public static final String VUE_RECHERCHE_ID = PLUGIN_ID + ".vueRecherche"; //$NON-NLS-1$

	/**
	 * ID de la vue race
	 */
	public static final String RACE_VIEW_ID = PLUGIN_ID + ".vueRace"; //$NON-NLS-1$

	/**
	 * Constructs a new <code>BabyDogApp</code>.
	 */
	public BabyDogApp() {
		// do nothing
	}

	/**
	 * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
	 */
	public Object run(Object args) throws Exception {
		Display display = PlatformUI.createDisplay();
		try {
			int code = PlatformUI.createAndRunWorkbench(display,
					new BabyDogAdvisor());
			// exit the application with an appropriate return code
			return code == PlatformUI.RETURN_RESTART
					? EXIT_RESTART
					: EXIT_OK;
		} finally {
			if (display != null)
				display.dispose();
		}
	}
}
