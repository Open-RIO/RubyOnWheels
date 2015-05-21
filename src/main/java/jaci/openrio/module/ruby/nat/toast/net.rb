module Toast
  java_import "jaci.openrio.toast.core.network.SocketManager"
  java_import "jaci.openrio.delegate.BoundDelegate"
  java_import "jaci.openrio.delegate.Security"

  def self.network delegate_id, &connection_callback
    Net.new delegate_id, &connection_callback
  end

  class Net

    def initialize delegate_id, &callback
      @serv = SocketManager.register delegate_id
      @cb = callback
      @serv.callback BoundDelegate::ConnectionCallback.impl { |method, socket, delegate|
        @cb.call socket, delegate
      }
    end

    def password pass, alg="SHA256"
      @serv.setPassword(pass, Security::HashType.match(alg))
    end

  end
end
