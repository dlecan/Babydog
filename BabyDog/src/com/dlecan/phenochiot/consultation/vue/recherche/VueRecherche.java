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


package com.dlecan.phenochiot.consultation.vue.recherche;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import com.dlecan.phenochiot.consultation.PluginBabyDog;
import com.dlecan.phenochiot.consultation.action.NouvelleRace;
import com.dlecan.phenochiot.consultation.utils.Logger;
import com.dlecan.phenochiot.framework.Util;
import com.dlecan.phenochiot.framework.db.GestionnaireAccesDonnees;
import com.dlecan.phenochiot.framework.exception.ExceptionTechnique;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class VueRecherche extends ViewPart {

	private static final int[] SASH_WEIGHTS_AVANCEE = new int[]{55, 45};

	private static final int[] SASH_WEIGHTS_SIMPLE = new int[]{20, 80};

	private static final int DELAI_INIT_RECHERCHE = 500;

	private static final int ORIENTATION_VUE_VERTICALE = 0;

	private static final int ORIENTATION_VUE_HORIZONTALE = 1;

	protected static final int EN_GROUPE = 1;

	protected static final int SANS_GROUPE = 0;

	private int orientation = SANS_GROUPE;

	/**
	 * L'orientation courante, soit <code>ORIENTATION_VUE_HORIZONTALE</code>
	 * soit <code>ORIENTATION_VUE_VERTICALE</code>.
	 */
	private int orientationCourante;

	private TreeViewer viewer;

	private Action actionChangerOrientation;

	private Action actionReduireTout;

	private Action actionEtendreTout;

	private Action remettreZeroRecherche;

	private Composite parent;

	private SashForm sashForm;

	private Label nombreResultatsRecherche;

	private Text zoneRecherche;

	private boolean zoneTexteSelectionnee = false;

	private Runnable lancerRechercheSimple = null;

	private Runnable lancerRechercheAvancee = null;

	private Listener listenerRechercheAvancee;

	private List<Button> listeBoutonsRobe = new ArrayList<Button>();

	private List<Long> listeValeursBoutonsRobe = new ArrayList<Long>();

	private CCombo comboHauteur;

	private CCombo comboSI;

	private CCombo comboPoids;

	private CCombo comboMuseau;

	private CCombo comboYeuxTaille;

	private CCombo comboYeuxEcartement;

	private CCombo comboTruffe;

	private CCombo comboOreilles;

	private CCombo comboQueue;

	private CCombo comboRobe;

	private Button boutonPeau;

	private boolean andAjoute = false;

	private CTabItem ongletRechercheAvancee;

	private CTabItem ongletRerchercheSimple;

	private FormToolkit toolkit;

	private Form form;

	/**
	 * The constructor.
	 */
	public VueRecherche() {
		// Rien
	}

	/**
	 * This is a callback that will allow us to create the viewer and
	 * initialize it.
	 * 
	 * @param nParent
	 */
	public void createPartControl(Composite nParent) {
		toolkit = new FormToolkit(nParent.getDisplay());
		form = toolkit.createForm(nParent);
		parent = form.getBody();

		addResizeListener(parent);
		construireActions();

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setNombreColonnes(gridLayout);
		parent.setLayout(gridLayout);

		sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		ViewForm haut = new ViewForm(sashForm, SWT.NONE);

		CTabFolder fTabFolder = new CTabFolder(haut, SWT.TOP);
		haut.setContent(fTabFolder);

		ongletRerchercheSimple = construireRechercheSimple(fTabFolder);
		ongletRechercheAvancee = construireRechercheAvancee(fTabFolder);
		fTabFolder.setSelection(ongletRechercheAvancee);

		addSelectionListener(fTabFolder);

		ViewForm bas = new ViewForm(sashForm, SWT.NONE);
		nombreResultatsRecherche = new Label(bas, SWT.NONE);
		bas.setTopLeft(nombreResultatsRecherche);

		ToolBar barreOutils = new ToolBar(bas, SWT.FLAT | SWT.WRAP);
		bas.setTopCenter(barreOutils);
		ToolBarManager gestionnaireBarreOutils = new ToolBarManager(barreOutils);
		gestionnaireBarreOutils.add(remettreZeroRecherche);
		gestionnaireBarreOutils.add(actionReduireTout);
		gestionnaireBarreOutils.add(actionEtendreTout);
		gestionnaireBarreOutils.add(actionChangerOrientation);
		gestionnaireBarreOutils.update(true);

		Tree arbre = toolkit.createTree(bas, SWT.NULL);
		toolkit.paintBordersFor(bas);

		viewer = new TreeViewer(arbre);

		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new TrieurGroupe());
		lancerRechercheSimple.run();
		gererMenuContextuel();
		gererDoubleClicSurArbre();

		bas.setContent(arbre);

		sashForm.setWeights(SASH_WEIGHTS_AVANCEE);
	}

	/**
	 * @param textViewer
	 * @return
	 */
	//	private static Completion ajouterContentAssist(final TextViewer
	// textViewer) {
	//		ContentAssistant assistant = new ContentAssistant();
	//		final Completion completion = new Completion();
	//		assistant.setContentAssistProcessor(completion,
	//				IDocument.DEFAULT_CONTENT_TYPE);
	//
	//		assistant.enableAutoActivation(true);
	//		assistant.setAutoActivationDelay(300);
	//		assistant
	//				.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
	//		assistant
	//				.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
	//
	//		assistant.install(textViewer);
	//		return completion;
	//	}
	/**
	 * @param fTabFolder
	 */
	private void addSelectionListener(CTabFolder fTabFolder) {
		fTabFolder.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				if (ongletRerchercheSimple.equals(e.item)) {
					setFocus();
					sashForm.setWeights(SASH_WEIGHTS_SIMPLE);
					parent.layout();
				} else {
					sashForm.setWeights(SASH_WEIGHTS_AVANCEE);
					parent.layout();
				}
			}
		});
	}

	/**
	 * @param fTabFolder
	 * @return
	 */
	private CTabItem construireRechercheSimple(CTabFolder fTabFolder) {
		CTabItem onglet = new CTabItem(fTabFolder, SWT.NONE);
		onglet.setText("Simple");
		onglet.setToolTipText("Recherche simple");

		Composite composite = toolkit.createComposite(fTabFolder);
		GridLayout layoutComposite = new GridLayout();
		layoutComposite.marginHeight = 4;
		layoutComposite.marginWidth = 4;
		layoutComposite.verticalSpacing = 15;
		composite.setLayout(layoutComposite);

		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
												| GridData.FILL_VERTICAL));
		Label texteRecherche = toolkit.createLabel(composite,
				"Nom de la race",
				SWT.CENTER);
		texteRecherche.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		zoneRecherche = toolkit.createText(composite, "");
		zoneRecherche.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toolkit.paintBordersFor(composite);

		//		final Completion completion = ajouterContentAssist(zoneRecherche);

		Listener listenerRechercheSimple = new Listener() {

			public void handleEvent(Event e) {
				if (zoneRecherche.getText().length() > 0
					&& !zoneTexteSelectionnee) {
					zoneTexteSelectionnee = true;
					zoneRecherche.setSelection(0, zoneRecherche.getText()
							.length());
				}
			}
		};

		zoneRecherche.addListener(SWT.FocusIn, listenerRechercheSimple);

		zoneRecherche.addListener(SWT.MouseDown, listenerRechercheSimple);

		zoneRecherche.addListener(SWT.MouseDoubleClick,
				listenerRechercheSimple);

		zoneRecherche.addListener(SWT.KeyUp, new Listener() {

			public void handleEvent(Event e) {
				zoneTexteSelectionnee = false;
				if (e.character == SWT.CR) {
					// Execution immédiate du timer :)
					Display.getCurrent().timerExec(0, lancerRechercheSimple);
				} else if (e.keyCode == SWT.ARROW_UP
							|| e.keyCode == SWT.ARROW_DOWN
							|| e.keyCode == SWT.ARROW_LEFT
							|| e.keyCode == SWT.ARROW_RIGHT
							|| e.keyCode == SWT.ALT
							|| e.keyCode == SWT.CTRL
							|| e.keyCode == SWT.CAPS_LOCK
							|| e.keyCode == SWT.SHIFT
							|| e.keyCode == SWT.ESC
							|| e.keyCode == SWT.TAB) {
					Display.getCurrent().timerExec(-1, lancerRechercheSimple);
					return;
				} else {
					Display.getCurrent().timerExec(DELAI_INIT_RECHERCHE,
							lancerRechercheSimple);
				}
			}
		});

		onglet.setControl(composite);

		lancerRechercheSimple = new Runnable() {

			public void run() {
				//				completion.ajouterElement(zoneRecherche.getDocument().get());
				viewer.setInput(lancerRecherche(0));
				repositionnerArbre();
			}
		};

		return onglet;
	}

	/**
	 * @param fTabFolder
	 * @return
	 */
	private CTabItem construireRechercheAvancee(CTabFolder fTabFolder) {
		CTabItem onglet = new CTabItem(fTabFolder, SWT.NONE);
		onglet.setText("Avancée");
		onglet.setToolTipText("Recherche avancée");

		Composite composite = toolkit.createComposite(fTabFolder);
		GridLayout layoutComposite = new GridLayout();
		layoutComposite.makeColumnsEqualWidth = true;
		layoutComposite.numColumns = 2;
		layoutComposite.marginHeight = 2;
		layoutComposite.marginWidth = 2;
		layoutComposite.horizontalSpacing = 2;
		layoutComposite.verticalSpacing = 3;
		composite.setLayout(layoutComposite);

		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
												| GridData.VERTICAL_ALIGN_FILL
												| GridData.GRAB_HORIZONTAL
												| GridData.GRAB_VERTICAL));

		listenerRechercheAvancee = new Listener() {

			public void handleEvent(Event e) {
				gererEventRechercheAvancee();
			}

		};
		creerLabelRechercheAvancee(composite,
				"Hauteur au garrot",
				"Hauteur au garrot (en cm)");
		comboHauteur = creerComboRechercheAvancee(composite);
		comboHauteur.add("");
		comboHauteur.add("H < 25");
		comboHauteur.add("H : [25 - 35]");
		comboHauteur.add("H > 35");
		comboHauteur.setToolTipText("en cm");

		creerLabelRechercheAvancee(composite,
				"Longueur SI",
				"Longueur scapulo-ischiale (en cm)");
		comboSI = creerComboRechercheAvancee(composite);
		comboSI.add("");
		comboSI.add("L < 25");
		comboSI.add("L : [25 - 35]");
		comboSI.add("L > 35");
		comboSI.setToolTipText("en cm");

		creerLabelRechercheAvancee(composite, "Poids", "Poids (en kg)");
		comboPoids = creerComboRechercheAvancee(composite);
		comboPoids.add("");
		comboPoids.add("P < 5");
		comboPoids.add("P : [5 - 9]");
		comboPoids.add("P > 9");
		comboPoids.setToolTipText("en kg");

		creerLabelRechercheAvancee(composite, "Oreilles");
		comboOreilles = creerComboRechercheAvancee(composite);
		comboOreilles.add("");
		comboOreilles.add("dressées");
		comboOreilles.add("semi-tombantes");
		comboOreilles.add("tombantes");

		creerLabelRechercheAvancee(composite, "Queue");
		comboQueue = creerComboRechercheAvancee(composite);
		comboQueue.add("");
		comboQueue.add("courte");
		comboQueue.add("longue");

		Section sectionRobe = toolkit.createSection(composite,
				ExpandableComposite.CLIENT_INDENT);
		sectionRobe.setText("Robe");
		toolkit.createCompositeSeparator(sectionRobe);
		GridData gridDataGroupeRobe = new GridData(GridData.FILL_HORIZONTAL);
		gridDataGroupeRobe.horizontalSpan = 2;
		sectionRobe.setLayoutData(gridDataGroupeRobe);

		Composite clientRobe = toolkit.createComposite(sectionRobe);

		GridLayout layoutGroupeRobe = new GridLayout();
		layoutGroupeRobe.numColumns = 2;
		layoutGroupeRobe.marginHeight = 1;
		layoutGroupeRobe.marginWidth = 1;
		layoutGroupeRobe.horizontalSpacing = 1;
		layoutGroupeRobe.verticalSpacing = 2;
		clientRobe.setLayout(layoutGroupeRobe);

		comboRobe = creerCombo(clientRobe,
				SWT.FLAT | SWT.READ_ONLY,
				new GridData(GridData.GRAB_HORIZONTAL));
		comboRobe.add("");
		comboRobe.add("Aubère");
		comboRobe.add("Beige");
		comboRobe.add("Bigarrée (arlequin)");
		comboRobe.add("Blanc");
		comboRobe.add("Bleu");
		comboRobe.add("Fauve");
		comboRobe.add("Grège");
		comboRobe.add("Gris");
		comboRobe.add("Marron");
		comboRobe.add("Noir");
		comboRobe.add("Sable");

		boutonPeau = toolkit.createButton(clientRobe, "Peau nue", SWT.RADIO);
		boutonPeau.setToolTipText("Peau nue");

		comboRobe.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				// Activation des autres boutons
				for (Iterator iter = listeBoutonsRobe.iterator(); iter
						.hasNext();) {
					Button bouton = (Button) iter.next();
					bouton.setEnabled(true);
				}
				// Déselection de 'peau nue'
				boutonPeau.setSelection(false);
				gererEventRechercheAvancee();
			}
		});

		boutonPeau.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				// Désactivation des autres boutons
				for (Iterator iter = listeBoutonsRobe.iterator(); iter
						.hasNext();) {
					Button bouton = (Button) iter.next();
					bouton.setSelection(false);
					bouton.setEnabled(false);
				}
				// Remise à "" pour le champ 'couleur'
				comboRobe.select(0);
				gererEventRechercheAvancee();
			}
		});

		creerBoutonRobe(clientRobe, "Robe charbonnée", "Charbonnée", 128);
		creerBoutonRobe(clientRobe,
				"Robe panachée (blanche et colorée)",
				"Panachée",
				65536);
		creerBoutonRobe(clientRobe, "Robe à masque", "A masque", 256);
		creerBoutonRobe(clientRobe, "Robe mouchetée", "Mouchetée", 131072);
		creerBoutonRobe(clientRobe, "Robe marquée", "Marquée", 8192);
		creerBoutonRobe(clientRobe, "Robe truitée", "Truitée", 262144);
		creerBoutonRobe(clientRobe, "Robe à manteau", "A manteau", 16384);
		creerBoutonRobe(clientRobe,
				"Robe rouannée ou aubérisée",
				"Rouannée",
				524288);
		creerBoutonRobe(clientRobe, "Robe bringée", "Bringée", 32768);
		creerBoutonRobe(clientRobe, "Robe grisonnée", "Grisonnée", 1048576);

		sectionRobe.setClient(clientRobe);

		Section sectionAutres = toolkit.createSection(composite,
				ExpandableComposite.EXPANDED);
		sectionAutres.setText("Autres");
		GridData gridDataAutres = new GridData(GridData.FILL_HORIZONTAL);
		gridDataAutres.horizontalSpan = 2;
		sectionAutres.setLayoutData(gridDataAutres);
		toolkit.createCompositeSeparator(sectionAutres);

		creerLabelRechercheAvancee(composite, "Museau");
		comboMuseau = creerComboRechercheAvancee(composite);
		comboMuseau.add("");
		comboMuseau.add("court");
		comboMuseau.add("long");

		creerLabelRechercheAvancee(composite, "Taille des yeux");
		comboYeuxTaille = creerComboRechercheAvancee(composite);
		comboYeuxTaille.add("");
		comboYeuxTaille.add("petits");
		comboYeuxTaille.add("grands");

		creerLabelRechercheAvancee(composite, "Ecartement des yeux");
		comboYeuxEcartement = creerComboRechercheAvancee(composite);
		comboYeuxEcartement.add("");
		comboYeuxEcartement.add("rapprochés");
		comboYeuxEcartement.add("écartés");

		creerLabelRechercheAvancee(composite, "Truffe");
		comboTruffe = creerComboRechercheAvancee(composite);
		comboTruffe.add("");
		comboTruffe.add("fine");
		comboTruffe.add("large");

		onglet.setControl(composite);

		lancerRechercheAvancee = new Runnable() {

			public void run() {
				viewer.setInput(lancerRecherche(1));
				repositionnerArbre();
			}
		};

		return onglet;
	}

	/**
	 * @param groupeRobe
	 * @param toolTip
	 * @param libelle
	 * @param valeurRecherche
	 * @return
	 */
	private Button creerBoutonRobe(Composite groupeRobe, String toolTip,
			String libelle, long valeurRecherche) {
		Button bouton = toolkit.createButton(groupeRobe, libelle, SWT.CHECK);
		bouton.setToolTipText(toolTip);
		bouton.addListener(SWT.Selection, listenerRechercheAvancee);
		listeBoutonsRobe.add(bouton);
		listeValeursBoutonsRobe.add(new Long(valeurRecherche));
		return bouton;
	}

	private CCombo creerCombo(Composite composite, int style, Object layoutData) {
		CCombo combo = new CCombo(composite, style);
		combo.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(composite);
		toolkit.adapt(combo, true, true);

		combo.setLayoutData(layoutData);
		return combo;
	}

	private CCombo creerComboRechercheAvancee(Composite composite) {
		CCombo combo = creerCombo(composite,
				SWT.FLAT | SWT.READ_ONLY,
				new GridData(GridData.FILL_HORIZONTAL));
		combo.addListener(SWT.Selection, listenerRechercheAvancee);
		return combo;
	}

	/**
	 * @param composite
	 * @param libelle
	 * @param libelleAlternatif
	 * @return
	 */
	private Label creerLabelRechercheAvancee(Composite composite,
			String libelle, String libelleAlternatif) {
		Label label = toolkit.createLabel(composite, libelle);
		if (libelleAlternatif != null) {
			label.setToolTipText(libelleAlternatif);
		}
		return label;
	}

	/**
	 * @param composite
	 * @param libelle
	 * @return
	 */
	private Label creerLabelRechercheAvancee(Composite composite,
			String libelle) {
		return creerLabelRechercheAvancee(composite, libelle, null);
	}

	private void gererMenuContextuel() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				VueRecherche.this.remplirMenuContextuel(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void remplirMenuContextuel(IMenuManager manager) {
		manager.add(actionChangerOrientation);
		manager.add(actionReduireTout);
		manager.add(new Separator());
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void construireActions() {
		actionChangerOrientation = new Action() {

			public void run() {
				if (isChecked()) {
					setChecked(true);
					orientation = EN_GROUPE;
				} else {
					setChecked(false);
					orientation = SANS_GROUPE;
				}
				viewer.refresh();
				repositionnerArbre();
			}
		};
		actionChangerOrientation.setChecked(orientation == EN_GROUPE);
		actionChangerOrientation.setText("Trier par groupe");
		actionChangerOrientation.setToolTipText("Trier par groupe");
		actionChangerOrientation.setImageDescriptor(PluginBabyDog.getDefault()
				.getImageDescriptor(PluginBabyDog.GROUPE));

		actionReduireTout = new Action() {

			public void run() {
				viewer.collapseAll();
			}
		};
		actionReduireTout.setText("Tout réduire");
		actionReduireTout.setToolTipText("Tout réduire");
		actionReduireTout.setImageDescriptor(PluginBabyDog.getDefault()
				.getImageDescriptor(PluginBabyDog.COLLAPSE_ALL));

		actionEtendreTout = new Action() {

			public void run() {
				repositionnerArbre();
			}
		};
		actionEtendreTout.setText("Tout étendre");
		actionEtendreTout.setToolTipText("Tout étendre");
		actionEtendreTout.setImageDescriptor(PluginBabyDog.getDefault()
				.getImageDescriptor(PluginBabyDog.EXPAND_ALL));

		remettreZeroRecherche = new Action() {

			public void run() {
				// On remet tout à zéro
				zoneRecherche.setText("");

				comboHauteur.select(0);
				comboMuseau.select(0);
				comboOreilles.select(0);
				comboPoids.select(0);
				comboQueue.select(0);
				comboRobe.select(0);
				comboSI.select(0);
				comboTruffe.select(0);
				comboYeuxEcartement.select(0);
				comboYeuxTaille.select(0);
				boutonPeau.setSelection(false);

				for (Iterator iter = listeBoutonsRobe.iterator(); iter
						.hasNext();) {
					Button bouton = (Button) iter.next();
					bouton.setSelection(false);
					bouton.setEnabled(true);
				}

				lancerRechercheSimple.run();
			}
		};
		remettreZeroRecherche.setText("Remettre à zéro");
		remettreZeroRecherche
				.setToolTipText("Remettre à zéro toute la recherche");
		remettreZeroRecherche.setImageDescriptor(PluginBabyDog.getDefault()
				.getImageDescriptor(PluginBabyDog.EFFACER));
	}

	private void repositionnerArbre() {
		viewer.expandAll();
		TreeItem[] selections = viewer.getTree().getSelection();
		if (selections != null && selections.length > 0) {
			viewer.getTree().setTopItem(selections[0]);
		} else {
			if (viewer.getTree().getItems() != null
				&& viewer.getTree().getItems().length > 0) {
				viewer.getTree().setTopItem(viewer.getTree().getItems()[0]);
			}
		}
	}

	private void gererDoubleClicSurArbre() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				if (obj instanceof ItemRace) {
					new NouvelleRace(	getSite().getWorkbenchWindow(),
										((ItemRace) obj).getId()).run();
				} else if (obj instanceof ItemGroupe) {
					if (viewer.getExpandedState(obj)) {
						viewer.collapseToLevel(obj,
								AbstractTreeViewer.ALL_LEVELS);
					} else {
						viewer.expandToLevel(obj,
								AbstractTreeViewer.ALL_LEVELS);
					}
				}
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		zoneRecherche.setFocus();
	}

	/**
	 * @param layout
	 */
	private void setNombreColonnes(GridLayout layout) {
		if (orientationCourante == ORIENTATION_VUE_HORIZONTALE)
			layout.numColumns = 2;
		else
			layout.numColumns = 1;
	}

	/**
	 * @param nParent
	 */
	private void addResizeListener(Composite nParent) {
		nParent.addListener(SWT.Resize, new Listener() {

			public void handleEvent(Event e) {
				calculerOrientation();
			}
		});
	}

	/**
	 * 
	 *
	 */
	private void calculerOrientation() {
		Point size = parent.getSize();
		if (size.x != 0 && size.y != 0) {
			if (size.x > size.y)
				setOrientation(ORIENTATION_VUE_HORIZONTALE);
			else
				setOrientation(ORIENTATION_VUE_VERTICALE);
		}
	}

	/**
	 * @param nOrientation
	 */
	private void setOrientation(int nOrientation) {
		if ((sashForm == null) || sashForm.isDisposed())
			return;
		boolean horizontal = nOrientation == ORIENTATION_VUE_HORIZONTALE;
		sashForm.setOrientation(horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
		GridLayout layout = (GridLayout) parent.getLayout();
		setNombreColonnes(layout);
		parent.layout();
	}

	/**
	 * Récupère l'orientation
	 * 
	 * @return L'orientation
	 */
	public final int getOrientation() {
		return orientation;
	}

	/**
	 * @param nbResultats
	 */
	private void updateNombreResultats(int nbResultats) {
		String phrase = "";
		if (nbResultats == 0) {
			phrase = "Aucune race trouvée";
		} else if (nbResultats == 1) {
			phrase = nbResultats + " race trouvée";
		} else {
			phrase = nbResultats + " races trouvées";
		}
		if (!nombreResultatsRecherche.isDisposed()) {
			nombreResultatsRecherche.setText(phrase);
		}
	}

	/**
	 * @param typeRecherche
	 * @return
	 */
	private ItemRacine lancerRecherche(int typeRecherche) {
		ItemRacine racine = new ItemRacine(this, "");

		try {
			GestionnaireAccesDonnees gestionnaire = GestionnaireAccesDonnees
					.getInstance(GestionnaireAccesDonnees.TYPE_CLIENT);

			ResultSet resultat = null;
			if (typeRecherche == 0) {
				resultat = lancerRechercheSimple();
			} else {
				resultat = lancerRechercheAvancee();
			}

			int nbResultats = 0;
			while (resultat.next()) {
				String nomRace = resultat.getString("nom_race");
				int idGroupe = resultat.getInt("groupe");
				int idRace = resultat.getInt("id_race");
				ItemRace race = new ItemRace(nomRace, idRace);
				ItemGroupe groupe = racine.fabriqueGroupe(idGroupe);
				groupe.addChild(race);
				racine.addChild(groupe);
				racine.addChild(race);
				nbResultats++;
			}

			updateNombreResultats(nbResultats);

			gestionnaire.terminerRequete(resultat);

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			Util.afficherErreur(getSite().getShell(), e);
		}
		return racine;
	}

	/**
	 * @return Un résultat
	 * @throws ExceptionTechnique
	 */
	private ResultSet lancerRechercheSimple() throws ExceptionTechnique {
		String chaineRecherche = zoneRecherche.getText();
		ResultSet retour = null;

		GestionnaireAccesDonnees gestionnaire = GestionnaireAccesDonnees
				.getInstance(GestionnaireAccesDonnees.TYPE_CLIENT);

		if ("".equals(chaineRecherche)) {
			retour = gestionnaire.executerSelect(1);
		} else {
			List<String> parametres = new ArrayList<String>();
			parametres.add("%" + chaineRecherche + "%");
			retour = gestionnaire.executerSelect(2, parametres);
		}
		return retour;
	}

	/**
	 * @return Un résultat
	 * @throws ExceptionTechnique
	 */
	private ResultSet lancerRechercheAvancee() throws ExceptionTechnique {
		andAjoute = false;

		// Gestion de tous les critères de la recherche avancée et
		// construction de la requête
		// Exemple :
		// SELECT nom_race, groupe, id_race
		//     FROM races
		//     WHERE etat_fiche=4 AND nom_race LIKE ?
		//     ORDER BY nom_race ASC

		String select = "SELECT DISTINCT races.nom_race, races.groupe, "
						+ "races.id_race ";
		String from = "FROM races, caracteristiques";
		StringBuffer where = new StringBuffer(" WHERE ");
		String orderBy = " ORDER BY nom_race ASC";

		String cas = "";

		// Mensurations
		// Hauteur au garrot
		cas = "";
		switch (comboHauteur.getSelectionIndex()) {
			case 1 :
				cas = "races.chiot_taille_min < " + 25;
				break;
			case 2 :
				cas = "races.chiot_taille_min >= "
						+ 25
						+ " AND races.chiot_taille_min <= "
						+ 35;
				break;
			case 3 :
				cas = "races.chiot_taille_min > " + 35;
				break;
		}
		where.append(gererAND(cas));

		// Longueur SI
		cas = "";
		switch (comboSI.getSelectionIndex()) {
			case 1 :
				cas = "races.chiot_longueur_si_min < " + 25;
				break;
			case 2 :
				cas = "races.chiot_longueur_si_min >= "
						+ 25
						+ " AND races.chiot_longueur_si_min <= "
						+ 35;
				break;
			case 3 :
				cas = "races.chiot_longueur_si_min > " + 35;
				break;
		}
		where.append(gererAND(cas));

		// Poids
		cas = "";
		switch (comboPoids.getSelectionIndex()) {
			case 1 :
				cas = "races.chiot_poids_min < " + 5;
				break;
			case 2 :
				cas = "races.chiot_poids_min >= "
						+ 5
						+ " AND races.chiot_poids_min <= "
						+ 9;
				break;
			case 3 :
				cas = "races.chiot_poids_min > " + 9;
				break;
		}
		where.append(gererAND(cas));

		// Museau
		cas = "";
		switch (comboMuseau.getSelectionIndex()) {
			case 1 :
				cas = "caracteristiques.museau & 3 > 0";
				break;
			case 2 :
				cas = "caracteristiques.museau & 6 > 0";
				break;
		}
		where.append(gererAND(cas));

		// Taille des yeux
		cas = "";
		switch (comboYeuxTaille.getSelectionIndex()) {
			case 1 :
				cas = "caracteristiques.yeux_taille & 3 > 0";
				break;
			case 2 :
				cas = "caracteristiques.yeux_taille & 6 > 0";
				break;
		}
		where.append(gererAND(cas));

		// Ecartement des yeux
		cas = "";
		switch (comboYeuxEcartement.getSelectionIndex()) {
			case 1 :
				cas = "caracteristiques.yeux_ecartement & 3 > 0";
				break;
			case 2 :
				cas = "caracteristiques.yeux_ecartement & 6 > 0";
				break;
		}
		where.append(gererAND(cas));

		// Truffe
		cas = "";
		switch (comboTruffe.getSelectionIndex()) {
			case 1 :
				cas = "caracteristiques.truffe & 3 > 0";
				break;
			case 2 :
				cas = "caracteristiques.truffe & 6 > 0";
				break;
		}
		where.append(gererAND(cas));

		// Oreilles
		cas = "";
		switch (comboOreilles.getSelectionIndex()) {
			case 1 :
				cas = "caracteristiques.oreilles & 1 > 0";
				break;
			case 2 :
				cas = "caracteristiques.oreilles & 4 > 0";
				break;
			case 3 :
				cas = "caracteristiques.oreilles & 2 > 0";
				break;
		}
		where.append(gererAND(cas));

		// Queue
		cas = "";
		switch (comboQueue.getSelectionIndex()) {
			case 1 :
				cas = "caracteristiques.queue & 3 > 0";
				break;
			case 2 :
				cas = "caracteristiques.queue & 6 > 0";
				break;
		}
		where.append(gererAND(cas));

		long somme = 0;
		cas = "";
		switch (comboRobe.getSelectionIndex()) {
			case 1 :
				somme = 2048;
				break;
			case 2 :
				somme = 8;
				break;
			case 3 :
				somme = 4096;
				break;
			case 4 :
				somme = 64;
				break;
			case 5 :
				somme = 4;
				break;
			case 6 :
				somme = 16;
				break;
			case 7 :
				somme = 1024;
				break;
			case 8 :
				somme = 512;
				break;
			case 9 :
				somme = 2;
				break;
			case 10 :
				somme = 1;
				break;
			case 11 :
				somme = 32;
				break;

		}

		if (boutonPeau.getSelection()) {
			somme = 2097152;
		}

		int i = 0;
		int nbBoutonsRobe = listeBoutonsRobe.size();
		for (i = 0; i < nbBoutonsRobe; i++) {
			Button bouton = listeBoutonsRobe.get(i);
			if (bouton.getSelection()) {
				somme += listeValeursBoutonsRobe.get(i).longValue();
			}
		}
		if (somme > 0) {
			cas = "caracteristiques.robe & " + somme + " = " + somme;
			where.append(gererAND(cas));
		}

		where.append(gererAND(" races.id_race = caracteristiques.id_race"));

		StringBuffer requeteComplete = new StringBuffer(select.length()
														+ from.length()
														+ where.length()
														+ orderBy.length());
		requeteComplete.append(select);
		requeteComplete.append(from);
		requeteComplete.append(where);
		requeteComplete.append(orderBy);

		GestionnaireAccesDonnees gestionnaire = GestionnaireAccesDonnees
				.getInstance(GestionnaireAccesDonnees.TYPE_CLIENT);

		ResultSet retour = gestionnaire
				.executerRequeteDynamique(requeteComplete.toString(), null);
		return retour;
	}

	/**
	 * 
	 */
	private void gererEventRechercheAvancee() {
		Display.getCurrent().timerExec(DELAI_INIT_RECHERCHE,
				lancerRechercheAvancee);
	}

	private String gererAND(String boutRequete) {
		String retour = null;
		if (andAjoute && boutRequete.length() > 0) {
			retour = " AND " + boutRequete;
		} else {
			retour = boutRequete;
		}
		if (!andAjoute && boutRequete.length() > 0) {
			andAjoute = true;
		}

		return retour;
	}
}