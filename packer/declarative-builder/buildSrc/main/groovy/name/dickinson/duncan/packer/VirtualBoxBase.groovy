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

package name.dickinson.duncan.packer

/**
 *
 *
 * @author Duncan Dickinson
 */
class VirtualBoxBase {
    PackerBuilder baseConfig

    def vm_name

    def headless = 'false'

    def disk_size = '10000'

    def ssh_username = baseConfig.username
    def ssh_password = baseConfig.password

    def ssh_port = 22

    def ssh_wait_timeout = '20000s'

    def guestAdditionsMode = "disable"

    def guest_additions_url, guest_additions_sha256

    def provisioners = [ ]

    def output_directory
}
