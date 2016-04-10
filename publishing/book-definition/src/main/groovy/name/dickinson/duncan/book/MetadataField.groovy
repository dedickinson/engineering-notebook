package name.dickinson.duncan.book
/*
 * Copyright 2015 Duncan Dickinson
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import groovy.util.logging.Slf4j;

/**
 * Constants for the metadata fields utilised from markdown files
 * @author Duncan Dickinson
 *
 */
@Slf4j
enum MetadataField {
	TITLE ('title'),
	DESCRIPTION('description'),
	STATUS('status')

	MetadataField(String value) {
		this.value = value
	}
	
	private final String value
	
	public String value() {
		return value
	}

	static getField(String name) throws IllegalArgumentException {
		Enum.valueOf(MetadataField, name.toUpperCase())
	}
	
	/**
	 * Helper method that takes a basic key:value map and converts the key to the appropriate MetaDataField enum value
	 * @param metadata	The basic metadata map (most likely extracted from a markdown file)
	 * @return an EnumMap version of the metadata param
	 */
	static EnumMap<MetadataField, String> createMetadataMap(Map<String,String> metadata) {
		EnumMap<MetadataField, String> map = new EnumMap<MetadataField, String>(MetadataField)
		for (item in metadata) {
			try {
				MetadataField key = MetadataField.getField(item.key)
				map.put(key, item.value)
			} catch (IllegalArgumentException e) {
				//This is ignored so as to allow people to enter any metadata in their document
				log.warn("The requested metadata field (${item.key}) is not supported so was ignored")
			}
		}
		return map
	}
}