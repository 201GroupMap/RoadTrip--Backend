package Test;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

public class MongoDB_Test {
	private MongoDatabase database;
	
	MongoDB_Test() {
		MongoClientURI connectionString = new MongoClientURI("mongodb://201team:roadtrip@ds153015.mlab.com:53015/201roadtrip");
		MongoClient mongoClient = new MongoClient(connectionString);
		database = mongoClient.getDatabase("201roadtrip");
		System.out.println("Connected to database");
//		MongoCollection<Document> collection = database.getCollection("counter");
//		Document doc = new Document("user_counter", "0")
//                .append("itin_counter", "0");
//                
//		collection.insertOne(doc);
		insertNewItin("zexia", "exampletrip", 500);
	}
	
	public void insertNewUser(String name, String oauth) {
		MongoCollection<Document> collection = database.getCollection("user");
		Document doc = new Document("oauth", oauth).append("name", name)
				.append("my_itineraries", Arrays.asList())
				.append("shared_itineraries", Arrays.asList());
		collection.insertOne(doc);
	}
	
	public void insertNewItin(String owner_name, String name, int total_time) {
		MongoCollection<Document> collection = database.getCollection("itinerary");
		Document doc = new Document("owner_name", owner_name).append("name", name)
				.append("shared_users", Arrays.asList())
				.append("stops", Arrays.asList())
				.append("lastModified", new Date())
				.append("total_trip_time", total_time);
		collection.insertOne(doc);
		ObjectId oid = (ObjectId) collection.find(and(eq("owner_name", owner_name), eq("name", name))).first().get("_id");
		MongoCollection<Document> collection2 = database.getCollection("user");
		ArrayList<ObjectId> itins = (ArrayList<ObjectId>) collection2.find(eq("name", owner_name)).first().get("my_itineraries");
		itins.add(oid);
		collection2.updateOne(eq("name",owner_name), set("my_itineraries", Arrays.asList(itins)));
	}
	
	/**
	 * @param name
	 * @return my itineraries in an arraylist of document
	 */
	public ArrayList<Document> loadMyItineraries(String name) {
		MongoCollection<Document> collection = database.getCollection("user");
		MongoCollection<Document> collection2 = database.getCollection("itinerary");
		ArrayList<ObjectId> itins = (ArrayList<ObjectId>) collection.find(eq("name", name)).first().get("my_itineraries");
		ArrayList<Document> res = new ArrayList<Document>();
		for(ObjectId oid: itins) {
			Document doc = collection2.find(eq("_id", oid)).first();
			res.add(doc);
		}
		return res;
	}
	
	public static void main(String [] args) {
		MongoDB_Test mytest = new MongoDB_Test();
	}
}
