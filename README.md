 


# DB Module for Android
========================================================================
This library provide you a some annotations, for help you for create a complete and easy persist data model.

This is compose by 4 modules:
>App--> this is only the sample application.

>dbprocessor--> this contains the three annotations, and generate the code for your app.

>dbMaster--> this is the Data Base interface, that is use by dbprocessor

>dbjsonimpl-->this is a implementation of dbMaster interface, that use json/serializable files for persist the information.

You can create more dbMaster implementations, for example you can create SQLLite implementation, or Realm or whatever kind of Db. This capability make that your app code dont care about the DB engine you are using.



### step 1: Add repo/dependency Root Build.gradlew
----
We upload the dependencies to jitpack, so you need to add jitpack to your repositories, for find it.
(root build gradle)
```groovy
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" } //add this line
    }
}
```

In the same gradle file you need to add:
```groovy
 dependencies {
     ...
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8' //add this line  
  }
 ```
### step 2: ':app' build.gradle
---
You must to add the plugin in your app, for process annotations.
```groovy
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt' //add this line
...
```
And in this file too we need to add the dependencies to DbMaster and DbJsonImplementation(or another dbmaster implementation) 

```groovy
compile('com.github.GigigoGreenLabs.DBModule:dbmaster:0.09RC')
compile('com.github.GigigoGreenLabs.DBModule:dbjsonimpl:0.09RC')
```

### step 3:  Getting started, add some code
----
##### Create model
 1º@DataTable, set alias
 2º Extend DbMasterTable, implement hashcode method 
 3º Create properties of Model, and generate getter/setter 
 4º Add @DataField for each field we want include in our persisten model
 ```java
 @DataTable(alias = "Usuarios",isOnlyOneRecord = false)
public class UsersModelv2 extends
    DBTableMaster  {
    @DataField String Name;
    @DataField String Surname;
    @DataField String Age;
    ....
     @Override public int hashCode() {
      if (BuildConfig.VERSION_CODE < 19) {
        int megaHash = 0;
        megaHash = megaHash + hashCodeObject(this.getName());
        megaHash = megaHash + hashCodeObject(this.getSurname());
        megaHash = megaHash + hashCodeObject(this.getAge());
        return megaHash;
      } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
          return Objects.hash(Name, Surname,Age);
        }
      }
      return -1;
    }
 ```
 You can use the menu code->generate->generate hashcode & equals if you don't want to write your own hashcode method like the one above.

### step 4: Using generated code
Is very important make a gradlew :clean always you build, this is for keeping updated the generated class in apt directory. 

You can check if the apt is working checking if the generated class are crreated in this path of your app:

: \build\generated\source\apt\debug\com\gigigo\dbmodule\generated

The annotation processor generate one class for manage the DataBase, usually named MyDB.class and one wrapper class for each one @DataTable.
##### Init our DbEngine/MyDB.class
In MyDB we have access to all Datatable wrapper for access/modify data models.
To start using you must init the MyDb generated class, like this:
```java
 DBEngineMaster bdEngine = new DBEngineJSON(this);
 mMyDataBase = new MyDB(bdEngine);
```

First we create a DBEngineMaster with the implementation we are wish, for now only have Json implementation, but in the future we can use SQLLite and others.

The engine only need a context for work.
##### Using DataTable Wrappers, select/modify model
For access to wrapper in the MyDb we have created one method named like @DataTable Alias, with the same sample above about "Usuarios" we should have:

```java
//fill the items
 ArrayList<UsersModelv2> lst25 = new ArrayList<UsersModelv2>();
 ...
mMyDataBase.Usuarios().setItems(lst25);
//get the items
mMyDataBase.Usuarios().getItems();
//add one item
mMyDataBase.Usuarios().addItem(myNewUserModelv2);
//delete Item by index
mMyDataBase.Usuarios().delItem(indexOfItemForDelete);
//or by item 
mMyDataBase.Usuarios().delItem(itemForDelete);
//update item, by index
mMyDataBase.Usuarios().setItem(itemForUpdate,index);
//or, only with the item
mMyDataBase.Usuarios().setItem(itemForUpdate);
//find index of item
int index=mMyDataBase.Usuarios().findItem(itemForFind);

```
##### Using DataTable Wrappers, query/sort model items
We have one more feature, for make querys/selecction, we can access to this with method FROM(), somethign like this, for example:
```java
List<UsersModelv2> all = mMyDataBase.Usuarios()
            .FROM()
            .where("getName", eq("Alberto"))
            .and("SurName",eq("Sainz"))
            .orderBy("getSurname")
            .all();
```
This get you items where name is "Alberto" and surname is "Sainz". We are using a modify lib called Coollection, you can check your readme in your github repository:
https://github.com/wagnerandrade/coollection/blob/master/README.textile
  
 
 ##### Migration feature
 We implement a way for do migrations, if you need to modify the scheme of @Datatable you need create new pojo, and keep the same alias in your table. This way you can recover the previous data model and add then in the new pojo scheme.
 
 For do this migration you must to give a mapper for convert from the previous model pojo to new pojo model.
 
 If you need migration you need to init the myDb class like this:
 
 ```java
 DBEngineMaster bdEngine = new DBEngineJSON(this);
 //create mapper and implement  
    DBMapperMaster myMapper = new DBMapperMaster("UsersModel", "UsersModelv2") {
      @Override public <I, O> O convert(I input) {
        UsersModel u1 = (UsersModel) input;
        UsersModelv2 u2 =
            new UsersModelv2(u1.getName() + " Migrated", u1.getSurname() + " Migrated", "357");
        return (O) u2;
      }
    };
    //set the mapper/s to engine
    bdEngine.setMigrationMappers(myMapper);
    //create database with engine
     mMyDataBase = new MyDB(bdEngine);
     //call for execute migration before all
    mMyDataBase.migrateDB();  
  ```
  
  
 
  
 
 Thanks to Wagner Andrade author of https://github.com/wagnerandrade/coollection that is     very usefull tool for emulate "linQ" in Java/Android and we use in our DbEngineMaster
 
That's all!!
 
 

