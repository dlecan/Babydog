package com.dlecan.phenochiot.framework.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.dlecan.phenochiot.framework.exception.ExceptionTechnique;

/**
 * @author dam
 * 
 * @revision $Revision: 1.6 $ 
 * @date $Date: 2004/01/09 18:25:47 $ 
 * @id $Id: GestionnaireAccesDonnees.java,v 1.6 2004/01/09 18:25:47 fris Exp $
 * @version $Name:  $
 */
public class GestionnaireAccesDonnees {

    /**
     * Le type admin des requ�tes
     */
    public static final int TYPE_ADMIN = 0;

    /**
     * Le type client des requ�tes
     */
    public static final int TYPE_CLIENT = 1;

    /**
     * Le fichier des requ�tes d'administration
     */
    private static final String NOM_FICHIER_REQUETES_ADMIN = "requetes_admin.properties";

    /**
     * Le fichier des requ�tes d'administration
     */
    private static final String NOM_FICHIER_REQUETES_CLIENT = "requetes_client.properties";

    /**
     * Logger de la classe
     */
    private static Logger log = Logger
            .getLogger(GestionnaireAccesDonnees.class);

    /**
     * La connexion � la base de donn�es
     */
    private static Connection connexion = null;

    /**
     * Le gestionnaire d'acc�s au donn�es du singleton
     */
    private static GestionnaireAccesDonnees gad = null;

    /**
     * Les propri�t�s repr�sentant les requ�tes SQL ex�cutables
     */
    private static Properties requetes = null;

    /**
     * 
     */
    private static Statement stmt = null;

    /**
     * 
     */
    private static PreparedStatement pstmt = null;

    /**
     * Un select
     */
    private int REQUETE_SELECT = 0;

    /**
     * Un update
     */
    private int REQUETE_UPDATE = 1;

    /**
     * Un delete
     */
    private int REQUETE_DELETE = 2;

    /**
     * @param type
     * @throws ExceptionTechnique
     */
    private GestionnaireAccesDonnees(int type) throws ExceptionTechnique {

        if (type == TYPE_ADMIN) {
            chargerFichierRequetes(NOM_FICHIER_REQUETES_ADMIN);
        } else {
            chargerFichierRequetes(NOM_FICHIER_REQUETES_CLIENT);
        }

        chargerPilote();
        instancierConnexion();
    }

    /**
     * @param nomFichierRequetes
     * @throws ExceptionTechnique
     */
    private void chargerFichierRequetes(String nomFichierRequetes)
            throws ExceptionTechnique {
        log.debug("D�but m�thode chargerFichierRequetes");

        InputStream entree = getClass().getResourceAsStream(
                "/" + nomFichierRequetes);

        if (entree != null) {
            requetes = new Properties();
            try {
                requetes.load(entree);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new ExceptionTechnique(
                        "Impossible de charger le fichier "
                                + "configuration des requ�tes");
            }
        } else {
            log.error("Impossible de trouver le fichier de configuration des "
                    + "requ�tes SQL");
            throw new ExceptionTechnique("Impossible de trouver le fichier "
                    + "de configuration des requ�tes SQL");
        }

        log.debug("Fin m�thode chargerFichierRequetes");
    }

    /**
     * @param type
     * @return
     * @throws ExceptionTechnique
     */
    public static GestionnaireAccesDonnees getInstance(int type)
            throws ExceptionTechnique {
        log.debug("D�but m�thode GestionnaireAccesDonnees");
        if (gad == null) {
            synchronized (GestionnaireAccesDonnees.class) {
                if (gad == null) {
                    gad = new GestionnaireAccesDonnees(type);
                }
            }
        }
        log.debug("Fin m�thode GestionnaireAccesDonnees");
        return gad;
    }

    /**
     * @throws ExceptionTechnique
     *  
     */
    private void instancierConnexion() throws ExceptionTechnique {
        log.debug("D�but m�thode instancierConnexion");

        try {
            connexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost/phenochiot", "root", "");
            stmt = connexion.createStatement();
        } catch (SQLException e) {
            log.error(
                    "Impossible d'�tablir la connexion avec la base de donn�es : "
                            + e.getMessage(), e);
            throw new ExceptionTechnique("Impossible d'�tablir la connexion "
                    + "avec la base de donn�es");
        }

        log.debug("Fin m�thode instancierConnexion");
    }

    /**
     * @throws ExceptionTechnique
     */
    private void chargerPilote() throws ExceptionTechnique {
        log.debug("D�but m�thode chargerPilote");
        try {
            Class.forName("com.p6spy.engine.spy.P6SpyDriver");
        } catch (ClassNotFoundException e) {
            log.error(
                    "Impossible de charge le pilote JDBC : " + e.getMessage(),
                    e);
            throw new ExceptionTechnique("Impossible de charger "
                    + "le pilote JDBC");
        } finally {
            log.debug("Fin m�thode chargerPilote");
        }
    }

