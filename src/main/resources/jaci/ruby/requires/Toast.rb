require 'java'

def registerCommand
    java_import 'jaci.openrio.toast.core.command.CommandBus'
    CommandBus.registerCommand(self)
end