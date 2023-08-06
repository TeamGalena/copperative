package galena.copperative.data;

import galena.copperative.content.event.CommonEvents;
import galena.copperative.data.provider.CLootProvider;
import galena.copperative.index.CItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static galena.copperative.index.CBlocks.*;

public class CLoot extends CLootProvider {

    public CLoot(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void registerTables(LootTableGenerationEvent event) {
        Stream<Supplier<? extends Block>> defaultBlocks = Stream.of(
                        COPPER_TILES.stream(),
                        COPPER_BRICKS.stream(),
                        COPPER_PILLAR.stream(),
                        WAXED_COPPER_TILES.stream(),
                        WAXED_COPPER_BRICKS.stream(),
                        WAXED_COPPER_PILLAR.stream(),
                        HEADLIGHT.stream(),
                        TOGGLER.stream(),
                        COPPER_TRAPDOORS.stream(),
                        WAXED_COPPER_TRAPDOORS.stream(),

                        EXPOSERS.weathered(),
                        RELAYERS.weathered(),
                        CRANKS.weathered(),
                        COG_BLOCKS.weathered(),
                        RANDOMIZERS.weathered(),

                        REPEATERS.weathered(),
                        COMPARATORS.weathered(),
                        PISTONS.weathered(),
                        STICKY_PISTONS.weathered(),
                        OBSERVERS.weathered(),
                        LEVERS.weathered(),
                        POWERED_RAILS.weathered(),

                        Stream.of(PATINA_BLOCK)
                )
                .flatMap(Function.identity());

        defaultBlocks
                .map(Supplier::get)
                .forEach(block -> event.register(block, blockLoot(block)));

        Stream.of(DROPPERS, DISPENSERS).flatMap(CopperSet::weathered)
                .map(Supplier::get)
                .forEach(block -> event.register(block, blockLootWithName(block)));

        Stream.of(COPPER_DOORS.stream(), WAXED_COPPER_DOORS.stream())
                .flatMap(it -> it)
                .map(RegistryObject::get)
                .forEach(block -> event.register(block, blockLoot(block, it ->
                        it.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                                )
                        ))
                ));

        event.register(CommonEvents.PATINA_LOOT_TABLE, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(CItems.PATINA.get())
                        .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.25F, 0.5F, 0.75F, 1.0F))
                )
        ), LootContextParamSets.BLOCK);
    }

}
