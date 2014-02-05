/* 
 * Project Name	:   TN_COMMON
 * File Name	:	BlockCipher.java
 * Date			:	2006. 5. 22. - 오후 9:44:48
 * History		:	2006. 5. 22.
 * Version		:	1.0
 * Author		:   임주섭	
 * Comment      :    
 */
 
package biz.trustnet.common.cipher;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;


public abstract class BlockCipher  {
	public static final int ENCRYPT_MODE = 0x01;
	public static final int DECRYPT_MODE = 0x02;

	// Cipher State constants
	protected final static int STATE_UNITIALIZED = 0x00;
	protected final static int STATE_ENCRYPT = 0x01;
	protected final static int STATE_DECRYPT = 0x02;
	
	// Operation mode constants
	protected final static int MODE_ECB = 0x00;
	protected final static int MODE_CBC = 0x01;
	protected final static int MODE_CFB = 0x02;
	protected final static int MODE_OFB = 0x04;
	
	// PaddingType constants
	protected final static int NO_PADDING = 0x00;
	protected final static int PKCS_PADDING = 0x01;
	protected final static int ZEROS_PADDING = 0x02;
	
	
	// 아직 초기화되지 않았기 때문에 Cipher state는 STATE_UNITIALIZED
	// (Cipher.DECRYPT_MODE - 복호화 모드,Cipher.ENCRYPT_MODE - 암호화 모드)
	private int state = STATE_UNITIALIZED;
	
	// Default operation mode : ECB
	private int mode = MODE_ECB;
	
	// Default padding mode : PKCS5Padding(RSA의 PKCS standard)
	private Padding pad = new PKCS5Padding();
	private int paddingType = PKCS_PADDING;
	
	// Cipher의 암호화/복호화 크기
	protected int blockSize;
	
	// Cipher에서 사용할 Initial Vector
	protected byte[] IV = null;
	
	private byte[] key = null;
	
	// Cipher 입력 버퍼
	protected byte[] inputBuffer;
	protected int inputBufferOffset;
	protected int inputBufferLen;
	
	// Cipher 운영 모드에서 사용될 중간 버퍼
	protected byte chainBlock[];
	
	private SecureRandom rand;
	
	// Operation Mode(운영모드)가 ECB 모드이면 IV는 필요없다.
	// Default Operation Mode는 ECB 모드이다.
	private boolean needIV = false;

	// Constructor
	protected BlockCipher() {
		this(16);
	}

	protected BlockCipher(int blockSize) {
		this.blockSize = blockSize;

		//initialize key....
		key = new byte[16];
		key[ 0] = Byte.parseByte("24",16);
		key[ 1] = Byte.parseByte("3f",16);
		key[ 2] = Byte.parseByte("12",16);
		key[ 3] = Byte.parseByte("72",16);
		key[ 4] = Byte.parseByte("59",16);
		key[ 5] = Byte.parseByte("28",16);
		key[ 6] = Byte.parseByte("1d",16);
		key[ 7] = Byte.parseByte("2b",16);
		key[ 8] = Byte.parseByte("51",16);
		key[ 9] = Byte.parseByte("74",16);
		key[10] = Byte.parseByte("3f",16);
		key[11] = Byte.parseByte("1a",16);
		key[12] = Byte.parseByte("3e",16);
		key[13] = Byte.parseByte("62",16);
		key[14] = Byte.parseByte("57",16);
		key[15] = Byte.parseByte("3e",16);
	}

