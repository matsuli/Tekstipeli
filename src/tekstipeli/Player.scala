package tekstipeli

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer


class Player {
  
  private var quitCommandGiven = false
  
  def hasQuit = this.quitCommandGiven
  
  def quit() = {
    this.quitCommandGiven = true
    ""
  }
  
}

object Player {
  
  def inventory(collection: Map[String, Buffer[Item]], have: String, dontHave: String): String = {
    if(collection.nonEmpty) {
      var currentItems = ""
      for(pair <- collection) {
        currentItems += pair._1 + (if(pair._2.size > 1) " x" + pair._2.size.toString else "") +  "\n"
      }
      have + currentItems
    } else dontHave
  }
  
}


