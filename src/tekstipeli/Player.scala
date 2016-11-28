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


