package com.fabien_gigante;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;

public class TeamEnderChestScreen extends ContainerScreen {
	private static final ResourceLocation CONTAINER_BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    protected Player player;

    public TeamEnderChestScreen(ChestMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title); 
        this.player = inventory.player;
    }

	@Override
	protected void renderBg(GuiGraphics graphics, float deltaTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, deltaTicks, mouseX, mouseY);
        int color = ARGB.color(/* alpha: */ 128,  player.getTeamColor());
        int x = 8-2, y = 72-2, w = 9*18+2, h = 3*18+2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, this.leftPos + x, this.topPos + y, x, y, w, h, 256, 256, color);
    }
}
