package dev.slasher.smartplugins.nms.interfaces.entity;

import dev.slasher.smartplugins.libraries.holograms.api.HologramLine;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public interface IItem {

  public void setPassengerOf(Entity entity);

  public void setItemStack(ItemStack item);

  public void setLocation(double x, double y, double z);

  public boolean isDead();

  public void killEntity();

  public Item getEntity();

  public HologramLine getLine();
}
