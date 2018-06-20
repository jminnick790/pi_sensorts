/**
 *  Pi Sensors
 *
 *  Copyright 2017 Jason Minnick
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Pi Sensors",
    namespace: "jminnick790",
    author: "Jason Minnick",
    description: "Update Virtual Sensor From Web Service Call Initiated by GPIO change on Raspberry Pi",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	 section("Choose sensors to trigger the action") {

            input "contactSensors", "capability.contactSensor",
                title: "Open/close sensors", multiple: true
        }
}

mappings {
  path("/contactSensors/:device/:command") {
    action: [
      PUT: "updateSensors"
    ]
  }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
}

// TODO: implement event handlers

void updateSensors() {
    // use the built-in request object to get the command parameter
    def command = params.command
    def device = params.device

    // all switches have the command
    // execute the command on all switches
    // (note we can do this on the array - the command will be invoked on every element
    switch(command) {
        case "open":
        	contactSensors.each{
            	if(it.displayName == device){
            		it.open()
            	}
            }
            break
        case "close":
            contactSensors.each{
            	if(it.displayName == device){
            		it.close()
            	}
            }
            break
        default:
            httpError(400, "$command is not a valid command for all switches specified")
    }
}