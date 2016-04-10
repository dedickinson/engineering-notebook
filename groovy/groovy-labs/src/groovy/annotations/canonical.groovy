import groovy.transform.Canonical

/**
 * A basic person class to illustrate the {@link Canonical} annotation
 */
@ToString(includeNames = true)
@Canonical
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