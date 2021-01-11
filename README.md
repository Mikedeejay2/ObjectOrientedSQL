# ObjectOrientedSQL
An attempt at an object oriented wrapper for SQL in Java.

:warning: Work in progress! This project isn't even close to being usable yet! If you really want to try to use this,
there's nothing stopping you from adding it as a dependency through Maven. 
Check [this POM file](https://github.com/Mikedeejay2/Mikedeejay2-Maven-Repo/blob/master/ExampleDependencyPOM.xml)
for how to use this.

### What is this project attempting to do?
Essentially, this project is trying to create a wrapper around SQL (Structured Query Language) that is object oriented, 
also serving as a way to use SQL without ever directly writing in SQL.

So instead of:
```
try
{
  PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + table_name + "`");
  ResultSet set = statement.executeQuery(); // This is what we want
}
catch(Exception e) { /* Blah blah blah */ }
```
we could simply type:
```
database.getTable("table_name"); // SAME THING!
```
It would be beautiful, simple, and amazing. This is my attempt at that.

### Overall Goal
* Use OOP for everything
* Implement most / all features of SQL in the form of OOP
* Keep a low memory overhead (Not a lot of objects, don't grab data from SQL until it's requested, etc)
