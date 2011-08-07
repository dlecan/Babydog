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

/**
 * @author Dam
 * @revision $Revision$
 * @version $Name$
 * @date $Date$
 * @id $Id$
 */
class ItemRace implements ItemArbre {

	private int id;

	private String name;

	private ItemRacine racine;

	private ItemGroupe groupe;

	/**
	 * @param nName
	 * @param nId
	 */
	public ItemRace(String nName, int nId) {
		id = nId;
		name = nName;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		return null;
	}

	/**
	 * @return Retourne id.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#setParent(ItemArbre)
	 */
	public void setParent(ItemArbre nParent) {
		if (nParent instanceof ItemRacine) {
			setParent((ItemRacine) nParent);
		} else if (nParent instanceof ItemGroupe) {
			setParent((ItemGroupe) nParent);
		}
	}

	/**
	 * @param nParent
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#setParent(ItemArbre)
	 */
	public void setParent(ItemRacine nParent) {
		racine = nParent;
	}

	/**
	 * @param nParent
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#setParent(ItemArbre)
	 */
	public void setParent(ItemGroupe nParent) {
		groupe = nParent;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getParent()
	 */
	public ItemArbre getParent() {
		if (true) {
			return racine;
		}
		return groupe;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#hasChildren()
	 */
	public boolean hasChildren() {
		return false;
	}

	/**
	 * @see com.dlecan.phenochiot.consultation.vue.recherche.ItemArbre#getChildren()
	 */
	public ItemArbre[] getChildren() {
		return new ItemArbre[0];
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
		// Rien
	}
}