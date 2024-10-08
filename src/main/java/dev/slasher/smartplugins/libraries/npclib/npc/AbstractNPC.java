package dev.slasher.smartplugins.libraries.npclib.npc;

import com.google.common.base.Preconditions;
import dev.slasher.smartplugins.libraries.npclib.api.EntityController;
import dev.slasher.smartplugins.libraries.npclib.api.metadata.MetadataStore;
import dev.slasher.smartplugins.libraries.npclib.api.metadata.SimpleMetadataStore;
import dev.slasher.smartplugins.libraries.npclib.api.npc.NPC;
import dev.slasher.smartplugins.libraries.npclib.api.npc.NPCAnimation;
import dev.slasher.smartplugins.libraries.npclib.npc.skin.SkinnableEntity;
import dev.slasher.smartplugins.libraries.npclib.trait.NPCTrait;
import dev.slasher.smartplugins.nms.NMS;
import dev.slasher.smartplugins.utils.Utils;
import dev.slasher.smartplugins.libraries.npclib.api.event.*;
import dev.slasher.smartplugins.libraries.npclib.trait.CurrentLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import dev.slasher.smartplugins.libraries.npclib.NPCLibrary;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbstractNPC implements NPC {

  private UUID uuid;
  private String name;
  private EntityController controller;

  private MetadataStore data;
  private Map<Class<? extends NPCTrait>, NPCTrait> traits;

  public AbstractNPC(UUID uuid, String name, EntityController controller) {
    this.uuid = uuid;
    this.name = name;
    this.controller = controller;

    this.data = new SimpleMetadataStore();
    this.traits = new HashMap<>();
    addTrait(CurrentLocation.class);
  }

  @Override
  public boolean spawn(Location location) {
    Preconditions.checkNotNull(location, "Location cannot be null!");
    Preconditions.checkState(!isSpawned(), "The npc is already spawned!");
    controller.spawn(location, this);

    boolean couldSpawn = Utils.isLoaded(location) && NMS.addToWorld(location.getWorld(), controller.getBukkitEntity(), SpawnReason.CUSTOM);
    if (couldSpawn) {
      SkinnableEntity entity = NMS.getSkinnable(getEntity());
      if (entity != null) {
        entity.getSkinTracker().onSpawnNPC();
      }
    }

    getTrait(CurrentLocation.class).setLocation(location);
    if (!couldSpawn) {
      Bukkit.getPluginManager().callEvent(new NPCNeedsRespawnEvent(this));
      controller.remove();
      return false;
    }

    NPCSpawnEvent event = new NPCSpawnEvent(this);
    if (event.isCancelled()) {
      controller.remove();
      return false;
    }

    NMS.setHeadYaw(getEntity(), location.getYaw());
    getEntity().setMetadata("NPC", new FixedMetadataValue(NPCLibrary.getPlugin(), this));

    for (NPCTrait trait : traits.values()) {
      trait.onSpawn();
    }

    if (getEntity() instanceof LivingEntity) {
      LivingEntity entity = (LivingEntity) getEntity();
      entity.setRemoveWhenFarAway(false);

      if (NMS.getStepHeight(entity) < 1.0f) {
        NMS.setStepHeight(entity, 1.0f);
      }

      if (getEntity() instanceof Player) {
        NMS.replaceTrackerEntry((Player) getEntity());
      }
    }

    getTrait(CurrentLocation.class).setLocation(getEntity().getLocation());
    return true;
  }

  @Override
  public boolean despawn() {
    Preconditions.checkState(isSpawned(), "The npc is not spawned!");
    NPCDespawnEvent event = new NPCDespawnEvent(this);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if (event.isCancelled()) {
      return false;
    }

    for (NPCTrait trait : traits.values()) {
      trait.onDespawn();
    }

    this.controller.remove();
    Bukkit.getOnlinePlayers().forEach(player -> {
      Scoreboard sb = player.getScoreboard();
      Team team = sb.getTeam("mNPCS");
      if (team != null) {
        team.removeEntry(this.name);
        if (team.getSize() == 0) {
          team.unregister();
        }
      }
    });
    return true;
  }

  @Override
  public void destroy() {
    if (isSpawned()) {
      despawn();
    }

    Bukkit.getOnlinePlayers().forEach(player -> {
      Scoreboard sb = player.getScoreboard();
      Team team = sb.getTeam("mNPCS");
      if (team != null) {
        team.removeEntry(this.name);
        if (team.getSize() == 0) {
          team.unregister();
        }
      }
    });
    this.uuid = null;
    this.name = null;
    this.controller = null;
    this.traits.clear();
    this.traits = null;
    NPCLibrary.unregister(this);
  }

  @Override
  public MetadataStore data() {
    return data;
  }

  private int ticksToUpdate;

  @Override
  public void update() {
    if (isSpawned()) {
      if (ticksToUpdate++ > 30) {
        ticksToUpdate = 0;

        Entity entity = controller.getBukkitEntity();
        if (entity instanceof Player) {
          for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            if (!NPCLibrary.isNPC(players)) {
              Scoreboard sb = players.getScoreboard();
              Team team = sb.getTeam("mNPCS");
              if (data().get(HIDE_BY_TEAMS_KEY, false)) {
                if (team == null) {
                  team = sb.registerNewTeam("mNPCS");
                  team.setNameTagVisibility(NameTagVisibility.NEVER);
                }

                if (!team.hasEntry(this.name)) {
                  team.addEntry(this.name);
                }

                continue;
              }

              if (team != null && team.getSize() == 0) {
                team.unregister();
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void playAnimation(NPCAnimation animation) {
    Preconditions.checkState(isSpawned(), "The npc is not spawned!");
    NMS.playAnimation(this.getEntity(), animation);
  }

  @Override
  public void addTrait(NPCTrait trait) {
    traits.put(trait.getClass(), trait);
    trait.onAttach();
  }

  @Override
  public void addTrait(Class<? extends NPCTrait> traitClass) {
    try {
      NPCTrait trait = (NPCTrait) traitClass.getDeclaredConstructors()[0].newInstance(this);
      traits.put(traitClass, trait);
      trait.onAttach();
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException("Failed to add trait " + traitClass.getName(), e);
    }
  }

  @Override
  public void removeTrait(Class<? extends NPCTrait> traitClass) {
    NPCTrait trait = traits.get(traitClass);
    if (trait != null) {
      trait.onRemove();
      traits.remove(traitClass);
    }
  }

  private Entity following;
  private boolean navigating;
  private Location walkingTo;

  public void finishNavigation() {
    Bukkit.getPluginManager().callEvent(new NPCNavigationEndEvent(this));
    this.navigating = false;
  }

  @Override
  public void setFollowing(Entity entity) {
    Preconditions.checkState(!this.navigating, "The npc is already walking to a location!");
    if (this.following != null) {
      Bukkit.getPluginManager().callEvent(new NPCStopFollowingEvent(this, this.following));
    }
    this.following = entity;
  }

  @Override
  public void setWalkingTo(Location location) {
    Preconditions.checkState(this.following == null, "The npc is already following an entity!");
    if (location == null) {
      this.walkingTo = null;
      return;
    }
    Preconditions.checkState(!this.navigating, "The npc is already walking to a location!");

    this.navigating = true;
    this.walkingTo = location;
  }

  private boolean laying;

  @Override
  public void setLaying(boolean laying) {
    this.laying = true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends NPCTrait> T getTrait(Class<T> traitClass) {
    return (T) this.traits.get(traitClass);
  }

  @Override
  public boolean isSpawned() {
    return this.controller != null && this.controller.getBukkitEntity() != null && this.controller.getBukkitEntity().isValid();
  }

  @Override
  public boolean isProtected() {
    return data().get(PROTECTED_KEY, true);
  }

  @Override
  public boolean isNavigating() {
    return this.navigating;
  }

  @Override
  public boolean isLaying() {
    return this.laying;
  }

  @Override
  public Entity getEntity() {
    return this.controller.getBukkitEntity();
  }

  @Override
  public Entity getFollowing() {
    return this.following;
  }

  @Override
  public Location getWalkingTo() {
    return this.walkingTo;
  }

  @Override
  public Location getCurrentLocation() {
    return this.getTrait(CurrentLocation.class).getLocation().getWorld() != null ?
      this.getTrait(CurrentLocation.class).getLocation() :
      this.isSpawned() ? this.getEntity().getLocation() : null;
  }

  @Override
  public UUID getUUID() {
    return this.uuid;
  }

  @Override
  public String getName() {
    return this.name;
  }
}
