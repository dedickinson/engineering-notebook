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
class VirtualBoxIsoBuilder extends VirtualBoxBase {

    static final type = 'virtualbox-iso'

    def iso_url, iso_checksum, iso_checksum_type
    def guest_os_type
    def boot_command
    def boot_wait = '5s'

    def format = 'ovf'
    def http_directory = 'http'
}
