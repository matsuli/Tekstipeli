package tekstipeli
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

class Adventure {

  /** The title of the adventure game. */
  val title = "60 seconds"
    
  private val hallway     = new Area("Hallway", "The Hallway")
  private val livingroom  = new Area("Livingroom", "The Living Room")
  private val kitchen     = new Area("Kitchen", "The Kitchen")
  private val bedroom1    = new Area("First bedroom", "A Bedroom")
  private val bedroom2    = new Area("Second bedroom", "A Bedroom")
  private val toilet      = new Area("Toilet", "A Toilet")
  private val destination = toilet
  
  private val cannedBeans  = new Item("Canned Beans", "A can of Beans", false)
  private val waterBottle  = new Item("Water Bottle", "A bottle of Water", false)
  private val axe          = new Item("Axe", "An Axe", false)
  private val radio        = new Item("Radio", "A Radio", false)
  private val gasMask      = new Item("Gas Mask", "A Gas Mask", false)
  private val medKit       = new Item("Medkit", "A Medkit", false)
  private val map          = new Item("Map", "A Map", false)
  private val flashlight   = new Item("Flashlight", "A Flashlight", false)
  private val bugSpray     = new Item("Bugspray", "A bottle of Bugspray", false)
  private val playingCards = new Item("Playingcards", "A deck of cards", false)
  
  private val peter        = new Human("Peter")
  private val georg        = new Human("Georg")
  private val mats         = new Human("mats")

     hallway.setNeighbors(Vector("livingroom"  -> livingroom,   "toilet" -> toilet                                                                  ))
  livingroom.setNeighbors(Vector(   "bedroom1" -> bedroom1,   "bedroom2" -> bedroom2, "hallway" -> hallway, "kitchen" -> kitchen, "toilet" -> toilet))
     kitchen.setNeighbors(Vector("livingroom"  -> livingroom                                                                                        ))
    bedroom1.setNeighbors(Vector("livingroom"  -> livingroom, "bedroom2" -> bedroom2                                                                ))
    bedroom2.setNeighbors(Vector("livingroom"  -> livingroom, "bedroom1" -> bedroom1                                                                ))
      toilet.setNeighbors(Vector(    "hallway" -> hallway,  "livingroom" -> livingroom                                                              ))
        
        
     hallway.setNeighbors(Vector("livingroom" -> livingroom,   "toilet" -> toilet                                                                  ))
  livingroom.setNeighbors(Vector(   "bedroom1" -> bedroom1,   "bedroom2" -> bedroom2, "hallway" -> hallway, "kitchen" -> kitchen, "toilet" -> toilet))
     kitchen.setNeighbors(Vector("livingroom" -> livingroom                                                                                        ))
    bedroom1.setNeighbors(Vector("livingroom" -> livingroom, "bedroom2" -> bedroom2                                                                ))
    bedroom2.setNeighbors(Vector("livingroom" -> livingroom, "bedroom1" -> bedroom1                                                                ))
      toilet.setNeighbors(Vector(    "hallway" -> hallway,  "livingroom" -> livingroom                                                              ))
        
      toilet.addHuman(mats)
    bedroom1.addHuman(peter)
     kitchen.addHuman(georg)
      
  private val rooms = Map[String, Area](hallway.name -> hallway, livingroom.name -> livingroom, kitchen.name -> kitchen, bedroom1.name -> bedroom1, bedroom2.name -> bedroom2, toilet.name -> toilet)
  private val items = Buffer(cannedBeans, cannedBeans, cannedBeans, cannedBeans, cannedBeans, waterBottle, axe, radio, medKit, map, flashlight, bugSpray, playingCards)
 
  /** The character that the player controls in the game. */
  val player = new Player(hallway)
  
  val house = new House(rooms, player)
  val bunker = new Bunker(player.warnedHumans)
  
  house.placeItems(items)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 40 


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = {
    this.player.location == this.destination &&
    (this.player.has("battery") && this.player.has("remote"))
    
  }

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */ 
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = {
    (if(house.distanceToItem("Radio") == 0) "pleb"
    else if(house.distanceToItem("Radio") == 1) "beep boop"
    else if(house.distanceToItem("Radio") == 2) "bööp beep"
    else "wqr") +
    "\nThe nuke is coming! Quick, you have 60 seconds to take everything you need!"
  }

    
  /** Returns a message that is to be displayed to the player at the end of the game. The message 
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete)
      "Home at last... and phew, just in time! Well done!"
    else if (this.turnCount == this.timeLimit)
      "Oh no! Time's up. Starved of entertainment, you collapse and weep like a child.\nGame over!"
    else  // game over due to player quitting
      "Quitter!" 
  }

  
  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual 
    * report of what happened, or an error message if the command was unknown. In the latter 
    * case, no turns elapse. */
  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) { 
      this.turnCount += 1 
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }
  
  
}

