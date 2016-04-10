/*
 * Copyright 2014 Duncan Dickinson <duncan at dickinson.name>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo

import groovy.util.logging.*
import org.springframework.integration.annotation.ServiceActivator
/**
 * Basic class for integration example
 */
@Log
class Handler {
    
    /**
     * A trivial method - purely logs a message and passes back your file
     * @param input A file that is returned back to you
     * @return the file you passed in
     */
    @ServiceActivator
	public File handleFile(File input) {
		log.info("Copying file: ${input.getAbsolutePath()}")
		return input
	}
}

