package controllers

import play._
import play.mvc._

object Application extends Controller {
    
    import views.Application._
    
    def index = html.index("Home")  
    def sayHello = html.sayHello(params.get("myName"))
    
}
