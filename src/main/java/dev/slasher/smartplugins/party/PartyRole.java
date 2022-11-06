package dev.slasher.smartplugins.party;

public enum PartyRole {
  MEMBER("Member"),
  LEADER("Leader");
  
  private final String name;
  
  PartyRole(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}
