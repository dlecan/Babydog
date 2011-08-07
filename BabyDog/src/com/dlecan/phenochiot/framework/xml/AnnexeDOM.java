/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


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
	 * @param element
	 * @param nom
	 * @return
	 */
	public static String trouveTexte(Element element, String nom) {
		Element elementNom = trouvePremierElement(element, nom);
		return trouveTexte(elementNom);
	}

	/**
	 * @param element
	 * @param nom
	 * @return
	 */
	public static Element trouvePremierElement(Element element, String nom) {
		NodeList nl = element.getElementsByTagName(nom);
		return (Element) nl.item(0);
	}

	/**
	 * @param element
	 * @return
	 */
	public static String trouveTexte(Element element) {
		String retour = "";
		if (element != null) {
			Node noeud = element.getFirstChild();
			if (noeud != null) {
				retour = noeud.getNodeValue();
			}
		}
		return retour;
	}
}