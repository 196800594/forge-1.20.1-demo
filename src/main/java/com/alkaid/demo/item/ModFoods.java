package com.alkaid.demo.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties FOOD_AI =
            new FoodProperties.Builder()
                    .nutrition(8)
                    .saturationMod(0.8f)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200), 3f)
                    .alwaysEat()
                    .build();

}
