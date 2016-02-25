package api;

import java.util.ArrayList;
import java.util.HashMap;

import main.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIProcess {

	private APICall api = new APICall();
	
	public JSONArray bucketList (String targetURL, String sessionKey){
		
		JSONArray bucketList = new JSONArray();
		String response = api.getBuckets(targetURL, sessionKey);
		
		try {
			JSONObject jsonResponse = new JSONObject(response);
			JSONArray jsonArray = jsonResponse.getJSONArray("buckets");
			
			for(int i = 0; i < jsonArray.length(); i ++){
				JSONObject bucket = new JSONObject();
				JSONObject bucketJSON = jsonArray.getJSONObject(i);
				
				String bucketName = bucketJSON.getString("name");
				
				JSONArray bucketArray = bucketJSON.getJSONArray("users");
				
				for(int x = 0; x < 1; x++){
					JSONObject userJSON = bucketArray.getJSONObject(x);
					int bucketID = userJSON.getInt("bucketId");
					bucket.put("bucketID", bucketID);
				}
				
				bucket.put("bucketName", bucketName);
				bucketList.put(bucket);
			}
			
			return bucketList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		//return null;
	}
	
	public JSONArray nodeLicenseList(String targetURL, String sessionKey, int bucketID){
		
		JSONArray nodeLicenseList = new JSONArray();
		
		String response = api.getNodeLicense(targetURL, sessionKey, bucketID);
		
		try {
			JSONObject licenseResponse = new JSONObject(response);
			
			JSONArray licenseList = licenseResponse.getJSONArray("node-licenses");
			
			for(int x = 0; x < licenseList.length(); x ++){
				
				JSONObject licenseJSON = licenseList.getJSONObject(x);
				
				String licenseStatus = licenseJSON.getString("status");
				
				if(licenseStatus.equals("UNUSED")){
					JSONObject license = new JSONObject();
					license.put("licenseNumber", licenseJSON.getString("licenseNumber"));
					nodeLicenseList.put(license);
				}
			}
			
			return nodeLicenseList;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public JSONArray inventoryList(String targetURL, String sessionKey){
		JSONArray inventoryList = new JSONArray();
		
		String response = api.getInventoryList(targetURL, sessionKey);
		
		try {
			
			JSONObject inventoryResponse = new JSONObject(response);
			
			JSONArray inventory = inventoryResponse.getJSONArray("inventory-list");
			
			for(int x = 0; x < inventory.length(); x ++){
				
				JSONObject item = inventory.getJSONObject(x);
				
				boolean itemStatus = item.getBoolean("activated");
				
				if(itemStatus == false){
					JSONObject itemAdd = new JSONObject();
					System.out.println("Registration Number: " + item.getString("registrationNumber"));
					itemAdd.put("id", item.get("inventoryId"));
					itemAdd.put("registrationNumber", item.get("registrationNumber"));
					itemAdd.put("macAddress", item.get("macAddress"));
					
					inventoryList.put(itemAdd);
				}
			}
			
			return inventoryList;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void featuresList(String targetURL, String sessionKey, int bucketID){
		Data.featureList = new HashMap<String, JSONArray>();
		
		String response = api.getAssignableFeatures(targetURL, sessionKey, bucketID);
		
		try{
			JSONObject featureResponse = new JSONObject(response);
			JSONArray features = featureResponse.getJSONArray("features");
			ArrayList<String> types = new ArrayList<String>();
			for(int x = 0; x < features.length(); x ++){
				JSONObject feature = features.getJSONObject(x);
				String type = feature.getString("type");
				if(Data.featureList.containsKey(type)){
					JSONArray featureArray = Data.featureList.get(type);
					featureArray.put(feature);
				}else{
					JSONArray featureArray = new JSONArray();
					featureArray.put(feature);
					Data.featureList.put(type, featureArray);
				}
			}
			System.out.println(types.size());
			
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		
	}
	
}
