package com.fabien_gigante;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TeamEnderChestScreen extends GenericContainerScreen {
	private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/generic_54.png");
    protected PlayerEntity player;

    public TeamEnderChestScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title); 
        this.player = inventory.player;
    }

	@Override
	protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        super.drawBackground(context, deltaTicks, mouseX, mouseY);
        int color = 0x80000000 | player.getTeamColorValue();
        int x = 8-2, y = 72-2, w = 9*18+2, h = 3*18+2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.x + x, this.y + y, x, y, w, h, 256, 256, color);
    }
}
