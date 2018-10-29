package jp.cloudii.example.fn;

import com.fnproject.fn.testing.FnTestingRule;
import com.fnproject.fn.testing.flow.FlowTesting;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Flow101Test {
	@Rule
	public final FnTestingRule testing = FnTestingRule.createDefault();

	private final FlowTesting flowTesting = FlowTesting.create(testing);

	@Test
	public void test() {
		testing.givenEvent().withBody("2").enqueue();

		flowTesting.givenFn("./is_prime").withAction(action -> {
			assertEquals(new String(action), "4");
			return "true".getBytes();
		});

		testing.thenRun(Flow101.class, "handleRequest");

		assertEquals(
				testing.getOnlyResult().getBodyAsString(),
				"true");
	}
}
