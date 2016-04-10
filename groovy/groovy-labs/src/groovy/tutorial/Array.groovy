//A fixed-length array
guesses = []
guesses << "cat"
guesses << "dog"
guesses << "rabbit"
println guesses

people = new String[2]
people[0] = "Harry"
people[1] = "Sally"
//This will fail
//people[2] = "Melvin"
println people

//More arrays
scores = [2, 6 ,8, 3, 1, 0, 7]
println scores

scores.add 9
scores.push 3
println scores

scores.remove 2
println scores

println 'Highest score: ' + Collections.max( scores )
println 'Lowest score: ' + Collections.min( scores )

scores.each{ println "Item: $it" }


def sum(arr) {
    sum = 0
    for (item in arr) {
        sum += item
    }
    return sum
}

total = sum(scores)

println """
The scores array: 
  - has $scores.size items
  - totals $total
  - has an average of ${sum / scores.size}
"""

def avg = {arr ->
    sum = 0
    for (score in scores) {
        sum +=score
    }
    return sum / arr.size
}

assert avg(scores) == sum / scores.size


//Sets
birdSightings = ['magpie', 'kookaburra', 'wagtail', 'magpie', 'budgie'] as Set
println birdSightings.sort()