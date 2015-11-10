Vagrant.configure("2") do |config|
   config.vm.define "centos-7-base"
   config.vm.box = "centos-7-base"

   config.vm.provider :virtualbox do |v, override|
     v.gui = false
     v.customize ["modifyvm", :id, "--memory", 1024]
     v.customize ["modifyvm", :id, "--cpus", 1]
   end

end
