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
		log.debug("D�but constructeur FicheImages");
		log.debug("Fin constructeur FicheImages");
	}

	/**
	 * @see Fiche#creerContenu()
	 */
	public void creerContenu() {
		log.debug("D�but m�thode creerContenu");
		texte = new Label(this, SWT.CENTER);
		texte.setText("A venir ...");
		log.debug("Fin m�thode creerContenu");
	}

	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		log.debug("D�but m�thode dispose");
		super.dispose();
		texte.dispose();
		log.debug("Fin m�thode dispose");
	}

	/**
	 * @see Fiche#getNomOnglet()
	 */
	public String getNomOnglet() {
		log.debug("M�thode getNomOnglet");
		return NOM_ONGLET;
	}

	/**
	 * @param e
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
	    // Rien � faire
	    e.toString();
	}

	/**
	 * @see com.dlecan.phenochiot.admin.Fiche#afficherDonnees()
	 */
	protected void afficherDonnees() {
		log.debug("D�but m�thode afficherDonnees");
		// TODO Auto-generated method stub

		log.debug("Fin m�thode afficherDonnees");
	}

	/**
	 * @see com.dlecan.phenochiot.admin.Fiche#majDonnees()
	 */
	protected void majDonnees() {
		log.debug("D�but m�thode majDonnees");
		// TODO Auto-generated method stub

		log.debug("Fin m�thode majDonnees");
	}

    /**
     * @see com.dlecan.phenochiot.admin.Fiche#sauver()
     */
    protected void sauver() {
        log.debug("D�but m�thode sauver");
        // TODO Auto-generated method stub
        
        log.debug("Fin m�thode sauver");
    }

    /**
     * @see com.dlecan.phenochiot.admin.Fiche#annuler()
     */
    protected void annuler() {
        log.debug("D�but m�thode annuler");
        // TODO Auto-generated method stub
        
        log.debug("Fin m�thode annuler");
    }

}
