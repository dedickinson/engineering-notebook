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

package name.dickinson.duncan.packer.os

/**
 *
 *
 * @author Duncan Dickinson
 */
class Centos {

    static final String osName = 'Centos'
    static final String osVersion = '7'
    static final String osFamily = 'Linux'
    static final String osWordSize = '64'

    static final shutdownCommand = "sudo systemctl poweroff"

    static final String = "$osFamily:$osName:$osVersion:$osWordSize"

    static final bootCommand = [
            "<wait><esc><esc>",
            "linux ks=http://{{.HTTPIP}}:{{.HTTPPort}}/kickstart<enter>"
    ].asImmutable()

    static final String clean = [
            'sudo yum -y clean all',
            'sudo swapoff -a',
            'sudo mkswap /dev/mapper/vg_vagrantcentos-lv_swap',
            'sudo dd if=/dev/zero of=/boot/EMPTY bs=1M',
            'sudo rm -f /boot/EMPTY',
            'sudo dd if=/dev/zero of=/EMPTY bs=1M',
            'sudo rm -f /EMPTY'
    ]

}
