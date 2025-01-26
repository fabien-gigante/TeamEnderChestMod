package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.fabien_gigante.IEnderChestHolder;
import com.fabien_gigante.TeamEnderChestMod;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin extends AbstractBlock {
    @Shadow @Final
    private static Text CONTAINER_NAME;

    private boolean withTeamKey = false;

    public EnderChestBlockMixin(Settings settings) { super(settings); }

    @Redirect(method="onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getEnderChestInventory()Lnet/minecraft/inventory/EnderChestInventory;"))
    private EnderChestInventory getEnderChestInventory(PlayerEntity player) {
        return useTeamInventory(player) && player.getScoreboardTeam() instanceof IEnderChestHolder holder 
            ? holder.getEnderChestInventory() : player.getEnderChestInventory();
    }

    @Redirect(method = "onUse", at = @At(value = "FIELD", target = "Lnet/minecraft/block/EnderChestBlock;CONTAINER_NAME:Lnet/minecraft/text/Text;"))
    private Text getContainerName(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (useTeamInventory(player) && player.getScoreboardTeam() instanceof IEnderChestHolder)
            return Text.empty().append(player.getScoreboardTeam().getFormattedName()).append(" ").append(CONTAINER_NAME);
        else return CONTAINER_NAME;
    }

    private boolean useTeamInventory(PlayerEntity player) { return withTeamKey || player.isSneaking(); }
    private boolean isTeamKey(ItemStack stack) { return TeamEnderChestMod.TEAM_KEYS.stream().anyMatch(stack::isOf); }

    @Override
	protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!isTeamKey(stack)) return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        withTeamKey = true;
        ActionResult result = this.onUse(state, world, pos, player, hit);
        withTeamKey = false;
        return result;
	}
}