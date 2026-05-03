package com.fabien_gigante;

import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.scores.PlayerTeam;

public class TeamEnderChestContainer extends PlayerEnderChestContainer {
    protected PlayerTeam team;

    public TeamEnderChestContainer(PlayerTeam team) { this.team = team; }

    @Override
    public void setChanged() { 
        super.setChanged();
        team.getScoreboard().onTeamChanged(team);
    }
}
