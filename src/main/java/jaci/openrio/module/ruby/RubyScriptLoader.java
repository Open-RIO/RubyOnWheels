package jaci.openrio.module.ruby;

import jaci.openrio.toast.core.Toast;
import jaci.openrio.toast.core.ToastBootstrap;
import jaci.openrio.toast.core.command.CommandBus;
import jaci.openrio.toast.core.io.usb.MassStorageDevice;
import jaci.openrio.toast.core.io.usb.USBMassStorage;
import jaci.openrio.toast.lib.crash.CrashHandler;
import jaci.openrio.toast.lib.log.Logger;
import org.jruby.Ruby;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.load.LoadService;

import java.io.File;
import java.util.ArrayList;

/**
 * The Ruby Script loader. Ruby scripts are loaded and instantiated by this class. The classes that are loaded
 * are defined in the RubyOnWheels.groovy configuration file
 *
 * @author Jaci
 */
public class RubyScriptLoader {

    static File rootDir;

    static ScriptingContainer container;
    static Logger logger;

    static ArrayList<String> loadQueue = new ArrayList<>();

    public static void init() {
        container = new ScriptingContainer();

        rootDir = new File(ToastBootstrap.toastHome, "ruby");
        rootDir.mkdirs();

        logger = new Logger("Ruby", Logger.ATTR_DEFAULT);

        Ruby rb = container.getProvider().getRuntime();
        LoadService ls = rb.getLoadService();
        ls.addPaths("uri:classloader:/jaci/openrio/module/ruby/nat");
        for (String s : loadQueue)
            ls.addPaths(s);
        ls.autoloadRequire("toast");

        CommandBus.registerCommand(new RubyScriptCommand());

        if (!USBMassStorage.overridingModules()) {
            ls.addPaths(rootDir.toURI().toString());
        }

        for (MassStorageDevice device : USBMassStorage.connectedDevices) {
            if (device.concurrent_modules || device.override_modules) {
                File ruby = new File(device.drivePath, "ruby");
                ruby.mkdirs();
                ls.addPaths(ruby.toURI().toString());
            }
        }

        container.put("LOG_", logger);
        container.put("TOAST_", Toast.getToast());
        ArrayList<String> filenames = (ArrayList<String>) ConfigurationManager.Properties.LOAD_FILES.get();
        for (String s : filenames) {
            try {
                container.runScriptlet("load '" + s + "'");
                logger.info("Ruby File Loaded: " + s);
            } catch (Exception e) {
                logger.error("Could not load Ruby File: " + s);
                CrashHandler.handle(e);
            }
        }
    }

    public static void add(String path) {
        String cl = "uri:classloader:/" + path.replace(".", "/");
        if (container != null) {
            container.getProvider().getRuntime().getLoadService().addPaths(cl);
        } else {
            loadQueue.add(cl);
        }
    }

    public static void addRaw(String path) {
        if (container != null) {
            container.getProvider().getRuntime().getLoadService().addPaths(path);
        } else {
            loadQueue.add(path);
        }
    }

    public static void addGems(String path) {
        if ((boolean)ConfigurationManager.Properties.LOAD_GEMS.get()) {
            logger.info("Loading system Gem Dir: " + path);
            File file = new File(path);
            File[] gems = file.listFiles();
            if (gems != null)
                for (File gemDir : gems) {
                    if (container != null) {
                        container.getProvider().getRuntime().getLoadService().addPaths(new File(gemDir, "lib").toURI().toString());
                    } else {
                        loadQueue.add(new File(gemDir, "lib").toURI().toString());
                    }
                }
        }
    }

}
