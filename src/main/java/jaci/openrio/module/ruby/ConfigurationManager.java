package jaci.openrio.module.ruby;

import jaci.openrio.toast.core.loader.groovy.GroovyPreferences;
import jaci.openrio.toast.lib.math.MathHelper;

/**
 * The configuration file manager for RubyOnWheels, this class loads the RubyOnWheels.groovy preferences file and handles
 * registration of the properties.
 *
 * @author Jaci
 */
public class ConfigurationManager {

    static GroovyPreferences pref;

    public static void init() {
        pref = new GroovyPreferences("RubyOnWheels");

        for (Properties prop : Properties.values())
            prop.init(pref);
    }

    public static enum Properties {
        LOAD_FILES("ruby.load.files", new String[] {"main.rb"}, "An Array of all the ruby files to load and instantiate at runtime. These are based from toast/ruby/, and all runtime instantiations should be defined here."),
        LOAD_GEMS("ruby.load.gems", true, "Should we load System Ruby Gems? These gems come from your local Ruby installation and will be added to the load path for you to access. You almost always want this to be true")
        ;

        String key;
        Object defaultValue, value;
        String[] comment;

        Properties(String key, Object defaultValue, String comment) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comment = MathHelper.splitStringByWords(comment, 15);
        }

        public Object init(GroovyPreferences preferences) {
            value = preferences.getObject(key, defaultValue, comment);
            return value;
        }

        public Object get() {
            return value;
        }
    }

}
