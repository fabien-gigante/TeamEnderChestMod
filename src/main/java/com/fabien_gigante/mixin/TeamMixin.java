package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.fabien_gigante.IEnderChestHolder;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

@Mixin(PlayerTeam.class)
public class TeamMixin implements IEnderChestHolder {
	@Shadow @Final private Scoreboard scoreboard;
	PlayerEnderChestContainer enderChestContainer = new PlayerEnderChestContainer();

	@Inject(method="<init>", at=@At(value="TAIL"))
	private void init(Scoreboard scoreboard, String name, CallbackInfo ci) {
		enderChestContainer.addListener((Container sender) -> { scoreboard.onTeamChanged((PlayerTeam)(Object)this); });
	}

	@Override
	public PlayerEnderChestContainer getEnderChestContainer() { return this.enderChestContainer; }
	@Override
	public void setEnderChestContainer(PlayerEnderChestContainer enderChestContainer) { this.enderChestContainer = enderChestContainer; }

    @Inject(method = "pack", at = @At("RETURN"), cancellable = true)
    private void onPack(CallbackInfoReturnable<PlayerTeam.Packed> cir) {
        ((IEnderChestHolder)(Object)(cir.getReturnValue())).setEnderChestContainer(enderChestContainer);
	}
}
