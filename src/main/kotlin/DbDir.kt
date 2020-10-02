import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.openlca.core.database.IDatabase
import org.openlca.core.database.derby.DerbyDatabase
import org.openlca.core.database.upgrades.Upgrades
import org.openlca.jsonld.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


object DbDir {

  fun list() {
    dbDir().listFiles()?.forEach {
      println("  ${it.name}")
    }
  }

  fun exists(name: String): Boolean {
    dbDir().listFiles()?.forEach {
      if (name.equals(it.name, ignoreCase = true))
        return true
    }
    return false
  }

  /**
   * Update the database with the given name in the
   * database folder.
   */
  fun update(name: String) {
    if (!exists(name)) {
      println("ERROR: an openLCA database `$name` does not exist")
      return
    }
    open(name).use { db ->
      Upgrades.on(db)
    }
  }

  private fun open(name: String): IDatabase {
    val dir = File(dbDir(), name)
    return DerbyDatabase(dir)
  }

  /**
   * Export the given database to a zolca file.
   */
  fun export(name: String, zolca: File) {
    if (zolca.exists()) {
      println("ERROR: the file $zolca already exists.")
      return
    }
    val dir = File(dbDir(), name)
    if (!dir.exists()) {
      println("ERROR: the database $name does not exist")
      return
    }
    val zip: Path = zolca.toPath()

    ZipOutputStream(Files.newOutputStream(zip)).use { stream ->
      val dbPath: Path = dir.toPath()
      try {
        Files.walk(dbPath)
          .filter { path -> !Files.isDirectory(path) }
          .forEach { path ->
            val zipEntry = ZipEntry(dbPath.relativize(path).toString())
            stream.putNextEntry(zipEntry)
            Files.copy(path, stream)
            stream.closeEntry()
          }
      } catch (e: Exception) {
        println("ERROR: database export failed: ${e.message}")
      }
    }
  }

  fun put(name: String) {
    val file = Store.file(name)
    if (!file.exists()) {
      println("ERROR: $name is not an existing database")
      return
    }
    val dbName = Store.name(file)
    val dir = File(dbDir(), dbName)
    if (dir.exists()) {
      println("ERROR: $dbName already exists in openLCA")
    }
    try {
      val zip = ZipFile(file)
      for (entry in zip.entries()) {
        if (entry.isDirectory)
          continue
        val target = File(dir, entry.name)
        target.parentFile.mkdirs()
        target.outputStream().use { out ->
          zip.getInputStream(entry).use { it.copyTo(out) }
        }
      }
    } catch (e: Exception) {
      println("ERROR: failed to extract db: " + e.message)
      return
    }
    addToRegistry(dbName)
  }

  /**
   * Removes the database with the given name from openLCA.
   */
  fun pop(name: String) {
    val dir = File(dbDir(), name)
    if (!dir.exists()) {
      println("ERROR: $name does not exist in openLCA")
      return
    }
    dir.deleteRecursively()
    removeFromRegistry(name)
  }

  /**
   * Removes all databases from openLCA.
   */
  fun popAll() {
    dbDir().listFiles()?.forEach {
      pop(it.name)
    }
  }

  private fun workspace(): File {
    val home = File(System.getProperty("user.home"))
    val dir = File(home, "openLCA-data-1.4")
    if (!dir.exists()) {
      dir.mkdirs()
    }
    return dir
  }

  private fun dbDir() = File(workspace(), "databases")

  private fun addToRegistry(name: String) {
    val file = File(workspace(), "databases.json")
    val root = Json.readObject(file).orElse(JsonObject())
    var dbs = Json.getArray(root, "localDatabases")
    if (dbs == null) {
      dbs = JsonArray()
    }
    val dbObj = JsonObject()
    dbObj.addProperty("name", name)
    dbs.add(dbObj)
    root.add("localDatabases", dbs)
    Json.write(root, file)
  }

  private fun removeFromRegistry(name: String) {
    val file = File(workspace(), "databases.json")
    val root = Json.readObject(file).orElse(JsonObject())
    val dbs = Json.getArray(root, "localDatabases") ?: return
    val nextDbs = JsonArray()
    for (elem in dbs) {
      if (!elem.isJsonObject)
        continue
      val db = elem.asJsonObject
      if (name.equals(Json.getString(db, "name"), ignoreCase = true))
        continue
      nextDbs.add(db)
    }
    root.add("localDatabases", nextDbs)
    Json.write(root, file)
  }

}