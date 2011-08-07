package com.dlecan.phenochiot.admin;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author dam
 * 
 * @revision $Revision: 1.5 $
 * @version $Name:  $
 * @date $Date: 2004/01/10 21:48:29 $
 * @id $Id: FicheImages.java,v 1.5 2004/01/10 21:48:29 fris Exp $
 */
public class FicheImages extends Fiche {

	/**
	 *  
	 */
	private static final String NOM_ONGLET = "Images";

	/**
	 * Logger de la classe
	 */
	private static Logger log = Logger.getLogger(FicheImages.class);

	/**
	 *  
	 */
	private Label texte;

	/**
	 * @param parent
	 * @param style
	 */
	public FicheImages(Composite parent, int style) {
		super(parent, style);
		log.debug("Début constructeur FicheImages");
		log.debug("Fin constructeur FicheImages");
	}

	/**
	 * @see Fiche#creerContenu()
	 */
	public void creerContenu() {
		log.debug("Début méthode creerContenu");
		texte = new Label(this, SWT.CENTER);
		texte.setText("A venir ...");
		log.debug("Fin méthode creerContenu");
	}

	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		log.debug("Début méthode dispose");
		super.dispose();
		texte.dispose();
		log.debug("Fin méthode dispose");
	}

	/**
	 * @see Fiche#getNomOnglet()
	 */
	public String getNomOnglet() {
		log.debug("Méthode getNomOnglet");
		return NOM_ONGLET;
	}

	/**
	 * @param e
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
	    // Rien à faire
	    e.toString();
	}

	/**
	 * @see com.dlecan.phenochiot.admin.Fiche#afficherDonnees()
	 */
	protected void afficherDonnees() {
		log.debug("Début méthode afficherDonnees");
		// TODO Auto-generated method stub

		log.debug("Fin méthode afficherDonnees");
	}

	/**
	 * @see com.dlecan.phenochiot.admin.Fiche#majDonnees()
	 */
	protected void majDonnees() {
		log.debug("Début méthode majDonnees");
		// TODO Auto-generated method stub

		log.debug("Fin méthode majDonnees");
	}

    /**
     * @see com.dlecan.phenochiot.admin.Fiche#sauver()
     */
    protected void sauver() {
        log.debug("Début méthode sauver");
        // TODO Auto-generated method stub
        
        log.debug("Fin méthode sauver");
    }

    /**
     * @see com.dlecan.phenochiot.admin.Fiche#annuler()
     */
    protected void annuler() {
        log.debug("Début méthode annuler");
        // TODO Auto-generated method stub
        
        log.debug("Fin méthode annuler");
    }

}
