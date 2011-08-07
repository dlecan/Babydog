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


package com.dlecan.phenochiot.framework.db;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;

import com.dlecan.phenochiot.consultation.BabyDogApp;
import com.dlecan.phenochiot.consultation.PluginBabyDog;
import com.dlecan.phenochiot.consultation.utils.Logger;
import com.dlecan.phenochiot.framework.exception.ExceptionTechnique;

/**
 * @author dam
 * @revision $Revision: 1.6 $
 * @date $Date: 2004/01/09 18:25:47 $
 * @id $Id: GestionnaireAccesDonnees.java,v 1.6 2004/01/09 18:25:47 fris Exp $
 * @version $Name: $
 */
public class GestionnaireAccesDonnees {

	/**
	 * Le type admin des requêtes
	 */
	public static final int TYPE_ADMIN = 0;

	/**
	 * Le type client des requêtes
	 */
	public static final int TYPE_CLIENT = 1;

	/**
	 * Le fichier des requêtes d'administration
	 */
	private static final String NOM_FICHIER_REQUETES_ADMIN = "requetes_admin.properties";

	/**
	 * Le fichier des requêtes d'administration
	 */
	private static final String NOM_FICHIER_REQUETES_CONSULTATION = "requetes_consultation.properties";

	/**
	 * La connexion à la base de données
	 */
	private static Connection connexion = null;

	/**
	 * Le gestionnaire d'accès au données du singleton
	 */
	private static GestionnaireAccesDonnees gad = null;

	/**
	 * Les propriétés représentant les requêtes SQL exécutables
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
			chargerFichierRequetes(NOM_FICHIER_REQUETES_CONSULTATION);
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
		InputStream flux = null;

		URL fichier = getClass().getResource("/" + nomFichierRequetes);

		if (fichier == null) {
			fichier = Platform.getBundle(BabyDogApp.PLUGIN_ID)
					.getEntry("conf/" + nomFichierRequetes);
		}

		try {
			if (fichier != null) {
				requetes = new Properties();
				try {
					flux = fichier.openStream();
					requetes.load(flux);
				} catch (IOException e) {
					Logger.error(e.getMessage(), e);
					throw new ExceptionTechnique("Impossible de charger le fichier "
													+ "configuration des requêtes");
				}
			} else {
				Logger
						.error("Impossible de trouver le fichier de configuration des "
								+ "requêtes SQL");
				throw new ExceptionTechnique("Impossible de trouver le fichier "
												+ "de configuration des requêtes SQL");
			}
		} finally {
			if (flux != null) {
				try {
					flux.close();
				} catch (IOException e) {
					// Rien
				}
			}
		}
	}

	/**
	 * @param type
	 * @return Un ResultSet
	 * @throws ExceptionTechnique
	 */
	public static GestionnaireAccesDonnees getInstance(int type)
			throws ExceptionTechnique {
		if (gad == null) {
			synchronized (GestionnaireAccesDonnees.class) {
				if (gad == null) {
					gad = new GestionnaireAccesDonnees(type);
				}
			}
		}
		return gad;
	}

	/**
	 * @throws ExceptionTechnique
	 */
	private void instancierConnexion() throws ExceptionTechnique {
		try {
			//		String url = "jdbc:mysql://localhost/phenochiot";
			URL urlConf = Platform.asLocalURL(PluginBabyDog.getDefault()
					.getBundle().getEntry("db/db.conf"));
			String url = "jdbc:mckoi:local://" + urlConf.getFile();

			connexion = DriverManager.getConnection(url, "root", "master");
			stmt = connexion.createStatement();
		} catch (Exception e) {
			Logger
					.error("Impossible d'établir la connexion avec la base de données : "
							+ e.getMessage(),
							e);
			throw new ExceptionTechnique("Impossible d'établir la connexion "
											+ "avec la base de données");
		}
	}

