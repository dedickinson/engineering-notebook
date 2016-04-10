/**
 * Determines the average for a list of numbers
 * @param   list  a list of numbers
 * @return        the average of the numbers in the list
 */
def determineAverage(list) {
    for (item in list) {
        if (! item instanceof Number) {
            throw new IllegalArgumentException()
        }
        if (item instanceof String) {
            throw new IllegalArgumentException()
        }
    }
    return list.sum() / list.size()
}

assert determineAverage([5, 5, 8]) == 6
assert determineAverage([-10, 5, 2, 7]) == 1

//assert determineAverage([12, '18']) == 15

//def data = [5, 5, 8, 'kitten']
//determineAverage(data)

//This still causes a problem as the Boolean is see as a number but not a string
determineAverage([14, false])