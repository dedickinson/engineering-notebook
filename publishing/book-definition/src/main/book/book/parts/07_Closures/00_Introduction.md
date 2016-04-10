---
title:	Closures
status:	in-progress
description:	Closures make methods (functions) first-class citizens. 
...

Closures represent a reference to a function (method) that is accompanied by a referencing environment. This means that you can store function references in a variable and pass them to a function and that the original scope of the closure is available. Confused? Well, let's get into it!

Oh, hang on, just a quick note. Whilst I tend to use the terms "function" and "method" somewhat interchangeably when discussing Groovy, people discussing closures more generally use the term "function". Some languages have very specific meanings for terms such as "function", "procedure", "method" but I'd suggest that if you use the term "method" or "function", most Java and Groovy developers will get your meaning. Alternatively, you could try to be more universal and call them all "subroutines"
