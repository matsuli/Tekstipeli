package tekstipeli


class Human(val name: String) {

  private var hunger = 7
  private var thirst = 4
  var alive = true
  
  def advanceOneDay = {
    hunger -= 1
    thirst -= 1
    isDead
  }
  
  def isDead = if(hunger < 1 || thirst < 1) alive = false
  
  def eat = this.hunger = 7

  def drink = this.thirst = 4

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
  
  def report = hungerStatus + thirstStatus
  
  override def toString = this.name
}