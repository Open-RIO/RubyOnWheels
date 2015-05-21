require 'java'
require 'toast/command'
require 'toast/net'
require 'toast/log'
require 'toast/toast'

module ::Kernel       # Redirect 'puts' to the logger
  def puts message
    Toast::Log << message
  end
end
