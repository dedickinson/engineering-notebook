import groovy.transform.*

interface StaffMember {
    public String getIdentification()
}

@Canonical
class Person implements StaffMember {
    /** An identifier for the person */
    int id
    
    /** The name of the person */
    String name
    
    public String description
    
    def hobbies = [] as Set
    
    public String getIdentification() {
        "${this.id}: ${this.name}"
    }   
}

def jim = new Person(name: 'Jim', id: 1)

println "Class name: ${jim.class.name}"

println 'Constructors: '
jim.class.constructors.each { cons ->
    println " - ${cons}"
}
println 'Fields: '
jim.class.fields.each { field ->
    println " - ${field}"
}

println 'Methods: '
jim.class.methods.each { method ->
    println " - ${method}"
}

println 'Implemented interfaces: '
jim.class.interfaces.each { iface ->
    println " - ${iface}"
}