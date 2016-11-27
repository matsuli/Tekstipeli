package tekstipeli

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random

class Bunker(val humans: Map[String, Human], val depositedItems: Map[String, Buffer[Item]], val allItems: Map[String, Item]) {
  
  private var yesterdayEvent: Option[Event] = None
  private var todaysEvent: Option[Event] = None
  private var day = 1
  
  def dailyReport = {
    if(day < 10) story + humanReport + (if(yesterdayEvent.isDefined) yesterdayEvent.get.outcome else "") + (if(todaysEvent.isDefined) todaysEvent.get.fullDescription else "")  else story
  }
  
  def story: String = {
    
   if (day == 1)       "Day 1 \n\nWe are now safe in the bunker. We could feel the ground shaking when the missile hit. Luckily we all made it." 
   else if (day == 2)  "Day 2 \n\n"
   else if (day == 3)  "Day 3 \n\n"
   else if (day == 4)  "Day 4 \n\n"
   else if (day == 5)  "Day 5 \n\n"
   else if (day == 6)  "Day 6 \n\n"
   else if (day == 7)  "Day 7 \n\n"
   else if (day == 8)  "Day 8 \n\n"
   else if (day == 9)  "Day 9 \n\n"
   else if (day == 10) "The End \n\nThe rescuers came and saved us from the terrible and dull bunker we had been living in for 10 days."
   else ""
  }
  
  def humanReport: String = {
    var humansReport = ""
    var humanCollection = humans.values.toVector
    for (currentHuman <- humanCollection) {
      humansReport += currentHuman.report
    }
    println(humansReport)
    humansReport
  }
  
  def advanceDay = {
    
    day += 1
    if(yesterdayEvent.isDefined) yesterdayEvent.get.addRewards
    yesterdayEvent = todaysEvent
    todaysEvent = Some(this.event)
    for (human <- humans.values) {
      human.advanceOneDay
    }
    "The next day"
    
  }
  
  def useItem(itemName: String) = {
    
    if(depositedItems.contains(itemName)) {
    if(todaysEvent.get.usefullItems.contains(itemName)) {
      todaysEvent.get.addItem(allItems(itemName))
      ""
     } else ""
    } else "You don't have " + itemName + " in the bunker"
  }
  
  
  
  def event: Event = {
    val randomSeed = new Random
    
    val antsEvent        = new Event("ants", "\nThere's a small problem... Apparently there was ants living in the bunker and they don't want us here.\nWhat should we do to them?",
                                     this, Buffer(allItems("Bugspray")), Buffer(allItems("Water Bottle")),
                                     "\nLuckily we hade the bugspray. Now those pesky ants won't disturb us. We also found a water bottle in their colony",
                                     "\nWe tried to fight them with our bare hands, we killed some but not all.")
    val knockGentleEvent = new Event("knockGentle", "\nWhat? Did someone knock on the door? The knock was so gentle that we hardly could hear it.\nTake the flashlight and open the door?",
                                     this, Buffer(allItems("Flashlight")), Buffer(allItems("Canned Beans")),
                                      "\nWe opened the door and there standing was other survivors, they asked us if we were okay and gave us food", "\nWe didn't hear any more knocks")
    val knockHardEvent   = new Event("knockHard","\nThere is someone banging on the door. Take the axe and go open the door?", this, Buffer(allItems("Axe")), Buffer(),
                                     "\nWe opened the door and saw someone running away, he probably got scared of the axe.","\nAfter a while the knocks just stopped.")
    val screamsEvent     = new Event("screams", "\nSomeone is screaming for help outside. Take the flashlight and go help?", this, Buffer(allItems("Flashlight")), Buffer(allItems("Bugspray")),
                                     "\nWe ran outside and saw a dog howling, guess it was the dog we heard. We saw a a bottle of bugspray on the ground and picked it up.","\nWe heard a explosion outside, after that there was silence.")
    
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