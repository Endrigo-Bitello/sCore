package dev.slasher.smartplugins.plugin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import dev.slasher.smartplugins.plugin.SPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;

public class SConfig {

  private SPlugin plugin;
  private File file;
  private YamlConfiguration config;

  private SConfig(SPlugin plugin, String path, String name) {
    this.plugin = plugin;
    this.file = new File(path + "/" + name + ".yml");
    if (!file.exists()) {
      this.file.getParentFile().mkdirs();
      InputStream in = plugin.getResource(name + ".yml");
      if (in != null) {
        plugin.getFileUtils().copyFile(in, file);
      } else {
        try {
          this.file.createNewFile();
        } catch (IOException ex) {
          plugin.getLogger().log(Level.SEVERE, "An unexpected error occurred while creating the file \"" + file.getName() + "\": ", ex);
        }
      }
    }

    try {
      this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    } catch (IOException ex) {
      plugin.getLogger().log(Level.SEVERE, "An unexpected error occurred while creating the config  \"" + file.getName() + "\": ", ex);
    }
  }

  public boolean createSection(String path) {
    this.config.createSection(path);
    return save();
  }

  public boolean set(String path, Object obj) {
    this.config.set(path, obj);
    return save();
  }

  public boolean contains(String path) {
    return this.config.contains(path);
  }

  public Object get(String path) {
    return this.config.get(path);
  }

  public int getInt(String path) {
    return this.config.getInt(path);
  }

  public int getInt(String path, int def) {
    return this.config.getInt(path, def);
  }

  public double getDouble(String path) {
    return this.config.getDouble(path);
  }

  public double getDouble(String path, double def) {
    return this.config.getDouble(path, def);
  }

  public String getString(String path) {
    return this.config.getString(path);
  }

  public boolean getBoolean(String path) {
    return this.config.getBoolean(path);
  }
  
  public boolean getBoolean(String path, boolean def) {
    return this.config.getBoolean(path, def);
  }

  public List<String> getStringList(String path) {
    return this.config.getStringList(path);
  }
  
  public List<Integer> getIntegerList(String path) {
    return this.config.getIntegerList(path);
  }

  public Set<String> getKeys(boolean flag) {
    return this.config.getKeys(flag);
  }

  public ConfigurationSection getSection(String path) {
    return this.config.getConfigurationSection(path);
  }

  public void reload() {
    try {
      this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    } catch (IOException ex) {
      plugin.getLogger().log(Level.SEVERE, "An unexpected error occurred while reloading the config \"" + file.getName() + "\": ", ex);
    }
  }

  public boolean save() {
    try {
      this.config.save(this.file);
      return true;
    } catch (IOException ex) {
      plugin.getLogger().log(Level.SEVERE, "An unexpected error occurred while saving the config \"" + file.getName() + "\": ", ex);
      return false;
    }
  }

  public File getFile() {
    return this.file;
  }

  public YamlConfiguration getRawConfig() {
    return this.config;
  }

  private static Map<String, SConfig> cache = new HashMap<>();

  public static SConfig getConfig(SPlugin plugin, String path, String name) {
    if (!cache.containsKey(path + "/" + name)) {
      cache.put(path + "/" + name, new SConfig(plugin, path, name));
    }

    return cache.get(path + "/" + name);
  }
}
