package tekstipeli

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random

class Bunker(val humans: Map[String, Human], val depositedItems: Map[String, Buffer[Item]], val allItems: Map[String, Item]) {
  
  private var yesterdayEvent: Option[Event] = None
  private var todaysEvent: Option[Event] = None
  private var day = 0
  
  def dailyReport = {
awda
    story + humanReport + yesterdayEvent + todaysEvent 
  }
  
  def story: String = {
    
   "Day 1 \n\nWe are now safe in the bunker. We could feel the ground shaking when the missile hit. Luckily we all made it." 
   
  }
  
  def humanReport = {
    var humansReport = ""
    var humanCollection = humans.values.toVector
    for (currentHuman <- humanCollection.indices) {
      humansReport += humanCollection(currentHuman).report
    }
    humansReport
  }
  
  def advanceDay = {
    
    day += 1
    yesterdayEvent = todaysEvent
    todaysEvent = Some(this.event)
    
    
  }
  
  
  def event: Event = {
    val randomSeed = new Random
    
    val antsEvent        = new Event("ants", "There's a small problem... Apparently there was ants living in the bunker and they don't want us here.\nWhat should we do to them?",
                                     this, Buffer(allItems("bugspray")), Buffer(allItems("Water Bottle")))
    val knockGentleEvent = new Event("knockGentle", "What? Did someone knock on the door? The knock was so gentle that we almost didn't hear it.\nShould we open?",
                                     this, Buffer(allItems("Axe")), Buffer(allItems("Canned Beans")))
    val knockHardEvent   = new Event("knockHard","There is someone banging on the door. Do we dare open?", this, Buffer(allItems("Axe")), Buffer())
    val screamsEvent     = new Event("screams", "Someone is screaming for help outside. Should we help?", this, Buffer(allItems("Flashlight")), Buffer(allItems("Bugspray")))
    
    val events = Buffer(antsEvent, knockGentleEvent, knockHardEvent, screamsEvent)
    
    def chooseEventRandom(events: Buffer[Event]) = {
      val randomizedEvents = randomSeed.shuffle(events)
      randomizedEvents(0)
    }
    
    chooseEventRandom(events)
  }
  
  def expedition(human: Human) = {
    human.daysLeftOfExpedition = 3
  }
  
 
}