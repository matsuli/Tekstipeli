package tekstipeli

class Human(val name: String) {
  println("haloj")
  var hunger = 7
  var thirst = 4
  var insanity = 10
  
  def eat(item: Item) = {
    this.hunger = 7
  }
  
  def drink(item: Item) = {
    this.thirst = 4
  }
  
  def heal(item: Item) = {
    null
  }
  
  def advanceDay = {
    hunger -= 1
    thirst -= 1
  }
  
  
  var hungerStatus: String = {
    
    if (hunger == 7) ""
    else if (hunger > 4) this.name + "is getting hungry" 
    else if (hunger > 1) this.name + "is very hungry!"
    else if (hunger == 1) this.name + "needs food now!!"
    else ""
  }
  
  var thirstStatus: String = {
    
    if (thirst == 4) ""
    else if (thirst > 2) this.name + "is getting thirsty" 
    else if (thirst > 1) this.name + "is very thirsty!"
    else if (thirst == 1)this.name + "needs water now!!"
    else ""
  }
  
  override def toString = hungerStatus + thirstStatus
  
}