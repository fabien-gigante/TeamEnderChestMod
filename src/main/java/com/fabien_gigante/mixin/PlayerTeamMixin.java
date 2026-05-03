package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.fabien_gigante.IEnderChestItemsHolder;
import com.fabien_gigante.TeamEnderChestContainer;

import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

@Mixin(PlayerTeam.class)
public class PlayerTeamMixin implements IEnderChestItemsHolder {
	@Shadow @Final private Scoreboard scoreboard;
	@Unique @Final private TeamEnderChestContainer enderChestContainer = new TeamEnderChestContainer((PlayerTeam)(Object)this);

	@Override
	public PlayerEnderChestContainer getEnderChestContainer() { return enderChestContainer; }

	@Inject(method = "pack", at = @At("RETURN"), cancellable = true)
    private void onPack(CallbackInfoReturnable<PlayerTeam.Packed> cir) {
		if ((Object)cir.getReturnValue() instanceof IEnderChestItemsHolder holder) holder.setEnderChestItems(getEnderChestItems());
	}
}