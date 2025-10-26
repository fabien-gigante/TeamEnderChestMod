package com.fabien_gigante;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamEnderChestMod implements ModInitializer {
	public static final String MOD_ID = "team-ender-chest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ScreenHandlerType<GenericContainerScreenHandler> SCREEN_HANDLER_TYPE 
		= new ScreenHandlerType<>(GenericContainerScreenHandler::createGeneric9x6, FeatureFlags.VANILLA_FEATURES);

	@Override
	public void onInitialize() {
		LOGGER.info("Team Ender Chest - Mod starting...");
		Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "ender_chest"), SCREEN_HANDLER_TYPE);
	}
}