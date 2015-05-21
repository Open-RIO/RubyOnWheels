module Toast
  java_import "jaci.openrio.toast.core.command.CommandBus"
  java_import "jaci.openrio.module.ruby.util.AbstractCommandProxy"

  def self.command name, &block
    Command.new name, &block
  end

  class Command

    def initialize name, &block
      @prox = AbstractCommandProxy.new name
      @prox.set AbstractCommandProxy::CallCMD.impl { |method, args|
        block.call args.to_a
      }
      CommandBus.registerCommand @prox
    end

  end
end
