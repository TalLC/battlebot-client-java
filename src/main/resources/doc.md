# BattleBots Client Java

## Libriairie

La librairie *battlebots-clients-1.1.0.jar* contient l'ensemble du client Battlebots ainsi que l'ensemble des dépendances nécessaire au développement de votre bot

## Connexion au server
La connexion au server se fait via la classe *org.battlebot.config.Configuration*.
Le constructeur permet de paramétrer tous les accès aux différents serveurs (http, MQTT et Stomp).
Dans le cadre du **Tech & Chill**, nous n'auront besoin que de spécifier la localisation du serveur BattleBot :
```
Configuration cfg = new Configuration("127.0.0.1")
```

## Enregistrement de votre Bot
On utilise cette configuration pour lancer notre gestionnaire de jeux :
```
GameManager gameMgr = new GameManager(cfg);
```

On déclare ensuite son bot auprés du serveur
```
gameMgr.register("team-id-1", "Warlock");
```
- Le premier paramètre est le nom de votre équipe (ne pas le changer pour vos développements).
- Le deuxième est le nom de votre Bot (soyez créatif)
- La méthode renvoie un booléen : *true* si l'enregistrement s'est efféctué, *false* sinon.

## A l'attaque
On utilise la méthode *startGame* pour démarrer le jeux :
```
gameMgr.startGame(
			(BotConfiguration botConfig)-> { ... },
			(Object message, StatusMessageType type) -> { ... }, 
			(ScannerDetection detection) -> { ... });
```

### Interception des évènements du jeux
La méthode *statGame* prend 3 interfaces fonctionnelles en paramère,  :

1. *StartGameCallback*, dont la méthode *onStart* prend en paramètre la configuration de votre bot (vitesse de déplacement, vitesse de rotation, santé, ...) Elle permet d'initaliser votre bot au démarrage de la partie
    ```
    int health = botConfig.health();
    ```

2. *StatusListener*, dont la méthode *onStatusMessage* prend en paramètre un message de status et son type. Ce type est un enum *org.battlebot.connection.StatusMessageType* dont la méthode associatedClass(), vous donne la classe de l'objet passé en premier paramètre. Il permet de gérer l'état de votre Bot.
	
	Voici les correspondance type de message <=> classe de message :
        . **health_status** : *BotHealthMessage.class*, niveau de santé
		. **stunning_status** : *BotStunningStatusMessage.class*, paralisé ou non
		. **moving_status** : *BotMovingStatusMessage.class*, état de l'avancé' (on avance, arrété)
		. **turning_status** : *BotTurningStatusMessage.class*, état de la rotation (left, right, stop)
		. **weapon_can_shoot** : *BotWeaponStatusMessage.class*, état de l'arme (on peut tirer ou non)

    Pour tous ces messages *getData().getValue()* permet d'accéder à la valeur du status émis
    ```
    switch(type){
    	case stunning_status :
    		BotStunningStatusMessage msgS = (BotStunningStatusMessage)message;
    		LOGGER.info("Bot paralisé : " + msg.getData().getValue());
    		break;
    	case health_status : 
    		BotHealthMessage msgH = (BotHealthMessage)message;
    		LOGGER.info("Niveau de vie : " + msg.getData().getValue());
    		break;
    	...
    }
    ```
3. *ScannerDetectionListener*, dont la méthode *onScannerMessage* prend en paramètre le résultat de votre scanner *org.battlebot.client.message.mqtt.ScannerDetection*. On peut alors analyser tout ce que votre Bot "voit".
    ```
    LOGGER.info("Detect : " + detection.toString());
	detection.getData().forEach((ScannerDetection.Data itemDetected) -> {
		itemDetected.getFrom(); //angle de départ de détection
		itemDetected.getTo(); //angle de fin de détection
		itemDetected.getDistance(); //distance de l'item
		itemDetected.getObject_type(); //type de l'item
		itemDetected.getName(); //nom de l'item
	});
	```
	
### Piloter votre Bot
La classe *GameManager* vous permet d'envoyer des ordres à votre Bot
 ```
    gameMgr.actionTurnLeft() // on tourne vers la gauche
    gameMgr.actionTurnRight() //on tourne vers la droite
    gameMgr.actionStopTurn() //on arrete de tourner
    gameMgr.actionFire(23) //on tire avec un angle de 23°
    gameMgr.actionMoveForward() // on avance
    gameMgr.actionMoveStop() // on s'arrette
 ```

## Squelette de code initial
 ```
 public class Launcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);
	
	public static void main(String[] args) throws Exception {
		LOGGER.info("Démarrage du bot");
		Configuration cfg = new Configuration("localhost");
		final GameManager gameMgr = new GameManager(cfg);
		
		if(gameMgr.register("team-id-1", "Warlock")) {
			gameMgr.startGame(
			(BotConfiguration botConfig)-> {
				
			},
			(Object message,StatusMessageType type) -> {
				
			}, 
			(ScannerDetection detection) -> {
				
			});
		}
	}

}
```