import com.beust.jcommander.JCommander
import com.beust.jcommander.MissingCommandException
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

@Grab('com.beust:jcommander:1.48')

// Some basic usage ideas with sub-commands

// Setup sub-commands for the CLI
enum CliCommands {
    HELP('help'),
    SEARCH('search')

    final String name

    CliCommands(String name) {
        this.name = name
    }

    @Override
    public String toString() {
        return name
    }
}

// An individual command
@Parameters(commandNames = ['search'], commandDescription = "Searches for a library using search text or coordinates")
class CommandSearch {

    @Parameter(description = '<search text>')
    List<String> queryText

    @Parameter(names = ['-g', '--groupId'], description = 'Group ID')
    String groupId

    @Parameter(names = ['-a', '--artifactId'], description = 'Artifact ID')
    String artifactId

    final String usage = 'Search text or coordinates (group and/or artifact ID) must be provided'

    boolean isValid() {
        (queryText || (groupId || artifactId))
    }

    String getQueryText(){
        queryText.join ' '
    }

    def performSearch() {
        if (queryText) {
            println "Searching for: $queryText"
        } else {
            println "Searching for: $groupId:$artifactId"
        }
    }
}

@Parameters(commandNames = ['help'], commandDescription = "Displays help information")
class CommandHelp {
    @Parameter(description = 'The requested command(s) for which you seek help')
    List<String> helpSubCommands
}

@Parameters(separators = "=")
class CliArgs {
    @Parameter(names = ['-h', '--help'], help = true)
    Boolean help = false

    final JCommander jc

    def commandHelp = new CommandHelp()
    def commandSearch = new CommandSearch()

    CliArgs() {
        jc = new JCommander(this)
        jc.programName = 'pomes'
        jc.addCommand commandHelp
        jc.addCommand commandSearch
    }

    Boolean parse(String... args) {
        try {
            jc.parse(args)
        } catch(MissingCommandException mce) {
            return false
        }
        true
    }

    String getCommand() { jc.parsedCommand }

    StringBuilder getUsage() {
        StringBuilder out = new StringBuilder()
        jc.usage(out)
        out
    }

    void getCommandUsage(String command = '', StringBuilder out) {
        out.append getCommandUsage(command)
    }

    StringBuilder getCommandUsage(String command = '') {
        StringBuilder out = new StringBuilder()
        if (command && jc.commands.containsKey(command)) {
            jc.usage(command, out)
        } else {
            out << 'Please select a command:\n'
            jc.commands.findAll { it.key != CliCommands.HELP.name }.each { cmd, cmdObj ->
                out << "  $cmd: ${jc.getCommandDescription(cmd)}\n"
            }
        }
        out
    }

    final Map<String, Closure> commandHandlers = [
            search: {
                if (commandSearch.isValid())
                    commandSearch.performSearch()
                else
                    println "Incorrect usage: ${commandSearch.usage}"
            },
            help: {
                println getCommandUsage(commandHelp.helpSubCommands?.get(0))
            }
    ]

    void handleRequest() {
        if (!command || !jc.commands.containsKey(command))
            jc.usage()
        else
            commandHandlers."$command"()
    }
}

def cli = new CliArgs()
if (cli.parse(args))
    cli.handleRequest()
else
    println cli.getCommandUsage()
