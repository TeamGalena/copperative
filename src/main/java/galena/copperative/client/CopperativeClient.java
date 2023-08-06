package galena.copperative.client;

import net.mehvahdjukaar.supplementaries.client.renderers.color.CogBlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static galena.copperative.index.CBlocks.COG_BLOCKS;
import static galena.copperative.index.CBlocks.COMPARATORS;
import static galena.copperative.index.CBlocks.COPPER_DOORS;
import static galena.copperative.index.CBlocks.COPPER_TRAPDOORS;
import static galena.copperative.index.CBlocks.POWERED_RAILS;
import static galena.copperative.index.CBlocks.RANDOMIZERS;
import static galena.copperative.index.CBlocks.REPEATERS;
import static galena.copperative.index.CBlocks.TOGGLER;
import static galena.copperative.index.CBlocks.WAXED_COPPER_DOORS;
import static galena.copperative.index.CBlocks.WAXED_COPPER_TRAPDOORS;

public class CopperativeClient {

    private static void render(Supplier<? extends Block> block, RenderType render) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), render);
    }

    private static void render(List<RegistryObject<Block>> blocks, RenderType render) {
        for (Supplier<? extends Block> block : blocks) {
            render(block, render);
        }
    }

    private static void registerBlockRenderers() {
        RenderType cutout = RenderType.cutout();

        REPEATERS.weathered().forEach(it -> render(it, cutout));
        COMPARATORS.weathered().forEach(it -> render(it, cutout));
        POWERED_RAILS.weathered().forEach(it -> render(it, cutout));

        render(TOGGLER, cutout);

        COPPER_DOORS.forEach(it -> render(it, cutout));
        COPPER_TRAPDOORS.forEach(it -> render(it, cutout));
        WAXED_COPPER_DOORS.forEach(it -> render(it, cutout));
        WAXED_COPPER_TRAPDOORS.forEach(it -> render(it, cutout));

        RANDOMIZERS.weathered().forEach(it -> render(it, cutout));
        COG_BLOCKS.weathered().forEach(it -> render(it, cutout));
    }

    private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        if (ModList.get().isLoaded("supplementaries")) {
            COG_BLOCKS.weathered().forEach(it ->
                    event.register(new CogBlockColor(), it.get())
            );
        }
    }

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DynamicCopperativeResourcePack.INSTANCE.register();
        modEventBus.addListener(CopperativeClient::registerBlockColors);
        modEventBus.addListener(CopperativeClient::setup);
    }

    private static void setup(FMLClientSetupEvent event) {
        CopperativeClient.registerBlockRenderers();
    }
}
