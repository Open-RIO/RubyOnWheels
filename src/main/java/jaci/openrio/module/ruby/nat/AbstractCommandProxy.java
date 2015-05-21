package jaci.openrio.module.ruby.nat;

import jaci.openrio.toast.core.command.AbstractCommand;

//Doing this here makes it cleaner in Ruby
public class AbstractCommandProxy extends AbstractCommand {

    String nm;
    CallCMD call;
    public AbstractCommandProxy(String name) {
        nm = name;
    }

    public void set(CallCMD call) {
        this.call = call;
    }

    @Override
    public String getCommandName() {
        return nm;
    }

    @Override
    public void invokeCommand(int argLength, String[] args, String command) {
        call.call(args);
    }

    public static interface CallCMD {
        public void call(String[] args);
    }
}
