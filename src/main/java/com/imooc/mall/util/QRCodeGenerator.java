package com.imooc.mall.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
  /**
   * @see "https://www.jb51.net/article/112830.htm"
   */
  public static void generatorQRCodeImage(String contents, int width, int height, String path) throws WriterException
      , IOException {
    //设置参数，输出文件
    Map<EncodeHintType, String> hints = new HashMap<>();
    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

    //输出目标文件
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
      file.createNewFile();
    }

    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", file.toPath());
  }

  public static void main(String[] args) {
    try {
      generatorQRCodeImage("HELLo", 320, 320, "D:\\codes\\me\\mall2\\src\\main\\resources\\static\\demo.png");
    } catch (WriterException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}