Vagrant.configure("2") do |config|
   config.vm.define "centos-7-base"
   config.vm.box = "centos-7-base"

   config.ssh.username = "automator"
   config.ssh.private_key_path = "~/.ssh/automator"
   config.ssh.port = "4444"
   config.ssh.insert_key = false
   config.ssh.pty = false

   config.vm.provider :virtualbox do |v, override|
     v.gui = false
     v.customize ["modifyvm", :id, "--memory", 1024]
     v.customize ["modifyvm", :id, "--cpus", 1]
   end

end
