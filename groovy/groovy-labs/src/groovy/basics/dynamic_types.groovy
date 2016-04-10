/**
 * #Problem
 * Which data types does Groovy map values to by default?
 *
 * #Solution
 * The `printer` closure will output the details of the class chosen by Groovy.
 * A number of 'raw' values are passed through to see what happens.
 */
 
printer = {
    //println "The value [${it}] was mapped to ${it.class.name}"
    println "|${it}|${it.class.name}|"
}

//A boolean value
printer true

//Single character
printer 'a'

//String
printer "This is a String"

//GString
def name = 'Larry'
printer "Hello ${name}"

//Max value for Byte
printer 127

//Max value for Short
printer 32767

//Max value for Integer
printer 2147483647

//Max value for Long
printer 9223372036854775807

//Something longer than Long
printer 92233720368547758070

//A small decimal value
printer 3.14

//Max value for Float
printer 3.4028235E38

//Max value for Double
printer 1.7976931348623157E308