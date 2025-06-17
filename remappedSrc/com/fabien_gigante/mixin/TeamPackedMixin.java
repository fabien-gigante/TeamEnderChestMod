package com.fabien_gigante.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fabien_gigante.IEnderChestHolder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextCodecs;
import net.minecraft.text.Text;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.Formatting;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.AbstractTeam.CollisionRule;
import net.minecraft.scoreboard.AbstractTeam.VisibilityRule;

@Mixin(Team.Packed.class)
public class TeamPackedMixin implements IEnderChestHolder {
    @Unique private EnderChestInventory enderChestInventory = new EnderChestInventory();

	@Override
	public EnderChestInventory getEnderChestInventory() { return this.enderChestInventory; }
	@Override
    public void setEnderChestInventory(EnderChestInventory enderChestInventory) { this.enderChestInventory = enderChestInventory;} 

	private static Team.Packed create(String name, Optional<Text> displayName, Optional<Formatting> color, boolean allowFriendlyFire, boolean seeFriendlyInvisibles, Text memberNamePrefix, Text memberNameSuffix, VisibilityRule nameTagVisibility, VisibilityRule deathMessageVisibility, CollisionRule collisionRule, List<String> players, List<ItemStack> enderItems) {
        Team.Packed packed = new Team.Packed(name, displayName, color, allowFriendlyFire, seeFriendlyInvisibles, memberNamePrefix, memberNameSuffix, nameTagVisibility, deathMessageVisibility, collisionRule, players);
		if (enderItems != null) ((TeamPackedMixin) (Object) packed).setEnderItems(enderItems);
        return packed;
	}

	@Shadow @Final @Mutable public static Codec<Team.Packed> CODEC;

 	@Inject(method="<clinit>",at=@At("TAIL"))
    private static void setCodec(CallbackInfo ci) {
		CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
				Codec.STRING.fieldOf("Name").forGetter(Team.Packed::name),
				TextCodecs.CODEC.optionalFieldOf("DisplayName").forGetter(Team.Packed::displayName),
				Formatting.COLOR_CODEC.optionalFieldOf("TeamColor").forGetter(Team.Packed::color),
				Codec.BOOL.optionalFieldOf("AllowFriendlyFire", true).forGetter(Team.Packed::allowFriendlyFire),
				Codec.BOOL.optionalFieldOf("SeeFriendlyInvisibles", true).forGetter(Team.Packed::seeFriendlyInvisibles),
				TextCodecs.CODEC.optionalFieldOf("MemberNamePrefix", ScreenTexts.EMPTY).forGetter(Team.Packed::memberNamePrefix),
				TextCodecs.CODEC.optionalFieldOf("MemberNameSuffix", ScreenTexts.EMPTY).forGetter(Team.Packed::memberNameSuffix),
				VisibilityRule.CODEC.optionalFieldOf("NameTagVisibility", VisibilityRule.ALWAYS).forGetter(Team.Packed::nameTagVisibility), 
				VisibilityRule.CODEC.optionalFieldOf("DeathMessageVisibility", VisibilityRule.ALWAYS).forGetter(Team.Packed::deathMessageVisibility),
				CollisionRule.CODEC.optionalFieldOf("CollisionRule", CollisionRule.ALWAYS).forGetter(Team.Packed::collisionRule), 
				Codec.STRING.listOf().optionalFieldOf("Players", List.of()).forGetter(Team.Packed::players),
				// Add the field for enderChestInventory
				ItemStack.OPTIONAL_CODEC.listOf().optionalFieldOf("EnderItems", null).forGetter(packed -> ((TeamPackedMixin) (Object) packed).getEnderItems())
			).apply(instance, TeamPackedMixin::create);
		});
	}
}