	// cipher는 clone(복제)되면 안되기 때문에 clone하게 되면 exception을 발생
	public final Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();	
	}

	// Cipher에서 해당 알고리즘의 인스턴스를 가져올 때 사용할 padding 클래스가 함께 선택됨
	protected void engineSetPadding(String padding) throws NoSuchPaddingException  {
		if(padding.equals("PKCS5Padding")) {
			this.pad = new PKCS5Padding();
			this.paddingType = PKCS_PADDING;
			
		} else if(padding.equals("NoPadding")) {
			this.pad = new Padding();
			this.paddingType = NO_PADDING;
			
		} else if(padding.equals("Zeroes")) {
			this.pad = new Zeroes();
			this.paddingType = ZEROS_PADDING;
			
		} else {
			throw new NoSuchPaddingException(padding + " Not supported");
		}
	}

	// Cipher에서 해당 알고리즘의 인스턴스를 가져올 때 사용할 Operation Mode가 함께 선택됨
	protected void engineSetMode(String mode) throws NoSuchAlgorithmException  {
		if(mode.equals("ECB")) {
			this.mode = MODE_ECB;
			this.needIV = false;
			
		} else if(mode.equals("CBC")) {
			this.mode = MODE_CBC;
			this.needIV = true;
			
		} else if(mode.equals("CFB")) {
			this.mode = MODE_CFB;
			this.needIV = true;
		} else {
			throw new NoSuchAlgorithmException(mode + " Not supported");
		}
	}

	// Cipher의 암호화/복호화하는 블럭의 크기를 return하는 method
	protected int engineGetBlockSize() {
		return blockSize;
	}

	// Cipher의 암호화/복호화할 때의 예상 결과값의 크기를 return하는 method
	protected int engineGetOutputSize(int inputLen) {
		int outSize = inputLen + inputBufferOffset;
		if(this.state == ENCRYPT_MODE) {
			outSize += blockSize - (outSize % blockSize);
		}

		return outSize;
	}

	// Block Cipher의 운영모드 중에서 IV(Initial Vector)를 사용하는 운영모드가 있다.
	// IV를 사용하지 않는 운영모드일 경우에는 IV 값이 null이다.
	protected byte[] engineGetIV() {
		return IV;
	}

	// Cipher를 초기화하는 method로 initialize가 되어야 암호화/복호화를 할 수 있음
	// 해당 알고리즘마다 암호화/복호화 방식이 다르므로 초기화하는 루틴도 다르기
	// 때문에 해당 알고리즘(DES,DESede,SEED 등등)에서 구현해야 한다.
	protected abstract void initialize(byte[] key, AlgorithmParameterSpec params)
		throws IllegalStateException, InvalidKeyException;

	// plainText(clearText)를 암호화하는 method
	// 해당 알고리즘마다 암호화/복호화 방식이 다르므로 암호화하는 루틴도 다르기
	// 때문에 해당 알고리즘(DES,DESede,SEED 등등)에서 구현해야 한다.
	protected abstract void encryptBlock(byte in[], byte out[]); 

	// cipherText를 복호화하는 method
	// 해당 알고리즘마다 암호화/복호화 방식이 다르므로 복호화하는 루틴도 다르기
	// 때문에 해당 알고리즘(DES,DESede,SEED 등등)에서 구현해야 한다.
	protected abstract void decryptBlock(byte in[], byte out[]);

	// Cipher를 초기화하는 method
	// 암호화/복호화 모드, 랜덤 값, IV 등을 초기화하고 세부적으로 초기화하는 initialize method를 호출
	protected void engineInit(int opmode, byte[] key, SecureRandom random)  throws InvalidKeyException {
		this.state = opmode;
		this.rand = random;

		if(this.needIV) {
			//this.IV = new byte[blockSize];
			this.IV = new byte[16];
			IV[ 0] = Byte.parseByte("30",16);
			IV[ 1] = Byte.parseByte("31",16);
			IV[ 2] = Byte.parseByte("32",16);
			IV[ 3] = Byte.parseByte("33",16);
			IV[ 4] = Byte.parseByte("34",16);
			IV[ 5] = Byte.parseByte("35",16);
			IV[ 6] = Byte.parseByte("36",16);
			IV[ 7] = Byte.parseByte("37",16);
			IV[ 8] = Byte.parseByte("38",16);
			IV[ 9] = Byte.parseByte("39",16);
			IV[10] = Byte.parseByte("30",16);
			IV[11] = Byte.parseByte("31",16);
			IV[12] = Byte.parseByte("32",16);
			IV[13] = Byte.parseByte("33",16);
			IV[14] = Byte.parseByte("34",16);
			IV[15] = Byte.parseByte("35",16);
	
			this.chainBlock = new byte[blockSize];
			//this.rand.nextBytes(IV);
		}

		initialize(key, null);
		reset();
	}

	// Cipher가 기본적인 초기화가 끝나고 실질적으로 동작을 기술하는 method로 engineUpdate는 여러가지 고려사항이 있다.
	// +------ block size
	// +------ operation mode(ECB,CBC,CFBn,OFBn)
	//	+------ Initial Vector(chainBlock)
	// 실제적으로 암호화/복호화할 데이터/메시지 등을 블럭 크기까지 채워질 때까지 또는 engineDoFinal이 호출되기 전까
	// 지 임시 버퍼에 저장해 놓는 것이 주역할이다. 각 Cipher의 블럭 사이즈가 넘을 경우에는 암호화/복호화를 수행하고 
	// 그 결과를 보관하며 engineDoFinal 함수가 호출되기 전까지 나머지 블럭도 보관된다. 이때 중요한 것은 운영 모드
	// (operation mode:ECB,CBC,CFB,OFB)에 따라서 부수적으로 사용되는 중간 바이트 값(chainBlock)들도 계산되어 저장된다.
	protected int engineUpdate(byte input[], int inputOffset, int inputLen, byte output[], int outputOffset) throws ShortBufferException {
		int bufferFillRequired;
		int remaining;
		int consumed = 0;
		int retval = 0;
		byte tempBlock[] = new byte[blockSize];

		if(state == ENCRYPT_MODE) {
			while(true) {
				bufferFillRequired = blockSize - inputBufferOffset;
				remaining = inputLen - consumed;

				if(bufferFillRequired <= remaining) {
					System.arraycopy(input, consumed + inputOffset, inputBuffer,inputBufferOffset, bufferFillRequired);

					if(mode == MODE_ECB) {
						encryptBlock(inputBuffer, chainBlock);
						System.arraycopy(chainBlock, 0, output, retval + outputOffset, blockSize);
					
					} else if(mode == MODE_CBC) {
						for(int i=0; i< blockSize; i++) 
							inputBuffer[i] ^= chainBlock[i];
						encryptBlock(inputBuffer, chainBlock);
						System.arraycopy(chainBlock, 0, output, retval + outputOffset, blockSize);
					
					} else if(mode == MODE_CFB) {
						encryptBlock(chainBlock, tempBlock);
						for(int i=0; i< blockSize; i++) {
							chainBlock[i] = (byte)(inputBuffer[i] ^ tempBlock[i]);
						}

						System.arraycopy(chainBlock, 0, output, retval + outputOffset, blockSize);
					}

					inputBufferOffset = 0;
					consumed += bufferFillRequired;
					retval += blockSize;
					
				}  else {
					System.arraycopy (input, consumed + inputOffset, inputBuffer, inputBufferOffset, remaining);
					inputBufferOffset += remaining;
					break;
				}
			}
			
			return retval;
			
		}  else if(state == DECRYPT_MODE) {
			while(true) {
				bufferFillRequired = blockSize - inputBufferOffset;
				remaining = inputLen - consumed;

				if(remaining - bufferFillRequired >= paddingType) {
					System.arraycopy(input, consumed + inputOffset, inputBuffer, inputBufferOffset, bufferFillRequired);

					if(mode == MODE_ECB) {
						decryptBlock(inputBuffer, tempBlock);
						System.arraycopy(tempBlock, 0, output, retval + outputOffset, blockSize);
					
					} else if(mode == MODE_CBC) {
						decryptBlock(inputBuffer, tempBlock);
						for(int i=0; i< blockSize; i++) {
							output[i + retval + outputOffset] = (byte)(chainBlock[i] ^ tempBlock[i]);
						}

						System.arraycopy(inputBuffer, 0, chainBlock, 0, blockSize);
					
					} else if(mode == MODE_CFB) {
						decryptBlock(chainBlock, tempBlock);
						for(int i=0; i< blockSize; i++) {
							output[i + retval + outputOffset] = (byte)(inputBuffer[i] ^ tempBlock[i]);
						}
						
						System.arraycopy(inputBuffer, 0, chainBlock, 0, blockSize);
					}

					inputBufferOffset = 0;
					consumed += bufferFillRequired;
					retval += blockSize;
					
				} else {
					System.arraycopy(input, consumed + inputOffset, inputBuffer, inputBufferOffset, remaining);
					inputBufferOffset += remaining;
					break;
				}
			}
			return retval;
		}
		
		else return 0;
	}

	// 암호화|복호화를 수행하는 함수
	// engineUpdate 함수를 호출하여 암호화/복호화를 수행하고 나머지 블럭(암호화/복호화 블럭 크기보다 작은 나머지 
	// 블럭)을 암호화 경우 Padding하고 암호화를 수행하며 복호화 경우 UnPadding을 하고 복호화를 수행한다.
	protected byte[] engineDoFinal(byte input[], int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
		try {
			byte output[] = new byte[engineGetOutputSize(inputLen)];
			int done = engineDoFinal(input, inputOffset, inputLen, output, 0);
			byte retval[] = new byte[done];
			System.arraycopy(output, 0, retval, 0, done);
			return retval;
			
		} catch(ShortBufferException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 위의 engineUpdate 함수 설명 참조
	protected int engineDoFinal(byte input[], int inputOffset, int inputLen, byte output[], int outputOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		int produced = engineUpdate(input, inputOffset, inputLen, output, outputOffset);

		if(state == ENCRYPT_MODE) {
			if(!(pad.doPadding())) return produced;

			if(mode == MODE_ECB){}
			
			if(mode == MODE_CBC) {
				for(int i=0; i< blockSize; i++) {
					inputBuffer[i] ^= chainBlock[i];
				}
			} else if(mode == MODE_CFB){}

			encryptBlock(inputBuffer, chainBlock);
			System.arraycopy(chainBlock, 0, output, produced + outputOffset, blockSize);

			reset();
			return (produced + blockSize);
			
		} else if(state == DECRYPT_MODE) {
			if(inputBufferOffset == 0) {
				reset();
				return produced;
			}

			if(inputBufferOffset != blockSize) {
				throw new IllegalBlockSizeException("Cipher Text length not a multiple of blockSize");
			}

			byte tempBlock[] = new byte[blockSize];
			decryptBlock(inputBuffer, tempBlock);


			if(mode == MODE_ECB) {
			}else if(mode == MODE_CBC) {
				for(int i=0; i< blockSize; i++) {
					tempBlock[i] ^= chainBlock[i];
				}
			} else if(mode == MODE_CFB) {}

			int dataSize = pad.doUnPadding(tempBlock);
			System.arraycopy(tempBlock, 0, output, outputOffset + produced, dataSize);

			reset();

			return (produced + dataSize);
		} else {
			return 0;
		} 
	}

	private void reset () {
		inputBuffer = new byte[blockSize];
		inputBufferOffset = 0;
		inputBufferLen = 0;
		if(this.needIV) chainBlock = (byte[])(IV.clone());
		else chainBlock = new byte[blockSize];
	}

	protected byte[] getKey() {
		int length = key.length;
		for(int i = 0; i < length; i++) {
			key[i] ^= key[length - i - 1];
		}
		
		return key;		
	}
		
	// BlockCipher에서 사용할 Padding inner 클래스들
	// 이 부분은 Cipher에서 getInstance method를 사용해서 해당 알고리즘을 가져오는 방식으로 
	// 코딩해도 된다. 여기서는 메모리와 편리성보다는 속도를 위해서 inner class로 해결한 것임.
	protected class PKCS5Padding extends Padding {
		protected boolean doPadding() {
			int extra = (inputBufferOffset != 0) ? blockSize - inputBufferOffset : blockSize;

			for(int i=0; i<extra; i++) {
				inputBuffer[inputBufferOffset + i] = (byte)extra;
			}
			
			return true;
		}

		protected int doUnPadding(byte buffer[]) throws BadPaddingException {
			int numPad = buffer[buffer.length - 1];

			if((numPad < 1) || (numPad > blockSize)) {
				throw new BadPaddingException("Weird number of padding bytes: " + numPad + " detected" + blockSize);
			}
			
			return (blockSize - numPad);
		}
	}

	protected class Zeroes extends Padding {
		protected boolean doPadding() throws IllegalBlockSizeException {
			if(inputBufferOffset == 0) return false;

			int extra = (inputBufferOffset != 0) ? blockSize - inputBufferOffset : 0;

			for(int i=0; i<extra; i++) {
				inputBuffer[inputBufferOffset + i] = (byte)0x00;
			}

			return true;
		}
	}

	protected class Padding {
		protected boolean doPadding() throws IllegalBlockSizeException {
			if(inputBufferOffset != 0) {
				throw new IllegalBlockSizeException("Needs padding");
			}

			return false;
		}

		protected int doUnPadding(byte buffer[]) throws BadPaddingException {
			return blockSize;
		}
	}
}

