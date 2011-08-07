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


package com.dlecan.phenochiot.consultation.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.dlecan.phenochiot.consultation.BabyDogApp;

/**
 * 
 * @author Dam
 *
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class NouvelleRace extends Action {

	/**
	 * 
	 */
	private final IWorkbenchWindow builder;

	/**
	 * 
	 */
	private static int counter = 10000;

	/**
	 * 
	 */
	private int numRace;

	/**
	 * @param window
	 * @param text
	 */
	public NouvelleRace(IWorkbenchWindow window, String text) {
		super(text);
		this.builder = window;
		numRace = ++counter;
	}

	/**
	 * @param window
	 * @param nNumRace
	 */
	public NouvelleRace(IWorkbenchWindow window, int nNumRace) {
		this(window, null);
		numRace = nNumRace;
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		try {
			String secondaryId = Integer.toString(numRace);
			builder.getActivePage().showView(	BabyDogApp.RACE_VIEW_ID,
												secondaryId,
												IWorkbenchPage.VIEW_ACTIVATE);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}