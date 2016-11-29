package tekstipeli

import tekstipeli.Player._
import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random


class House(val rooms: Map[String, Area], startingArea: Area) {
  
  private var currentLocation = startingArea
  private val carrying = Map[String, Buffer[Item]]()
  private val inventoryLimit = 4
  val warnedHumans = Map[String, Human]()
  
  def help: String = "Pick up items and deposit them to the bunker. Also warn the others. Usable commands: get/take, go, open, drop, deposit, inventory, warn"
  
  def distanceToItem(item: String): Int = {
    var distance = 1
    var currentArea = Vector(location)
    if(location.contains(item)) {
        distance -= 1
    } else {
      for(currentRoom <- 0 until currentArea.size) {
        if(currentArea(currentRoom).neighboringAreas.exists( _.contains(item) )) {
          return distance
        } else {
        distance += 1
        currentArea = currentArea.flatMap( _.neighboringAreas )
        }
      }
    }
    distance
  }
  
  def placeItems(items: Buffer[Item]) = {
    val randomSeed = new Random
    val randomizedItems = randomSeed.shuffle(items)
    val roomCollection = this.rooms.values.toVector
    var n = 0
    while(randomizedItems.nonEmpty) {
      if(randomizedItems(0).isOpenable) {
        while(!randomizedItems(0).isFull && randomizedItems.size >= 2) {
          randomizedItems(0).addItemInside(randomizedItems(1))
          randomizedItems -= randomizedItems(1)
        }
      }
      roomCollection(n).addItem(randomizedItems(0))
      randomizedItems -= randomizedItems(0)
      n = (n + 1) % roomCollection.size
    }
  }
  
  //Methods the player can call in the house.
  
  def location = this.currentLocation
  
  def has(itemName: String) = this.carrying.contains(itemName)
  
  def go(direction: String) = {
    if(direction == "Bunker") {
      "You shouldn't enter the bunker yet. There's plenty of time left! Don't forget your suitcase. You wouldn't fight a real apocalypse without it, would you?"
    } else {
      val destination = this.location.neighbor(direction)
      this.currentLocation = destination.getOrElse(this.currentLocation) 
      if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
    }
  }
  
  def drop(itemName: String) = {
    if(this.has(itemName)) {
      this.currentLocation.addItem(this.carrying(itemName)(0))
      if(this.carrying(itemName).size == 1) this.carrying -= itemName else this.carrying(itemName) -= this.carrying(itemName)(0)
      "You drop the " + itemName + "."
    } else "You don't have that!"
  }
  
  def get(itemName: String) = {
    if(this.currentLocation.contains(itemName)) {
      if(this.carrying.values.flatten.size < this.inventoryLimit && this.currentLocation.items(itemName)(0).canPickUp) {
        if(this.carrying.contains(itemName)) {
          this.carrying(itemName) += this.currentLocation.removeItem(itemName).get
        } else this.carrying += itemName -> Buffer(this.currentLocation.removeItem(itemName).get)
        "You pick up the " + itemName + "."
      } else if(this.carrying.values.flatten.size >= this.inventoryLimit) "You can't carry anymore items."
        else if(!this.currentLocation.items(itemName)(0).canPickUp) "You can't pick up that!"
        else ""
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
    } else "I can't see " + itemName + " here."
  }
  
  def warn(human: String) = {
    if(currentLocation.containsHuman(human)) {
      this.warnedHumans += human -> this.currentLocation.removeHuman(human).get
      "You warn " + human + ", he runs to the bunker."
    } else human + " is not here!" 
  }
  
  def deposit = {
    if(this.currentLocation.name == "Livingroom") {
      if(this.carrying.nonEmpty) {
        val bunker = this.currentLocation.neighbors("Bunker")
        val depositedItems = Buffer[Item]()
        for(item <- this.carrying.values.flatten) {
          if(bunker.contains(item.name)) {
            bunker.items(item.name) += item
          } else bunker.items += item.name -> Buffer(item)
          if(this.carrying(item.name).size == 1) this.carrying -= item.name else this.carrying(item.name) -= this.carrying(item.name)(0)
          depositedItems += item
        }
        "You deposited: " + depositedItems.mkString(", ")
      } else "Try picking up some items first"
    } else "You need to be near the bunker to deposit items"
  }
  
  def inventory: String = Player.inventory(this.carrying, "You are carrying:\n", "You are empty-handed.")

}