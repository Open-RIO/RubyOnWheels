package jaci.openrio.module.ruby;

import jaci.openrio.toast.core.loader.annotation.Priority;
import jaci.openrio.toast.core.shared.ModuleEventBus;
import jaci.openrio.toast.core.shared.ModuleEventListener;
import jaci.openrio.toast.lib.module.ToastModule;

/**
 * A Toast module designed to load Ruby scripts into the classpath and interpret them. This module uses the
 * JRuby API to create a Ruby-Java bridge so that the robot can be programmed in Ruby language
 *
 * @author Jaci
 */
public class RubyOnWheels extends ToastModule implements ModuleEventListener {

    @Override
    public String getModuleName() {
        return "RubyOnWheels";
    }

    @Override
    public String getModuleVersion() {
        return "1.0.0";
    }

    public void onConstruct() {
        ModuleEventBus.registerListener(this);
    }

    @Override
    @Priority(level = Priority.Level.HIGHEST)
    public void prestart() {
        ConfigurationManager.init();
        RubyScriptLoader.init();
    }

    @Override
    public void start() {
    }

    @Override
    public void onModuleEvent(String sender, String event_type, Object... data) {
        // Register with event_type being 'row_rb' and data[0] begin 'your.ruby.package'
        if (event_type.equals("row_rb")) {
            String rb_path = (String) data[0];
            RubyScriptLoader.add(rb_path);
        }
    }
}
