package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.fabien_gigante.IEnderChestHolder;
import com.fabien_gigante.TeamEnderChestContainer;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

@Mixin(PlayerTeam.class)
public class TeamMixin implements IEnderChestHolder {
	@Shadow @Final private Scoreboard scoreboard;
	@Final TeamEnderChestContainer enderChestContainer;

	@Inject(method="<init>", at=@At(value="TAIL"))
	private void init(Scoreboard scoreboard, String name, CallbackInfo ci) {
		enderChestContainer = new TeamEnderChestContainer((PlayerTeam)(Object)this);
	}

	@Override
	public TeamEnderChestContainer getEnderChestContainer() { return this.enderChestContainer; }
	@Override
	public void setEnderChestContainer(TeamEnderChestContainer enderChestContainer) {
		this.enderChestContainer = enderChestContainer;
		this.enderChestContainer.setTeam((PlayerTeam)(Object)this);
	}

    @Inject(method = "pack", at = @At("RETURN"), cancellable = true)
    private void onPack(CallbackInfoReturnable<PlayerTeam.Packed> cir) {
		if ((Object)cir.getReturnValue() instanceof IEnderChestHolder holder)
			holder.setEnderChestContainer(enderChestContainer);
	}
}