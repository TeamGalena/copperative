package galena.coopperative.data;

import galena.coopperative.Coopperative;
import galena.coopperative.data.provider.CBlockStateProvider;
import galena.coopperative.index.CBlocks;
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
        return Coopperative.MOD_NAME + " Block States";
    }

    private static String unwaxedName(Supplier<? extends Block> block) {
        return name(block).substring(6);
    }

    @Override
    protected void registerStatesAndModels() {
        block(CBlocks.PATINA_BLOCK);
        weatheringBlock(CBlocks.COPPER_BRICKS);
        weatheringPillarBlock(CBlocks.COPPER_PILLAR);
        weatheringPillarBlock(CBlocks.COPPER_TILES);

        repeater(CBlocks.EXPOSED_REPEATER);
        repeater(CBlocks.WEATHERED_REPEATER);
        repeater(CBlocks.OXIDIZED_REPEATER);

        comparator(CBlocks.EXPOSED_COMPARATOR);
        comparator(CBlocks.WEATHERED_COMPARATOR);
        comparator(CBlocks.OXIDIZED_COMPARATOR);

        piston(CBlocks.EXPOSED_PISTON);
        piston(CBlocks.WEATHERED_PISTON);
        piston(CBlocks.OXIDIZED_PISTON);

        piston(CBlocks.EXPOSED_STICKY_PISTON);
        piston(CBlocks.WEATHERED_STICKY_PISTON);
        piston(CBlocks.OXIDIZED_STICKY_PISTON);

        dispenser(CBlocks.EXPOSED_DISPENSER);
        dispenser(CBlocks.WEATHERED_DISPENSER);
        dispenser(CBlocks.OXIDIZED_DISPENSER);

        dropper(CBlocks.EXPOSED_DROPPER);
        dropper(CBlocks.WEATHERED_DROPPER);
        dropper(CBlocks.OXIDIZED_DROPPER);

        observer(CBlocks.EXPOSED_OBSERVER);
        observer(CBlocks.WEATHERED_OBSERVER);
        observer(CBlocks.OXIDIZED_OBSERVER);

        lever(CBlocks.EXPOSED_LEVER);
        lever(CBlocks.WEATHERED_LEVER);
        lever(CBlocks.OXIDIZED_LEVER);

        poweredRail(CBlocks.EXPOSED_POWERED_RAIL);
        poweredRail(CBlocks.WEATHERED_POWERED_RAIL);
        poweredRail(CBlocks.OXIDIZED_POWERED_RAIL);

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
            var name =  unwaxedName(block);
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
