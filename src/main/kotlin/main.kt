fun main(args: Array<String>) {
  if (args.isEmpty()) {
    printHelp()
    return
  }
  when (args[0]) {
    "add" -> add(args)
    "connect" -> connect(args)
    "list" -> list(args)
    "put" -> put(args)
    "pop" -> pop(args)
    "update" -> update(args)
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
    Store.add(args[1], args[2])
  } else {
    Store.add(args[1])
  }
}

fun connect(args: Array<String>) {
  if (args.size < 2) {
    println("No openLCA database given.")
    return
  }
  DbDir.connect(args[1])
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

fun update(args: Array<String>) {
  if (args.size < 2) {
    println("No database given.")
    return
  }
  DbDir.update(args[1])
}

fun printHelp() {
  println(
    """
dbdo is a tool for managing openLCA databases.

Usage:

    dbdo <command> [arguments]

The commands are:

    help
      prints this help

    add [thing] [name?]
      add the given thing as a database to the dbdo store (optionally
      with the specified name), where thing can be:
      - a openLCA database
      - a zolca file

    connect [db]
      connects to the given openLCA database. it starts ij which is
      an interactive REPL for working with Derby databases.

    list ["olca"?]
      list the available databases in the dbdo store, or in openLCA if
      the `olca` argument is given

    put [db]
      adds the given database to openLCA

    pop [db | "-all"]
      removes the given database from openLCA, if the all flag is 
      given it will remove all databases from openLCA

    update [db]
      runs the current (v2.0) update sequence on the given database.
"""
  )
}



