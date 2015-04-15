package com.torres.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.torres.utils.JsonReader;

@Controller
@RequestMapping("/")
public class MainController {

	@RequestMapping(value = "atm/{city}", method = RequestMethod.GET)
	public String getATMsbyCity(@PathVariable String city, ModelMap model) {

		if (city == null) {
			model.addAttribute("city", "No city selected");
		} else {
			model.addAttribute("city", city);
			try {
				URL url = new URL("https://www.ing.nl/api/locator/atms/");
				// JSONObject json =
				// JsonReader.readJsonFromUrl("https://www.ing.nl/api/locator/atms/");

				// ING JSON is not well formed so I need to remove the first
				// line and parsed again
				Scanner s = new Scanner(url.openStream());
				s.next();
				StringBuilder sb = new StringBuilder();
				while (s.hasNextLine()) {
					sb.append(s.nextLine());
				}
				String jsonText = sb.toString();
				JSONArray listATMs = new JSONArray(jsonText);

				JSONArray listATMsByCity = new JSONArray();

				ArrayList listAddressesMap = new ArrayList();

				for (int i = 0; i < listATMs.length(); i++) {
					JSONObject atm = listATMs.getJSONObject(i);
					JSONObject atmAddress = atm.getJSONObject("address");
					if (atmAddress != null
							&& atmAddress.has("city")
							&& city.equalsIgnoreCase(atmAddress
									.getString("city"))) {
						listATMsByCity.put(atmAddress);
						listAddressesMap.add(JsonReader.jsonToMap(atmAddress));
					}
				}

				model.addAttribute("addresses", listAddressesMap);

			} catch (MalformedURLException e) {
				System.out.println("Bad URL");
			} catch (IOException e) {
				System.out.println("Open Stream Exception");
			} catch (JSONException e) {
				System.out.println(e);
			}
		}

		return "list";

	}

	@RequestMapping(value = {"/","atm", "atm/"}, method = RequestMethod.GET)
	public String getDefaultMovie(ModelMap model) {

		model.addAttribute("city", "No city selected");
		return "list";

	}

}