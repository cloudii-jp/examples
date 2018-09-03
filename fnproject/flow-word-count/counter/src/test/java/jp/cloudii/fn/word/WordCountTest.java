package jp.cloudii.fn.word;

import com.fnproject.fn.testing.FnFunctionStubBuilder;
import com.fnproject.fn.testing.FnTestingRule;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class WordCountTest {
	@Rule
	public FnTestingRule fn = FnTestingRule.createDefault();

	@Test
	public void count() throws JSONException {
		String url = getClass().getResource("dummy.txt").toString();

		fn.givenEvent()
				.withMethod("POST")
				.withBody("{\"urls\": [\"" + url + "\",\"" + url + "\"]}")
				.enqueue();


		fn.givenFn("./mapper").withAction(dummyAction(url));

		fn.thenRun(WordCount.class, "handleRequest");

		JSONAssert.assertEquals(
				"{\"hello\":2, \"thank\":2, \"you\":2}",
				fn.getOnlyResult().getBodyAsString(),
				false);
	}

	@Test
	public void filtered() throws JSONException {
		String url = getClass().getResource("dummy.txt").toString();

		fn.givenEvent()
				.withMethod("POST")
				.withBody("{\"urls\": [\"" + url + "\",\"" + url + "\"], \"words\": [\"thank\"]}")
				.enqueue();


		fn.givenFn("./mapper").withAction(dummyAction(url));

		fn.thenRun(WordCount.class, "handleRequest");

		JSONAssert.assertEquals(
				"{\"thank\":2}",
				fn.getOnlyResult().getBodyAsString(),
				false);
	}

	private FnFunctionStubBuilder.ExternalFunctionAction dummyAction(String url) {
		return action -> {
			try {
				JSONAssert.assertEquals("{\"url\":\"" + url+ "\"}", new String(action), false);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			return "{\"count\":{\"hello\":1,\"thank\":1,\"you\":1},\"errorMessage\":null}".getBytes();
		};
	}
}
