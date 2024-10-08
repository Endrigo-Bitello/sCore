package dev.slasher.smartplugins.party;

import dev.slasher.smartplugins.Manager;
import dev.slasher.smartplugins.player.role.Role;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static dev.slasher.smartplugins.party.PartyRole.LEADER;
import static dev.slasher.smartplugins.party.PartyRole.MEMBER;

public abstract class Party {
  
  /**
   * The time in minutes it takes to delete a Party if all players in it are offline.
   */
  private static final long MINUTES_UNTIL_DELETE = 5L;
  /**
   * The time in minutes it takes to delete a Party if all players in it are offline.
   */
  private static final long MINUTES_UNTIL_EXPIRE_INVITE = 1L;
  protected PartyPlayer leader;
  protected List<PartyPlayer> members;
  protected Map<String, Long> invitesMap;
  private int slots;
  private boolean isOpen;
  private long lastOnlineTime;
  
  public Party(String leader, int slots) {
    this.slots = slots;
    this.leader = new PartyPlayer(leader, LEADER);
    this.members = new ArrayList<>();
    this.invitesMap = new ConcurrentHashMap<>();
    this.members.add(this.leader);
  }
  
  public void setIsOpen(boolean flag) {
    this.isOpen = flag;
  }
  
  public void invite(Object target) {
    String leader = Role.getPrefixed(this.getLeader());
    this.invitesMap.put(Manager.getName(target).toLowerCase(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(MINUTES_UNTIL_EXPIRE_INVITE));
    BaseComponent component = new TextComponent("");
    for (BaseComponent components : TextComponent.fromLegacyText("§6----------------------------------\n" + leader + " §ehas invited you to join their party!\n§6")) {
      component.addExtra(components);
    }
    BaseComponent accept = new TextComponent("Click here");
    accept.setColor(ChatColor.GREEN);
    accept.setBold(true);
    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + this.getLeader()));
    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aClick to join " + leader + "'s party!")));
    component.addExtra(accept);
    for (BaseComponent components : TextComponent.fromLegacyText(" §eto join! You have 60 seconds to accept.\n§6----------------------------------")) {
      component.addExtra(components);
    }
   /* BaseComponent reject = new TextComponent("NEGAR");
    reject.setColor(ChatColor.RED);
    reject.setBold(true);
    reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party negar " + this.getLeader()));
    reject.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Clique para negar o convite de Party de " + leader + "§7.")));
    component.addExtra(reject);
    for (BaseComponent components : TextComponent.fromLegacyText(" §7o convite.\n ")) {
      component.addExtra(components);
    } */
    
    Manager.sendMessage(target, component);
  }
  
  public void reject(String member) {
    this.invitesMap.remove(member.toLowerCase());
    this.leader.sendMessage(" \n" + Role.getPrefixed(member) + " §edeclined the invitation to your party.!\n ");
  }
  
  public void join(String member) {
    this.broadcast(" \n" + Role.getPrefixed(member) + " §ajoined the party!!\n ");
    this.members.add(new PartyPlayer(member, MEMBER));
    this.invitesMap.remove(member.toLowerCase());
  }
  
  public void leave(String member) {
    String leader = this.getLeader();
    this.members.removeIf(pp -> pp.getName().equalsIgnoreCase(member));
    if (this.members.isEmpty()) {
      this.delete();
      return;
    }
    
    String prefixed = Role.getPrefixed(member);
    if (leader.equals(member)) {
      this.leader = this.members.get(0);
      this.leader.setRole(LEADER);
      this.broadcast(" \n" + prefixed + " §abecame the new Party Leader!\n ");
    }
    this.broadcast(" \n" + prefixed + " §aleft the party!\n ");
  }
  
  public void kick(String member) {
    this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(member)).findFirst().ifPresent(pp -> {
      pp.sendMessage(" \n" + Role.getPrefixed(this.getLeader()) + " §ekicked you out of the Party!\n ");
      this.members.removeIf(pap -> pap.equals(pp));
    });
  }
  
  public void transfer(String name) {
    PartyPlayer newLeader = this.getPlayer(name);
    if (newLeader == null) {
      return;
    }
    this.leader.setRole(newLeader.getRole());
    newLeader.setRole(LEADER);
    this.leader = newLeader;
  }
  
  public void broadcast(String message) {
    this.broadcast(message, false);
  }
  
  public void broadcast(String message, boolean ignoreLeader) {
    this.members.stream().filter(pp -> !ignoreLeader || !pp.equals(this.leader)).forEach(pp -> pp.sendMessage(message));
  }
  
  public void update() {
    if (onlineCount() == 0) {
      if (this.lastOnlineTime + (TimeUnit.MINUTES.toMillis(MINUTES_UNTIL_DELETE)) < System.currentTimeMillis()) {
        this.delete();
      }
      
      return;
    }
    
    this.lastOnlineTime = System.currentTimeMillis();
    this.invitesMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
  }
  
  public abstract void delete();
  
  public void destroy() {
    this.slots = 0;
    this.leader = null;
    this.members.clear();
    this.members = null;
    this.invitesMap.clear();
    this.invitesMap = null;
    this.lastOnlineTime = 0L;
  }
  
  public int getSlots() {
    return this.slots;
  }
  
  public long onlineCount() {
    return this.members.stream().filter(PartyPlayer::isOnline).count();
  }
  
  public String getLeader() {
    return this.leader.getName();
  }
  
  public String getName(String name) {
    return this.members.stream().map(PartyPlayer::getName).filter(ppName -> ppName.equalsIgnoreCase(name)).findAny().orElse(name);
  }
  
  public PartyPlayer getPlayer(String name) {
    return this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(name)).findAny().orElse(null);
  }
  
  public boolean isOpen() {
    return this.isOpen;
  }
  
  public boolean canJoin() {
    return this.members.size() < this.slots;
  }
  
  public boolean isInvited(String name) {
    return this.invitesMap.containsKey(name.toLowerCase());
  }
  
  public boolean isMember(String name) {
    return this.members.stream().anyMatch(pp -> pp.getName().equalsIgnoreCase(name));
  }
  
  public boolean isLeader(String name) {
    return this.leader.getName().equalsIgnoreCase(name);
  }
  
  public List<PartyPlayer> listMembers() {
    return this.members;
  }
}
