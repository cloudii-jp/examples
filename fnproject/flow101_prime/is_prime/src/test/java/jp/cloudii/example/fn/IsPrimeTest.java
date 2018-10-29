package jp.cloudii.example.fn;

import com.fnproject.fn.testing.FnTestingRule;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class IsPrimeTest {
	@Rule
	public FnTestingRule fn = FnTestingRule.createDefault();

	static class Fixture {
		final int value;
		final boolean expected;
		Fixture(int value, boolean expected) {
			this.value = value;
			this.expected = expected;
		}
	}
	@DataPoints
	public static Fixture[] fixtures = {
			new Fixture(1, false),
			new Fixture(2, true),
			new Fixture(3, true),
			new Fixture(4, false),
	};

	@Theory
	public void test(Fixture fixture) {
		fn.givenEvent().withBody(String.valueOf(fixture.value)).enqueue();

		fn.thenRun(IsPrime.class, "handleRequest");

		assertEquals(fn.getOnlyResult().getBodyAsString(), String.valueOf(fixture.expected));
	}
}
