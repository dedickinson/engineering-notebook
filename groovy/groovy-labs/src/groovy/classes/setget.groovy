/**
 * The Person class is very basic - holding only a name.
 */
class Person {
    /**  */
    public String name
    
    /**
     * The person's name is set at instantiation
     * @param name  The person's name
     */
    public Person(String name = '') {
        this.name = name
    }
}

def jim = new Person()
jim.name = 'Jim'
jim.name = 'Jimmy'
assert jim.name == 'Jimmy'
println "Hi, I'm ${jim.name}"
//This next line won't work as we'd need to explicitly declare getName()
//println "Hi, I'm ${jim.getName()}"

/**
 * This might jar a bit - people using this class can change
 * the private `name` property as Groovy creates a setter.
 */ 

/**
 * The Person2 class is very basic - holding only a name.
 */
class Person2 {
    /** 
     * Changing `name` to be `private`. 
     * Groovy will still create a getter and a setter
     */
    private String name
    
    /**
     * The person's name is set at instantiation
     * @param name  The person's name
     */
    public Person2(String name = '') {
        this.name = name
    }
}

def alex = new Person2('Alex')
assert alex.name == 'Alex'
println "Hi, I'm ${alex.name}"
try {
    //This works as Groovy creates a field setter
    alex.name = 'Lexia'
} catch (all) {
    println "I caught an exception when you tried to change ${alex.name}'s name: ${all}"
}
/** This will work: */
assert alex.name == 'Lexia'


/**
 * Person3 aims to protect the private `name` property
 * by explicitly providing a setter
 */

/**
 * The Person3 class is very basic - holding only a name.
 */
class Person3 {
    /** 
     * Changing `name` to be `private`. 
     * Groovy will still create a getter and a setter
     */
    private String name
    
    /**
     * This is daft but proves a point. The `name` field setter now defers to 
     * `setName` and this setter doesn't like the name 'Mavis'
     */
    public setName(String name) {
        if (name == 'Mavis') throw new Exception('Mavis is not allowed')
        this.name = name
    }
    
    /**
     * The person's name is set at instantiation
     * @param name  The person's name
     */
    public Person3(String name = '') {
        this.name = name
    }
}

def duncan = new Person3('Duncan')
assert duncan.name == 'Duncan'
println "Hi, I'm ${duncan.name}"
try {
    //This works as Groovy creates a field setter
    duncan.name = 'Mavis'
} catch (all) {
    println "I caught an exception when you tried to change ${duncan.name}'s name: ${all}"
}
assert duncan.name == 'Duncan'

/** Changing the name to 'Hank' will work just fine: */
duncan.setName 'Hank'
assert duncan.name == 'Hank'