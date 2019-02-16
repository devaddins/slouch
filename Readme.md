```
       ___                           __         
      /\_ \                         /\ \        
  ____\//\ \     ___   __  __    ___\ \ \___    
 /',__\ \ \ \   / __`\/\ \/\ \  /'___\ \  _ `\  
/\__, `\ \_\ \_/\ \L\ \ \ \_\ \/\ \__/\ \ \ \ \ 
\/\____/ /\____\ \____/\ \____/\ \____\\ \_\ \_\
 \/___/  \/____/\/___/  \/___/  \/____/ \/_/\/_/
 ```

 # slouch
 Simple Lazy couchbase lite plugin for cordova

# requirements

* Cordova
  * version 8.1+

# supports

* Android

# install
Add the plugin from the root of your cordova project

``` bash
$ cordova plugin add https://github.com/devaddins/slouch.git
```

# howto
Slouch does not require any setup.  
Like most (all?) cordova plugins you need to wait for the deviceready event before you can make use of this plugin.
There are three methods you can call all of which return a promise.

* get(slouchLocation {string})
* post(slouchLocation {string}, data {object})
* delete(slouchLocation {string})

The slouch location string takes this format

```
slouch://dbname/typename/id
```

Where 

* slouch://
  * is just the exact string "slouch://"
* dbname
  * is the couchbase lite database name
* typename
  * is the name of a type in the couchbase lite database
* id
  * is a guid representing a specific entry

for example

```
slouch://timeentries/2021-02-03/
```

could be used to represent all time entries for that date, and

```
slouch://timeentries/2021-02-03/89557d53-8aad-4ff8-9fb7-583ea8d97cdb
```

would represent a specific time entry on that day. 

# get 
The get method gets results in one of three ways.

* Entire Database
  * slouch.get('slouch://dbname')
* All entries of type
  * slouch.get('slouch://dbname/typename')
* Entry with a specific id
  * slouch.get('slouch://dbname/typename/3e32a90d-612f-4d3f-aeaf-c9e18a4246e8')

``` js
cordova.plugins.slouch.get('slouch://lifelog/logentry/' + id)
.then(e => {
    /*
        e = {
            "lifelog": {
                "text": "did the thing with the sutff",
                "_type": "logentry", 
                "_id": "b2f7bee5-99e2-4d23-8262-43185332a6a8"
            }
        }
    */
    document.write(e.lifelog.text); // or something useful
})
.catch(x => console.log(x));
```

# post
The post method will either create a new entry or update an existing entry.

* Create
  * slouch.post('slouch://dbname/typename', {foo: "bar"})
* Update
  * slouch.post('slouch://dbname/typename/f5410793-9938-45cd-be28-b6f9daa10f8e', {foo: "bar"})


``` js
    // TODO! create an example 
```

# delete
The delete method will either delete the entire database, an entire type, or a specific entry.

* Database 
  * slouch.delete("slouch://dbname/DELETE_DATABASE/DELETE_DATABASE_dbname")
* Type
  * slouch.delete("slouch://dbname/typename/DELETE_TYPE_typename")
* Entry
  * slouch.delete("slouch://dbname/typename/guid")

``` js
    // TODO! create an example 
```
