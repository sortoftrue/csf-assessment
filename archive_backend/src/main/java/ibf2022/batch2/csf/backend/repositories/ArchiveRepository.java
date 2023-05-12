package ibf2022.batch2.csf.backend.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.csf.backend.models.Summary;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Repository
public class ArchiveRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	// TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public Object recordBundle(JsonObject bundleData) throws Exception {


		Document toUpload = Document.parse(bundleData.toString());
		Document newDoc = mongoTemplate.insert(toUpload, "uploads");

		System.out.println(newDoc);

		

		return null;
	}

	// TODO: Task 5
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public String getBundleByBundleId(String bundleId) {

		Criteria criteria = Criteria.where("bundleId").regex(bundleId);
		Query query = Query.query(criteria);

		Document retrievedBundle = mongoTemplate.findOne(query, Document.class, "uploads");

		if (retrievedBundle == null){
			return null;
		}

		return retrievedBundle.toJson();

	}

	// TODO: Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public String getBundles() {

		Query query = new Query();
		query.fields().exclude("_id").include("bundleId", "date", "title");

		List<Summary> retrievedBundles = mongoTemplate.find(query, Summary.class, "uploads");

		JsonArrayBuilder arrayBlder = Json.createArrayBuilder();

		for (Summary summary : retrievedBundles) {
			JsonObject summaryJSON = Json.createObjectBuilder()
					.add("bundleId", summary.getBundleId())
					.add("date",summary.getDate())
					.add("title",summary.getTitle())
					.build();
			
			arrayBlder.add(summaryJSON);
		}

		JsonArray summaryArray = arrayBlder.build();

		System.out.println(summaryArray.toString());

		return summaryArray.toString();
	}

}
