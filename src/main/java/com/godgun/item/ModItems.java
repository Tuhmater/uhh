package com.godgun.item;

import com.godgun.GodGunMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GodGunMod.MODID);

    public static final RegistryObject<Item> GOD_GUN =
            ITEMS.register("god_gun", GodGunItem::new);
}
