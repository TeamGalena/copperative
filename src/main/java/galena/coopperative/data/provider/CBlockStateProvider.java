package galena.coopperative.data.provider;

import galena.coopperative.Coopperative;
import galena.coopperative.content.block.HeadLightBlock;
import galena.coopperative.content.block.WeatheringPillarBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
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

    public void axisBlock(Supplier<? extends RotatedPillarBlock> block) {
        axisBlock(block.get());
    }

    public void logBlock(Supplier<? extends RotatedPillarBlock> block) {
        logBlock(block.get());
    }

    public void weatheringBlock(ArrayList<RegistryObject<WeatheringCopperFullBlock>> blockArrayList) {
        for (Supplier<? extends Block> blocks : blockArrayList)
            block(blocks);
    }

    public void weatheringPillarBlock(ArrayList<RegistryObject<WeatheringPillarBlock>> blockArrayList) {
        for (Supplier<? extends RotatedPillarBlock> blocks : blockArrayList)
            logBlock(blocks.get());
    }

    public void copperTilesBlock(ArrayList<RegistryObject<WeatheringPillarBlock>> blockArrayList) {
        for (Supplier<? extends RotatedPillarBlock> blocks : blockArrayList)
            axisBlock(blocks.get(), copperTilesModel(blocks, false), copperTilesModel(blocks, true));
    }

    public ModelFile copperTilesModel(Supplier<? extends Block> block, Boolean hv) {
        String name = name(block);
        ResourceLocation side = texture(name + (hv ? "_horizontal" : "_vertical"));
        ResourceLocation end = texture(name + "_top");
        return models().cubeColumn(name, side, end);
    }

    public ModelFile observerModel(Supplier<? extends Block> block, Boolean powered) {
        String name = name(block);
        ResourceLocation side = texture(name + "_side");
        ResourceLocation front = texture(name + "_front");
        ResourceLocation back = texture(name + "_back" + (powered ? "_on" : "_off"));
        ResourceLocation top = texture(name + "_top");
        return models().withExistingParent(name + (powered ? "_powered" : ""), BLOCK_FOLDER + "/observer")
                .texture("bottom", back)
                .texture("side", side)
                .texture("top", top)
                .texture("front", front)
                .texture("particle", front);
    }

    public void observer(Supplier<? extends Block> waxed, Supplier<? extends Block> block) {
        getVariantBuilder(waxed.get()).forAllStates(state -> {
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

    public ModelFile headlightModel(Supplier<? extends Block> block, Boolean lit, Boolean broken) {
        String name = name(block);
        ResourceLocation side = texture(name + "_side");
        ResourceLocation front = texture(name + (broken ? "_broken" : lit ? "_lit" : "_unlit"));
        ResourceLocation back = texture(name + "_back" + (lit ? "_on" : "_off"));
        return models().withExistingParent(name + (broken ? "_broken" : lit ? "_lit" : ""), BLOCK_FOLDER + "/observer")
                .texture("bottom", back)
                .texture("side", side)
                .texture("top", texture("dispenser_top"))
                .texture("front", front)
                .texture("particle", side);
    }

    public void headlight(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean lit = state.getValue(HeadLightBlock.LIT);
            boolean broken = state.getValue(HeadLightBlock.BROKEN);
            Direction facing = state.getValue(HeadLightBlock.FACING);
            int x = 0;
            int y = 0;
            //if (facing == Direction.NORTH) do nothing;
            if (facing == Direction.EAST) y = 90;
            if (facing == Direction.SOUTH) y = 180;
            if (facing == Direction.WEST) y = 270;
            if (facing == Direction.DOWN) x = 90;
            if (facing == Direction.UP) x = 270;

            return ConfiguredModel.builder().modelFile(headlightModel(block, lit, broken)).rotationX(x).rotationY(y).build();
        });
    }

    public void door(Supplier<? extends DoorBlock> block) {
        doorBlock(block.get(), texture(name(block) + "_bottom"), texture(name(block) + "_top"));
    }

    public void trapdoor(Supplier<? extends TrapDoorBlock> block) {
        trapdoorBlock(block.get(), texture(name(block)), true);
    }
}
