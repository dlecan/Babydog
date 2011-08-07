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


package com.dlecan.phenochiot.framework.exception;

/**
 * @author dam
 *
 * @revision $Revision: 1.1 $
 * @version $Name:  $
 * @date $Date: 2003/11/04 21:45:12 $
 * @id $Id: ExceptionTechnique.java,v 1.1 2003/11/04 21:45:12 fris Exp $
 */
public class ExceptionTechnique extends Exception {

	static final long serialVersionUID = -3387516993124432948L;
	
	/**
	 * 
	 */
	public ExceptionTechnique() {
		super();
	}

	/**
	 * @param arg0
	 */
	public ExceptionTechnique(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ExceptionTechnique(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ExceptionTechnique(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
