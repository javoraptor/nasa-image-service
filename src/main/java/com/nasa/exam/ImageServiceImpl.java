package com.nasa.exam;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasa.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ImageServiceImpl {

	public static List<String> fetchDateList() {

		return Utils.readFileIntoDateArray("src/MarsDates.txt");
	}

	public static void executeMultipleRestCalls(List<String> dateList) {
		dateList.forEach(e -> executeRestCall(e));
	}

	public static void executeRestCall(String date) {
		OkHttpClient client = new OkHttpClient();

		client.newCall(buildRequest(date)).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Response response) throws IOException {
				if (!response.isSuccessful()) {
					throw new IOException("Unexpected code " + response);
				} else {
					downloadToFile(response);
				}
			}
		});
	}

	private static void downloadToFile(Response response) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(response.body().string().toString());

			// check if any photos are present
			if (root.path("photos").size() > 0) {
				JsonNode photoNode = root.path("photos").get(0);
				String idSource = photoNode.path("id").asText();
				String name = photoNode.path("img_src").asText();

				Utils.downloadImageToFile(name, idSource);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static Request buildRequest(String date) {
		
		HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos")
				.newBuilder();
		urlBuilder.addQueryParameter("api_key", "DEMO_KEY");// DEMO_KEY
		urlBuilder.addQueryParameter("earth_date", date);

		return new Request.Builder().url(urlBuilder.build().toString()).build();
		
	}
}
