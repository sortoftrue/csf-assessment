package ibf2022.batch2.csf.backend.controllers;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.repositories.ImageRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
public class UploadController {

	@Autowired
	ImageRepository imageRepo;

	@Autowired
	ArchiveRepository mongoRepo;

	// TODO: Task 2, Task 3, Task 4

	@PostMapping(path="/upload")
	public ResponseEntity<String> uploadPhoto(@RequestPart MultipartFile file,
	@RequestPart String comments, @RequestPart String name, @RequestPart String title){

		System.out.println(comments + name + title);

		JsonObject returnToAng=null;

		try{
			//Upload to S3
			JsonObject bundleData = imageRepo.upload(file, comments, name,title);

			//Upload to Mongo
			mongoRepo.recordBundle(bundleData);

			returnToAng = Json.createObjectBuilder().add("bundleId", bundleData.getString("bundleId")).build();
			return ResponseEntity.status(201).body(returnToAng.toString());

		} catch(Exception e){

			returnToAng = Json.createObjectBuilder().add("error", e.getMessage()).build();
			return ResponseEntity.status(500).body(returnToAng.toString());

		}
		
	}

	// TODO: Task 5

	@GetMapping(path = "/bundle/{bundleId}")
	public ResponseEntity<String> getBundle(@PathVariable String bundleId) {
		System.out.println(bundleId);

		String bundle = mongoRepo.getBundleByBundleId(bundleId);

		if (bundle == null){
			return ResponseEntity.status(404).body(Json.createObjectBuilder().add("error", "Upload not found").build().toString());
		}

		return ResponseEntity.status(200).body(bundle);
	}

	// TODO: Task 6
	@GetMapping(path = "/bundles")
	public ResponseEntity<String> getBundles() {

		String bundles = mongoRepo.getBundles();

		return ResponseEntity.status(200).body(bundles);
	}
}
