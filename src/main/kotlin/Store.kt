import java.io.File

/**
 * The store that contains the database templates.
 */
object Store {

  fun dir(): File {
    val home = File(System.getProperty("user.home"))
    val dir = File(home, ".dbdo")
    if (!dir.exists()) {
      dir.mkdirs()
    }
    return dir
  }

  fun add(zolca: File, name: String? = null): Boolean {
    if(!zolca.exists()) {
      println("ERROR: $zolca does no exist")
      return false
    }
    var target = zolca.name
    if (name !=null) {
      target = name;
    }
    if (!target.endsWith(".zolca")) {
      target += ".zolca"
    }
    val targetFile = File(dir(), target)
    if (targetFile.exists()) {
      println("ERROR: a database ${name(targetFile)} already exists")
      return false
    }
    try {
      zolca.copyTo(targetFile)
      return true
    } catch (e: Exception) {
      println("ERROR: failed to copy $zolca to $target")
      return false
    }
  }

  private fun name(zolca: File) = zolca.name.removeSuffix(".zolca")

}