package jaci.openrio.toastextension.ruby;

import jaci.openrio.toast.core.ToastBootstrap;
import jaci.openrio.toast.core.command.CommandBus;
import jaci.openrio.toast.core.io.usb.MassStorageDevice;
import jaci.openrio.toast.core.io.usb.USBMassStorage;
import jaci.openrio.toast.lib.log.Logger;
import org.jruby.*;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.jruby.javasupport.proxy.InternalJavaProxy;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.load.LoadService;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The Ruby Script loader. Ruby scripts are loaded and instantiated by this class
 *
 * @author Jaci
 */
public class RubyScriptLoader {

    static File rootDir;

    static ScriptingContainer container;
    public static HashMap<File, RubyObject> gems;

    static Logger logger;

    public static void init() {
        container = new ScriptingContainer();
        gems = new HashMap<>();

        rootDir = new File(ToastBootstrap.toastHome, "ruby");
        rootDir.mkdirs();

        logger = new Logger("RubyOnWheels", Logger.ATTR_DEFAULT);

        Ruby rb = container.getProvider().getRuntime();
        LoadService ls = rb.getLoadService();
        ls.addPaths("uri:classloader:/jaci/ruby/requires");
        ls.autoloadRequire("Load");

        CommandBus.registerCommand(new RubyScriptCommand());

        if (!USBMassStorage.overridingModules())
            crawl(rootDir);

        for (MassStorageDevice device : USBMassStorage.connectedDevices) {
            if (device.concurrent_modules || device.override_modules) {
                File ruby = new File(device.drivePath, "ruby");
                ruby.mkdirs();
                crawl(ruby);
            }
        }
    }

    public static void crawl(File dir) {
        File[] ruby = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".rb");
            }
        });

        File[] subDirectories = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });

        for (File rb : ruby)
            loadRuby(rb);
        for (File sub : subDirectories)
            crawl(sub);
    }

    public static void loadRuby(File file) {
        Object rb = container.runScriptlet(PathType.ABSOLUTE, file.getAbsolutePath());
        if (rb instanceof InternalJavaProxy) {
            InternalJavaProxy proxy = (InternalJavaProxy) rb;
            rb = proxy.___getInvocationHandler().getOrig();
        }
        if (rb instanceof RubyObject) {
            RubyObject gem = (RubyObject) rb;
            gems.put(file, gem);

            logger.info("Ruby File Loaded: " + file.getName());

            try {
                gem.callMethod("init");
            } catch (Exception e) { }
        }

    }

}
