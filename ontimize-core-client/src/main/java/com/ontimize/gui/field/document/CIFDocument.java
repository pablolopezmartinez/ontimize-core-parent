package com.ontimize.gui.field.document;

import javax.swing.text.AttributeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the document model to insert a CIFs into a JTextField
 *
 * @version 1.0 01/04/2001
 */

public class CIFDocument extends MaskDocument {

	private static final Logger logger = LoggerFactory.getLogger(CIFDocument.class);

	/**
	 * Constructor. Uses the #MaskDocument constructor, with the correct mask to insert the CIF, this is: A0000000%
	 */
	public CIFDocument() {
		super("A0000000%");
	}

	/**
	 * Text insertion with input validations
	 */
	@Override
	public void insertString(int offset, String stringValue, AttributeSet attributes) {
		super.insertString(offset, stringValue, attributes);
		// If length is 8 then must insert the control character
		try {
			if (this.getLength() == 8) {
				// Get the 7 digits
				String sNumber = this.getText(1, 7);
				// Sum the digits in even positions
				boolean even = false;
				int evenSum = 0;
				for (int i = 0; i < sNumber.length(); i++) {
					if (even) {
						String evenDigit = sNumber.substring(i, i + 1);
						evenSum = evenSum + Integer.parseInt(evenDigit);
					}
					even = !even;
				}
				// Now odd digits: multiply by 2 y sum the digits
				int odd1 = 2 * Integer.parseInt(sNumber.substring(0, 1));
				int odd2 = 2 * Integer.parseInt(sNumber.substring(2, 3));
				int odd3 = 2 * Integer.parseInt(sNumber.substring(4, 5));
				int odd4 = 2 * Integer.parseInt(sNumber.substring(6, 7));
				// Now sum the digits (only greater than 10 values)
				String imp1 = Integer.toString(odd1);
				String imp2 = Integer.toString(odd2);
				String imp3 = Integer.toString(odd3);
				String imp4 = Integer.toString(odd4);
				odd1 = CIFDocument.checkStringMaximumLenghtIs2(odd1, imp1);
				odd2 = CIFDocument.checkStringMaximumLenghtIs2(odd2, imp2);
				odd3 = CIFDocument.checkStringMaximumLenghtIs2(odd3, imp3);
				odd4 = CIFDocument.checkStringMaximumLenghtIs2(odd4, imp4);
				// Now the total
				int totalOdd = odd1 + odd2 + odd3 + odd4;
				int totalEvenOdd = evenSum + totalOdd;
				int unitsDigit = 0;
				// Get the units digit of totalEvenOdd value.
				// Max length of totalEvenOdd is 2
				if (totalEvenOdd >= 10) {
					unitsDigit = Integer.parseInt(Integer.toString(totalEvenOdd).substring(1, 2));
				} else {
					unitsDigit = totalEvenOdd;
				}

				// Subtract the units digit to 10 (10 - units digit)
				int subtract = 10 - unitsDigit;
				// Automatic operation:
				// If first character is N, W, P, Q, R, S the control character
				// is a
				// letter, other case is a digit
				char firstCharacter = this.getText(0, 1).charAt(0);
				char characterToInsert = this.calculateCharacter(subtract, firstCharacter);
				// Now insert
				Character insertLetter = new Character(characterToInsert);
				super.insertString(8, insertLetter.toString(), attributes);
			}
		} catch (Exception e) {
			if (com.ontimize.gui.ApplicationManager.DEBUG) {
				CIFDocument.logger.debug(this.getClass().toString() + ": " + e.getMessage(), e);
			}
		}
	}

