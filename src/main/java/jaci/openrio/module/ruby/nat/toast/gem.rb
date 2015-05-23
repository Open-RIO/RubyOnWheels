module Toast
  class Gem

    java_import "jaci.openrio.module.ruby.RubyScriptLoader"

    def self.init
      begin
        @env = `gem environment`
        @split = @env.split(/\r?\n/)
        opts = {}
        @split.each_with_index.map do |x, index|
          if x =~ /\s*(-\s*GEM\s+PATHS:?)/
            opts[:start] = index
          elsif x=~ /\s*(-\s*GEM\s+CONFIGURATION:?)/
            opts[:end] = index
          end
        end

        if opts.key?(:start) && opts.key?(:end)
          @paths = []
          for i in opts[:start]+1...opts[:end]
            if @split[i] =~ /\s*-\s*(.*)\r?\n?/
              @paths << $1
              RubyScriptLoader.addGems "#{$1}/gems"
            end
          end
        end
      rescue
      end
    end

    init

  end
end
