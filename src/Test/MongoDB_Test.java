package Test;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB_Test {
	private MongoClient mongoClient;
	
	MongoDB_Test() {
		MongoClientURI connectionString = new MongoClientURI("mongodb://201team:roadtrip@ds153015.mlab.com:53015/201roadtrip");
		MongoClient mongoClient = new MongoClient(connectionString);
		MongoDatabase database = mongoClient.getDatabase("201roadtrip");
		System.out.println("Connected to database");
		MongoCollection<Document> collection = database.getCollection("test");
		Document doc = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));
		collection.insertOne(doc);
	}
	
	public static void main(String [] args) {
		MongoDB_Test mytest = new MongoDB_Test();
	}
}
