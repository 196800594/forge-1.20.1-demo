package com.alkaid.demo.item;

import com.alkaid.demo.DemoMod;
import com.alkaid.demo.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DemoMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DEMO_TAB =
            CREATIVE_MODE_TABS.register("demo_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.RAW_ICE_ETHER.get()))
                    .title(Component.translatable("itemGroup.demo_mod_tap"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.ICE_ETHER.get());
                        output.accept(ModItems.RAW_ICE_ETHER.get());
                        output.accept(ModItems.CARDBOARD.get());
                        output.accept(ModBlocks.RAW_ICE_ETHER_BLOCK.get());
                        output.accept(ModBlocks.CARDBOARD_BLOCK.get());
                        output.accept(ModBlocks.FROSTED_GLASS_BLOCK.get());
                        output.accept(ModBlocks.MAGICAL_BLOCK.get());
                        output.accept(ModItems.MENTAL_DETECTOR.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}