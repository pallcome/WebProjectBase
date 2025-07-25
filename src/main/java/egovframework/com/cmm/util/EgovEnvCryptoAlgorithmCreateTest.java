package egovframework.com.cmm.util;

import org.egovframe.rte.fdl.cryptography.EgovPasswordEncoder;

public class EgovEnvCryptoAlgorithmCreateTest {
	//계정암호화키 키
	public String algorithmKey = "s*ol&uti%s";
 
	//계정암호화 알고리즘(MD5, SHA-1, SHA-256)
	public String algorithm = "SHA-256";
 
	//계정암호화키 블럭사이즈
	public int algorithmBlockSize = 1024;
 /*
	public static void main(String[] args) {
		EgovEnvCryptoAlgorithmCreateTest cryptoTest = new EgovEnvCryptoAlgorithmCreateTest();
 
		EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
		egovPasswordEncoder.setAlgorithm(cryptoTest.algorithm);
 
		System.out.println("------------------------------------------------------");
		System.out.println("알고리즘(algorithm) : "+cryptoTest.algorithm);
		System.out.println("알고리즘 키(algorithmKey) : "+cryptoTest.algorithmKey);
		System.out.println("알고리즘 키 Hash(algorithmKeyHash) : "+egovPasswordEncoder.encryptPassword(cryptoTest.algorithmKey));
		System.out.println("알고리즘 블럭사이즈(algorithmBlockSize)  :"+cryptoTest.algorithmBlockSize);
	}
*/
}