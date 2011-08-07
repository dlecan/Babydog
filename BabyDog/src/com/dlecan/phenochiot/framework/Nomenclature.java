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


package com.dlecan.phenochiot.framework;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.dlecan.phenochiot.consultation.BabyDogApp;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class Nomenclature {

	private static final String FICHIER_NOMENCLATURE = "nomenclature.xml";

	private static int POS_GP = 100;

	private static int POS_SEC = 10;

	private static int POS_SSS = 1;

	private static Map<Integer, String> tableNomenclature = new HashMap<Integer, String>();

	static {
		new Nomenclature();
	}

	/**
	 * Constructeur chargeant les données du fichier de description de la
	 * nomenclature.
	 */
	protected Nomenclature() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = null;
		Document document = null;

		try {
			parser = factory.newDocumentBuilder();
			document = parser.parse(new InputSource(Platform
					.getBundle(BabyDogApp.PLUGIN_ID)
					.getEntry("conf/" + FICHIER_NOMENCLATURE).openStream()));
		} catch (MalformedURLException e) {
			// A gérer
		} catch (SAXException e) {
			// A gérer
		} catch (IOException e) {
			// A gérer
		} catch (ParserConfigurationException e) {
			// A gérer
		}

		Element nomenclature = document.getDocumentElement();
		NodeList listeGroupes = nomenclature.getElementsByTagName("groupe");

		int nbGroupes = listeGroupes.getLength();

		for (int i = 0; i < nbGroupes; i++) {
			Element noeudGroupe = (Element) listeGroupes.item(i);
			Integer idGroupe = Integer.valueOf(noeudGroupe.getAttribute("id"));
			String nomGroupe = noeudGroupe.getAttribute("nom");
			tableNomenclature.put(new Integer(idGroupe.intValue() * POS_GP),
					nomGroupe);

			NodeList listeSections = noeudGroupe
					.getElementsByTagName("section");
			int nbSections = listeSections.getLength();

			for (int j = 0; j < nbSections; j++) {
				Element noeudSection = (Element) listeSections.item(j);
				Integer idSection = Integer.valueOf(noeudSection
						.getAttribute("id"));
				String nomSection = noeudSection.getAttribute("nom");
				tableNomenclature.put(new Integer(idGroupe.intValue()
													* POS_GP
													+ idSection.intValue()
													* POS_SEC), nomSection);

				NodeList listeSsSections = noeudSection
						.getElementsByTagName("sssection");
				int nbSsSections = listeSsSections.getLength();

				for (int k = 0; k < nbSsSections; k++) {
					Element noeudSsSection = (Element) listeSsSections.item(k);
					Integer idSsSection = Integer.valueOf(noeudSsSection
							.getAttribute("id"));
					String nomSsSection = noeudSsSection.getAttribute("nom");
					tableNomenclature.put(new Integer(idGroupe.intValue()
														* POS_GP
														+ idSection.intValue()
														* POS_SEC
														+ idSsSection
																.intValue()
														* POS_SSS),
							nomSsSection);
				}
			}
		}
	}

	/**
	 * @param idGroupe
	 * @return Le groupe
	 */
	public static String getGroupe(int idGroupe) {
		return tableNomenclature.get(new Integer(idGroupe * POS_GP));
	}

	/**
	 * @param idGroupe
	 * @param idSection
	 * @return La section
	 */
	public static String getSection(int idGroupe, int idSection) {
		return tableNomenclature.get(new Integer(idGroupe
															* POS_GP
															+ idSection
															* POS_SEC));
	}

	/**
	 * @param idGroupe
	 * @param idSection
	 * @param idSsSection
	 * @return La sous-section
	 */
	public static String getSsSection(int idGroupe, int idSection,
			int idSsSection) {
		return tableNomenclature.get(new Integer(idGroupe
															* POS_GP
															+ idSection
															* POS_SEC
															+ idSsSection
															* POS_SSS));
	}
}