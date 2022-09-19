package coding.challenge.shop.apotheke;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RepositoryTest {

	@Test
	void testFindByDate() {
		JSONArray val = (new RepositoryController().findByDate("2022-08-05"));
		FileReader fr;
		try {
			fr = new FileReader("\\Coding_Challenge\\shop.apotheke\\src\\test\\java\\findByDate.json");
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(fr);
			Assertions.assertEquals(val, jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testFindByLanguage() {
		JSONArray val = (new RepositoryController().findByLanguage("Go"));
		FileReader fr;
		try {
			fr = new FileReader("\\Coding_Challenge\\shop.apotheke\\src\\test\\java\\findByLanguage.json");
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(fr);
			Assertions.assertEquals(val, jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
