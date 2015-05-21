# RubyOnWheels
The Toast Module that allows you to code in Ruby!  

Travis CI Build Status:  
[![Build status](http://dev.imjac.in/travisalt/Open-RIO/RubyOnWheels)](https://travis-ci.org/Open-RIO/RubyOnWheels/)
## An Introduction  
Ruby on Wheels is a Toast Module that allows loading and compiling of Ruby scripts into the classpath, much like the inbuilt Groovy support. This is made possible by the [JRuby](http://github.com/jruby/jruby) API. A simple Ruby file to be loaded by RubyOnWheels is shown below:
```ruby
#TestScript.rb
puts "Hello World!"                        # Prints to Toast's logger
motor = Toast::Motor.new :talon, :can, 1   # Creates a new Talon on the CAN Bus with ID 1
motor.set 0.4                              # Set the motor speed to 0.4 (40%)
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
  require 'RequireMe'

  req = RequireMe.new
  req.execute
end

#->Require Me!
```

Additionally, Ruby code can be compiled immediately via the CommandBus, similar to the 'Script' command present in the Toast core. This command can be executed as such.  
```ruby
ruby puts "Hello from the Command Bus!"
#->Hello from the Command Bus!
```

Here is an example of many of RubyOnWheels' most notable functions. For more details, look in the repository to see the Ruby SRC
```ruby
Toast::Log << "Ruby Program Starting!"                        # These 2 methods do the same thing
puts "Ruby Program also Starting!"                            # They write to Toast's Logger

Toast::Log.puts :error, "Oh no!"                              # Writes an error to Toast's Logger

puts "Am I connected to FMS? #{Toast.FMS?}"                   # We include hooks for FMS, Simulation, whatever
puts "Am I in a simulated environment? #{Toast.simulation?}"

motor_1 = Toast::Motor.new 1        # Most subclasses have
motor_2 = Toast.motor 2             # a method shortcut to the constructor
motor_1.set 0.5
motor_2.set -0.5

motor_3 = Toast::Motor.new :jaguar, :pwm, 3    # Define your motor type and interface

drive = Toast::Drive.new 5, 6, 7, 8            # RobotDrive is implemented as well
drive.tank 0.75, 0.75

drive.mecanum 0.8, 0.4, 0.2                    # Mecanum drive was never easier
drive.polar 1, 20, 0.4

Toast.go {                                     # Need a new thread? Got ya fam
  puts "Hello World!"
}

Toast.tick :autonomous do                      # Equivilant of autonomousPeriodic
  # ...
end

Toast.tick {                                        # Periodic for all modes (disabled, autonomous, teleop, test)
  # ...
}

Toast.transition :teleop do |state, oldState|       # Need to check when we switch modes? No prob
  puts "I'm now in #{state} (I was in #{oldState})"
end

Toast.command "test_command_pls_ignore" do |args|   # Easy access to the CommandBus
  puts "Test: #{args}"
end

java_import "some.non.toast.package.MyClass"   # Did we miss a hook? No problem, it's easy to call it yourself.
MyClass.my_method

```

For more information on how Ruby implements with Java, take a look at the [JRuby Repository](http://github.com/jruby/jruby)  

## Downloads  
Downloads (stable) can be found on the [releases](http://github.com/Open-RIO/RubyOnWheels/releases), and is similar in deployment to any other Toast module. For more details, refer to the [Toast Repository](http://github.com/Open-RIO/ToastAPI).  

If you wish to build the Module yourself from the SRC, you may do the following:  
- Fork this Repo
- Mirror it to your local machine
- Run ``` gradlew idea ``` or ``` gradlew eclipse ```, depending on your Development Environment.
- Run ``` gradlew deploy ``` to deploy to the RoboRIO
