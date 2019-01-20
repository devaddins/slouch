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
* iOS (planned)

# howto
Like most (all?) cordova plugins you need to wait for the deviceready event before you can make use of this plugin.

# get 
``` js
function show(id) {
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
}
```
