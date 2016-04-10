import groovy.transform.*

/**
 * A basic person class to illustrate the {@link Immutable} annotation
 */
@Immutable
class Person {
    /** An identifier for the person */
    int id
    
    /** The name of the person */
    String name
    
}

def jim = new Person()
println jim.toString()

def bill = new Person(id: 101, name: 'Bill')
println bill.toString()

def jane = new Person(202, 'Jane')
println jane.toString()
try {
    jane.id = 2020
} catch (ReadOnlyPropertyException e) {
    println "Attempt to change Jane's ID failed: ${e}"
}