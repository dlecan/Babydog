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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
public class Divers {

	/**
	 * @param shell
	 * @return
	 */
	public static Point calculerCentreEcran(Shell shell) {
		Rectangle shellRect = shell.getBounds();
		Rectangle displayRect = shell.getDisplay().getBounds();
		int x = (displayRect.width - shellRect.width) / 2;
		int y = (displayRect.height - shellRect.height) / 2;
		return new Point(x, y);
	}

	/**
	 * @param fond
	 * @param premierPlan
	 * @return
	 */
	public static Point calculerCentreApplication(Shell fond, Shell premierPlan) {
		Rectangle premierPlanRect = premierPlan.getBounds();
		Rectangle fondRect = fond.getBounds();
		int x = (fondRect.width - premierPlanRect.width) / 2;
		int y = (fondRect.height - premierPlanRect.height) / 2;
		return new Point(x, y);
	}

}