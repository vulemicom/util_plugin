package io.flutter.plugins;

import io.flutter.plugin.common.PluginRegistry;
import colleagues.product.skglobal.utilplugin.UtilPlugin;

/**
 * Generated file. Do not edit.
 */
public final class GeneratedPluginRegistrant {
  public static void registerWith(PluginRegistry registry) {
    if (alreadyRegisteredWith(registry)) {
      return;
    }
    UtilPlugin.registerWith(registry.registrarFor("colleagues.product.skglobal.utilplugin.UtilPlugin"));
  }

  private static boolean alreadyRegisteredWith(PluginRegistry registry) {
    final String key = GeneratedPluginRegistrant.class.getCanonicalName();
    if (registry.hasPlugin(key)) {
      return true;
    }
    registry.registrarFor(key);
    return false;
  }
}
