package dev.slasher.smartplugins.bungee.party;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.slasher.smartplugins.party.Party;
import dev.slasher.smartplugins.party.PartyPlayer;
import dev.slasher.smartplugins.party.PartyRole;
import dev.slasher.smartplugins.player.role.Role;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("unchecked")
public class BungeeParty extends Party {

  public BungeeParty(String leader, int slots) {
    super(leader, slots);
    this.sendData();
  }

  @Override
  public void delete() {
    this.sendData("delete", "true");
    BungeePartyManager.listParties().remove(this);
    this.destroy();
  }

  @Override
  public void transfer(String name) {
    PartyPlayer newLeader = this.getPlayer(name);
    this.sendData("newLeader", newLeader.getName());
    this.leader.setRole(newLeader.getRole());
    newLeader.setRole(PartyRole.LEADER);
    this.leader = newLeader;
  }

  @Override
  public void join(String member) {
    super.join(member);
    this.sendData();
  }

  @Override
  public void leave(String member) {
    String leader = this.getLeader();
    if (this.members.size() == 1) {
      this.delete();
      return;
    }

    this.members.removeIf(pp -> pp.getName().equalsIgnoreCase(member));
    this.sendData("remove", member);
    if (leader.equals(member)) {
      this.sendData("newLeader", this.members.get(0).getName());
      this.leader = this.members.get(0);
      this.leader.setRole(PartyRole.LEADER);
      this.broadcast("§dParty> " + Role.getColored(this.leader.getName()) + " §eé o novo líder da party.");
    }
    this.broadcast("§dParty> " + Role.getColored(member) + " §csaiu da party.");
  }

  @Override
  public void kick(String member) {
    super.kick(member);
    this.sendData("remove", member);
  }

  public void sendData(ServerInfo serverInfo) {
    this.sendData(null, null, Collections.singleton(serverInfo));
  }

  private void sendData() {
    this.sendData(null, null);
  }

  private void sendData(String extraKey, String extraValue) {
    this.sendData(extraKey, extraValue, ProxyServer.getInstance().getServers().values());
  }

  private void sendData(String extraKey, String extraValue, Collection<ServerInfo> serverInfos) {
    JSONObject changes = new JSONObject();
    changes.put("leader", this.leader.getName());
    if (extraKey != null) {
      changes.put(extraKey, extraValue);
    }
    JSONArray members = new JSONArray();
    listMembers().forEach(member -> members.add(member.getName()));
    changes.put("members", members);

    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("PARTY");
    out.writeUTF(changes.toString());
    serverInfos.forEach(info -> info.sendData("kCore", out.toByteArray()));
  }
}
