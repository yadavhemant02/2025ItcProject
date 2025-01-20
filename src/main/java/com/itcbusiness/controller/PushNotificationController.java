package com.itcbusiness.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@RestController
//public class PushNotificationController {
//
//	private static final String apiKey = "os_v2_app_nrnybqjbcvfp5hoykcs2icice4zatryta36uwov54ot77s2p3lrdm4jbida6dajtbceh57w6ysxsplrh4t2d5jh6ohurhmsfo3g4dwa";
//	private static final String ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications";
//	private static final String APP_ID = "6c5b80c1-2115-4afe-9dd8-50a5a4090227";
//
//	@PostMapping("/send")
//	public ResponseEntity<String> sendNotification(@RequestParam String title, @RequestParam String message,
//			@RequestParam String userId) {
//		try {
//			RestTemplate restTemplate = new RestTemplate();
//
//			// Create request headers
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Authorization", "Basic " + apiKey);
//			headers.set("Content-Type", "application/json");
//
//			// Create request body
//			Map<String, Object> body = new HashMap<>();
//			body.put("app_id", APP_ID);
//			body.put("headings", Map.of("en", title));
//			body.put("contents", Map.of("en", message));
//			body.put("include_external_user_ids", new String[] { userId }); // Send to specific user
//
//			HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//			// Send POST request
//			ResponseEntity<String> response = restTemplate.exchange(ONESIGNAL_API_URL, HttpMethod.POST, request,
//					String.class);
//
//			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("bAd" + e.getMessage(), HttpStatus.BAD_GATEWAY);
//		}
//	}
//}

//..........................

