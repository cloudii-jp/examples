package jp.cloudii.fn.word;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordCountMapper implements Serializable {

	public Message.MapperResponse handleRequest(Message.MapperRequest request) {
		try (InputStream is = new URL(request.url).openStream()) {
			try (Scanner scan = new Scanner(is)) {

				Map<String, Integer> count = new HashMap<>();

				scan.useDelimiter(",|¥n|\\.|!|\\?|\\s");

				while(scan.hasNext()) {
					String word = scan.next().trim();
					if (! word.isEmpty()) {
						String key = word.toLowerCase();

						Integer c = count.getOrDefault(key, 0);
						count.put(key, c + 1);
					}
				}

				return new Message.MapperResponse(count, null);
			}
		} catch (Throwable  t) {
			t.printStackTrace();
			return new Message.MapperResponse(Collections.emptyMap(), t.getMessage());
		}
	}
}
