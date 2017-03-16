# DB Module for Android
This library provide you a some annotations, for help you for create a complete and easy persist data model.

This is compose by 4 modules:
>App--> this is only the sample application.

>dbprocessor--> this contains the three annotations, and generate the code for your app.

>dbMaster--> this is the Data Base interface, that is use by dbprocessor

>dbjsonimpl-->this is a implementation of dbMaster interface, that use json/serializable files for persist the information.

You can create more dbMaster implementations, for example you can create SQLLite implementation, or Realm or whatever kind of Db. This capability make that your app code dont care about the DB engine you are using.



### step 1: Add repo/dependency Root Build.gradlew
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
You must to add the plugin in your app, for annotation
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
## create model
 1º@DataTable, set alias
 2º Extend DbMasterTable, implement hashcode method(PONER EJEMPLO)
 3º Create properties of Model, and generate getter/setter 
 4º Add @DataField for each field we want include in our persisten model

### step 4: Using generated code
Is very important make a gradlew clean always you build, this is for keeping updated the generated class in apt directory. 
 
 comentar las fumadas de la migracion, como se debe cerar el nuevo modelo.
 comentar los features futuribles
 
 
 Comentar donde quedan las clases y explicar mydb y los wrapper.
 
 Para la parte del FROM(), readme de nuestro colega wagner
 h1. Coollection

*A cool way to manipulate collections in Java.*

Iterate over a collection is a medieval way to filtering, mapping and ordering. And with Java we are used to work like that. Coollection changes that, is the future, while closures don't arrives for Java.

"Download Coollection 0.2.0":http://github.com/downloads/wagnerandrade/coollection/coollection-0.2.0.jar

h2. How it works?

It's easy to use. Just add @import static com.wagnerandade.coollection.Coollection.*;@ in your class and... More nothing!

h2. How to use this?

h3. 1 - Filter

First you need a Collection. Here we create a Animal List, and we called it animals.

<pre>
List<Animal> animals;
</pre>

Later you goes add a lot of animals in this list.

Now, you want to take *all* cats, it's easy for Coollections! In this case, name is a method (@animal.name()@).

<pre>
from(animals).where("name", eq("Cat")).all();
</pre>

Or, would the *first* animal with 2 year old? Easy too!

<pre>
from(animals).where("age", eq(2)).first();
</pre>

h3. 2 - Filter specification

You can be more specific in your query, adding more specifications, like *and* and *or*.

<pre>
from(animals).where("name", eq("Lion")).and("age", eq(2)).all();
from(animals).where("name", eq("Dog")).or("age", eq(5)).all();
</pre>

h3. 3 - Matchers

There are other matchers to be precise!

<pre>
eq("Cat")
eqIgnoreCase("Cat")
contains("og")
greaterThan(3)
lessThan(10)
isNull()
</pre>

Or a special matcher, called *not*.

<pre>
not(eq("Bird"))
not(contains("at"))
not(isNull())
</pre>

h3. 4 - Order

Order is a very interesting feature to sort your collection.

<pre>
from(animals).where("name", eq("Cat")).orderBy("age").all();
from(animals).where("age", eq(5)).orderBy("name", Order.DESC).first();
</pre>

You can use just order, without filter.

<pre>
from(animals).orderBy("name");
</pre>

Be happy!
 
 Thanks to Wagner Andrade author of https://github.com/wagnerandrade/coollection that is     very usefull tool for emulate "linQ" in Java/Android and we use in our DbEngineMaster
 
That's all!!
 
 