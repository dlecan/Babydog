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

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class PluginBabyDog extends AbstractUIPlugin {

	public static final String ZOOM_IN = "zoomin";

	public static final String ZOOM_OUT = "zoomout";

	public static final String QUITTER = "quitter";

	public static final String RECHERCHER = "rechercher";

	public static final String A_PROPOS = "a_propos";

	public static final String COLLAPSE_ALL = "collapseall";

	public static final String EXPAND_ALL = "expandall";

	public static final String GROUPE = "groupe";

	public static final String RACE = "race";

	public static final String EFFACER = "effacer";
	
	public static final String FOND = "form_banner";
	
	public static final String CHIOT = "chiot";
	
	public static final String ADULTE = "adulte";

	private static PluginBabyDog plugin;

	private FormColors formColors;

	/**
	 * 
	 */
	public PluginBabyDog() {
		super();
		plugin = this;
	}

	/**
	 * 
	 */
	public PluginBabyDog(IPluginDescriptor nPlugin) {
		super();
		plugin = this;
	}

	/**
	 * @return
	 */
	public static PluginBabyDog getDefault() {
		return plugin;
	}

	public FormColors getFormColors(Display display) {
		if (formColors == null) {
			formColors = new FormColors(display);
			formColors.markShared();
		}
		return formColors;
	}

	protected void initializeImageRegistry(ImageRegistry registry) {
		registerImage(registry, ZOOM_IN, "ZoomIn16.gif");
		registerImage(registry, ZOOM_OUT, "ZoomOut16.gif");
		registerImage(registry, QUITTER, "quitter.png");
		registerImage(registry, RECHERCHER, "rechercher.png");
		registerImage(registry, A_PROPOS, "a_propos.png");
		registerImage(registry, COLLAPSE_ALL, "collapseall.gif");
		registerImage(registry, EXPAND_ALL, "expandall.gif");
		registerImage(registry, GROUPE, "groupe16.gif");
		registerImage(registry, RACE, "babydog16.png");
		registerImage(registry, EFFACER, "effacer.gif");
		registerImage(registry, FOND, "form_banner.gif");
		registerImage(registry, CHIOT, "chiot.gif");
		registerImage(registry, ADULTE, "adulte.gif");
	}

	private void registerImage(ImageRegistry registry, String key,
			String fileName) {
		try {
			IPath path = new Path("icons/" + fileName);
			URL url = find(path);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
			// Rien
		}
	}

	public Image getImage(String key) {
		return getImageRegistry().get(key);
	}

	public ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}

	public void stop(BundleContext context) throws Exception {
		try {
			if (formColors != null) {
				formColors.dispose();
				formColors = null;
			}
		} finally {
			super.stop(context);
		}
	}
}
