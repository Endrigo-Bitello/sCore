package dev.slasher.smartplugins.reflection;

import dev.slasher.smartplugins.libraries.MinecraftVersion;

import java.util.Arrays;

/**
 * Class used to get various classes and methods<br/>
 * from the Minecraft Server through Reflection in java.
 */
@SuppressWarnings("rawtypes")
public class MinecraftReflection {

  public static final MinecraftVersion VERSION = MinecraftVersion.getCurrentVersion();

  public static String NMU_PREFIX = "";
  public static String OBC_PREFIX;
  public static String NMS_PREFIX;

  private static Class<?> craftItemStack;

  private static Class<?> block, blocks;
  private static Class<?> entity, entityHuman;
  private static Class<?> enumDirection, enumProtocol, enumGamemode, enumPlayerInfoAction, enumTitleAction;
  private static Class<?> nbtTagCompound;
  private static Class<?> channel, playerInfoData, serverPing, serverData, serverPingPlayerSample;
  private static Class<?> serverConnection;
  private static Class<?> world, worldServer;
  private static Class<?> blockPosition, iBlockData;
  private static Class<?> vector3F;
  private static Class<?> iChatBaseComponent, chatSerializer, itemStack;
  private static Class<?> gameProfile, propertyMap, property;
  private static Class<?> dataWatcher, dataWatcherObject, dataWatcherSerializer, dataWatcherRegistry, watchableObject;

  static {
    try {
      getClass("net.minecraft.util.com.google.common.collect.ImmutableList");
      NMU_PREFIX = "net.minecraft.util.";
    } catch (Exception ex) {
      // nao possui mais a net.minecraft.util
    }

    OBC_PREFIX = "org.bukkit.craftbukkit." + VERSION.getVersion() + ".";
    NMS_PREFIX = OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");
  }

  // -- Classes em geral para usar.

  public static Class getServerPing() {
    if (serverPing == null) {
      serverPing = getMinecraftClass("ServerPing");
    }

    return serverPing;
  }

  public static Class getServerData() {
    if (serverData == null) {
      serverData = getMinecraftClass("ServerPing$ServerData");
    }

    return serverData;
  }

  public static Class getServerPingPlayerSample() {
    if (serverPingPlayerSample == null) {
      serverPingPlayerSample = getMinecraftClass("ServerPing$ServerPingPlayerSample");
    }

    return serverPingPlayerSample;
  }

  public static Class getEnumDirectionClass() {
    if (enumDirection == null) {
      enumDirection = getMinecraftClass("EnumDirection");
    }

    return enumDirection;
  }

  public static Class getEnumProtocolDirectionClass() {
    return getMinecraftClass(true, "EnumProtocolDirection");
  }

  public static Class getEnumProtocolClass() {
    if (enumProtocol == null) {
      enumProtocol = getMinecraftClass("EnumProtocol");
    }

    return enumProtocol;
  }

  public static Class getEnumGamemodeClass() {
    if (enumGamemode == null) {
      enumGamemode = getMinecraftClass("EnumGamemode", "WorldSettings$EnumGamemode");
    }

    return enumGamemode;
  }

