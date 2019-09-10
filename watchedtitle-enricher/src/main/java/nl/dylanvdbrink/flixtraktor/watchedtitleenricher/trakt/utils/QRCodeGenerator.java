package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.utils;

import java.util.EnumMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.log4j.Log4j;

@CommonsLog
public class QRCodeGenerator {
	
	private QRCodeGenerator() { }

	/**
	 * Generate the qr code
	 * @param text
	 * @return The string with the qrcode in it
	 */
	public static String generate(String text) {
		String s = "";
		int width = 0;
		int height = 0;
		Map<EncodeHintType, Object> qrParam = new EnumMap<>(EncodeHintType.class);
		qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		qrParam.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, qrParam);
			s = toAscii(bitMatrix);
		} catch (WriterException e) {
			log.error(e);
		}
		return s;
	}

	private static String toAscii(BitMatrix bitMatrix) {
		StringBuilder sb = new StringBuilder();
		for (int rows = 0; rows < bitMatrix.getHeight(); rows++) {
			for (int cols = 0; cols < bitMatrix.getWidth(); cols++) {
				boolean x = bitMatrix.get(rows, cols);
				if (!x) {
					// white
					sb.append("\033[47m  \033[0m");
				} else {
					sb.append("\033[40m  \033[0m");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
