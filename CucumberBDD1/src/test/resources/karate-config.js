function() {
 var config={REST_HOST: 'http://services.groupkt.com/country/get/iso2code/IN',
 SOAP_HOST: 'http://www.holidaywebservice.com/HolidayService_v2/HolidayService2.asmx',
 USERNAME: 'DummyUN',
 PASSWORD: 'DummyTestPassword',
 url: 'Dummydriver',
 DB_USERNAME: 'dbUserName',
 DB_PASSWORD: 'dbPassword'
 };
 print(karate.env);
 switch(karate.env){
    case 'UAT':
            config.USERNAME= 'uatUser';
            config.PASSWORD= 'uatPassword';
            //config.REST_HOST= 'https://restcountries.eu/rest/v2/capital';
            config.REST_HOST= 'http://services.groupkt.com/country/get/iso2code/';
            config.SOAP_HOST= 'http://www.holidaywebservice.com/HolidayService_v2/HolidayService2.asmx';
            break;

    case 'DEV':
             config.USERNAME= "devUser";
             config.PASSWORD= "devPassword";
             config.REST_HOST= 'http://services.groupkt.com/country/get/iso2code/IN';
             config.SOAP_HOST= 'http://www.holidaywebservice.com/HolidayService_v2/HolidayService2.asmx';
             break;
    case 'PROD':
             config.USERNAME= "devUser";
             config.PASSWORD= "devPassword";
             config.REST_HOST= 'http://services.groupkt.com/country/get/iso2code/IN';
             config.SOAP_HOST= 'http://www.holidaywebservice.com/HolidayService_v2/HolidayService2.asmx';
             break;
 }
var dirPath = karate.properties['user.dir'];
config = karate.callSingle(dirPath + '/src/test/resources/Utility.js',config);
return config;
}