package dev.slasher.smartplugins.libraries.npclib;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import dev.slasher.smartplugins.libraries.npclib.api.EntityController;
import dev.slasher.smartplugins.libraries.npclib.api.npc.NPC;
import dev.slasher.smartplugins.libraries.npclib.npc.AbstractNPC;
import dev.slasher.smartplugins.libraries.npclib.npc.EntityControllers;
import dev.slasher.smartplugins.libraries.npclib.npc.ai.NPCHolder;
import dev.slasher.smartplugins.plugin.SPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class NPCLibrary {

  private static Plugin plugin;
  private static Listener LISTENER;
  private static final List<NPC> NPCS = new ArrayList<>();

  public static void setupNPCs(SPlugin pl) {
    if (pl == null || plugin != null) {
      return;
    }

    plugin = pl;
    LISTENER = new NPCListeners();
    Bukkit.getServer().getPluginManager().registerEvents(LISTENER, pl);
  }

  public static NPC createNPC(EntityType type, String name) {
    return createNPC(type, UUID.randomUUID(), name);
  }

  public static NPC createNPC(EntityType type, UUID uuid, String name) {
    Preconditions.checkNotNull(type, "Type cannot be null");
    Preconditions.checkNotNull(name, "Name cannot be null");

    EntityController controller = EntityControllers.getController(type);
    NPC npc = new AbstractNPC(uuid, name, controller);
    NPCS.add(npc);
    return npc;
  }

  public static void unregister(NPC npc) {
    NPCS.remove(npc);
  }

  public static void unregisterAll() {
    for (NPC npc : listNPCS()) {
      npc.destroy();
    }

    HandlerList.unregisterAll(LISTENER);
    NPCS.clear();
    plugin = null;
  }

  public static boolean isNPC(Entity entity) {
    return getNPC(entity) != null;
  }

  public static NPC getNPC(Entity entity) {
    return entity instanceof NPCHolder ? ((NPCHolder) entity).getNPC() : null;
  }

  public static NPC findNPC(UUID uuid) {
    return listNPCS().stream().filter(npc -> npc.getUUID().equals(uuid)).findFirst().orElse(null);
  }

  public static Plugin getPlugin() {
    return plugin;
  }

  public static Collection<NPC> listNPCS() {
    return ImmutableList.copyOf(NPCS);
  }
}
