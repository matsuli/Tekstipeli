package tekstipeli

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer


class Area(var name: String, var description: String) {
  
  private val neighbors = Map[String, Area]()
  private val items = Map[String, Buffer[Item]]()
  private val humans = Map[String, Human]()
  
  def neighboringAreas = this.neighbors.values.toVector
  
  def neighbor(direction: String) = this.neighbors.get(direction)

  def setNeighbor(direction: String, neighbor: Area) = {
    this.neighbors += direction -> neighbor
  }

  def setNeighbors(neighbors: Vector[(String, Area)]) = {
    this.neighbors ++= neighbors
  }

  def fullDescription = {
    var itemString = ""
    val houseRoomList = "\n\nRooms nearby: " + this.neighbors.keys.mkString(", ")
    val humanList = "\nHumans in the room: " + this.humans.values.mkString(", ")
    for(currentBuffer <- this.items.values) {
      for(currentItem <- 0 until currentBuffer.size) {
        itemString += currentBuffer(currentItem).name + ":"
      }
    }
    val itemList = "\nYou see here: " + itemString.split(":").mkString(", ")
    this.description + (if(!this.items.isEmpty) itemList else "") + (if(!this.humans.isEmpty) humanList else "") + houseRoomList
  }
  
  def addItem(item: Item) = {
    if(this.items.contains(item.name)) this.items(item.name) += item else this.items += item.name -> Buffer(item)
  }
  
  def addHuman(human: Human) = {
    this.humans += human.name -> human
  }
  
  def removeHuman(humanName: String) = this.humans.remove(humanName)
  
  def removeItem(itemName: String): Option[Item] = {
    def deleteItem = {
      val removedItem = this.items(itemName)(0)
      this.items(itemName) -= removedItem
      Some(removedItem)
    }
    if(this.items(itemName).size == 1) {
      val itemInsideBuffer = this.items(itemName)(0)
      this.items -= itemName
      Some(itemInsideBuffer)
    } else deleteItem
    //this.items.remove(itemName)
  }
  
  def contains(itemName: String) = this.items.contains(itemName)
  
  def containsHuman(humanName: String) = this.humans.contains(humanName)
  
  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)

  
  
}