package com.dlecan.phenochiot.framework.exception;

import org.apache.log4j.Logger;

/**
 * @author dam
 *
 * @revision $Revision: 1.1 $
 * @version $Name:  $
 * @date $Date: 2003/11/04 21:45:12 $
 * @id $Id: ExceptionTechnique.java,v 1.1 2003/11/04 21:45:12 fris Exp $
 */
public class ExceptionTechnique extends Exception {

	/**
	 * Logger de la classe
	 */
	private static Logger log = Logger.getLogger(ExceptionTechnique.class);
	
	/**
	 * 
	 */
	public ExceptionTechnique() {
		super();
		log.debug("Début constructeur ExceptionTechnique");
		// TODO Auto-generated constructor stub
		log.debug("Début constructeur ExceptionTechnique");
	}

	/**
	 * @param arg0
	 */
	public ExceptionTechnique(String arg0) {
		super(arg0);
		log.debug("Début constructeur ExceptionTechnique");
		// TODO Auto-generated constructor stub
		log.debug("Début constructeur ExceptionTechnique");
	}

	/**
	 * @param arg0
	 */
	public ExceptionTechnique(Throwable arg0) {
		super(arg0);
		log.debug("Début constructeur ExceptionTechnique");
		// TODO Auto-generated constructor stub
		log.debug("Début constructeur ExceptionTechnique");
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ExceptionTechnique(String arg0, Throwable arg1) {
		super(arg0, arg1);
		log.debug("Début constructeur ExceptionTechnique");
		// TODO Auto-generated constructor stub
		log.debug("Début constructeur ExceptionTechnique");
	}

}
