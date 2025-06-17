package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;

import com.fabien_gigante.IEnderChestHolder;

@Mixin(Scoreboard.class)
public abstract class ScoreboardMixin {
   // Inject at the end of the addTeam method
    @Inject(method = "addTeam(Lnet/minecraft/scoreboard/Team$Packed;)V", at = @At("TAIL"))
    private void onAddTeam(Team.Packed packedTeam, CallbackInfo ci, @Local Team team) {
        ((IEnderChestHolder)(Object)team).setEnderChestInventory(((IEnderChestHolder)(Object)packedTeam).getEnderChestInventory());
    }

}
