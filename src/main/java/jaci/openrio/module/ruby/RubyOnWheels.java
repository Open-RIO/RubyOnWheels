package jaci.openrio.module.ruby;

import jaci.openrio.toast.core.loader.annotation.Priority;
import jaci.openrio.toast.lib.module.ToastModule;

/**
 * A Toast module designed to load Ruby scripts into the classpath and interpret them. This module uses the
 * JRuby API to create a Ruby-Java bridge so that the robot can be programmed in Ruby language
 *
 * @author Jaci
 */
public class RubyOnWheels extends ToastModule {

    @Override
    public String getModuleName() {
        return "RubyOnWheels";
    }

    @Override
    public String getModuleVersion() {
        return "1.0.0";
    }

    @Override
    @Priority(level = Priority.Level.LOWEST)
    public void prestart() {
        ConfigurationManager.init();
        RubyScriptLoader.init();
    }

    @Override
    public void start() {
    }

}