---
title: 	Causing an Exception
status:	draft
description:	The `throw` keyword is used to intentionally cause an exception
...

The `throw` statement will cause an exception to be thrown. You'll use this inside your own code to either throw an exception type already provided by Java or Groovy or to throw an exception type that you have developed. Remembering that exceptions are classes, you can create your own subclass of `Exception` for your own project. Let's take a small step back for now and see how we throw an exception. 

Often, `new` is used within a `throw` statement to initialise a new exception instance. The example below will `throw` a `new` instance of the `IllegalStateException`. As part of creating the new exception instance I pass it a message to help inform the user as to why they're getting an exception:

```groovy
def numerator = 10
def denominator = -1

try {
    if (denominator < 0) {
        throw new IllegalStateException('I haven\'t learnt how to divide negative numbers')
    } else {
        return numerator / denominator
    }
} catch (any) {
    println "${any.message} (${any.class.name})"
}
```

You can use `new` to create a new instance of an exception but this won't cause the exception to be thrown - you'll need to `throw` it. The long-form version of the previous example would appear as:

```groovy
def ise = new IllegalStateException('I haven\'t learnt how to divide negative numbers')
throw ise
```

>Use the short-form - it's more concise and it's what other developers expect to see.

In the code above I `throw` an exception (`IllegalStateException`) to indicate a limitation in my program. As before, the `catch` will receive the thrown exception but this time it could be either the `IllegalStateException` or the `ArithmeticException`:

```groovy
def numerator = 10
def denominator = -1

try {
    if (denominator < 0) {
        throw new IllegalStateException('I haven\'t learnt how to divide negative numbers')
    } else {
        return numerator / denominator
    }
} catch (IllegalStateException e) {
    println 'I just caught an IllegalStateException'
} catch (ArithmeticException e) {
    println 'I just caught an ArithmeticException'
} catch (any) {
    println 'What just happened?'
}
```

The code above makes a little more sense than my earlier example as `denominator` may be `0` or a negative number and we are concerned with both possibilities. As mentioned earlier, we can use `|` to handle more than one exception type within the same `catch` block: 

```groovy
def numerator = 10
def denominator = -1

try {
    if (denominator < 0) {
        throw new IllegalStateException('I haven\'t learnt how to divide negative numbers')
    } else {
        return numerator / denominator
    }
} catch (IllegalStateException | ArithmeticException e) {
    println 'Stand back, I know how to handle this'
} catch (any) {
    println 'What just happened?'
}
```

# Constructing an Exception

There are a few approaches to constructing an exception, the main ones being:

`new IllegalStateException()`
: Creates a new instance without a message

`new IllegalStateException('Sorry, Dave, I cannot allow this')`
: Creates a new instance with a message

`new IllegalStateException('Sorry, Dave, I cannot allow this', e)`
: Creates a new instance with a message and provides the exception (`e`) that caused the exception you're now throwing.

Let's look at an example usage of the last variation. In the following code snippet I `catch` two possible exceptions and bundle `e` into a new exception instance but pass `e` to the new instance so that the caller could determine the cause:

```groovy
catch (IllegalStateException | ArithmeticException e) {
    throw new IllegalStateException('Unable to perform operation', e)
} 
```

Upon having the exception thrown at me I could use the `getCause()` method to determine if there was an underlying cause.

# Creating Your Own Exception

Whilst you can write your own Exceptions (they're just objects after all), you should look to the pre-packaged ones and see if they meet your needs. Alternatively, consider if you can write a subclass that provides refinement to an existing exception rather than just extending `Exception`.