package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import com.fabien_gigante.IEnderChestHolder;

@Mixin(Scoreboard.class)
public abstract class ScoreboardMixin {
   // Inject at the end of the loadPlayerTeam method
    @Inject(method = "loadPlayerTeam(Lnet/minecraft/world/scores/PlayerTeam$Packed;)V", at = @At("TAIL"))
    private void onLoadPlayerTeam(PlayerTeam.Packed packedTeam, CallbackInfo ci, @Local PlayerTeam team) {
        ((IEnderChestHolder)(Object)team).setEnderChestContainer(((IEnderChestHolder)(Object)packedTeam).getEnderChestContainer());
    }

}
