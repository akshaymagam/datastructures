package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import bigint.BigInteger;

class BigIntegerTest {

	@Test
	void testAdd() {
		BigInteger yes = new BigInteger();
		BigInteger no = new BigInteger();
		for(int i = -1000; i <=1000; i++) {
			for(int j = -1000; j<=1000;j++) {
				yes = BigInteger.parse("" + i);
				no = BigInteger.parse(""+j);
				BigInteger result = BigInteger.add(yes, no);
				assertEquals(""+(i+j),result.toString());
				System.out.println(result + " " + i + " " + j);
			}
		}
	}
	
//	void testMultiply() {
//		BigInteger yes = new BigInteger();
//		BigInteger no = new BigInteger();
//		for(int i = -1000; i <= 1000; i++) {
//			for(int j = -1000; j <= 1000; j++) {
//				yes = BigInteger.parse("" + i);
//				no = BigInteger.parse(""+j);
//				BigInteger result = BigInteger.multiply(yes, no);
//				assertEquals(""+(i*j),result.toString());
//				System.out.println(result + " " + i + " " + j);
//			}
//		}
//	}

}
