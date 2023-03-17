import scala.collection.immutable.ArraySeq
import scala.io.Source

/**
 * Main app containg program loop
 */
object Main extends App {

  println("Starting application")

  val status = run()

  println("\nExiting application")
  println(s"Final status: ${status.message}")

  /**
   * Read action from Stdin and execute it
   * Exit if action is 'exit' or if an error occured (status > 0)
   * DO NOT MODIFY THIS FUNCTION
   */
  def run(canvas: Canvas = Canvas()): Status = {
    println("\n======\nCanvas:")
    canvas.display

    print("\nAction: ")

    val action = scala.io.StdIn.readLine()

    val (newCanvas, status) = execute(ArraySeq.unsafeWrapArray(action.split(' ')), canvas)

    if (status.error) {
      println(s"ERROR: ${status.message}")
    }

    if (status.exit) {
      status 
    } else {
      run(newCanvas)  
    }
  }

  /**
   * Execute various actions depending on an action command and optionnaly a Canvas
   */
  def execute(action: Seq[String], canvas: Canvas): (Canvas, Status) = {
    val execution: (Seq[String], Canvas) => (Canvas, Status) = action.head match {
      case "exit" => Canvas.exit
      case "dummy" => Canvas.dummy
      case "dummy2" => Canvas.dummy2
      case "new_canvas" => Canvas.new_Canvas
      case "update_pixel" => Canvas.update_pixel
      case "draw_fill_recursive" =>Canvas.draw_fill_recursive
      case "drawLine" =>Canvas.drawLine
      case "drawLine1" =>Canvas.drawLine1
      case "load_image" => Canvas.load_image
      case "drawRectangle" => Canvas.drawRectangle

      // TODO: Add command here
      case _ => Canvas.default
    }

    execution(action.tail, canvas)
  }

 
}

/**
 * Define the status of the previous execution
 */
case class Status(
  exit: Boolean = false,
  error: Boolean = false,
  message: String = ""
)

/**
 * A pixel is defined by its coordinates along with its color as a char
 */
case class Pixel(x: Int, y: Int, color: Char = ' ') {
  override def toString(): String = {
    color.toString
  }
  //val p = Pixel("0,0", '#')
}

/**
 * Companion object of Pixel case class
 */
object Pixel {
  /**
   * Create a Pixel from a string "x,y"
   */
  def apply(s: String): Pixel = {
    // TODO
    val position= s.split(",")
    Pixel(position(0).toInt, position(1).toInt)
    //Pixel(0,0)
  }

  /**
   * Create a Pixel from a string "x,y" and a color 
   */
  def apply(s: String, color: Char): Pixel = {
    // TODO
    val position= s.split(",")
    Pixel(position(0).toInt, position(1).toInt,color)
    //Pixel(0, 0)
  }
}


/**
 * A Canvas is defined by its width and height, and a matrix of Pixel
 */
case class Canvas(width: Int = 0, height: Int = 0, pixels: Vector[Vector[Pixel]] = Vector()) {

  /**
   * Print the canvas in the console
   */
  def display: Unit = {
    if (pixels.size == 0) {
      println("Empty Canvas")
    } else {
      println(s"Size: $width x $height")
      // TODO
      for(h <-0 to height-1){
        for(w<-0 to width-1) {
          print(pixels(h)(w))
        }
      println("")
      }
    }
  }

  /**
   * Takes a pixel in argument and put it in the canvas
   * in the right position with its color
   */
  def update(pixel: Pixel): Canvas = {
    
    val newPixels = pixels.updated(pixel.y,pixels(pixel.y).updated(pixel.x,pixel))
    this.copy(pixels = newPixels)
  }

//val nouveauVecteurDeVecteurs = vecteurDeVecteurs.updated(positionLigne, vecteurDeVecteurs(positionLigne).updated(positionColonne, nouvelElement))
  /**
   * Return a Canvas containing all modifications
   */
  def updates(pixels: Seq[Pixel]): Canvas = {
    pixels.foldLeft(this)((f, p) => f.update(p))
  }
    /**
   * Return a Canvas containing after changing color for one pixel
   */
  def update_color(pixel_to_modify: Pixel, newPixel:Pixel,firstColor:Char): Canvas = {
      if(pixel_to_modify.color == '.') {
        val newPixels = pixels.updated(pixel_to_modify.y,pixels(pixel_to_modify.y).updated(pixel_to_modify.x,newPixel))
        this.copy(pixels = newPixels)

      } else {
        this // faire un jeu de test avec elle pour retourner une autre couleur 
        
      }

  }
    def update_color1(pixel: Pixel, newPixel:Pixel): Canvas = {

      val newPixels = pixels.updated(pixel.y,pixels(pixel.y).updated(pixel.x,newPixel))
      this.copy(pixels = newPixels)
     }



  //val updatedMatrix = pixelsMatrix.updated(rowIndex, pixelsMatrix(rowIndex).updated(columnIndex, pixelsMatrix(rowIndex)(columnIndex).copy(color = newColor)))

  def updates_color(pixels: Seq[Pixel],newPixel : Pixel,firstColor: Char): Canvas = {
    pixels.foldLeft(this)((f, p) => f.update_color(p,newPixel,firstColor))
  }
  // TODO: Add any useful method

}

