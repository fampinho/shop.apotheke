package coding.challenge.shop.apotheke;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {
 
	// FETCH REPOSITORIES FROM 01-01-2022 INTO JSON_FILE
	// **https://api.github.com/search/repositories?per_page=100&q=created:>2022-01-01&sort=order=asc
	final static private String JSON_FILE = "\\Coding_Challenge\\shop.apotheke\\src\\main\\resources\\gitHubAPI.json";
	private JSONParser parser = new JSONParser();
	private JSONObject jsonObject;
	private JSONArray items;

	public RepositoryController() {
		prepareJson();
	}

	/**
	 * Method findAll = Find all repositories from 01-01-2022 and sorted by stars
	 * http://localhost:8080/findAll
	 */

	@RequestMapping("/findAll")
	public @ResponseBody JSONArray findAll() {
		String request = null;
		BufferedReader br = null;
		JSONArray array = new JSONArray();
		try {
			URL u = new URL(
					"https://api.github.com/search/repositories?per_page=100&q=created:>2022-01-01&sort=stars&order=desc");
			HttpsURLConnection http = (HttpsURLConnection) u.openConnection();
			InputStreamReader inputStreamReader = new InputStreamReader(http.getInputStream());
			br = new BufferedReader(inputStreamReader);
			while ((request = br.readLine()) != null) {
				array.add(request);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Method findByDate = Given a date it returns the most popular repositories
	 * created from this date onwards.
	 * http://localhost:8080/findByDate?date=2022-07-05
	 */
	@RequestMapping("/findByDate")
	public @ResponseBody JSONArray findByDate(@RequestParam("date") String date) {
		JSONObject jsonItem = null;
		JSONArray response = new JSONArray();
		int count = 0;
		for (int i = 0; i < this.items.size(); i++) {
			jsonItem = (JSONObject) this.items.get(i);
			LocalDate filterDate = LocalDate.parse(date);
			LocalDate created_at = LocalDate.parse(jsonItem.get("created_at").toString().subSequence(0, 10));

			if (created_at.isAfter(filterDate)) {
				count++;
				response.add(jsonItem);
			}
		}
		return response;
	}

	/**
	 * Method findByLanguage = It returns all repositories according to the programming
	 * language filter.
	 * http://localhost:8080/findByLanguage?language=Go
	 */
	@RequestMapping("/findByLanguage")
	public @ResponseBody JSONArray findByLanguage(@RequestParam("language") String filterLanguage) {
		JSONObject jsonItem = null;
		JSONArray response = new JSONArray();
		int count = 0;
		for (int i = 0; i < this.items.size(); i++) {
			jsonItem = (JSONObject) this.items.get(i);
			String str = (String) jsonItem.get("language");
			if (str != null) {
				str.trim();
			}
			if (filterLanguage.equals(str)) {
				count++;
				response.add(jsonItem);
			}
		}
		return response;
	}

	/**
	 * Method findMostPopular = It returns the most popular repository.
	 * http://localhost:8080/findMostPopular
	 */
	@RequestMapping("/findMostPopular")
	public @ResponseBody JSONArray findMostPopular() {
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		for (int i = 0; i < this.items.size(); i++) {
			list.add((JSONObject) this.items.get(i));
		}
		Collections.sort(list, Collections.reverseOrder(new StarsComparator()));
		JSONArray response = new JSONArray();
		response.add(list.subList(0, 1));
		return response;
	}

	/**
	 * Method findMostPopular = It returns the 10 / 50 / 100  most popular repository.
	 * http://localhost:8080/findTopList?limit=10
	 * http://localhost:8080/findTopList?limit=50
	 * http://localhost:8080/findTopList?limit=100
	 */
	@RequestMapping("/findTopList")
	public @ResponseBody JSONArray findTopList(@RequestParam("limit") int filter) {
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		for (int i = 0; i < this.items.size(); i++) {
			list.add((JSONObject) this.items.get(i));
		}
		Collections.sort(list, Collections.reverseOrder(new StarsComparator()));
		JSONArray response = new JSONArray();
		if (filter > list.size()) {
			response.add(list);
			return response;
		}
		if (filter == 10) {
			response.add(list.subList(0, 10));
			return response;
		} else if (filter == 50) {
			response.add(list.subList(0, 50));
			return response;
		} else if (filter == 100) {
			response.add(list.subList(0, 100));
			return response;
		}
		return response;
	}

	//Preparing JsonObject
	private JSONObject prepareJson() {
		try {
			this.jsonObject = (JSONObject) this.parser.parse(new FileReader(JSON_FILE));
			this.items = (JSONArray) this.jsonObject.get("items");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this.jsonObject;
	}

}

class StarsComparator implements Comparator<JSONObject> {

	public int compare(JSONObject j1, JSONObject j2) {
		String s1 = j1.get("stargazers_count").toString();
		String s2 = j2.get("stargazers_count").toString();
		return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
	}
}
