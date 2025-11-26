package com.fabien_gigante;

import java.util.List;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;

public interface IEnderChestHolder {
    PlayerEnderChestContainer getEnderChestContainer();
    void setEnderChestContainer(PlayerEnderChestContainer enderChestContainer);

    default List<ItemStack> getEnderItems() { return getEnderChestContainer().getItems(); }
    default void setEnderItems(List<ItemStack> enderItems) {
        PlayerEnderChestContainer enderChestContainer = getEnderChestContainer();
        for(int i = 0; i < enderChestContainer.getContainerSize(); i++) enderChestContainer.items.set(i, enderItems.get(i));
    }
}
