package dev.slasher.smartplugins.game;

public enum GameState {

  NONE, WAITING, STARTING, INGAME, ENDING;
  
  public boolean canJoin() {
    return this == WAITING;
  }
}
