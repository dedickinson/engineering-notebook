package com.github.labnotebook.groovylabs.chapter2;
class Bus {
	static final Integer MAX_PASSENGERS = 125
	static final SEATS = 40
}

println Bus.MAX_PASSENGERS

Bus dodgyBus = new Bus()
try {
	dodgyBus.MAX_PASSENGERS = 200
} catch(any) {
	println "Exception caught: ${any}"
}

try {
	dodgyBus.SEATS = 60
} catch(any) {
	println "Exception caught: ${any}"
}

//This won't work
//final MAX_WEIGHT = 240
//MAX_WEIGHT = 275
//assert MAX_WEIGHT == 240

class TestAnswers{
	//Which animals are cuter?
	static final ANSWER1 = "bilbies"
}

String myAnswer = "kittens"
if (myAnswer == TestAnswers.ANSWER1) {
	println "Correct!"
} else {
	println "Incorrect!"
}