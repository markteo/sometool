package api;

import httpUtil.HttpDownloadUtility;
import httpUtil.Upload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;

import main.Helper;
import ui.frame.UIFileUploadHTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class APICall {

	public static final String enc = "UTF-8";

	public String loginBucket(String targetURL, String username, String password) {
		String api = "login";
		targetURL = targetURL + api;
		String urlParameters;
		try {

			urlParameters = "user-name=" + URLEncoder.encode(username, enc) + "&password="
					+ URLEncoder.encode(password, enc);

			String response = executePost(targetURL, urlParameters);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;

	}

	public String getUserFeatures(String targetURL, String sessionKey) {
		String api = "getuserfeatures";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getInventoryList(String targetURL, String sessionKey) {

		String api = "getinventorylist";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String updateInventoryList(String targetURL, JSONObject inventoryDetails, String sessionKey) {

		String api = "updateinventory";
		targetURL = targetURL + api;
		String urlParameters;

		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc) + "&inventory-id="
					+ URLEncoder.encode(inventoryDetails.getString("inventoryID"), enc) + "&registration-name="
					+ URLEncoder.encode(inventoryDetails.getString("registrationName"), enc) + "&model-name="
					+ URLEncoder.encode(inventoryDetails.getString("modelName"), enc) + "&mac-address="
					+ URLEncoder.encode(inventoryDetails.getString("macAddress"), enc);
			String response = executePost(targetURL, urlParameters);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getBucketDevices(String targetURL, String sessionKey) {

		String api = "getbucketdevices";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getBuckets(String targetURL, String sessionKey) {
		String api = "getbuckets";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getNodeLicense(String targetURL, String sessionKey, int bucketID) {

		String api = "getnodelicenses";
		targetURL = targetURL + api;
		String urlParameters;
		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc) + "&bucket-id="
					+ URLEncoder.encode(Integer.toString(bucketID), enc);

			String response = executePost(targetURL, urlParameters);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String addNodeLicense(String targetURL, String sessionKey, int bucketID, String[] features, String duration,
			String storage, String maxVCA) {
		String api = "addnodelicense";
		targetURL = targetURL + api;

		String urlParameters;

		String feature = "[";

		for (int i = 0; i < features.length; i++) {
			if (i == 0) {
				feature = feature + "\"" + features[i] + "\"";
			} else {
				feature = feature + ",\"" + features[i] + "\"";
			}

		}
		feature = feature + "]";
		System.out.println(feature);
		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc) + "&bucket-id="
					+ URLEncoder.encode(Integer.toString(bucketID), enc) + "&duration-months="
					+ URLEncoder.encode(duration, enc) + "&cloud-storage-gb=" + URLEncoder.encode(storage, enc)
					+ "&max-vca-count=" + URLEncoder.encode(maxVCA, enc) + "&features="
					+ URLEncoder.encode(feature, enc);
			String response = executePost(targetURL, urlParameters);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public String updateNodeLicense(String targetURL, String sessionKey, String licenseNumber, String features) {
		String api = "updatenodelicense";
		targetURL = targetURL + api;

		String urlParameters;

		String durationMonths = Helper.readString("Enter duration (months) > ");
		String cloudStorage = Helper.readString("Enter cloud storage space (GB) > ");
		String maxVCA = Helper.readString("Enter max VCA count > ");

		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc) + "&license-number="
					+ URLEncoder.encode(licenseNumber, enc) + "&duration-months="
					+ URLEncoder.encode(durationMonths, enc) + "&cloud-storage-gb="
					+ URLEncoder.encode(cloudStorage, enc) + "&max-vca-count=" + URLEncoder.encode(maxVCA, enc)
					+ "&features=" + URLEncoder.encode(features, enc);
			String response = executePost(targetURL, urlParameters);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAssignableFeatures(String targetURL, String sessionKey, int bucketID) {
		String api = "getassignablenodefeatures";
		targetURL = targetURL + api;
		String urlParameters;

		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc) + "&bucket-id="
					+ URLEncoder.encode(Integer.toString(bucketID), enc);
			String response = executePost(targetURL, urlParameters);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public String getCSVSample(String targetURL, String sessionKey, String fileLocation)
			throws IOException {
		String api = "/public/files/samples/inventory_template.csv";
		targetURL = targetURL + api;

		System.out.println(targetURL);
		HttpDownloadUtility http = new HttpDownloadUtility();
		String response = http.downloadFile(targetURL, fileLocation, sessionKey);
		// http.addPropertyChangeListener(ui);
		http.execute();
		System.out.println(response);

		return response;
	}

	public String uploadInventory(String targetURL, String fileURL, String sessionKey) {
		String api = "uploadinventory";
		targetURL = targetURL + api;
		File file = new File(fileURL);
		String urlParameters;
		System.out.println(sessionKey);
		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc);
			Upload upload = new Upload();
			String response = upload.runUpload(targetURL, fileURL, urlParameters);
			System.out.println("Upload Completed");
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String generateAccessKey(String targetURL, String sessionKey, int userID, String ttl, String maxUse) {

		String api = "generateaccesskey";
		targetURL = targetURL + api;
		String urlParameters;

		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc) + "&user-id="
					+ URLEncoder.encode(Integer.toString(userID), enc) + "&ttl=" + URLEncoder.encode(ttl, enc)
					+ "&max-use-count=" + URLEncoder.encode(maxUse, enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getUserList(String targetURL, String sessionKey, int bucketID) {

		String api = "getbucketusersbybucketid";
		targetURL = targetURL + api;
		String urlParameters;

		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc) + "&bucketid="
					+ URLEncoder.encode(Integer.toString(bucketID), enc);
			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getAccessKeyList(String targetURL, String sessionKey) {
		String api = "getaccesskeylist";
		targetURL = targetURL + api;
		String urlParameters;

		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String logout(String targetURL, String sessionKey) {
		String api = "logout";
		targetURL = targetURL + api;
		String urlParameters;

		try {
			urlParameters = "session-key=" + URLEncoder.encode(sessionKey, enc);
			String response = executePost(targetURL, urlParameters);
			return response;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String executePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (SocketException se) {
			se.printStackTrace();
			System.out.println("Server is down.");
			return null;
		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
