package tekstipeli

class Item(val name: String, val description: String, val openable: Boolean) {
  
  private val items = Map[String, Item]
  
  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name
  
  
}