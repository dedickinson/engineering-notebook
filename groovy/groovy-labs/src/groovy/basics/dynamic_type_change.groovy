/**
 * #Problem
 * Does the class determined by Groovy (under its dynamic typing model) change depending on the value
 * assigned to a variable?
 *
 * #Solution
 * Attempt to declare a variable and change its value in a number of ways to determine the outcome
 */
 
/**
 * ##Test 1
 *
 * 1. Assign a value to a variable declaration
 * 1. determine the data type selected 
 * 1. then change the value
 */
def test1 = "abc"
println test1.class.name
test1 = 3.14
println test1.class.name

/**
 * ##Test 2
 * 
 * 1. Don't assign a value to a variable declaration
 * 1. then set the value
 * 1. determine the data type selecte
 * 1. then change the value
 */
def test2 
test2 = "abc"
println test2.class.name
test2 = 3.14
println test2.class.name

/**
 * #Conclusion
 * Both tests revealed that the variable's type changed (String to BigDecimal)
 */