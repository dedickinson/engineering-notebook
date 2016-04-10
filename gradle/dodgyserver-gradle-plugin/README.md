# DodgyServer - a Gradle plugin

The DodgyServer plugin provides a very, very, very basic HTTP server for use in checking out your project build.
I use it when working with [JBake](http://jbake.org/) via the 
[JBake Gradle Plugin](https://plugins.gradle.org/plugin/me.champeau.jbake). Configuring DodgyServer should be pretty 
easy - just point it at the directory you'll need to serve. 

Probably best if you run DodgyServer on builds using the Gradle daemon or continuous builds (i.e. with the `-t` or
`--continuous` flags. I like running the continuous build when JBaking as I can update content/styling etc and see
my work updated quickly - like the proverbial hamster in a wheel.

DodgyServer only supports GET requests and shouldn't be used as a production server unless you are seeking to
sabotage your career.

## Usage

Probably best to check out the [DodgyServer Gradle plugin page](https://plugins.gradle.org/plugin/name.dickinson.duncan.dodgyserver)

### The old way

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "name.dickinson.duncan:dodgyserver:0.1"
      }
    }
    apply plugin: name.dickinson.duncan.dodgyserver


### The new way

    plugins {
        id 'name.dickinson.duncan.dodgyserver' version 0.1
    }

## Tasks

* `startDodgyServer`: Starts a server
* `stopDodgyServer`: Stops the server
* `stopAllDodgyServers`: Stops all known running DodgyServers
* `listAllDodgyServers`: Lists all known running DodgyServers

## Properties

* `httpPort`: the server port (9999 by default)
* `documentRoot`: The base directory of your site (`build/doc` by default)
* `directoryIndex`: A list of possible directory index file names (`['index.html']` by default)

## Problems?
Any DodgyServers should just die if you call `gradle --stop` (when using the daemon) or just hitting `ctrl-d` when
running a continuous build.

Any fan noises coming from your laptop are purely related to my love of white noise and not my poor thread management.
Maybe log a job.