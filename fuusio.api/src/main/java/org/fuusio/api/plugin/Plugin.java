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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Plugin} defined an interface for plugin components.
 */
public interface Plugin {

    String DEFAULT_PLUG_NAME = "*";
    boolean DEFAULT_VALUE_CREATE = false;

	String getPlugName();

    void onPlugged(PluginBus bus);

    void onUnplugged(PluginBus bus);

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Plug {
        String name() default DEFAULT_PLUG_NAME;
        boolean create() default DEFAULT_VALUE_CREATE;
    }
}
