# dbdo
Setting up test databases for openLCA is boring. `dbdo` is a small command line tool
that helps. It manages databases in as plain `*.zolca` files in the `~/.dbdo` folder.

## Usage

`dbdo help` should show this, it should be quite clear:

```
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
```
