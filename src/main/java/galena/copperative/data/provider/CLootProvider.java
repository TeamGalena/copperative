package galena.copperative.data.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class CLootProvider extends LootTableProvider {

    protected CLootProvider(DataGenerator gen) {
        super(gen.getPackOutput(), Set.of(), List.of());
    }

    @Override
    protected final void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        // do not validate against all registered loot tables
    }

    private final List<BuiltLootTable> lootTables = new ArrayList<>();

    public List<BuiltLootTable> getLootTables() {
        return lootTables;
    }

    @Override
    public List<LootTableProvider.SubProviderEntry> getTables() {
        registerTables((id, lootBuilder, contextSet) -> lootTables.add(new BuiltLootTable(id, lootBuilder, contextSet)));
        return lootTables.stream().map(table -> {
            Supplier<LootTableSubProvider> supplier = () -> event -> {
                event.accept(table.id, table.lootBuilder);
            };
            return new LootTableProvider.SubProviderEntry(supplier, table.set);
        }).toList();
    }

    protected abstract void registerTables(LootTableGenerationEvent event);

    public record BuiltLootTable(ResourceLocation id, LootTable.Builder lootBuilder, LootContextParamSet set) {
    }

    public interface LootTableGenerationEvent {
        void register(ResourceLocation id, LootTable.Builder lootBuilder, LootContextParamSet contextSet);

        default void register(Block block, LootTable.Builder lootBuilder) {
            var id = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
            register(new ResourceLocation(id.getNamespace(), "blocks/" + id.getPath()), lootBuilder, LootContextParamSets.BLOCK);
        }
    }

    public static LootTable.Builder blockLoot(Block block, UnaryOperator<LootPool.Builder> func) {
        var pool = new LootPool.Builder()
                .add(LootItem.lootTableItem(block))
                .when(ExplosionCondition.survivesExplosion());

        return new LootTable.Builder().withPool(func.apply(pool));
    }

    public static LootTable.Builder blockLoot(Block block) {
        return blockLoot(block, UnaryOperator.identity());
    }

    public static LootTable.Builder blockLootWithName(Block block) {
        return blockLoot(block, it -> it.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));
    }

}