//@RestController
//public class PushNotificationController {
//	private static final String apiKey = "os_v2_app_nrnybqjbcvfp5hoykcs2icice4zatryta36uwov54ot77s2p3lrdm4jbida6dajtbceh57w6ysxsplrh4t2d5jh6ohurhmsfo3g4dwa";
//	private static final String ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications";
//	private static final String APP_ID = "6c5b80c1-2115-4afe-9dd8-50a5a4090227";
//
//
//
//	@PostMapping("/send")
//	public ResponseEntity<Map<String, Object>> sendNotification(@RequestParam String title,
//			@RequestParam String message, @RequestParam String userId) {
//		Map<String, Object> response = new HashMap<>();
//		response.put("id", "");
//
//		try {
//			if (userId == null || userId.trim().isEmpty()) {
//				response.put("errors", List.of("All included players are not subscribed"));
//				//System.out.print(userId);
//				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//			}
//
//			RestTemplate restTemplate = new RestTemplate();
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Authorization", "Basic " + apiKey);
//			headers.set("Content-Type", "application/json");
//
//			Map<String, Object> body = new HashMap<>();
//			body.put("app_id", APP_ID);
//			body.put("headings", Map.of("en", title));
//			body.put("contents", Map.of("en", message));
//			body.put("include_external_user_ids", new String[] { userId });
//
//			HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//			restTemplate.exchange(ONESIGNAL_API_URL, HttpMethod.POST, request, String.class);
//
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		} catch (Exception e) {
//			response.put("errors", List.of("All included players are not subscribed"));
//			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//		}
//	}
//}

@RestController
@RequestMapping("/api")
public class PushNotificationController {

	/**
	 * 
	 * @param title
	 * @param message for 5173
	 * @param userId
	 * @return
	 */
//	private static final String API_KEY = "os_v2_app_53d5zprosjdfnld547vtem3ewt6wkqhluv2umk5bjppvzqfokqoms2ty5abfwtv7kmvafnkwmuj6obr7mxnpv2f7kkvfhv5jf4ucudi";
//	private static final String ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications";
//	private static final String APP_ID = "eec7dcbe-2e92-4656-ac7d-e7eb323364b4";

	/**
	 * for 5175
	 */
	private static final String API_KEY = "os_v2_app_op7kecby6ja3ra3hfn5oq7g7tr45z7zexwvehpe4eoknromuijvwpbcsjksh3mmefdz4aot2nlyjoml23xlfnwt76koor3fu5bxwmxa";
	private static final String ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications";
	private static final String APP_ID = "73fea208-38f2-41b8-8367-2b7ae87cdf9c";

//
//	@PostMapping("/send")
//	public ResponseEntity<Map<String, Object>> sendNotification(@RequestParam String title,
//			@RequestParam String message, @RequestParam String userId) {
//
//		Map<String, Object> response = new HashMap<>();
//		try {
//			if (userId == null || userId.trim().isEmpty()) {
//				response.put("errors", List.of("User ID is missing or invalid."));
//				return ResponseEntity.badRequest().body(response);
//			}
//
//			RestTemplate restTemplate = new RestTemplate();
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Authorization", "Basic " + API_KEY);
//			headers.set("Content-Type", "application/json");
//
//			Map<String, Object> body = new HashMap<>();
//			body.put("app_id", APP_ID);
//			body.put("headings", Map.of("en", title));
//			body.put("contents", Map.of("en", message));
//			body.put("include_external_user_ids", List.of(userId));
//
//			HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//			restTemplate.exchange(ONESIGNAL_API_URL, HttpMethod.POST, request, String.class);
//
//			response.put("message", "Notification sent successfully.");
//			return ResponseEntity.ok(response);
//
//		} catch (Exception e) {
//			response.put("errors", List.of("Failed to send notification: " + e.getMessage()));
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		}
//	}

//	@PostMapping("/send")
//	public ResponseEntity<Map<String, Object>> sendNotification(@RequestParam String title,
//			@RequestParam String message, @RequestParam(required = false) String userId) {
//
//		Map<String, Object> response = new HashMap<>();
//		try {
//			if (userId == null || userId.trim().isEmpty()) {
//				response.put("errors", List.of("User ID is missing or invalid."));
//				return ResponseEntity.badRequest().body(response);
//			}
//
//			RestTemplate restTemplate = new RestTemplate();
//			HttpHeaders headers = new HttpHeaders();
//			headers.set("Authorization", "Basic " + API_KEY);
//			headers.set("Content-Type", "application/json");
//
//			Map<String, Object> body = new HashMap<>();
//			body.put("app_id", APP_ID);
//			body.put("headings", Map.of("en", title));
//			body.put("contents", Map.of("en", message));
//			body.put("include_external_user_ids", List.of(userId));
//
//			HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//			ResponseEntity<String> responseEntity = restTemplate.exchange(ONESIGNAL_API_URL, HttpMethod.POST, request,
//					String.class);
//
//			System.out.println("Response Body: " + responseEntity.getBody());
//			System.out.println("Status Code: " + responseEntity.getStatusCode());
//
//			response.put("message", "Notification sent successfully.");
//			return ResponseEntity.ok(response);
//
//		} catch (Exception e) {
//			response.put("errors", List.of("Failed to send notification: " + e.getMessage()));
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		}
//	}

	@PostMapping("/send")
	public ResponseEntity<Map<String, Object>> sendNotificationToAll(@RequestParam String title,
			@RequestParam String message) {

		Map<String, Object> response = new HashMap<>();
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Basic " + API_KEY);
			headers.set("Content-Type", "application/json");

			Map<String, Object> body = new HashMap<>();
			body.put("app_id", APP_ID);
			body.put("headings", Map.of("en", title));
			body.put("contents", Map.of("en", message));
			body.put("included_segments", List.of("All")); // Send to all users

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(ONESIGNAL_API_URL, HttpMethod.POST, request,
					String.class);

			System.out.println("Response Body: " + responseEntity.getBody());
			System.out.println("Status Code: " + responseEntity.getStatusCode());

			response.put("message", "Notification sent to all users successfully.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("errors", List.of("Failed to send notification: " + e.getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
