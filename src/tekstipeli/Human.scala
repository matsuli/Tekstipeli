package tekstipeli


class Human(val name: String, val description: String) {

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
    else if (hunger > 4) "\n" + this.name + " is getting hungry. " 
    else if (hunger > 1) "\n" + this.name + "'s stomach is grumbling violently. "
    else if (hunger == 1) "\n" + this.name + " really needs some food. NOW. "
    else ""
  }
  
  def thirstStatus: String = {
         if (thirst == 4) ""
    else if (thirst > 2) this.name + " is getting thirsty." 
    else if (thirst > 1) this.name + " 's throat is quite dry."
    else if (thirst == 1)this.name + " won't last any longer without water!"
    else ""
  }
  
  def report = hungerStatus + thirstStatus
  
  override def toString = this.name
}