    /**
     * 
     * @param type
     * @param numRequete
     * @param parametres
     * @return
     * @throws ExceptionTechnique
     */
    private ResultSet executerRequete(int type, int numRequete, List parametres)
            throws ExceptionTechnique {
        log.debug("D�but m�thode executerRequete");

        ResultSet retour = null;
        String nomRequete = null;

        try {
            nomRequete = requetes.getProperty(Integer.toString(numRequete));

            if (nomRequete != null) {
                if (REQUETE_UPDATE == type) {
                    pstmt = ajouterParametres(nomRequete, parametres);
                    pstmt.executeUpdate();
                } else if (REQUETE_DELETE == type) {
                    retour = null;
                } else if (REQUETE_SELECT == type) {
                    if (parametres != null) {
                        pstmt = ajouterParametres(nomRequete, parametres);
                        retour = pstmt.executeQuery();
                    } else {
                        retour = stmt.executeQuery(nomRequete);
                    }
                }
            } else {
                throw new ExceptionTechnique(
                        "Impossible de trouver la requ�te #" + numRequete);
            }

        } catch (SQLException e) {
            log.error("Impossible d'ex�cuter la requ�te " + nomRequete + ", "
                    + e.getMessage(), e);
            throw new ExceptionTechnique(
                    "Impossible d'ex�cuter une requ�te SQL");
        } finally {
            log.debug("Fin m�thode executerRequete");
        }
        return retour;
    }

    /**
     * @param nomRequete
     * @param parametres
     * @return
     * @throws ExceptionTechnique
     */
    private PreparedStatement ajouterParametres(String nomRequete,
            List parametres) throws ExceptionTechnique {
        log.debug("D�but m�thode ajouterParametres");

        try {
            pstmt = connexion.prepareStatement(nomRequete);

            for (int i = 0; i < parametres.size(); i++) {
                Object elt = parametres.get(i);

                if (elt == null) {
                	pstmt.setNull(i + 1, Types.NULL);
                } else if (elt instanceof String) {
                    pstmt.setString(i + 1, (String) elt);
                } else if (elt instanceof Integer) {
                    pstmt.setInt(i + 1, ((Integer) elt).intValue());
                } else if (elt instanceof Long) {
                    pstmt.setLong(i + 1, ((Long) elt).longValue());
                } else if (elt instanceof Float) {
                    pstmt.setFloat(i + 1, ((Float) elt).floatValue());
                } else if (elt instanceof Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((Date) elt)
                            .getTime()));
                } else {
                    log.error("Type non reconnu");
                    throw new ExceptionTechnique(
                            "Impossible de reconna�tre le type de l'objet");
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new ExceptionTechnique("Impossible de pr�parer "
                    + "l'ex�cution de la requ�te SQL");
        }

        log.debug("Fin m�thode ajouterParametres");
        return pstmt;
    }

    /**
     * 
     * @param numRequete
     * @return
     * @throws ExceptionTechnique
     */
    public ResultSet executerSelect(int numRequete) throws ExceptionTechnique {
        log.debug("M�thode executerSelect");
        return executerSelect(numRequete, null);
    }

    /**
     * 
     * @param numRequete
     * @param parametres
     * @return
     * @throws ExceptionTechnique
     */
    public ResultSet executerSelect(int numRequete, ArrayList parametres)
            throws ExceptionTechnique {
        log.debug("M�thode executerSelect");
        return executerRequete(REQUETE_SELECT, numRequete, parametres);
    }

    /**
     * 
     * @param numRequete
     * @param parametres
     * @throws ExceptionTechnique
     */
    public void executerUpdate(int numRequete, List parametres)
            throws ExceptionTechnique {
        log.debug("M�thode executerUpdate");
        executerRequete(REQUETE_UPDATE, numRequete, parametres);
    }

    /**
     * 
     */
    public void terminerRequete() {
        terminerRequete(null);
    }

    /**
     * @param resultats
     */
    public void terminerRequete(ResultSet resultats) {
        log.debug("D�but m�thode terminerRequete");
        if (resultats != null) {
            try {
                resultats.close();
            } catch (SQLException sqlEx) {
                log.debug(sqlEx.getMessage());
                resultats = null;
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException sqlEx) {
                log.debug(sqlEx.getMessage());
                pstmt = null;
            }
        }

        log.debug("Fin m�thode terminerRequete");
    }
}