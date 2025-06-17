package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventory;

import com.fabien_gigante.IEnderChestHolder;

@Mixin(Team.class)
public class TeamMixin implements IEnderChestHolder {
	@Shadow @Final private Scoreboard scoreboard;
	EnderChestInventory enderChestInventory = new EnderChestInventory();

	@Inject(method="<init>", at=@At(value="TAIL"))
	private void init(Scoreboard scoreboard, String name, CallbackInfo ci) {
		enderChestInventory.addListener((Inventory sender) -> { scoreboard.updateScoreboardTeam((Team)(Object)this); });
	}

	@Override
	public EnderChestInventory getEnderChestInventory() { return this.enderChestInventory; }
	@Override
	public void setEnderChestInventory(EnderChestInventory enderChestInventory) { this.enderChestInventory = enderChestInventory; }

    @Inject(method = "pack", at = @At("RETURN"), cancellable = true)
    private void onPack(CallbackInfoReturnable<Team.Packed> cir) {
        ((IEnderChestHolder)(Object)(cir.getReturnValue())).setEnderChestInventory(enderChestInventory);
	}
}
