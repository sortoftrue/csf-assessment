package ibf2022.batch2.csf.backend.repositories;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import ibf2022.batch2.csf.backend.services.UnzipUtility;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Repository
public class ImageRepository {

	@Autowired
	private AmazonS3 s3;

	// TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public JsonObject upload(MultipartFile file,
			String comments, String name, String title) throws Exception {

		
		String uuid = UUID.randomUUID().toString().substring(0, 8);
		final String digitalOceanURL = "https://james.sgp1.digitaloceanspaces.com/";
		JsonArrayBuilder urlArrayBlder = Json.createArrayBuilder();
		List<MultipartFile> photos = new LinkedList<>();
		Map<String, String> userData = new HashMap<>();
		userData.put("comments", comments);
		userData.put("name", name);
		userData.put("title", title);

		// Unzip photos
		try {
			photos = UnzipUtility.unzip(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Upload to digitalOcean

		for (MultipartFile photo : photos) {
			System.out.println(photo.getContentType());
		}

		for (MultipartFile photo : photos) {
			System.out.println("uploading photo" + photo.getSize());
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(photo.getContentType());
			metadata.setContentLength(photo.getSize());
			String fileUploadName = uuid + photo.getOriginalFilename();

			PutObjectRequest putRequest;

			try {
				putRequest = new PutObjectRequest(
						"james", fileUploadName,
						photo.getInputStream(), metadata);

				putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
				s3.putObject(putRequest);
				urlArrayBlder.add(digitalOceanURL + fileUploadName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Create JsonObject for Mongo Repo

		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		String dateString = currentDate.format(dateFormat);

		JsonArray urlArray = urlArrayBlder.build();

		JsonObject result = Json.createObjectBuilder()
				.add("bundleId", uuid)
				.add("date", dateString)
				.add("title", title)
				.add("name", name)
				.add("comments", comments)
				.add("urls", urlArray)
				.build();

		System.out.println(result);

		return result;

	}
}
