package galena.coopperative.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static galena.coopperative.index.CBlocks.*;

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
    }

    public static void registerDynamicResources() {
        DynamicCooperativeResourcePack.INSTANCE.register();
    }
}
