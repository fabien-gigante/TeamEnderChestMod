package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.fabien_gigante.IEnderChestHolder;

import net.minecraft.block.BlockState;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(EnderChestBlock.class)
public class EnderChestBlockMixin {
    @Shadow @Final
    private static Text CONTAINER_NAME;

    @Redirect(method="onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getEnderChestInventory()Lnet/minecraft/inventory/EnderChestInventory;"))
    private EnderChestInventory getEnderChestInventory(PlayerEntity player) {
        return player.isSneaking() && player.getScoreboardTeam() instanceof IEnderChestHolder holder 
            ? holder.getEnderChestInventory() : player.getEnderChestInventory();
    }

    @Redirect(method = "onUse", at = @At(value = "FIELD", target = "Lnet/minecraft/block/EnderChestBlock;CONTAINER_NAME:Lnet/minecraft/text/Text;"))
    private Text getContainerName(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (player.isSneaking() && player.getScoreboardTeam() instanceof IEnderChestHolder)
            return Text.empty().append(player.getScoreboardTeam().getFormattedName()).append(" ").append(CONTAINER_NAME);
        else return CONTAINER_NAME;
    }
}
