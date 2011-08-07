package com.dlecan.phenochiot.admin;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import com.dlecan.phenochiot.framework.db.GestionnaireAccesDonnees;
import com.dlecan.phenochiot.framework.exception.ExceptionTechnique;
/**
 * @author dam
 * 
 * @revision $Revision: 1.7 $
 * @version $Name: $ @date $Date: 2004/01/11 15:03:14 $ @id $Id:
 *          PanneauDroit.java,v 1.7 2004/01/11 15:03:14 fris Exp $
 */
public class PanneauDroit extends Composite
		implements
			MouseListener,
			VerifyKeyListener,
			KeyListener,
			ModifyListener {
	/**
	 * Logger de la classe
	 */
	private static Logger log = Logger.getLogger(PanneauDroit.class);
	/**
	 *  
	 */
	private Label titre;
	/**
	 *  
	 */
	private Label autreNom;
	/**
	 *  
	 */
	private Label groupe;
	/**
	 *  
	 */
	private Label section;
	/**
	 *  
	 */
	private Label sousSection;
	/**
	 *  
	 */
	private Text etatFiche;
	/**
	 *  
	 */
	private Button appliquer;
	/**
	 *  
	 */
	private Button annuler;
	/**
	 *  
	 */
	private TabFolder tabFolder;
	/**
	 *  
	 */
	private TabItem ongletStandard;
	/**
	 *  
	 */
	private ScrolledComposite scStandard;
	/**
	 *  
	 */
	private ScrolledComposite scChiot;
	/**
	 *  
	 */
	private ScrolledComposite scImages;
	/**
	 *  
	 */
	private ScrolledComposite scCaracteristique;
	/**
	 *  
	 */
	private TabItem ongletCaracteristique;
	/**
	 *  
	 */
	private TabItem ongletChiot;
	/**
	 *  
	 */
	private TabItem ongletImages;
	/**
	 *  
	 */
	private Fiche ficheStandard;
	/**
	 *  
	 */
	private Fiche ficheChiot;
	/**
	 *  
	 */
	private Fiche ficheImages;
	/**
	 *  
	 */
	private Fiche ficheCaracteristique;
	/**
	 *  
	 */
	private int numOngletCourant = 0;
	/**
	 *  
	 */
	private int idRace = 0;
	/**
	 * @param parent
	 * @param style
	 */
	public PanneauDroit(Composite parent, int style) {
		super(parent, style);
		log.debug("Début constructeur PanneauDroit");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		titre = new Label(this, SWT.CENTER);
		autreNom = new Label(this, SWT.LEFT);
		groupe = new Label(this, SWT.LEFT);
		section = new Label(this, SWT.LEFT);
		sousSection = new Label(this, SWT.LEFT);
		etatFiche = new Text(this, SWT.BORDER);
		setLayout(gridLayout);
		log.debug("Fin constructeur PanneauDroit");
	}
	protected void creerContenu() {
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Font policeTitre = new Font(getDisplay(), "Arial", 15, SWT.BOLD);
		titre.setFont(policeTitre);
		titre.setText("Nom de la race animale");
		titre.setLayoutData(gridData);
		//
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		autreNom.setLayoutData(gridData);
		//
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		groupe.setLayoutData(gridData);
		//
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		section.setLayoutData(gridData);
		//
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		sousSection.setLayoutData(gridData);
		//
		gridData = new GridData();
		etatFiche.setLayoutData(gridData);
		etatFiche.addModifyListener(this);
		//
		gridData = new GridData(GridData.FILL_BOTH);
		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(gridData);
		tabFolder.addMouseListener(this);
		/*
		 * 
		 *  
		 */
		ongletStandard = new TabItem(tabFolder, SWT.NULL);
		scStandard = new ScrolledComposite(tabFolder, SWT.NONE | SWT.V_SCROLL);
		ficheStandard = new FicheStandard(scStandard, SWT.NONE, this);
		ficheStandard.creerContenu();
		ongletStandard.setText(ficheStandard.getNomOnglet());
		scStandard.setContent(ficheStandard);
		scStandard.setExpandVertical(true);
		scStandard.setExpandHorizontal(true);
		scStandard.setMinHeight(6650);
		ongletStandard.setControl(scStandard);
		/*
		 * 
		 *  
		 */
		ongletCaracteristique = new TabItem(tabFolder, SWT.NULL);
		scCaracteristique = new ScrolledComposite(tabFolder, SWT.NONE
				| SWT.V_SCROLL);
		ficheCaracteristique = new FicheUtilisationCaractere(scCaracteristique,
				SWT.NONE, this);
		ficheCaracteristique.creerContenu();
		ongletCaracteristique.setText(ficheCaracteristique.getNomOnglet());
		scCaracteristique.setContent(ficheCaracteristique);
		scCaracteristique.setExpandVertical(true);
		scCaracteristique.setExpandHorizontal(true);
		scCaracteristique.setMinHeight(450);
		ongletCaracteristique.setControl(scCaracteristique);
		/*
		 * 
		 *  
		 */
		ongletChiot = new TabItem(tabFolder, SWT.NULL);
		scChiot = new ScrolledComposite(tabFolder, SWT.NONE | SWT.V_SCROLL);
		ficheChiot = new FicheChiot(scChiot, SWT.NONE, this);
		ficheChiot.creerContenu();
		ongletChiot.setText(ficheChiot.getNomOnglet());
		scChiot.setContent(ficheChiot);
		scChiot.setExpandVertical(true);
		scChiot.setExpandHorizontal(true);
		scChiot.setMinHeight(6480);
		ongletChiot.setControl(scChiot);
		/*
		 * 
		 *  
		 */
		ongletImages = new TabItem(tabFolder, SWT.NULL);
		scImages = new ScrolledComposite(tabFolder, SWT.NONE | SWT.V_SCROLL);
		ficheImages = new FicheImages(scImages, SWT.NONE);
		ficheImages.creerContenu();
		ongletImages.setText(ficheImages.getNomOnglet());
		scImages.setContent(ficheImages);
		scImages.setExpandVertical(true);
		scImages.setExpandHorizontal(true);
		scImages.setMinHeight(getParent().getParent().getSize().y);
		ongletImages.setControl(scImages);
		//
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		Composite boutonsSortie = new Composite(this, SWT.NONE);
		boutonsSortie.setLayoutData(gridData);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.wrap = false;
		boutonsSortie.setLayout(layout);
		appliquer = new Button(boutonsSortie, SWT.PUSH);
		appliquer.setText("Appliquer");
		appliquer.setEnabled(false);
		appliquer.addMouseListener(this);
		annuler = new Button(boutonsSortie, SWT.PUSH);
		annuler.setText("Annuler");
		annuler.setEnabled(false);
		annuler.addMouseListener(this);
	}
	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		log.debug("Début méthode dispose");
		super.dispose();
		titre.dispose();
		section.dispose();
		sousSection.dispose();
		etatFiche.dispose();
		groupe.dispose();
		autreNom.dispose();
		annuler.dispose();
		appliquer.dispose();
		tabFolder.dispose();
		// Suppression des scrollComposite
		scStandard.dispose();
		scCaracteristique.dispose();
		scChiot.dispose();
		scImages.dispose();
		// Suppression des onglets
		ongletCaracteristique.dispose();
		ongletChiot.dispose();
		ongletImages.dispose();
		ongletStandard.dispose();
		// Suppression des fiches
		ficheStandard.dispose();
		ficheImages.dispose();
		ficheCaracteristique.dispose();
		ficheChiot.dispose();
		log.debug("Fin méthode dispose");
	}
	/**
	 * @param nIdRace
	 */
	public void majDonnees(int nIdRace) {
		log.debug("Début méthode majDonnees");
		log.debug("Id de la race à afficher : " + nIdRace);
		idRace = nIdRace;
		verifierDonnneesAJour();
		GestionnaireAccesDonnees gad = null;
		ResultSet resultats = null;
		try {
			gad = GestionnaireAccesDonnees
					.getInstance(GestionnaireAccesDonnees.TYPE_ADMIN);
			ArrayList parametres = new ArrayList();
			parametres.add(new Integer(nIdRace));
			resultats = gad.executerSelect(2, parametres);
			while (resultats.next()) {
				titre.setText(resultats.getString("nom_race") + " (" + idRace
						+ ")");
				String strAutreNom = resultats.getString("autre_nom");
				if (!"".equals(strAutreNom)) {
					autreNom.setVisible(true);
					autreNom.setText("Autre nom : " + strAutreNom);
				} else {
					autreNom.setVisible(false);
				}
				groupe.setText("Groupe : " + resultats.getInt("groupe"));
				section.setText("Section : " + resultats.getInt("section"));
				sousSection.setText("Sous-section : "
						+ resultats.getInt("ss_section"));
				etatFiche.setText(resultats.getString("etat_fiche"));
			}
		} catch (ExceptionTechnique e) {
			log.error(e.getMessage(), e);
			Administration.afficherErreur(getShell(), e);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			Administration.afficherErreur(getShell(), e);
		} finally {
			gad.terminerRequete(resultats);
		}
		ficheStandard.setRace(nIdRace);
		ficheStandard.majDonnees();
		ficheCaracteristique.setRace(nIdRace);
		ficheCaracteristique.majDonnees();
		ficheChiot.setRace(nIdRace);
		ficheChiot.majDonnees();
		log.debug("Fin méthode majDonnees");
	}
	/**
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
		// Rien à faire
		e.toString();
	}
	/**
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		log.debug("Début méthode mouseDown");
		Object source = e.getSource();
		if (source instanceof Button) {
			Button boutonClique = (Button) source;
			if (appliquer.equals(boutonClique)) {
				sauver();
			} else if (annuler.equals(boutonClique)) {
				annuler();
			}
		} else if (source instanceof TabFolder) {
			if (tabFolder.equals(source)) {
				verifierDonnneesAJour();
			}
		}
		log.debug("Fin méthode mouseDown");
	}
	/**
	 *  
	 */
	public void verifierDonnneesAJour() {
		log.debug("Début méthode verifierDonnneesAJour");
		Fiche fiche = getFicheCourante();
		if (fiche.isModifie()) {
			boolean retour = Administration.afficherQuestion(getShell(),
					"La fiche a été " + "modifiée, mais pas sauvegardée.\n"
							+ "Voulez-vous "
							+ "la sauvegarder ? (les données seront "
							+ "perdues sinon)");
			if (retour) {
				sauver();
			} else {
				annuler();
			}
		}
		numOngletCourant = tabFolder.getSelectionIndex();
		log.debug("Fin méthode verifierDonnneesAJour");
	}
	/**
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		// Rien à faire
		e.toString();
	}
	/**
	 *  
	 */
	private void annuler() {
		log.debug("Début méthode annuler");
		boolean retour = Administration.afficherQuestion(getShell(),
				"Cette action va supprimer toutes vos modifications "
						+ "depuis le dernier enregistrement.\nEtes-vous "
						+ "sûr de vouloir annuler ?");
		if (retour) {
			final Shell splash = new Shell(getShell(), SWT.BORDER
					| SWT.APPLICATION_MODAL);
			final ProgressIndicator bar = new ProgressIndicator(splash);
			splash.setLayout(new FillLayout(SWT.HORIZONTAL));
			bar.beginTask(30);
			splash.pack();
			Rectangle splashRect = splash.getBounds();
			Rectangle displayRect = getDisplay().getBounds();
			int x = (displayRect.width - splashRect.width) / 2;
			int y = (displayRect.height - splashRect.height) / 2;
			splash.setLocation(x, y);
			splash.open();
			getDisplay().syncExec(new Runnable() {
				public void run() {
					bar.worked(10.0);
					Fiche fiche = getFicheCourante();
					bar.worked(10.0);
					fiche.majDonnees();
					bar.worked(10.0);
					fiche.setModifie(false);
					appliquer.setEnabled(false);
					annuler.setEnabled(false);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Rien
					}
					bar.sendRemainingWork();
					bar.done();
					splash.close();
					bar.dispose();
					splash.dispose();
				}
			});
		}
		log.debug("Fin méthode annuler");
	}
	/**
	 *  
	 */
	private void sauver() {
		log.debug("Début méthode sauver");
		final Shell splash = new Shell(getShell(), SWT.BORDER
				| SWT.APPLICATION_MODAL);
		final ProgressIndicator bar = new ProgressIndicator(splash);
		splash.setLayout(new FillLayout(SWT.HORIZONTAL));
		bar.beginTask(30);
		splash.pack();
		Rectangle splashRect = splash.getBounds();
		Rectangle displayRect = getDisplay().getBounds();
		int x = (displayRect.width - splashRect.width) / 2;
		int y = (displayRect.height - splashRect.height) / 2;
		splash.setLocation(x, y);
		splash.open();
		getDisplay().syncExec(new Runnable() {
			public void run() {
				bar.worked(10.0);
				Fiche fiche = getFicheCourante();
				bar.worked(10.0);
				fiche.sauver();
				bar.worked(10.0);
				fiche.setModifie(false);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Rien
				}
				bar.sendRemainingWork();
				bar.done();
				splash.close();
				bar.dispose();
				splash.dispose();
			}
		});
		appliquer.setEnabled(false);
		annuler.setEnabled(false);
		log.debug("Fin méthode sauver");
	}
	/**
	 * @return
	 */
	private Fiche getFicheCourante() {
		TabItem tabEnCours = tabFolder.getItem(numOngletCourant);
		Fiche fiche = (Fiche) ((ScrolledComposite) tabEnCours.getControl())
				.getContent();
		return fiche;
	}
	/**
	 * @see org.eclipse.swt.custom.VerifyKeyListener#verifyKey(org.eclipse.swt.events.VerifyEvent)
	 */
	public void verifyKey(VerifyEvent event) {
		log.debug("Début méthode verifyKey");
		log.debug(event);
		Fiche fiche = getFicheCourante();
		fiche.setModifie(true);
		appliquer.setEnabled(true);
		annuler.setEnabled(true);
		log.debug("Fin méthode verifyKey");
	}
	/**
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		//Rien
		e.toString();
	}
	/**
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		// Rien pour le moment
		e.toString();
	}
	/**
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText(ModifyEvent event) {
		log.debug("Début méthode modifyText");
		if (etatFiche.equals(event.getSource())) {
			if (!etatFiche.getText().equals("")) {
				GestionnaireAccesDonnees gad = null;
				try {
					gad = GestionnaireAccesDonnees
							.getInstance(GestionnaireAccesDonnees.TYPE_ADMIN);
					List parametres = new ArrayList();
					parametres.add(new Integer(etatFiche.getText()));
					parametres.add(new Integer(idRace));
					gad.executerUpdate(7, parametres);
				} catch (ExceptionTechnique e) {
					log.error(e.getMessage(), e);
					Administration.afficherErreur(getShell(), e);
				} finally {
					gad.terminerRequete();
				}
			}
		}
		log.debug("Fin méthode modifyText");
	}
}
