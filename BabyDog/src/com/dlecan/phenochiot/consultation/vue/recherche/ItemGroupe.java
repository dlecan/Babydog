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
 * 
 * @author Dam
 *
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
class ItemGroupe implements ItemArbre {

	List<ItemRace> races;

	int id;

	ItemRacine racine;

	/**
	 * @param nId
	 */
	public ItemGroupe(int nId) {
		id = nId;
		races = new ArrayList<ItemRace>();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		return null;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getName()
	 */
	public String getName() {
		return "Groupe " + id;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#setParent(ItemArbre)
	 */
	public void setParent(ItemArbre nParent) {
		racine = (ItemRacine) nParent;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getParent()
	 */
	public ItemArbre getParent() {
		return racine;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#hasChildren()
	 */
	public boolean hasChildren() {
		return races.size() > 0;
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
	public void removeChild(ItemRace child) {
		races.remove(child);
		child.setParent((ItemRace) null);
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getChildren()
	 */
	public ItemArbre[] getChildren() {
		return races.toArray(new ItemRace[races.size()]);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ItemGroupe) {
			return ((ItemGroupe) obj).getName().equals(this.getName());
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getName().hashCode();
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
		for (Iterator iter = races.iterator(); iter.hasNext();) {
			((ItemRace) iter.next()).setParent((ItemGroupe) null);
		}
		races.clear();
	}
}