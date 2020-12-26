package club.moddedminecraft.insanesaneshutdown;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("insane-sane-shutdown")
public class InsaneSaneShutdown
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public InsaneSaneShutdown() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void shutdownHook(final FMLServerStoppingEvent event) {
        new Thread(() -> {
            try {
                Thread.sleep(10000); //10 seconds for everything to stop
                System.out.println("Forcibly stopping server after 10 seconds");
                System.exit(0);
            } catch (InterruptedException ignored) {
            }
            System.exit(0);
        }).start();
    }

}
