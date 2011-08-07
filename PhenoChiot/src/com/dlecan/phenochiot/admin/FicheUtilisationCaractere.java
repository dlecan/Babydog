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
 * @revision $Revision: 1.4 $
 * @version $Name: $ @date $Date: 2004/01/10 21:48:29 $ @id $Id:
 *          FicheUtilisationCaractere.java,v 1.4 2004/01/10 21:48:29 fris Exp $
 */
public class FicheUtilisationCaractere extends Fiche {
	/**
	 *  
	 */
	private static final String NOM_ONGLET = "Utilisation et caract�re";
	/**
	 * Logger de la classe
	 */
	private static Logger log = Logger
			.getLogger(FicheUtilisationCaractere.class);
	/**
	 * Panneau dans lequel est ins�r�e la fiche
	 */
	private PanneauDroit panneauOrigine;
	/**
	 *  
	 */
	private GenerateurContenuFiche generateur;
	/**
	 * @param parent
	 * @param style
	 */
	public FicheUtilisationCaractere(Composite parent, int style,
			PanneauDroit nPanneau) {
		super(parent, style);
		log.debug("D�but constructeur FicheCaracteristique");
		panneauOrigine = nPanneau;
		log.debug("Fin constructeur FicheCaracteristique");
	}
	/**
	 * @see Fiche#creerContenu()
	 */
	public void creerContenu() {
		log.debug("D�but m�thode creerContenu");
		generateur = new GenerateurContenuFiche(
				"desc_fiche_utilisation_caractere.xml", this, panneauOrigine);
		log.debug("Fin m�thode creerContenu");
	}
	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		log.debug("D�but m�thode dispose");
		super.dispose();
		generateur.dispose();
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
	 * @see com.dlecan.phenochiot.admin.Fiche#afficherDonnees()
	 */
	protected void afficherDonnees() {
		log.debug("D�but m�thode afficherDonnees");

		log.debug("Fin m�thode afficherDonnees");
	}
	/**
	 * @see com.dlecan.phenochiot.admin.Fiche#majDonnees()
	 */
	public void majDonnees() {
		log.debug("D�but m�thode majDonnees");

		GestionnaireAccesDonnees gad = null;
		ResultSet resultats = null;

		try {
			gad = GestionnaireAccesDonnees
					.getInstance(GestionnaireAccesDonnees.TYPE_ADMIN);
			ArrayList parametres = new ArrayList();
			parametres.add(new Integer(getRace()));
			resultats = gad.executerSelect(8, parametres);
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
		log.debug("Fin m�thode majDonnees");
	}
	/**
	 * @see com.dlecan.phenochiot.admin.Fiche#sauver()
	 */
	protected void sauver() {
		log.debug("D�but m�thode sauver");

		GestionnaireAccesDonnees gad = null;

        try {
            gad = GestionnaireAccesDonnees
                    .getInstance(GestionnaireAccesDonnees.TYPE_ADMIN);

            List parametres = generateur.getParametres();

            parametres.add(new Integer(getRace()));

            gad.executerUpdate(9, parametres);

            setModifie(false);

        } catch (ExceptionTechnique e) {
            log.error(e.getMessage(), e);
            Administration.afficherErreur(getShell(), e);
        } finally {
            gad.terminerRequete();
        }

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