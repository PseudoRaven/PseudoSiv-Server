# PseudoSiv-Server
RS3 876 Nocturne Source
This directory contains the source project for the Runescape 3 876 Nocturne server. In terms of searching online for help, this is the source and the cache. The cache is located in data/cache.

## Setup(Localhost)
1. Download this project.
2. Open the project in an [IDE](https://www.eclipse.org/downloads/)
3. Navigate to src/net/nocturne/Settings.java
4. On line 21, change `case "nocturne":` to the host name of your Computer/Server/VPS. Ex. `case "PseudoRaven-PC":`
5. On line 34, change the HOSTED IP address to `127.0.0.1` Ex. `VPS1_IP = HOSTED ? "127.0.0.1" : "127.0.0.1";`
6. On line 44, add your preferred username to the `SERVER_OWNERS` ArrayList. Ex. `add("PseudoRaven");`
7. On line 178, change the `GAME_ADDRESS_BASE` IP address to `127.0.0.1`. Ex. `Settings.HOSTED ? "127.0.0.1" : "127.0.0.1", 43593);`
8. On line 190, change the `LOGIN_SERVER_ADDRESS_BASE` IP address to `127.0.0.1` Ex. `HOSTED ? "127.0.0.1" : "127.0.0.1", 7777);`
9. On line 192, change the `LOGIN_CLIENT_ADDRESS_BASE` IP address to `127.0.0.1` Ex. `HOSTED ? "10.132.0.2" : "127.0.0.1", 7778);`
10. On line 196, change the `WorldInformation` IP address for `World1` to `127.0.0.1` Ex. `new WorldInformation(1, 0, "World1", 0, 0x2 | 0x8, "Nocturne :D", "127.0.0.1", 100)`

## Setup(Hosted/Public)
1. Download this project.
2. Open the project in an [IDE](https://www.eclipse.org/downloads/)
3. Navigate to src/net/nocturne/Settings.java
4. On line 21, change `case "nocturne":` to the host name of your Computer/Server/VPS. Ex. `case "PseudoRaven-PC":`
5. On line 34, change the HOSTED IP address to your local IP address. Ex. `VPS1_IP = HOSTED ? "192.168.0.50" : "127.0.0.1";`
6. On line 44, add your preferred username to the `SERVER_OWNERS` ArrayList. Ex. `add("PseudoRaven");`
7. On line 178, change the `GAME_ADDRESS_BASE` IP address to your local IP address. Ex. `Settings.HOSTED ? "192.168.0.50" : "127.0.0.1", 43593);`
8. On line 190, change the `LOGIN_SERVER_ADDRESS_BASE` IP address to the local IP address of your server. Ex. `HOSTED ? "192.168.0.50" : "127.0.0.1", 7777);`
9. On line 192, change the `LOGIN_CLIENT_ADDRESS_BASE` IP address to the local IP address of your server. Ex. `HOSTED ? "192.168.0.50" : "127.0.0.1", 7778);`
10. On line 196, change the `WorldInformation` IP address for `World1` to the local IP address of your server. Ex. `new WorldInformation(1, 0, "World1", 0, 0x2 | 0x8, "Nocturne :D", "192.168.0.50", 100)`

## Starting the server
The main class of the server package is `src.net.nocturne.Engine`
1. Open the project in the Eclipse IDE
2. Navigate to `src.net.nocturne.Engine.java`
3. Right click on `Engine.java` in the navigation pane
4. Click `Run As > 1 Java Application`
5. Watch the Console pane for init status and errors. When `[Login server] Login Engine connected.` appears in the console, the server has finished its start up sequence.


## Commands:
All commands are made in the chat box and must be preceeded by ;; for example, ";;item 123456 1" would give the player 1 item of id 123456

### item itemID Qty
  Item IDs can be found in "information/itemList.txt"
  
**Example:**";;item 1127 1" will give the player 1 Rune Platebody. Many items will have 2 entrys in the list. The first one is the item and the second is the item in noted form. Noted form makes the item unusable but stackable. The noted items can be exchanged at a bank for the real item.
  


## Links
https://22i.github.io/how.to.run.latest.nocturne.876.html
