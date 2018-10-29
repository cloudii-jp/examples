package jp.cloudii.fn.word;

import com.fnproject.fn.testing.FnTestingRule;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class WordCountMapperTest {
	@Rule
	public FnTestingRule fn = FnTestingRule.createDefault();

	@Test
	public void count() throws JSONException {
		String url = getClass().getResource("sample.txt").toString();

		fn.givenEvent()
				.withBody("{\"url\": \"" + url + "\"}")
				.enqueue();

		fn.thenRun(WordCountMapper.class, "handleRequest");

		JSONAssert.assertEquals(
				"{\"count\":{\"hello\":1,\"thank\":1,\"you\":1},\"errorMessage\":null}",
				fn.getOnlyResult().getBodyAsString(),
				false);
	}
}
