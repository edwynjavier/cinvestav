package mx.cinvestav.tamps.tm.ner;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

/**
 * It allows to encode and decode words into a number representation. This can 
 * be done by a given alphabet (set of ordered symbols/letters) or by the ASCII 
 * alphabet/code.
 * @author cgaytan@tamps.cinvestav.mx
 */
public class TextEncoding {
    
    /**
     * A defined alphabet, where a Character is a symbol of it.
     */
    private static final List<Character> alphabet = Arrays.asList('&', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
    
    /**
     * Encodes a word into a number representation between 0 and 1. This method 
     * do the encoding based in the previously defined alphabet.
     * @param   word    a String object to be encoded.
     * @return          a BigDecimal object (number) representing the given word,
     *                  or -1 if one, or more letters, of the word aren't into 
     *                  the alphabet.
     */
    public static BigDecimal toEncode(String word) {
        if (validate(word)) {
            return encode(word, false);
        } else {
            return BigDecimal.valueOf(-1);
        }
    }
    
    /**
     * Encodes a list of words into a number representation between 0 and 1. This
     * method do the encoding based in the previously defined alphabet.
     * @param   words   a list of Strings to be encoded.
     * @return          a HashMap object of Strings (words) with its respective 
     *                  BigDecimal (number representation), or an empty HashMap
     *                  if one word is invalid.
     */
    public static HashMap<String, BigDecimal> toEncode(List<String> words) {
        HashMap<String, BigDecimal> encodes = new HashMap();
        for (String word : words) {
            if (!validate(word)) {
                return encodes;
            } 
        }
        for (String word : words) {
            encodes.put(word, encode(word, false));
        }
        return encodes;
    }
    
    /**
     * Encodes a word into a number representation between 0 and 1. This method 
     * do the encoding based in the ASCII code.
     * @param   word    a String object to be encoded.
     * @return          a BigDecimal object (number) representing the given word.
     */
    public static BigDecimal toEncodeASCII(String word) {
        return encode(word, true);
    }
    
    /**
     * Encodes a list of words into a number representation between 0 and 1. This
     * method do the encoding based in the ASCII code.
     * @param   words   a list of String objects (words) to be encoded.
     * @return          a HashMap object of Strings (words) with its respective 
     *                  BigDecimal (number representation).
     */
    public static HashMap<String, BigDecimal> toEncodeASCII(List<String> words) {
        HashMap<String, BigDecimal> encodes = new HashMap();
        for (String word : words) {
            encodes.put(word, encode(word, true));
        }
        return encodes;
    }
    
    /**
     * Encodes a word into a number representation between 0 and 1.
     * @param   word    a String object to be encoded.
     * @param   ascci   false if the encoding will be done with the alphabet, or
     *                  true if will be with the ASCII code.
     * @return          a BigDecimal object (number) representing the given word.
     */
    private static BigDecimal encode(String word, boolean ascii) {
        BigDecimal symbolCode, base, encode = BigDecimal.ZERO;
        char[] charArray = word.toCharArray();
        
        if (ascii) {
            base = BigDecimal.valueOf(Character.MAX_CODE_POINT);
        } else {
            base = BigDecimal.valueOf(alphabet.size());
        }
        
        for (int i = 0; i < charArray.length; i++) {
            if (ascii) {
                symbolCode = BigDecimal.valueOf(charArray[i]);
            } else {
                symbolCode = BigDecimal.valueOf(alphabet.indexOf(charArray[i]));
            }
            encode = encode.add(powNegative(base, -(i+1)).multiply(symbolCode));
        }
        return encode;
    }
    
    /**
     * Decodes a number that represent a word. This method do the decoding based 
     * in the previously defined alphabet.
     * @param   encode  a BigDecimal object (number) representing a word.
     * @return          a String object (word) representing the given number, or
     *                  null if the number is invalid.
     */
    public static String toDecode(BigDecimal encode) {
        if (encode.signum() != -1) {
            return decode(encode, false);
        } else {
            return "null";
        }
    }
    
    /**
     * Decodes a list of numbers that represent words. This method do the decoding 
     * based in the previously defined alphabet.
     * @param   encodes a list of BigDecimal objects (numbers) representing words.
     * @return          a HashMap object of BigDecimal (numbers) with its 
     *                  respective String (representating word), or an empty 
     *                  HashMap if one number is invalid.
     */
    public static HashMap<BigDecimal, String> toDecode(List<BigDecimal> encodes) {
        HashMap<BigDecimal, String> decodes = new HashMap();
        for (BigDecimal encode : encodes) {
            if (encode.signum() == -1) {
                return decodes;
            }
        }
        for (BigDecimal encode : encodes) {
            decodes.put(encode, decode(encode, false));
        }
        return decodes;
    }
    
    /**
     * Decodes a number that represent a word. This method do the decoding based 
     * in the ASCII code.
     * @param   encode  a BigDecimal object (number) representing a word.
     * @return          a String object (word) representing the given number, or
     *                  null if the number is invalid.
     */
    public static String toDecodeASCII(BigDecimal encode) {
        if (encode.signum() != -1) {
            return decode(encode, true);
        } else {
            return null;
        }
    }
    
    /**
     * Decodes a list of numbers that represent words. This method do the decoding 
     * based in the ASCII code.
     * @param   encodes a list of BigDecimal objects (numbers) representing words.
     * @return          a HashMap object of BigDecimal (numbers) with its 
     *                  respective String (representating word), or an empty 
     *                  HashMap if one number is invalid.
     */
    public static HashMap<BigDecimal, String> toDecodeASCII(List<BigDecimal> encodes) {
        HashMap<BigDecimal, String> decodes = new HashMap();
        for (BigDecimal encode : encodes) {
            if (encode.signum() != -1) {
                decodes.put(encode, decode(encode, true));
            } else {
                decodes.put(encode, "null");
            }
        }
        return decodes;
    }
    
    /**
     * Decodes a number that represent a word.
     * @param   encode  a BigDecimal object (number) representing a word.
     * @param   ascci   false if the decoding will be done with the alphabet, or
     *                  true if will be with the ASCII code.
     * @return          a String object (word) representing the given number.
     */
    private static String decode(BigDecimal encode, boolean ascii) {
        BigDecimal base, exponential, result, decode = BigDecimal.ZERO;
        boolean stop = true;
        String word = "";
        
        if (ascii) {
            base = BigDecimal.valueOf(Character.MAX_CODE_POINT);
        } else {
            base = BigDecimal.valueOf(alphabet.size());
        }
        
        do {
            exponential = powNegative(base, -(word.length() + 1));
            for (int i = 0; i <= base.intValue(); i++) {
                result = exponential.multiply(BigDecimal.valueOf(i));
                result = decode.add(result);
                if (encode.compareTo(result) == -1) {
                    if (i-1 == 0) {
                        stop = false;
                        break;
                    }

                    result = exponential.multiply(BigDecimal.valueOf(i-1));
                    decode = decode.add(result);

                    if (ascii) {
                        word += String.valueOf((char) (i - 1));
                    } else {
                        word += alphabet.get(i-1);
                    }

                    break;
                }
            }
        } while(stop);
        
        return word;
    }
    
    /**
     * Valid if all the letters (symbols) that compose a word are in the 
     * previously defined alphabet.
     * @param   word    the word to by validated.
     * @return          true if all the letters are into the alphabet, false if 
     *                  not.
     */
    private static boolean validate(String word) {
        return alphabet.containsAll(
            Arrays.asList(
                ArrayUtils.toObject(
                    word.toCharArray()
                )
            )
        );
    }
    
    /**
     * Allows to power a number (BigDecimal object) to a nevatige exponent. Note:
     * naturally, the pow() BigDecimal method don't allows nevatige exponents.
     * @param   num a BigDecimal object (number) to be power.
     * @param   exp a positive number (times that the number will be divided).
     * @return      a BigDecimal object representing the negative pow of the given 
     *              number.
     */
    private static BigDecimal powNegative(BigDecimal num, int exp) {
        BigDecimal pow = BigDecimal.ONE;
        for (int i = exp; i < 0; i++) {
            pow = pow.divide(num, MathContext.DECIMAL128);
        }
        return pow;
    }

    public static void main(String[] args) throws Exception {
        List<String> words = Arrays.asList("editar", "ciudad", "ver", "hoteles");
//        List<String> words = Arrays.asList("edi/()tar", "ciu%&dad", "v()er", "hot^+-eles");
        for (String w : words) {
            System.out.println("word:\t" + w);
            BigDecimal encode = TextEncoding.toEncode(w);
            System.out.println("encode:\t" + encode);
            String decode = TextEncoding.toDecode(encode);
            System.out.println("decode:\t" + decode);
            System.out.println("equals:\t" + w.equals(decode));
        }
    }
}