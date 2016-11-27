package tekstipeli

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer
import scala.util.Random


class Event(val name: String, val description: String, val bunker: Bunker, val usefullItems: Buffer[Item], private val potentialRewards: Buffer[Item], val outcomeSuccess: String, val outcomeFail: String) {
  
  private val addedItems = Map[String, Item]()
  private var success = false
  
  def fullDescription = {
    def itemStatus = {
      var itemStatus = ""
      for(currentItem <- usefullItems) {
      itemStatus += (if(bunker.depositedItems.contains(currentItem.name)) currentItem + "[ ]" else if (addedItems.contains(currentItem.name)) currentItem + "[v]" + ":" else currentItem + "[x]") + ":"
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
      for(item <- this.potentialRewards) {
        if(this.bunker.depositedItems.contains(item.name)) {
          this.bunker.depositedItems(item.name) +=  item
        } else this.bunker.depositedItems += item.name -> Buffer(item)
      }
    }
  }
  
  def outcome = {
    
    if(success) outcomeSuccess else outcomeFail
    
  }
  
  
  
  override def toString = this.description
  
}