package tekstipeli

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer
import scala.util.Random



class Event(val name: String, val description: String, val bunker: Bunker, val usefullItems: Buffer[Item], private val potentialRewards: Buffer[Item], val outcomeSuccess: String, val outcomeFail: String, val outcomeAlmost: String) {

  
  private val addedItems = Map[String, Item]()
  private var almost = false
  var success = false
  
  //returns a description if a item is added to the event
  def itemStatus = {
    var itemStatus = ""
    for(currentItem <- usefullItems) {
    itemStatus += (if(bunker.depositedItems.contains(currentItem.name)) currentItem + "[ ]" 
                   else if (addedItems.contains(currentItem.name) && currentItem.name != "Door") currentItem + "[v]" + ":" 
                   else if(currentItem.name == "Door" ) "" 
                   else currentItem + "[x]") + ":"
    }
    itemStatus.split(":").mkString(", ")
  }
  
  //adds the specific item to the event
  def addItem(item: Item) = {
    if(this.usefullItems.contains(item)) {
      this.addedItems += item.name -> item
      if(item.name == "Door") "" else "You used " + item.name + "."
    } else {
      "That won't be of any help."
    }
  }
  
  //checks if the event is successfull
  def completionStatus = {
    if(this.addedItems.contains("Door") && this.addedItems.size == this.usefullItems.size) {
      success = true
    } else if(this.addedItems.contains("Door") && (this.addedItems.size < this.usefullItems.size)) {
      almost = true
    } else if(this.addedItems.size == this.usefullItems.size) {
      success = true
    } else if (0 < this.addedItems.size && this.addedItems.size < this.usefullItems.size) {
      val randomSeed = new Random
      val roll = randomSeed.nextInt(2)
      if(roll == 0) success = true else if(roll == 1) success = false
    }
  }
  
  //adds rewards to the bunkers inventory
  def addRewards = {
    if(success == true) {
      for(item <- this.potentialRewards) {
        if(this.bunker.depositedItems.contains(item.name)) {
          this.bunker.depositedItems(item.name) +=  item
        } else this.bunker.depositedItems += item.name -> Buffer(item)
      }
    }
  }
  
  //tells the outcome of a event
  def outcome = if(success) outcomeSuccess else if(almost) outcomeAlmost else outcomeFail

  override def toString = this.description
  
}