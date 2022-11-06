package dev.slasher.smartplugins.utils;

import org.bukkit.Location;

/**
 * Class with various utilities without predefined niche.
 */
public class Utils {

  public static float clampYaw(float yaw) {
    while (yaw < -180.0F) {
      yaw += 360.0F;
    }
    while (yaw >= 180.0F) {
      yaw -= 360.0F;
    }

    return yaw;
  }

  /**
   * Checks if a location has the chunk loaded in the world.
   *
   * @param location location to check.
   * @return TRUE if the location has the chunk loaded, FALSE if it doesn't.
   */
  public static boolean isLoaded(Location location) {
    if (location == null || location.getWorld() == null) {
      return false;
    }

    int chunkX = location.getBlockX() >> 4;
    int chunkZ = location.getBlockZ() >> 4;
    return location.getWorld().isChunkLoaded(chunkX, chunkZ);
  }
}
