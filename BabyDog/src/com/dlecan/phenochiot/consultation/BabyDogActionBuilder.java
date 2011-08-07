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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.dlecan.phenochiot.consultation.action.ActionAPropos;

/**
 * Construit les actions
 */
public class BabyDogActionBuilder {

	private IWorkbenchWindow window;

	private ActionFactory.IWorkbenchAction actionQuitter, actionAPropos;

	private IAction actionRecherche;

	/**
	 * @param nWindow La fenêtre
	 */
	public BabyDogActionBuilder(IWorkbenchWindow nWindow) {
		this.window = nWindow;
	}

	/**
	 * @param configurer
	 * @param flags
	 */
	public void fillActionBars(IActionBarConfigurer configurer, int flags) {
		if ((flags & WorkbenchAdvisor.FILL_PROXY) == 0) {
			makeActions();
		}
		if ((flags & WorkbenchAdvisor.FILL_COOL_BAR) != 0) {
			fillCoolBar(configurer.getCoolBarManager());
		}
	}

	private void makeActions() {
		actionQuitter = ActionFactory.QUIT.create(window);
		actionQuitter.setText("Quitter");
		actionQuitter.setImageDescriptor(PluginBabyDog.getDefault()
				.getImageDescriptor(PluginBabyDog.QUITTER));

		actionAPropos = new ActionAPropos(window);
		actionAPropos.setText("A propos");
		actionAPropos.setImageDescriptor(PluginBabyDog.getDefault()
				.getImageDescriptor(PluginBabyDog.A_PROPOS));

		actionRecherche = new Action("Recherche") {

			public void run() {
				try {
					window.getActivePage()
							.showView(BabyDogApp.VUE_RECHERCHE_ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		};
		actionRecherche.setImageDescriptor(PluginBabyDog.getDefault()
				.getImageDescriptor(PluginBabyDog.RECHERCHER));
	}

	/**
	 * @param menuBar
	 */
	public void fillMenuBar(IMenuManager menuBar) {
		IMenuManager fileMenu = new MenuManager("&Fichier", "Fichier"); //$NON-NLS-2$
		menuBar.add(fileMenu);
		fileMenu.add(actionQuitter);

		IMenuManager viewMenu = new MenuManager("&Affichage", "Vue"); //$NON-NLS-2$
		menuBar.add(viewMenu);
		viewMenu.add(actionRecherche);

		IMenuManager helpMenu = new MenuManager("&?", "Aide"); //$NON-NLS-2$
		menuBar.add(helpMenu);
		helpMenu.add(actionAPropos);
	}

	/**
	 * @param coolBar
	 */
	public void fillCoolBar(ICoolBarManager coolBar) {
		coolBar.setLockLayout(true);
		IToolBarManager toolBar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolBar, "standard"));

		ActionContributionItem quitterCI = new ActionContributionItem(actionQuitter);
		quitterCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBar.add(quitterCI);

		ActionContributionItem rechercherCI = new ActionContributionItem(actionRecherche);
		rechercherCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBar.add(rechercherCI);

		ActionContributionItem aProposCI = new ActionContributionItem(actionAPropos);
		aProposCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
		toolBar.add(aProposCI);
	}

	/**
	 *
	 */
	public void dispose() {
		// Rien
	}

	/**
	 * @return Retourne window.
	 */
	public final IWorkbenchWindow getWindow() {
		return window;
	}
}