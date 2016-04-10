import groovy.transform.*

interface StaffMember {
    public String getIdentification()
}

@Canonical
class Person {
    
    /** The name of the person */
    String name
  
}

@Canonical
@TupleConstructor(includeSuperProperties = true)
class Staff extends Person implements StaffMember {
    /** An identifier for the person */
    int id
    
    public String getIdentification() {
        "${this.id}: ${this.name}"
    }   
}

def attendees = [
    new Person('David'),
    new Staff(id: 102, name: 'Alice'),
    new Staff(id: 99, name: 'Pamela'),
    new Person('Daisy')
]

attendees.each{attendee -> 
    if (StaffMember in attendee.class.interfaces) {
        println "${attendee.name} - can I see your identification?"
        println ">> Sure, it's ${attendee.getIdentification()}"
    } else {
        println "Welcome ${attendee.name}, please sign the guest register."
    }
}