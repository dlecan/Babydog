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


package com.dlecan.phenochiot.consultation.vue.recherche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ContextInformationValidator;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.dlecan.phenochiot.consultation.utils.Logger;

/**
 * @author SII - dlecan
 * @version.interne $Id$
 * @version $Name$
 * @date $Date$
 */
public class Completion implements IContentAssistProcessor {
	
	private final static char[] ALL = new char[]{'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z'};

	private List<String> listeMots = new ArrayList<String>();

	/**
	 * Constructeur
	 */
	public Completion() {
		super();
	}

	/**
	 * @param element
	 */
	public void ajouterElement(String element) {
		if (!listeMots.contains(element) && !"".equals(element)) {
			listeMots.add(element);
			Collections.sort(listeMots);
		}
	}

	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer,
	 *      int)
	 * @param viewer
	 * @param documentOffset
	 * @return Une liste de proposition de completion
	 */
	public ICompletionProposal[] computeCompletionProposals(
			ITextViewer viewer, int documentOffset) {
		try {
			String text = viewer.getDocument().get(0, documentOffset);
			return getCompletionProposal(text);
		} catch (BadLocationException e) {
			Logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
	 * @return Un tableau de char
	 */
	public char[] getCompletionProposalAutoActivationCharacters() {
		return ALL;
	}

	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationAutoActivationCharacters()
	 * @return <code>null</code>
	 */
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeContextInformation(org.eclipse.jface.text.ITextViewer,
	 *      int)
	 * @param viewer
	 * @param documentOffset
	 * @return Des informations
	 */
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int documentOffset) {
		return new ContextInformation[0];
	}

	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage()
	 * @return "Error"
	 */
	public String getErrorMessage() {
		return "Error";
	}

	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationValidator()
	 * @return Des informations
	 */
	public IContextInformationValidator getContextInformationValidator() {
		return new ContextInformationValidator(this);
	}

	/**
	 * @param prefix
	 * @return Un ensemble de propositions de completion
	 */
	private ICompletionProposal[] getCompletionProposal(String prefix) {
		List<CompletionProposal> completionsProposal = new ArrayList<CompletionProposal>();

		Iterator iter = listeMots.iterator();

		while (iter.hasNext()) {
			String element = (String) iter.next();
			if (element.startsWith(prefix)) {
				completionsProposal
						.add(new CompletionProposal(element, 0, prefix
								.length(), 0));
			}
		}
		return completionsProposal
				.toArray(new ICompletionProposal[completionsProposal.size()]);
	}
}