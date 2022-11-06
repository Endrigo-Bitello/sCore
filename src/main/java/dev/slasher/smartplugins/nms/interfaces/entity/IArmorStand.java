package dev.slasher.smartplugins.nms.interfaces.entity;

import dev.slasher.smartplugins.libraries.holograms.api.HologramLine;
import org.bukkit.entity.ArmorStand;

public interface IArmorStand {
  
  int getId();

  void setName(String name);

  void setLocation(double x, double y, double z);

  boolean isDead();

  void killEntity();

  ArmorStand getEntity();

  HologramLine getLine();
}
