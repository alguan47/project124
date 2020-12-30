package Parts;

public class Trio {
	private Token identifier;
	private Token value;
	
	public Trio(Token identifier, Token value) {
		this.identifier=identifier;
		this.value=value;
	}
	
	public Token getValue() {
		return this.value;
	}
	
	public Token getIdentifier() {
		return this.identifier;
	}
}



//public static Token addition(Token a, Token b) {
//	if(a.getType().equals("Numbr") && b.getType().equals("Numbr")) {
//		int x = Integer.parseInt(a.getLexeme());
//		int y = Integer.parseInt(b.getLexeme());
//		int sum = x + y;
//		
//		String thisString = Integer.toString(sum);
//		System.out.println(thisString);
//		
//		Token bago = new Token(thisString, "Numbr");
//		
//		return bago;
//		
//		//printUpdatedTable();
//	}else {
//		//means it not int so either float or another arith
//		if(a.getType().equals("Arithmetic operator")) {
//			switch(a.getLexeme()) {
//				case "SUM OF":
//					return addition(a, b);
//				case "DIFF OF":
//					return subtraction(a,b);
//				case "PRODUKT OF":
//					return multiplication(a,b);
//				case "QUOSHUNT OF":
//					return division(a,b);
//				case "MOD OF":
//					
//			}
//				
//			
//			Token bago = new Token(null, null);
//			return bago;
//			
//			
//		}else {
//			//it not int, it not arith, must be float
//			float x = Float.parseFloat(a.getLexeme());
//			float y = Float.parseFloat(b.getLexeme());
//			float sum = x + y;
//			
//			String thatString = Float.toString(sum);
//			System.out.println(thatString);
//			
//			Token bago = new Token(thatString, "Numbar");
//			 return bago;
//			
//		}
//		
//		
//	}
//}
//
//public static Token subtraction(Token a, Token b) {
//	if(a.getType().equals("Numbr") && b.getType().equals("Numbr")) {
//		int x = Integer.parseInt(a.getLexeme());
//		int y = Integer.parseInt(b.getLexeme());
//		int diff = x - y;
//		
//		String thisString = Integer.toString(sum);
//		System.out.println(thisString);
//		
//		Token bago = new Token(thisString, "Numbr");
//		
//		return bago;
//		
//		//printUpdatedTable();
//	}else {
//		//means it not int so either float or another arith
//		if(a.getType().equals("Arithmetic operator") || b.getType().equals("Arithmetic operator")) {
//			
//			Token bago = new Token(null, null);
//			return bago;
//			
//			
//		}else {
//			//it not int, it not arith, must be float
//			float x = Float.parseFloat(a.getLexeme());
//			float y = Float.parseFloat(b.getLexeme());
//			float sum = x - y;
//			
//			String thatString = Float.toString(sum);
//			System.out.println(thatString);
//			
//			Token bago = new Token(thatString, "Numbar");
//			 return bago;
//			
//		}
//		
//		
//	}
//}
//
//public static Token multiplication(Token a, Token b) {
//	if(a.getType().equals("Numbr") && b.getType().equals("Numbr")) {
//		int x = Integer.parseInt(a.getLexeme());
//		int y = Integer.parseInt(b.getLexeme());
//		int sum = x * y;
//		
//		String thisString = Integer.toString(sum);
//		System.out.println(thisString);
//		
//		Token bago = new Token(thisString, "Numbr");
//		
//		return bago;
//		
//		//printUpdatedTable();
//	}else {
//		//means it not int so either float or another arith
//		if(a.getType().equals("Arithmetic operator") || b.getType().equals("Arithmetic operator")) {
//			
//			Token bago = new Token(null, null);
//			return bago;
//			
//			
//		}else {
//			//it not int, it not arith, must be float
//			float x = Float.parseFloat(a.getLexeme());
//			float y = Float.parseFloat(b.getLexeme());
//			float sum = x * y;
//			
//			String thatString = Float.toString(sum);
//			System.out.println(thatString);
//			
//			Token bago = new Token(thatString, "Numbar");
//			 return bago;
//			
//		}
//		
//		
//	}
//}
//public static Token division(Token a, Token b) {
//	if(a.getType().equals("Numbr") && b.getType().equals("Numbr")) {
//		int x = Integer.parseInt(a.getLexeme());
//		int y = Integer.parseInt(b.getLexeme());
//		int sum = x / y;
//		
//		String thisString = Integer.toString(sum);
//		System.out.println(thisString);
//		
//		Token bago = new Token(thisString, "Numbr");
//		
//		return bago;
//		
//		//printUpdatedTable();
//	}else {
//		//means it not int so either float or another arith
//		if(a.getType().equals("Arithmetic operator") || b.getType().equals("Arithmetic operator")) {
//			
//			Token bago = new Token(null, null);
//			return bago;
//			
//			
//		}else {
//			//it not int, it not arith, must be float
//			float x = Float.parseFloat(a.getLexeme());
//			float y = Float.parseFloat(b.getLexeme());
//			float sum = x / y;
//			
//			String thatString = Float.toString(sum);
//			System.out.println(thatString);
//			
//			Token bago = new Token(thatString, "Numbar");
//			 return bago;
//			
//		}
//		
//		
//	}
//}