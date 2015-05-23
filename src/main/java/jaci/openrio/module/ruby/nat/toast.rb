require 'java'
require 'toast/gem'
require 'toast/command'
require 'toast/net'
require 'toast/log'
require 'toast/toast'
require 'toast/motor'

module ::Kernel       # Redirect 'puts' to the logger
  def puts message
    Toast::Log << message.to_s
  end
end