	protected char calculateCharacter(int subtract, char firstCharacter) {
		char characterToInsert = ' ';
		switch (firstCharacter) {
		case 'P':
			characterToInsert = CIFDocument.calculateLetter(subtract);
			break;
		case 'Q':
			characterToInsert = CIFDocument.calculateLetter(subtract);
			break;
		case 'X':
			characterToInsert = CIFDocument.calculateLetter(subtract);
			break;
		case 'N':
			characterToInsert = CIFDocument.calculateLetter(subtract);
			break;
		case 'S':
			characterToInsert = CIFDocument.calculateLetter(subtract);
			break;

		default:
			characterToInsert = CIFDocument.calculateNumber(subtract);
			break;
		}
		return characterToInsert;
	}

	public static char calculateLetter(int number) {
		switch (number) {
		case 1:
			return 'A';
		case 2:
			return 'B';
		case 3:
			return 'C';
		case 4:
			return 'D';
		case 5:
			return 'E';
		case 6:
			return 'F';
		case 7:
			return 'G';
		case 8:
			return 'H';
		case 9:
			return 'I';
		case 10:
			return 'J';
		}
		return ' ';
	}

	public static char calculateNumber(int number) {
		switch (number) {
		case 1:
			return '1';
		case 2:
			return '2';
		case 3:
			return '3';
		case 4:
			return '4';
		case 5:
			return '5';
		case 6:
			return '6';
		case 7:
			return '7';
		case 8:
			return '8';
		case 9:
			return '9';
		case 10:
			return '0';
		}
		return ' ';
	}

