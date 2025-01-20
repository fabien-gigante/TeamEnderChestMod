package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.fabien_gigante.IEnderChestHolder;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.ScoreboardState;
import net.minecraft.scoreboard.Team;

@Mixin(ScoreboardState.class)
public class ScoreboardStateMixin {

    @Inject(method="readTeamsNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/ScoreboardState;readTeamPlayersNbt(Lnet/minecraft/scoreboard/Team;Lnet/minecraft/nbt/NbtList;)V"))
    private void readTeamEnderChestNbt(NbtList nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo ci, @Local NbtCompound nbtCompound, @Local Team team) {
        if (nbtCompound.contains("EnderItems", NbtElement.LIST_TYPE) && team instanceof IEnderChestHolder holder) {
            NbtList list = nbtCompound.getList("EnderItems", NbtElement.COMPOUND_TYPE);
            holder.getEnderChestInventory().readNbtList(list, registries);
        }
    }

    @Inject(method="teamsToNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;put(Ljava/lang/String;Lnet/minecraft/nbt/NbtElement;)Lnet/minecraft/nbt/NbtElement;"))
    private void teamEnderChestToNbt(RegistryWrapper.WrapperLookup registries, CallbackInfoReturnable<NbtList> ci, @Local NbtCompound nbtCompound, @Local Team team) {
        if (team instanceof IEnderChestHolder holder) {
            EnderChestInventory inventory = holder.getEnderChestInventory();
            if (!inventory.isEmpty()) nbtCompound.put("EnderItems", inventory.toNbtList(registries));
        }
    }

}
