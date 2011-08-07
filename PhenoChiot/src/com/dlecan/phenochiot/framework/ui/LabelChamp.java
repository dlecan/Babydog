package com.dlecan.phenochiot.framework.ui;

import org.apache.log4j.Logger;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.dlecan.phenochiot.admin.PanneauDroit;

/**
 * @author dam
 *
 * @revision $Revision: 1.5 $
 * @version $Name:  $
 * @date $Date: 2004/01/10 21:49:02 $
 * @id $Id: LabelChamp.java,v 1.5 2004/01/10 21:49:02 fris Exp $
 */
public class LabelChamp {

    /**
     * Logger de la classe
     */
    private static Logger log = Logger.getLogger(LabelChamp.class);

    /**
     * 
     */
    private Label label;

    /**
     *  
     */
    private TextViewer texte;

    /**
     * 
     */
    private String nomChampSql;

    /**
     * 
     */
    private Composite parent;

    /**
     * 
     */
    private String type;

    /**
     * 
     * @param nParent
     * @param nPanneau
     * @param nLabel
     * @param nNomChampSql
     * @param nType
     */
    public LabelChamp(Composite nParent, PanneauDroit nPanneau, String nLabel,
            String nNomChampSql, String nType) {
        this(nParent, nPanneau, nLabel, nNomChampSql, 80, 20, nType);
    }

    /**
     * 
     * @param nParent
     * @param nPanneau
     * @param nLabel
     * @param nNomChampSql
     * @param nLongueur
     * @param nHauteur
     * @param nType
     */
    public LabelChamp(Composite nParent, PanneauDroit nPanneau, String nLabel,
            String nNomChampSql, int nLongueur, int nHauteur, String nType) {
        log.debug("Début constructeur LabelChamp");

        nomChampSql = nNomChampSql;
        parent = nParent;
        type = nType;

        Composite contenant = new Composite(nParent, SWT.NONE);
        GridData gridDataContenant = new GridData();
        gridDataContenant.horizontalAlignment = GridData.FILL;
        gridDataContenant.grabExcessHorizontalSpace = true;
        contenant.setLayoutData(gridDataContenant);

        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        contenant.setLayout(layout);

        label = new Label(contenant, SWT.NONE);
        label.setText(nLabel);

        GridData gridDataLabel = new GridData();

        GridData gridDataTexte = new GridData();
        gridDataTexte.horizontalAlignment = GridData.FILL;

        if (nHauteur == -1) {
            texte = new TextViewer(contenant, SWT.BORDER | SWT.SINGLE);
        } else {
            texte = new TextViewer(contenant, SWT.BORDER | SWT.MULTI | SWT.WRAP
                    | SWT.V_SCROLL);
        }

        GC gc = new GC(texte.getControl());
        FontMetrics fm = gc.getFontMetrics();

        if (nLongueur != -1) {
            gridDataTexte.widthHint = nLongueur * fm.getAverageCharWidth();
        } else {
            gridDataTexte.grabExcessHorizontalSpace = true;
        }

        if (nHauteur == -1) {
            gridDataTexte.heightHint = (int) Math.round(fm.getHeight() * 1.2);
            gridDataLabel.verticalAlignment = GridData.CENTER;
        } else {
            gridDataTexte.heightHint = nHauteur;
            gridDataLabel.verticalAlignment = GridData.BEGINNING;
        }

        gc.dispose();

        texte.appendVerifyKeyListener(nPanneau);

        texte.getControl().setLayoutData(gridDataTexte);
        label.setLayoutData(gridDataLabel);

        log.debug("Fin constructeur LabelChamp");
    }

    /**
     * 
     *
     */
    public void dispose() {
        log.debug("Début méthode dispose");
        label.dispose();
        //        texte.dispose();
        log.debug("Fin méthode dispose");
    }

    /**
     * 
     * @param nText
     */
    public void setText(String nText) {
        IDocument doc = texte.getDocument();
        if (doc == null) {
            texte.setDocument(new Document(nText));
        } else {
            doc.set(nText);
        }
    }

    /**
     * @return Retourne texte, mais dans le bon type
     */
    public Object getText() {
        log.debug("Accesseur getText");
        String valeur = texte.getDocument().get();
        Object retour = null;

        if ("numerique".equals(type)) {
            if (valeur.indexOf(',') > -1 || valeur.indexOf('.') > -1) {
                retour = new Float(valeur.replace(',', '.'));
            } else if ("".equals(valeur)) {
            	retour = null;
            } else {
                retour = new Integer(valeur);
            }
        } else {
            retour = valeur;
        }

        return retour;
    }

    /**
     * @return Retourne nomChampSql.
     */
    public String getNomChampSql() {
        log.debug("Accesseur getNomChampSql");
        return nomChampSql;
    }

    /**
     * 
     * @param valeur
     */
    public void afficher(String valeur) {
        setText(valeur);
    }

    /**
     * @return Retourne parent.
     */
    public Composite getParent() {
        log.debug("Accesseur getParent");
        return parent;
    }

    /**
     * @return Retourne type.
     */
    public String getType() {
        log.debug("Accesseur getType");
        return type;
    }
}
