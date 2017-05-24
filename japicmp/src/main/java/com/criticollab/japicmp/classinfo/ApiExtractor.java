package com.criticollab.japicmp.classinfo;

import com.criticollab.japicmp.classinfo.api.ClassApiSignature;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSource;
import com.criticollab.japicmp.classinfo.api.ClassApiSignatureSourceProvider;

import javax.inject.Inject;

public class ApiExtractor {
	@Inject
	static ClassApiSignatureSourceProvider provider;

	private ApiExtractor() {

	}

	public static ClassApiSignatureSource newSignatureSource() {
		return provider.newInstance();
	}

	public static long getDefaultSerialUID(ClassApiSignature apiSignature) {
	 /* The serialVersionUID is computed using the signature of a stream of bytes that reflect the class definition. The National Institute of Standards and Technology (NIST) Secure Hash Algorithm (SHA-1) is used to compute a signature for the stream. The first two 32-bit quantities are used to form a 64-bit hash. A java.lang.DataOutputStream is used to convert primitive data types to a sequence of bytes. The values input to the stream are defined by the Java Virtual Machine (VM) specification for classes. Class modifiers may include the ACC_PUBLIC, ACC_FINAL, ACC_INTERFACE, and ACC_ABSTRACT flags; other flags are ignored and do not affect serialVersionUID computation. Similarly, for field modifiers, only the ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_VOLATILE, and ACC_TRANSIENT flags are used when computing serialVersionUID values. For constructor and method modifiers, only the ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED, ACC_STATIC, ACC_FINAL, ACC_SYNCHRONIZED, ACC_NATIVE, ACC_ABSTRACT and ACC_STRICT flags are used. Names and descriptors are written in the format used by the java.io.DataOutputStream.writeUTF method.

	  The sequence of items in the stream is as follows:


	  The class name.
	  The class modifiers written as a 32-bit integer.
		  The name of each interface sorted by name.
	  For each field of the class sorted by field name (except private static and private transient fields:
	  The name of the field.
		  The modifiers of the field written as a 32-bit integer.
		  The descriptor of the field.
	  If a class initializer exists, write out the following:
	  The name of the method, <clinit>.
	  The modifier of the method, java.lang.reflect.Modifier.STATIC, written as a 32-bit integer.
		  The descriptor of the method, ()V.
		  For each non-private constructor sorted by method name and signature:
	  The name of the method, <init>.
	  The modifiers of the method written as a 32-bit integer.
		  The descriptor of the method.
	  For each non-private method sorted by method name and signature:
	  The name of the method.
		  The modifiers of the method written as a 32-bit integer.
		  The descriptor of the method.
	  The SHA-1 algorithm is executed on the stream of bytes produced by DataOutputStream and produces five 32-bit values sha[0..4].
	  The hash value is assembled from the first and second 32-bit values of the SHA-1 message digest. If the result of the message digest, the five 32-bit words H0 H1 H2 H3 H4, is in an array of five int values named sha, the hash value would be computed as follows:
	  long hash = ((sha[0] >>> 24) & 0xFF) |
				  ((sha[0] >>> 16) & 0xFF) << 8 |
				  ((sha[0] >>> 8) & 0xFF) << 16 |
				  ((sha[0] >>> 0) & 0xFF) << 24 |
				  ((sha[1] >>> 24) & 0xFF) << 32 |
				  ((sha[1] >>> 16) & 0xFF) << 40 |
				  ((sha[1] >>> 8) & 0xFF) << 48 |
				  ((sha[1] >>> 0) & 0xFF) << 56;
				  */

	 throw new NoSuchMethodError();
	}

	public static ClassApiSignatureSource newSignatureSource(boolean b) {
		return newSignatureSource();
	}
}
