# Polimi 2020 - Software engineering project

## Group members
- Miah Mohd Ehtesham
- Longhi Leonardo
- Ouahidi Yassine

## Implemented features
- Regole complete
- CLI 
- GUI
- Socket
- FA Divinità avanzate (12, 15, 21, 28, 30)
- FA Partite multiple

## Quickstart
Prerequisites:
- Java 9 (suggested Java 11)
- Maven 3 (suggested Maven 3.6)

The latest version of the game can be downloaded from the release page of this repository.  
In order to play download the file `santorini.zip`, extract it in a directory, inside you should find 3 jars.

First start the server:
```shell script
$ java -jar server.jar
```
Now you can start the client:
```shell script
$ java -jar gui.jar
```
or if you want to use the cli version:
```shell script
$ java -jar cli.jar
```

### Build from source
You can build the game directly from the source.
Clone the repository and run:
```shell script
$ mvn clean package
```
you'll find the built jars inside the folder `deliveries/jars`.

### How to play
After connecting to the server, one player needs to create a new game indicating how many players will be participating.  
In order to join a game you'll need to know use the game id, which was communicated to the player that created the game.  




