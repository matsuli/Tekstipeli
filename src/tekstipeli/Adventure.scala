package tekstipeli

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer


class Adventure {

  /** The title of the adventure game. */
  val title = "21 turns"
    
  private val hallway       = new Area("Hallway", "The Hallway")
  private val livingroom    = new Area("Livingroom", "The Living Room")
  private val kitchen       = new Area("Kitchen", "The Kitchen")
  private val masterBedroom = new Area("First bedroom", "A Bedroom")
  private val bedroom       = new Area("Second bedroom", "A Bedroom")
  private val toilet        = new Area("Toilet", "A Toilet")
  private val bunkerArea    = new Area("Bunker", "The Bunker")
  private val destination   = bunkerArea
  
  private val cannedBeans   = new Item("Canned Beans", "A can of Beans", 0, true)
  private val waterBottle   = new Item("Water Bottle", "A bottle of Water", 0, true)
  private val axe           = new Item("Axe", "An Axe", 0, true)
  private val radio         = new Item("Radio", "A Radio", 0, true)
  private val gasMask       = new Item("Gas Mask", "A Gas Mask", 0, true)
  private val medKit        = new Item("Medkit", "A Medkit", 0, true)
  private val map           = new Item("Map", "A Map", 0, true)
  private val flashlight    = new Item("Flashlight", "A Flashlight", 0, true)
  private val bugSpray      = new Item("Bugspray", "A bottle of Bugspray", 0, true)
  private val playingCards  = new Item("Playingcards", "A deck of cards", 0, true)
  private val suitcase      = new Item("Suitcase", "A suitcase", 1, true)
  private val closet        = new Item("Closet", "An openable Closet", 3, false)
  private val smallcloset   = new Item("Small Closet", "An openable Closet", 2, false)
  private val door          = new Item("Door", "A ghost item", 0, false)
  
  private val peter         = new Human("Peter")
  private val georg         = new Human("Georg")
  private val mats          = new Human("Mats")


          hallway.setNeighbors(Vector("Livingroom"     -> livingroom     , "Toilet"         -> toilet                                                                                          ))
       livingroom.setNeighbors(Vector("Master Bedroom" -> masterBedroom  , "Bedroom"        -> bedroom, "Hallway" -> hallway, "Kitchen" -> kitchen, "Toilet" -> toilet, "Bunker" -> bunkerArea ))
          kitchen.setNeighbors(Vector("Livingroom"     -> livingroom                                                                                                                           ))
    masterBedroom.setNeighbors(Vector("Livingroom"     -> livingroom     , "Bedroom"        -> bedroom                                                                                         ))
          bedroom.setNeighbors(Vector("Livingroom"     -> livingroom     , "Master Bedroom" -> masterBedroom                                                                                   ))
           toilet.setNeighbors(Vector("Hallway"        -> hallway        , "Livingroom"     -> livingroom                                                                                      ))
        
           toilet.addHuman(mats)

    masterBedroom.addHuman(peter)
          kitchen.addHuman(georg)

  private val rooms = Map[String, Area](hallway.name -> hallway, livingroom.name -> livingroom, kitchen.name -> kitchen, masterBedroom.name -> masterBedroom, bedroom.name -> bedroom, toilet.name -> toilet)

  private val items = Buffer(cannedBeans, cannedBeans, cannedBeans, cannedBeans, cannedBeans, cannedBeans, waterBottle, waterBottle, waterBottle, waterBottle, waterBottle, waterBottle, axe, radio, medKit, medKit, map, flashlight, bugSpray, playingCards, closet, smallcloset, suitcase)
  private val allItems = Map[String, Item](cannedBeans.name -> cannedBeans, waterBottle.name -> waterBottle, axe.name -> axe, radio.name -> radio, gasMask.name -> gasMask,
                                medKit.name -> medKit, map.name -> map, flashlight.name -> flashlight, bugSpray.name -> bugSpray, playingCards.name -> playingCards, closet.name -> closet, door.name -> door)
  

  
  /** The character that the player controls in the game. */
  val player = new Player
  val house  = new House(rooms, hallway)
  val bunker = new Bunker(house.warnedHumans, bunkerArea.items, allItems)
  
  house.placeItems(items)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 21
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 40 


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = {
    house.location == this.destination &&
    (this.house.has("battery") && house.has("remote"))
    
  }

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */ 
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = {
    "You hear the radio " + (if(house.distanceToItem("Radio") == 0) "blaring a few feet away." else if(house.distanceToItem("Radio") == 1) "broadcast muffled nearby." else "buzzing far away.") + " 'bzrr' This is a w..rni.. message: Nuclear launch detected 'shr'. Get to a sh....." + 
    "\nYou couldn't catch the message completely, but the nuke seems to be heading your way. You better hurry up! Collect the items you need and head to the bunker. Oh, and maybe warn the others."
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
    val outcomeReport = if(turnCount < 1) {
      action.executeBunker(this.bunker)
    } else action.executeHouse(this.house)
    if (outcomeReport.isDefined) { 
      this.turnCount -= 1 
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }
  
  
}

