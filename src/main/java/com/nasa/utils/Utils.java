package com.nasa.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class Utils {

	public static void makeRestCall(String date) {

		OkHttpClient client = new OkHttpClient();

		HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos")
				.newBuilder();
		urlBuilder.addQueryParameter("api_key", "DEMO_KEY");// DEMO_KEY
		urlBuilder.addQueryParameter("earth_date", date);
		String url = urlBuilder.build().toString();

		Request request = new Request.Builder().url(url).build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Response response) throws IOException {
				if (!response.isSuccessful()) {
					throw new IOException("Unexpected code " + response);
				} else {
					String JSONResponse = response.body().string().toString();
					System.out.println(JSONResponse);
					ObjectMapper mapper = new ObjectMapper();
					try {
						JsonNode root = mapper.readTree(JSONResponse);

						// check if any photos are present
						if (root.path("photos").size() > 0) {
							JsonNode photoNode = root.path("photos").get(0);
							String idSource = photoNode.path("id").asText();
							String name = photoNode.path("img_src").asText();
							downloadImageToFile(name, idSource);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});

	}

	public static void downloadImageToFile(String uri, String idSource) {

		OkHttpClient client = new OkHttpClient();
		HttpUrl.Builder urlBuilder = HttpUrl.parse(uri).newBuilder();
		String url = urlBuilder.build().toString();
		Request request = new Request.Builder().url(url).build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Request request, IOException e) {
				e.printStackTrace();

			}

			@Override
			public void onResponse(Response response) throws IOException {
				InputStream inputStream = response.body().byteStream();

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int nRead;
				byte[] data = new byte[1024];
				while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
					buffer.write(data, 0, nRead);
				}

				buffer.flush();
				byte[] byteArray = buffer.toByteArray();
				writeBytesToFileClassic(byteArray, idSource + ".jpg");
			}
		});
	}

	private static void writeBytesToFileClassic(byte[] bFile, String fileDest) {

		FileOutputStream fileOuputStream = null;

		try {
			fileOuputStream = new FileOutputStream(".\\images\\" + fileDest);
			fileOuputStream.write(bFile);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileOuputStream != null) {
				try {
					fileOuputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static List<String> readFileIntoDateArray(String fileName) {
		List<String> dateList = new ArrayList<>(5);

		try (Scanner scan = new Scanner(new File(fileName))) {
			while (scan.hasNext()) {

				dateList.add(formatDate(scan.next()));
			}
		} catch (Exception e) {
			System.out.printf("Caught Exception: %s%n", e.getMessage());
			e.printStackTrace();
		}
		return dateList;
	}

	private static String formatDate(String next) {

		return convertToYearMonthDay(next);
	}

	private static String convertToYearMonthDay(String temp) {
		SimpleDateFormat before = new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateInString = temp;
		Date date = null;

		try {
			date = before.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatter.format(date);
	}

}
