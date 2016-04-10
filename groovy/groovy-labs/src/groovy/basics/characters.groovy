
/**
 * # Problem
 * How do you convert a numeric value into a character and vice-versa?
 * 
 * # Solution
 * It's easy to cast an integer (int) to a character (char):
 */

println "Integer value of a character (\$): ${(int)'$'}"
println "Character value of an integer (36): ${(char)36}"
println "Character value of a hexidecimal integer (0x24): ${(char)0x24}" 

/**
 * This next loop will print every possible value of a character variable.
 * The `println` is commented out as this loop can can a while to complete.
 * Uncomment but it may take a while.
 */
(0x0000..0xffff).each {
    //println "${it}: ${(char) it}"
}