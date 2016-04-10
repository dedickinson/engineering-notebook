import groovy.transform.*

@Canonical
@ToString(includeNames = true)
class Person {
    String name
    def friends = []
    def hobbies = [] as Set
    
    public void addHobby(String hobby) {
        this.hobbies << hobby
    }
    
    public void addFriend(String friend) {
        this.friends << friend
    }
}

def alice = new Person(name: 'Alice')
alice.with {
    addHobby 'Tennis'
    addHobby 'Cards'
    addFriend 'John'
    addFriend 'Cosmo'
}

println alice.toString()