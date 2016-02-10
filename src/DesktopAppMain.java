import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DesktopAppMain {
	private static String apiURL = "";
	private static final String bucket = "/api/superadmin/";
	private static String api = "";

	public static void main(String args[]) {

		String apiURL = Helper.readString("Enter api url > ");
		
		String targetURL = apiURL + bucket;
		
		//Login to bucket
		String response = loginBucket(targetURL);
		
	}
	
	public static String loginBucket(String targetURL){
		String api = "login";
		targetURL = targetURL + api;
		String urlParameters;
		try {

			String username = Helper.readString("Enter username > ");
			String password = Helper.readString("Enter password > ");
			urlParameters = "user-name="
					+ URLEncoder.encode(username, "UTF-8") + "&password="
					+ URLEncoder.encode(password, "UTF-8");
			urlParameters = "user-name=" + URLEncoder.encode("root", "UTF-8") + "&password="
					+ URLEncoder.encode("root", "UTF-8");

			String response = executePost(targetURL, urlParameters);
			System.out.println(response);
			return response;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;


	}

	public static String executePost(String targetURL, String urlParameters) {
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
