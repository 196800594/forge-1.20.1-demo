package com.alkaid.demo.blocks.entity;

import com.alkaid.demo.DemoMod;
import com.alkaid.demo.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DemoMod.MOD_ID);

    //将方块实体与方块绑定

    public static final RegistryObject<BlockEntityType<DemoMachineBlockEntity>> DEMO_MACHINE_BE =
            BLOCK_ENTITIES.register("demo_machine_be",
                    () -> BlockEntityType.Builder.of(DemoMachineBlockEntity::new,
                            ModBlocks.MAGICAL_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
