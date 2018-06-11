
Pod::Spec.new do |s|
  s.name         = "RNSmaato"
  s.version      = "1.0.0"
  s.summary      = "RNSmaato"
  s.description  = <<-DESC
                  https://github.com/jerson/react-native-smaato
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "jeral17@gmail.com" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/jerson/react-native-smaato.git", :tag => "master" }
  s.source_files  = "RNSmaato/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  s.dependency "SmaatoSDK", "~> 9.0.1"

end

  