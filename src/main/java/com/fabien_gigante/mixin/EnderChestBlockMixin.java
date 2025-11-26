package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.fabien_gigante.IEnderChestHolder;
import com.fabien_gigante.TeamEnderChestMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {
    @Shadow @Final private static Component CONTAINER_TITLE;

    @Redirect(method = "useWithoutItem", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/EnderChestBlock;CONTAINER_TITLE:Lnet/minecraft/network/chat/Component;"))
    private Component getContainerTitle(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!(player.getTeam() instanceof IEnderChestHolder)) return CONTAINER_TITLE;
        return Component.translatable("%s %s", CONTAINER_TITLE, player.getTeam().getFormattedDisplayName());
    }

    @Redirect(method = "method_55773", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ChestMenu;threeRows(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;)Lnet/minecraft/world/inventory/ChestMenu;"))
    private static ChestMenu createMenu(int syncId, Inventory inventory, Container container) {
        if (inventory.player.getTeam() instanceof IEnderChestHolder team)
            return new ChestMenu(TeamEnderChestMod.MENU_TYPE, syncId, inventory, new CompoundContainer(container, team.getEnderChestContainer()), 6);
        else return ChestMenu.threeRows(syncId, inventory, container);
    }
}