  public static Class getEnumPlayerInfoActionClass() {
    if (enumPlayerInfoAction == null) {
      enumPlayerInfoAction = getMinecraftClass(int.class, "PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
    }

    return enumPlayerInfoAction;
  }

  public static Class getEnumTitleAction() {
    if (enumTitleAction == null) {
      enumTitleAction = getMinecraftClass(int.class, "PacketPlayOutTitle$EnumTitleAction");
    }

    return enumTitleAction;
  }

  public static Class<?> getNBTTagCompoundClass() {
    if (nbtTagCompound == null) {
      nbtTagCompound = getMinecraftClass("NBTTagCompound");
    }

    return nbtTagCompound;
  }

  public static Class<?> getDataWatcherClass() {
    if (dataWatcher == null) {
      dataWatcher = getMinecraftClass("DataWatcher");
    }

    return dataWatcher;
  }

  public static Class<?> getDataWatcherObjectClass() {
    if (dataWatcherObject == null) {
      dataWatcherObject = getMinecraftClass(int.class, "DataWatcherObject");
    }

    return dataWatcherObject;
  }

  public static Class<?> getDataWatcherSerializerClass() {
    if (dataWatcherSerializer == null) {
      dataWatcherSerializer = getMinecraftClass(null, "DataWatcherSerializer");
    }

    return dataWatcherSerializer;
  }

  public static Class<?> getDataWatcherRegistryClass() {
    if (dataWatcherRegistry == null) {
      dataWatcherRegistry = getMinecraftClass(null, "DataWatcherRegistry");
    }

    return dataWatcherRegistry;
  }

  public static Class<?> getWatchableObjectClass() {
    if (watchableObject == null) {
      watchableObject = getMinecraftClass("DataWatcher$Item", "DataWatcher$WatchableObject", "WatchableObject");
    }

    return watchableObject;
  }

  public static Class<?> getBlock() {
    if (block == null) {
      block = getMinecraftClass("Block");
    }

    return block;
  }

  public static Class getBlocks() {
    if (blocks == null) {
      blocks = getMinecraftClass("Blocks");
    }

    return blocks;
  }

  public static Class<?> getChannelClass() {
    if (channel == null) {
      channel = getMinecraftUtilClass("io.netty.channel.Channel");
    }

    return channel;
  }

  public static Class<?> getEntityClass() {
    if (entity == null) {
      entity = getMinecraftClass("Entity");
    }

    return entity;
  }

  public static Class<?> getEntityHumanClass() {
    if (entityHuman == null) {
      entityHuman = getMinecraftClass("EntityHuman");
    }

    return entityHuman;
  }

  public static Class<?> getPlayerInfoDataClass() {
    if (playerInfoData == null) {
      playerInfoData = getMinecraftClass("PacketPlayOutPlayerInfo$PlayerInfoData");
    }

    return playerInfoData;
  }

  public static Class<?> getServerConnectionClass() {
    if (serverConnection == null) {
      serverConnection = getMinecraftClass("ServerConnection");
    }

    return serverConnection;
  }

  public static Class<?> getWorldClass() {
    if (world == null) {
      world = getMinecraftClass("World");
    }

    return world;
  }

  public static Class<?> getWorldServerClass() {
    if (worldServer == null) {
      worldServer = getMinecraftClass("WorldServer");
    }

    return worldServer;
  }

  public static Class getIChatBaseComponent() {
    if (iChatBaseComponent == null) {
      iChatBaseComponent = getMinecraftClass("IChatBaseComponent");
    }

    return iChatBaseComponent;
  }

  public static Class<?> getChatSerializer() {
    if (chatSerializer == null) {
      chatSerializer = getMinecraftClass("IChatBaseComponent$ChatSerializer", "ChatSerializer");
    }

    return chatSerializer;
  }

  public static Class<?> getCraftItemStackClass() {
    if (craftItemStack == null) {
      craftItemStack = getCraftBukkitClass("inventory.CraftItemStack");
    }

    return craftItemStack;
  }

  public static Class<?> getItemStackClass() {
    if (itemStack == null) {
      itemStack = getMinecraftClass("ItemStack");
    }

    return itemStack;
  }

  public static Class<?> getBlockPositionClass() {
    if (blockPosition == null) {
      blockPosition = getMinecraftClass("BlockPosition");
    }

    return blockPosition;
  }

  public static Class<?> getIBlockDataClass() {
    if (iBlockData == null) {
      iBlockData = getMinecraftClass("IBlockData");
    }

    return iBlockData;
  }

  public static Class<?> getVector3FClass() {
    if (vector3F == null) {
      vector3F = getMinecraftClass("Vector3f");
    }

    return vector3F;
  }

  public static Class<?> getGameProfileClass() {
    if (gameProfile == null) {
      gameProfile = getMinecraftUtilClass("com.mojang.authlib.GameProfile");
    }

    return gameProfile;
  }

  public static Class<?> getPropertyMapClass() {
    if (propertyMap == null) {
      propertyMap = getMinecraftUtilClass("com.mojang.authlib.properties.PropertyMap");
    }

    return propertyMap;
  }

  public static Class<?> getPropertyClass() {
    if (property == null) {
      property = getMinecraftUtilClass("com.mojang.authlib.properties.Property");
    }

    return property;
  }

  // -- Utilities

  /**
   * Checks if the object is of the chosen class.
   *
   * @param clazz - The class to check.
   * @param object - The object that is of the class.
   * @return TRUE if the object is class-related, FALSE otherwise.
   */
  public static boolean is(Class<?> clazz, Object object) {
    if (clazz == null || object == null) {
      return false;
    }

    return clazz.isAssignableFrom(object.getClass());
  }

  // -- Metodos para buscar classes.

  /**
   * Method used to search for a class and load it.
   *
   * @param name The name of the class.
   * @return The class with the desired name.
   * @throws IllegalArgumentException If not find class.
   */
  public static Class<?> getClass(String name) {
    try {
      return MinecraftReflection.class.getClassLoader().loadClass(name);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Cannot find class " + name);
    }
  }

  /**
   * A shortener for {@link MinecraftReflection#getCraftBukkitClass(Object, String...)}<br>
   * with Object <code>false</code> to always throw exception.
   */
  public static Class<?> getCraftBukkitClass(String... names) {
    return getCraftBukkitClass(false, names);
  }

  /**
   * Method used to look up a class from package <code>org.bukkit.craftbukkit</code> from
   * of the desired names.
   *
   * @param canNull FALSE if you want the Exception to occur, or a Class to return if you don't
   *                find.
   * @param names The possible names of the class.
   * @return The class found.
   * @throws IllegalArgumentException If no class is found.
   */
  public static Class<?> getCraftBukkitClass(Object canNull, String... names) {
    for (String name : names) {
      try {
        return getClass(OBC_PREFIX + name);
      } catch (Exception ignored) {}
    }

    if (canNull instanceof Boolean && !((Boolean) canNull)) {
      throw new IllegalArgumentException("Cannot find CraftBukkit Class from names " + Arrays.asList(names) + ".");
    }

    return canNull != null && canNull.getClass().equals(Class.class) ? (Class) canNull : null;
  }

  /**
   * A shortener for {@link MinecraftReflection#getMinecraftClass(Object, String...)}<br>
   * with Object <code>false</code> to always throw exception.
   */
  public static Class<?> getMinecraftClass(String... names) {
    return getMinecraftClass(false, names);
  }

  /**
   * Method used to search for a class from the <code>net.minecraft.server</code> package from the
   * desired names.
   *
   * @param canNull FALSE if you want the Exception to occur, or a Class to return if you don't
   * find.
   * @param names Possible class names.
   * @return The class found.
   * @throws IllegalArgumentException If no class is found.
   */
  public static Class<?> getMinecraftClass(Object canNull, String... names) {
    for (String name : names) {
      try {
        return getClass(NMS_PREFIX + name);
      } catch (Exception ignored) {}
    }

    if (canNull instanceof Boolean && !((Boolean) canNull)) {
      throw new IllegalArgumentException("Cannot find MinecraftServer Class from names " + Arrays.asList(names) + ".");
    }

    return canNull != null && canNull.getClass().equals(Class.class) ? (Class) canNull : null;
  }

  /**
   * Method used to search for a minecraft utility class<br>
   * If the net.minecraft.util package still exists, it will use it as a prefix. Otherwise it will
   * search by name without any modification.
   *
   * @param name The name of the class.
   * @return The class found.
   * @throws IllegalArgumentException If no class is found.
   */
  public static Class<?> getMinecraftUtilClass(String name) {
    try {
      return getClass(NMU_PREFIX + name);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Cannot find MinecraftUtil Class from name " + name + ".");
    }
  }
}
