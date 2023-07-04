package galena.coopperative.data.provider;

import galena.coopperative.Coopperative;
import galena.coopperative.content.block.HeadLightBlock;
import galena.coopperative.content.block.TogglerBlock;
import galena.coopperative.content.block.weatheringvanilla.WeatheringPistonBlock;
import galena.oreganized.content.block.ExposerBlock;
import io.netty.util.collection.IntObjectHashMap;
import net.mehvahdjukaar.moonlight.api.util.math.Vec2i;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CrankBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.quark.content.automation.base.RandomizerPowerState;
import vazkii.quark.content.automation.block.RedstoneRandomizerBlock;

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
        String suffix = powering ? "_on" : "";
        return models().withExistingParent(name + suffix, modLoc("block/base_toggler" + suffix))
                .texture("top", texture(name + suffix));
    }

    public void toggler(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            boolean powering = state.getValue(TogglerBlock.POWERING);
            Direction facing = state.getValue(TogglerBlock.FACING);
            int y = 180;
            //if (facing == Direction.NORTH) do nothing;
            if (facing == Direction.EAST) y = 270;
            if (facing == Direction.SOUTH) y = 0;
            if (facing == Direction.WEST) y = 90;

            return ConfiguredModel.builder().modelFile(togglerModel(block, powering)).rotationY(y).build();
        }, TogglerBlock.POWERED);
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

    public void exposer(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            var level = Math.round((ExposerBlock.TexturedFrames - 1) / (float) (state.getValue(ExposerBlock.LEVEL) + 1));
            var facing = state.getValue(DirectionalBlock.FACING);

            int x = 0;
            int y = 0;
            if (facing == Direction.EAST) y = 90;
            if (facing == Direction.SOUTH) y = 180;
            if (facing == Direction.WEST) y = 270;
            if (facing == Direction.DOWN) x = 90;
            if (facing == Direction.UP) x = 270;

            var base = "block/compat/oreganized/" + name(block);
            var front = base + "_level_" + level;
            var side = base + "_side";
            var back = state.getValue(ExposerBlock.LEVEL) > 0 ? base + "_back_on" : base + "_back";
            var top = base + "_top";

            var model = models().withExistingParent(front + "_" + facing, new ResourceLocation("block/observer"))
                    .texture("bottom", back)
                    .texture("side", side)
                    .texture("top", top)
                    .texture("particle", front)
                    .texture("front", front);

            return ConfiguredModel.builder().modelFile(model).rotationX(x).rotationY(y).build();
        });
    }

    private Vec2i rotation(Direction facing) {
        if (facing == Direction.EAST) return new Vec2i(0, 90);
        if (facing == Direction.SOUTH) return new Vec2i(0, 180);
        if (facing == Direction.WEST) return new Vec2i(0, 270);
        if (facing == Direction.DOWN) return new Vec2i(90, 0);
        if (facing == Direction.UP) return new Vec2i(270, 0);
        return new Vec2i(0, 0);
    }

    public void relayer(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            var powered = state.getValue(BlockStateProperties.POWERED);
            var facing = state.getValue(DirectionalBlock.FACING);
            var rotation = rotation(facing);

            var name = "block/compat/supplementaries/" + name(block);
            var suffix = powered ? "_on" : "_off";

            var model = models().withExistingParent(name + suffix, new ResourceLocation(Coopperative.MOD_ID, "block/base_relayer"))
                    .texture("side", name + "_side")
                    .texture("bottom", name + "_back" + suffix)
                    .texture("platform", name + "_front");
            return ConfiguredModel.builder().modelFile(model).rotationX(rotation.x()).rotationY(rotation.y()).build();
        }, BlockStateProperties.POWER);
    }

    public void crank(Supplier<? extends Block> block) {
        var name = "block/compat/supplementaries/" + name(block);
        var base = models().withExistingParent(name + "_base", new ResourceLocation("supplementaries", "block/crank/crank_base"))
                .texture("1", name + "_base")
                .texture("particle", name + "_base");
        var handles = new IntObjectHashMap<ModelFile>();

        for (int i = 0; i < 16; i++) {
            var model = models().withExistingParent(name + "_handle_" + i, new ResourceLocation("supplementaries", "block/crank/crank_handle_" + i))
                    .texture("0", name + "_handle")
                    .texture("particle", name + "_handle");
            handles.put(i, model);
        }

        var builder = getMultipartBuilder(block.get());
        for (Direction facing : Direction.values()) {
            var rotation = rotation(facing);
            builder.part()
                    .modelFile(base).rotationX(rotation.x()).rotationY(rotation.y()).addModel()
                    .condition(CrankBlock.FACING, facing);


            for (int i = 0; i < 16; i++) {
                builder.part()
                        .modelFile(handles.get(i)).rotationX(rotation.x()).rotationY(rotation.y()).addModel()
                        .condition(CrankBlock.FACING, facing).condition(CrankBlock.POWER, i);
            }
        }
    }

    public void randomizer(Supplier<? extends Block> block) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            RandomizerPowerState powered = state.getValue(RedstoneRandomizerBlock.POWERED);
            var facing = state.getValue(RedstoneRandomizerBlock.FACING);
            var rotation = rotation(facing);

            var name = "block/compat/quark/" + name(block);
            var suffix = switch (powered) {
                case OFF -> "_off";
                case LEFT -> "_on_left";
                case RIGHT -> "_on_right";
            };

            var model = models().withExistingParent(name + suffix, new ResourceLocation("quark", "block/randomizer" + suffix))
                    .texture("particle", name + suffix)
                    .texture("slab", name + suffix)
                    .texture("top", name + suffix);
            return ConfiguredModel.builder().modelFile(model).rotationX(rotation.x()).rotationY(rotation.y() + 180).build();
        }, BlockStateProperties.POWER);
    }
}
