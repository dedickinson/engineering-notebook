def list1 = [10, 10, 10]
list1 << 20
println list1

def list2 = [10, 10, 10].asImmutable()
list2 << 20
println list2