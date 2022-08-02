package galena.coopperative.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static galena.coopperative.content.index.CBlocks.*;

public class CoopperativeClient {

    private static void render(Supplier<? extends Block> block, RenderType render) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), render);
    }

    public static void registerBlockRenderers() {
        RenderType cutout = RenderType.cutout();
        RenderType mipped = RenderType.cutoutMipped();
        RenderType translucent = RenderType.translucent();

        render(COPPER_DOOR, cutout);
        render(EXPOSED_COPPER_DOOR, cutout);
        render(WEATHERED_COPPER_DOOR, cutout);
        render(OXIDIZED_COPPER_DOOR, cutout);

        render(COPPER_TRAPDOOR, cutout);
        render(EXPOSED_COPPER_TRAPDOOR, cutout);
        render(WEATHERED_COPPER_TRAPDOOR, cutout);
        render(OXIDIZED_COPPER_TRAPDOOR, cutout);
    }
}
