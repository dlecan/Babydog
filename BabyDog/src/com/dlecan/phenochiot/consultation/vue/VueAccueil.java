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


package com.dlecan.phenochiot.consultation.vue;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.part.ViewPart;

import com.dlecan.phenochiot.consultation.PluginBabyDog;
import com.dlecan.phenochiot.consultation.action.NouvelleRace;
import com.dlecan.phenochiot.consultation.utils.Logger;
import com.dlecan.phenochiot.framework.Nomenclature;
import com.dlecan.phenochiot.framework.Util;
import com.dlecan.phenochiot.framework.db.GestionnaireAccesDonnees;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class VueAccueil extends ViewPart {

	private FormToolkit toolkit;

	private static Color couleurFond;

	private static Color couleurGris;

	private static Color couleurLien;

	private static Color couleurLienSelection;

	private static Color couleurGroupeLienSelectionne;

	private static Cursor curseurMain;

	private Label labelSelectionne = null;

	//	private ScrolledComposite contenant;

	private HashMap<Integer, List<List<Object>>> racesParGroupe = new HashMap<Integer, List<List<Object>>>();

	private Font policeSoulignee;

	private Composite contenantListeRaces;

	private Composite page;

	/**
	 * Constructs a new <code>VueAccueil</code>.
	 */
	public VueAccueil() {
		// do nothing
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(PluginBabyDog.getDefault()
				.getFormColors(parent.getDisplay()));

		couleurFond = JFaceColors.getBannerBackground(parent.getDisplay());
		couleurGris = parent.getDisplay().getSystemColor(SWT.COLOR_GRAY);
		couleurLien = JFaceColors.getHyperlinkText(parent.getDisplay());
		couleurLienSelection = JFaceColors.getActiveHyperlinkText(parent
				.getDisplay());
		couleurGroupeLienSelectionne = parent.getDisplay()
				.getSystemColor(SWT.COLOR_RED);

		curseurMain = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
		Font police = JFaceResources.getHeaderFont();
		FontData[] fontData = police.getFontData();
		fontData[0].data.lfUnderline = 1;
		policeSoulignee = new Font(parent.getDisplay(), fontData);

		parent.setBackground(couleurFond);

		page = new Composite(parent, SWT.NONE);
		page.setBackground(couleurFond);

		GridLayout layoutPage = new GridLayout();
		layoutPage.numColumns = 5;
		layoutPage.makeColumnsEqualWidth = true;
		page.setLayout(layoutPage);

		Label separateurVide = new Label(page, SWT.NONE);
		separateurVide.setBackground(couleurFond);
		GridData gridSeparateurVide = new GridData(GridData.FILL_HORIZONTAL);
		gridSeparateurVide.horizontalSpan = 5;
		gridSeparateurVide.heightHint = 15;
		separateurVide.setLayoutData(gridSeparateurVide);

		try {
			GestionnaireAccesDonnees gestionnaire = GestionnaireAccesDonnees
					.getInstance(GestionnaireAccesDonnees.TYPE_CLIENT);

			ResultSet resultat = gestionnaire.executerSelect(3);

			int idGroupePrecedent = 0;
			Composite groupe = null;
			List<List<Object>> listeRacesDuGroupe = null;
			while (resultat.next()) {
				String nomRace = resultat.getString("nom_race");
				final int idGroupe = resultat.getInt("groupe");
				final String nomGroupe = Nomenclature.getGroupe(idGroupe);
				int idRace = resultat.getInt("id_race");
				int etatFiche = resultat.getInt("etat_fiche");

				if (idGroupe != idGroupePrecedent) {
					groupe = new Composite(page, SWT.NONE);
					groupe.setBackground(parent.getBackground());
					groupe.setLayoutData(creerGridData());

					listeRacesDuGroupe = new ArrayList<List<Object>>();
					racesParGroupe.put(new Integer(idGroupe),
							listeRacesDuGroupe);

					GridLayout layoutComposant = new GridLayout();
					layoutComposant.numColumns = 1;
					groupe.setLayout(layoutComposant);

					final Label numeroGp = new Label(groupe, SWT.CENTER);
					numeroGp.setBackground(couleurFond);
					numeroGp.setText("Groupe " + idGroupe);
					numeroGp.setToolTipText(nomGroupe);
					numeroGp.setFont(JFaceResources.getHeaderFont());
					numeroGp
							.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
					numeroGp.setCursor(curseurMain);
					numeroGp.addListener(SWT.MouseUp, new Listener() {

						public void handleEvent(Event e) {
							if (labelSelectionne != null) {
								labelSelectionne.setFont(JFaceResources
										.getHeaderFont());
								labelSelectionne.setForeground(couleurLien);
							}
							labelSelectionne = numeroGp;
							numeroGp.setFont(policeSoulignee);
							numeroGp
									.setForeground(couleurGroupeLienSelectionne);
							afficherListeRaces(contenantListeRaces, idGroupe);
						}
					});
					numeroGp.addListener(SWT.MouseEnter, new Listener() {

						public void handleEvent(Event e) {
							numeroGp.setFont(policeSoulignee);
							numeroGp.setForeground(couleurLienSelection);
						}
					});
					numeroGp.addListener(SWT.MouseExit, new Listener() {

						public void handleEvent(Event e) {
							if (numeroGp.equals(labelSelectionne)) {
								numeroGp
										.setForeground(couleurGroupeLienSelectionne);
								numeroGp.setFont(policeSoulignee);
							} else {
								numeroGp.setFont(JFaceResources
										.getHeaderFont());
								numeroGp.setForeground(couleurLien);
							}
						}
					});
				}
				idGroupePrecedent = idGroupe;

				List<Object> donneesRace = new ArrayList<Object>();
				donneesRace.add(new Integer(idRace));
				donneesRace.add(nomRace);
				donneesRace.add(new Integer(etatFiche));
				listeRacesDuGroupe.add(donneesRace);
			}

			Composite separateur = toolkit.createCompositeSeparator(page);
			
//			Label separateur = new Label(page, SWT.SEPARATOR | SWT.HORIZONTAL);
//			separateur.setBackground(couleurFond);
			GridData gridSeparateur = new GridData(GridData.FILL_HORIZONTAL);
			gridSeparateur.horizontalSpan = 5;
			gridSeparateur.heightHint = 2;
			separateur.setLayoutData(gridSeparateur);

			contenantListeRaces = new Composite(page, SWT.NONE);
			contenantListeRaces.setBackground(couleurFond);
			GridLayout layoutContenantListeRaces = new GridLayout();
			layoutContenantListeRaces.makeColumnsEqualWidth = false;
			layoutContenantListeRaces.numColumns = 3;
			contenantListeRaces.setLayout(layoutContenantListeRaces);
			GridData gridDataContenantListeRaces = new GridData(GridData.FILL_HORIZONTAL
																| GridData.FILL_VERTICAL);
			gridDataContenantListeRaces.horizontalSpan = 5;
			contenantListeRaces.setLayoutData(gridDataContenantListeRaces);
			afficherListeRaces(contenantListeRaces, 0);

			gestionnaire.terminerRequete(resultat);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			Util.afficherErreur(getSite().getShell(), e);
		}
		page.setSize(page.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * @param parent
	 * @param idGroupe
	 */
	private void afficherListeRaces(Composite parent, int idGroupe) {
		Control[] listeControl = parent.getChildren();
		for (int i = 0; i < listeControl.length; i++) {
			listeControl[i].dispose();
		}

		List<List<Object>> listeRaces = racesParGroupe
				.get(new Integer(idGroupe));

		if (idGroupe > 0) {

			String nomGroupe = Nomenclature.getGroupe(idGroupe);

			Label labelNomGroupe = new Label(parent, SWT.WRAP);
			labelNomGroupe.setText(nomGroupe);
			labelNomGroupe.setBackground(couleurFond);
			labelNomGroupe.setFont(JFaceResources.getHeaderFont());

			GridData gridLabelNomGroupe = new GridData(	SWT.CENTER,
														SWT.TOP,
														true,
														false,
														3,
														1);
			labelNomGroupe.setLayoutData(gridLabelNomGroupe);

			for (int i = 0; i < listeRaces.size(); i++) {
				ArrayList donneesRace = (ArrayList) listeRaces.get(i);

				final int idRace = ((Integer) donneesRace.get(0)).intValue();
				String nomRace = (String) donneesRace.get(1);
				int etatFiche = ((Integer) donneesRace.get(2)).intValue();

				Hyperlink lien = toolkit.createHyperlink(parent,
						nomRace,
						SWT.CENTER);

				//				final Label race = new Label(parent, SWT.CENTER);
				//				race.setBackground(couleurFond);
				//				race.setText(nomRace);
				if (etatFiche < 4) {
					lien.setForeground(couleurGris);
					lien.setEnabled(false);
					lien.setUnderlined(false);
				} else {
					//					race.setForeground(couleurLien);
					//					race.setCursor(curseurMain);
					//					race.addListener(SWT.MouseUp, new Listener() {
					//
					//						public void handleEvent(Event e) {
					//							new NouvelleRace(getViewSite()
					//									.getWorkbenchWindow(), idRace).run();
					//						}
					//					});
					//					race.addListener(SWT.MouseEnter, new Listener() {
					//
					//						public void handleEvent(Event e) {
					//							race.setForeground(couleurLienSelection);
					//						}
					//					});
					//					race.addListener(SWT.MouseExit, new Listener() {
					//
					//						public void handleEvent(Event e) {
					//							race.setForeground(couleurLien);
					//						}
					//					});
					lien.addHyperlinkListener(new IHyperlinkListener() {

						public void linkEntered(HyperlinkEvent e) {
							// Rien
						}

						public void linkExited(HyperlinkEvent e) {
							// Rien
						}

						public void linkActivated(HyperlinkEvent e) {
							new NouvelleRace(getViewSite()
									.getWorkbenchWindow(), idRace).run();
						}
					});
				}
				GridData gridRace = new GridData(GridData.FILL_HORIZONTAL);
				gridRace.horizontalAlignment = GridData.CENTER;
				//				race.setLayoutData(gridRace);
				lien.setLayoutData(gridRace);
			}
		} else {
			Label cliquezPourUnGp = new Label(parent, SWT.NONE);
			cliquezPourUnGp.setText("Cliquez sur un groupe");
			cliquezPourUnGp.setBackground(couleurFond);
			GridData gridCliquezPourUnGp = new GridData(GridData.FILL_HORIZONTAL);
			gridCliquezPourUnGp.horizontalAlignment = GridData.CENTER;
			cliquezPourUnGp.setLayoutData(gridCliquezPourUnGp);
		}
		parent.layout();
		page.layout();
	}

	/**
	 * @return
	 */
	private GridData creerGridData() {
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = GridData.CENTER;
		return gridData;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		//		if (contenant != null && !contenant.isDisposed()) {
		//			contenant.setFocus();
		//		}
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		curseurMain.dispose();
		policeSoulignee.dispose();
		toolkit.dispose();
	}
}