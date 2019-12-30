package com.ontimize.util;

import java.lang.reflect.Array;

public class ArrayUtils {

	public static int findElementIndex(int[] array, int element) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == element) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @since 5.2077EN-0.3
	 */
	public static Object[] addAll(Object[] array1, Object[] array2) {
		if (array1 == null) {
			return ArrayUtils.clone(array2);
		} else if (array2 == null) {
			return ArrayUtils.clone(array1);
		}
		Object[] joinedArray = (Object[]) Array.newInstance(array1.getClass().getComponentType(), array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * @since 5.2077EN-0.3
	 */
	public static byte[] addAll(byte[] array1, byte[] array2) {
		if (array1 == null) {
			return ArrayUtils.clone(array2);
		} else if (array2 == null) {
			return ArrayUtils.clone(array1);
		}
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	public static Object[] clone(Object[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

}
