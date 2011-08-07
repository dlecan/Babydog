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
import java.util.Iterator;
import java.util.List;


/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
class ItemRacine implements ItemArbre {

	private final VueRecherche recherche;

	private String name;

	private List<ItemRace> races;

	private List<ItemGroupe> groupes;

	/**
	 * @param nName
	 * @param nRecherche TODO
	 */
	public ItemRacine(VueRecherche nRecherche, String nName) {
		name = nName;
		recherche = nRecherche;
		races = new ArrayList<ItemRace>();
		groupes = new ArrayList<ItemGroupe>();
	}

	/**
	 * @param child
	 */
	public void addChild(ItemRace child) {
		races.add(child);
		child.setParent(this);
	}

	/**
	 * @param child
	 */
	public void addChild(ItemGroupe child) {
		if (!groupes.contains(child)) {
			groupes.add(child);
			child.setParent(this);
		}
	}

	/**
	 * @param child
	 */
	public void removeChild(ItemRace child) {
		races.remove(child);
		child.setParent((ItemRace) null);
	}

	/**
	 * @param child
	 */
	public void removeChild(ItemGroupe child) {
		groupes.remove(child);
		child.setParent((ItemGroupe) null);
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getChildren()
	 */
	public ItemArbre[] getChildren() {
		switch (recherche.getOrientation()) {
			case VueRecherche.SANS_GROUPE :
				return races.toArray(new ItemRace[races
						.size()]);
			case VueRecherche.EN_GROUPE :
			default :
				return groupes
						.toArray(new ItemGroupe[groupes.size()]);
		}
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#hasChildren()
	 */
	public boolean hasChildren() {
		switch (recherche.getOrientation()) {
			case VueRecherche.SANS_GROUPE :
				return races.size() > 0;
			case VueRecherche.EN_GROUPE :
			default :
				return groupes.size() > 0;
		}
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nParent
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#setParent(ItemArbre)
	 */
	public void setParent(ItemArbre nParent) {
		// Rien
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getParent()
	 */
	public ItemArbre getParent() {
		return null;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * @param idGroupe
	 * @return L'item du groupe
	 */
	public ItemGroupe fabriqueGroupe(int idGroupe) {
		ItemGroupe nouveauGroupe = new ItemGroupe(idGroupe);
		int position = groupes.indexOf(nouveauGroupe);

		if (position > -1) {
			return groupes.get(position);
		}
		return nouveauGroupe;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#removeAll()
	 */
	public void removeAll() {
		for (Iterator iter = groupes.iterator(); iter.hasNext();) {
			ItemGroupe element = (ItemGroupe) iter.next();
			element.removeAll();
			element.setParent(null);
		}
		groupes.clear();
		for (Iterator iter = races.iterator(); iter.hasNext();) {
			((ItemRace) iter.next()).setParent((ItemRacine) null);
		}
		races.clear();
	}
}