# RubyOnWheels
The Toast Module that allows you to code in Ruby!  

Master | Development  
[![Build status](https://travis-ci.org/Open-RIO/RubyOnWheels.svg?branch=master)](https://travis-ci.org/Open-RIO/RubyOnWheels/)
[![Build status](https://travis-ci.org/Open-RIO/RubyOnWheels.svg?branch=development)](https://travis-ci.org/Open-RIO/RubyOnWheels/)

## An Introduction  
Ruby on Wheels is a Toast Module that allows loading and compiling of Ruby scripts into the classpath, much like the inbuilt Groovy support. This is made possible by the [JRuby](http://github.com/jruby/jruby) API. A simple Ruby file to be loaded by RubyOnWheels is shown below:
```ruby
#TestScript.rb
class Script
  require 'java'
  java_import 'jaci.openrio.toast.lib.log.Logger'
  java_import 'jaci.openrio.toast.core.StateTracker'

  module States
    include_package "jaci.openrio.toast.lib.state"
  end

  include States::StateListener::Transition

  def init
    StateTracker.addTransition(self)
  end

  def transitionState(newState, oldState)
    puts "We are in: #{newState}"
  end
end
Script.new
```

Additionally, other ruby files can be loaded within these scripts like so  
```ruby
#RequireMe.rb
class RequireMe
  def execute
    puts "Require Me!"
  end
end

#Module.rb
class Module
  require 'java'
  require_relative 'RequireMe'

  def init
    req = RequireMe.new
    req.execute
  end
end

#->Require Me!
```

The ``` init() ``` method is called as soon as the Module is loaded in Robot Prestart. All setup should be done here.  

Additionally, Ruby code can be compiled immediately via the CommandBus, similar to the 'Script' command present in the Toast core. This command can be executed as such.  
```ruby
ruby puts "Hello from the Command Bus!"
#->Hello from the Command Bus!
```

For more information on how Ruby implements with Java, take a look at the [JRuby Repository](http://github.com/jruby/jruby)  

## Downloads  
Downloads (stable) can be found on the [releases](http://github.com/Open-RIO/RubyOnWheels/releases), and is similar in deployment to any other Toast module. For more details, refer to the [Toast Repository](http://github.com/Open-RIO/ToastAPI).  

If you wish to build the Module yourself from the SRC, you may do the following:  
- Fork this Repo
- Mirror it to your local machine
- Run ``` gradlew idea ``` or ``` gradlew eclipse ```, depending on your Development Environment.
- Run ``` gradlew deploy ``` to deploy to the RoboRIO
