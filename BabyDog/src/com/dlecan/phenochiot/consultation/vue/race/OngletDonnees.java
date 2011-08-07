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

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.dlecan.phenochiot.consultation.BabyDogApp;
import com.dlecan.phenochiot.consultation.PluginBabyDog;
import com.dlecan.phenochiot.consultation.utils.Logger;
import com.dlecan.phenochiot.consultation.utils.SWTImageCanvas;
import com.dlecan.phenochiot.framework.xml.AnnexeDOM;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public abstract class OngletDonnees {

	private static final int MODE_IMAGES_PETITES = 0;

	private static final int MODE_IMAGE_PLEIN_ECRAN = 1;

	private static final int MODE_IMAGE_DEFAUT = MODE_IMAGES_PETITES;

	private static final String STYLE_HTML_DEBUT = "<style type=\"text/css\">\n"
													+ "body {"
													+ "	font-family: Tahoma;"
													+ "}\n"
													+ "legend {"
													+ "	font-size: medium;"
													+ "	font-weight: bold;"
													+ "	text-transform:uppercase;"
													+ "}\n"
													+ "fieldset {"
													+ "	padding: 2px 0;"
													+ "}\n"
													+ ".sstitre  {"
													+ "	font-size: x-small;"
													+ "	font-weight: bold;"
													+ "}\n";

	private static final String STYLE_HTML_FIN = "ul {"
													+ "	font-size: 11px;"
													+ " text-align: justify;"
													+ "}\n"
													+ ".age {"
													+ "	font-size: 11px;"
													+ " padding-left: 5px;"
													+ "}"
													+ "</style>";

	protected static final int COLONNE_GAUCHE = 1;

	protected static final int COLONNE_DROITE = 2;

	protected static final int DEUX_COLONNES = COLONNE_GAUCHE | COLONNE_DROITE;

	protected static final String pattern = "###.##";

	protected static final DecimalFormat formateurNombres = (DecimalFormat) NumberFormat
			.getNumberInstance(new Locale("fr_FR"));

	static {
		formateurNombres.applyPattern(pattern);
	}

	/**
	 *  
	 */
	protected Browser navigateur;

	/**
	 *  
	 */
	private int modeImagesCourant = MODE_IMAGE_DEFAUT;

	/**
	 * Instance de la vue d'origine
	 */
	protected VueRace vueRace;

	/**
	 *  
	 */
	protected Document document = null;

	/**
	 *  
	 */
	private Composite contenantOnglet;

	/**
	 *  
	 */
	protected CTabFolder parent;

	/**
	 *  
	 */
	private CTabItem onglet;

	/**
	 *  
	 */
	private Font policeOnglet;

	/**
	 *  
	 */
	protected int idRace;

	/**
	 * Crée l'onglet
	 * 
	 * @param nParent le conteneur d'onglets
	 * @param nVueRace la vue race d'origine
	 */
	public void creerOnglet(CTabFolder nParent, VueRace nVueRace) {
		vueRace = nVueRace;
		parent = nParent;

		chargerFichierFiche();

		onglet = new CTabItem(nParent, SWT.NONE);
		FontData[] donneesPolice = onglet.getFont().getFontData();
		for (int i = 0; i < donneesPolice.length; i++) {
			donneesPolice[i].setHeight(10);
			donneesPolice[i].setStyle(SWT.BOLD);
		}

		policeOnglet = new Font(nParent.getDisplay(), donneesPolice);
		onglet.setFont(policeOnglet);
		onglet.setText(getTitre());
//		onglet.setImage(getImage());

		creerContenuPetitesImages();
	}

	/**
	 *  
	 */
	private void initCompositeContenant() {
		contenantOnglet = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		contenantOnglet.setLayout(layout);
		onglet.setControl(contenantOnglet);
	}

	/**
	 *  
	 */
	private void creerContenuPetitesImages() {
		initCompositeContenant();

		FormToolkit toolkit = vueRace.getToolkit();
		Form form = toolkit.createForm(contenantOnglet);
		form.setBackgroundImage(PluginBabyDog.getDefault()
				.getImage(PluginBabyDog.FOND));
		form.setText(getNomRaceComplet());

		form.getBody().setLayout(getLayoutOnglet());

		form.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
										| GridData.FILL_VERTICAL));

		creerOngletSpecifique(form.getBody());
	}

	/**
	 * @return Le titre de l'onglet
	 */
	protected abstract String getTitre();

	/**
	 * @return Le nom complet de la race
	 */
	protected final String getNomRaceComplet() {
		String donneesRace = vueRace.getNomRace();
		if (vueRace.getAutreNom().length() > 0) {
			donneesRace += " - ";
			donneesRace += vueRace.getAutreNom();
		}
		return donneesRace;
	}

	/**
	 * @return L'image de l'onget
	 */
	protected abstract Image getImage();

	/**
	 * Crée l'onglet
	 * 
	 * @param form
	 */
	public abstract void creerOngletSpecifique(Composite form);

	/**
	 * @return
	 */
	protected Layout getLayoutOnglet() {
		Layout layoutGlobal = null;

		if (modeImagesCourant == MODE_IMAGES_PETITES) {
			layoutGlobal = new GridLayout();
			((GridLayout) layoutGlobal).numColumns = 2;
			((GridLayout) layoutGlobal).horizontalSpacing = 0;
			((GridLayout) layoutGlobal).verticalSpacing = 0;
		} else {
			layoutGlobal = new FillLayout();
		}
		return layoutGlobal;
	}

	/**
	 * @param image
	 */
	protected void chargerImageSeule(final Image image) {
		modeImagesCourant = MODE_IMAGE_PLEIN_ECRAN;

		initCompositeContenant();

		Composite stockageImage = new Composite(contenantOnglet, SWT.NONE);
		stockageImage.setBackground(JFaceColors
				.getBannerBackground(contenantOnglet.getDisplay()));
		stockageImage.setLayout(new GridLayout());
		stockageImage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
													| GridData.FILL_VERTICAL));

		final SWTImageCanvas canvasImageSeule = new SWTImageCanvas(stockageImage);
		canvasImageSeule.setSourceImage(image);

		canvasImageSeule.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				modeImagesCourant = MODE_IMAGE_DEFAUT;
				contenantOnglet.dispose();
				creerContenuPetitesImages();
				contenantOnglet.layout();
			}
		});

		GridData gridData = new GridData(GridData.FILL_BOTH);
		canvasImageSeule.setLayoutData(gridData);
		canvasImageSeule.setCursor(vueRace.getCurseurZoomOut());

		contenantOnglet.layout();
	}

	/**
	 * 
	 *  
	 */
	public void dispose() {
		if (navigateur != null) {
			navigateur.dispose();
		}
		//		policeOnglet.dispose();
	}

	/**
	 * @param stockageImages
	 * @param imageGrande
	 * @return
	 */
	protected Canvas creerCanvasImage(Composite stockageImages,
			final Image imageGrande) {
		final Canvas canvasImage = new Canvas(stockageImages, SWT.NONE);
		canvasImage.setBackground(JFaceColors
				.getBannerBackground(stockageImages.getDisplay()));
		canvasImage.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				Rectangle dimensionsRelles = imageGrande.getBounds();
				Rectangle dimensionsConteneur = canvasImage.getBounds();
				Rectangle dimensionsRetaillee = calculerDimension(dimensionsRelles,
						dimensionsConteneur);

				e.gc.fillRectangle(dimensionsRetaillee);
				e.gc.drawImage(imageGrande,
						0,
						0,
						dimensionsRelles.width,
						dimensionsRelles.height,
						0,
						0,
						dimensionsRetaillee.width,
						dimensionsRetaillee.height);
				e.gc.drawRectangle(dimensionsRetaillee);
			}
		});
		canvasImage.addListener(SWT.MouseUp, new Listener() {

			public void handleEvent(Event event) {
				contenantOnglet.dispose();
				chargerImageSeule(imageGrande);
			}
		});

		canvasImage.setCursor(vueRace.getCurseurZoomIn());
		return canvasImage;
	}

	/**
	 *  
	 */
	public void setFocus() {
		// Rien
	}

	/**
	 * @param nIdRace
	 */
	public final void setIdRace(int nIdRace) {
		this.idRace = nIdRace;
	}

	protected abstract String getFichierDescriptionFiche();

	private void chargerFichierFiche() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = null;

		try {
			parser = factory.newDocumentBuilder();
			document = parser.parse(new InputSource(Platform
					.getBundle(BabyDogApp.PLUGIN_ID)
					.getEntry("conf/" + getFichierDescriptionFiche())
					.openStream()));
		} catch (MalformedURLException e) {
			Logger.error(e.getMessage(), e);
		} catch (SAXException e) {
			Logger.error(e.getMessage(), e);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			Logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @param resultat
	 * @return Données formatées
	 * @throws SQLException
	 */
	protected StringBuffer creerDonneesGenerales(ResultSet resultat)
			throws SQLException {
		Element comparaison = document.getDocumentElement();
		NodeList listeChamps = comparaison.getElementsByTagName("champ");

		StringBuffer html = new StringBuffer();

		int nbChamps = listeChamps.getLength();

		for (int i = 0; i < nbChamps; i++) {
			StringBuffer sbChamps = new StringBuffer();
			boolean ajouterCritere = false;

			Element noeudChamp = (Element) listeChamps.item(i);
			String nomChamp = noeudChamp.getAttribute("nom");

			sbChamps.append("<fieldset>\n<legend align=\"left\">");
			sbChamps.append(nomChamp);
			sbChamps.append("</legend>\n");

			NodeList listeCriteres = noeudChamp
					.getElementsByTagName("critere");

			int nbCriteres = listeCriteres.getLength();

			for (int j = 0; j < nbCriteres; j++) {
				Element critere = (Element) listeCriteres.item(j);
				String nomCritere = critere.getAttribute("nom");
				String champSQL = AnnexeDOM.trouveTexte(critere);
				String valeurChampSQL = resultat.getString(champSQL);

				if (valeurChampSQL != null && !"".equals(valeurChampSQL)) {
					ajouterCritere = true;
					sbChamps.append("<p class=\"corps\">\n");

					if (!"".equals(nomCritere)) {
						sbChamps.append("<span class=\"sstitre\">");
						sbChamps.append(nomCritere);
						sbChamps.append(" : </span>\n");
					}

					NodeList listeSsCriteres = critere
							.getElementsByTagName("sscritere");

					sbChamps.append(valeurChampSQL);

					int nbSsCriteres = listeSsCriteres.getLength();

					if (nbSsCriteres > 0) {
						sbChamps.append("<ul>\n");
					}
					for (int k = 0; k < nbSsCriteres; k++) {
						Element ssCritere = (Element) listeSsCriteres.item(k);
						String nomSsCritere = ssCritere.getAttribute("nom");
						String champSQLSsCritere = AnnexeDOM
								.trouveTexte(ssCritere);
						String valeurChampSQLSsCritere = resultat
								.getString(champSQLSsCritere);

						if (valeurChampSQLSsCritere != null
							&& !"".equals(valeurChampSQLSsCritere)) {
							sbChamps.append("<li>\n");
							sbChamps.append(nomSsCritere);
							sbChamps.append(" : ");
							sbChamps.append(valeurChampSQLSsCritere);
							sbChamps.append("</li>\n");
						}
					}
					if (nbSsCriteres > 0) {
						sbChamps.append("</ul>\n");
					}
					sbChamps.append("</p>\n");
				}
			}
			sbChamps.append("</fieldset>\n");
			if (ajouterCritere) {
				html.append(sbChamps);
			}
		}
		return html;
	}

	/**
	 * @param critere
	 * @param adulteMaleMin
	 * @param adulteMaleMax
	 * @param adulteFemMin
	 * @param adulteFemMax
	 * @param chiotMin
	 * @param chiotMax
	 * @param unites
	 * @param colonnes
	 * @return
	 */
	protected StringBuffer creerMensuration(String critere,
			float adulteMaleMin, float adulteMaleMax, float adulteFemMin,
			float adulteFemMax, float chiotMin, float chiotMax, String unites,
			int colonnes) {

		String strAdulteMaleMin = formateurNombres.format(adulteMaleMin);
		String strAdulteMaleMax = formateurNombres.format(adulteMaleMax);
		String strAdulteFemMax = formateurNombres.format(adulteFemMax);
		String strAdulteFemMin = formateurNombres.format(adulteFemMin);
		String strChiotMin = formateurNombres.format(chiotMin);
		String strChiotMax = formateurNombres.format(chiotMax);

		StringBuffer retour = new StringBuffer();

		String critereComplet = "<span class=\"sstitre\">"
								+ critere
								+ "</span>";

		String chaineInferieur = "A partir de ";
		String chaineSuperieur = "Jusqu'à ";
		String chaineDe = "De ";
		String chaineA = " à ";

		// Données du chiot
		if (colonnes == COLONNE_GAUCHE || colonnes == DEUX_COLONNES) {
			retour.append("<p class=\"corps\">\n");
			retour.append(critereComplet);

			if (chiotMax == 0) {
				retour.append(strChiotMin);
				retour.append(unites);
			} else if (chiotMax != 0 && chiotMin != 0) {
				retour.append(chaineDe);
				retour.append(strChiotMin);
				retour.append(chaineA);
				retour.append(strChiotMax);
				retour.append(unites);
			} else {
				// Erreur !
			}
			retour.append("</p>\n");
		}

		// Données de l'adulte
		if (colonnes == COLONNE_DROITE || colonnes == DEUX_COLONNES) {
			retour.append("<p class=\"corps\">\n");
			retour.append(critereComplet);

			if (adulteFemMax == adulteFemMin
				&& adulteFemMax == adulteMaleMax
				&& adulteFemMax == adulteMaleMin) {
				if (adulteFemMax == 0) {
					retour.append(" -<br/>");
				} else {
					retour.append(strAdulteFemMax);
					retour.append(unites);
				}
			} else if (adulteFemMax == adulteMaleMax
						&& adulteFemMin == adulteMaleMin) {
				if (adulteFemMin == 0) {
					retour.append(chaineSuperieur);
					retour.append(strAdulteFemMax);
					retour.append(unites);
				} else {
					retour.append(chaineDe);
					retour.append(strAdulteMaleMin);
					retour.append(chaineA);
					retour.append(strAdulteMaleMax);
					retour.append(unites);
				}
			} else {
				retour.append("<br/>");
				retour.append("Femelle : ");
				if (adulteFemMax == adulteFemMin) {
					retour.append(strAdulteFemMax);
					retour.append(unites);
				} else {
					if (adulteFemMax == 0) {
						retour.append(chaineInferieur);
						retour.append(strAdulteFemMin);
						retour.append(unites);
					} else if (adulteFemMin == 0) {
						retour.append(chaineSuperieur);
						retour.append(strAdulteFemMax);
						retour.append(unites);
					} else {
						retour.append(chaineDe);
						retour.append(strAdulteFemMin);
						retour.append(chaineA);
						retour.append(strAdulteFemMax);
						retour.append(unites);
					}
				}
				retour.append("Mâle : ");
				if (adulteMaleMax == adulteMaleMin) {
					retour.append(strAdulteMaleMax);
					retour.append(unites);
				} else {
					if (adulteMaleMax == 0) {
						retour.append(chaineInferieur);
						retour.append(strAdulteMaleMin);
						retour.append(unites);
					} else if (adulteMaleMin == 0) {
						retour.append(chaineSuperieur);
						retour.append(strAdulteMaleMax);
						retour.append(unites);
					} else {
						retour.append(chaineDe);
						retour.append(strAdulteMaleMin);
						retour.append(chaineA);
						retour.append(strAdulteMaleMax);
						retour.append(unites);
					}
				}
			}
			retour.append("</p>\n");
		}

		return retour;
	}

	/**
	 * @return Le résultat HTML
	 * @param resultat
	 * @throws SQLException
	 */
	protected StringBuffer creerAffichageAgeChiot(ResultSet resultat)
			throws SQLException {
		int ageChiot = resultat.getInt("chiot_age");
		StringBuffer retour = new StringBuffer();
		retour.append("<span class=\"age\">Données recueillies à ");
		retour.append(ageChiot);
		retour.append(" jours</span>");
		return retour;
	}

	/**
	 * @param form
	 * @param affNumStd
	 * @return
	 */
	protected Composite creerNomenclatureRace(Composite form, boolean affNumStd) {
		Composite composite = new Composite(form, SWT.NONE);
		composite.setBackground(JFaceColors
				.getBannerBackground(contenantOnglet.getDisplay()));

		GridData gridDataComposite = new GridData(GridData.HORIZONTAL_ALIGN_FILL
													| GridData.VERTICAL_ALIGN_FILL);
		gridDataComposite.widthHint = 300;

		composite.setLayoutData(gridDataComposite);
		composite.setLayout(new GridLayout());

		creerLabelNomenclatureRace(composite, "Groupe "
												+ vueRace.getIdGroupe()
												+ " : "
												+ vueRace.getNomGroupe());

		creerLabelNomenclatureRace(composite, "Section "
												+ vueRace.getSection()
												+ " : "
												+ vueRace.getNomSection());

		if (affNumStd) {
			Label label = new Label(composite, SWT.WRAP);
			label.setBackground(composite.getBackground());
			label.setText("\nStandard FCI n°"
							+ vueRace.getNumeroStd()
							+ " du "
							+ vueRace.getDate());
			GridData gridDataLabel = generateurGridDataWrap();
			label.setLayoutData(gridDataLabel);
		}

		return composite;
	}

	/**
	 * @param parentLocal
	 * @param valeur
	 * @return
	 */
	private Label creerLabelNomenclatureRace(Composite parentLocal,
			String valeur) {
		Label label = new Label(parentLocal, SWT.WRAP);
		label.setBackground(parentLocal.getBackground());
		label.setText(valeur);
		GridData gridDataLabel = generateurGridDataWrap();
		label.setLayoutData(gridDataLabel);
		return label;
	}

	/**
	 * @return
	 */
	private GridData generateurGridDataWrap() {
		GridData gridDataLabel = new GridData(	SWT.FILL,
												SWT.TOP,
												true,
												false,
												1,
												1);
		return gridDataLabel;
	}

	protected abstract String getTailleParagraphe();

	protected String getStyleHTML() {
		return STYLE_HTML_DEBUT + getTailleParagraphe() + STYLE_HTML_FIN;
	}

	/**
	 * @param dimensionsActuelle
	 * @param dimensionsConteneur
	 * @return
	 */
	public Rectangle calculerDimension(Rectangle dimensionsActuelle,
			Rectangle dimensionsConteneur) {

		float ratio1 = (float) dimensionsActuelle.width
						/ (float) dimensionsConteneur.width;
		int nouvelleHauteur = Math.round(dimensionsActuelle.height / ratio1);

		float ratio2 = (float) dimensionsActuelle.height
						/ (float) dimensionsConteneur.height;
		int nouvelleLargeur = Math.round(dimensionsActuelle.width / ratio2);

		Rectangle retour = null;

		if (nouvelleHauteur > dimensionsConteneur.height) {
			retour = new Rectangle(	0,
									0,
									nouvelleLargeur,
									dimensionsConteneur.height
			- dimensionsConteneur.y);
		} else {
			retour = new Rectangle(	0,
									0,
									dimensionsConteneur.width
			- dimensionsConteneur.x	,
									nouvelleHauteur);
		}
		return retour;
	}

	/**
	 * Empêche l'affichage du bouton droit dans le navigateur
	 */
	protected void stopBoutonDroit() {
		navigateur.addListener(SWT.MenuDetect, new Listener() {

			public void handleEvent(Event event) {
				event.doit = false;
			}
		});
	}
}