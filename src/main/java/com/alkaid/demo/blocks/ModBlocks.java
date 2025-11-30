package com.alkaid.demo.blocks;

import com.alkaid.demo.DemoMod;
import com.alkaid.demo.blocks.custom.DemoMachineBlock;
import com.alkaid.demo.blocks.custom.SoundBlock;
import com.alkaid.demo.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * ModBlocks类用于注册和管理模组中的方块
 * 包含方块的创建、注册以及相关物品的自动注册功能
 */
public class ModBlocks {
    /**
     * 方块注册器，用于延迟注册方块到Forge注册表中
     * 使用MOD_ID作为命名空间标识该模组的方块
     */
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DemoMod.MOD_ID);

    /**
     * 原始冰醚方块的注册对象
     * 具有1.5的硬度和3.0的爆炸抗性
     */
    public static final RegistryObject<Block> RAW_ICE_ETHER_BLOCK = registerBlock("raw_ice_ether_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(1.5F, 3.0F)));

    /**
     * 纸板方块的注册对象
     * 属性复制自原版石头方块
     */
    public static final RegistryObject<Block> CARDBOARD_BLOCK = registerBlock("material/cardboard_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));


    public static final  RegistryObject<Block> FROSTED_GLASS_BLOCK = registerBlock("frosted_glass_block",
            () -> new GlassBlock(BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.GLASS).strength(0.3f)));

    public static final RegistryObject<Block> MAGICAL_BLOCK = registerBlock("magical_block",
            () -> new DemoMachineBlock(BlockBehaviour.Properties.of().copy(Blocks.IRON_BLOCK).noOcclusion()));



    //透明方块
    public static final List<RegistryObject<Block>> TRANSLUCENT_BLOCKS = Arrays.asList(
            FROSTED_GLASS_BLOCK
    );



    private static <T extends Block> void registerBlockItems(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> blocks = BLOCKS.register(name, block);
        registerBlockItems(name, blocks);
        return blocks;

    }

    /**
     * 将方块注册器注册到事件总线
     * 完成所有方块的实际注册过程
     *
     * @param eventBus Forge事件总线，用于处理注册事件
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }


}
