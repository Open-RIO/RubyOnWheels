package jaci.openrio.module.ruby;

import jaci.openrio.toast.core.command.AbstractCommand;
import jaci.openrio.toast.core.command.IHelpable;

/**
 * A command used for on-the-fly Ruby Scripting
 *
 * @author Jaci
 */
public class RubyScriptCommand extends AbstractCommand implements IHelpable {
    @Override
    public String getCommandName() {
        return "ruby";
    }

    @Override
    public String getHelp() {
        return "Run a given Ruby Script instantly";
    }

    @Override
    public void invokeCommand(int argLength, String[] args, String full) {
        String s = "";
        for (String s1 : args)
            s += s1 + " ";

        RubyScriptLoader.container.runScriptlet(s);
    }
}
