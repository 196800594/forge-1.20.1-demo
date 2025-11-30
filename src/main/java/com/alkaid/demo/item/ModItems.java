package com.alkaid.demo.item;

import com.alkaid.demo.DemoMod;
import com.alkaid.demo.item.custom.FuelItem;
import com.alkaid.demo.item.custom.MentalDetectorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    /**
     * 物品注册器，用于注册模组中的所有物品
     * 通过DeferredRegister机制延迟注册物品到Forge注册表中
     */
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DemoMod.MOD_ID);

    /**
     * 冰以太物品的注册对象
     * 使用RegistryObject包装注册的物品实例，确保在正确的时机进行初始化
     */
    public static final RegistryObject<Item> ICE_ETHER =
            ITEMS.register("ice_ether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_ICE_ETHER =
            ITEMS.register("raw_ice_ether", () -> new Item(new Item.Properties().food(ModFoods.FOOD_AI)));
    public static final RegistryObject<Item> CARDBOARD =
            ITEMS.register("material/cardboard", () -> new FuelItem(new Item.Properties(), 200));

    public static final RegistryObject<Item> MENTAL_DETECTOR =
            ITEMS.register("mental_detector", () -> new MentalDetectorItem(new Item.Properties().durability(100)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
