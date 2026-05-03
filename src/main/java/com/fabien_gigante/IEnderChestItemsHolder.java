package com.fabien_gigante;

import java.util.List;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;

public interface IEnderChestItemsHolder {
    default @Nullable PlayerEnderChestContainer getEnderChestContainer() { return null; }
    default List<ItemStack> getEnderChestItems() { 
        var container = getEnderChestContainer();
        assert container != null : "One of getEnderChestItems or getEnderChestContainer should be overridden";
        return container.getItems(); 
    }
    
    default void setEnderChestItems(List<ItemStack> enderItems) {
        List<ItemStack> items = getEnderChestItems();
        assert items != null && enderItems != null && items.size() == enderItems.size();
        for (int i = 0; i < items.size(); i++) items.set(i, enderItems.get(i));
        var container = getEnderChestContainer();
        if (container != null) container.setChanged();
    }
}
