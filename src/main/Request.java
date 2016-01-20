package main;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.json.JSONArray;
import org.json.JSONObject;


public class Request {

//	public String makeHTTPRequestJokeNorris() {
//		JSONObject jsonObj = null;
//		String joke;
//		try {
//			URL url = new URL("http://api.icndb.com/jokes/random/");
//			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//			String strTemp = "";
//			while (null != (strTemp = br.readLine())) {
//				jsonObj = (JSONObject) new JSONParser().parse(strTemp);
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		jsonObj = (JSONObject) jsonObj.get("value");
//		joke = (String) jsonObj.get("joke");
//		return joke;
//	}
//
//	public String makeHTTPRequestJokeMomma() {
//		JSONObject jsonObj = null;
//		String joke;
//		try {
//			URL url = new URL("http://api.yomomma.info/");
//			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//			String strTemp = "";
//			while (null != (strTemp = br.readLine())) {
//				jsonObj = (JSONObject) new JSONParser().parse(strTemp);
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		joke = (String) jsonObj.get("joke");
//		return joke;
//	}
//
//	public String makeHTTPRequestYesOrNo() {
//		JSONObject jsonObj = null;
//		String yesOrNo;
//		try {
//			URL url = new URL("http://yesno.wtf/api/");
//			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//			String strTemp = "";
//			while (null != (strTemp = br.readLine())) {
//				jsonObj = (JSONObject) new JSONParser().parse(strTemp);
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		yesOrNo = (String) jsonObj.get("image");
//		return yesOrNo;
//	}
//
//	public String makeHTTPRequestMusic() {
//		JSONArray jsonArray = new JSONArray();
//		JSONObject jsonObj = null;
//		String yesOrNo, line = "",linkToSong = "";
//		try {
//			URL url = new URL("http://api.soundcloud.com/tracks?client_id=ffcaccc2a3bf0998c26d5a980a8b8607");
//			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//			while ((line = br.readLine()) != null) {
//				System.out.println(line);
//				String objects[] = line.split("\\},");
//				
//				for(int i=0;i<objects.length;i++){
//					
//					objects[i] = objects[i].replace("[", "");
//					objects[i] = objects[i] + "}";
//					System.out.println(objects[i] + "\n");
//					jsonObj = (JSONObject) new JSONParser().parse(objects[i]);
//					
//					jsonArray.add(jsonObj);
//				}
//			}
//			Random rn = new Random();
//			int randomNum = rn.nextInt((jsonArray.size() - 0) + 1) + 0;
//			
//			jsonObj = (JSONObject) jsonArray.get(randomNum);
//			linkToSong = (String) jsonObj.get("permalink_url");
//			
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return linkToSong;
//	}
	
	public void writeVideosToRDF(String keyword){

		String api_key = "AIzaSyCZO2nHBNMSGgRg4VHMZ9P8dWT0H23J-Fc";
		String yt_url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="
				+ keyword + "&type=video&videoCaption=closedCaption&key=" + api_key + "&format=5&maxResults=10&v=2";
		String line = "", stringArray;
		StringBuilder stringArrayBuilder = new StringBuilder();
		
		String titleOfVideo;
		String description;
		String thumbnailURL;
		String videoId;
		
		Model model = ModelFactory.createDefaultModel();
		
		try{
			URL url = new URL(yt_url);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((line = br.readLine()) != null) {
				stringArrayBuilder = stringArrayBuilder.append(line);
			}
			stringArray = stringArrayBuilder.toString();
			
			JSONObject nodeRoot = new JSONObject(stringArray);
			JSONArray jsonArray = (JSONArray)nodeRoot.get("items");
			
			
			for(int i=0; i<jsonArray.length();i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				
				JSONObject snippet = (JSONObject) obj.get("snippet");
				
				description = (String) snippet.get("description");
				titleOfVideo = (String) snippet.get("title");
								
				
				JSONObject thumbnails = (JSONObject)snippet.get("thumbnails");
				JSONObject thumbnail = (JSONObject)thumbnails.get("high");
				thumbnailURL = (String) thumbnail.get("url");
				
				JSONObject id = (JSONObject) obj.get("id");
				videoId = (String) id.get("videoId");
				
				
				Resource video = model.createResource("video"+i);
				Property p1 = model.createProperty("title");
				video.addProperty(p1, titleOfVideo);
				Property p2 = model.createProperty("description");
				video.addProperty(p2, description);
				Property p3 = model.createProperty("thumbnail");
				video.addProperty(p3, thumbnailURL);
				Property p4 = model.createProperty("id");
				video.addProperty(p4, videoId);
								
			}
			FileOutputStream fos = new FileOutputStream(keyword+".nt");
			
			RDFDataMgr.write(fos, model, Lang.NTRIPLES);

			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	public void readVideosFromRDF(String keyword){
		Model model = ModelFactory.createDefaultModel();
		model.read(keyword+".nt", "NTRIPLES");
		
		RDFDataMgr.write(System.out, model, Lang.NTRIPLES);
		
	}
}
	