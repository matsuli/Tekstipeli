package tekstipeli

class Item(val name: String, val description: String, val contain: Boolean) {
  
  def isOpenable = this.contain
  
  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name
  
  
}