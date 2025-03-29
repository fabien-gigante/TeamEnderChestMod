package com.fabien_gigante;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.command.argument.TeamArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.scoreboard.ScoreboardState;
import net.minecraft.scoreboard.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamEnderChestMod implements ModInitializer {
	public static final String MOD_ID = "team-ender-chest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Team Ender Chest - Mod starting...");
		// TODO : better integration with DataCommand (using DataCommandObject, DataCommand$ObjectType for Team)
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("data")
				.then(CommandManager.literal("get")
					.then(CommandManager.literal("team")
						.then(CommandManager.argument("team", TeamArgumentType.team())
							.executes(this::onCommandDataGetTeam)))));
		});	
	}

	private int onCommandDataGetTeam(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		Team team = TeamArgumentType.getTeam(context, "team");
		ITeamState state = (ITeamState)new ScoreboardState(team.getScoreboard());
		NbtCompound nbt = state.teamToNbt(team.getName(), source.getRegistryManager());
		source.sendFeedback(() -> {
			return Text.translatable("commands.data.team.query", new Object[]{team.getFormattedName(), NbtHelper.toPrettyPrintedText(nbt)});
         }, false);
		return 1;
	}
}