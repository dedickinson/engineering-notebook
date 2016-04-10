import groovy.transform.*

/**
 * The Person class is very basic - holding only a name.
 */
class Person {
    /**  */
    final private String name
    
    /**
     * The person's name is set at instantiation
     * @param name  The person's name
     */
    public Person(String name = '') {
        this.name = name
    }
}

def katrina = new Person('Katrina')
def trina = new Person('Katrina')

if (katrina == trina) {
    println "I believe that katrina and trina are the same person"
} else {
    println "I believe that katrina and trina are different people"
}

/**
 * The Person class is very basic - holding only a name - but annotated with @EqualsAndHashCode
 *
 * @see EqualsAndHashCode
 */
@EqualsAndHashCode(includeFields = true)
class Person2 {
    /**  */
    String name
    
    /**
     * The person's name is set at instantiation
     * @param name  The person's name
     */
    public Person2(String name = '') {
        this.name = name
    }
}

def billy = new Person2('Billy')
def william = new Person2('Billy')

if (billy == william) {
    println "I believe that billy and william are the same person"
} else {
    println "I believe that billy and william are different people"
}


/**
 * The Person3 class adds an ID property to allow for easy
 * differentiation between people to better suit EqualsAndHashCode.
 *
 * <p>The {@link EqualsAndHashCode} annotation needs `(includeFields = true)`
 * to ensure that the object's fields are taken into account.
 * 
 * <p>Important note:  <a href="http://en.wikipedia.org/wiki/Number_Six_%28The_Prisoner%29#I_am_not_a_number.2C_I_am_a_free_man">I am not a number. I am a free man.</a>
 *
 * @see     EqualsAndHashCode
 */
@ToString(includeNames = true)
@EqualsAndHashCode
class Person3 {
    /** */
    int id
    
    /**  */
    String name
    
    /**
     * The person's name is set at instantiation
     * @param name  The person's name
     */
    public Person3(int id, String name = '') {
        this.id = id
        this.name = name
    }
}

def doug = new Person3(1, 'Douglas')
def douglas = new Person3(2, 'Douglas')

if (doug == douglas) {
    println "I believe that doug and douglas are the same person"
} else {
    println "I believe that doug and douglas are different people"
}
println "${doug.toString()} - Hash code: ${doug.hashCode()}"
println "${douglas.toString()} - Hash code: ${douglas.hashCode()}"