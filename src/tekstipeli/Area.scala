package tekstipeli

import scala.collection.mutable.Map


class Area(var name: String, var description: String) {
  
  private val neighbors = Map[String, Area]()
  private val items = Map[String, Item]()
  
  def neighboringAreas = this.neighbors.values.toVector
  
  def neighbor(direction: String) = this.neighbors.get(direction)

  def setNeighbor(direction: String, neighbor: Area) = {
    this.neighbors += direction -> neighbor
  }

  def setNeighbors(rooms: Vector[(String, Area)]) = {
    this.neighbors ++= rooms
  }

  def fullDescription = {
    val houseRoomList = "\n\nRooms nearby: " + this.neighbors.keys.mkString(" ")
    val itemList = "\nYou see here: " + this.items.values.mkString(" ")
    this.description + (if(!this.items.isEmpty) itemList else "") +  houseRoomList
  }
  
  def addItem(item: Item) = {
    this.items += item.name -> item
  }
  
  def removeItem(itemName: String) = this.items.remove(itemName)
  
  def contains(itemName: String) = this.items.contains(itemName)
  
  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)

  
  
}