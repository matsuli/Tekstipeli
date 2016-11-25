package tekstipeli

class Human(val name: String) {
  
  var hunger = 7
  var thirst = 4
  var insanity = 10
  
  def eat(item: Item) = {
    if(item.name == "cannedBeans") {
      this.hunger = 7
      this.name + "ate the can of beans and is satisfied"
    } else "You can't eat " + item.name + "!"
  }
  
  def drink(item: Item) = {
    if (item.name == "water") {
      this.thirst = 4
      this.name + "drank the water and feels refreshed!"
    } else "You can't drink that!"
  }
  
  def heal(item: Item) = {
    null
  }
  
  def advanceDay = {
    hunger -= 1
    thirst -= 1
  }
  
  
  def hungerStatus: String = {
    
    if (hunger == 7) ""
    else if (hunger > 4) this.name + "is getting hungry" 
    else if (hunger > 1) this.name + "is very hungry!"
    else if (hunger == 1) this.name + "needs food now!!"
    else ""
  }
  
  def thirstStatus: String = {
    
    if (thirst == 4) ""
    else if (thirst > 2) this.name + "is getting thirsty" 
    else if (thirst > 1) this.name + "is very thirsty!"
    else if (thirst == 1)this.name + "needs water now!!"
    else ""
  }
  
  def report = hungerStatus + thirstStatus
  
  
}