package galena.coopperative.data.provider;

import com.machinezoo.noexception.optional.OptionalBoolean;
import galena.coopperative.Coopperative;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public abstract class CBlockStateProvider extends BlockStateProvider {

    public CBlockStateProvider(DataGenerator gen, ExistingFileHelper help) {
        super(gen, Coopperative.MOD_ID, help);
    }

    protected ResourceLocation texture(String name) {
        return modLoc("block/" + name);
    }

    protected String name(Supplier<? extends Block> block) {
        return ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
    }

    public void block(Supplier<? extends Block> block) {
        simpleBlock(block.get());
    }

    public ModelFile observerModel(Supplier<? extends Block> block, Boolean powered) {
        String name = name(block);
        ResourceLocation side = texture(name + "_side");
        ResourceLocation front = texture(name + "_front");
        ResourceLocation back = texture(name + "_back" + (powered ? "_on" : "_off"));
        ResourceLocation top = texture(name + "_top");
        return models().withExistingParent(name, BLOCK_FOLDER + "/observer")
                .texture("bottom", back)
                .texture("side", side)
                .texture("top", top)
                .texture("front", front)
                .texture("particle", front);
    }

    public void observer(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean powered = state.getValue(ObserverBlock.POWERED);
            Direction facing = state.getValue(ObserverBlock.FACING);
            int x = 0;
            int y = 0;
            //if (facing == Direction.NORTH) do nothing;
            if (facing == Direction.EAST) y = 90;
            if (facing == Direction.SOUTH) y = 180;
            if (facing == Direction.WEST) y = 270;
            if (facing == Direction.DOWN) x = 90;
            if (facing == Direction.UP) x = 270;

            return ConfiguredModel.builder().modelFile(observerModel(block, powered)).rotationX(x).rotationY(y).build();
        });
    }

    public void door(Supplier<? extends DoorBlock> block, String name) {
        doorBlockInternal(block.get(), name(block), texture(name + "_door_bottom"), texture(name + "_door_top"));
    }

    private void doorBlockInternal(DoorBlock block, String baseName, ResourceLocation bottom, ResourceLocation top) {
        ModelFile bottomLeft = door(baseName + "_bottom_left", "door_bottom_left", bottom, top);
        ModelFile bottomLeftOpen = door(baseName + "_bottom_left_open", "door_bottom_left_open", bottom, top);
        ModelFile bottomRight = door(baseName + "_bottom_right", "door_bottom_right", bottom, top);
        ModelFile bottomRightOpen = door(baseName + "_bottom_right_open", "door_bottom_right_open", bottom, top);
        ModelFile topLeft = door(baseName + "_top_left", "door_top_left", bottom, top);
        ModelFile topLeftOpen = door(baseName + "_top_left_open", "door_top_left_open", bottom, top);
        ModelFile topRight = door(baseName + "_top_right", "door_top_right", bottom, top);
        ModelFile topRightOpen = door(baseName + "_top_right_open", "door_top_right_open", bottom, top);
        doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
    }

    private ModelBuilder<?> door(String name, String model, ResourceLocation bottom, ResourceLocation top) {
        return models().withExistingParent(name, "block/" + model)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    private void doorBlock(DoorBlock block, ModelFile bottomLeft, ModelFile bottomLeftOpen, ModelFile bottomRight, ModelFile bottomRightOpen, ModelFile topLeft, ModelFile topLeftOpen, ModelFile topRight, ModelFile topRightOpen) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            int yRot = ((int) state.getValue(DoorBlock.FACING).toYRot()) + 90;
            boolean right = state.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
            boolean open = state.getValue(DoorBlock.OPEN);
            if (open) {
                yRot += 90;
            }
            if (right && open) {
                yRot += 180;
            }
            yRot %= 360;
            return ConfiguredModel.builder().modelFile(state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? (right ? (open ? bottomRightOpen : bottomRight) : (open ? bottomLeftOpen : bottomLeft)) : (right ? (open ? topRightOpen : topRight) : (open ? topLeftOpen : topLeft)))
                    .rotationY(yRot)
                    .build();
        }, DoorBlock.POWERED);
    }

    public void trapdoor(Supplier<? extends TrapDoorBlock> block, String name) {
        trapdoorBlock(block.get(), texture(name + "_trapdoor"), true);
    }
}
