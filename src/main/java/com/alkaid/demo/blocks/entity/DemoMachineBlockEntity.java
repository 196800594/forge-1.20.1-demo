package com.alkaid.demo.blocks.entity;

import com.alkaid.demo.blocks.screen.DemoMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DemoMachineBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2);
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT = 1;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private final int maxProgress = 60;


    //进度条
    public DemoMachineBlockEntity(BlockPos pPos, BlockState pStates) {
        super(ModBlockEntities.DEMO_MACHINE_BE.get(), pPos, pStates);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DemoMachineBlockEntity.this.progress;
                    case 1 -> DemoMachineBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DemoMachineBlockEntity.this.progress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }



//    这是 Forge 的“能力接口”，用来告诉其他模组或系统：“我这个方块实体有什么功能？”
//    当某个模组想从你的方块中获取物品库存、能量、红石信号等时，它会调用 getBlockEntity().getCapability(...)。
//    如果你没有实现这个方法，它将返回空，导致无法交互。
//
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }



//    这是一个生命周期回调方法，当方块实体被加载到世界中时调用。
//
//    比如你进入一个区块，或者服务器启动，游戏会自动调用此方法。
//    通常在这里执行一些初始化操作，比如：
//    设置 LazyOptional 的值
//            注册事件监听器
//    启动后台任务
//🔍 注意事项：
//    不要在这里做耗时操作（如读取大量 NBT）
//    必须调用 super.onLoad()，否则可能影响 Forge 的内部机制
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

//    这是另一个生命周期方法，当方块实体即将被移除或失效时调用。
//
//    例如：方块被破坏、玩家离开维度、服务器重启
//    它的作用是 清理资源、取消注册、释放引用
//    特别重要的是：必须清除 LazyOptional 的引用，防止内存泄漏
//🔍 为什么需要？
//    如果你不清理 LazyOptional，即使方块已经被删除，其他模组仍可能持有它的引用，导致崩溃或错误
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }


    //方块被破坏时掉落容器内部物品
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
//        当方块被破坏时，将容器（inventory）中的所有物品掉落出来。
//        具体通过 Containers.dropContents 方法实现，该方法会将指定位置（worldPosition）处的物品以实体形式投放到世界中。
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    //游戏关闭时保存物品数据和进度整数
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("demo_machine.progress", progress);
        super.saveAdditional(pTag);
    }

    //游戏启动时加载物品数据和进度整数
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("demo_machine.progress");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRicipe()){
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()){
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        ItemStack result = new ItemStack(Items.DIAMOND, 1);
        this.itemHandler.extractItem(SLOT_INPUT, 1, false);
        this.itemHandler.setStackInSlot(SLOT_OUTPUT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(SLOT_OUTPUT).getCount()+result.getCount()));
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRicipe() {
        boolean hasCraftingItem = this.itemHandler.getStackInSlot(SLOT_INPUT).getItem() == Items.COAL;
        ItemStack result = new ItemStack(Items.DIAMOND);

        return hasCraftingItem && canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(SLOT_OUTPUT).isEmpty() || this.itemHandler.getStackInSlot(SLOT_OUTPUT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(SLOT_OUTPUT).getCount() + count <= this.itemHandler.getStackInSlot(SLOT_OUTPUT).getMaxStackSize();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.demo_mod.magical_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DemoMachineMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}
