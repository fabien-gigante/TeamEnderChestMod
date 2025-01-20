package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;

import com.fabien_gigante.IEnderChestHolder;

@Mixin(targets= "net/minecraft/block/entity/EnderChestBlockEntity$1")
public class EnderChestBlockEntityMixin {
    @Shadow(aliases = { "this$0", "field_27218" }) @Final
    private EnderChestBlockEntity outerThis;

    @Inject(method="isPlayerViewing", at=@At("RETURN"), cancellable = true)
    private boolean isPlayerViewing(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        return cir.getReturnValue() || isPlayerViewingTeamEnderChest(player);
    }

    private boolean isPlayerViewingTeamEnderChest(PlayerEntity player) {
        return player.getScoreboardTeam() instanceof IEnderChestHolder holder && holder.getEnderChestInventory().isActiveBlockEntity(outerThis);
    }
}
