package com.fabien_gigante;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamEnderChestMod implements ModInitializer {
	public static final String MOD_ID = "team-ender-chest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final List<Item> TEAM_KEYS = List.of( Items.TRIAL_KEY, Items.OMINOUS_TRIAL_KEY, Items.ENDER_PEARL, Items.ENDER_EYE, Items.SPYGLASS );

	@Override
	public void onInitialize() {
		LOGGER.info("Team Ender Chest - Mod starting...");
	}
}