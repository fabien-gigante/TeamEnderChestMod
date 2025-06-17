package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import com.fabien_gigante.TeamEnderChestUtil;

@Mixin(GenericContainerScreen.class)
public abstract class GenericContainerScreenMixin extends HandledScreen<GenericContainerScreenHandler> {
    @Shadow @Final private static  Identifier TEXTURE;
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
        int color = 0x80000000 | player.getTeamColorValue();
        int x = 8-2, y = 72-2, w = 9*18+2, h = 3*18+2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.x + x, this.y + y, x, y, w, h, 256, 256, color);
    }

}
