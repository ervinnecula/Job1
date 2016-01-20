package main;

public class Main {

	public static void main(String[] args) {
		Request r = new Request();
		
		r.writeVideosToRDF("italian");
		
		r.readVideosFromRDF("italian");

	}

}
