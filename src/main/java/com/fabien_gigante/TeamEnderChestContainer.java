package com.fabien_gigante;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.scores.PlayerTeam;

public class TeamEnderChestContainer extends PlayerEnderChestContainer {
    protected @Nullable PlayerTeam team;

    public TeamEnderChestContainer() { this(null); }
    public TeamEnderChestContainer(PlayerTeam team) { this.team = team; }

    public @Nullable PlayerTeam getTeam() { return team; }
    public void setTeam(PlayerTeam team) { this.team = team; }

    @Override
    public void setChanged() { 
        super.setChanged();
        if (team != null) team.getScoreboard().onTeamChanged(team);
    }
}
