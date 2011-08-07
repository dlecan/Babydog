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
public class OngletStandardAdulte extends OngletDonnees {

	/**
	 * @see #creerOngletSpecifique(Composite)
	 */
	public void creerOngletSpecifique(Composite form) {
		creerNomenclatureRace(form, true);

		navigateur = new Browser(form, SWT.NONE);
		stopBoutonDroit();

		GridData gridDataNavigateur = new GridData(GridData.FILL_HORIZONTAL
													| GridData.FILL_VERTICAL);
		gridDataNavigateur.verticalSpan = 2;
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

			ResultSet resultat = gestionnaire.executerSelect(5, parametres);

			resultat.next();

			// Mensurations
			html.append("<fieldset>\n<legend>");
			html.append("Mensurations</legend>");

			float adulteHauteurMaleMin = resultat.getFloat("std_m_taille_min");
			float adulteHauteurMaleMax = resultat.getFloat("std_m_taille_max");
			float adulteHauteurFemMin = resultat.getFloat("std_f_taille_min");
			float adulteHauteurFemMax = resultat.getFloat("std_f_taille_max");

			String centimetres = " cm<br/>";
			StringBuffer bufferHauteur = creerMensuration("Hauteur au garrot : ",
					adulteHauteurMaleMin,
					adulteHauteurMaleMax,
					adulteHauteurFemMin,
					adulteHauteurFemMax,
					0,
					0,
					centimetres,
					COLONNE_DROITE);

			html.append(bufferHauteur);

			float adultePoidsMaleMin = resultat.getFloat("std_m_poids_min");
			float adultePoidsMaleMax = resultat.getFloat("std_m_poids_max");
			float adultePoidsFemMin = resultat.getFloat("std_f_poids_min");
			float adultePoidsFemMax = resultat.getFloat("std_f_poids_max");

			String poids = " kg<br/>";
			StringBuffer bufferPoids = creerMensuration("Poids : ",
					adultePoidsMaleMin,
					adultePoidsMaleMax,
					adultePoidsFemMin,
					adultePoidsFemMax,
					0,
					0,
					poids,
					COLONNE_DROITE);

			html.append(bufferPoids);

			html.append("</fieldset>");

			// Fin mensurations

			StringBuffer retour = creerDonneesGenerales(resultat);
			html.append(retour);

			gestionnaire.terminerRequete(resultat);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			Util.afficherErreur(parent.getShell(), e);
		}

		html.append("</body></html>");

		navigateur.setText(html.toString());

		Composite stockageImagesPA = new Composite(form, SWT.NONE);
		stockageImagesPA.setBackground(JFaceColors.getBannerBackground(form
				.getDisplay()));
		GridData gridDataStockage = new GridData(GridData.HORIZONTAL_ALIGN_FILL
													| GridData.FILL_VERTICAL);
		gridDataStockage.widthHint = 300;
		stockageImagesPA.setLayoutData(gridDataStockage);
		stockageImagesPA.setLayout(new GridLayout());

		final Canvas canvasImage = creerCanvasImage(stockageImagesPA, vueRace
				.getImagePA());
		canvasImage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
												| GridData.FILL_VERTICAL));
	}

	protected String getTitre() {
		return "Standard de l'adulte";
	}

	protected String getFichierDescriptionFiche() {
		return "race_standard.xml";
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
		return PluginBabyDog.getDefault().getImage(PluginBabyDog.ADULTE);
	}
}