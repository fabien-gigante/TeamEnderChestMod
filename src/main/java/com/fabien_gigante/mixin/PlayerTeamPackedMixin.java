package com.fabien_gigante.mixin;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team.CollisionRule;
import net.minecraft.world.scores.Team.Visibility;

import com.fabien_gigante.IEnderChestHolder;

@Mixin(PlayerTeam.Packed.class)
public class PlayerTeamPackedMixin implements IEnderChestHolder {
	@Unique @Final private PlayerEnderChestContainer enderChestContainer = new PlayerEnderChestContainer();

	@Override
	public PlayerEnderChestContainer getEnderChestContainer() {	return enderChestContainer; }

	private static PlayerTeam.Packed create(String name, Optional<Component> displayName, Optional<ChatFormatting> color, boolean allowFriendlyFire, boolean seeFriendlyInvisibles, Component memberNamePrefix, Component memberNameSuffix, Visibility nameTagVisibility, Visibility deathMessageVisibility, CollisionRule collisionRule, List<String> players, List<ItemStackWithSlot> enderItems) {
        PlayerTeam.Packed packed = new PlayerTeam.Packed(name, displayName, color, allowFriendlyFire, seeFriendlyInvisibles, memberNamePrefix, memberNameSuffix, nameTagVisibility, deathMessageVisibility, collisionRule, players);
		if ((Object)packed instanceof IEnderChestHolder holder) holder.setEnderChestContent(upgradeEnderItems(enderItems));
        return packed;
	}

	private static List<ItemStackWithSlot> upgradeEnderItems(List<ItemStackWithSlot> enderItems) {
		// If there are duplicate slots, we are likely loading from an old save where the content was serialized as a List<ItemStack>
		// In this case we assign slots based on the order in the list
		if (enderItems.size() == enderItems.stream().map(ItemStackWithSlot::slot).distinct().count()) return enderItems;
		return IntStream.range(0, enderItems.size())
            .mapToObj(i -> new ItemStackWithSlot(i, enderItems.get(i).stack())).toList();
	}

	@Inject(method = "equals", at = @At("RETURN"), cancellable = true)
	private void equals(Object obj, CallbackInfoReturnable<Boolean> cir) {
		// ScoreboardSaveData.setData() relies on PlayerTeam.Packed equality, so we need to take the ender chest inventory into account
		if (!cir.getReturnValue() || this == obj) return;
	    cir.setReturnValue( Objects.equals(this.getEnderChestContent(), ((IEnderChestHolder)obj).getEnderChestContent()) );
	}

	@Shadow @Final @Mutable public static Codec<PlayerTeam.Packed> CODEC;

 	@Inject(method="<clinit>",at=@At("TAIL"))
    private static void setCodec(CallbackInfo ci) {
		CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
				// Vanilla fields
				Codec.STRING.fieldOf("Name").forGetter(PlayerTeam.Packed::name),
				ComponentSerialization.CODEC.optionalFieldOf("DisplayName").forGetter(PlayerTeam.Packed::displayName),
				ChatFormatting.COLOR_CODEC.optionalFieldOf("TeamColor").forGetter(PlayerTeam.Packed::color),
				Codec.BOOL.optionalFieldOf("AllowFriendlyFire", true).forGetter(PlayerTeam.Packed::allowFriendlyFire),
				Codec.BOOL.optionalFieldOf("SeeFriendlyInvisibles", true).forGetter(PlayerTeam.Packed::seeFriendlyInvisibles),
				ComponentSerialization.CODEC.optionalFieldOf("MemberNamePrefix", CommonComponents.EMPTY).forGetter(PlayerTeam.Packed::memberNamePrefix),
				ComponentSerialization.CODEC.optionalFieldOf("MemberNameSuffix", CommonComponents.EMPTY).forGetter(PlayerTeam.Packed::memberNameSuffix),
				Visibility.CODEC.optionalFieldOf("NameTagVisibility", Visibility.ALWAYS).forGetter(PlayerTeam.Packed::nameTagVisibility), 
				Visibility.CODEC.optionalFieldOf("DeathMessageVisibility", Visibility.ALWAYS).forGetter(PlayerTeam.Packed::deathMessageVisibility),
				CollisionRule.CODEC.optionalFieldOf("CollisionRule", CollisionRule.ALWAYS).forGetter(PlayerTeam.Packed::collisionRule), 
				Codec.STRING.listOf().optionalFieldOf("Players", List.of()).forGetter(PlayerTeam.Packed::players),
				// Add the field for enderChestInventory
				ItemStackWithSlot.CODEC
					.withAlternative(MapCodec.unitCodec(new ItemStackWithSlot(0, ItemStack.EMPTY))) // upward compatibility with old saves
					.listOf().optionalFieldOf("EnderItems", List.of()).forGetter(packed -> ((IEnderChestHolder)(Object) packed).getEnderChestContent())
			).apply(instance, PlayerTeamPackedMixin::create);
		});
	}
}