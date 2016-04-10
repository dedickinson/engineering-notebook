package com.github.labnotebook.groovylabs.chapter5
/**
 * Experiments into the scope of enclosing variables used by closures
 * @author Duncan Dickinson
 * @category experiment
 */

/**
 * # Experiment
 * How do you convert a numeric value into a character and vice-versa?
 * 
 * # Solution
 * 
 */

/**
 * A basic phone message bank (answering machine). 
 */
class MessageBank {
	/** A display name for the person */
	private String name
	
	/** The phone number for the person */
	private String phone
	
	/** A pin number used to access a message bank */
	private String pin = '0000'
	
	/** A list of all messages in the message bank */
	private messages = []
	
	/** A closure to be used when trying to phone the person */
	public leaveMessage = { message ->
		this.messages << message
		"Thankyou for calling ${name} on ${phone}. Unfortunately I can't answer my phone so please leave a message."
	}
	
	/**
	 * 
	 * @param pin
	 * @return If pin is correct return the list of messages in the message bank. Otherwise, null.
	 */
	public accessMessageBank = {pin ->
		if (pin == this.pin) return messages
		null
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", phone=" + phone + "]";
	}	
	
}

/** Create a new message bank instance */
MessageBank myMessageBank = new MessageBank(name: 'Duncan', phone:'555-1234-5678', pin: '1234')

/** I could use the `callDuncan` variable as a parameter for various method calls */
def callDuncan = myMessageBank.leaveMessage

/** */
def getMessages = myMessageBank.accessMessageBank

/** Now release the myMessageBank reference */
myMessageBank = null

/** People can now leave me messages even though I blew away the reference to myMessageBank */
println callDuncan("It's your Mum here - you forgot Dad's birthday")
println callDuncan("It's your Mum here again - where are you?")
println callDuncan("Hey there Jane - who's Duncan?")
println callDuncan("The fish will fly in the evening")

/** Now check for messages */
int i = 0
for (msg in getMessages('1234')) {
	println "Message number ${++i}: ${msg}"
}
 
/**
 * # Conclusion
 **/