package tekstipeli

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random


class House(val rooms: Map[String, Area], val player: Player) {
  
  def distanceToItem(item: String): Int = {
    var distance = 1
    var currentArea = Vector(this.player.location)
    if(this.player.location.contains(item)) {
        distance -= 1
    } else {
      for(currentRoom <- 0 until currentArea.size) {
        if(currentArea(currentRoom).neighboringAreas.exists( _.contains(item) )) {
          return distance
        } else {
        distance += 1
        currentArea = currentArea.flatMap( _.neighboringAreas )
        }
      }
    }
    distance
  }
  
  def placeItems(items: Buffer[Item]) = {
    val randomSeed = new Random
    val randomizedItems = randomSeed.shuffle(items)
    val roomCollection = this.rooms.values.toVector
    var n = 0
    while(randomizedItems.nonEmpty) {
      if(randomizedItems(0).isOpenable && randomizedItems.size >= 2) {
        while(!randomizedItems(0).isFull && randomizedItems.size >= 2) {
          randomizedItems(0).addItemInside(randomizedItems(1))
          randomizedItems -= randomizedItems(1)
        }
      }
      roomCollection(n).addItem(randomizedItems(0))
      randomizedItems -= randomizedItems(0)
      n = (n + 1) % roomCollection.size
    }
  }
  

}