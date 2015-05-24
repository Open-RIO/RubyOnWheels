module Toast

  java_import "jaci.openrio.toast.core.Environment"
  java_import "jaci.openrio.toast.core.StateTracker"
  java_import "jaci.openrio.toast.lib.state.StateListener"
  java_import "jaci.openrio.toast.core.thread.Heartbeat"
  java_import "jaci.openrio.toast.core.thread.HeartbeatListener"
  java_import "jaci.openrio.toast.core.thread.ToastThreadPool"
  java_import "jaci.openrio.toast.lib.state.LoadPhase"

  module WPI
    include_package "edu.wpi.first.wpilibj"
  end

  module Toast
    include_package 'jaci.openrio.toast'
  end

  class Controller < WPI::Joystick
  end

  class DigitalInput < WPI::DigitalInput
  end

  class DigitalOutput < WPI::DigitalOutput
  end

  class Encoder < WPI::Encoder
  end

  def self.shutdown method=:safe
    if method == :crash
      TOAST_.shutdownCrash
    else
      TOAST_.shutdownSafely
    end
  end

  def self.station
    TOAST_.station
  end

  def self.taste
    TOAST_.getRandomTaste
  end

  def self.tick filter=:all, &block
    StateTracker.addTicker StateListener::Ticker.impl { |method, tickstate|
      state = tickstate.to_s.downcase.to_sym
      if filter == :all || filter == state
        block.call state
      end
    }
  end

  def self.transition filter=:all, &block
    StateTracker.addTransition StateListener::Transition.impl { |method, tickstate, oldstate|
      state = tickstate.to_s.downcase.to_sym
      old = oldstate.to_s.downcase.to_sym
      if filter == :all || filter == state
        block.call state, old
      end
    }
  end

  def self.loadphase &block
    LoadPhase.addCallback java.util.function.Function.impl { |method, phase|
      block.call phase.to_s.downcase.to_sym
      java.lang.Void
    }
  end

  def self.start &block
    loadphase {
      if phase == :start
        block.call
      end
    }
  end

  def self.heartbeat &block
    Heartbeat.add HeartbeatListener.impl { |method, skipped|
      block.call skipped
    }
  end

  # Ambiguity~~
  class ToastThreadPool
    java_alias :submit, :addWorker, [java.util.concurrent.Callable.java_class]
  end
  def self.go &block
    ToastThreadPool.INSTANCE.submit {
      block.call
    }
  end

  def self.FMS?
     Environment.isCompetition
  end

  def self.simulation?
    Environment.isSimulation
  end

  def self.verification?
    Environment.isVerification
  end

  def self.embedded?
    Environment.isEmbedded
  end

end
