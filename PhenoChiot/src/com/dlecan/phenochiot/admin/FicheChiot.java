package com.dlecan.phenochiot.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.dlecan.phenochiot.framework.db.GestionnaireAccesDonnees;
import com.dlecan.phenochiot.framework.exception.ExceptionTechnique;
import com.dlecan.phenochiot.utils.GenerateurContenuFiche;

/**
 * @author dam
 * 
 * @revision $Revision: 1.5 $
 * @version $Name:  $
 * @date $Date: 2003/12/20 13:39:25 $
 * @id $Id: FicheChiot.java,v 1.5 2003/12/20 13:39:25 fris Exp $
 */
public class FicheChiot extends Fiche {

    /**
     * 
     */
    private static final String NOM_ONGLET = "Caractéristiques du chiot";

    /**
     * Logger de la classe
     */
    private static Logger log = Logger.getLogger(FicheChiot.class);

    /**
     * Panneau dans lequel est insérée la fiche
     */
    private PanneauDroit panneauOrigine;

    /**
     * 
     */
    private GenerateurContenuFiche generateur;

    /**
     * @param parent
     * @param style
     * @param nPanneau
     */
    public FicheChiot(Composite parent, int style, PanneauDroit nPanneau) {
        super(parent, style);
        log.debug("Début constructeur FicheChiot");

        panneauOrigine = nPanneau;

        log.debug("Fin constructeur FicheChiot");
    }

    /**
     * @see Fiche#creerContenu()
     */
    public void creerContenu() {
        log.debug("Début méthode creerContenu");

        generateur = new GenerateurContenuFiche("desc_fiche_chiot.xml", this,
                panneauOrigine);

        log.debug("Fin méthode creerContenu");
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose() {
        log.debug("Début méthode dispose");
        super.dispose();

        generateur.dispose();

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
     * @see Fiche#majDonnees()
     */
    public void majDonnees() {
        log.debug("Début méthode majDonnees");

        GestionnaireAccesDonnees gad = null;
        ResultSet resultats = null;

        try {
            gad = GestionnaireAccesDonnees
                    .getInstance(GestionnaireAccesDonnees.TYPE_ADMIN);
            ArrayList parametres = new ArrayList();
            parametres.add(new Integer(getRace()));

            resultats = gad.executerSelect(5, parametres);

            while (resultats.next()) {
                generateur.afficher(resultats);
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

        afficherDonnees();

        log.debug("Fin méthode majDonnees");
    }

    /**
     * @see com.dlecan.phenochiot.admin.Fiche#afficherDonnees()
     */
    protected void afficherDonnees() {
        log.debug("Début méthode afficherDonnees");

        log.debug("Fin méthode afficherDonnees");
    }

    /**
     * @see com.dlecan.phenochiot.admin.Fiche#sauver()
     */
    protected void sauver() {
        log.debug("Début méthode sauver");

        GestionnaireAccesDonnees gad = null;

        try {
            gad = GestionnaireAccesDonnees
                    .getInstance(GestionnaireAccesDonnees.TYPE_ADMIN);

            List parametres = generateur.getParametres();

            parametres.add(new Integer(getRace()));

            gad.executerUpdate(6, parametres);

            setModifie(false);

        } catch (ExceptionTechnique e) {
            log.error(e.getMessage(), e);
            Administration.afficherErreur(getShell(), e);
        } finally {
            gad.terminerRequete();
        }

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
