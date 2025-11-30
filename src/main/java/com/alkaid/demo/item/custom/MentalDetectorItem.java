package com.alkaid.demo.item.custom;

import com.alkaid.demo.util.ModTags;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MentalDetectorItem extends Item {
    public MentalDetectorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide){
            BlockPos clickedPos = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            boolean foundBlock = false;
            for (int i=0;i<clickedPos.getY() + 64;i++){
                BlockState state = pContext.getLevel().getBlockState(clickedPos.below(i));

                if (isValueableBlock(state)){
                    outPutValueableCoordinates(player, clickedPos.below(i), state.getBlock());
                    foundBlock = true;
                    break;
                }
            }
            if (!foundBlock){
                player.sendSystemMessage(Component.literal("没有发现任何矿物"));
            }
        }

        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;
    }

    private void outPutValueableCoordinates(Player player, BlockPos pos, Block block){
        player.sendSystemMessage(Component.literal(" 在 (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")" + "发现 " + I18n.get(block.getDescriptionId())));

    }

    private boolean isValueableBlock(BlockState state){
        return state.is(ModTags.Blocks.METAL_DETECTOR_VALUES);

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pIsAdvanced) {
        pTooltip.add(Component.translatable("tooltip.demo_mod.mental_detector.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltip, pIsAdvanced);
    }
}
