package com.dlecan.phenochiot.framework.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author dam
 *
 * @revision $Revision: 1.2 $
 * @version $Name:  $
 * @date $Date: 2004/01/10 21:49:23 $
 * @id $Id: AnnexeDOM.java,v 1.2 2004/01/10 21:49:23 fris Exp $
 */
public class AnnexeDOM {

    /**
     * 
     * @param element
     * @param nom
     * @return
     */
    public static String trouveTexte(Element element, String nom) {
        Element elementNom = trouvePremierElement(element, nom);
        return trouveTexte(elementNom);
    }

    /**
     * 
     * @param element
     * @param nom
     * @return
     */
    public static Element trouvePremierElement(Element element, String nom) {
        NodeList nl = element.getElementsByTagName(nom);
        return (Element) nl.item(0);
    }

    /**
     * 
     * @param element
     * @return
     */
    public static String trouveTexte(Element element) {
        if (element == null) {
            return "";
        } else {
            Node noeud = element.getFirstChild();
            if (noeud != null) {
                return noeud.getNodeValue();
            } else {
                return "";
            }
        }
    }
}
