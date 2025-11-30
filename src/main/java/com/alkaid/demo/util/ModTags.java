package com.alkaid.demo.util;

import com.alkaid.demo.DemoMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> METAL_DETECTOR_VALUES = tags("metal_detector_values");
        private static TagKey<Block> tags(String name){
            return BlockTags.create(new ResourceLocation(DemoMod.MOD_ID, name));
        }
    }

    public static class Items {
        private static TagKey<Item> tags(String name) {
            return ItemTags.create(new ResourceLocation(DemoMod.MOD_ID, name));
        }
    }
}

