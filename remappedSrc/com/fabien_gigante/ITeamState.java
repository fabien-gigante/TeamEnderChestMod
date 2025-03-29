package com.fabien_gigante;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

public interface ITeamState {
    public NbtCompound teamToNbt(String team, RegistryWrapper.WrapperLookup registries);
}
