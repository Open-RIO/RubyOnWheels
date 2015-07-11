package jaci.openrio.module.ruby;

import jaci.openrio.toast.lib.math.MathHelper;
import jaci.openrio.toast.lib.module.ModuleConfig;

/**
 * The configuration file manager for RubyOnWheels, this class loads the RubyOnWheels.conf preferences file and handles
 * registration of the properties.
 *
 * @author Jaci
 */
public class ConfigurationManager {

    static ModuleConfig pref;

    public static void init() {
        pref = new ModuleConfig("RubyOnWheels");

        for (Properties prop : Properties.values())
            prop.init(pref);
    }

    public static enum Properties {
        LOAD_FILES("ruby.load.files", new String[] {"main.rb"}),
        ;

        String key;
        Object defaultValue, value;

        Properties(String key, Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public Object init(ModuleConfig preferences) {
            value = preferences.get(key, defaultValue);
            return value;
        }

        public Object get() {
            return value;
        }
    }

}
