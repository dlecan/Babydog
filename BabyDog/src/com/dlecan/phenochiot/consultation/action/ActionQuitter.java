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


package com.dlecan.phenochiot.consultation.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;

/**
 * Action pour quitter
 * 
 * @author dam
 *
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class ActionQuitter extends Action {

	/**
	 * Fenetre
	 */
	private ApplicationWindow fenetre;

	/**
	 * Constrcteur
	 * 
	 * @param nFenetre
	 *            La fenêtre
	 */
	public ActionQuitter(ApplicationWindow nFenetre) {
		fenetre = nFenetre;

		setText("&Quitter@Ctrl+W");
		setToolTipText("Quitter l'application");
	}

	/**
	 * Run de l'action
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		//		boolean confirmation = Administration
		//				.afficherConfirmation( fenetre.getShell(),
		//										"Quitter",
		//										"Voulez-vous vraiment quitter cette application ?");

		// L'utilisateur veut quitter
		//		if (confirmation) {
		fenetre.close();
		//		}

	}

}