package jaci.openrio.toastextension.ruby;

import jaci.openrio.toast.core.ToastBootstrap;
import jaci.openrio.toast.core.command.CommandBus;
import jaci.openrio.toast.lib.log.Logger;
import org.jruby.RubyObject;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import java.io.File;
import java.io.FilenameFilter;
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
        CommandBus.registerCommand(new RubyScriptCommand());
        crawl(rootDir);
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
