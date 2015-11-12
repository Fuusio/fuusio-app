/*
 * Copyright (C) 2000-2014 Marko Salmela.
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

package org.fuusio.api.plugin;

import java.lang.reflect.Field;

/**
 * {@link PlugDescriptor} is a utility class used internally by {@link PluginBus} to describe
 * plugs created from {@link Plugin.Plug} annotations and {@link PluginInterface}s.
 */
public class PlugDescriptor {

	private final Field mField;
	private final String mName;
	private final Class<? extends PluginInterface> mPluginInterface;
    // OPTION private final boolean mSynced;

    private boolean mCreated;

	public PlugDescriptor(final Field field, final String name, final Class<? extends PluginInterface> pluginInterface) {
		super();
        mCreated = false;
		mField = field;
		mName = (name != null) ? name : Plugin.DEFAULT_PLUG_NAME;
		mPluginInterface = pluginInterface;
        // OPTION mSynced =  (mField != null) && (mField.getType().getAnnotation(Plugin.UISynchronized.class) != null);
	}

    @SuppressWarnings("unused")
    public final boolean isCreated() {
        return mCreated;
    }

    /* OPTION
    public final boolean isSynced() {
        return mSynced;
    }*/

    public final void setCreated(final boolean created) {
        mCreated = created;
    }

    public final Field getField() {
		return mField;
	}

	public final String getName() {
		return mName;
	}

	public final Class<? extends PluginInterface> getPluginInterface() {
		return mPluginInterface;
	}
}
