package bibCreator;

public class Test {

	public static void main(String[] args) {
		String s = "title={A Generalized Approach to Implement Efficient CMOS-Based Threshold Logic Functions},";
		int start = s.indexOf('{');
		int end = s.indexOf('}');
		s  = s.substring(start + 1, end);
		
		System.out.println(s);

	}

}
