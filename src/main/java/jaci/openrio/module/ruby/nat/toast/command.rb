module Toast
  java_import "jaci.openrio.toast.core.command.CommandBus"
  java_import "jaci.openrio.module.ruby.nat.AbstractCommandProxy"

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
