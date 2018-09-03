package jp.cloudii.example.fn;

public class IsPrime {
	public String handleRequest(int x) {
		if(x == 2)     return "true";
		if(x % 2 == 0) return "false";

		for(int i = 3; i <= Math.sqrt(x); i+=2) {
			if(x % i == 0) return "false";
		}

		return "true";
	}
}