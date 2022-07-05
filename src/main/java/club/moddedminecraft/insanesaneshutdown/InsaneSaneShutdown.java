package club.moddedminecraft.insanesaneshutdown;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("insane_sane_shutdown")
public class InsaneSaneShutdown
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public InsaneSaneShutdown()
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


    @SubscribeEvent
    public void shutdownHook(final ServerStoppingEvent event){
        new Thread(() -> {
            try {
                Thread.sleep(120000); //120 seconds for everything to stop
                System.out.println("Forcibly stopping server after 80 seconds");
                System.exit(0);
            } catch (InterruptedException ignored) {
            }
            System.exit(0);
        }).start();
    }
}
