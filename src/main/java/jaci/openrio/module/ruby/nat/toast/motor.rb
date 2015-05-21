module Toast

  def self.motor type=:talon, interface=:pwm, channel
    Motor.new type, interface, channel
  end

  class Motor

    # Actually just gives a reference to WPILib's SpeedController child classes, but makes it cleaner in Ruby Implementations
    def self.new type=:talon, interface=:pwm, channel
      pwm = true if interface == :pwm
      can = true if interface == :can

      case type
      when :talon
        if pwm
          return WPI::Talon.new channel
        elsif can
          return WPI::CANTalon.new channel
        end
      when :srx
        if pwm
          return WPI::TalonSRX.new channel
        elsif can
          return WPI::CANTalon.new channel    # For some reason this is how WPILIb handles their SRX :L
        end
      when :jaguar
        if pwm
          return WPI::Jaguar.new channel
        elsif can
          return WPI::CANJaguar.new channel
        end
      when :victor
        return WPI::Victor.new channel if pwm
      when :victorsp
        return WPI::VictorSP.new channel if pwm
      else
        raise "Speed Controller not Supported"
      end

      raise "Invalid Interface"
    end

  end
end