	/**
	 * @throws ExceptionTechnique
	 */
	private void chargerPilote() throws ExceptionTechnique {
		try {
			//			Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mckoi.JDBCDriver");
		} catch (ClassNotFoundException e) {
			Logger.error("Impossible de charge le pilote JDBC : "
						+ e.getMessage(), e);
			throw new ExceptionTechnique("Impossible de charger "
											+ "le pilote JDBC");
		}
	}

	/**
	 * @param type
	 * @param numRequete
	 * @param parametres
	 * @return Un ResultSet
	 * @throws ExceptionTechnique
	 */
	private ResultSet executerRequete(int type, int numRequete, List<? extends Object> parametres)
			throws ExceptionTechnique {
		ResultSet retour = null;
		String nomRequete = null;

		try {
			nomRequete = requetes.getProperty(Integer.toString(numRequete));

			if (nomRequete != null) {
				retour = executerRequete(type, parametres, nomRequete);
			} else {
				throw new ExceptionTechnique("Impossible de trouver la requête #"
												+ numRequete);
			}

		} catch (SQLException e) {
			Logger.error("Impossible d'exécuter la requête "
						+ nomRequete
						+ ", "
						+ e.getMessage(), e);
			throw new ExceptionTechnique("Impossible d'exécuter une requête SQL");
		}
		return retour;
	}

	/**
	 * @param requete
	 * @param parametres
	 * @return Un ResultSet
	 * @throws ExceptionTechnique
	 */
	public ResultSet executerRequeteDynamique(String requete, List<? extends Object> parametres)
			throws ExceptionTechnique {
		ResultSet retour = null;

		try {
			retour = executerRequete(REQUETE_SELECT, parametres, requete);

		} catch (SQLException e) {
			Logger.error("Impossible d'exécuter la requête "
						+ requete
						+ ", "
						+ e.getMessage(), e);
			throw new ExceptionTechnique("Impossible d'exécuter une requête SQL");
		}
		return retour;
	}

	/**
	 * @param type
	 * @param parametres
	 * @param nomRequete
	 * @return Un ResultSet
	 * @throws ExceptionTechnique
	 * @throws SQLException
	 */
	private ResultSet executerRequete(int type, List<? extends Object> parametres,
			String nomRequete) throws ExceptionTechnique, SQLException {
		ResultSet retour = null;

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
		return retour;
	}

	/**
	 * @param nomRequete
	 * @param parametres
	 * @return Un ResultSet
	 * @throws ExceptionTechnique
	 */
	private PreparedStatement ajouterParametres(String nomRequete,
			List<? extends Object> parametres) throws ExceptionTechnique {
		try {
			pstmt = connexion.prepareStatement(nomRequete);

			int nbParametres = parametres.size();

			for (int i = 0; i < nbParametres; i++) {
				Object elt = parametres.get(i);

				if (elt instanceof String) {
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
					Logger.error("Type non reconnu");
					throw new ExceptionTechnique("Impossible de reconnaître le type de l'objet");
				}
			}
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new ExceptionTechnique("Impossible de préparer "
											+ "l'exécution de la requête SQL");
		}
		return pstmt;
	}

	/**
	 * @param numRequete
	 * @return Un ResultSet
	 * @throws ExceptionTechnique
	 */
	public ResultSet executerSelect(int numRequete) throws ExceptionTechnique {
		return executerSelect(numRequete, null);
	}

	/**
	 * @param numRequete
	 * @param parametres
	 * @return Un ResultSet
	 * @throws ExceptionTechnique
	 */
	public ResultSet executerSelect(int numRequete, List<? extends Object> parametres)
			throws ExceptionTechnique {
		return executerRequete(REQUETE_SELECT, numRequete, parametres);
	}

	/**
	 * @param numRequete
	 * @param parametres
	 * @throws ExceptionTechnique
	 */
	public void executerUpdate(int numRequete, List<? extends Object> parametres)
			throws ExceptionTechnique {
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
		if (resultats != null) {
			try {
				resultats.close();
			} catch (SQLException sqlEx) {
				resultats = null;
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException sqlEx) {
				pstmt = null;
			}
		}
	}
}