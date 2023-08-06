package galena.copperative.data;

import galena.copperative.Copperative;
import galena.copperative.data.provider.CBlockStateProvider;
import galena.copperative.index.CBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class CBlockStates extends CBlockStateProvider {

    public CBlockStates(DataGenerator gen, ExistingFileHelper help) {
        super(gen, help);
    }

    @Override
    public @NotNull String getName() {
        return Copperative.MOD_NAME + " Block States";
    }

    private static String unwaxedName(Supplier<? extends Block> block) {
        return name(block).substring(6);
    }

    @Override
    protected void registerStatesAndModels() {
        staticColumn(CBlocks.PATINA_BLOCK);
        weatheringBlock(CBlocks.COPPER_BRICKS);
        weatheringPillarBlock(CBlocks.COPPER_PILLAR);
        weatheringPillarBlock(CBlocks.COPPER_TILES);

        CBlocks.REPEATERS.weathered().forEach(this::repeater);

        CBlocks.COMPARATORS.weathered().forEach(this::comparator);

        CBlocks.PISTONS.weathered().forEach(this::piston);

        CBlocks.STICKY_PISTONS.weathered().forEach(this::piston);

        CBlocks.DISPENSERS.weathered().forEach(this::dispenser);

        CBlocks.DROPPERS.weathered().forEach(this::dropper);

        CBlocks.OBSERVERS.weathered().forEach(this::observer);

        CBlocks.LEVERS.weathered().forEach(this::lever);

        CBlocks.POWERED_RAILS.weathered().forEach(this::poweredRail);

        headlight(CBlocks.HEADLIGHT);
        toggler(CBlocks.TOGGLER);

        CBlocks.COPPER_DOORS.forEach(this::door);
        CBlocks.COPPER_TRAPDOORS.forEach(this::trapdoor);

        var emptyModel = models().withExistingParent("empty", "block/block");
        simpleBlock(CBlocks.SPOT_LIGHT.get(), emptyModel);

        CBlocks.WAXED_COPPER_BRICKS.forEach(block ->
                simpleBlock(block.get(), models().withExistingParent(name(block), modLoc(unwaxedName(block))))
        );

        Stream.of(
                CBlocks.WAXED_COPPER_TILES.stream(),
                CBlocks.WAXED_COPPER_PILLAR.stream()
        ).flatMap(it -> it).forEach(block -> {
            var name = unwaxedName(block);
            axisBlock(block.get(), texture(name), texture(name + "_top"));
        });

        CBlocks.WAXED_COPPER_DOORS.forEach(block ->
                doorBlock(block.get(), texture(unwaxedName(block) + "_bottom"), texture(unwaxedName(block) + "_top"))
        );

        CBlocks.WAXED_COPPER_TRAPDOORS.forEach(block ->
                trapdoorBlock(block.get(), texture(unwaxedName(block)), true)
        );
    }
}
