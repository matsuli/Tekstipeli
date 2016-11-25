package tekstipeli
import scala.collection.mutable.Buffer


class Bunker {
  
  var humans = Buffer[Human]()
  
  def dailyReport = {
    
    story + humans.foreach(_.report) + event
    
    
  }
  
  def story: String = {
    ???
  }
  
  def event: String = {
    ???
  }
  
  
  
  
  
  
  
  
  
  
  
}