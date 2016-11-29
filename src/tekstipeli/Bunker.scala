package tekstipeli

import tekstipeli.Player._
import scala.collection.mutable.Buffer
import scala.collection.mutable.Map
import scala.util.Random

class Bunker(val humans: Map[String, Human], val depositedItems: Map[String, Buffer[Item]], val allItems: Map[String, Item]) {
  
  private var yesterdayEvent: Option[Event] = None
  private var todaysEvent: Option[Event] = None
  private var haveRadio = this.depositedItems.contains("Radio")
  private var day = 0
  private var militaryEventsDone = 0
  var gameCompleted = false
  var allDead = false
  var deadHumans = Buffer[Human]()

  def currentDay = this.day
  
  def checkHumans = {
    for(human <- this.humans.values) {
      if(!human.alive) {
        this.deadHumans += human
        this.humans -= human.name
      }
    }
    if(this.humans.size == 0) allDead = true
  }
  
  //Stories to be told. And the methods used for that.
  
  private val jokes         = "\n\nToday we had a joke telling competition. It was bunkerous... I'm bad at telling jokes."
  private val oldStuff      = "\n\nThere's all kinds of stuff here in the bunker. Old pictures, school books. Nothing too usefull I'm afraid..."
  private val funGame       = "\n\nWe came up with a fun game today. Try to argument why Apple is better than everything else. The game didn't last long."
  private val chickenJoke   = "\n\nWhy did the chicken cross the road. Because he wanted to talk to the ugly neighbour. Knock knock. Who's there? It's the chicken."
  private val life          = "\n\nWhat is life? Baby don't hurt me..."
  private val diary         = "\n\nDear diary, today I couldn't believe what happened. HEY this is private!"
  private val watches       = "\n\nA man with one watch knows what time it is; a man with two watches is never quite sure."
  private val clover        = "\n\nIf a man who cannot count finds a four-leaf clover, is he lucky?"
  private val studentMaster = "\n\nThe master has failed more times than the beginner has even tried."
  
  private val day0          = "\n\nWe are now safe in the bunker. We could feel the ground shaking when the missile hit. Luckily we all made it."
  private val day2          = "\n\nDuring the night we heard explosions from not that far away. This can't be happening, it has to be a bad dream."
  private val day4          = "\n\nIt has been quiet now for a while. Too quiet... Who even launched the missile and what is this missile we are talking about??"
  private val day7          = "\n\nHmm, we have our suspicions on who launched the missile. Most likely a newly elected president..."
  private val day17         = "\n\nYou have alot of time to think about stuff when you are sitting in a bunker. Why didn't I like my co-worker, couldn't we just have been friends?"
  private val day21         = "\n\nWhat is the point in even trying to survive? No one is going to rescue us."
  private val day25         = "\n\nIt's going to be okay. What happened happened and there was nothing we could do. Now lets do our best and wait for the rescuers."
  private val day29         = "\n\nWe are still positive that we will be rescued. It is just a matter of time."
  private val day33         = "\n\nHOLY MOLY!! There was a broadcast on the radio and they are rescuing survivors. They will save us soon!"
  
  private val storyCollection = Vector(jokes, oldStuff, funGame, chickenJoke, life, diary, watches, clover, studentMaster)
  
  def randomStory(storyCollection: Vector[String]): String = {
    val randomSeed = new Random
    val randomizedStories = randomSeed.shuffle(storyCollection)
    randomizedStories(0)
  }
  
  //preset stories
  def Story: String = {
        if (day == 0)  day0
   else if (day == 2)  day2
   else if (day == 4)  day4
   else if (day == 7)  day7
   else if (day == 17) day17
   else if (day == 21) day21
   else if (day == 25) day25
   else if (day == 29) day29
   else if (day == 33) day33
   else randomStory(storyCollection)
  }
  
  //Events.
  
