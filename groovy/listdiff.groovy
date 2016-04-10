// A script demo for diff'ing lists

def oldData = [
    [1, 'doug', 'carrots'],
    [2, 'sam', 'mushrooms'],
    [3, 'anne', 'peas'],
    [4, 'jim', 'none'],
    [5, 'alex', 'lettuce'],
    [6, 'ginger', 'tomato']
].collectEntries{["${it[0]}": [it[1], it[2]] ]}

def newData = [
    [1, 'doug', 'carrots'],
    [2, 'sam', 'cauliflower'],
    [5, 'alex', 'lettuce'],
    [6, 'ginger', 'cabbage'],
    [10, 'jane', 'potato']
].collectEntries{["${it.head()}": it.tail()]}



def deletedRecords = oldData.keySet() - newData.keySet().intersect(oldData.keySet())
println "To delete: $deletedRecords"


def newRecordIds = newData.keySet() - newData.keySet().intersect(oldData.keySet())
def newRecords = newData.findAll{ k, v -> k in newRecordIds}
println "To insert: $newRecordIds:"
newRecords.each{ println "  - $it" }

def updatedRecordIds = (newData - oldData).keySet() - newRecordIds
def updatedRecords = newData.findAll{ k, v -> k in updatedRecordIds}
println "To update: $updatedRecordIds"
updatedRecords.each{ println "  - $it was ${oldData.get(it.key)}" }
