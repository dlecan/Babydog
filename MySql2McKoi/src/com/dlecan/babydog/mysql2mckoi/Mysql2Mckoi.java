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


package com.dlecan.babydog.mysql2mckoi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class Mysql2Mckoi {

	/**
	 * @throws Exception
	 */
	public Mysql2Mckoi() throws Exception {

		Connection mckoi = getMckoiConnection();
		Connection mysql = getMysqlConnection();

		BufferedReader fichierRequetes = new BufferedReader(new InputStreamReader(getClass()
				.getResourceAsStream("/requetes_babydog.sql")));

		System.out.println("Initialisation de la base");

		Statement state = mckoi.createStatement();
		String ligne;
		while ((ligne = fichierRequetes.readLine()) != null) {
			state.execute(ligne);
		}
		state.close();

		System.out.println("Traitement des races");

		String requete = "SELECT * FROM races";
		PreparedStatement stt = mysql.prepareStatement(requete);
		ResultSet resulRaces = stt.executeQuery();

		int iterator = 1;

		while (resulRaces.next()) {
			StringBuffer requeteMcKoi = new StringBuffer("INSERT INTO races SET ");
			int nbColonnes = resulRaces.getMetaData().getColumnCount();
			List parametres = new ArrayList(nbColonnes);

			for (int i = 1; i <= nbColonnes; i++) {
				String nomColonne = resulRaces.getMetaData().getColumnName(i);
				Object valeur = resulRaces.getObject(i);

				requeteMcKoi.append(nomColonne);
				requeteMcKoi.append(" = ?");
				if (i != nbColonnes) {
					requeteMcKoi.append(',');
				}
				parametres.add(valeur);
			}

			PreparedStatement sttMcKoi = mckoi.prepareStatement(requeteMcKoi
					.toString());
			for (int i = 0; i < nbColonnes; i++) {
				sttMcKoi.setObject(i + 1, parametres.get(i));
			}

			sttMcKoi.executeQuery();
			sttMcKoi.close();

			if (iterator % 20 == 0) {
				System.out.println(iterator + " races traitées");
			}

			iterator++;
		}

		if (stt != null) {
			stt.close();
		}

		System.out.println("Traitement des caractéristiques");

		String requeteCarac = "SELECT id_race, "
								+ "museau + 0 AS museau, "
								+ "yeux_taille + 0 AS yeux_taille, "
								+ "yeux_ecartement + 0 AS yeux_ecartement, "
								+ "truffe + 0 AS truffe, "
								+ "oreilles + 0 AS oreilles, "
								+ "queue + 0 AS queue, "
								+ "robe FROM caracteristiques";
		PreparedStatement sttCarac = mysql.prepareStatement(requeteCarac);
		ResultSet resulCarac = sttCarac.executeQuery();

		while (resulCarac.next()) {
			StringBuffer requeteMcKoi = new StringBuffer("INSERT INTO caracteristiques SET ");
			int nbColonnes = resulCarac.getMetaData().getColumnCount();
			List parametres = new ArrayList(nbColonnes);

			for (int i = 1; i <= nbColonnes; i++) {
				String nomColonne = resulCarac.getMetaData().getColumnName(i);
				Object valeur = resulCarac.getObject(i);

				requeteMcKoi.append(nomColonne);
				requeteMcKoi.append(" = ?");
				if (i != nbColonnes) {
					requeteMcKoi.append(',');
				}
				parametres.add(valeur);
			}

			PreparedStatement sttMcKoi = mckoi.prepareStatement(requeteMcKoi
					.toString());
			for (int i = 0; i < nbColonnes; i++) {
				sttMcKoi.setObject(i + 1, parametres.get(i));
			}

			sttMcKoi.executeQuery();
			sttMcKoi.close();
		}

		System.out.println("Fermetures des connexions");

		if (sttCarac != null) {
			sttCarac.close();
		}

		if (mckoi != null) {
			mckoi.close();
		}

		if (mysql != null) {
			mysql.close();
		}

		System.out.println("Fin du programme");
	}

	public Connection getMckoiConnection() {
		try {
			Class.forName("com.mckoi.JDBCDriver").newInstance();
		} catch (Exception e) {
			System.out.println("Unable to register the JDBC Driver.\n"
								+ "Make sure the JDBC driver is in the\n"
								+ "classpath.\n");
			System.exit(1);
		}

		String url = "jdbc:mckoi:local://D:/workspace/MySql2McKoi/db.conf";

		String username = "root";
		String password = "master";

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out
					.println("Unable to make a connection to the database.\n"
								+ "The reason: "
								+ e.getMessage());
			System.exit(1);
		}
		return connection;
	}

	public Connection getMysqlConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.out.println("Unable to register the JDBC Driver.\n"
								+ "Make sure the JDBC driver is in the\n"
								+ "classpath.\n");
			System.exit(1);
		}

		String url = "jdbc:mysql://localhost/phenochiot";

		String username = "root";
		String password = "";

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);

		} catch (SQLException e) {
			System.out
					.println("Unable to make a connection to the database.\n"
								+ "The reason: "
								+ e.getMessage());
			System.exit(1);
		}
		return connection;
	}

	public static void main(String[] args) throws Exception {
		new Mysql2Mckoi();
	}
}