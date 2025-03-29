package com.fabien_gigante;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class TeamEnderChestUtil {
	private static final Text CONTAINER_NAME = Text.translatable("container.enderchest");
    public static Text getTeamEnderChestTitle(PlayerEntity player) {
        if (player.getScoreboardTeam() == null) return null;
        return Text.empty().append(CONTAINER_NAME).append(" ").append(player.getScoreboardTeam().getFormattedName());
    }
}
