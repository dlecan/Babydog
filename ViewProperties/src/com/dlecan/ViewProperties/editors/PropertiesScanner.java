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


/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.dlecan.ViewProperties.editors;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class PropertiesScanner extends RuleBasedScanner {

	public PropertiesScanner(ColorManager manager) {
		IToken procInstr =
			new Token(
				new TextAttribute(
					manager.getColor(IPropertiesColorConstants.COMMENT)));

		IToken procInstr2 =
			new Token(
				new TextAttribute(
					manager.getColor(IPropertiesColorConstants.STRING)));

		char escape = '\\';
		char escape2 = '#';

		IRule[] rules = new IRule[3];
		//Add rule for processing instructions
		rules[0] = new SingleLineRule("#", "", procInstr, escape, true);
		rules[1] = new SingleLineRule("=", "", procInstr2, escape2, true);
		// Add generic whitespace rule.
		rules[2] = new WhitespaceRule(new PropertiesWhitespaceDetector());

		setRules(rules);
	}
}
