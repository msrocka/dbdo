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

  /**
   * Adds the given thing as a new database to this store.
   */
  fun add(thing: String, name: String? = null) {

    // the thing is an openLCA database
    if (DbDir.exists(thing)) {
      val dbName = name ?: thing
      val file = file(dbName)
      DbDir.export(thing, file)
      return
    }

    // the thing is a zolca file
    val file = File(thing)
    if (file.exists() && file.name.endsWith(".zolca")) {
      addZolca(file, name)
      return
    }

    println("ERROR: do not know how to add $thing to the store.")
  }

  private fun addZolca(zolca: File, name: String? = null) {
    if (!zolca.exists()) {
      println("ERROR: $zolca does no exist")
      return
    }
    if (!zolca.name.endsWith(".zolca")) {
      println("ERROR: $zolca is not a zolca file")
      return
    }
    var target = zolca.name
    if (name != null) {
      target = name
    }
    val targetFile = file(target)
    if (targetFile.exists()) {
      println("ERROR: a database ${name(targetFile)} already exists")
      return
    }
    try {
      zolca.copyTo(targetFile)
    } catch (e: Exception) {
      println("ERROR: failed to copy $zolca to $target")
    }
  }

  fun del(name: String) {
    val file = file(name)
    if (!file.exists()) {
      println("ERROR: a database $name does" +
        " not exist in the dbdo store")
      return
    }
    if (!file.delete()) {
      println("ERROR: failed to delete $file")
    }
  }

  fun list() {
    dir().listFiles()?.forEach {
      println("  ${name(it)}")
    }
  }

  fun name(zolca: File) = zolca.name.removeSuffix(".zolca")

  fun file(name: String): File {
    if (!name.endsWith(".zolca")) {
      return File(dir(), name + ".zolca")
    } else {
      return File(dir(), name)
    }
  }

}