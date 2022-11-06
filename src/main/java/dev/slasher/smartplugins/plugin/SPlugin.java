package dev.slasher.smartplugins.plugin;

import java.io.File;

import dev.slasher.smartplugins.plugin.config.FileUtils;
import dev.slasher.smartplugins.plugin.config.SConfig;
import dev.slasher.smartplugins.plugin.config.SWriter;
import dev.slasher.smartplugins.plugin.logger.SLogger;
import dev.slasher.smartplugins.reflection.Accessors;
import dev.slasher.smartplugins.reflection.acessors.FieldAccessor;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SPlugin extends JavaPlugin {

  private static final FieldAccessor<PluginLogger> LOGGER_ACCESSOR = Accessors.getField(JavaPlugin.class, "logger", PluginLogger.class);
  private final FileUtils fileUtils;

  public SPlugin() {
    this.fileUtils = new FileUtils(this);
    LOGGER_ACCESSOR.set(this, new SLogger(this));
    
    this.start();
  }

  public abstract void start();

  public abstract void load();

  public abstract void enable();

  public abstract void disable();

  @Override
  public void onLoad() {
    this.load();
  }

  @Override
  public void onEnable() {
    this.enable();
  }

  @Override
  public void onDisable() {
    this.disable();
  }

  public SConfig getConfig(String name) {
    return this.getConfig("", name);
  }

  public SConfig getConfig(String path, String name) {
    return SConfig.getConfig(this, "plugins/" + this.getName() + "/" + path, name);
  }
  
  public SWriter getWriter(File file) {
    return this.getWriter(file, "");
  }
  
  public SWriter getWriter(File file, String header) {
    return new SWriter((SLogger) this.getLogger(), file, header);
  }

  public FileUtils getFileUtils() {
    return this.fileUtils;
  }
}
