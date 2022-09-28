package galena.coopperative.data.provider;

import galena.coopperative.Coopperative;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperFullBlock;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public abstract class CItemModelProvider extends ItemModelProvider {

    public CItemModelProvider(DataGenerator gen, ExistingFileHelper help) {
        super(gen, Coopperative.MOD_ID, help);
    }
    public CItemModelProvider(DataGenerator gen, String target, ExistingFileHelper help) {
        super(gen, target, help);
    }

    protected String blockName(Supplier<? extends Block> block) {
        return ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
    }

    private ResourceLocation texture(String name) {
        return modLoc("block/" + name);
    }

    public ItemModelBuilder block(Supplier<? extends Block> block) {
        return block(block, blockName(block));
    }

    public <B extends Block> ArrayList<ItemModelBuilder> weatheringBlock(ArrayList<RegistryObject<B>> blockArrayList) {
        ArrayList<ItemModelBuilder> itemArrayList = new ArrayList<>(blockArrayList.size());
        for (Supplier<? extends B> blocks : blockArrayList)
            itemArrayList.add(block(blocks));
        return itemArrayList;
    }

    public <B extends Block> ArrayList<ItemModelBuilder> weathingBlockWithItem(ArrayList<RegistryObject<B>> blockArrayList) {
        ArrayList<ItemModelBuilder> itemArrayList = new ArrayList<>(blockArrayList.size());
        for (Supplier<? extends B> blocks : blockArrayList)
            itemArrayList.add(blockWithItem(blocks));
        return itemArrayList;
    }

    public ItemModelBuilder block(Supplier<? extends Block> block, String name) {
        return withExistingParent(blockName(block), modLoc("block/" + name));
    }



    public ItemModelBuilder block(Supplier<? extends Block> waxed, Supplier<? extends Block> block) {
        String name = blockName(block);
        return block(waxed, name);
    }

    public ItemModelBuilder blockFlat(Supplier<? extends Block> block) {
        return blockFlat(block, blockName(block));
    }
    public ItemModelBuilder blockFlat(Supplier<? extends Block> block, Supplier<? extends Block> fullBlock) {
        return blockFlat(block, blockName(fullBlock));
    }

    public ItemModelBuilder blockFlat(Supplier<? extends Block> block, String name) {
        return withExistingParent(blockName(block), mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + name));
    }

    public ItemModelBuilder blockFlatWithItemName(Supplier<? extends Block> block, String name) {
        return withExistingParent(blockName(block), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + name));
    }

    public ItemModelBuilder normalItem(Supplier<? extends Item> item) {
        return withExistingParent(ForgeRegistries.ITEMS.getKey(item.get()).getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + ForgeRegistries.ITEMS.getKey(item.get()).getPath()));
    }

    public ItemModelBuilder blockWithItem(Supplier<? extends Block> block) {
        return withExistingParent(blockName(block), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + blockName(block)));
    }

    public ItemModelBuilder blockInventorySpecific(Supplier<? extends Block> block) {
        return withExistingParent(blockName(block), modLoc("block/" + blockName(block) + "_inventory"));
    }

    public ItemModelBuilder piston(Supplier<? extends Block> piston) {
        ResourceLocation bottom = texture(CBlockStateProvider.weatheringPrefix(piston) + "dispenser_top");
        ResourceLocation side = texture(blockName(piston).replace("sticky_", "") + "_side");
        return withExistingParent(blockName(piston), new ResourceLocation("block/" + (blockName(piston).contains("sticky") ? "sticky_" : "") + "piston_inventory"))
                .texture("bottom", bottom)
                .texture("side", side);
    }

    public ItemModelBuilder trapDoor(Supplier<? extends Block> block) {
        return block(block, blockName(block) + "_bottom");
    }
}
