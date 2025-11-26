package com.fabien_gigante;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamEnderChestMod implements ModInitializer {
	public static final String MOD_ID = "team-ender-chest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final MenuType<ChestMenu> MENU_TYPE 
		= new MenuType<>(ChestMenu::sixRows, FeatureFlags.VANILLA_SET);

	@Override
	public void onInitialize() {
		LOGGER.info("Team Ender Chest - Mod starting...");
		Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(MOD_ID, "ender_chest"), MENU_TYPE);
	}
}