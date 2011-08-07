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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.dlecan.phenochiot.consultation.PluginBabyDog;
import com.dlecan.phenochiot.consultation.utils.Logger;
import com.dlecan.phenochiot.framework.Util;
import com.dlecan.phenochiot.framework.db.GestionnaireAccesDonnees;
import com.dlecan.phenochiot.framework.xml.AnnexeDOM;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class OngletComparaison extends OngletDonnees {

	/**
	 * @see #creerOngletSpecifique(Composite)
	 */
	public void creerOngletSpecifique(Composite form) {
		Composite stockageImagesPC = new Composite(form, SWT.NONE);
		stockageImagesPC.setBackground(JFaceColors.getBannerBackground(form
				.getDisplay()));
		GridData gridDataStockagePC = new GridData(GridData.VERTICAL_ALIGN_FILL
													| GridData.FILL_HORIZONTAL);
		gridDataStockagePC.heightHint = 260;
		stockageImagesPC.setLayoutData(gridDataStockagePC);
		stockageImagesPC.setLayout(new GridLayout());

		final Canvas canvasImagePC = creerCanvasImage(stockageImagesPC,
				vueRace.getImagePC());
		canvasImagePC.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
													| GridData.FILL_VERTICAL));

		Composite stockageImagesPA = new Composite(form, SWT.NONE);
		stockageImagesPA.setBackground(JFaceColors.getBannerBackground(form
				.getDisplay()));
		GridData gridDataStockagePA = new GridData(GridData.VERTICAL_ALIGN_FILL
													| GridData.FILL_HORIZONTAL);
		gridDataStockagePA.heightHint = 260;
		stockageImagesPA.setLayoutData(gridDataStockagePA);
		stockageImagesPA.setLayout(new GridLayout());

		final Canvas canvasImagePA = creerCanvasImage(stockageImagesPA,
				vueRace.getImagePA());
		canvasImagePA.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
													| GridData.FILL_VERTICAL));

		navigateur = new Browser(form, SWT.NONE);
		stopBoutonDroit();

		GridData gridDataScroll = new GridData(GridData.FILL_HORIZONTAL
												| GridData.FILL_VERTICAL);
		gridDataScroll.horizontalSpan = 2;
		navigateur.setLayoutData(gridDataScroll);

		StringBuffer html = new StringBuffer();
		html.append("<html><head>");
		html.append(getStyleHTML());
		html.append("</head><body>\n");

		try {
			GestionnaireAccesDonnees gestionnaire = GestionnaireAccesDonnees
					.getInstance(GestionnaireAccesDonnees.TYPE_CLIENT);

			List<Integer> parametres = new ArrayList<Integer>();
			parametres.add(new Integer(idRace));

			ResultSet resultat = gestionnaire.executerSelect(6, parametres);
			resultat.next();

			// Mensurations
			html.append("<fieldset>\n<legend align=\"center\">");
			html.append("Mensurations</legend>");

			StringBuffer ageChiot = creerAffichageAgeChiot(resultat);
			html.append(ageChiot);

			float adulteHauteurMaleMin = resultat.getFloat("std_m_taille_min");
			float adulteHauteurMaleMax = resultat.getFloat("std_m_taille_max");
			float adulteHauteurFemMin = resultat.getFloat("std_f_taille_min");
			float adulteHauteurFemMax = resultat.getFloat("std_f_taille_max");

			float chiotHauteurMin = resultat.getFloat("chiot_taille_min");
			float chiotHauteurMax = resultat.getFloat("chiot_taille_max");

			String centimetres = " cm<br/>";
			StringBuffer bufferHauteur = creerMensuration("Hauteur au garrot : ",
					adulteHauteurMaleMin,
					adulteHauteurMaleMax,
					adulteHauteurFemMin,
					adulteHauteurFemMax,
					chiotHauteurMin,
					chiotHauteurMax,
					centimetres,
					DEUX_COLONNES);

			html.append(bufferHauteur);

			float adultePoidsMaleMin = resultat.getFloat("std_m_poids_min");
			float adultePoidsMaleMax = resultat.getFloat("std_m_poids_max");
			float adultePoidsFemMin = resultat.getFloat("std_f_poids_min");
			float adultePoidsFemMax = resultat.getFloat("std_f_poids_max");

			float chiotPoidsMin = resultat.getFloat("chiot_poids_min");
			float chiotPoidsMax = resultat.getFloat("chiot_poids_max");

			String poids = " kg<br/>";
			StringBuffer bufferPoids = creerMensuration("Poids : ",
					adultePoidsMaleMin,
					adultePoidsMaleMax,
					adultePoidsFemMin,
					adultePoidsFemMax,
					chiotPoidsMin,
					chiotPoidsMax,
					poids,
					DEUX_COLONNES);

			html.append(bufferPoids);

			html.append("</fieldset>");

			// Fin mensurations

			Element comparaison = document.getDocumentElement();
			NodeList listeChamps = comparaison.getElementsByTagName("champ");

			int nbChamps = listeChamps.getLength();

			for (int i = 0; i < nbChamps; i++) {
				StringBuffer sbChamps = new StringBuffer();
				boolean ajouterChamps = false;

				Element noeudChamp = (Element) listeChamps.item(i);
				String nomChamp = noeudChamp.getAttribute("nom");

				sbChamps.append("<fieldset>\n<legend align=\"center\">");
				sbChamps.append(nomChamp);
				sbChamps.append("</legend>\n");
				sbChamps.append("<table width=\"100%\">\n");

				NodeList listeCriteres = noeudChamp
						.getElementsByTagName("critere");

				int nbCriteres = listeCriteres.getLength();

				for (int j = 0; j < nbCriteres; j++) {
					Element critere = (Element) listeCriteres.item(j);
					String nomCritere = critere.getAttribute("nom");

					NodeList listeElements = critere
							.getElementsByTagName("element");

					int nbElements = listeElements.getLength();

					StringBuffer sbElements = new StringBuffer();
					boolean ajouterElements = true;
					for (int k = 1; k <= nbElements; k++) {
						sbElements.append("<td width=\"50%\" ");
						sbElements.append("valign=\"top\" class=\"corps\">\n");

						Element element = (Element) listeElements.item(k - 1);
						NodeList listeSql = element
								.getElementsByTagName("sql");

						int nbSql = listeSql.getLength();
						boolean ajouterSql = false;
						for (int l = 0; l < nbSql; l++) {
							Element sql = (Element) listeSql.item(l);
							String titre = sql.getAttribute("titre");
							String champSQL = AnnexeDOM.trouveTexte(sql);
							String valeurChampSQL = resultat
									.getString(champSQL);

							if (!"".equals(nomCritere)) {
								sbElements.append("<span class=\"sstitre\">");
								sbElements.append(nomCritere);
								if (!"".equals(titre)) {
									sbElements.append(' ');
									sbElements.append(titre);
								}
								sbElements.append(" : </span>");
							}

							if (valeurChampSQL != null
								&& !"".equals(valeurChampSQL)) {
								ajouterSql = true;
								sbElements.append(valeurChampSQL);
								// Cas où on a 3 sql de suite ...
								if (nbSql > 0 && l < nbSql - 1) {
									sbElements.append("<br/>\n");
								}
							}
						}
						ajouterElements &= ajouterSql;

						sbElements.append("</td>\n");

						if (k % 2 == 0) {
							sbElements.append("</tr>\n<tr>\n");
						}
					}
					if (ajouterElements) {
						sbChamps.append("<tr>\n");
						sbChamps.append(sbElements);
						sbChamps.append("</tr>\n");
					}
					ajouterChamps |= ajouterElements;
				}
				sbChamps.append("</table>\n");
				sbChamps.append("</fieldset>\n");

				if (ajouterChamps) {
					html.append(sbChamps);
				}
			}
			gestionnaire.terminerRequete(resultat);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			Util.afficherErreur(form.getShell(), e);
		}
		html.append("</body></html>");
		navigateur.setText(html.toString());
	}

	protected String getTitre() {
		return "Comparaison chiot/adulte";
	}

	protected String getFichierDescriptionFiche() {
		return "race_comparaison.xml";
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.race.OngletDonnees#getTailleParagraphe()
	 */
	protected String getTailleParagraphe() {
		String style = "p.corps {"
						+ "	font-size: 11px;"
						+ " text-align: justify;"
						+ " float: left;"
						+ " margin: 5px; "
						+ " width: 47%;"
						+ "}\n"
						+ "td.corps {"
						+ "	font-size: 11px;"
						+ " text-align: justify;"
						+ " padding: 5px; "
						+ "}\n";
		return style;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.race.OngletDonnees#getImage()
	 */
	protected Image getImage() {
		return PluginBabyDog.getDefault().getImage(PluginBabyDog.GROUPE);
	}
}