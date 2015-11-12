/*
 * Copyright (C) 2001 - 2015 Marko Salmela, http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuusio.api.model;

import java.util.ArrayList;
import java.util.List;

public class Models<T_Model extends Model> extends Model {

    protected final Model mContainerModel;
	protected final ArrayList<T_Model> mModels;
	
	protected Models() {
        this(null);
	}
	
	public Models(final Model containerModel) {
        mContainerModel = containerModel;
		mModels = new ArrayList<>();
	}

	public final Model getContainerModel() {
		return mContainerModel;
	}

	public T_Model addModel(final T_Model model) {
		if (!mModels.contains(model)) {
			mModels.add(model);
			return model;
		}
		return null;
	}
	
	public T_Model removeModel(final T_Model model) {
		if (mModels.contains(model)) {
			mModels.remove(model);
			return model;
		}
		return null;
	}
	
	public T_Model getModelAt(final int index) {
		return mModels.get(index);
	}
	
	public T_Model getModelWithName(final String name) {
		for (final T_Model model : mModels) {
			if (name.equalsIgnoreCase(model.getName())) {
				return model;
			}
		}
		return null;
	}
	
	public int getModelCount() {
		return mModels.size();
	}
	
	public List<T_Model> getModels() {
		return mModels;
	}
	
	public void setModels(final List<T_Model> models) {
		mModels.clear();
		mModels.addAll(models);
	}
	
	public boolean hasModelWithName(final String name) {
		for (final T_Model model : mModels) {
			if (name.equalsIgnoreCase(model.getName())) {
				return true;
			}
		}
		return false;
	}

}
