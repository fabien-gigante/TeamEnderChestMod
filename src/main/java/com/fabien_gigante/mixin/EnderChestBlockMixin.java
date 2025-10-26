package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.fabien_gigante.IEnderChestHolder;
import com.fabien_gigante.TeamEnderChestMod;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {
    @Shadow @Final
    private static Text CONTAINER_NAME;

    @Redirect(method = "onUse", at = @At(value = "FIELD", target = "Lnet/minecraft/block/EnderChestBlock;CONTAINER_NAME:Lnet/minecraft/text/Text;"))
    private Text getContainerName(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(player.getScoreboardTeam() instanceof IEnderChestHolder)) return CONTAINER_NAME;
        return Text.empty().append(CONTAINER_NAME).append(" ").append(player.getScoreboardTeam().getFormattedName());
    }

    @Redirect(method = "method_55773", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GenericContainerScreenHandler;createGeneric9x3(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)Lnet/minecraft/screen/GenericContainerScreenHandler;"))
    private static GenericContainerScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        if (playerInventory.player.getScoreboardTeam() instanceof IEnderChestHolder team)
            return new GenericContainerScreenHandler(TeamEnderChestMod.SCREEN_HANDLER_TYPE, syncId, playerInventory, new DoubleInventory(inventory, team.getEnderChestInventory()), 6);
        else return  GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, inventory);
    }
}
