package com.demai.cornel.util;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: IDLongEncryptor.java
 * Description:
 * @version 1.0
 */

public class IDLongEncryptor {
	private static Logger logger = LoggerFactory.getLogger(IDLongEncryptor.class);
	// TODO use object pool to reuse IDEncryptor
	// Take care! IDEncryptor is not thread safe
	// so to resue IDEncryptor, we have to set up
	// a pool, maybe we should consider apache common pool
	private static IDLongEncryptor instance;

	public static IDLongEncryptor getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new IDLongEncryptor(DEFAULT_KEY, DEFAULT_KEY_LONG);
		return instance;
	}

	private static BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
	private static BigInteger LARGE_VAL_LONG = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
	// ////////////////
	// Private Data //
	// ////////////////

	/**
	 * Non-zero default key, from www.random.org
	 */
	private final static int DEFAULT_KEY = 0x6CFB18E2;
	private final static long DEFAULT_KEY_LONG = 0x6CFB18E2A2C4E7L;

	private final static int LOW_16_MASK = 0xFFFF;
	private final static long LOW_32_MASK = 0xFFFFFFFFL;
	private final static int HALF_SHIFT = 16;
	private final static int HALF_SHIFT_LONG = 32;
	private final static int NUM_ROUNDS = 4;

	/**
	 * Permutation key
	 */
	private int mKey;
	private long mKeyLong;

	/**
	 * Round key schedule
	 */
	private int[] mRoundKeys = new int[NUM_ROUNDS];
	private long[] mRoundKeysLong = new long[NUM_ROUNDS];

	// ////////////////
	// Constructors //
	// ////////////////
	private IDLongEncryptor() {
		this(DEFAULT_KEY, DEFAULT_KEY_LONG);
	}

	private IDLongEncryptor(int key, long keyLong) {
		setKey(key, keyLong);
	}

	// //////////////////
	// Public Methods //
	// //////////////////

	/**
	 * Sets a new value for the key and key schedule.
	 */
	private void setKey(int newKey, long newKeyLong) {

		mKey = newKey;
		mKeyLong = newKeyLong;

		mRoundKeys[0] = mKey & LOW_16_MASK;
		mRoundKeys[1] = ~(mKey & LOW_16_MASK);
		mRoundKeys[2] = mKey >>> HALF_SHIFT;
		mRoundKeys[3] = ~(mKey >>> HALF_SHIFT);

		mRoundKeysLong[0] = mKeyLong & LOW_32_MASK;
		mRoundKeysLong[1] = ~(mKeyLong & LOW_32_MASK);
		mRoundKeysLong[2] = mKeyLong >>> HALF_SHIFT_LONG;
		mRoundKeysLong[3] = ~(mKeyLong >>> HALF_SHIFT_LONG);

	} // end setKey()

	/**
	 * Returns the current value of the key.
	 */
	private int getKey() {
		return mKey;
	}

	private long getKeyLong() {
		return mKeyLong;
	}

	/**
	 * Calculates the enciphered (i.e. permuted) value of the given integer
	 * under the current key.
	 * 
	 * @param plain
	 *            the integer to encipher.
	 * @return the enciphered (permuted) value.
	 */
	private static long LARGE_VAL = 1l << 32;

	private long encipher(int plain) {
		// 1 Split into two halves.
		int rhs = plain & LOW_16_MASK;
		int lhs = plain >>> HALF_SHIFT; // >>>表示无符号右移位

		// 2 Do NUM_ROUNDS simple Feistel rounds.
		for (int i = 0; i < NUM_ROUNDS; ++i) {
			if (i > 0) {
				// Swap lhs <-> rhs
				final int temp = lhs;
				lhs = rhs;
				rhs = temp;
			} // end if
				// Apply Feistel round function F().
			rhs ^= F(lhs, i);
		} // end for

		// 3 Recombine the two halves and return.
		long v = (lhs << HALF_SHIFT) + (rhs & LOW_16_MASK);
		// 把负数变成正数
		if (v < 0) {
			v += LARGE_VAL;
		}
		return v;
	} // end encipher()

	private BigInteger encipher(long plain) {
		// 1 Split into two halves.
		long rhs = plain & LOW_32_MASK;
		long lhs = plain >>> HALF_SHIFT_LONG; // >>>表示无符号右移位

		// 2 Do NUM_ROUNDS simple Feistel rounds.
		for (int i = 0; i < NUM_ROUNDS; ++i) {
			if (i > 0) {
				// Swap lhs <-> rhs
				final long temp = lhs;
				lhs = rhs;
				rhs = temp;
			} // end if
				// Apply Feistel round function F().
			rhs ^= F(lhs, i);
		} // end for

		// 3 Recombine the two halves and return.
		long temp = (lhs << HALF_SHIFT_LONG) + (rhs & LOW_32_MASK);

		BigInteger v = BigInteger.valueOf(temp);
		// 把负数变成正数
		if (temp < 0) {
			v = v.add(LARGE_VAL_LONG);
		}
		return v;
	} // end encipher()

	/**
	 * Calculates the deciphered (i.e. inverse permuted) value of the given
	 * integer under the current key.
	 * 
	 * @return the deciphered (inverse permuted) value.
	 */
	private int decipher(long cypherlong) {
		int cypher;
		if (cypherlong > Integer.MAX_VALUE) {
			cypher = (int) (cypherlong - LARGE_VAL);
		} else {
			cypher = (int) cypherlong;
		}
		// 1 Split into two halves.
		int rhs = cypher & LOW_16_MASK;
		int lhs = cypher >>> HALF_SHIFT;

		// 2 Do NUM_ROUNDS simple Feistel rounds.
		for (int i = 0; i < NUM_ROUNDS; ++i) {
			if (i > 0) {
				// Swap lhs <-> rhs
				final int temp = lhs;
				lhs = rhs;
				rhs = temp;
			} // end if
				// Apply Feistel round function F().
			rhs ^= F(lhs, NUM_ROUNDS - 1 - i);
		} // end for

		// 4 Recombine the two halves and return.
		return (lhs << HALF_SHIFT) + (rhs & LOW_16_MASK);
	} // end decipher()

	private long decipherLong(BigInteger cypherlong) {
		long cypher;
		if (cypherlong.compareTo(LONG_MAX) > 0) {
			cypher = cypherlong.subtract(LARGE_VAL_LONG).longValue();
		} else {
			cypher = cypherlong.longValue();
		}
		// 1 Split into two halves.
		long rhs = cypher & LOW_32_MASK;
		long lhs = cypher >>> HALF_SHIFT_LONG;

		// 2 Do NUM_ROUNDS simple Feistel rounds.
		for (int i = 0; i < NUM_ROUNDS; ++i) {
			if (i > 0) {
				// Swap lhs <-> rhs
				final long temp = lhs;
				lhs = rhs;
				rhs = temp;
			} // end if
				// Apply Feistel round function F().
			rhs ^= F(lhs, NUM_ROUNDS - 1 - i);
		} // end for

		// 4 Recombine the two halves and return.
		return (lhs << HALF_SHIFT_LONG) + (rhs & LOW_32_MASK);
	} // end decipher()
		// ///////////////////
		// Private Methods //
		// ///////////////////

	// The F function for the Feistel rounds.
	private int F(int num, int round) {
		// XOR with round key.
		num ^= mRoundKeys[round];
		// Square, then XOR the high and low parts.
		num *= num;
		return (num >>> HALF_SHIFT) ^ (num & LOW_16_MASK);
	} // end F()

	private long F(long num, int round) {
		// XOR with round key.
		num ^= mRoundKeysLong[round];
		// divided by 2, then XOR the high and low parts.
		num = num >>> 1;
		return (num >>> HALF_SHIFT_LONG) ^ (num & LOW_32_MASK);
	} // end F()

	private String encrypt(int id) throws Exception {
		long enc = this.encipher(id);
		return String.valueOf(enc);
	}

	private String encrypt(long id) throws Exception {
		//Geely
//		if (id <= Integer.MAX_VALUE) {
//			int intid = (int) id;
//			return encrypt(intid);
//		}
		return String.valueOf(this.encipher(id));
	}

	private String encryptWithoutException(int id) {
		String str = null;
		try {
			str = this.encrypt(id);
		} catch (Exception e) {
			logger.warn("errorMsg in encrypt. id:{}", id);
		}
		return str;
	}

	public String encryptWithoutException(long id) {
		String str = null;
		try {
			str = this.encrypt(id);
		} catch (Exception e) {
			logger.warn("errorMsg in encrypt. id:{}", id);
		}
		return str;
	}

    public int decryptIntWithoutException(String str) {
        int id = -1;
        try {
            id = this.decipher(Long.parseLong(str));
        } catch (Exception e) {
            logger.warn("errorMsg in decrypt. str:{}", str);
        }
        return id;
    }
    
	public long decryptWithoutException(String str) {
		long id = -1;
		try {
			BigInteger numBig = new BigInteger(str, 10);
			//Geely
//			if (numBig.compareTo(LONG_MAX) > 0) {
				id = this.decipherLong(numBig);
//			} else {
//				long num = Long.parseLong(str);
//				id = this.decipher(num);
//			}
		} catch (Exception e) {
			logger.warn("errorMsg in decrypt. str:{}", str);
			// e.printStackTrace();
		}
		return id;
	}

	public long decryptDefultWithoutException(String str) {
		long id = -1;
		try {
			if (str.length() < 18) {
				return Long.parseLong(str);
			}
			BigInteger numBig = new BigInteger(str, 10);
			id = this.decipherLong(numBig);
		} catch (Exception e) {
			logger.warn("errorMsg in decrypt. str:{}", str);
		}
		return id;
	}

	public static void main(String[] args) {
//		System.out.println(getInstance().decryptIntWithoutException("1169859722884586702"));
		System.out.println(getInstance().encryptWithoutException(75825));
	}

	/**
	 * 使用源有方式，代码太长，很不好看，因此这里提供了encode，用来代替
	 * IDEncryptor.getInstance().encryptWithoutException() 现在只需 import static
	 *  直接使用encode和decode即可
	 * <p/>
	 * {@link #encryptWithoutException(int)}
	 * 
	 * @param id
	 * @return 如果错误的id，那么将会返回null 加密id的便捷方法
	 * 
	 * @param id
	 *            {@link #encryptWithoutException(int)}
	 * @return 如果id==null那么返回null 如果产生异常那么返回null
	 */
	private static String encode(Integer id) {
		if (id == null)
			return null;
		return getInstance().encryptWithoutException(id);
	}

	private static String encodeLong(Long id) {
		if (id == null)
			return null;
		if (id <= Integer.MAX_VALUE) {
			return getInstance().encryptWithoutException(id.intValue());
		} else {
			return getInstance().encryptWithoutException(id);
		}
	}
}
