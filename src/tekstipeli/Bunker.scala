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
   else if (day == 2)  "Day 2 \n\nDuring the night we heard explosions from not that far away. This can't be happening, it has to be a bad dream."
   else if (day == 3)  "Day 3 \n\nIt has been quiet now for a while. Too quiet... Who even launched the missile and what is this missile we are talking about??"
   else if (day == 4)  "Day 4 \n\nHmm, we have our suspicions on who launched the missile. Most likely a newly elected president..."
   else if (day == 5)  "Day 5 \n\nYou have alot of time to think about stuff when you are sitting in a bunker. Why didn't I like my co-worker, couldn't we just have been friends?"
   else if (day == 6)  "Day 6 \n\nWhat is the point in even trying to survive? No one is going to rescue us."
   else if (day == 7)  "Day 7 \n\nIt's going to be okay. What happened happened and there was nothing we could do. Now lets do our best and wait for the rescuers."
   else if (day == 8)  "Day 8 \n\nWait a second.. we have the radio with us. Don't know who took it here. Let's see if it works."
   else if (day == 9)  "Day 9 \n\nHOLY MOLY!! There was a broadcast on the radio and they are searching survivors. They will come close to us tomorrow!"
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
    yesterdayEvent = todaysEvent
    if(yesterdayEvent.isDefined) {
      yesterdayEvent.get.completionStatus
      yesterdayEvent.get.addRewards
    }
    todaysEvent = Some(this.event)
    for (human <- humans.values) {
      human.advanceOneDay
    }
    "The next day"
    
  }
  
  def feed = {
    
    var humanCollection = humans.values.toVector
    if(depositedItems.contains("Canned Beans")) {
    for (currentHuman <- humanCollection) {
      currentHuman.eat
     } 
    "Everyone feels satisfied"
    }else "You don't have any Canned Beans left"
    
  }
  
  def give = {
    
    var humanCollection = humans.values.toVector
    if(depositedItems.contains("Water Bottle")) {
    for (currentHuman <- humanCollection) {
      currentHuman.drink
     } 
    "Everyone feels refreshed"
    }else "You don't have any Bottles of Water left"
    
  }

  def useItem(itemName: String) = {
    if(itemName == "Door") {
      todaysEvent.get.addItem(allItems(itemName))
      "You slowly open the door."
    } else {
    if(depositedItems.contains(itemName)) {
    if(todaysEvent.get.usefullItems.contains(allItems(itemName))) {
      todaysEvent.get.addItem(allItems(itemName))
      "You take the " + itemName + "..."
     } else "That won't help."
    } else "You don't have " + itemName + " in the bunker"
    }
  }
  
  
  
  def event: Event = {
    val randomSeed = new Random
    

    val antsEvent          = new Event("ants", "\nThere's a small problem... Apparently there was ants living in the bunker and they don't want us here.\nWhat should we do to them?",
                                      this, Buffer(allItems("Bugspray")), Buffer(allItems("Water Bottle")),
                                      "\nLuckily we hade the bugspray. Now those pesky ants won't disturb us. We also found a water bottle in their colony.",
                                      "\nWe tried to fight them with our bare hands, we killed some but not all.", "")
    val knockGentleEvent   = new Event("knockGentle", "\nWhat? Did someone knock on the door? The knock was so gentle that we hardly could hear it.\nTake the flashlight and open the door?",
                                      this, Buffer(allItems("Flashlight"), allItems("Door")), Buffer(allItems("Canned Beans")),
                                      "\nWe opened the door and there standing was other survivors, they asked us if we were okay and gave us food", "\nWe didn't hear any more knocks",
                                      "\nWe opened the door but saw no one")
    val knockHardEvent     = new Event("knockHard","\nThere is someone banging on the door. Take the axe and go open the door?", this, Buffer(allItems("Axe"), allItems("Door")), Buffer(),
                                      "\nWe opened the door and saw someone running away, he probably got scared of the axe.","\nAfter a while the knocks just stopped.",
                                      "\nWe didn't dare open the door without protection")
    val screamsEvent       = new Event("screams", "\nSomeone is screaming for help outside. Take the flashlight and go help?", this, Buffer(allItems("Flashlight"), allItems("Door")), Buffer(allItems("Bugspray")),
                                      "\nWe ran outside and saw a dog howling, guess it was the dog we heard. We saw a a bottle of bugspray on the ground and took it.",
                                      "\nWe heard a explosion outside, after that there was silence.", "\nWe peeked out the door but it was so dark we couldn't see anything.")
    
    val booringEvent       = new Event("booring", "\nIt's soo booring in here. Can't we do anything else but wait?", this, Buffer(allItems("Playingcards")), Buffer(),
                                      "\nThat's a good idea, this will entertain us for a while.", "\nWell maybe we can't expect too much.", "")
                                     
    
    val expeditionGasEvent = new Event("expeditionGas", "\nWe could go on an expedition outside, there's maybe toxic gases outside. Take the gas mask and go?", this, Buffer(allItems("Gas Mask")), Buffer(allItems("Canned Beans")),
                                      "\nWe walked around the quarter and everything was destroyed. Atleast we found food!",
                                      "\nYeah, maybe we shouldn't take any risks.", "")
    
    val expeditionEvent    = new Event("expedition", "\nShould we go on a expedition? Only on a short one. Please. We could take the map with us?", this, Buffer(allItems("Map")), Buffer(allItems("Water Bottle")),
                                      "\nThanks to the map we could navigate around in the city. We didn't see any signs of life... but we found a bottle of water.",
                                      "\nYeah, maybe we shouldn't take any risks.", "")
    
    
    val events = Buffer(antsEvent, knockGentleEvent, knockHardEvent, screamsEvent, booringEvent, expeditionGasEvent, expeditionEvent)
    
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