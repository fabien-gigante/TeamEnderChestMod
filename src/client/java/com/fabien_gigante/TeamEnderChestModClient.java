package com.fabien_gigante;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ClientModInitializer;

public class TeamEnderChestModClient implements ClientModInitializer {
	public static final String MOD_ID = "team-ender-chest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	// Client-side mod entry point
	@Override
	public void onInitializeClient() {
		LOGGER.info("Team Ender Chest - Mod starting (client)...");
	}
}