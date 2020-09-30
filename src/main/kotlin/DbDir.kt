import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.File
import org.openlca.jsonld.Json
import java.util.zip.ZipFile

object DbDir {

  fun list() {
    dbDir().listFiles()?.forEach {
      println("  ${it.name}")
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
        val target = File(dir, entry.name);
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

  fun pop(name: String) {
    val dir = File(dbDir(), name)
    if (!dir.exists()) {
      println("ERROR: $name does not exist in openLCA")
      return
    }
    dir.deleteRecursively()
    removeFromRegistry(name)
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
    val file = File(workspace(), "databases.json");
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
    val file = File(workspace(), "databases.json");
    val root = Json.readObject(file).orElse(JsonObject())
    val dbs = Json.getArray(root, "localDatabases") ?: return
    val nextDbs = JsonArray()
    for ( elem in dbs) {
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