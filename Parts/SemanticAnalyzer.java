package Parts;

import java.util.ArrayList;
import java.util.Scanner;

public class SemanticAnalyzer {	
	private ArrayList<Token> copyTable;
	private ArrayList<Trio> updatedTable;
	
	public SemanticAnalyzer(){
		this.copyTable = new ArrayList<Token>();
		this.updatedTable = new ArrayList<Trio>();
	}
	
	public void setCopyTable(Token token) {
		copyTable.add(token);
		//System.out.println(token.getLexeme() + " and " + token.getType() + " added.");
	}
	
	public void printUpdatedTable() {
		System.out.println("===== TABLE OF VALUES =====");
		
		for(int i = 0; i < updatedTable.size(); i++) {
			if(updatedTable.get(i).getValue()== null) {
				System.out.println(updatedTable.get(i).getIdentifier().getLexeme());
				
			}else {
				System.out.println(updatedTable.get(i).getIdentifier().getLexeme() + " " +updatedTable.get(i).getValue().getLexeme() );
			}
			
		}
		
		System.out.println("===== =========== =====");
	}
	
	public void deleteElement(String cmp) {
		//System.out.println("inside fxn");
		for(int i = 0; i < updatedTable.size(); i++) {
			//System.out.println(updatedTable.get(i).getIdentifier().getLexeme());
			
			if(cmp.equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				updatedTable.remove(i);
				//System.out.println("found");
				return;
			}
		}
	}
	
	public String inputGetter() {
		//System.out.print("input: ");
		Scanner sc = new Scanner(System.in);
		String dummy = sc.nextLine();
		
		return dummy;
		
	}
	
