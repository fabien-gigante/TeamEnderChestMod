package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.CopperChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState.ChestMaterialType;

// Fix MC-305292, MC-305274 : Disable Christmas textures for copper chests 
@Mixin(ChestRenderer.class)
public abstract class ChestRendererMixin {
    @Inject(method = "getChestMaterial",at = @At("HEAD"), cancellable = true)
    private void disableChristmasForCopper(BlockEntity blockEntity, boolean xmas, CallbackInfoReturnable<ChestMaterialType> cir) {
        if (xmas && blockEntity.getBlockState().getBlock() instanceof CopperChestBlock copper) {
            cir.setReturnValue(
                switch (copper.getState()) {
                    case UNAFFECTED -> ChestMaterialType.COPPER_UNAFFECTED;
                    case EXPOSED -> ChestMaterialType.COPPER_EXPOSED;
                    case WEATHERED -> ChestMaterialType.COPPER_WEATHERED;
                    case OXIDIZED -> ChestMaterialType.COPPER_OXIDIZED;
                }
            );
        }
    }
}