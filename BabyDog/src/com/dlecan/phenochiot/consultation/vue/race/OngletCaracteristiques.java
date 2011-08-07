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


package com.dlecan.phenochiot.consultation.vue.race;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.dlecan.phenochiot.consultation.PluginBabyDog;
import com.dlecan.phenochiot.consultation.utils.Logger;
import com.dlecan.phenochiot.framework.Util;
import com.dlecan.phenochiot.framework.db.GestionnaireAccesDonnees;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class OngletCaracteristiques extends OngletDonnees {

	/**
	 * @see #creerOngletSpecifique(Composite)
	 */
	public void creerOngletSpecifique(Composite form) {
		creerNomenclatureRace(form, false);

		navigateur = new Browser(form, SWT.NONE);
		stopBoutonDroit();

		GridData gridDataNavigateur = new GridData(GridData.FILL_HORIZONTAL
													| GridData.FILL_VERTICAL);
		gridDataNavigateur.verticalSpan = 3;
		navigateur.setLayoutData(gridDataNavigateur);

		StringBuffer html = new StringBuffer();
		html.append("<html><head>");
		html.append(getStyleHTML());
		html.append("</head><body>\n");

		try {
			GestionnaireAccesDonnees gestionnaire = GestionnaireAccesDonnees
					.getInstance(GestionnaireAccesDonnees.TYPE_CLIENT);

			List<Integer> parametres = new ArrayList<Integer>();
			parametres.add(new Integer(idRace));

			ResultSet resultat = gestionnaire.executerSelect(4, parametres);

			resultat.next();

			// Mensurations
			html.append("<fieldset>\n<legend>");
			html.append("Mensurations</legend>");

			StringBuffer ageChiot = creerAffichageAgeChiot(resultat);
			html.append(ageChiot);

			float chiotHauteurMin = resultat.getFloat("chiot_taille_min");
			float chiotHauteurMax = resultat.getFloat("chiot_taille_max");

			String centimetres = " cm<br/>";
			StringBuffer bufferHauteur = creerMensuration("Hauteur au garrot : ",
					0,
					0,
					0,
					0,
					chiotHauteurMin,
					chiotHauteurMax,
					centimetres,
					COLONNE_GAUCHE);

			html.append(bufferHauteur);

			float chiotLongueurSIMin = resultat
					.getFloat("chiot_longueur_si_min");
			float chiotLongueurSIMax = resultat
					.getFloat("chiot_longueur_si_max");

			StringBuffer bufferLongueur = creerMensuration("Longueur scapulo-ischiale : ",
					0,
					0,
					0,
					0,
					chiotLongueurSIMin,
					chiotLongueurSIMax,
					centimetres,
					COLONNE_GAUCHE);

			html.append(bufferLongueur);

			float chiotPoidsMin = resultat.getFloat("chiot_poids_min");
			float chiotPoidsMax = resultat.getFloat("chiot_poids_max");

			String poids = " kg<br/>";
			StringBuffer bufferPoids = creerMensuration("Poids : ",
					0,
					0,
					0,
					0,
					chiotPoidsMin,
					chiotPoidsMax,
					poids,
					COLONNE_GAUCHE);

			html.append(bufferPoids);

			html.append("</fieldset>");

			// Fin mensurations

			StringBuffer retour = creerDonneesGenerales(resultat);
			html.append(retour);

			gestionnaire.terminerRequete(resultat);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			Util.afficherErreur(form.getShell(), e);
		}

		html.append("</body></html>");
		navigateur.setText(html.toString());

		Composite stockageImagesTC = new Composite(form, SWT.NONE);
		stockageImagesTC.setBackground(JFaceColors.getBannerBackground(form
				.getDisplay()));

		GridData gridDataStockageTC = new GridData(GridData.HORIZONTAL_ALIGN_FILL
													| GridData.FILL_VERTICAL);
		gridDataStockageTC.widthHint = 300;
		stockageImagesTC.setLayoutData(gridDataStockageTC);
		stockageImagesTC.setLayout(new GridLayout());

		final Canvas canvasImageTC = creerCanvasImage(stockageImagesTC,
				vueRace.getImageTC());
		canvasImageTC.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
													| GridData.FILL_VERTICAL));

		Composite stockageImagesPC = new Composite(form, SWT.NONE);
		stockageImagesPC.setBackground(JFaceColors.getBannerBackground(vueRace
				.getParent().getDisplay()));
		GridData gridDataStockagePC = new GridData(GridData.HORIZONTAL_ALIGN_FILL
													| GridData.FILL_VERTICAL);
		gridDataStockagePC.widthHint = 300;
		stockageImagesPC.setLayoutData(gridDataStockagePC);
		stockageImagesPC.setLayout(new GridLayout());

		final Canvas canvasImagePC = creerCanvasImage(stockageImagesPC,
				vueRace.getImagePC());
		canvasImagePC.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
													| GridData.FILL_VERTICAL));
	}

	protected String getTitre() {
		return "Phénotype du chiot";
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.race.OngletDonnees#dispose()
	 */
	public void dispose() {
		// Rien
	}

	protected String getFichierDescriptionFiche() {
		return "race_caracteristiques.xml";
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.race.OngletDonnees#getTailleParagraphe()
	 */
	protected String getTailleParagraphe() {
		String style = "p.corps {"
						+ "	font-size: 11px;"
						+ " text-align: justify;"
						+ " margin: 5px; "
						+ " width: 99%;"
						+ "}\n";
		return style;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.race.OngletDonnees#getImage()
	 */
	protected Image getImage() {
		return PluginBabyDog.getDefault().getImage(PluginBabyDog.CHIOT);
	}
}