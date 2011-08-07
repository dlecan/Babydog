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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import com.dlecan.phenochiot.consultation.BabyDogApp;
import com.dlecan.phenochiot.consultation.PluginBabyDog;
import com.dlecan.phenochiot.consultation.utils.Divers;
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
public class VueRace extends ViewPart {

	private static final String CHEMIN_PHOTOS = "icons/photos/";

	private static final String patternDate = "dd/MM/yyyy";

	private static SimpleDateFormat formateurDate = new SimpleDateFormat(patternDate);

	private Composite parent;

	private Cursor curseurZoomIn;

	private Cursor curseurZoomOut;

	private CTabFolder panneauOnglets;

	private OngletDonnees caracteristiques;

	private OngletDonnees standard;

	private OngletDonnees comparaison;

	protected Image imageTC;

	protected Image imagePC;

	protected Image imagePA;

	private ProgressBar barreProgression;

	private int idRace;

	private String nomRace;

	private int idGroupe;

	private String nomGroupe;

	private int section;

	private String nomSection;

	private int sssection;

	private String nomSsSection;

	private String autreNom;

	private String date;

	private int numeroStd;

	private FormToolkit toolkit;

	private Color couleur = new Color(Display.getCurrent(), 216, 223, 237);

	private Color couleur2 = new Color(Display.getCurrent(), 49, 106, 197);

	/**
	 * 
	 */
	public VueRace() {
		// Rien
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite nParent) {
		parent = nParent;

		toolkit = new FormToolkit(PluginBabyDog.getDefault()
				.getFormColors(parent.getDisplay()));

		idRace = Integer.parseInt(getViewSite().getSecondaryId());
		
		Logger.debug("Chargement race " + idRace);

		final Shell dialogProgressBar = new Shell(	parent.getShell(),
													SWT.APPLICATION_MODAL
				| SWT.BORDER);
		RowLayout layout = new RowLayout();
		layout.fill = true;
		layout.justify = true;
		layout.type = SWT.VERTICAL;
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		layout.spacing = 10;
		dialogProgressBar.setLayout(layout);

		Label info = new Label(dialogProgressBar, SWT.NONE);
		info.setText("Chargement de la race en cours ...");

		barreProgression = new ProgressBar(dialogProgressBar, SWT.HORIZONTAL);
		barreProgression.setMaximum(7);
		dialogProgressBar.pack();
		dialogProgressBar.setLocation(Divers.calculerCentreApplication(parent
				.getShell(), dialogProgressBar));
		dialogProgressBar.open();

		curseurZoomIn = new Cursor(	parent.getDisplay(),
									PluginBabyDog.getDefault()
											.getImage(PluginBabyDog.ZOOM_IN)
											.getImageData(),
									0,
									0);
		curseurZoomOut = new Cursor(parent.getDisplay(),
									PluginBabyDog.getDefault()
											.getImage(PluginBabyDog.ZOOM_OUT)
											.getImageData(),
									0,
									0);

		avancerBarreProgression();

		try {
			GestionnaireAccesDonnees gestionnaire = GestionnaireAccesDonnees
					.getInstance(GestionnaireAccesDonnees.TYPE_CLIENT);

			List<Integer> parametres = new ArrayList<Integer>();
			parametres.add(new Integer(idRace));

			ResultSet resultat = gestionnaire.executerSelect(8, parametres);

			resultat.next();

			nomRace = resultat.getString("nom_race");
			autreNom = resultat.getString("autre_nom");
			idGroupe = resultat.getInt("groupe");
			nomGroupe = Nomenclature.getGroupe(getIdGroupe());
			section = resultat.getInt("section");
			nomSection = Nomenclature.getSection(getIdGroupe(), getSection());
			sssection = resultat.getInt("ss_section");
			nomSsSection = Nomenclature.getSsSection(getIdGroupe(),
					getSection(),
					getSssection());
			Date dateLocale = resultat.getDate("std_date");
			date = formateurDate.format(dateLocale);
			numeroStd = resultat.getInt("std_numero");

			setPartName(nomRace);
			String chaine = nomRace;
			if (!"".equals(autreNom)) {
				chaine += " - " + autreNom;
			}
			setTitleToolTip(chaine);

			gestionnaire.terminerRequete(resultat);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			Util.afficherErreur(parent.getShell(), e);
		}

		avancerBarreProgression();

		chargerPhotos();

		creerOnglets();

		barreProgression.dispose();
		dialogProgressBar.dispose();
		Logger.debug("Fin chargement race " + idRace);
	}

	/**
	 * 
	 */
	private void avancerBarreProgression() {
		int position = barreProgression.getSelection();
		barreProgression.setSelection(position + 1);
	}

	/**
	 * 
	 */
	private void creerOnglets() {
		panneauOnglets = new CTabFolder(parent, SWT.TOP | SWT.FLAT);
		panneauOnglets.setSelectionBackground(couleur);
		panneauOnglets.setSelectionForeground(couleur2);
		panneauOnglets.setSimple(false);
		panneauOnglets.setUnselectedImageVisible(false);

		chargerOnglets(panneauOnglets);

		panneauOnglets.setSelection(0);
	}

