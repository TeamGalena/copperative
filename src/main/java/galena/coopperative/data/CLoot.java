package galena.coopperative.data;

import galena.coopperative.data.provider.CLootProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.stream.Stream;

import static galena.coopperative.index.CBlocks.*;

public class CLoot extends CLootProvider {

    public CLoot(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void registerTables(LootTableGenerationEvent event) {
        Stream<RegistryObject<? extends Block>> defaultBlocks = Stream.of(
                        COPPER_TILES.stream(),
                        COPPER_BRICKS.stream(),
                        COPPER_PILLAR.stream(),
                        HEADLIGHT.stream(),
                        TOGGLER.stream(),
                        Stream.of(
                                PATINA_BLOCK,

                                EXPOSED_REPEATER,
                                OXIDIZED_REPEATER,
                                WEATHERED_REPEATER,

                                EXPOSED_COMPARATOR,
                                OXIDIZED_COMPARATOR,
                                WEATHERED_COMPARATOR,

                                EXPOSED_PISTON,
                                OXIDIZED_PISTON,
                                WEATHERED_PISTON,

                                EXPOSED_STICKY_PISTON,
                                OXIDIZED_STICKY_PISTON,
                                WEATHERED_STICKY_PISTON,

                                EXPOSED_OBSERVER,
                                OXIDIZED_OBSERVER,
                                WEATHERED_OBSERVER,

                                EXPOSED_LEVER,
                                OXIDIZED_LEVER,
                                WEATHERED_LEVER,

                                EXPOSED_COPPER_TRAPDOOR,
                                OXIDIZED_COPPER_TRAPDOOR,
                                WEATHERED_COPPER_TRAPDOOR,

                                EXPOSED_POWERED_RAIL,
                                OXIDIZED_POWERED_RAIL,
                                WEATHERED_POWERED_RAIL
                        )
                )
                .flatMap(Function.identity());

        defaultBlocks
                .map(RegistryObject::get)
                .forEach(block -> event.register(block, blockLoot(block)));

        Stream.of(
                        EXPOSED_DROPPER,
                        OXIDIZED_DROPPER,
                        WEATHERED_DROPPER,

                        EXPOSED_DISPENSER,
                        OXIDIZED_DISPENSER,
                        WEATHERED_DISPENSER
                )
                .map(RegistryObject::get)
                .forEach(block -> event.register(block, blockLootWithName(block)));

        Stream.of(EXPOSED_COPPER_DOOR, OXIDIZED_COPPER_DOOR, WEATHERED_COPPER_DOOR)
                .map(RegistryObject::get)
                .forEach(block -> event.register(block, blockLoot(block, it ->
                        it.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                                )
                        ))
                ));
    }

}
