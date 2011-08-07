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


package com.dlecan.phenochiot.consultation.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.dlecan.phenochiot.consultation.BabyDogApp;
import com.dlecan.phenochiot.consultation.PluginBabyDog;

/**
 * Classe de logging
 * 
 * @author SII - dlecan
 * @version.interne $Id$
 * @version $Name$
 * @date $Date$
 */
public class Logger {
	
	private static final SimpleDateFormat FORMATEUR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	

	/**
	 * Print a debug message to the console. 
	 * Pre-pend the message with the current date and the name of the current thread.
	 */
	public static void debug(String message) {
		afficherSdtOut(message, null);
	}

	/**
	 * Print a debug message to the console. 
	 * Pre-pend the message with the current date and the name of the current thread.
	 */
	public static void debug(String message, Throwable exception) {
		afficherSdtOut(message, exception);
	}

	/**
	 * 
	 * @param message
	 */
	public static void warning(String message) {
		warning(message, null);
	}

	/**
	 * 
	 * @param message
	 * @param exception
	 */
	public static void warning(String message, Throwable exception) {
		log(IStatus.WARNING, message, exception);
	}

	/**
	 * 
	 * @param message
	 */
	public static void info(String message) {
		info(message, null);
	}

	/**
	 * 
	 * @param message
	 * @param exception
	 */
	public static void info(String message, Throwable exception) {
		log(IStatus.INFO, message, exception);
	}

	/**
	 * 
	 * @param message
	 */
	public static void error(String message) {
		error(message, null);
	}

	/**
	 * 
	 * @param message
	 * @param exception
	 */
	public static void error(String message, Throwable exception) {
		log(IStatus.ERROR, message, exception);
	}

	/**
	 * 
	 * @param severite
	 * @param message
	 * @param exception
	 */
	public static void log(int severite, String message, Throwable exception) {
		if (PluginBabyDog.getDefault().isDebugging()) {
			afficherSdtOut(message, exception);
		}
		PluginBabyDog.getDefault().getLog()
				.log(new Status(severite,
								BabyDogApp.PLUGIN_ID,
								IStatus.OK,
								message,
								exception));
	}
	
	private synchronized static void afficherSdtOut(String message, Throwable exception) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(FORMATEUR.format(new Date()));
		buffer.append(" - ["); //$NON-NLS-1$
		buffer.append(Thread.currentThread().getName());
		buffer.append("] "); //$NON-NLS-1$
		buffer.append(message);
		if (exception != null) {
			buffer.append('\n');
			buffer.append(exception.getMessage());
			StackTraceElement[] ste = exception.getStackTrace();
			
			if (ste.length > 0) {
				buffer.append('\n');
			}
			
			for (int i = 0; i < ste.length; i++) {
				StackTraceElement traceElement = ste[i];
				buffer.append(traceElement.toString());
				buffer.append('\n');
			}
		}
		System.out.println(buffer.toString());
	}
}