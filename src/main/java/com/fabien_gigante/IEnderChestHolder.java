package com.fabien_gigante;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EnderChestInventory;

public interface IEnderChestHolder {
    EnderChestInventory getEnderChestInventory();
    void setEnderChestInventory(EnderChestInventory enderChestInventory);

    default List<ItemStack> getEnderItems() { return getEnderChestInventory().getHeldStacks(); }
    default void setEnderItems(List<ItemStack> enderItems) {
        EnderChestInventory enderChest = getEnderChestInventory();
        for(int i = 0; i < enderChest.size(); i++) enderChest.heldStacks.set(i, enderItems.get(i));
    }
}