	/**
	 * CIF VALIDATION.<br>
	 * 1) First letter can be: A, B, C, D, E, F, G, H, N, P, Q, S, J ,U ,V, W, R. <br>
	 * A - Public corporation / Public limited company<br>
	 * B - Limited corporation / Limited company<br>
	 * C - Association / Collective corporation <br>
	 * D - Partnership <br>
	 * E - Co - ownership <br>
	 * F - Cooperatives <br>
	 * G - Associations and other not defined types <br>
	 * H - Home owners associations <br>
	 * J - Civil corporations <br>
	 * U - Temporal companies association <br>
	 * V - Not defined types <br>
	 * N - Foreign entity <br>
	 * W - Permanent establishment of non-resident entity in Spanish territory <br>
	 * Q - Public corporations <br>
	 * R - Religious corporations S - State and autonomous regions administration organisms <br>
	 * P - Local corporations <br>
	 * <br>
	 * 2) Validation algorithm <br>
	 * 1 - Remove the first and the last character in the CIF (first letter and control character), this return a string with 7 digits. <br>
	 * 2- Sum all digits in even position, getting a result whose name will be A <br>
	 * 3- For each digit in a odd position multiply it by 2, sum the result digits and store this value whose name will be B.<br>
	 * 4- Sum A and B getting the result C.<br>
	 * 5 - Get the units digit of C and subtract it from 10 (10 - C units digit) getting D. <br>
	 * 6 - Using this result D is possible to obtain the control character. If control character must be a numeric value, this is directly D. If control character must be a letter
	 * the equivalence is 1-->A, 1 -->B etc. <br>
	 * <br>
	 * EXAMPLE: CIF: "A58818501" <br>
	 * Remove first and last character getting "5881854" <br>
	 * Sum the even digits: A = 8 + 1 + 5 = 14 <br>
	 * With the odd positions multiply by 2 and sum the result digits:<br>
	 * 5 * 2 = 10 ==> 1 + 0 = 1 <br>
	 * 8 * 2 = 16 ==> 1 + 6 = 7 <br>
	 * 8 * 2 = 16 ==> 1 + 6 = 7 <br>
	 * 0 * 2 = 0 ==> 0 <br>
	 * B = 1 + 7 + 7 = 15 <br>
	 * C = A + B = 14 + 15 = 29 <br>
	 * The units digit of C is 9. Abstract this value from 10 getting 10 - 9 = 1<br>
	 * If control character is a letter this is "A". If must be a number is directly 1.<br>
	 * In this example CIF values starts with C and all CIF with C uses a numeric control character. <br>
	 * In case of result = 10 - 0, the validation number will be ZERO or the letter J
	 *
	 * @param CIF
	 * @return
	 */
	public static boolean isCIFWellFormed(String CIF) {
		try {
			if (CIF == null) {
				return false;
			}
			int length = CIF.length();
			if (length < 9) {
				return false;
			}

			if (!CIFDocument.checkFirstLetter(CIF)) {
				CIFDocument.logger.info("Error in first letter: {}", CIF);
				return false;
			}

			// Check the control digit
			char controlDigit = ' ';
			char controlLetter = ' ';
			// Get only the first 7 digits
			String sNumber = CIF.substring(1, 8);
			// Sum the even position digits
			boolean even = false;
			int evenSum = 0;
			for (int i = 0; i < sNumber.length(); i++) {
				if (even) {
					String evenDigit = sNumber.substring(i, i + 1);
					evenSum = evenSum + Integer.parseInt(evenDigit);
				}
				even = !even;
			}
			// Now odd digits: multiply by 2 and sum the digits in the result
			// value
			int odd1 = 2 * Integer.parseInt(sNumber.substring(0, 1));
			int odd2 = 2 * Integer.parseInt(sNumber.substring(2, 3));
			int odd3 = 2 * Integer.parseInt(sNumber.substring(4, 5));
			int odd4 = 2 * Integer.parseInt(sNumber.substring(6, 7));
			// Now sum the digits (in values greater than 10)
			String sOdd1 = Integer.toString(odd1);
			String sOdd2 = Integer.toString(odd2);
			String sOdd3 = Integer.toString(odd3);
			String sOdd4 = Integer.toString(odd4);

			// If string length is 1, do nothing. Maximum length is 2
			odd1 = CIFDocument.checkStringMaximumLenghtIs2(odd1, sOdd1);
			odd2 = CIFDocument.checkStringMaximumLenghtIs2(odd2, sOdd2);
			odd3 = CIFDocument.checkStringMaximumLenghtIs2(odd3, sOdd3);
			odd4 = CIFDocument.checkStringMaximumLenghtIs2(odd4, sOdd4);
			// Now the total
			int totalOdd = odd1 + odd2 + odd3 + odd4;
			int totalEvenOdd = evenSum + totalOdd;
			int unitsDigit = 0;

			// Get the units digit of totalEvenOdd value.
			// Maximum length of totalEvenOdd value is 2
			if (totalEvenOdd >= 10) {
				unitsDigit = Integer.parseInt(Integer.toString(totalEvenOdd).substring(1, 2));
			} else {
				unitsDigit = totalEvenOdd;
			}
			// Subtract the units digits from 10 (10 - units digit)
			int subtract = 10 - unitsDigit;
			// Check
			controlDigit = CIFDocument.calculateNumber(subtract);
			controlLetter = CIFDocument.calculateLetter(subtract);
			if ((CIF.charAt(8) != controlDigit) && (CIF.charAt(8) != controlLetter)) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			if (com.ontimize.gui.ApplicationManager.DEBUG) {
				CIFDocument.logger.debug("CIFDocument: " + e.getMessage(), e);
			}
			return false;
		}
	}

	protected static boolean checkFirstLetter(String CIF) {
		// Check the first letter
		char fistLetter = CIF.charAt(0);
		switch (fistLetter) {
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'N':
		case 'P':
		case 'Q':
		case 'S':
		case 'J':
		case 'R':
		case 'U':
		case 'V':
		case 'W':
			return true;
		default:
			// If first letter is a different one, is invalid
			return false;
		}

	}

	/**
	 * Method used to reduce the complexity of {@link #isCIFWellFormed(String)}
	 *
	 * @param odd1
	 * @param sOdd1
	 * @return
	 * @throws NumberFormatException
	 */
	protected static int checkStringMaximumLenghtIs2(int odd1, String sOdd1) throws NumberFormatException {
		if (sOdd1.length() > 1) {
			odd1 = Integer.parseInt(sOdd1.substring(0, 1)) + Integer.parseInt(sOdd1.substring(1, 2));
		}
		return odd1;
	}

}
