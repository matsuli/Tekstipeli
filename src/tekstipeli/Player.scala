package tekstipeli

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer


class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private val carrying = Map[String, Buffer[Item]]()
  val warnedHumans = Map[String, Human]()
  
  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the current location of the player. */
  def location = this.currentLocation
  
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation) 
    if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
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
      if(this.carrying.contains(itemName)) {
        this.carrying(itemName) += this.currentLocation.removeItem(itemName).get
      } else this.carrying += itemName -> Buffer(this.currentLocation.removeItem(itemName).get)
      "You pick up the " + itemName + "."
    } else "There is no " + itemName + " here to pick up."
  }
  
  def open(itemName: String) = {
    if(this.currentLocation.contains(itemName)) {
      val itemToOpen = currentLocation.items(itemName)(0)
      if(itemToOpen.isOpenable) {
      val itemsInside = itemToOpen.contains.values.toBuffer
      for(currentItem <- itemsInside.indices) {
        this.currentLocation.addItem(itemsInside(currentItem))
        itemToOpen.contains -= itemsInside(currentItem).name
      }
      currentLocation.removeItem(itemName)
      "You open the " + itemName + ". Out falls some items."
    } else "You can't open that!"
    } else "I can't see " + itemName + "here."
  }

  def drop(itemName: String) = {
    if(this.has(itemName)) {
      this.currentLocation.addItem(this.carrying(itemName)(0))
      if(this.carrying(itemName).size == 1) this.carrying -= itemName else this.carrying(itemName) -= this.carrying(itemName)(0)
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
  
  def has(itemName: String) = this.carrying.contains(itemName)
  
  def inventory: String = {
    if(this.carrying.nonEmpty) {
      var currentItems = ""
      for(pair <- this.carrying) {
        currentItems += pair._1 + "\n"
      }
      "You are carrying:\n" + currentItems
    } else "You are empty-handed."
  }

  def warn(human: String) = {
    if(currentLocation.containsHuman(human)) {
      this.warnedHumans += human -> this.currentLocation.removeHuman(human).get
      "You warn " + human + ", he runs to the bunker."
    } else human + " is not here!" 
  }
  
  
  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name   


}


