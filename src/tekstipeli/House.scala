package tekstipeli

class House(val rooms: Map[String, Area], val player: Player) {
  
  def distanceToRadio: Int = {
    var distance = 0
    while(!this.player.location.neighboringAreas.exists( _.contains("Radio"))) {
      distance += 1
    }
    distance
  }
  
  def placeItems = {
    while(player == mats) quit'
    qwtqwtqwropj
  }
  
}