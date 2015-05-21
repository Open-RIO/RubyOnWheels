module Toast

  def self.motor type=:talon, interface=:pwm, channel
    Motor.new type, interface, channel
  end

  def self.drive c1, c2, c3=nil, c4=nil
    Drive.new c1, c2, c3, c4
  end

  class Motor

    # Actually just gives a reference to WPILib's SpeedController child classes, but makes it cleaner in Ruby Implementations
    def self.new type=:talon, interface=:pwm, channel
      pwm = true if interface == :pwm
      can = true if interface == :can

      case type
      when :talon
        if pwm
          @sc = WPI::Talon.new channel
        elsif can
          @sc = WPI::CANTalon.new channel
        end
      when :srx
        if pwm
          @sc = WPI::TalonSRX.new channel
        elsif can
          @sc = WPI::CANTalon.new channel    # For some reason this is how WPILIb handles their SRX :L
        end
      when :jaguar
        if pwm
          @sc = WPI::Jaguar.new channel
        elsif can
          @sc = WPI::CANJaguar.new channel
        end
      when :victor
        @sc = WPI::Victor.new channel if pwm
      when :victorsp
        @sc = WPI::VictorSP.new channel if pwm
      else
        raise "Speed Controller not Supported"
      end

      if @sc != nil
        return @sc
      end

      raise "Invalid Interface"
    end
  end

  module Java::EduWpiFirstWpilibj::SpeedController
    def << value
      set(value)
    end
  end

  class Drive < WPI::RobotDrive

    def tank *args
      tankDrive *args
    end

    def polar *args
      mecanumDrive_Polar *args
    end

    def cartesian x, y, rotation, gyro=0
      mecanumDrive_Cartesian x, y, rotation, gyro
    end

    def mecanum x, y, rotation, gyro=0
      cartesian x, y, rotation, gyro
    end

    def stop
      stopMotor
    end

  end

end
