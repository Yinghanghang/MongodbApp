package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Sorts.descending;
//import com.mongodb.diagnostics.logging.Logger;
import static com.mongodb.client.model.Filters.eq;


public class App {
    private static MongoDatabase trafficDB;
    private static MongoCollection<Document> collisionCollection;
    private static final String[] numField = new String[]{  "CASE_ID",   "ACCIDENT_YEAR",   "PROC_DATE",   "JURIS",    "COLLISION_DATE",    "COLLISION_TIME", 
    "OFFICER_ID",    "REPORTING_DISTRICT",    "DAY_OF_WEEK",    "CHP_SHIFT",    "POPULATION",   "CNTY_CITY_LOC",    "SPECIAL_COND",    "BEAT_TYPE",   
     "CHP_BEAT_TYPE",   "CHP_BEAT_CLASS”,    “DISTANCE", "COLLISION_SEVERITY",    "NUMBER_KILLED",    "NUMBER_INJURED",    "PARTY_COUNT",    
     "PCF_VIOL_CATEGORY",    "PCF_VIOLATION",    "CHP_ROAD_TYPE",    "CHP_VEHTYPE_AT_FAULT",    "COUNT_SEVERE_INJ”, “COUNT_VISIBLE_INJ",  
    "COUNT_COMPLAINT_PAIN",    "COUNT_PED_KILLED",    "COUNT_PED_INJURED",    "COUNT_BICYCLIST_KILLED",    "COUNT_BICYCLIST_INJURED",  
    "COUNT_MC_KILLED”, “COUNT_MC_INJURED" } ;

    private static List<String> numList = new ArrayList<>(Arrays.asList(numField));

    public static void main( String[] args ){      
        //String connectionString = "mongodb://54.86.18.237:27022";
        String connectionString = "mongodb://127.0.0.1:27017";
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE); 
            
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            trafficDB = mongoClient.getDatabase("traffic");
            collisionCollection = trafficDB.getCollection("collision");

            Scanner scanner = new Scanner(System.in);
            System.out.println();
            int input = 0;

            while(input != 16){
                System.out.println("1.    Insert a new collision record");
                System.out.println("2.    Update a collision record");
                System.out.println("3.    Delete a collision record");
                System.out.println("4.    Find a collision record");
                System.out.println("5.    Get the number of collisions and killed victims each year");
                System.out.println("6.    Find the weather that causes the most collisions");
                System.out.println("7.    Get the percentage of fatal collisions with alcohol involved");    
                System.out.println("8.    Find the day of the week that causes the most traffic collisions");      
                System.out.println("9.    Find the traffic violation types that cause the most collisions");   
                System.out.println("10.   Find the effect of lighting on traffic collisions");   
                System.out.println("11.   Find the effect of road condition on traffic collisions");   
                System.out.println("12.   Find the most common collision type");   
                System.out.println("13.   Find the collision type that causes the most deaths");   
                System.out.println("14.   Get the collision severity distribution among all collisions");   
                System.out.println("15.   Get the collision severity distribution with motorcycle involved");   
                System.out.println("16.   Exit");
                System.out.print ("Please enter your choice:  ");

                while(!scanner.hasNextInt()){
                    System.out.print("Invalid input, please enter again:  ");
                    scanner.next();
                }
                input = scanner.nextInt();
                switch (input) {
                    case 1:
                        System.out.println();
                        insertRecord();
                        System.out.println();
                        break;
                    case 2:
                        System.out.println();
                        updateRecord();
                        System.out.println();
                        break;
                    case 3:
                        System.out.println();
                        deleteRecord();
                        System.out.println();
                        break;
                    case 4:
                        System.out.println();
                        findRecord();
                        System.out.println();
                        break;
                    case 5:
                        System.out.println();
                        countCollisionsAndVictims();
                        System.out.println();
                        break;    
                    case 6:
                        System.out.println();
                        weatherAnalysis();
                        System.out.println();
                        break;
                    case 7:
                        System.out.println();
                        alcoholAnalysis();
                        System.out.println();
                        break;    
                    case 8:
                        System.out.println();
                        weekdayAnalysis();
                        System.out.println(); 
                        break;   
                    case 9:
                        System.out.println();
                        violationTypeAnalysis();
                        System.out.println();     
                        break;     
                    case 10:
                        System.out.println();
                        lightingAnalysis();
                        System.out.println();      
                        break;   
                    case 11:
                        System.out.println();
                        roadConditionAnalysis();
                        System.out.println();     
                        break;   
                    case 12:
                        System.out.println();
                        mostCommonCollision();
                        System.out.println();  
                        break;   
                    case 13:
                        System.out.println();
                        collisionTypeAnalysis();
                        System.out.println();    
                        break;   
                    case 14:
                        System.out.println();
                        collisionSeverityAnalysis();
                        System.out.println();       
                        break;   
                    case 15:
                        System.out.println();
                        motorcycleAnalysis();
                        System.out.println();   
                        break;   
                    case 16:
                        break;
                    default:
                        System.out.println();
                        System.out.println("Error, invalid input");
                        System.out.println();
                        break;
                }
                System.out.println();
            } 

