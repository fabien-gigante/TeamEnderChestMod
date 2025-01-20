package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.scoreboard.Team;
import net.minecraft.inventory.EnderChestInventory;

import com.fabien_gigante.IEnderChestHolder;

@Mixin(Team.class)
public class TeamMixin implements IEnderChestHolder {
	EnderChestInventory enderChestInventory = new EnderChestInventory();
	@Override
	public EnderChestInventory getEnderChestInventory() { return this.enderChestInventory; }
}