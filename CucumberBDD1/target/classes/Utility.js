function(config){
 /**
  * This can be accessed directly on feature files
  * eg:
  * def time = Utility.getTime()
  * or
  * call Utility.wait 5
     **/
  config['Utility'] = {
           getTimestamp : function(){
               return Date.now();
           },
           waitFor : function(timeInSec){
               if(timeInSec == undefined)
                    {
                      timeInSec= 0;
                    }
                java.lang.Thread.sleep(timeInSec*1000)
            },
            replaceJsonKey : function(json,mapping){
                            objMapping = JSON.parse(mapping)
                            obj = JSON.parse(json);
                            for(var i=0; i<Object.keys(obj).length; i++){
                                for(var j=0; j<Object.keys(objMapping).length;j++) {
                                    var key = objMapping[j]["Output_File_Field"];
                                    var newkey = objMapping[j]["Input_File_Field"];
                                    obj[i][newkey] = obj[i][key];
                                    delete obj[i][key];
                                }
                            }
                            json = JSON.stringify(obj)
                            return json
                        },
            dbUtility : function(db){

                    var config = karate.read(karate.properties['user.dir'] + '/src/test/resources/dbConfig.yaml');
                    if(db == "access")
                    {
                        //print dbConfig.access;
                        return config.access;
                    }
                    if(db == "access2")
                     {
                     //print dbConfig.access;
                     return config.access2;
                     }
                    if(db == "MySQL")
                     {
                      //print dbConfig.access;
                      return config.MySQL;
                      }
                    if(db == "sql")
                    {
                        return dbConfig.sql;
                    }
                },
             validateDateFormat : function(s){
                var SimpleDateFormat = Java.type("java.text.SimpleDateFormat");
                var sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                  sdf.parse(s).time;
                  return true;
                } catch(e) {
                  karate.log('*** invalid date string:', s);
                  return false;
                }
              },
              writeToFile : function(strContent,filename){
              var time = java.lang.System.currentTimeMillis();
               var csvPath = karate.properties['user.dir']; + '/target/'+ filename + ".csv"
                karate.write(strContent, csvPath);
               }
            }

return config;
}