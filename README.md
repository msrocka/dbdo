# dbdo
Setting up test databases for openLCA is boring. `dbdo` is a small command line tool
that helps. It manages databases as plain `*.zolca` files in a `~/.dbdo` folder.

## Usage

`dbdo help` should show this, it should be quite clear:

```
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
```

## Installation

1. Build the fat jar via `mvn package`
2. Put a shell/batch script into your path, like this

```batch
rem dbdo.bat
@echo off
java -Xmx5G -jar C:\tools\bin\dbdo-1.0-jar-with-dependencies.jar %*
```
