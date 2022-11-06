package dev.slasher.smartplugins.libraries.npclib.npc.skin;

import dev.slasher.smartplugins.libraries.npclib.api.npc.NPC;
import org.bukkit.entity.Player;

public interface SkinnableEntity {

  public NPC getNPC();

  public Player getEntity();

  public SkinPacketTracker getSkinTracker();

  public void setSkin(Skin skin);

  public Skin getSkin();

  public void setSkinFlags(byte flags);
}
