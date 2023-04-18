package galena.coopperative.data.provider;

import galena.coopperative.Coopperative;
import galena.coopperative.content.block.HeadLightBlock;
import galena.coopperative.content.block.TogglerBlock;
import galena.coopperative.content.block.weatheringvanilla.WeatheringPistonBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public abstract class CBlockStateProvider extends BlockStateProvider {

    public CBlockStateProvider(DataGenerator gen, ExistingFileHelper help) {
        super(gen, Coopperative.MOD_ID, help);
    }

    protected ResourceLocation texture(String name) {
        return modLoc("block/" + name);
    }

    protected static String name(Supplier<? extends Block> block) {
        return ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
    }

    protected static String weatheringPrefix(Supplier<? extends Block> block) {
        if (block.get() instanceof WeatheringCopper) {
            String name = name(block);
            String[] split = name.split("_");
            WeatheringCopper.WeatherState weatherState = ((WeatheringCopper) block.get()).getAge();
            for (String string : split) {
                if (string.equals(weatherState.name().toLowerCase(Locale.ROOT)) && !weatherState.equals(WeatheringCopper.WeatherState.UNAFFECTED))
                    return string + "_";
            }
        }
        return "";
    }

    protected int getXOrientation(Direction facing) {
        //if (facing == Direction.NORTH) do nothing;
        if (facing == Direction.DOWN) return 90;
        if (facing == Direction.UP) return 270;
        return 0;
    }

    protected int getYOrientation(Direction facing) {
        //if (facing == Direction.NORTH) do nothing;
        if (facing == Direction.EAST) return 90;
        if (facing == Direction.SOUTH) return 180;
        if (facing == Direction.WEST) return 270;
        return 0;
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

    public <B extends Block> void weatheringBlock(List<RegistryObject<B>> blockArrayList) {
        for (Supplier<B> blocks : blockArrayList)
            block(blocks);
    }

    public <B extends RotatedPillarBlock> void weatheringPillarBlock(List<RegistryObject<B>> blockArrayList) {
        for (Supplier<B> blocks : blockArrayList)
            logBlock(blocks.get());
    }

    public ModelFile repeaterModel(Supplier<? extends Block> block, int ticks, boolean powering, boolean locked) {
        String name = name(block);
        String affix = "_" + ticks + "tick" + (powering ? "_on" : "") + (locked ? "_locked" : "");
        return models().withExistingParent(name + affix, BLOCK_FOLDER + "/repeater" + affix)
                .texture("particle", texture(name + "_off"))
                .texture("slab", texture(weatheringPrefix(block) + "dispenser_side"))
                .texture("top", texture(name + (powering ? "_on" : "_off")))
                .texture("unlit", "minecraft:block/redstone_torch" + (powering ? "" : "_off"));
    }

    public void repeater(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            int ticks = state.getValue(RepeaterBlock.DELAY);
            boolean powered = state.getValue(RepeaterBlock.POWERED);
            boolean locked = state.getValue(RepeaterBlock.LOCKED);
            Direction facing = state.getValue(RepeaterBlock.FACING);
            int y = 180;
            //if (facing == Direction.NORTH) do nothing;
            if (facing == Direction.EAST) y = 270;
            if (facing == Direction.SOUTH) y = 0;
            if (facing == Direction.WEST) y = 90;

            return ConfiguredModel.builder().modelFile(repeaterModel(block, ticks, powered, locked)).rotationY(y).build();
        });
    }

    public ModelFile comparatorModel(Supplier<? extends Block> block, boolean powering, boolean subtracting) {
        String name = name(block);
        String affix = (powering ? "_on" : "") + (subtracting ? "_subtract" : "");
        return models().withExistingParent(name + affix, BLOCK_FOLDER + "/comparator" + affix)
                .texture("particle", texture(name + "_off"))
                .texture("slab", texture(weatheringPrefix(block) + "dispenser_side"))
                .texture("top", texture(name + (powering ? "_on" : "_off")));
    }

    public void comparator(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean powered = state.getValue(ComparatorBlock.POWERED);
            boolean subtracting = state.getValue(ComparatorBlock.MODE).equals(ComparatorMode.SUBTRACT);
            Direction facing = state.getValue(ComparatorBlock.FACING);
            int y = 180;
            //if (facing == Direction.NORTH) do nothing;
            if (facing == Direction.EAST) y = 270;
            if (facing == Direction.SOUTH) y = 0;
            if (facing == Direction.WEST) y = 90;

            return ConfiguredModel.builder().modelFile(comparatorModel(block, powered, subtracting)).rotationY(y).build();
        });
    }

    public ModelFile pistonModel(Supplier<? extends Block> block, boolean extended) {
        String name = name(block);
        ResourceLocation bottom = texture(weatheringPrefix(block) + "dispenser_top");
        ResourceLocation side = texture(name.replace("sticky_", "") + "_side");
        ResourceLocation platform = extended
                ? texture(weatheringPrefix(block) + "piston_inner")
                : mcLoc("block/piston_top" + (name.contains("sticky") ? "_sticky" : ""));
        String suffix = extended ? "_base" : "";
        return models().withExistingParent(name + suffix, BLOCK_FOLDER + "/piston" + suffix)
                .texture(extended ? "inside" : "platform", platform)
                .texture("bottom", bottom)
                .texture("side", side);
    }

    public void piston(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean extended = state.getValue(WeatheringPistonBlock.EXTENDED);
            Direction facing = state.getValue(WeatheringPistonBlock.FACING);
            int x = 0;
            int y = 0;
            //if (facing == Direction.NORTH) do nothing;
            if (facing == Direction.EAST) y = 90;
            if (facing == Direction.SOUTH) y = 180;
            if (facing == Direction.WEST) y = 270;
            if (facing == Direction.DOWN) x = 90;
            if (facing == Direction.UP) x = 270;

            return ConfiguredModel.builder().modelFile(pistonModel(block, extended)).rotationX(x).rotationY(y).build();
        });
    }

    public ModelFile orientableModel(Supplier<? extends Block> block) {
        String name = name(block);
        ResourceLocation top = texture(name + "_top");
        ResourceLocation front = texture(name + "_front");
        ResourceLocation side = texture(name + "_side");
        return models().orientable(name, side, front, top);
    }

    public void orientable(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction facing = state.getValue(DirectionalBlock.FACING);
            int x = getXOrientation(facing);
            int y = getYOrientation(facing);
            return ConfiguredModel.builder().modelFile(orientableModel(block)).rotationX(x).rotationY(y).build();
        });
    }

    public void dispenser(Supplier<? extends Block> block, boolean dropper) {
        String name = name(block);
        String weatherPrefix = weatheringPrefix(block);
        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction facing = state.getValue(DirectionalBlock.FACING);
            boolean upOrDown = facing == Direction.DOWN || facing == Direction.UP;
            ResourceLocation top = texture((dropper ? weatherPrefix + "dispenser" : name) + "_top");
            ResourceLocation front = texture(name + (upOrDown ? "_front" : ""));
            ResourceLocation side = texture((dropper ? weatherPrefix + "dispenser" : name) + "_side");
            int x = getXOrientation(facing);
            int y = getYOrientation(facing);
            if (upOrDown)
                return ConfiguredModel.builder().modelFile(models().orientable(name + "_vertical", side, front, top)).rotationX(x).build();
            return ConfiguredModel.builder().modelFile(models().orientable(name, side, front, top)).rotationY(y).build();
        });
    }

    public void dispenser(Supplier<? extends Block> block) {
        dispenser(block, false);
    }

    public void dropper(Supplier<? extends Block> block) {
        dispenser(block, true);
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
            int x = getXOrientation(facing);
            int y = getYOrientation(facing);

            return ConfiguredModel.builder().modelFile(observerModel(block, powered)).rotationX(x).rotationY(y).build();
        });
    }

    public void observer(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean powered = state.getValue(ObserverBlock.POWERED);
            Direction facing = state.getValue(ObserverBlock.FACING);
            int x = getXOrientation(facing);
            int y = getYOrientation(facing);

            return ConfiguredModel.builder().modelFile(observerModel(block, powered)).rotationX(x).rotationY(y).build();
        });
    }

    public ModelFile leverModel(Supplier<? extends Block> block, boolean powering) {
        String name = name(block);
        return models().withExistingParent(name + (powering ? "_on" : ""), texture("custom_lever" + (powering ? "_on" : "")))
                .texture("particle", texture(name))
                .texture("base", modLoc("block/" + name));
    }

    public void lever(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean powering = state.getValue(LeverBlock.POWERED);
            Direction facing = state.getValue(LeverBlock.FACING);
            AttachFace face = state.getValue(LeverBlock.FACE);
            int x = 0;
            int y = 0;
            /*
            I need to figure out a little more math to simplify these if statements.
             */
            if (face == AttachFace.CEILING) x = 180;
            //if (face == AttachFace.FLOOR) x = 0;
            if (face == AttachFace.WALL) x = 90;
            if (face == AttachFace.CEILING && facing == Direction.NORTH) {
                y = 180;
            }
            if (face == AttachFace.FLOOR && facing == Direction.NORTH) {
                // Do Nothing
            }
            if (face == AttachFace.WALL && facing == Direction.NORTH) {
                // Do Nothing
            }
            if (face == AttachFace.CEILING && facing == Direction.EAST) {
                y = 270;
            }
            if (face == AttachFace.FLOOR && facing == Direction.EAST) {
                y = 90;
            }
            if (face == AttachFace.WALL && facing == Direction.EAST) {
                y = 90;
            }
            if (face == AttachFace.CEILING && facing == Direction.SOUTH) {
                // Do Nothing
            }
            if (face == AttachFace.FLOOR && facing == Direction.SOUTH) {
                y = 180;
            }
            if (face == AttachFace.WALL && facing == Direction.SOUTH) {
                y = 180;
            }
            if (face == AttachFace.CEILING && facing == Direction.WEST) {
                y = 90;
            }
            if (face == AttachFace.FLOOR && facing == Direction.WEST) {
                y = 270;
            }
            if (face == AttachFace.WALL && facing == Direction.WEST) {
                y = 270;
            }

            return ConfiguredModel.builder().modelFile(leverModel(block, powering)).rotationX(x).rotationY(y).build();
        });
    }

    public ModelFile poweredRailModel(Supplier<? extends Block> block, String affix, Boolean powered, String shape) {
        String name = name(block);
        return models().withExistingParent(name + affix, BLOCK_FOLDER + "/powered_rail" + affix)
                .texture("rail", texture(name + (powered ? "_on" : "")));
    }

    public void poweredRail(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean powered = state.getValue(PoweredRailBlock.POWERED);
            String shape = state.getValue(PoweredRailBlock.SHAPE).getName();
            String affix = (powered ? "_on" : "") +
                    (state.getValue(PoweredRailBlock.SHAPE).isAscending() ? "_raised" : "") +
                    (shape.equals("ascending_east") || shape.equals("ascending_north") ? "_ne" : shape.equals("ascending_south") || shape.equals("ascending_west") ? "_sw" : "");
            int y =
                    shape.equals("ascending_east") || shape.equals("ascending_west") || shape.equals("east_west")
                            ? 90 : 0;

            return ConfiguredModel.builder().modelFile(poweredRailModel(block, affix, powered, shape)).rotationY(y).build();
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
                .texture("top", texture(weatheringPrefix(block) + "dispenser_top"))
                .texture("front", front)
                .texture("particle", side);
    }

    public void headlight(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean lit = state.getValue(HeadLightBlock.POWERED);
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

    public void headlight(List<RegistryObject<Block>> blockArrayList) {
        for (Supplier<? extends Block> blocks : blockArrayList) {
            headlight(blocks);
        }
    }

    public ModelFile togglerModel(Supplier<? extends Block> block, Boolean powering) {
        String name = name(block);
        String affix = powering ? "_on" : "";
        return models().withExistingParent(name + affix, modLoc("block/base_toggler" + affix))
                .texture("top", texture(name + affix));
    }

    public void toggler(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean powering = state.getValue(TogglerBlock.POWERING);
            Direction facing = state.getValue(TogglerBlock.FACING);
            int y = 180;
            //if (facing == Direction.NORTH) do nothing;
            if (facing == Direction.EAST) y = 270;
            if (facing == Direction.SOUTH) y = 0;
            if (facing == Direction.WEST) y = 90;

            return ConfiguredModel.builder().modelFile(togglerModel(block, powering)).rotationY(y).build();
        });
    }

    public void toggler(List<RegistryObject<Block>> blockArrayList) {
        for (Supplier<? extends Block> blocks : blockArrayList) {
            toggler(blocks);
        }
    }

    public void door(Supplier<? extends DoorBlock> block) {
        doorBlock(block.get(), texture(name(block) + "_bottom"), texture(name(block) + "_top"));
    }

    public void trapdoor(Supplier<? extends TrapDoorBlock> block) {
        trapdoorBlock(block.get(), texture(name(block)), true);
    }
}