            scanner.close();
            // close mongodb connection    
        }catch (Exception e) {
            System.out.println("operation failed");
            return;
        }     
        System.out.println("Thank for using our service. Bye");   
    }


    private static void insertRecord(){
        Scanner scan = new Scanner(System.in);
        Document record = new Document();

        int caseId;
        System.out.print("Please enter the case id: ");

        while (!scan.hasNextInt()) {
            System.out.print("Invalid case id. Please enter a number:  ");
            scan.next();
        }

        caseId = scan.nextInt();
        record.append("CASE_ID", caseId);

        int choice = 0;
        while(choice != 2) {
            System.out.println("1.     Add field");
            System.out.println("2.     Done");
            System.out.print("Please enter your choice: ");

            while (!scan.hasNextInt()) {
                System.out.print("Invalid choice. Please enter a number:  ");
                scan.next();
            }

            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    addFields(record);
                    System.out.println();
                    break;
                case 2:
                    System.out.println();
                    break;
                default:
                     System.out.println("Invalid option. Please enter a number: ");
                     System.out.println();
                     break;
            }
        }
        
        try{
            collisionCollection.insertOne(record);
            System.out.println("record inserted successfully");
            System.out.println(record.toJson());
        }
        catch (Exception e) {
            System.out.println("Insertion failed. Duplicate CASE_ID error");
            return;
        }    
    }

    private static void updateRecord(){
        Scanner scan = new Scanner(System.in);
        int caseId;
        Bson updates;
        System.out.print("Please enter the case id: ");

        while (!scan.hasNextInt()) {
            System.out.print("Invalid case id. Please enter a number:  ");
            scan.next();
        }

        caseId = scan.nextInt();
        Bson filter = Filters.eq("CASE_ID", caseId);

        int choice = 0;
        while(choice != 2) {
            System.out.println("1.     Update field");
            System.out.println("2.     Done");
            System.out.print("Please enter your choice: ");

            while (!scan.hasNextInt()) {
                System.out.print("Invalid choice. Please enter a number:  ");
                scan.next();
            }

            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    updates = updateFields();
                    collisionCollection.updateMany(filter, updates);
                    System.out.println();
                    break;
                case 2:
                    System.out.println();
                    break;
                default:
                     System.out.println("Invalid option. Please enter a number: ");
                     System.out.println();
                     break;
            }
        }

        //collisionCollection.updateOne(Filters.eq("CASE_ID", 100000000), Updates.set("TYPE_OF_COLLISION", 'D'));
        System.out.println("record update successfully");
        System.out.println("record after update: ");
        List<Document> records = collisionCollection.find(eq("CASE_ID", 100000000)).into(new ArrayList<>());
        for(Document record: records){
            System.out.println(record.toJson());
        }        
    }

    private static void findRecord(){
        String caseId;
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("Please enter the case id: ");
            caseId = scan.nextLine().trim();
            if (caseId.isEmpty()) {
                System.out.println("Case id cannot be empty");
                System.out.println();
                continue;
            }

            try{
                Integer.parseInt(caseId);
            }catch (Exception e){
                System.out.println("Error: The case id is invalid");
                System.out.println();
                continue;
            }
            break;
        }

        List<Document> records = collisionCollection.find(eq("CASE_ID", Integer.parseInt(caseId))).into(new ArrayList<>());
        if (records.size() == 0) {
            System.out.println("No results found.");
        } else{
            for(Document record: records){
                System.out.println(record.toJson());
            }  
        }            
    }

    private static void deleteRecord(){
        String caseId;
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("Please enter the case id: ");
            caseId = scan.nextLine().trim();
            if (caseId.isEmpty()) {
                System.out.println("Case id cannot be empty");
                System.out.println();
                continue;
            }

            try{
                Integer.parseInt(caseId);
            }catch (Exception e){
                System.out.println("Error: The case id is invalid");
                System.out.println();
                continue;
            }
            break;
        }

        collisionCollection.deleteMany(Filters.eq("CASE_ID", Integer.parseInt(caseId)));
        System.out.println("Record deleted successfully.");
    }

    private static void weatherAnalysis(){
        long totalDocument = collisionCollection.countDocuments();

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.group("$WEATHER_1", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("weather", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalDocument
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")),
                Aggregates.limit(3)
            )
        );

        System.out.println(
            "A - Clear;  B - Cloudy;  C - Raining;  D - Snowing;"
            + "\n" + "E - Fog;  F - Other;  G - Wind;  -  - Not Stated\n");

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void countCollisionsAndVictims() {
        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.group("$ACCIDENT_YEAR", Accumulators.sum("Number_Collisions", 1),Accumulators.sum("Number_Killed", "$NUMBER_KILLED")),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Accident Year", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("Number_Collisions"),
                        Projections.include("Number_Killed")                                                 
                    )
                ),
                Aggregates.sort(Sorts.ascending("Accident Year"))
            )
        );

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void alcoholAnalysis() {
        long totalCollisionSeverity  = collisionCollection.countDocuments(eq("COLLISION_SEVERITY", 1));

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.match(Filters.eq("COLLISION_SEVERITY", 1)),
                Aggregates.group("$ALCOHOL_INVOLVED", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Alcohol Involved", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalCollisionSeverity
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")) 
            )
        );

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void weekdayAnalysis() {
        long totalDocument = collisionCollection.countDocuments();

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.group("$DAY_OF_WEEK", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Day_Of_Week", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalDocument
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")) 
            )
        );

        System.out.println("1 - Monday;  2 - Tuesday; 3 - Wednesday; 4 - Thursday;  5 - Friday; 6 - Saturday; 7 - Sunday\n");
        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void violationTypeAnalysis() {
        long totalDocument = collisionCollection.countDocuments();

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.group("$PCF_VIOL_CATEGORY", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Violation Type", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalDocument
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")),
                Aggregates.limit(5)
            )
        );

        System.out.println(
        "01 - Driving or Bicycling Under the Influence of Alcohol or Drug;  02 - Impeding Traffic" 
        + "\n" + "03 - Unsafe Speed; 04 - Following Too Closely;  05 - Wrong Side of Road; 06 - Improper Passing"
        + "\n" + "07 - Unsafe Lane Change; 08 - Improper Turning;  09 - Automobile Right of Way"
        + "\n" + "10 - Pedestrian Right of Way;  11 - Pedestrian Violation; 12 - Traffic Signals and Signs"
        + "\n" + "13 - Hazardous Parking;  14 - Lights;  15 - Brakes\n");
        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void mostCommonCollision() {
        long totalDocument = collisionCollection.countDocuments();

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.group("$TYPE_OF_COLLISION", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Collision Type", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalDocument
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")),
                Aggregates.limit(5)
            )
        );

        System.out.println("A - Head-On;  B - Sideswipe;  C - Rear End;  D - Broadside;  E - Hit Object"
        + "\n" + "F - Overturned;  G - Vehicle/Pedestrian;  H - Other;  -  - Not Stated\n");

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }

    }

    private static void collisionTypeAnalysis() {
        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.match(Filters.eq("COLLISION_SEVERITY", 1)),
                Aggregates.group("$TYPE_OF_COLLISION", Accumulators.sum("Fatal_Cases", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Collision Type", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("Fatal_Cases")                                                 
                    )
                ),
                Aggregates.sort(descending("Fatal_Cases")),
                Aggregates.limit(3) 
            )
        );

        System.out.println("A - Head-On;  B - Sideswipe;  C - Rear End;  D - Broadside;  E - Hit Object"
        + "\n" + "F - Overturned;  G - Vehicle/Pedestrian;  H - Other;  -  - Not Stated\n");

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void roadConditionAnalysis() {
        long totalDocument = collisionCollection.countDocuments();

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.group("$ROAD_SURFACE", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Road Condition", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalDocument
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")),
                Aggregates.limit(3)
            )
        );

        System.out.println("A - Dry;  B - Wet;  C - Snowy or Icy;  D - Slippery (Muddy, Oily, etc.;  -  - Not Stated\n");

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }   
    }

    private static void lightingAnalysis() {
        long totalDocument = collisionCollection.countDocuments();

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.group("$LIGHTING", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Lighting Type", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalDocument
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")),
                Aggregates.limit(3)
            )
        );

        System.out.println(
        "A - Daylight;  B - Dusk - Dawn;  C - Dark - Street Lights; D - Dark - No Street Lights"
        + "\n" +   "E - Dark - Street Lights Not Functioning ; -  - Not Stated\n");

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void collisionSeverityAnalysis() {
        long totalDocument = collisionCollection.countDocuments();

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.group("$COLLISION_SEVERITY", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Collision Severity", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalDocument
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")) 
            )
        );

        System.out.println("1 - Fatal; 2 - Injury (Severe); 3 - Injury (Other Visible); 4 - Injury (Complaint of Pain); 0 - Property Damage Only\n");

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void motorcycleAnalysis() {
        long totalMotoDocs = collisionCollection.countDocuments(eq("MOTORCYCLE_ACCIDENT", "Y"));

        AggregateIterable<Document> output = collisionCollection.aggregate(
            Arrays.asList(
                Aggregates.match(Filters.eq("MOTORCYCLE_ACCIDENT", "Y")),
                Aggregates.group("$COLLISION_SEVERITY", Accumulators.sum("count", 1)),
                Aggregates.project(
                    Projections.fields(                       
                        Projections.computed("Collision Severity", "$_id"),    
                        Projections.excludeId(), 
                        Projections.include("count"),                               
                        Projections.computed(                                                      
                            "percentage", 
                            new Document("$concat", Arrays.asList(
                                new Document("$substr", Arrays.asList(
                                    new Document("$multiply", Arrays.asList(
                                        new Document("$divide", Arrays.asList(
                                            "$count", totalMotoDocs
                                        )),
                                        100
                                    )),
                                    0,
                                    2
                                )),
                                "",
                                "%"
                            ))
                        )                        
                    )
                ),
                Aggregates.sort(descending("count")) 
            )
        );

        System.out.println("1 - Fatal; 2 - Injury (Severe); 3 - Injury (Other Visible); 4 - Injury (Complaint of Pain); 0 - Property Damage Only\n");

        for (Document doc : output) {
            System.out.println(doc.toJson());
        }
    }

    private static void addFields(Document record){
        Scanner scan = new Scanner(System.in);      
        String fieldName, fieldValue;
       
        while (true) {
            System.out.print("Please enter the field name: ");
            fieldName = scan.nextLine().trim();
            if (fieldName.isEmpty()) {
                System.out.println("field name cannot be empty");
                System.out.println();
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Please enter the field value: ");
            fieldValue = scan.nextLine().trim();
            if (fieldValue.isEmpty()) {
                System.out.println("field value cannot be empty");
                System.out.println();
                continue;
            }
            break;
        }

        if(numList.contains(fieldName)){
            record.append(fieldName, Integer.parseInt(fieldValue));
        }else {
            record.append(fieldName, fieldValue);
        }                  
    }    

    private static Bson updateFields(){
        Scanner scan = new Scanner(System.in);      
        String fieldName, fieldValue;
        Bson updates;
       
        while (true) {
            System.out.print("Please enter the field name: ");
            fieldName = scan.nextLine().trim();
            if (fieldName.isEmpty()) {
                System.out.println("field name cannot be empty");
                System.out.println();
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Please enter the field value: ");
            fieldValue = scan.nextLine().trim();
            if (fieldValue.isEmpty()) {
                System.out.println("field value cannot be empty");
                System.out.println();
                continue;
            }
            break;
        }

        if(numList.contains(fieldName)){
            updates = Updates.set(fieldName, Integer.parseInt(fieldValue));
        }else {
            updates = Updates.set(fieldName, fieldValue);
        }    
        return updates;              
    }    

}    


