/**
 * # Problem
 * What is the range of values available for each numeric type in java.lang?
 *
 * # Solution
 * The numeric types provide a MIN_VALUE and MAX_VALUE static property containing
 * the information we need. We can also use the SIZE static property to determine 
 * how many bits each type uses to store a value.
 */

/**
 * The printer closure prints basic numerical class info
 */
printer = {
println """
${it.name}:
  - Minimum value: ${it.MIN_VALUE}
  - Maximum value: ${it.MAX_VALUE}
  - Number of bits used to store values: ${it.SIZE}
"""
}
printer(Byte.class)
printer(Short.class)
printer(Integer.class)
printer(Long.class)
printer(Float.class)
printer(Double.class)
printer(Character.class)

/**
 * BigInteger and BigDecimal don't work the same way so the following calls will fail:
 */
//printer(BigInteger.class)
//printer(BigDecimal.class)