    private val antsEvent          = new Event("ants", "\nThere's a small problem... Apparently there was ants living in the bunker and they don't want us here.\nWhat should we do to them?",
                                      this, Buffer(allItems("Bugspray")), Buffer(allItems("Water Bottle"), allItems("Water Bottle")),
                                      "\nLuckily we hade the bugspray. Now those pesky ants won't disturb us. We also found a water bottle in their colony.",
                                      "\nWe tried to fight them with our bare hands, we killed some but not all.", "")
    private val knockGentleEvent   = new Event("knockGentle", "\nWhat? Did someone knock on the door? The knock was so gentle that we hardly could hear it.\nTake the flashlight and open the door?",
                                      this, Buffer(allItems("Flashlight"), allItems("Door")), Buffer(allItems("Canned Beans"), allItems("Canned Beans")),
                                      "\nWe opened the door and there standing was other survivors, they asked us if we were okay and gave us food", "\nWe didn't hear any more knocks",
                                      "\nWe opened the door but saw no one")
    private val knockHardEvent     = new Event("knockHard","\nThere is someone banging on the door. Take the axe and go open the door?", this, Buffer(allItems("Axe"), allItems("Door")), Buffer(allItems("Gas Mask")),
                                      "\nWe opened the door and saw someone running away, he probably got scared of the axe. He left an gas mask behind.","\nAfter a while the knocks just stopped.",
                                      "\nWe didn't dare open the door without protection")
    private val screamsEvent       = new Event("screams", "\nSomeone is screaming for help outside. Take the flashlight and go help?", this, Buffer(allItems("Flashlight"), allItems("Door")), Buffer(allItems("Bugspray"), allItems("Water Bottle")),
                                      "\nWe ran outside and saw a dog howling, guess it was the dog we heard. We saw a a bottle of bugspray on the ground and took it.",
                                      "\nWe heard a explosion outside, after that there was silence.", "\nWe peeked out the door but it was so dark we couldn't see anything.")
    private val booringEvent       = new Event("booring", "\nIt's soo booring in here. Can't we do anything else but wait?", this, Buffer(allItems("Playingcards")), Buffer(),
                                      "\nThat's a good idea, this will entertain us for a while.", "\nWell maybe we can't expect too much.", "")
    private val expeditionGasEvent = new Event("expeditionGas", "\nWe could go on an expedition outside, there's maybe toxic gases outside. Take the gas mask and go?", this, Buffer(allItems("Gas Mask")), Buffer(allItems("Canned Beans"), allItems("Water Bottle")),
                                      "\nWe walked around the quarter and everything was destroyed. Atleast we found food and water!",
                                      "\nYeah, maybe we shouldn't take any risks.", "")
    private val expeditionEvent    = new Event("expedition", "\nShould we go on a expedition? Only on a short one. Please? We could take the map with us?", this, Buffer(allItems("Map")), Buffer(allItems("Water Bottle"), allItems("Axe"), allItems("Canned Beans")),
                                      "\nThanks to the map we could navigate around in the city. We didn't see any signs of life... but we found a bottle of water.",
                                      "\nYeah, maybe we shouldn't take any risks.", "")
    private val militaryTreesEvent    = new Event("militaryTrees", "\nThe military said on the radio that you should go cut down some trees nearby your shelter." + (if(!haveRadio) "Oh, we don't have a radio. Worth a shot anyways. ")+ "That way they can know where you are. Take the axe and go cut some trees?", this, Buffer(allItems("Axe")), Buffer(allItems("Water Bottle"), allItems("Water Bottle")),
                                      "\nWe managed to cut down some trees in the park nearby, hopefully the military will spot it.",
                                      "\nMaybe some other day.", "")
    private val militaryFlashlightEvent = new Event("militaryFlashlight", "\nThe military said on the radio that they will do a fly by over our area during the night." + (if(!haveRadio) "Oh, we don't have a radio. Worth a shot anyways. ") + "Take the flashlight and go out?", this, Buffer(allItems("Flashlight")), Buffer(allItems("Water Bottle"), allItems("Canned Food")),
                                      "\nThey did see us! We will be rescued soon...",
                                      "\nMaybe some other day.", "")
    private val rescuedEvent            = new Event("rescuedEvent", "\n" + (if(!haveRadio) "Oh, we don't have a radio. Worth a shot anyways") + "Take the flashlight and go out?", this, Buffer(allItems("Flashlight")), Buffer(),
                                      "\nThey did see us! We will be rescued soon...",
                                      "\nMaybe some other day.", "")
    private val noEvent                 = new Event("noEvent", "\nThere is nothing special to report today.", this, Buffer(), Buffer(), "", "", "")
      
