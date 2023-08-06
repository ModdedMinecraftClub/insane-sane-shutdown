package club.moddedminecraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;


@Mod(modid = InsaneSaneShutdown.MODID, name = InsaneSaneShutdown.NAME, version = InsaneSaneShutdown.VERSION, acceptableRemoteVersions = "*")
@EventBusSubscriber
public class InsaneSaneShutdown
{
    public static final String MODID = "insanesaneshutdown";
    public static final String NAME = "InsaneSane Shutdown";
    public static final String VERSION = "1.0";

    private static Logger LOGGER;
    public static Consumer<String> infoLogger;
    public static Consumer<String> warningLogger;
    public static Consumer<String> errorLogger;

    public static MinecraftServer server;

    public static final File outputDir = new File("insanesaneshutdown");

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        infoLogger = LOGGER::info;
        warningLogger = LOGGER::warn;
        errorLogger =  LOGGER::error;
        if (!outputDir.exists()) {
            outputDir.mkdirs(); 
        }
    }



    public InsaneSaneShutdown()
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void onServerStopping(final FMLServerStoppingEvent event) {
        new Thread(() -> {            
            try {
                infoLogger.accept("Shutdown hook received. Waiting 20 seconds...");
                Thread.sleep(20000); //20 seconds for everything to stop
                errorLogger.accept("Server has not shutdown after 20 seconds! Will dump a stacktrace then exit...");
                try {
                    dumpToFile();
                } catch (final IOException e) {
                    errorLogger.accept("Error writing trace!");
                    e.printStackTrace();
                }
                System.exit(0);
            } catch (final InterruptedException ignored) {
            }
            System.exit(0);

        }).start();
    }


    public static void dumpToFile() throws IOException{
        final Date date = new Date();
        final String pattern = "yyyy.MM.dd_hh.mm";
        final File outputFile = new File(outputDir, new SimpleDateFormat(pattern).format(date) + ".txt");
        final FileWriter writer = new FileWriter(outputFile);
        final StringBuilder builder = new StringBuilder();
        final Map<Thread, StackTraceElement[]> a1 = Thread.getAllStackTraces();
        for (final Thread a2 : a1.keySet()){
            builder.append("==========\n\n\n");
            if (a2.isDaemon()) {
                builder.append("Thread is daemon! It will not keep the jvm running, something else is blocking!\n");
                builder.append("==========\n");
            }
            for (final StackTraceElement a3 : a2.getStackTrace()){
                builder.append(a3.toString());
                builder.append("\n");
            }
            builder.append("\n\n==========");
        }

        writer.write(builder.toString());
        writer.close();
    }
    

}
