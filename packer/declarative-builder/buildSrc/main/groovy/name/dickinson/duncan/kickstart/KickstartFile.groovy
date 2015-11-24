/*
 *    Copyright ${year} Duncan Dickinson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package name.dickinson.duncan.kickstart

/**
 *
 *
 * @author Duncan Dickinson
 */
class KickstartFile {

    def options = ['install',
                   'cdrom',
                   'lang en_US.UTF-8',
                   'keyboard us',
                   'network --onboot yes --device eth0 --bootproto dhcp',
                   'firewall --enabled --service=ssh',
                   'selinux --permissive',
                   'timezone Australia/Brisbane',
                   'bootloader --location=mbr --driveorder=sda --append="crashkernel=auto rhgb quiet"',
                   'text',
                   'skipx',
                   'zerombr',
                   'unsupported_hardware',
                   'firstboot --disabled',
                   'reboot',
                   'authconfig --useshadow --passalgo=sha512']
    def partitioning = ['clearpart --all --initlabel',
                        'autopart --type=plain --fstype=xfs']
    def includePackages = []
    def excludePackages = []

    static final template = '''\
${kickstart.options.join('\n')}

${kickstart.partitioning.join('\n')}

rootpw --iscrypted --lock ${packer.rootPwdCrypt}
user --iscrypted --lock --name=${packer.user} --password=${packer.userPwdCrypt} --uid=9999 --groups=wheel

%packages --excludedocs
${kickstart.packages.join('\\n')}
%end

%post
echo "${packer.user}        ALL=(ALL)       NOPASSWD: ALL" >> /etc/sudoers.d/${packer.user}
/bin/chmod 0440 /etc/sudoers.d/${packer.user}

mkdir /home/${packer.user}/.ssh
chmod 700 /home/${packer.user}/.ssh
sudo chown -R ${packer.user}:${packer.user} /home/${packer.user}/.ssh

#Prevent user from pressing I to initiate service load
echo PROMPT=no>>/etc/sysconfig/init

yum -y update

yum -y install ${kickstart.postPackages.join(' ')}

%end
'''
    }