    private val militaryEvents  = Buffer(militaryTreesEvent, militaryFlashlightEvent)
    private val eventsAvailable = Buffer(antsEvent, knockGentleEvent, knockHardEvent, screamsEvent, booringEvent, expeditionGasEvent, expeditionEvent)
    private val eventsOnHold    = Buffer[Event]()
  
  //Some other stuff.
  
  def dailyReport = {
    "Day " + day.toString + Story + "\n" + humanReport + 
    (if(yesterdayEvent.isDefined) "\n" + yesterdayEvent.get.outcome else "") + 
    (if(todaysEvent.isDefined) "\n" + todaysEvent.get.description  + "\nUsefull items: " + todaysEvent.get.itemStatus else "")
  }
  
  def humanReport: String = {
    var humansReport = ""
    var humanCollection = humans.values.toVector
    for (currentHuman <- humanCollection) {
      humansReport += currentHuman.report
    }
    humansReport
  }
  
  //advances one day and does everything that is needed
  def advanceOneDay = {
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
    this.checkHumans
    "The next day."
  }
  
  def inventory = Player.inventory(this.depositedItems, "Stashed items:\n", "You seem to be empty-handed. That's not too good, is it?")
  
  //feeds all or o specific person
  def feed(who: String) = {
    if(who == "All" || who == "Everyone" && this.depositedItems("Canned Beans").size >= this.humans.size) {
      var humanCollection = humans.values.toVector
      if(depositedItems.contains("Canned Beans")) {
      for(currentHuman <- humanCollection) {
        currentHuman.eat
       }
      for(index <- humanCollection.indices) {
        if(this.depositedItems("Canned Beans").size == 1) this.depositedItems -= "Canned Beans" else this.depositedItems("Canned Beans") -= this.depositedItems("Canned Beans")(0)
      }
      "Everyone feels satisfied."
      } else "You don't have enough food for everyone."
    } else if(this.humans.contains(who)) {
      if(depositedItems.contains("Canned Beans")) {
        this.humans(who).eat
        if(this.depositedItems("Canned Beans").size == 1) this.depositedItems -= "Canned Beans" else this.depositedItems("Canned Beans") -= this.depositedItems("Canned Beans")(0)
        who + " feels satisfied."
      } else "You don't have any Canned Beans left."
    } else "You can't feed " + who + "anything."  
  }
  
  //gives water to everyone or to a specific person
  def give(who: String) = {
    if(who == "All" || who == "Everyone" && this.depositedItems("Water Bottle").size >= this.humans.size) {
      var humanCollection = humans.values.toVector
      if(depositedItems.contains("Water Bottle")) {
      for(currentHuman <- humanCollection) {
        currentHuman.drink
       }
      for(index <- humanCollection.indices) {
        if(this.depositedItems("Water Bottle").size == 1) this.depositedItems -= "Water Bottle" else this.depositedItems("Water Bottle") -= this.depositedItems("Water Bottle")(0)
      }
      "Everyone feels refreshed."
      } else "You don't have enough water for everyone."
    } else if(this.humans.contains(who)) {
      if(depositedItems.contains("Water Bottle")) {
        this.humans(who).drink
        if(this.depositedItems("Water Bottle").size == 1) this.depositedItems -= "Water Bottle" else this.depositedItems("Water Bottle") -= this.depositedItems("Water Bottle")(0)
        who + " feels refreshed."
      } else "You don't have any Water Bottles left."
    } else "You can't give " + who + "anything."
  }

  //uses a item for the event
  def useItem(itemName: String) = {
    if(itemName == "Door") {
      todaysEvent.get.addItem(allItems(itemName))
      "Terrified of what is to come, you slowly open the door."
    } else {
    if(depositedItems.contains(itemName)) {
    if(todaysEvent.get.usefullItems.contains(allItems(itemName))) {
      todaysEvent.get.addItem(allItems(itemName))
      "You take the " + itemName + "..."
     } else "That won't of any help."
    } else "You don't have a " + itemName + " in here."
    }
    
  }
  
  //chooses the next event. Event chosen depends on the day count and some randomnes
  def event: Event = {
    val randomSeed = new Random
    
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
    
    if(day == 10) {
      for(currentEvent <- militaryEvents) {
        eventsAvailable += currentEvent
      }
    }
    
    if(day > 42) {
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
  
  def help: String = "Make decisions for the different events. Usable commands: next day, feed all, give water, use and open door"
  
 
}