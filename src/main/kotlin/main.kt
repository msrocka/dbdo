import java.io.File

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    printHelp()
    return
  }
  when (args[0]) {
    "add" -> add(args)
    "list" -> list(args)
    "put" -> put(args)
    "pop" -> pop(args)
    "help" -> printHelp()
    else -> println("Unknown command `${args[0]}`.")
  }
}

fun add(args: Array<String>) {
  if (args.size < 2) {
    println("No zolca file given.")
    return
  }
  if (args.size > 2) {
    Store.add(File(args[1]), args[2])
  } else {
    Store.add(File(args[1]))
  }
}

fun put(args: Array<String>) {
  if (args.size < 2) {
    println("No database given.")
    return
  }
  DbDir.put(args[1])
}

fun pop(args: Array<String>) {
  if (args.size < 2) {
    println("No database given.")
    return
  }
  val arg = args[1]
  if (arg == "-all") {
    DbDir.popAll()
  } else {
    DbDir.pop(args[1])
  }
}

fun list(args: Array<String>) {
  if (args.size > 1 && args[1] == "olca") {
    DbDir.list()
  } else {
    Store.list()
  }
}

fun printHelp() {
  println(
    """
dbdo is a tool for managing openLCA databases.

Usage:

    dbdo <command> [arguments]

The commands are:

    add   [db]           - add a database to openLCA
    del   [db]           - delete the database from the store
    help                 - show this help
    imp   [zolca]        - import a zolca file into the local store
    lib   [db]           - creates a library from a database
    list                 - list the databases
    mount [db] [lib]     - mount a library to the given database 
    new   [name] [dbs..] - create a new database, optionally from others
    rem   [db]           - remove a database from openLCA
"""
  )
}



