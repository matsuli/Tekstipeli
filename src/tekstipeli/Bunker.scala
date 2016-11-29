package tekstipeli

import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random

class Bunker(val humans: Map[String, Human], val depositedItems: Map[String, Buffer[Item]], val allItems: Map[String, Item]) {
  
  private var yesterdayEvent: Option[Event] = None
  private var todaysEvent: Option[Event] = None
  private var haveRadio = this.depositedItems.contains("Radio")
  private var day = 1
  private var militaryEventsDone = 0
  var gameCompleted = false
  
  def dailyReport = {
    story + "\n" + humanReport + (if(yesterdayEvent.isDefined) "\n" + yesterdayEvent.get.outcome else "") + (if(todaysEvent.isDefined) "\n" + todaysEvent.get.fullDescription else "")
  }
  
  def story: String = {
    
   if (day == 1)        "Day 1 \n\nWe are now safe in the bunker. We could feel the ground shaking when the missile hit. Luckily we all made it." 
   else if (day == 2)   "Day 2 \n\nDuring the night we heard explosions from not that far away. This can't be happening, it has to be a bad dream."
   else if (day == 4)   "Day 4 \n\nIt has been quiet now for a while. Too quiet... Who even launched the missile and what is this missile we are talking about??"
   else if (day == 7)   "Day 7 \n\nHmm, we have our suspicions on who launched the missile. Most likely a newly elected president..."
   else if (day == 17)  "Day 17 \n\nYou have alot of time to think about stuff when you are sitting in a bunker. Why didn't I like my co-worker, couldn't we just have been friends?"
   else if (day == 21)  "Day 21 \n\nWhat is the point in even trying to survive? No one is going to rescue us."
   else if (day == 25)  "Day 25 \n\nIt's going to be okay. What happened happened and there was nothing we could do. Now lets do our best and wait for the rescuers."
   else if (day == 29)  "Day 29 \n\nWe are still positive that we will be rescued. It is just a matter of time."
   else if (day == 33)  "Day 33 \n\nHOLY MOLY!! There was a broadcast on the radio and they are rescuing survivors. They will save us soon!"
   else "Day " + day.toString() + randomStory
  }
  
  def randomStory: String = {
    val randomSeed = new Random
    
    val jokes         = "\n\nToday we had a joke telling competition. It was bunkerous... I'm bad at telling jokes."
    val oldStuff      = "\n\nThere's all kinds of stuff here in the bunker. Old pictures, school books. Nothing too usefull I'm afraid..."
    val funGame       = "\n\nWe came up with a fun game today. Try to argument why Apple is better than everything else. The game didn't last long."
    val chickenJoke   = "\n\nWhy did the chicken cross the road. Because he wanted to talk to the ugly neighbour. Knock knock. Who's there? It's the chicken."
    val life          = "\n\nWhat is life? Baby don't hurt me..."
    val diary         = "\n\nDear diary, today I couldn't believe what happened. HEY this is private!"
    val watches       = "\n\nA man with one watch knows what time it is; a man with two watches is never quite sure."
    val clover        = "\n\nIf a man who cannot count finds a four-leaf clover, is he lucky?"
    val studentMaster = "\n\nThe master has failed more times than the beginner has even tried."
    
    val storyCollection = Buffer(jokes, oldStuff, funGame, chickenJoke, life, diary, watches, clover, studentMaster)
    val randomizedStories = randomSeed.shuffle(storyCollection)
    randomizedStories(0)
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
      if(yesterdayEvent.get.name == "militaryTrees" || yesterdayEvent.get.name == "militaryFlashlight" && yesterdayEvent.get.success) militaryEventsDone += 1 
    }
    todaysEvent = Some(this.event)
    for (human <- humans.values) {
      human.advanceOneDay
    }
    "The next day"
    
  }
  
  def help: String = "Make decisions for the different events. Usable commands: next day, feed all, give water, use and open door"
  
  def inventory = {
    if(this.depositedItems.nonEmpty) {
      var currentItems = ""
      for(pair <- this.depositedItems) {
        currentItems += pair._1 + (if(pair._2.size > 1) " x" + pair._2.size.toString else "") +  "\n"
      }
      "List of the items in the bunker:\n" + currentItems
    } else "There's no usable items in the bunker"
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
    
    val expeditionEvent    = new Event("expedition", "\nShould we go on a expedition? Only on a short one. Please? We could take the map with us?", this, Buffer(allItems("Map")), Buffer(allItems("Water Bottle"), allItems("Axe")),
                                      "\nThanks to the map we could navigate around in the city. We didn't see any signs of life... but we found a bottle of water.",
                                      "\nYeah, maybe we shouldn't take any risks.", "")
    
    val militaryTreesEvent    = new Event("militaryTrees", "\nThe military said on the radio that you should go cut down some trees nearby your shelter." + (if(!haveRadio) "Oh, we don't have a radio. Worth a shot anyways")+ "That way they can know where you are. Take the axe and go cut some trees?", this, Buffer(allItems("Axe")), Buffer(),
                                      "\nWe managed to cut down some trees in the park nearby, hopefully the military will spot it.",
                                      "\nMaybe some other day.", "")
    
    val militaryFlashlightEvent = new Event("militaryFlashlight", "\nThe military said on the radio that they will do a fly by over our area during the night." + (if(!haveRadio) "Oh, we don't have a radio. Worth a shot anyways") + "Take the flashlight and go out?", this, Buffer(allItems("Flashlight")), Buffer(),
                                      "\nThey did see us! We will be rescued soon...",
                                      "\nMaybe some other day.", "")
    
    val rescuedEvent            = new Event("rescuedEvent", "\n" + (if(!haveRadio) "Oh, we don't have a radio. Worth a shot anyways") + "Take the flashlight and go out?", this, Buffer(allItems("Flashlight")), Buffer(),
                                      "\nThey did see us! We will be rescued soon...",
                                      "\nMaybe some other day.", "")
    
    val noEvent                 = new Event("noEvent", "\nThere is nothing special to report today.", this, Buffer(), Buffer(), "", "", "")
    
    val militaryEvents = Buffer(militaryTreesEvent, militaryFlashlightEvent)
    val eventsAvailable = Buffer(antsEvent, knockGentleEvent, knockHardEvent, screamsEvent, booringEvent, expeditionGasEvent, expeditionEvent)
    val eventsOnHold = Buffer[Event]()
    
    def chooseEventRandom(events: Buffer[Event]) = {
      if(eventsAvailable.isEmpty) {
        for(currentEvent <- eventsOnHold) {
          eventsAvailable += currentEvent 
          eventsOnHold -= currentEvent
        }
      }
      val randomizedEvents = randomSeed.shuffle(events)
      val roll = randomSeed.nextInt(2)
      if(roll == 0) randomizedEvents(0) else noEvent
    }
    
    if(day > 10) {
      for(currentEvent <- militaryEvents) {
        eventsAvailable += currentEvent
      }
    }
    
    if(day > 35) {
      val roll = randomSeed.nextInt(10)
      if(militaryEventsDone > 1) {
        eventsAvailable += rescuedEvent 
      } else if(militaryEventsDone == 1 && roll > 4) {
        eventsAvailable += rescuedEvent 
      } else if(militaryEventsDone == 0 && roll > 7) {
        eventsAvailable += rescuedEvent
      }
    }
    
    val chosenEvent = chooseEventRandom(eventsAvailable)
    eventsAvailable -= chosenEvent
    eventsOnHold += chosenEvent
    chosenEvent
  }
  
  def expedition(human: Human) = {
    human.daysLeftOfExpedition = 3
  }
  
 
}