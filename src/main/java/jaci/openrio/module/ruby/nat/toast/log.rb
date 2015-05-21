module Toast
    class Log
      def self.puts type=:info, message
        if type == :info
          LOG_.info message
        elsif type == :warn
          LOG_.warn message
        elsif type == :error
          LOG_.error message
        elsif type == :severe
          LOG_.severe message
        end
      end

      def self.<< message
        puts message
      end
    end
end
