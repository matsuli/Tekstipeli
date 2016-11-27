package tekstipeli

import scala.collection.mutable.Map


class Item(val name: String, val description: String, private val capacity: Int, private val pickUp: Boolean) {
  
  val contains = Map[String, Item]()
  
  def canPickUp = this.pickUp
  
  def itemsInside = this.contains.keys.toVector
  
  def fullness = this.contains.size
  
  def isFull = fullness >= this.capacity
  
  def isOpenable = this.capacity > 0
  
  def addItemInside(item: Item) = {
    this.contains += item.name -> item
  }
  
  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name
  
  
}