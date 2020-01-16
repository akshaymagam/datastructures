package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		integer = integer.trim();
		
		BigInteger ans = new BigInteger();	
		Character input = integer.charAt(0);
		int length; 
		ans.negative = integer.charAt(0) == '-';
		if(integer.charAt(0) == '+' || integer.charAt(0) == '-') {
				integer = integer.substring(1);
				length = integer.length();
		}
		
		length = integer.length();
		if(Character.getNumericValue(input) == 0 && integer.length() != 1) {
			for(int i = 0; i < length; i++) {
				input = integer.charAt(0);
				if(Character.getNumericValue(input) == 0 && integer.length() != 1) {
					integer = integer.substring(1);
					i--;
				} else {
					break;
				}
			}
		}
		if(integer.equals("0")) {
			return new BigInteger();
		}
		length = integer.length();
		for(int i = 0; i<integer.length(); i++){
			if(Character.isDigit(integer.charAt(i)) == false) {
				throw new IllegalArgumentException();
			}
		}
		
		ans.numDigits = length;
		
		DigitNode current = new DigitNode(Character.getNumericValue(integer.charAt(0)), null);
		integer = integer.substring(1);
		DigitNode end = new DigitNode(0, null);
		
		for(int i = 0; i< integer.length(); i++) {
			end = current;
			current = new DigitNode(Character.getNumericValue(integer.charAt(i)), end);
			
		}
		ans.front = removeZeros(current);
		return ans;
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		BigInteger ans = new BigInteger();
		
		DigitNode sum = new DigitNode(0,null);
		DigitNode temp = new DigitNode(0, null);
		int length;
		
		//add both of the same sign
		if (first.front != null && second.front == null) {
			return first;
		} else if (first.front == null && second.front != null) {
			return second;
		}
		if(first.negative == second.negative) {
			if(first.numDigits >= second.numDigits) {
				length = first.numDigits;
			} else {
				length = second.numDigits;
			}
			for (int i = 0; i < length ; i++) {
				temp = sum;
				sum = new DigitNode(0, temp);
			}
			int carry;
			int xy;
			DigitNode x = first.front;
			DigitNode y = second.front;
			DigitNode temp2 = sum;
			for (int i = 0; i < length; i++) {
				carry = 0;
				xy = (x.digit + y.digit);
				temp2.digit += xy;
				carry = temp2.digit/10;
				temp2.digit = temp2.digit%10;
				temp2.next.digit = carry;
				if(y.next == null && x.next != null) {
					x = x.next;
					temp2 = temp2.next;
					for(int j = i+1; j<length; j++) {
						carry = 0;
						temp2.digit = temp2.digit + x.digit;
						carry = temp2.digit/10;
						temp2.digit = temp2.digit%10;
						
						temp2.next.digit = carry;
						x = x.next;
						DigitNode z = temp2;
						temp2 = temp2.next;
						if (j == length-1 && temp2.digit == 0) {
							z.next = null;
						}
					}
					break;
				} else if (x.next ==null && y.next != null) {
					y=y.next;
					temp2 = temp2.next;
					for(int j = i+1; j<length; j++) {
						carry = 0;
						temp2.digit += y.digit;
						carry = temp2.digit/10;
						temp2.digit = temp2.digit%10;
						temp2.next.digit = carry;
						y = y.next;
						DigitNode z = temp2;
						temp2 = temp2.next;
						if (j == length-1 && temp2.digit == 0) {
							z.next = null;
						}
					}
					break;
				} else {
					x = x.next;
					y = y.next;
					DigitNode z = temp2;
					temp2 = temp2.next;
					if(temp2.next == null && temp2.digit ==0) {
						z.next=null;
					}
				}
			}
			ans.front = sum;
			ans.negative = first.negative;
			ans.numDigits = length;
			DigitNode z = temp2;
			if(temp2.next == null && temp2.digit ==0) {
				z.next=null;
			}
			return ans;
		} else {
			//subtracting
			if (isEqual(first,second) == true) {
				ans.front = new DigitNode(0,null);
				ans.numDigits = 1;
				ans.negative = false;
				return ans;
			} else if (isGreaterThan(first,second) == true) {
				//first minus second
				int carry = 0;
				DigitNode x = first.front;
				DigitNode y = second.front;
				DigitNode temp2 = new DigitNode(0,null);
				DigitNode tempHead = temp2;
				for(int i = 0; i < first.numDigits; i++) {
					if(y!=null) {
						int xy = x.digit-y.digit;
						if(temp2.digit + xy < 0) {
							carry = -1;
							xy = xy + 10;
							temp2.digit += xy;
							if(i<first.numDigits-1) {
								temp2.next = new DigitNode(carry, null);
							}
							temp2 = temp2.next;
						} else {
							carry = 0;
							temp2.digit += xy;
							if(i<first.numDigits-1) {
								temp2.next = new DigitNode(0, null);
							}
							temp2 = temp2.next;
						}
						x = x.next;
						y = y.next;
					} else {
						int xy = x.digit;
						if(temp2.digit + xy < 0) {
							carry = -1;
							xy = xy + 10;
							temp2.digit += xy;
							if(i<first.numDigits-1) {
								temp2.next = new DigitNode(carry, null);
							}
							temp2 = temp2.next;
						} else {
							carry = 0;
							temp2.digit += xy;
							if(i<first.numDigits-1) {
								temp2.next = new DigitNode(0, null);
							}
							temp2 = temp2.next;
						}
						x = x.next;
					}
				}
				ans.front = removeZeros(tempHead);
				int digitCounter = 0;
				for(DigitNode temp3 = tempHead; temp3 != null; digitCounter++) {
					temp3 = temp3.next;
				}
				ans.numDigits = digitCounter;
				ans.negative = first.negative;
				return ans;
			} else {
				//second minus first
				int carry = 0;
				DigitNode x = second.front;
				DigitNode y = first.front;
				DigitNode temp2 = new DigitNode(0,null);
				DigitNode tempHead = temp2;
				for(int i = 0; i < second.numDigits; i++) {
					if(y!=null) {
						int xy = x.digit-y.digit;
						if(temp2.digit + xy < 0) {
							carry = -1;
							xy = xy + 10;
							temp2.digit += xy;
							if(i<second.numDigits-1) {
								temp2.next = new DigitNode(carry, null);
							}
							temp2 = temp2.next;
						} else {
							carry = 0;
							temp2.digit += xy;
							if(i<second.numDigits-1) {
								temp2.next = new DigitNode(0, null);
							}
							temp2 = temp2.next;
						}
						x = x.next;
						y = y.next;
					} else {
						int xy = x.digit;
						if(temp2.digit + xy < 0) {
							carry = -1;
							xy = xy + 10;
							temp2.digit += xy;
							if(i<second.numDigits-1) {
								temp2.next = new DigitNode(carry, null);
							}
							temp2 = temp2.next;
						} else {
							carry = 0;
							temp2.digit += xy;
							if(i<second.numDigits-1) {
								temp2.next = new DigitNode(0, null);
							}
							temp2 = temp2.next;
						}
						x = x.next;
					}
				}
				ans.front = removeZeros(tempHead);
				int digitCounter = 0;
				for(DigitNode temp3 = tempHead; temp3 != null; digitCounter++) {
					temp3 = temp3.next;
				}
				ans.numDigits = digitCounter;
				ans.negative = second.negative;
				return ans;
			}
		}
	}
	
	private static DigitNode removeZeros(DigitNode input) {
		DigitNode temp = input;
		DigitNode prev = new DigitNode(0,null);
		if(input.next == null && input.digit == 0) {
			return input;
		}
		while(temp != null) {
			if(temp.digit == 0 && temp.next == null) {
				prev.next = null;
				removeZeros(input);
			}
			prev = temp;
			temp = temp.next;
		}
		return input;
	}
	
	private static boolean isEqual(BigInteger first, BigInteger second) {
		if(first.numDigits != second.numDigits) {
			return false;
		} else {
			DigitNode temp1 = first.front;
			DigitNode temp2 = second.front;
			for(int i = 0; i< first.numDigits; i++) {
				if(temp1.digit != temp2.digit) {
					return false;
				}
				temp1 = temp1.next;
				temp2 = temp2.next;
			}
				
		}
		return true;
	}
	
	private static boolean isGreaterThan(BigInteger first, BigInteger second) {
		if(first.numDigits > second.numDigits) {
			return true;
		} else if (first.numDigits < second.numDigits) {
			return false;
		} else {
			DigitNode temp1 = reverse(first.front);
			DigitNode temp2 = reverse(second.front);
			for(int i = 0; i < first.numDigits; i++) {
				if(temp1.digit > temp2.digit) {
					return true;
				} else if (temp1.digit < temp2.digit) {
					return false;
				} else {
				temp1 = temp1.next;
				temp2 = temp2.next;
				}
			}
		}
		return false;
	}
	
	private static DigitNode reverse(DigitNode node) 
    { 
		DigitNode head = new DigitNode(0,null);
		DigitNode temp = head;
		DigitNode temp2 = node;
		while(temp2 != null) {
			temp.digit = temp2.digit;
			temp2 = temp2.next;
			temp.next = new DigitNode(0,null);
			temp = temp.next;
		}
        DigitNode prev = null; 
        DigitNode current = head; 
        DigitNode next = null; 
        while (current != null) { 
            next = current.next; 
            current.next = prev; 
            prev = current; 
            current = next; 
        } 
        head = prev; 
        return head.next; 
    } 
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		BigInteger sumxy = new BigInteger();
		if(first.toString().equals("0") || second.toString().equals("0")) {
			return sumxy;
		}
		if(first.negative != second.negative) {
			sumxy.negative = true;
		} else {
			sumxy.negative = false;
		}
		BigInteger sumhead = new BigInteger();
		DigitNode x = first.front;
		DigitNode y = second.front;
		DigitNode xy = new DigitNode(0,null);
		sumxy.front = xy;
		int carry = 0;
		sumxy.numDigits = first.numDigits + second.numDigits;
		sumhead.numDigits = second.numDigits + 1;
		for(int i = 0; i < second.numDigits; i++) {
			DigitNode head = new DigitNode(0,null);
			DigitNode temp = head;
			for(int k = 0; k<i;k++) {
				temp.next = new DigitNode(0,null);
				temp = temp.next;
			}
			int z = 0;
			while(z<first.numDigits) {
				temp.digit += x.digit * y.digit;
				if(temp.digit >=10) {
					carry = temp.digit/10;
				} else {
					carry = 0;
				}
				temp.digit = temp.digit%10;
				temp.next = new DigitNode(carry,null);
				temp = temp.next;
				x = x.next;
				z++;
			}
			x = first.front;
			y = y.next;
			sumhead.negative = sumxy.negative;
			sumhead.front = removeZeros(head);
			DigitNode count = sumxy.front;
			int sumxyNumDigits = 0;
			while(count!=null) {
				sumxyNumDigits++;
				count = count.next;
			}
			sumxy.numDigits = sumxyNumDigits;
			DigitNode count2 = sumhead.front;
			int sumheadNumDigits = 0;
			while(count2!=null) {
				sumheadNumDigits++;
				count2 = count2.next;
			}
			sumhead.numDigits = sumheadNumDigits;
			
			sumxy = add(sumxy, sumhead);
		}

		return sumxy;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
}
