package tekstipeli

import scala.collection.mutable.Map


class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private val items = Map[String, Item]()
   
  
  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the current location of the player. */
  def location = this.currentLocation
  
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation) 
    if (destination.isDefined) "You go to the " + direction + "." else "You can't go " + direction + "."
  }

  def rest() = {
    "You rest for a while. Better get a move on, though." 
  }
  
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


