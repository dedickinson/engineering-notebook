re = /.*net$/

println "internet " + (("internet" =~ re) ? "matched" : "failed")
println "network " + (("network" =~ re) ? "matched" : "failed")