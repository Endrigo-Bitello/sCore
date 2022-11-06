package dev.slasher.smartplugins.libraries.npclib.api.trait;

public interface Trait {
  
  /**
   * called to Trace be added.
   */
  public void onAttach();
  
  /**
   * called to Trace be removed.
   */
  public void onRemove();
  
  /**
   * called to the NPC to be spawned.
   */
  public void onSpawn();
  
  /**
   * called to the NPC to be despawned.
   */
  public void onDespawn();
}
