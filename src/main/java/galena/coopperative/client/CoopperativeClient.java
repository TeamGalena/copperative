package galena.coopperative.client;

import net.mehvahdjukaar.supplementaries.client.renderers.color.CogBlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static galena.coopperative.index.CBlocks.COG_BLOCKS;
import static galena.coopperative.index.CBlocks.COPPER_DOORS;
import static galena.coopperative.index.CBlocks.COPPER_TRAPDOORS;
import static galena.coopperative.index.CBlocks.EXPOSED_COMPARATOR;
import static galena.coopperative.index.CBlocks.EXPOSED_POWERED_RAIL;
import static galena.coopperative.index.CBlocks.EXPOSED_REPEATER;
import static galena.coopperative.index.CBlocks.OXIDIZED_COMPARATOR;
import static galena.coopperative.index.CBlocks.OXIDIZED_POWERED_RAIL;
import static galena.coopperative.index.CBlocks.OXIDIZED_REPEATER;
import static galena.coopperative.index.CBlocks.RANDOMIZERS;
import static galena.coopperative.index.CBlocks.TOGGLER;
import static galena.coopperative.index.CBlocks.WAXED_COPPER_DOORS;
import static galena.coopperative.index.CBlocks.WAXED_COPPER_TRAPDOORS;
import static galena.coopperative.index.CBlocks.WEATHERED_COMPARATOR;
import static galena.coopperative.index.CBlocks.WEATHERED_POWERED_RAIL;
import static galena.coopperative.index.CBlocks.WEATHERED_REPEATER;

public class CoopperativeClient {

    private static void render(Supplier<? extends Block> block, RenderType render) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), render);
    }

    private static void render(List<RegistryObject<Block>> blocks, RenderType render) {
        for (Supplier<? extends Block> block : blocks) {
            render(block, render);
        }
    }

    public static void registerBlockRenderers() {
        RenderType cutout = RenderType.cutout();

        render(EXPOSED_REPEATER, cutout);
        render(WEATHERED_REPEATER, cutout);
        render(OXIDIZED_REPEATER, cutout);

        render(EXPOSED_COMPARATOR, cutout);
        render(WEATHERED_COMPARATOR, cutout);
        render(OXIDIZED_COMPARATOR, cutout);

        render(EXPOSED_POWERED_RAIL, cutout);
        render(WEATHERED_POWERED_RAIL, cutout);
        render(OXIDIZED_POWERED_RAIL, cutout);

        render(TOGGLER, cutout);

        COPPER_DOORS.forEach(it -> render(it, cutout));
        COPPER_TRAPDOORS.forEach(it -> render(it, cutout));
        WAXED_COPPER_DOORS.forEach(it -> render(it, cutout));
        WAXED_COPPER_TRAPDOORS.forEach(it -> render(it, cutout));

        RANDOMIZERS.weathered().forEach(it -> render(it, cutout));
        COG_BLOCKS.weathered().forEach(it -> render(it, cutout));
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        if (ModList.get().isLoaded("supplementaries")) {
            COG_BLOCKS.weathered().forEach(it ->
                    event.register(new CogBlockColor(), it.get())
            );
        }
    }

    public static void registerDynamicResources() {
        DynamicCooperativeResourcePack.INSTANCE.register();
    }
}
