package tekstipeli

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer
import scala.util.Random


class Event(val name: String, val description: String, val bunker: Bunker, private val usefullItems: Map[String, Item], private val potentialRewards: Map[String, Item]) {
  
  private val addedItems = Map[String, Item]()
  private var success = false
  
  def fullDescription = {
    def itemStatus = {
      var itemStatus = ""
      for(currentItem <- usefullItems.keys.toVector) {
      itemStatus += (if(bunker.items.contains(currentItem)) currentItem + "[ ]" else if (addedItems.contains(currentItem)) currentItem + "[v]" + ":" else currentItem + "[x]") + ":"
      }
      itemStatus.split(":").mkString(", ")
    }
    val requiredItems = "\nUsefull items: " + itemStatus
    this.description + requiredItems  
  }
  
  def addItem(item: Item) = {
    if(this.usefullItems.contains(item.name)) {
      this.addedItems += item.name -> item
      "You used " + item.name + "."
    } else "That won't be of any help"
  }
  
  def completionStatus = {
    if(this.addedItems.size == this.usefullItems.size) {
      success = true
    } else if (0 < this.addedItems.size && this.addedItems.size < this.usefullItems.size) {
      val randomSeed = new Random
      val roll = randomSeed.nextInt(2)
      if(roll == 0) success = true else if(roll == 1) success = false
    }
  }
  
  def addRewards = {
    if(success) {
      for(item <- this.potentialRewards.values) {
        if(this.bunker.items.contains(item.name)) {
          this.bunker.items(item.name) +=  item
        } else this.bunker.items += item.name -> Buffer(item)
      }
    }
  }
  
  
  
  
  
  override def toString = this.description
  
}