	/**
	 * @param nPanneauOnglets
	 */
	private void chargerOnglets(CTabFolder nPanneauOnglets) {
		caracteristiques = new OngletCaracteristiques();
		caracteristiques.setIdRace(idRace);
		caracteristiques.creerOnglet(nPanneauOnglets, this);

		avancerBarreProgression();

		standard = new OngletStandardAdulte();
		standard.setIdRace(idRace);
		standard.creerOnglet(nPanneauOnglets, this);

		avancerBarreProgression();

		comparaison = new OngletComparaison();
		comparaison.setIdRace(idRace);
		comparaison.creerOnglet(nPanneauOnglets, this);

		avancerBarreProgression();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// Rien
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		caracteristiques.dispose();
		comparaison.dispose();
		standard.dispose();

		curseurZoomIn.dispose();
		curseurZoomOut.dispose();

		imageTC.dispose();

		imagePC.dispose();

		imagePA.dispose();

		couleur.dispose();
		couleur2.dispose();
	}

	/**
	 * @return Retourne curseurZoomIn.
	 */
	public final Cursor getCurseurZoomIn() {
		return curseurZoomIn;
	}

	/**
	 * @return Retourne curseurZoomOut.
	 */
	public final Cursor getCurseurZoomOut() {
		return curseurZoomOut;
	}

	/**
	 * @return Retourne parent.
	 */
	public final Composite getParent() {
		return parent;
	}

	/**
	 * 
	 */
	private void chargerPhotos() {
		DecimalFormat formateur = new DecimalFormat("000");
		String chiffreFormate = formateur.format(idRace);
		
		Logger.debug("Avant chargement image");

		Logger.debug("Chargement image 1");
		try {
			URL cheminImageTC = Platform.resolve(Platform
					.getBundle(BabyDogApp.PLUGIN_ID)
					.getEntry(CHEMIN_PHOTOS + chiffreFormate + "_TC.jpg"));
			
			imageTC = new Image(Display.getCurrent(), cheminImageTC.getFile());
			Logger.debug("Image 1 chargée");

			avancerBarreProgression();
			
			Logger.debug("Chargement image 2");
			URL cheminImagePC = Platform.resolve(Platform.getBundle(BabyDogApp.PLUGIN_ID)
					.getEntry(CHEMIN_PHOTOS + chiffreFormate + "_PC.jpg"));
			imagePC = new Image(Display.getCurrent(), cheminImagePC.getFile());
			Logger.debug("Image 2 chargée");

			avancerBarreProgression();
			
			Logger.debug("Chargement image 3");
			URL cheminImagePA = Platform.resolve(Platform.getBundle(BabyDogApp.PLUGIN_ID)
					.getEntry(CHEMIN_PHOTOS + chiffreFormate + "_PA.jpg"));
			imagePA = new Image(Display.getCurrent(), cheminImagePA.getFile());
			Logger.debug("Image 3 chargée");
	
			avancerBarreProgression();

		} catch (Exception e) {
			// TODO: handle exception
		}		
	}

	/**
	 * @return Retourne imagePA.
	 */
	public final Image getImagePA() {
		return imagePA;
	}

	/**
	 * @return Retourne imagePC.
	 */
	public final Image getImagePC() {
		return imagePC;
	}

	/**
	 * @return Retourne imageTC.
	 */
	public final Image getImageTC() {
		return imageTC;
	}

	/**
	 * @return Retourne idRace.
	 */
	public final int getIdRace() {
		return idRace;
	}

	/**
	 * @return Retourne nomRace.
	 */
	public final String getNomRace() {
		return nomRace;
	}

	/**
	 * @return Retourne section.
	 */
	public final int getSection() {
		return section;
	}

	/**
	 * @return Retourne sssection.
	 */
	public final int getSssection() {
		return sssection;
	}

	/**
	 * @return Retourne idGroupe.
	 */
	public final int getIdGroupe() {
		return idGroupe;
	}

	/**
	 * @return Retourne autreNom.
	 */
	public final String getAutreNom() {
		return autreNom;
	}

	/**
	 * @return Retourne nomGroupe.
	 */
	public final String getNomGroupe() {
		return nomGroupe;
	}

	/**
	 * @return Retourne nomSection.
	 */
	public final String getNomSection() {
		return nomSection;
	}

	/**
	 * @return Retourne nomSsSection.
	 */
	public final String getNomSsSection() {
		return nomSsSection;
	}

	/**
	 * @return Retourne date.
	 */
	public final String getDate() {
		return date;
	}

	/**
	 * @return Retourne numeroStd.
	 */
	public final int getNumeroStd() {
		return numeroStd;
	}

	/**
	 * @return Retourne toolkit.
	 */
	public final FormToolkit getToolkit() {
		return toolkit;
	}
}