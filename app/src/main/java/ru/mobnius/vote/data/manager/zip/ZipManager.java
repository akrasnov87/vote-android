package ru.mobnius.vote.data.manager.zip;

import android.os.Build;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import ru.mobnius.vote.data.manager.packager.MetaSize;

/**
 * Сжатие строки через ZIP
 */
public class ZipManager {

    public static String getMode() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.M ? "LIB" : "ZIP";
    }

    /**
     * Сжать информацию
     * @param inputText текст
     * @return массив байтов
     */
    public static ZipResult compress(String inputText) throws IOException {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return oldCompress(inputText);
        } else {
            return newCompress(inputText);
        }
    }

    /**
     * Распаковать информацию
     * @param compressed данные
     * @return строка с информацией
     * @throws IOException исключение при обработке
     */
    public static byte[] decompress(byte[] compressed) throws IOException, DataFormatException {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return oldDeCompress(compressed);
        } else {
            return newDeCompress(compressed);
        }
    }

    private static ZipResult oldCompress(String inputText) {
        byte[] input = inputText.getBytes(StandardCharsets.UTF_8);
        ZipResult zipResult = new ZipResult(input);

        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        while (!compresser.finished()) {
            int byteCount = compresser.deflate(buf);
            baos.write(buf, 0, byteCount);
        }
        compresser.end();
        return zipResult.getResult(baos.toByteArray());
    }

    private static ZipResult newCompress(String inputText) throws IOException {
        byte[] bytes = inputText.getBytes(StandardCharsets.UTF_8);

        ZipResult zipResult = new ZipResult(bytes);

        InputStream stream = new ByteArrayInputStream(bytes);
        byte data[] = new byte[2048];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream( bos );
        BufferedInputStream entryStream = new BufferedInputStream( stream, 2048);
        ZipEntry entry = new ZipEntry( "" );
        zos.putNextEntry( entry );
        int count;
        while ( ( count = entryStream.read( data, 0, 2048) ) != -1 )
        {
            zos.write( data, 0, count );
        }
        entryStream.close();
        zos.closeEntry();
        zos.close();

        return zipResult.getResult(bos.toByteArray());
    }

    private static byte[] newDeCompress(byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        ZipInputStream zin = new ZipInputStream(bis);

        while(zin.getNextEntry()!=null) {
            // распаковка
            ByteArrayOutputStream fout = new ByteArrayOutputStream();
            for (int c = zin.read(); c != -1; c = zin.read()) {
                fout.write(c);
            }
            fout.flush();
            zin.closeEntry();
            fout.close();
            return fout.toByteArray();
        }
        return  null;
    }

    private static byte[] oldDeCompress(byte[] compressed) throws DataFormatException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressed.length);
        Inflater decompressor = new Inflater();
        try {
            decompressor.setInput(compressed);
            final byte[] buf = new byte[2048];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
        } finally {
            decompressor.end();
        }
        return bos.toByteArray();
    }
}
