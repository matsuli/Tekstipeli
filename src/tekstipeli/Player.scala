package tekstipeli

import scala.collection.mutable.Map

  
/** A `Player` object represents a player character controlled by the real-life user of the program. 
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private val items = Map[String, Item]()
   
  
  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  
  /** Returns the current location of the player. */
  def location = this.currentLocation
  

  /** Attempts to move the player in the given direction. This is successful if there 
    * is an exit from the player's current location towards the direction name. 
    * Returns a description of the results of the attempt. */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation) 
    if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
  }

  
  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() = {
    "You rest for a while. Better get a move on, though." 
  }
  
  
  /** Signals that the player wants to quit the game. Returns a description of what happened within 
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }
  
  def get(itemName: String) = {
    if(currentLocation.contains(itemName)) {
      this.items += itemName -> this.currentLocation.removeItem(itemName).get
      currentLocation.removeItem(itemName)
      "You pick up the " + itemName + "."
    } else "There is no " + itemName + " here to pick up."
  }
  
  def drop(itemName: String) = {
    if(this.has(itemName)) {
      this.currentLocation.addItem(this.items(itemName))
      this.items -= itemName
      "You drop the " + itemName + "."
    } else "You don't have that!"
  }
  
  def examine(itemName: String): String = {
    if(this.has(itemName)) {
      if(itemName == "remote") {
        "You look closely at the remote.\nIt's the remote control for your TV.\nWhat it was doing in the forest, you have no idea.\nProblem is, there's no battery."
      } else if(itemName == "battery") {
        "You look closely at the battery.\nIt's a small battery cell. Looks new."
      } else ""
    } else "If you want to examine something, you need to pick it up first."
  }
  
  def has(itemName: String) = this.items.contains(itemName)
  
  def inventory: String = {
    if(this.items.nonEmpty) {
      var currentItems = ""
      for(pair <- this.items) {
        currentItems += pair._1 + "\n"
      }
      "You are carrying:\n" + currentItems
    } else "You are empty-handed."
  }

  
  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name   


}


