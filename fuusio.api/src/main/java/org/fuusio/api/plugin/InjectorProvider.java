package org.fuusio.api.plugin;

/**
 * {@link InjectorProvider} defines interface for objects that provide a {@link PluginInjector}.
 */
public interface InjectorProvider {

    PluginInjector getInjector();
}
