package tekstipeli

/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect, 
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as "go east" or "rest" */
class Action(input: String) {

  private val commandText = input.trim.toLowerCase
          val verb        = commandText.takeWhile( _ != ' ' ).toLowerCase
  private val modifiers   = commandText.drop(verb.length).trim.toLowerCase.split(' ').map( _.capitalize ).mkString(" ")

  
  /** Causes the given player to take the action represented by this object, assuming 
    * that the command was understood. Returns a description of what happened as a result 
    * of the action (such as "You go west."). The description is returned in an `Option` 
    * wrapper; if the command was not recognized, `None` is returned. */
  
  def execute(actor: Player) = {
    if (this.verb == "quit") {
      Some(actor.quit())
    } else {
      None
    }
  }
  
  def executeHouse(actor: House) = {                             
    if (this.verb == "go") {
      Some(actor.go(this.modifiers))
    } else if (this.verb == "get" || this.verb == "take") {
      Some(actor.get(this.modifiers))
    } else if (this.verb == "open") {
      Some(actor.open(this.modifiers))
    } else if (this.verb == "drop") {
      Some(actor.drop(this.modifiers))
    } else if (this.verb == "deposit") {
      Some(actor.deposit)
    } else if (this.verb == "inventory") {
      Some(actor.inventory)
    } else if (this.verb == "warn") {
      Some(actor.warn(this.modifiers))
    } else if (this.verb == "help") {
      Some(actor.help)
    } else {
      None
    }
  }
  
  def executeBunker(actor: Bunker) = {                             
    if (this.verb == "next") {
      Some(actor.advanceOneDay)
    } else if (this.verb == "use") {
      Some(actor.useItem(this.modifiers))
    } else if (this.verb == "open") {
      Some(actor.useItem(this.modifiers))
    } else if (this.verb == "feed") {
      Some(actor.feed(this.modifiers))
    } else if (this.verb == "give") {
      Some(actor.give(this.modifiers))
    } else if (this.verb == "inventory") {
      Some(actor.inventory)
    } else if (this.verb == "help") {
      Some(actor.help)
    } else {
      None
    }
  }


  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"  

  
}

