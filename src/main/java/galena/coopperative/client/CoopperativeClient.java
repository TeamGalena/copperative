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
        RenderType mipped = RenderType.cutoutMipped();
        RenderType translucent = RenderType.translucent();

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
