package com.fabien_gigante;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamEnderChestMod implements ModInitializer {
	public static final String MOD_ID = "team-ender-chest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Team Ender Chest - Mod starting...");
	}
}