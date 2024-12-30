# RobotWars

RobotWars is a multiplayer game where two or more robots can battle each other in a 
shared world. RobotWars utilizes a client/server architecture where the server is
responsible for the game logic and the client is responsible for user input.

---

## Dependencies

RobotWars relies on the following non-standard dependencies to function:
* org.json.json
* de.vandemeer.asciitable
* de.gurkenlabs.litiengine

These dependencies are automatically included with the RobotWars .jar after it is packaged
with maven.

---

## Usage

RobotWars is built upon Java21 and requires it to function.
Please ensure that your device is currently running Java21 with the following command:

```bash
java --version
```

#### Build From Source

RobotWars can be compiled from source into a portable .jar using the maven package lifecycle.

Please ensure that you have maven installed with the following command:

```bash
mvn --version
```

In order to run RobotWars, please use the following commands:

###### Clone:
```bash
git clone github.com/Monnelisa/Robot_Wars.git
```

###### Change Directory:
```bash
cd cpt3_robot_wars
```

###### Build jar with maven:
```bash
mvn clean package
```

###### Change directory:
```bash
cd dist
```

#### Download Executable

We have a gitlab pipeline that will make available just the exectuable .jar should you
not wish to build from source yourself.

You can have an executable built and downloaded by doing the following on gitlab:

* Navigate to the [project](https://github.com/Monnelisa/Robot_Wars.git)

* Build -> Pipelines -> Run Pipeline

After the pipeline has done its' work:

* Build -> Artifacts -> Download from latest build

* Unzip artifacts.zip and navigate into the dist folder

---

###### Execute programme:
```bash
java -jar RobotWars-0.1.jar [Server IP] [Server Port]
```

If you would like to run the client with its' GUI, you can use this command:

```bash
java -jar RobotWars-0.1.jar [Server IP] [Server Port] -g
```
