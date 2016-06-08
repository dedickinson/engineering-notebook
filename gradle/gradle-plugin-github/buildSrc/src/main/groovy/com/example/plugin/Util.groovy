/*
 *    Copyright $year slavinson
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

package com.example.plugin

import java.time.LocalDateTime
import java.time.ZoneOffset

class Util {
    static Map nodeToMap(Node node) {
        def handle
        handle = { n ->
            if (n in String)
                return n

            Map retMap = n.attributes() ?: [:]
            List values = n.collect(handle)
            if (values) {
                List lst = values.split { it in String }
                if (lst[0]) {
                    retMap << [values: lst[0]]
                }

                lst[1].each {
                    it.keySet().each { key ->
                        if (retMap.containsKey(key)) {
                            retMap.get(key) << it.get(key)
                        } else {
                            retMap.put(key, [it.get(key)])
                        }
                    }
                }

            }
            [(n.name()): retMap]
        }

        // Convert it to a Map containing a List of Maps
        [(node.name()): node.collect { nodeChild -> [(nodeChild.name()): nodeChild.collect(handle)] }]
    }

    static LocalDateTime convertDate(Date date) {
        date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
    }
}
