package jp.cloudii.fn.word;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnproject.fn.api.FnFeature;
import com.fnproject.fn.api.flow.Flow;
import com.fnproject.fn.api.flow.FlowFuture;
import com.fnproject.fn.api.flow.Flows;
import com.fnproject.fn.runtime.flow.FlowFeature;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@FnFeature(FlowFeature.class)
public class WordCount {
	private ObjectMapper mapper = new ObjectMapper();

	public String handleRequest(Message.CounterRequest request) {
		try {
			FlowFuture<Map<String, Integer>> futureCount = counter(request);

			if (request.words != null) {
				futureCount = futureCount.thenApply(count ->
						Arrays.stream(request.words)
								.map(k -> new AbstractMap.SimpleEntry<>(k, count.getOrDefault(k, 0)))
								.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			}

			return mapper.writeValueAsString(futureCount.get());
		} catch (Throwable t) {
			return mkErrorString(t);
		}
	}

	private FlowFuture<Map<String, Integer>> counter(Message.CounterRequest request)  {
		Flow flow = Flows.currentFlow();

		List<FlowFuture<Message.MapperResponse>> futures = new ArrayList<>();

		for (String url : request.urls) {
			Message.MapperRequest mapperRequest = new Message.MapperRequest(url);

			FlowFuture<Message.MapperResponse> future =
					flow.invokeFunction("./mapper", mapperRequest, Message.MapperResponse.class);
			futures.add(future);
		}

		FlowFuture<List<Message.MapperResponse>> futureResults = flow.allOf(futures.toArray(new FlowFuture[0]))
				.thenApply(VOID -> futures.stream()
						.map(FlowFuture::get)
						.collect(Collectors.toList()));

		return futureResults.thenApply(results -> {
			Map<String, Integer> count = new HashMap<>();
			for (Message.MapperResponse r : results) {
				merge(count, r.count);
			}

			return count;
		});
    }

    private static void merge(Map<String, Integer> m1, Map<String, Integer> m2) {
		m2.forEach((key, count)-> {
			Integer oldC = m1.getOrDefault(key, 0);
			m1.put(key, oldC + count);
		});
	}

	static String mkErrorString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}
}
