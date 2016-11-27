package tekstipeli

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random

class Bunker(val humans: Map[String, Human], val depositedItems: Map[String, Buffer[Item]]) {
  
  
  def dailyReport = {
    println(humans)
    story + humanReport + event 
  }
  
  def story: String = {
    
    
   "Day 1 \n\nWe are now safe in the bunker. We could feel the ground shaking when the missile hit. Luckily we all made it." 
   
  }
  
  def humanReport = {
    if(humans.nonEmpty) humans.values.toVector.foreach(_.report) else ""
  }
  
  
  
  def event: String = {
    val randomSeed = new Random
    
    val ants = "There's a small problem... Apparently there was ants living in the bunker and they don't want us here.\nWhat should we do to them?"
    val knockGentle = "What? Did someone knock on the door? The knock was so gentle that we almost didn't hear it.\nShould we open?"
    val knockHard = "There is someone banging on the door. Do we dare open?"
    val screams = "Someone is screaming for help outside. Should we help?"
    
    val events = Buffer(ants, knockGentle, knockHard, screams)
    
    def chooseEventRandom(events: Buffer[String]) = {
      val randomizedEvents = randomSeed.shuffle(events)
      randomizedEvents(0)
    }
    
    chooseEventRandom(events)
  }
  
  def expedition(human: Human) = {
    human.daysLeftOfExpedition = 3
  }
  
 
}