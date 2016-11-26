package tekstipeli

import scala.collection.mutable.Map


class Item(val name: String, val description: String, val contain: Boolean) {
  
  private val contains = Map[String, Item]()
  private val capacity = if(isOpenable) 2 else 0
  
  def isOpenable = this.contain
  
  def addItemInside(item: Item) = {
    this.contains += item.name -> item
  }
  
  
  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name
  
  
}