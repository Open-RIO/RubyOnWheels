module Toast

  java_import "jaci.openrio.toast.core.Environment"
  java_import "jaci.openrio.toast.core.StateTracker"
  java_import "jaci.openrio.toast.lib.state.StateListener"

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
    StateTracker.addTicker StateListener::Transition.impl { |method, tickstate|
      state = tickstate.to_s.downcase.to_sym
      if filter == :all || filter == state
        block.call state
      end
    }
  end

end
