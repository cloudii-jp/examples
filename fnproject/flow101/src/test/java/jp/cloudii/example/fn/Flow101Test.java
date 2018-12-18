package jp.cloudii.example.fn;

import com.fnproject.fn.testing.FnTestingRule;
import com.fnproject.fn.testing.flow.FlowTesting;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class Flow101Test {
	@Rule
	public final FnTestingRule testing = FnTestingRule.createDefault();

	private final FlowTesting flowTesting = FlowTesting.create(testing);

	@Test
	public void shouldBeTwice() {
		testing.givenEvent().withBody("2").enqueue();

		testing.thenRun(Flow101.class, "handleRequest");

		Assert.assertEquals("Your number is 4", testing.getOnlyResult().getBodyAsString());
	}
}
