package dev.slasher.smartplugins.player.hotbar;

import dev.slasher.smartplugins.player.Profile;

public class HotbarAction {

  private String value;
  private HotbarActionType actionType;

  public HotbarAction(String action) {
    String[] splitter = action.split(">");
    this.value = splitter.length > 1 ? splitter[1] : "";
    this.actionType = HotbarActionType.fromName(splitter[0]);
  }

  public void execute(Profile profile) {
    if (this.actionType != null) {
      if (this.value.isEmpty()) {
        return;
      }
      
      this.actionType.execute(profile, this.value);
    }
  }
  
  public String getValue() {
    return this.value;
  }
  
  public HotbarActionType getActionType() {
    return this.actionType;
  }
}