/**
 * Companion object for Canvas case class
 */
object Canvas {
  /**
   * Exit execution
   */
  def exit(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    (canvas, Status(exit = true, message="Received exit signal"))

  /**
   * Default execution for unknown action
   */
  def default(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    (canvas, Status(error = true, message = s"Unknown command"))

  /**
   * Create a static Canvas
   */
  def dummy(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    if (arguments.size > 0) 
      (canvas, Status(error = true, message = "dummy action does not excpect arguments"))
    else  {
      val dummyCanvas = Canvas(
        width = 3,
        height = 4,
        pixels = Vector(
          Vector(Pixel(0, 0, '#'), Pixel(1, 0, '.'), Pixel(2, 0, '#')),
          Vector(Pixel(0, 1, '#'), Pixel(1, 1, '.'), Pixel(2, 1, '#')),
          Vector(Pixel(0, 2, '#'), Pixel(1, 2, '.'), Pixel(2, 2, '#')),
          Vector(Pixel(0, 3, '#'), Pixel(1, 3, '.'), Pixel(2, 3, '#'))
        )
      )
      
      (dummyCanvas, Status())
    }

  /**
   * Create a static canvas using the Pixel companion object
   */
  def dummy2(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    if (arguments.size > 0) 
      (canvas, Status(error = true, message = "dummy action does not excpect arguments"))
    else  {
      val dummyCanvas = Canvas(
        width = 3,
        height = 1,
        pixels = Vector(
          Vector(Pixel("0,0", '#'), Pixel("1,0",'.'), Pixel("2,0", '#')),
        )
      )
      
      (dummyCanvas, Status())
    }

  // TODO: Add any useful method
  def new_Canvas(arguments: Seq [String], canvas: Canvas): (Canvas, Status) =
    if (arguments.size == 3) {     
      val width = arguments(0).toInt
      val height = arguments(1).toInt
      val char =  arguments(2).charAt(0) 
      
      val newPixels = Vector.tabulate(height, width) { case (i, j) => Pixel(i, j, char) }
      val newCanvas = Canvas(width, height, newPixels)
      (newCanvas, Status())
    }         
    else
      (canvas, Status(error = true, message = "new_Canvas action does not excpect arguments"))

  def load_image(arguments: Seq[String], canvas: Canvas): (Canvas, Status) =
    if (arguments.size < 1)
      (canvas, Status(error = true, message = "La commande load_image attend 1 argument. Or vous en avez donne 0"))
    else {
      val fileName = arguments(0)
      try {
        val lines: Vector[String] = Source.fromFile(fileName).getLines().toVector
        val pixels = lines.map { line =>
          line.map(char => Pixel(0, 0, char)).toVector
        }
        val newCanvas = Canvas(pixels(0).size, pixels.size, pixels)
       // printCanvas(newCanvas)
        (newCanvas, Status())
      } catch {
        case e: Exception => (canvas, Status(error = true, message = s"Erreur de chargement de l'image: $e. Verifiez que le fichier existe et que vous avez les droits d'accès."))
      }
    }
  

  
    def update_pixel(arguments: Seq[String], canvas: Canvas): (Canvas, Status) = 
    if (arguments.size > 2) 
      (canvas, Status(error = true, message = "dummy action does not excpect more than two arguments"))
    else  {
      val updatedCanvas=canvas.update(Pixel(arguments(0),arguments(1).charAt(0)))      
      (updatedCanvas, Status())
    }

 

  def draw_fill_recursive(arguments: Seq [String], canvas: Canvas): (Canvas, Status) = {
  //val currentColor = Pixel(arguments(0)).color
  val currentColor = Pixel(arguments(0)).color
  val newColor=arguments(1).charAt(0) 
  val pixel = Pixel(arguments(0))
  if (currentColor == newColor) {
    (canvas, Status()) // If the pixel already has the new color, return the canvas unchanged
  } else {
    val filledCanvas = canvas.update(pixel.copy(color = newColor)) // Update the current pixel with the new color
    val adjacentPixels = List((pixel.x - 1, pixel.y), (pixel.x + 1, pixel.y), (pixel.x, pixel.y - 1), (pixel.x, pixel.y + 1))
                          .filter(p => p._1 >= 0 && p._2 >= 0 && p._1 < canvas.width && p._2 < canvas.height)
                          .map(p => Pixel(p._1, p._2))
    val adjacentPixelsToFill = adjacentPixels.filter(_.color == currentColor) // Get adjacent pixels with the same color
    adjacentPixelsToFill.foldLeft(filledCanvas) { case (c, p) => draw_fill_recursive(Seq(p.toString, newColor.toString), c)._1 } // Fill adjacent pixels recursively
    (filledCanvas, Status())
  }
}

  def drawLine(arguments: Seq[String] ,canvas: Canvas): (Canvas, Status) = {
    if (arguments.size > 3) 
      (canvas, Status(error = true, message = "drawLine action does not excpect arguments"))
    else {
      val pixel1=Pixel(arguments(0))
      val pixel2=Pixel(arguments(1))
      //require(pixel1.x == pixel2.x || pixel1.y == pixel2.y, "Invalid line: must be horizontal or vertical")
      if (pixel1.x == pixel2.x) {
        // vertical line
        val start = Math.min(pixel1.y, pixel2.y)
        val end = Math.max(pixel1.x, pixel2.x)
        val linePixels = (start to end).map(x => Pixel(x, pixel1.y,arguments(2).charAt(0))).toVector
        val vline = canvas.updates(linePixels)
        (vline,Status())
      }
      else {
        val start = Math.min(pixel1.x, pixel2.x)
        val end = Math.max(pixel1.y, pixel2.y)
        val linePixels = (start to end).map(y => Pixel(pixel1.x, y,arguments(2).charAt(0))).toVector
        val hline = canvas.updates(linePixels)
        (hline,Status())

      }

    }
  }

    def drawLine1(arguments: Seq[String] ,canvas: Canvas): (Canvas, Status) = {
    if (arguments.size > 3) 
      (canvas, Status(error = true, message = "drawLine action does not excpect arguments"))
    else {
      val pixel1=Pixel(arguments(0))
      val pixel2=Pixel(arguments(1))
      //require(pixel1.x == pixel2.x || pixel1.y == pixel2.y, "Invalid line: must be horizontal or vertical")
      if (pixel1.x == pixel2.x) {
        // vertical line
        val start = Math.min(pixel1.y, pixel2.y)
        val end = Math.max(pixel1.y, pixel2.y)
        //val linePixels = (start to end).map(y => Pixel(pixel1.x, y)).toVector
        val linePixels = Seq(Pixel(pixel1.x,start),Pixel(pixel1.x,end))
        
        val vline = canvas.updates(linePixels)
        (vline,Status())
      }
      else {
        val start = Math.min(pixel1.x, pixel2.x)
        val end = Math.max(pixel1.x, pixel2.x)
        val linePixels = (start to end).map(y => Pixel(pixel1.x, y)).toVector
        val hline = canvas.updates(linePixels)
        (hline,Status())

      }

    }
  }

  def drawRectangle(arguments: Seq[String] ,canvas: Canvas): (Canvas, Status) = {
    if (arguments.size > 3) 
      (canvas, Status(error = true, message = "drawRectangle action does not excpect arguments"))
    else {
      val pixel1=Pixel(arguments(0))
      val pixel2=Pixel(arguments(1))    
      val topLeft = Pixel(math.min(pixel1.x, pixel2.x), math.min(pixel1.y, pixel2.y), arguments(2).charAt(0))
      val bottomRight = Pixel(math.max(pixel1.x, pixel2.x), math.max(pixel1.y, pixel2.y), arguments(2).charAt(0))

      val horizontalPixels = (topLeft.x to bottomRight.x).map(x => Pixel(x, topLeft.y, arguments(2).charAt(0))) ++ 
                              (topLeft.x to bottomRight.x).map(x => Pixel(x, bottomRight.y, arguments(2).charAt(0)))

      val verticalPixels = (topLeft.y to bottomRight.y).map(y => Pixel(topLeft.x, y, arguments(2).charAt(0))) ++ 
                            (topLeft.y to bottomRight.y).map(y => Pixel(bottomRight.x, y, arguments(2).charAt(0)))

      val rect = canvas.updates(horizontalPixels ++ verticalPixels)
      (rect,Status())
    }
    
  }





}
