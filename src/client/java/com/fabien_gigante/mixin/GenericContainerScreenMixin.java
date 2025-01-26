package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import com.fabien_gigante.TeamEnderChestUtil;

@Mixin(GenericContainerScreen.class)
public abstract class GenericContainerScreenMixin extends HandledScreen<GenericContainerScreenHandler> {
    private boolean isTeamEnderChest = false;
    protected PlayerEntity player;

    private GenericContainerScreenMixin(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) { super(handler, inventory, title); }

	@Inject(method="<init>", at=@At(value="TAIL"))
	private void init(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
		this.player = inventory.player;
        this.isTeamEnderChest = handler.getInventory().size() == 54 && title.equals(TeamEnderChestUtil.getTeamEnderChestTitle(player));
	}

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (!this.isTeamEnderChest) return;
        int color = 0x30000000 | player.getTeamColorValue();
        for(int i = 27; i < 54; i++) {
            Slot slot = this.handler.getSlot(i);
            context.fill(RenderLayer.getGui(), this.x + slot.x, this.y + slot.y, this.x + slot.x + 16,this.y + slot.y + 16, color);
        }
    }

}
