package galena.coopperative.config;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.stream.Stream;

public class CommonConfig {

    private static final List<Block> overwrittenBlocks = List.of(
            Blocks.REPEATER,
            Blocks.COMPARATOR,
            Blocks.PISTON,
            Blocks.STICKY_PISTON,
            Blocks.DISPENSER,
            Blocks.DROPPER,
            Blocks.OBSERVER,
            Blocks.LEVER,
            Blocks.POWERED_RAIL
    );

    public static Stream<Block> getPossibleOverwrites() {
        return overwrittenBlocks.stream();
    }

    public static Stream<Block> getOverwrittenBlocks() {
        return getPossibleOverwrites().filter(CommonConfig::isOverwriteEnabled);
    }

    public static boolean isOverwriteEnabled(Block block) {
        return true;
    }

}
