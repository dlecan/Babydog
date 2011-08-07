package com.dlecan.phenochiot.admin;

import org.apache.log4j.Logger;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

/**
 * @author dam
 * 
 * @revision $Revision: 1.4 $
 * @version $Name:  $
 * @date $Date: 2003/12/20 13:39:25 $
 * @id $Id: Fiche.java,v 1.4 2003/12/20 13:39:25 fris Exp $
 */
public abstract class Fiche extends Composite {

    /**
     * Logger de la classe
     */
    private static Logger log = Logger.getLogger(Fiche.class);

    /**
     * Layout par défaut des fiches
     */
    protected Layout layoutParDefaut;

    /**
     * La race en cours
     */
    private int race;

    /**
     * 
     */
    private boolean modifie = false;

    /**
     * @param parent
     * @param style
     */
    public Fiche(Composite parent, int style) {
        super(parent, style);
        log.debug("Début constructeur Fiche");

        layoutParDefaut = new GridLayout();
        ((GridLayout) layoutParDefaut).numColumns = 1;
        setLayout(layoutParDefaut);

        log.debug("Fin constructeur Fiche");
    }

    /**
     *  
     */
    protected abstract void creerContenu();

    /**
     * @return Le nom que l'onglet de la fiche doit afficher
     */
    protected abstract String getNomOnglet();

    /**
     * 
     */
    protected abstract void majDonnees();

    /**
     * 
     *
     */
    protected abstract void sauver();

    /**
     * 
     *
     */
    protected abstract void annuler();

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose() {
        log.debug("Début méthode dispose");
        super.dispose();
        log.debug("Fin méthode dispose");
    }

    /**
     * 
     */
    protected abstract void afficherDonnees();

    /**
     * @return Retourne race.
     */
    protected final int getRace() {
        log.debug("Accesseur getRace");
        return race;
    }

    /**
     * @param nRace race à définir.
     */
    protected final void setRace(int nRace) {
        log.debug("Début setter setRace");
        this.race = nRace;
        log.debug("Fin setter setRace");
    }

    /**
     * @return Retourne modifie.
     */
    public boolean isModifie() {
        log.debug("Accesseur isModifie");
        return modifie;
    }

    /**
     * @param nModifie modifie à définir.
     */
    public void setModifie(boolean nModifie) {
        log.debug("Début setter setModifie");
        this.modifie = nModifie;
        log.debug("Fin setter setModifie");
    }
}
