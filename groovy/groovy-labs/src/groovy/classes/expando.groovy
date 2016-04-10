package com.github.labnotebook.groovylabs.chapter4

car = new Expando()
car.year = '1994'
car.make = 'Toyota'
car.model = 'Corolla'



println car.year
println car.make
println car.model
println car

car.paint = {colour = it}
car.paint 'green'
println colour