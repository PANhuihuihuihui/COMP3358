## About
This is a game project built on java. **RMI** is used to support the communication between the server and clients. Login, registration and logout function is provided through RMI. **JMS** is used to support other message delivering and communication between clients and the server. It is asynchronous and safer. **JDBC** and MySQL is used to store the game data. it get start as soon as 4 online player arrive.

### How to run

You should need 3 setup. There are JMS RMI JDBN
1. RMI
run the rmiregistry in the working directory
2. JMS
using the glassfish4 to register JMS connection factory, Topic, Queue
3. JDBC
open the local mysql server and input the follow command 
```SQL
CREATE DATABASE 24Game;
GRANT ALL ON c3358.* to 'c3358'@'localhost'; 
CREATE TABLE 24Game_UserList (
    username varchar(16) NOT NULL,
    password varchar(32) NOT NULL,
    win_number INT(5),
    game_number INT(5),
    vg_time FLOAT(10),
    PRIMARY KEY username (username)
);
```
4. compile the file
for server
```sh
/usr/bin/env /usr/lib/jvm/java-1.8.0-openjdk-amd64/bin/java -Djava.security.policy=./security.policy - Dfile.encoding=UTF-8 -cp GameServer
```
for the client
```sh
cd software/Gminer/cd /home/phj/Downloads/test/JPoker_24_Game ; 
/usr/bin/env /usr/lib/jvm/java- 1.8.0-openjdk-amd64/bin/java -Djava.security.policy=./security.policy -Dfile.encoding=UTF-8 -cp /tmp/cp_5odlhhubhghrelj2aapw57dyi.jar GameGUI 127.0.0.1
```
add the reference library into your IDE
![](https://raw.githubusercontent.com/PANhuihuihuihui/PicBed/main/202209120014814.png)


### Appearance
![](https://raw.githubusercontent.com/PANhuihuihuihui/PicBed/main/202209120016459.png)
![](https://raw.githubusercontent.com/PANhuihuihuihui/PicBed/main/202209120017464.png)