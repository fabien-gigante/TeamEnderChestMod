package com.fabien_gigante;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;

public interface IEnderChestHolder {
    PlayerEnderChestContainer getEnderChestContainer();

    default List<ItemStackWithSlot> getEnderChestContent() {
        return convertToSlots(getEnderChestContainer().getItems());
    }

    static List<ItemStackWithSlot> convertToSlots(List<ItemStack> items) {
		List<ItemStackWithSlot> converted = new ArrayList<>();
		for(int i = 0; i < items.size(); i++) {
			ItemStack stack = items.get(i);
			if (!stack.isEmpty()) converted.add(new ItemStackWithSlot(i, stack));
		}
        return converted;
    }

    default void setEnderChestContent(List<ItemStackWithSlot> otherItems) {
        var container = getEnderChestContainer();
        container.clearContent();
        for(ItemStackWithSlot item : otherItems) container.setItem(item.slot(), item.stack());
    }

    default void setEnderChestContent(IEnderChestHolder source) {
        setEnderChestContent(source.getEnderChestContent());
    }
}
