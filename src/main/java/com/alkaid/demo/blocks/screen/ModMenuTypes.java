package com.alkaid.demo.blocks.screen;

import com.alkaid.demo.DemoMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, DemoMod.MOD_ID);

    public static final RegistryObject<MenuType<DemoMachineMenu>> DEMO_MACHINE_MENU =
            registerMenuTypes("demo_machine_menu", DemoMachineMenu::new);

    public static final <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuTypes(String name, IContainerFactory<T> factory){
        return  MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
