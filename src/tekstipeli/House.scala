package tekstipeli
import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random

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
  
  def placeItems(items: Buffer[Item]) = {
    val randomSeed = new Random
    val randomizedItems = randomSeed.shuffle(items)
    for(currentRoom <- 0 until this.rooms.size) {
      val roomCollection = this.rooms.values.toVector
      for(currentItem <- 0 to 1) {
        roomCollection(currentRoom).addItem(randomizedItems(currentItem))
        randomizedItems -= randomizedItems(currentItem)
      }
    }
  }
  
}