	public Token addition(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				//found val of token a		
				
				for(int j = 0; j < updatedTable.size(); j++) {
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
						if(updatedTable.get(i).getValue().getType().equals("Numbr") && updatedTable.get(j).getValue().getType().equals("Numbr")) {
							//both numbr, parseint
							int x = Integer.parseInt(updatedTable.get(i).getValue().getLexeme());
							int y = Integer.parseInt(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Integer.toString(x+y);
							Token t = new Token(sumOf, "Numbr");
							return t;
						}else {
							//one is numbar, parse float
							float x = Float.parseFloat(updatedTable.get(i).getValue().getLexeme());
							float y = Float.parseFloat(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Float.toString(x/y);
							Token t = new Token(sumOf, "Numbar");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}
	
	public Token subtraction(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				//found val of token a		
				
				for(int j = 0; j < updatedTable.size(); j++) {
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
						if(updatedTable.get(i).getValue().getType().equals("Numbr") && updatedTable.get(j).getValue().getType().equals("Numbr")) {
							//both numbr, parseint
							int x = Integer.parseInt(updatedTable.get(i).getValue().getLexeme());
							int y = Integer.parseInt(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Integer.toString(x-y);
							Token t = new Token(sumOf, "Numbr");
							return t;
						}else {
							//one is numbar, parse float
							float x = Float.parseFloat(updatedTable.get(i).getValue().getLexeme());
							float y = Float.parseFloat(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Float.toString(x/y);
							Token t = new Token(sumOf, "Numbar");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}
	
	public Token multiplication(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				//found val of token a		
				
				for(int j = 0; j < updatedTable.size(); j++) {
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
						if(updatedTable.get(i).getValue().getType().equals("Numbr") && updatedTable.get(j).getValue().getType().equals("Numbr")) {
							//both numbr, parseint
							int x = Integer.parseInt(updatedTable.get(i).getValue().getLexeme());
							int y = Integer.parseInt(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Integer.toString(x*y);
							Token t = new Token(sumOf, "Numbr");
							return t;
						}else {
							//one is numbar, parse float
							float x = Float.parseFloat(updatedTable.get(i).getValue().getLexeme());
							float y = Float.parseFloat(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Float.toString(x/y);
							Token t = new Token(sumOf, "Numbar");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}
	
	public Token division(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				//found val of token a		
				for(int j = 0; j < updatedTable.size(); j++) {
			
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
						
						
						if(updatedTable.get(i).getValue().getType().equals("Numbr") && updatedTable.get(j).getValue().getType().equals("Numbr")) {
							//both numbr, parseint
							int x = Integer.parseInt(updatedTable.get(i).getValue().getLexeme());
							int y = Integer.parseInt(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Integer.toString(x/y);
							Token t = new Token(sumOf, "Numbr");
							return t;
						}else {
							//one is numbar, parse float
							float x = Float.parseFloat(updatedTable.get(i).getValue().getLexeme());
							float y = Float.parseFloat(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Float.toString(x/y);
							Token t = new Token(sumOf, "Numbar");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}
	
	public Token modulo(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				
				//found val of token a		
				
				for(int j = 0; j < updatedTable.size(); j++) {
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
					
						if(updatedTable.get(i).getValue().getType().equals("Numbr") && updatedTable.get(j).getValue().getType().equals("Numbr")) {
							//both numbr, parseint
							int x = Integer.parseInt(updatedTable.get(i).getValue().getLexeme());
							int y = Integer.parseInt(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Integer.toString(x%y);
							Token t = new Token(sumOf, "Numbr");
							return t;
						}else {
							//one is numbar, parse float
							float x = Float.parseFloat(updatedTable.get(i).getValue().getLexeme());
							float y = Float.parseFloat(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Float.toString(x/y);
							Token t = new Token(sumOf, "Numbar");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}
	
	public Token maximum(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				
				//found val of token a		
				
				for(int j = 0; j < updatedTable.size(); j++) {
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
					
						if(updatedTable.get(i).getValue().getType().equals("Numbr") && updatedTable.get(j).getValue().getType().equals("Numbr")) {
							//both numbr, parseint
							int x = Integer.parseInt(updatedTable.get(i).getValue().getLexeme());
							int y = Integer.parseInt(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Integer.toString(Math.max(x, y));
							Token t = new Token(sumOf, "Numbr");
							return t;
						}else {
							//one is numbar, parse float
							float x = Float.parseFloat(updatedTable.get(i).getValue().getLexeme());
							float y = Float.parseFloat(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Float.toString(Math.max(x, y));
							Token t = new Token(sumOf, "Numbar");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}
	
	public Token minimum(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				
				//found val of token a		
				
				for(int j = 0; j < updatedTable.size(); j++) {
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
					
						if(updatedTable.get(i).getValue().getType().equals("Numbr") && updatedTable.get(j).getValue().getType().equals("Numbr")) {
							//both numbr, parseint
							int x = Integer.parseInt(updatedTable.get(i).getValue().getLexeme());
							int y = Integer.parseInt(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Integer.toString(Math.min(x, y));
							Token t = new Token(sumOf, "Numbr");
							return t;
						}else {
							//one is numbar, parse float
							float x = Float.parseFloat(updatedTable.get(i).getValue().getLexeme());
							float y = Float.parseFloat(updatedTable.get(j).getValue().getLexeme());
							
							String sumOf = Float.toString(Math.min(x, y));
							Token t = new Token(sumOf, "Numbar");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}
	
	public Token equalEqual(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				
				//found val of token a		
				
				for(int j = 0; j < updatedTable.size(); j++) {
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
						if(Integer.parseInt(updatedTable.get(i).getValue().getLexeme()) == Integer.parseInt(updatedTable.get(j).getValue().getLexeme())){
							Token t = new Token("true", "boolean");
							return t;
						}else {
							Token t = new Token("false", "boolean");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}

	public Token notEqual(Token a, Token b) {
		for(int i = 0; i < updatedTable.size(); i++ ) {
			
			if(a.getLexeme().equals(updatedTable.get(i).getIdentifier().getLexeme())) {
				
				//found val of token a		
				
				for(int j = 0; j < updatedTable.size(); j++) {
					if(b.getLexeme().equals(updatedTable.get(j).getIdentifier().getLexeme())) {
						//found val of token b
						// do addition
						if(Integer.parseInt(updatedTable.get(i).getValue().getLexeme()) != Integer.parseInt(updatedTable.get(j).getValue().getLexeme())){
							Token t = new Token("true", "boolean");
							return t;
						}else {
							Token t = new Token("false", "boolean");
							return t;
						}
						
					}
				}
				//token b value not existing cannot perform arith
				System.out.println("cannot perform arithmethic, second value isnt an integer");				
				return null;
			}
		}
		
		System.out.println("cannot perform arithmethic, first value isnt an integer");
		return null;
	}
	
	public Token arithmetic(int index) {
		if(copyTable.get(index).getType().equals("Variable identifier")) {
			Token t = null;
			
			if(copyTable.get(index+2).getType().equals("Variable identifier")) {
				
				switch(copyTable.get(index-1).getLexeme()) {
					case "SUM OF":
						t = (addition(copyTable.get(index),copyTable.get(index+2)));
						break;
					case "DIFF OF":
						t = (subtraction(copyTable.get(index),copyTable.get(index+2)));
						break;
					case "PRODUKT OF":
						t = (multiplication(copyTable.get(index),copyTable.get(index+2)));
						break;
					case "QUOSHUNT OF":
						t = (division(copyTable.get(index),copyTable.get(index+2)));
						break;
					case "MOD OF":
						t = (modulo(copyTable.get(index),copyTable.get(index+2)));
						break;
					case "BIGGR OF":
						t = (maximum(copyTable.get(index),copyTable.get(index+2)));
						break;
					case "SMALLR OF":
						t = (minimum(copyTable.get(index),copyTable.get(index+2)));
						break;
					case "BOTH SAEM":
						t = (equalEqual(copyTable.get(index),copyTable.get(index+2)));
						break;
					case "DIFFRINT":
						t = (notEqual(copyTable.get(index),copyTable.get(index+2)));
						
						
				}
							
				
				return t;
				//printUpdatedTable();
				
			}else {
				arithmetic(index+2);
			}
			
		}else {
			//it arith
			arithmetic(index+1);
					
			}
		return null;
		}
	
	public void doTheDew() {
		//lexeme = identifier
		//type = value
		
		String IT = new String("IT");
		copyTable.add(new Token(IT, "Special"));
		
		for(int a = 0; a < copyTable.size(); a++) {
			if(IT.equals(copyTable.get(a).getLexeme())) {
				updatedTable.add(new Trio(copyTable.get(a), null));
				break;
			}
		}
			
		for( int i = 0; i < copyTable.size(); i++) {
			//printUpdatedTable();
			
//			for(int z = 0; z < updatedTable.size(); z++) {
//				if(updatedTable.get(z).getValue() != null) {
//					if(updatedTable.get(z).getValue().getType() != "Numbr" || updatedTable.get(z).getValue().getType() != "Numbar") {
//						// must change value to IT
//						int index = z;
//						Token bagoId = updatedTable.get(z).getIdentifier();
//						
//						
//						for(int y = 0; y < updatedTable.size(); y++) {
//							if(updatedTable.get(y).getIdentifier().getLexeme().equals("IT")) {
//								
//							}
//						}
//					}
//				}
//			}
			
			
			String compare = new String(copyTable.get(i).getType());
			//System.out.println(compare);
			
			switch (compare) {
				case "Print operator":
					// must put checker if null 
					
					System.out.println();
					boolean checker = true;
					int count = i+1;
					while(checker) {				
						if(copyTable.get(count).getLexeme()!="\n") {
							//do print
							if(copyTable.get(count).getType().equals("Yarn")) {
								System.out.print(copyTable.get(count).getLexeme());
							}else {
								String cmp = copyTable.get(count).getType();
								
								if(cmp.equals("Arithmetic operator") || cmp.equals("Comparison operator")){
									
									for(int x = 0; x < updatedTable.size(); x++) {
										if(updatedTable.get(x).getIdentifier().getLexeme().equals("IT")) {
											System.out.print(updatedTable.get(x).getValue().getLexeme());
											break;
										}
									}
					
								}else {
									String cpy = copyTable.get(count).getLexeme();
									for(int x = 0; x < updatedTable.size(); x++) {
										if(updatedTable.get(x).getIdentifier().getLexeme().equals(cpy)) {
											System.out.print(updatedTable.get(x).getValue().getLexeme());
											break;
										}
								
									}					
								}
							}
						
						//increment
						count= count +1;
						
						}else {
							//stop
							checker = false;
						}
					}
					
					break;
					
					
				case "Input operator":				
					//call input getter fxn
					String dummy = new String();
					dummy = inputGetter();
					//System.out.print(dummy);
					
					
					System.out.println("this is dummy: " + dummy);
					//need to categorize the dummy, then make it a token
					try {
						
						int dummyInt = Integer.parseInt(dummy);
						//shit is int
						Token bago = new Token(dummy, "Numbr");
						copyTable.add(bago);
						
						//need to look for the lexeme in the updated table and delete it
						String compare3 = new String(copyTable.get(i+1).getLexeme());
						//System.out.println("looking for: " + compare3);
						deleteElement(compare3);
						
						updatedTable.add(new Trio(copyTable.get(i+1), bago));
						
						
					}catch(NumberFormatException e){
						try {
							
							System.out.println("shit is not int");
							
							Token bago = new Token(dummy, "Numbar");
							Float dummyFloat = Float.parseFloat(dummy);	
							
							//shit is float
							copyTable.add(bago);
							
							//need to look for the lexeme in the updated table and delete it
							String compare3 = new String(copyTable.get(i+1).getLexeme());
							//System.out.println("looking for: " + compare3);
							deleteElement(compare3);
							
							updatedTable.add(new Trio(copyTable.get(i+1), bago));
							
							
						}catch(NumberFormatException e1) {
							System.out.println("shit is not float");
							
							Token bago = new Token(dummy, "Yarn");
							copyTable.add(bago);
							
							//need to look for the lexeme in the updated table and delete it
							String compare3 = new String(copyTable.get(i+1).getLexeme());
							//System.out.println("looking for: " + compare3);
							deleteElement(compare3);
							
							updatedTable.add(new Trio(copyTable.get(i+1), bago));
							
						}
						
					}
					
					
					printUpdatedTable();
																		
					break;
				
				case "Arithmetic operator":
					//System.out.println("found arithmetic operator");

					int index = i+1;
					Token t = arithmetic(index);
					
					if(t!=null) {
						//update value of IT
						Token bagoIt = new Token("IT", "Variable");
						Trio ito = new Trio(bagoIt, t);
						
						for(int i1 = 0; i1 < updatedTable.size(); i1++){
							if(updatedTable.get(i1).getIdentifier().getLexeme().equals("IT")) {
								updatedTable.set(i1, ito);
							}
						}
					}
					
					printUpdatedTable();
					
					break;
					
				case "Assignment operator":
					System.out.println("found assignment operator");
					
					//IF ARITH NEED TO GET THE VALUE OF IT
					
					//need to look for the lexeme in the updated table and delete it
					String compare1 = new String(copyTable.get(i-1).getLexeme());
					//System.out.println("looking for: " + compare1);
					deleteElement(compare1);
					
					updatedTable.add(new Trio(copyTable.get(i-1), copyTable.get(i+1)));
					//printUpdatedTable();
					
					
					
					switch(copyTable.get(i+1).getType()) {
						case "Arithmetic operator":
							Token t1 = arithmetic(i+2);
							
							if(t1!=null) {
								
								Token bago = new Token(copyTable.get(i-1).getLexeme(), copyTable.get(i-1).getType());
								Trio ito = new Trio(bago, t1);
								
								boolean foundit = false;
								for(int i1 = 0; i1 < updatedTable.size(); i1++){						
									if(updatedTable.get(i1).getIdentifier().getLexeme().equals(copyTable.get(i-1).getLexeme())) {
										updatedTable.set(i1, ito);
										foundit= true;
									}
								}
								
								if(!foundit) {
									updatedTable.add(ito);
								}
							}
							break;
						case "Variable identifier":
							
							Token bago = new Token(copyTable.get(i-1).getLexeme(), copyTable.get(i-1).getType());
							
							boolean foundit = false;
							for(int i1 = 0; i1 < updatedTable.size(); i1++){
								if(updatedTable.get(i1).getIdentifier().getLexeme().equals(copyTable.get(i-1).getLexeme())) {
									Trio ito = new Trio(bago, updatedTable.get(i1).getValue());
									updatedTable.set(i1, ito);
									foundit = true;
								}
							}
							
							if(!foundit) {
								updatedTable.add(new Trio(bago, copyTable.get(i+1)));
							}
							
							break;
//						case "Troof":
//							Token bago1 = new Token(copyTable.get(i-1).getLexeme(), copyTable.get(i-1).getType());
//							
//							boolean foundit1 = false;
//							if(copyTable.get(i+1).getLexeme()== "WIN") {
//								for(int w = 0; w < updatedTable.size(); w++) {
//									if(updatedTable.get(w).getIdentifier().getLexeme().equals(copyTable.get(i-1).getLexeme())) {
//										Trio ito = new Trio (bago1, updatedTable.get(w).getValue());
//										updatedTable.set(w, ito);
//										foundit1 = true;
//									}
//								}
//								
//								if(!foundit) {
//									updatedTable.add(new Trio(bago1, ))
//								}
//								
//								
//							}
							
						default:
							
							Token bago1 = new Token(copyTable.get(i-1).getLexeme(), copyTable.get(i-1).getType());
							Trio ito = new Trio(bago1, copyTable.get(i+1));
							for(int i1 = 0; i1 < updatedTable.size(); i1++){
								if(updatedTable.get(i1).getIdentifier().getLexeme().equals(copyTable.get(i-1).getLexeme())) {
									updatedTable.set(i1, ito);
								}
							}
							
							
					}
					
					break;
				
				case "Declaration operator":
					//System.out.println("found declaration operator");
					updatedTable.add(new Trio(copyTable.get(i+1), null));
					
					break;
					
				case "Assignment during declaration operator":
					// troof 
					System.out.println("found Assignment during declaration operator");
					
					//need to look for the lexeme in the updated table and delete it
					String compare2 = new String(copyTable.get(i-1).getLexeme());				
					deleteElement(compare2);
	
					updatedTable.add(new Trio(copyTable.get(i-1), copyTable.get(i+1)));
					//printUpdatedTable();
					
					break;
				
				case "Comparison operator":
					
					int index1 = i+1;
					Token t1 = arithmetic(index1);
					
					if(t1!=null) {
						//update value of IT
						Token bagoIt = new Token("IT", "Variable");
						Trio ito = new Trio(bagoIt, t1);
						
						for(int i1 = 0; i1 < updatedTable.size(); i1++){
							if(updatedTable.get(i1).getIdentifier().getLexeme().equals("IT")) {
								updatedTable.set(i1, ito);
							}
						}
					}
			}
		}
	}
}
