package galena.copperative.content.inventory.menu;

import galena.copperative.index.CMenuTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SolderingMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess accessWorld;

    private final Container ingredientSlots = new SimpleContainer(4) {
        @Override
        public void setChanged() {
            SolderingMenu.this.slotsChanged(this);
            super.setChanged();
        }
    };

    private final ResultContainer resultSlots = new ResultContainer() {
        @Override
        public void setChanged() {
            SolderingMenu.this.slotsChanged(this);
            super.setChanged();
        }
    };

    public SolderingMenu(int id, Inventory inventory, ContainerLevelAccess accessWorld) {
        super(CMenuTypes.SOLDERING_MENU.get(), id);
        this.accessWorld = accessWorld;
        this.addSlot(new Slot(this.ingredientSlots, 0, 44, 29));
        this.addSlot(new Slot(this.ingredientSlots, 1, 44, 50));
        this.addSlot(new Slot(this.ingredientSlots, 2, 80, 50));
        this.addSlot(new Slot(this.ingredientSlots, 3, 116, 50));
        this.addSlot(new Slot(this.resultSlots, 4, 79, 21) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack itemStack) {
                SolderingMenu.this.slots.get(0).remove(1);
                SolderingMenu.this.slots.get(1).remove(1);
                SolderingMenu.this.slots.get(2).remove(1);
                SolderingMenu.this.slots.get(3).remove(1);
                super.onTake(player, itemStack);
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    public SolderingMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
        //return stillValid(this.accessWorld, player, CBlocks.SOLDERING_TABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        // TODO what does this method even do?
        return ItemStack.EMPTY;
    }
}
