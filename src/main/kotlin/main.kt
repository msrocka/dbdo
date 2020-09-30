import java.io.File

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    printHelp()
    return
  }
  when (args[0]) {
    "add" -> add(args)
    "del" -> del(args)
    "help" -> printHelp()

    "lib" -> lib(args)
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

fun del(args: Array<String>) {
  TODO("Not yet implemented")
}

fun imp(args: Array<String>) {
  TODO("Not yet implemented")
}

fun lib(args: Array<String>) {
  TODO("Not yet implemented")
}

fun list() {
  TODO("Not yet implemented")
}

fun mount(args: Array<String>) {
  TODO("Not yet implemented")
}

fun newDB(args: Array<String>) {
  TODO("Not yet implemented")
}

fun rem(args: Array<String>) {

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



