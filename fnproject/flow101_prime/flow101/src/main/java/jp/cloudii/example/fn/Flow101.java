package jp.cloudii.example.fn;

import com.fnproject.fn.api.FnFeature;
import com.fnproject.fn.api.flow.Flow;
import com.fnproject.fn.api.flow.FlowFuture;
import com.fnproject.fn.api.flow.Flows;
import com.fnproject.fn.api.flow.HttpResponse;
import com.fnproject.fn.runtime.flow.FlowFeature;

@FnFeature(FlowFeature.class)
public class Flow101 {
	public String handleRequest(int x) {
		Flow flow = Flows.currentFlow();

		 FlowFuture<byte[]> result = flow.completedValue(x)
				.thenApply( i -> i * 2 )
				.thenCompose( s -> flow.invokeFunction("./is_prime", s).thenApply(HttpResponse::getBodyAsBytes));

		return new String(result.get());
	}
}