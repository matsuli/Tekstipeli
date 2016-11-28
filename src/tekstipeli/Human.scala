package tekstipeli


class Human(val name: String) {

  private var hunger = 7
  private var thirst = 4
  private var insanity = 10
  private var injured = false
  var daysLeftOfExpedition = 0
  private var alive = true
  
  def advanceOneDay = {
    hunger -= 1
    thirst -= 1
    if (onExpedition) daysLeftOfExpedition -= 1
  }
  
  def eat = {
    //if(item.name == "Canned Beans") {
      this.hunger = 7
    //} else "You can't eat " + item.name + "!"
  }
  
  def drink = {
    //if (item.name == "water") {
      this.thirst = 4
   //   this.name + "drank the water and feels refreshed!"
    //} else "You can't drink that!"
  }
  
  def heal(item: Item) = {
    null
  }
  
  
  def health = {
    if (injured) this.name + "got a cut in his hand and that should be treated" else ""
    
  }
  
  def hungerStatus: String = {
    if (hunger == 7) ""
    else if (hunger > 4) this.name + " is getting hungry. " 
    else if (hunger > 1) this.name + " is very hungry! "
    else if (hunger == 1) this.name + " needs food now!! "
    else ""
  }
  
  def thirstStatus: String = {
    if (thirst == 4) ""
    else if (thirst > 2) this.name + " is getting thirsty " 
    else if (thirst > 1) this.name + " is very thirsty! "
    else if (thirst == 1)this.name + " needs water now!! "
    else ""
  }
  
  def onExpedition = if (daysLeftOfExpedition > 0) true else false
  
  def report = health + hungerStatus + thirstStatus
  
  override def toString = this.name
}