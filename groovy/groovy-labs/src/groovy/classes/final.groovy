/**
 * The Person class is very basic - holding only a name.
 */
class Person {
    /** Once set, name can't be changed */
    final String name
    
    /**
     * The person's name is set at instantiation
     * @param name  The person's name
     */
    public Person(String name) {
        this.name = name
    }
}

def billy = new Person('Billy')
assert billy.name == 'Billy'

try {
    billy.name = 'Silly'
} catch (all) {
    println "I caught an exception when you tried to change ${billy.name}'s name: ${all}"
    assert billy.name == 'Billy'
}

/**
 * Test 2: Don't provide a constructor parameter to set the name
 */

/**
 * The Person class is very basic - holding only a name.
 */
class Person2 {
    /** Once set, name can't be changed */
    final String name
    
    /**
     * Basic constructor
     */
    public Person2() {
        
    }
}

def jane = new Person2()
try {
    jane.name = 'Jane'
} catch (all) {
    println "I caught an exception when you tried to set a name after instantiation: ${all}"
}



/**
 * Test 3: Add a Set to the class that is final and see what happens
 */

/**
 * The Person3 class allows for a name and a set of friends.
 */
class Person3 {
    /** Once set, name can't be changed */
    final String name
    
    /**  */
    final friends = [] as Set
    
    /**
     * The person's name is set at instantiation
     * @param name  The person's name
     */
    public Person3(String name) {
        this.name = name
    }
}

def john = new Person3('John')

/**
 * Even though `friends` is `final` that's the reference to the `Set`
 * not the contents of the `Set`. The next two friend additions will work:
 */
john.friends << 'Billy'
john.friends << 'Jane'

/**
 * But if someone tried to swap in a new `Set` we'll get an exception:
 */
try {
    john.friends = ['Andrew', 'Isambard']
} catch(all) {
    println "I caught an exception when you tried to change ${john.name}'s friend list: ${all}"
}