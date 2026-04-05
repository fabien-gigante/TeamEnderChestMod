package com.fabien_gigante;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface IEnderChestHolder {
    TeamEnderChestContainer getEnderChestContainer();
    void setEnderChestContainer(TeamEnderChestContainer enderChestContainer);

    default List<ItemStack> getEnderItems() { return getEnderChestContainer().getItems(); }
    default void setEnderItems(List<ItemStack> enderItems) {
        TeamEnderChestContainer enderChestContainer = getEnderChestContainer();
        for(int i = 0; i < enderChestContainer.getContainerSize(); i++) enderChestContainer.items.set(i, enderItems.get(i));
    }
}
