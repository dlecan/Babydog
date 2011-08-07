package com.dlecan.phenochiot.framework.exception;

import org.apache.log4j.Logger;

/**
 * @author dam
 *
 * @revision $Revision: 1.1 $
 * @version $Name:  $
 * @date $Date: 2003/11/04 21:45:13 $
 * @id $Id: ExceptionFonctionnelle.java,v 1.1 2003/11/04 21:45:13 fris Exp $
 */
public class ExceptionFonctionnelle extends Exception {

	/**
	 * Logger de la classe
	 */
	private static Logger log = Logger.getLogger(ExceptionFonctionnelle.class);

	/**
	 * 
	 */
	public ExceptionFonctionnelle() {
		super();
		log.debug("Début constructeur ExceptionFonctionnelle");
		// TODO Auto-generated constructor stub
		log.debug("Début constructeur ExceptionFonctionnelle");
	}

	/**
	 * @param arg0
	 */
	public ExceptionFonctionnelle(String arg0) {
		super(arg0);
		log.debug("Début constructeur ExceptionFonctionnelle");
		// TODO Auto-generated constructor stub
		log.debug("Début constructeur ExceptionFonctionnelle");
	}

	/**
	 * @param arg0
	 */
	public ExceptionFonctionnelle(Throwable arg0) {
		super(arg0);
		log.debug("Début constructeur ExceptionFonctionnelle");
		// TODO Auto-generated constructor stub
		log.debug("Début constructeur ExceptionFonctionnelle");
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ExceptionFonctionnelle(String arg0, Throwable arg1) {
		super(arg0, arg1);
		log.debug("Début constructeur ExceptionFonctionnelle");
		// TODO Auto-generated constructor stub
		log.debug("Début constructeur ExceptionFonctionnelle");
	}

}
