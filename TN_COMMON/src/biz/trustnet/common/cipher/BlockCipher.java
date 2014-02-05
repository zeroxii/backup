/* 
 * Project Name	:   TN_COMMON
 * File Name	:	BlockCipher.java
 * Date			:	2006. 5. 22. - ���� 9:44:48
 * History		:	2006. 5. 22.
 * Version		:	1.0
 * Author		:   ���ּ�	
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
	
	
	// ���� �ʱ�ȭ���� �ʾұ� ������ Cipher state�� STATE_UNITIALIZED
	// (Cipher.DECRYPT_MODE - ��ȣȭ ���,Cipher.ENCRYPT_MODE - ��ȣȭ ���)
	private int state = STATE_UNITIALIZED;
	
	// Default operation mode : ECB
	private int mode = MODE_ECB;
	
	// Default padding mode : PKCS5Padding(RSA�� PKCS standard)
	private Padding pad = new PKCS5Padding();
	private int paddingType = PKCS_PADDING;
	
	// Cipher�� ��ȣȭ/��ȣȭ ũ��
	protected int blockSize;
	
	// Cipher���� ����� Initial Vector
	protected byte[] IV = null;
	
	private byte[] key = null;
	
	// Cipher �Է� ����
	protected byte[] inputBuffer;
	protected int inputBufferOffset;
	protected int inputBufferLen;
	
	// Cipher � ��忡�� ���� �߰� ����
	protected byte chainBlock[];
	
	private SecureRandom rand;
	
	// Operation Mode(����)�� ECB ����̸� IV�� �ʿ����.
	// Default Operation Mode�� ECB ����̴�.
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

	// cipher�� clone(����)�Ǹ� �ȵǱ� ������ clone�ϰ� �Ǹ� exception�� �߻�
	public final Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();	
	}

	// Cipher���� �ش� �˰����� �ν��Ͻ��� ������ �� ����� padding Ŭ������ �Բ� ���õ�
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

	// Cipher���� �ش� �˰����� �ν��Ͻ��� ������ �� ����� Operation Mode�� �Բ� ���õ�
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

	// Cipher�� ��ȣȭ/��ȣȭ�ϴ� ���� ũ�⸦ return�ϴ� method
	protected int engineGetBlockSize() {
		return blockSize;
	}

	// Cipher�� ��ȣȭ/��ȣȭ�� ���� ���� ������� ũ�⸦ return�ϴ� method
	protected int engineGetOutputSize(int inputLen) {
		int outSize = inputLen + inputBufferOffset;
		if(this.state == ENCRYPT_MODE) {
			outSize += blockSize - (outSize % blockSize);
		}

		return outSize;
	}

	// Block Cipher�� ���� �߿��� IV(Initial Vector)�� ����ϴ� ���尡 �ִ�.
	// IV�� ������� �ʴ� ������ ��쿡�� IV ���� null�̴�.
	protected byte[] engineGetIV() {
		return IV;
	}

	// Cipher�� �ʱ�ȭ�ϴ� method�� initialize�� �Ǿ�� ��ȣȭ/��ȣȭ�� �� �� ����
	// �ش� �˰��򸶴� ��ȣȭ/��ȣȭ ����� �ٸ��Ƿ� �ʱ�ȭ�ϴ� ��ƾ�� �ٸ���
	// ������ �ش� �˰���(DES,DESede,SEED ���)���� �����ؾ� �Ѵ�.
	protected abstract void initialize(byte[] key, AlgorithmParameterSpec params)
		throws IllegalStateException, InvalidKeyException;

	// plainText(clearText)�� ��ȣȭ�ϴ� method
	// �ش� �˰��򸶴� ��ȣȭ/��ȣȭ ����� �ٸ��Ƿ� ��ȣȭ�ϴ� ��ƾ�� �ٸ���
	// ������ �ش� �˰���(DES,DESede,SEED ���)���� �����ؾ� �Ѵ�.
	protected abstract void encryptBlock(byte in[], byte out[]); 

	// cipherText�� ��ȣȭ�ϴ� method
	// �ش� �˰��򸶴� ��ȣȭ/��ȣȭ ����� �ٸ��Ƿ� ��ȣȭ�ϴ� ��ƾ�� �ٸ���
	// ������ �ش� �˰���(DES,DESede,SEED ���)���� �����ؾ� �Ѵ�.
	protected abstract void decryptBlock(byte in[], byte out[]);

	// Cipher�� �ʱ�ȭ�ϴ� method
	// ��ȣȭ/��ȣȭ ���, ���� ��, IV ���� �ʱ�ȭ�ϰ� ���������� �ʱ�ȭ�ϴ� initialize method�� ȣ��
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

	// Cipher�� �⺻���� �ʱ�ȭ�� ������ ���������� ������ ����ϴ� method�� engineUpdate�� �������� ��������� �ִ�.
	// +------ block size
	// +------ operation mode(ECB,CBC,CFBn,OFBn)
	//	+------ Initial Vector(chainBlock)
	// ���������� ��ȣȭ/��ȣȭ�� ������/�޽��� ���� �� ũ����� ä���� ������ �Ǵ� engineDoFinal�� ȣ��Ǳ� ����
	// �� �ӽ� ���ۿ� ������ ���� ���� �ֿ����̴�. �� Cipher�� �� ����� ���� ��쿡�� ��ȣȭ/��ȣȭ�� �����ϰ� 
	// �� ����� �����ϸ� engineDoFinal �Լ��� ȣ��Ǳ� ������ ������ ���� �����ȴ�. �̶� �߿��� ���� � ���
	// (operation mode:ECB,CBC,CFB,OFB)�� ���� �μ������� ���Ǵ� �߰� ����Ʈ ��(chainBlock)�鵵 ���Ǿ� ����ȴ�.
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

	// ��ȣȭ|��ȣȭ�� �����ϴ� �Լ�
	// engineUpdate �Լ��� ȣ���Ͽ� ��ȣȭ/��ȣȭ�� �����ϰ� ������ ��(��ȣȭ/��ȣȭ �� ũ�⺸�� ���� ������ 
	// ��)�� ��ȣȭ ��� Padding�ϰ� ��ȣȭ�� �����ϸ� ��ȣȭ ��� UnPadding�� �ϰ� ��ȣȭ�� �����Ѵ�.
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

	// ���� engineUpdate �Լ� ���� ����
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
		
	// BlockCipher���� ����� Padding inner Ŭ������
	// �� �κ��� Cipher���� getInstance method�� ����ؼ� �ش� �˰����� �������� ������� 
	// �ڵ��ص� �ȴ�. ���⼭�� �޸𸮿� �������ٴ� �ӵ��� ���ؼ� inner class�� �ذ��� ����.
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

