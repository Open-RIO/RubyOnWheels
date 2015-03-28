package jaci.openrio.toastextension.ruby;

import jaci.openrio.toast.core.command.AbstractCommand;

/**
 * A command used for on-the-fly Ruby Scripting
 *
 * @author Jaci
 */
public class RubyScriptCommand extends AbstractCommand {
    @Override
    public String getCommandName() {
        return "ruby";
    }

    @Override
    public void invokeCommand(int argLength, String[] args, String full) {
        String s = "";
        for (String s1 : args)
            s += s1 + " ";

        RubyScriptLoader.container.runScriptlet(s);
    }
}
