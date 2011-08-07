package com.dlecan.phenochiot.utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.dlecan.phenochiot.admin.PanneauDroit;
import com.dlecan.phenochiot.framework.ui.LabelChamp;
import com.dlecan.phenochiot.framework.xml.AnnexeDOM;

/**
 * @author dam
 *
 * @revision $Revision: 1.3 $
 * @version $Name:  $
 * @date $Date: 2004/01/10 21:49:41 $
 * @id $Id: GenerateurContenuFiche.java,v 1.3 2004/01/10 21:49:41 fris Exp $
 */
public class GenerateurContenuFiche {

    /**
     * Logger de la classe
     */
    private static Logger log = Logger.getLogger(GenerateurContenuFiche.class);

    static {
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }

    /**
     * 
     */
    private SimpleDateFormat formateurDate = null;

    /**
     * 
     */
    private String fichierConfiguration;

    /**
     * 
     */
    private LabelChamp[] champs;

    /**
     * 
     */
    private int nbChamps;

    /**
     * 
     * @param configuration
     * @param parent
     * @param panneau
     */
    public GenerateurContenuFiche(String configuration, Composite parent,
            PanneauDroit panneau) {
        fichierConfiguration = configuration;

        formateurDate = new SimpleDateFormat("dd-MM-yyyy");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = null;
        Document document = null;

        try {
            parser = factory.newDocumentBuilder();
            document = parser.parse(new InputSource(getClass()
                    .getResourceAsStream("/" + fichierConfiguration)));
        } catch (SAXException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        Element catalogue = document.getDocumentElement();
        NodeList champsXML = catalogue.getElementsByTagName("champ");

        nbChamps = champsXML.getLength();
        champs = new LabelChamp[nbChamps];

        for (int i = 0; i < nbChamps; i++) {
            String nom = AnnexeDOM.trouveTexte((Element) champsXML.item(i),
                    "nom");
            String sql = AnnexeDOM.trouveTexte((Element) champsXML.item(i),
                    "sql");
            String strLargeur = AnnexeDOM.trouveTexte((Element) champsXML
                    .item(i), "largeur");
            String type = AnnexeDOM.trouveTexte((Element) champsXML.item(i),
                    "type");
            String strHauteur = AnnexeDOM.trouveTexte((Element) champsXML
                    .item(i), "hauteur");

            int largeur = -1;
            int hauteur = -1;

            if (!"".equals(strLargeur)) {
                largeur = Integer.parseInt(strLargeur);
            }

            if (!"".equals(strHauteur)) {
                hauteur = Integer.parseInt(strHauteur);
            }

            champs[i] = new LabelChamp(parent, panneau, nom, sql, largeur,
                    hauteur, type);
        }

    }

    /**
     * @param resultat
     * @throws SQLException
     */
    public void afficher(ResultSet resultat) throws SQLException {
        log.debug("Début méthode afficher");
        for (int i = 0; i < nbChamps; i++) {
            LabelChamp champ = champs[i];
            String type = champ.getType();
            String valeur = "";
            if (type.equals("date")) {
                Date date = resultat.getDate(champ.getNomChampSql());
                if (date != null) {
                    valeur = formateurDate.format(date);
                } else {
                    valeur = "01-01-1970";
                }
            } else {
                valeur = resultat.getString(champ.getNomChampSql());
            }

            if (valeur == null) {
                valeur = "";
            }

            champs[i].afficher(valeur);
        }
        log.debug("Fin méthode afficher");
    }

    /**
     * 
     *
     */
    public void dispose() {
        log.debug("Début méthode dispose");
        for (int i = 0; i < nbChamps; i++) {
            champs[i].dispose();
        }
        log.debug("Fin méthode dispose");
    }

    /**
     * 
     * @return Les paramètres de la requête SQL
     */
    public List getParametres() {
        log.debug("Début méthode getParametres");
        List parametres = new ArrayList();

        for (int i = 0; i < nbChamps; i++) {
            String type = champs[i].getType();
            if (type.equals("numerique")) {
                parametres.add(champs[i].getText());
            } else if (type.equals("texte")) {
                parametres.add(champs[i].getText());
            } else if (type.equals("date")) {
                Date date = null;
                try {
                    date = formateurDate.parse((String) champs[i].getText());
                } catch (ParseException e) {
                    log.error(e.getMessage(), e);
                    date = new Date();
                }
                parametres.add(date);
            } else {
                log.error("Type non reconnu !");
            }
        }

        log.debug("Fin méthode getParametres");
        return parametres;
    }
}