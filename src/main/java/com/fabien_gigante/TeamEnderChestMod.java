package com.fabien_gigante;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.ScoreboardSaveData;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.Scoreboard.PackedScore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class TeamEnderChestMod implements ModInitializer {
	public static final String MOD_ID = "team-ender-chest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MenuType<ChestMenu> MENU_TYPE 
		= new MenuType<>(ChestMenu::sixRows, FeatureFlags.VANILLA_SET);

	@Override
	public void onInitialize() {
		LOGGER.info("Team Ender Chest - Mod starting...");
		Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(MOD_ID, "ender_chest"), MENU_TYPE);
		
		// TODO : better integration with DataCommand (using DataCommandObject, DataCommand$ObjectType for Team)
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(Commands.literal("data").then(Commands.literal("get")
				.then(Commands.literal("team").then(Commands.argument("team", TeamArgument.team())
				.executes(this::onCommandDataGetTeam)))));
		});
	}

	private int onCommandDataGetTeam(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSourceStack source = context.getSource();
		PlayerTeam team = TeamArgument.getTeam(context, "team");
		PlayerTeam.Packed packed = team.pack();
		Tag nbt = PlayerTeam.Packed.CODEC.encode(packed, net.minecraft.nbt.NbtOps.INSTANCE, new CompoundTag()).result().orElseThrow(null);
		source.sendSuccess( () -> Component.translatable( "commands.data.team.query", team.getFormattedDisplayName(), NbtUtils.toPrettyComponent(nbt)), false);
		return 1;
	}

}
	