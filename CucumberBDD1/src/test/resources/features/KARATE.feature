Feature: Karate


#UI Test Demo

Scenario: Test with Google
Given I navigate to "http:\\google.com"
Then I enter "search abcdefghijklmnopqrstuvwxyz" into input field general.google_search


# REST Service Test Demo

Scenario: Fetch Name Of Country using a REST Service
Given url 'https://restcountries.eu/rest/v2/capital'
And path '/washington'
When method GET
Then status 200
Then match response[*].name == ["United States of America"]




# SOAP Service Test Demo
Scenario: Fetch Holiday List using a SOAP Service and validate Veterans Day exists in the response
Given url 'http://www.holidaywebservice.com/HolidayService_v2/HolidayService2.asmx'
And def requestXML = read(karate.properties['user.dir'] + '/src/test/resources/request.xml')
Given request requestXML
And header Content-Type = 'text/xml'
When method POST
Then status 200
And print 'response: ', response
Then match //Holiday[1]/HolidayCode == "VETERANS-DAY-ACTUAL"




# SOAP Service Test Demo with DataDriven and generic xml
Scenario Outline: Fetch Holiday List using a SOAP Service and validate the number of Federal Holidays in different months.
Given url SOAP_HOST
* def requestXML = read(karate.properties['user.dir'] + '/src/test/resources/requestDataParameterized.xml')
* def month = '<month>'
* replace requestXML
|token          |value|
|${holidayMonth}|<month>|
And request requestXML
And header Content-Type = 'text/xml'
When method POST
Then status 200
And print 'response: ', response
* match response count(//Holiday) == <count>
Examples:
| month | count|
|   1   |    4 |
|   2   |    7 |
|   5   |    1 |



#Access Database Validation: Call a REST service and validate access Database

Scenario: Validate rest response against Access DB
Given url REST_HOST
Given path 'US'
When method GET
Then status 200
#And match response[*].name == ["United States of America"]
Then def country = response.RestResponse.result.name
And def db = "access"
And def config = Utility.dbUtility(db)
And def DbUtil = Java.type('karate.DbUtil')
And def db = new DbUtil(config)
And def sqlQuery = db.readRow("SELECT * FROM CUSTOMER where FirstName='RAKTIM'")
Then match sqlQuery.COUNTRY == country
#Then match sqlQuery contains { COUNTRY: 'United States Of America'}



#Database Validation: SQL DB
Scenario: Database Validation SQL DB
Given url 'http://services.groupkt.com/country/get/iso2code/IN'
When method GET
Then status 200
And match response.RestResponse.result.name == 'India'
* def name = 'RAKTIM'
* def config = { username: 'US\\VS197HG', password: 'R@ktim2019',db:'sql', url: 'jdbc:sqlserver://localhost\\SQLEXPRESS',instanceName:SQLEXPRESS,databaseName:'Demo',driverClassName:'com.microsoft.sqlserver.jdbc.SQLServerDriver' }
* def DbUtil = Java.type('karate.DbUtil')
* def db = new DbUtil(config)
* def sqlQuery = db.readValue("SELECT STATE FROM tblCustomer where FirstName='RAKTIM'")
    #    * def config = { username: 'US\VS197HG', password: 'R@ktim2018', url: 'jdbc:sqlserver://US1232885W2\SQLEXPRESS',instanceName:SQLEXPRESS,integratedSecurity:true,databaseName:'Demo',driverClassName:driverClassName:'com.microsoft.sqlserver.jdbc.SQLServerDriver' }
  #'net.ucanaccess.jdbc.UcanaccessDriver'}





#Scenario: Multiple codes
Scenario Outline: Validate Country codes for multiple countries
  * url REST_HOST
Given path '<Code>'
When method GET
Then status 200
And match response.RestResponse.result.name == '<ExpCountry>'
Examples:
| Code|ExpCountry|
| IN  |  India   |
| CA  | Canada   |
| US  |United States of America|


#Schema Validation: Validate JSON response against schema
Scenario: JSON Schema Validation
Given url 'https://holidayapi.pl/v1/holidays?country=US&year=2018'
When method GET
Then status 200
And match each response.holidays.[*][*] ==
"""
  {
     "name": '#string',
      "country" : '#regex [A-Z]{2}',
      "date" : '#? isDateValid(_)',
      "public" : '#boolean'

  }
"""



#Datadriven Testing: Reading test data from CSV File.
Scenario Outline: Data Driven Testing using CSV
Given url SOAP_HOST
And def requestXML = read(karate.properties['user.dir'] + '/src/test/resources/requestDataParameterized.xml')
And def month = <month>
And replace requestXML
      |token          |value|
      |${holidayMonth}| month|
And request requestXML
And header Content-Type = 'text/xml'
When method POST
Then status 200
And print 'response: ', response
Then match response count(//Holiday) == <count>
Examples:
| read('C:/Users/vs197hg/Desktop/CucumberBDD1/src/test/resources/testData.csv') |



#Compare 2 different Access Database
Scenario: Validate two  Access DBs
Given  def db = "access"
And def config = Utility.dbUtility(db)
And def DbUtil = Java.type('karate.DbUtil')
And def db = new DbUtil(config)
And  def sqlQuery = db.readRow("SELECT FirstName,SSN FROM CUSTOMER where SSN=900808080")
Given  def db2 = "access2"
And def config = Utility.dbUtility(db2)
And def DbUtil = Java.type('karate.DbUtil')
And def db2 = new DbUtil(config)
And def sqlQuery2 = db2.readRow("SELECT FirstName,SSN FROM SELLER where SSN= 900808080")
Then match sqlQuery == sqlQuery2
Then print sqlQuery
Then print sqlQuery2





#ETL : Compare Access Database and MySQL  Database
Scenario: Validate Database  Access DB and MySQL Database
  Given  def db = "access"
  And def config = Utility.dbUtility(db)
  And def DbUtil = Java.type('karate.DbUtil')
  And def db = new DbUtil(config)
  And  def sqlRes1 = db.readRow("SELECT FirstName,Last_Name FROM CUSTOMER where SSN=900808080")
  Then print sqlRes1
  Given def db2 = "MySQL"
  And def config = Utility.dbUtility(db2)
  And def DbUtil = Java.type('karate.DbUtil')
  And def db2 = new DbUtil(config)
  And def sqlRes2 = db2.readRow("SELECT FullName FROM seller where SSN= 900808080")
  Then print sqlRes2
  And def sqlRes = sqlRes1.FirstName+' '+sqlRes1.Last_Name
  Then match sqlRes == sqlRes2.FullName






# ETL SCenario :Compare two databases using a CSV mapping sheet.

  Scenario Outline: Validate Database  Access DB and MySQL Database using CSV mapping file
    Given  def db = "access"
    And def config = Utility.dbUtility(db)
    And def DbUtil = Java.type('karate.DbUtil')
    And def db = new DbUtil(config)
    And  def sqlRes1 = db.readRow(<Query1>)
    Then print sqlRes1
    Given def db2 = "MySQL"
    And def config = Utility.dbUtility(db2)
    And def DbUtil = Java.type('karate.DbUtil')
    And def db2 = new DbUtil(config)
    And def sqlRes2 = db2.readRow(<Query2>)
    Then print sqlRes2
    And def db1Mapping = <Database1>
    Then match db1Mapping == <Database2>
  Examples:
  |read('C:/Users/vs197hg/Desktop/CucumberBDD1/src/test/resources/mapping.csv')|




#Call different feature
  Scenario: Call another Feature
  Given def rootPath = karate.properties['user.dir']
  And def featureResponse = call read(rootPath+'/src/test/resources/features/Country.feature') {country: 'United States of America'}
  Then match featureResponse.response.RestResponse.result.alpha3_code == 'USA'



# ETL SCenario :Compare two databases for mutiple records.

  Scenario Outline: Validate Database  Access DB and MySQL Database using CSV mapping
    Given  def db = "access"
    And def config = Utility.dbUtility(db)
    And def DbUtil = Java.type('karate.DbUtil')
    And def db = new DbUtil(config)
    And def sqlRes1 = db.readRows(<db1_query1>)
    And def db1RecordCount = sqlRes1.length
    Then print sqlRes1
    Given def db2 = "MySQL"
    And def config = Utility.dbUtility(db2)
    And def DbUtil = Java.type('karate.DbUtil')
    And def db2 = new DbUtil(config)
    And def sqlRes2 = db2.readRows(<db2_query2>)
    And print sqlRes2
    And  def db2RecordCount = sqlRes2.length
    Then match db1RecordCount == db2RecordCount
    And def json = Java.type('karate.JsonCompare')
    And def js = new json()
    Then def res = js.CompareJSON(sqlRes1,sqlRes2,"FULLNAME_DB1","FullName")
    Examples:
      |read('C:/Users/vs197hg/Desktop/CucumberBDD1/src/test/resources/mapping2.csv')|


  Scenario: Validate CSV and Database
    And def data1 = read('C:/Users/vs197hg/Desktop/CucumberBDD1/src/test/resources/TradeBlotter_041719.csv')
    And def data2 = read('C:/Users/vs197hg/Desktop/CucumberBDD1/src/test/resources/DSO Query Results Check.csv')
    And print data1
    #And def data3 = Utility.replaceJsonKey(data2)
    And print data3

  @karate
  Scenario: Validate CSV Files
    Given def sourceSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/TradeBlotter_TROW.csv')
    Given def targetSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Day_1_JUAT_Results.csv')
    Given def mappingSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Mapping_POC_1.csv')
    Given def transformationData = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Transformation.json')
    When def targetHeaderUpdated = Utility.replaceJsonKey(targetSheet,mappingSheet)
    And def csv = Java.type('karate.DataCompareTransaction')
    And def js = new csv()
    Then def result = js.compareData(sourceSheet,targetHeaderUpdated,mappingSheet,"ClientRefID",transformationData,"Trade ID")
    Then print result
    #And def email = Java.type('karate.SendEmail')
    #And def js1 = new email()
    #Then def emailResult = js1.sendAttachmentEmail('raktim.saha@ey.com', subject, body,filename)





  @Positions_cr1
  Scenario: Validate Positions Files
    Given def sourceSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Derma_test.csv')
    Given def targetSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/tonytest.csv')
    Given def mappingSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Mapping__POC2.csv')
    And print targetSheet
    When def targetHeaderUpdated = Utility.replaceJsonKey(targetSheet,mappingSheet)
    And print targetHeaderUpdated
    And def csv = Java.type('karate.TrowDataComparePosition')
    And def js = new csv()
    Then def result = js.compareData(sourceSheet,targetHeaderUpdated,mappingSheet,"CRDSecid")
    Then print result

  @transaction_run2
  Scenario: Validate Transaction Files
    Given def sourceSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/CRDextract_0528.csv')
    Given def targetSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/TradeBlotter_0530.csv')
    Given def mappingSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Mapping_TRP_C.csv')
    When def targetHeaderUpdated = Utility.replaceJsonKey(targetSheet,mappingSheet)
    And def csv = Java.type('karate.TransactionData')
    And def js = new csv()
    Then def result = js.compareData(sourceSheet,targetHeaderUpdated,mappingSheet,"CRDSecid")
    Then print result




  @transaction_run1
  Scenario: Validate Transaction Files
    Given def sourceSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Transactions/CRDextract_0606.csv')
    Given def targetSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Transactions/TradeBlotter_0606.csv')
    Given def mappingSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources//Mapping_TRP_C.csv')
    When def targetHeaderUpdated = Utility.replaceJsonKey(targetSheet,mappingSheet)
    And def csv = Java.type('karate.TransactionData')
    And def js = new csv()
    Then def result = js.compareData(sourceSheet,targetHeaderUpdated,mappingSheet,"CRDSecid")
    Then print result



  @Positions_run2
  Scenario: Validate Positions Files
    Given def sourceSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Positions/Derma_0624.csv')
    Given def targetSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Positions/TriOptima_0624.csv')
    Given def mappingSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Mapping_Utils/Mapping__Positions.csv')
    When def targetHeaderUpdated = Utility.replaceJsonKey(targetSheet,mappingSheet)
    And def csv = Java.type('karate.TrowDataComparePosition')
    And def js = new csv()
    Then def result = js.compareData(sourceSheet,targetHeaderUpdated,mappingSheet,"CRDSecid")
    Then print result




  @conversion_run1
  Scenario: Validate Conversion Files
    Given def sourceSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Conversions/TrowConversion_0611.csv')
    Given def targetSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources//Conversions/Trioptima_0611.csv')
    Given def mappingSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Mapping_Utils/Mapping_Conversion.csv')
    Given def transformationSheet = read('C:/Users/ff717qc/Desktop/CucumberBDD4/CucumberBDD1/src/test/resources/Mapping_Utils/Transformation.csv')
    When def targetHeaderUpdated = Utility.replaceJsonKey(targetSheet,mappingSheet)
    And def csv = Java.type('karate.DataCompareConversion')
     And def js = new csv()
    Then def result = js.compareData(sourceSheet,targetHeaderUpdated,mappingSheet,transformationSheet,"CRDSecid")
    Then print result














# * def config = {db: 'MySQL',username: 'postgres', password: 'pass123', url: 'jdbc:postgresql://172.17.0.11:5432/raktim_db?currentSchema=testdb=public', driverClassName: 'org.postgresql.Driver'}
  #    * def dogs = db.readRow("SELECT * FROM CUSTOMER")
#    * def dogs = db.readRow("SELECT * FROM CUSTOMER where FirstName='RAKTIM'")
#    * def dogs = db.readValue("SELECT STATE FROM CUSTOMER where FirstName='RAKTIM'")
#    * match dogs contains 'VA'
#* def ymlData = read('C:/Users/vs197hg/Desktop/CucumberBDD1/src/test/resources/Test.yaml')
#* def config = ymlData.access
  #* def config = { db:'access',url:'C:\\Users\\vs197hg\\Desktop\\Database1.accdb',driverClassName:'net.ucanaccess.jdbc.UcanaccessDriver'}

  #@karate
#  Scenario: Fetch Name Of Country using a REST Service
 #   * url 'http://services.groupkt.com/country/get/iso2code'
  #  Given path '/IN'
  # When method GET
  #  Then status 200
  #  And match response.RestResponse.result.name == 'India'

      #And  def sqlRes1 = db.readRow("SELECT FirstName,Last_Name FROM CUSTOMER where SSN=900808080")
      #And def sqlRes2 = db2.readRow("SELECT FullName FROM seller where SSN= 900808080")