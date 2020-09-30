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
    if (!zolca.exists()) {
      println("ERROR: $zolca does no exist")
      return false
    }
    if (!zolca.name.endsWith(".zolca")) {
      println("ERROR: $zolca is not a zolca file")
      return false
    }
    var target = zolca.name
    if (name != null) {
      target = name;
    }
    val targetFile = file(target)
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

  fun list() {
    dir().listFiles()?.forEach {
      println("  ${name(it)}")
    }
  }

  private fun name(zolca: File) = zolca.name.removeSuffix(".zolca")

  private fun file(name: String): File {
    if (!name.endsWith(".zolca")) {
      return File(dir(), name + ".zolca")
    } else {
      return File(dir(), name)
    }
  }

}