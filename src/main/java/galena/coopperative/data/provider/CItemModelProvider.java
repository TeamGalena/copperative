package galena.coopperative.data.provider;

import com.google.common.base.Preconditions;
import galena.coopperative.Coopperative;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
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

    protected String unwaxedBlockName(Supplier<? extends Block> block) {
        return blockName(block).substring(6);
    }

    protected ResourceLocation blockTexture(String name) {
        return modLoc("block/" + name);
    }

    protected ResourceLocation itemTexture(String name) {
        return modLoc("item/" + name);
    }

    public ItemModelBuilder block(Supplier<? extends Block> block) {
        return block(block, blockName(block));
    }

    public <B extends Block> List<ItemModelBuilder> weatheringBlock(List<RegistryObject<B>> blockArrayList) {
        ArrayList<ItemModelBuilder> itemArrayList = new ArrayList<>(blockArrayList.size());
        for (Supplier<? extends B> blocks : blockArrayList)
            itemArrayList.add(block(blocks));
        return itemArrayList;
    }

    public <B extends Block> List<ItemModelBuilder> weathingBlockWithItem(List<RegistryObject<B>> blockArrayList) {
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
                .texture("layer0", blockTexture(name));
    }

    public ItemModelBuilder normalItem(Supplier<? extends ItemLike> itemLike) {
        var item = itemLike.get().asItem();
        var itemId = ForgeRegistries.ITEMS.getKey(item).getPath();
        return withExistingParent(itemId, mcLoc("item/generated"))
                .texture("layer0", itemTexture(itemId));
    }

    public ItemModelBuilder blockWithItem(Supplier<? extends Block> block, String texture) {
        return withExistingParent(blockName(block), mcLoc("item/generated"))
                .texture("layer0", itemTexture(texture));
    }

    public ItemModelBuilder blockWithItem(Supplier<? extends Block> block) {
        return blockWithItem(block, blockName(block));
    }

    public ItemModelBuilder blockInventorySpecific(Supplier<? extends Block> block) {
        return withExistingParent(blockName(block), modLoc("block/" + blockName(block) + "_inventory"));
    }

    public ItemModelBuilder piston(Supplier<? extends Block> piston) {
        ResourceLocation bottom = blockTexture(CBlockStateProvider.weatheringPrefix(piston) + "dispenser_top");
        ResourceLocation side = blockTexture(blockName(piston).replace("sticky_", "") + "_side");
        return withExistingParent(blockName(piston), new ResourceLocation("block/" + (blockName(piston).contains("sticky") ? "sticky_" : "") + "piston_inventory"))
                .texture("bottom", bottom)
                .texture("side", side);
    }

    public ItemModelBuilder trapDoor(Supplier<? extends Block> block, String texture) {
        return block(block, texture + "_bottom");
    }

    public ItemModelBuilder trapDoor(Supplier<? extends Block> block) {
        return trapDoor(block, blockName(block));
    }

    public ItemModelBuilder compatBlock(Block block, String namespace, String name) {
        var id = Preconditions.checkNotNull(ForgeRegistries.BLOCKS.getKey(block));
        return withExistingParent(id.getPath(), new ResourceLocation(Coopperative.MOD_ID, "block/compat/" + namespace + "/" + name));
    }

    public String weatherPrefix(Block block) {
        if (block instanceof WeatheringCopper it) {
            var age = it.getAge();
            if (age != WeatheringCopper.WeatherState.UNAFFECTED) return age.name().toLowerCase() + "_";
        }
        return "";
    }
}
