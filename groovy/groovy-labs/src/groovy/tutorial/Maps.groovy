def leagueTable = [:]

leagueTable['Souths'] = 10
leagueTable['Norths'] = 12
leagueTable['Easts'] = 8
leagueTable['Wests'] = 10

prettyPrint = { team, points ->
    println "$team has $points points"
} 

leagueTable.each(prettyPrint)

leagueTable = [
    'Souths': 10,
    'Norths': 12,
    'Easts': 8,
    'Wests': 10
]

for (entry in leagueTable) {
    println "$entry.key - $entry.value"
}