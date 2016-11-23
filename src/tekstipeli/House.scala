package tekstipeli

class House(val rooms: Map[String, Area], val player: Player) {
  
  def distanceToItem(item: String): Int = {
    var distance = 0
    var currentArea = Vector(this.player.location)
    for(currentRoom <- 0 until currentArea.size) {
      if(currentArea(currentRoom).neighboringAreas.exists( _.contains(item) )) { 
        return distance
      } else {
        distance += 1
        currentArea = currentArea.flatMap( _.neighboringAreas )
      }
    }
    distance
  }
  
  def placeItems = {

  }
  
}