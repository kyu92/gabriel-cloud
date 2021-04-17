package com.kyu.gabriel.book.util;

import lombok.Cleanup;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

public class CoverUtil {

    public static InputStream getEpubCover(InputStream inputStream) throws IOException {
        EpubReader reader = new EpubReader();
        Book book = reader.readEpub(inputStream);
        Resource cover = book.getCoverImage();
        if (cover == null){
            return null;
        }
        return book.getCoverImage().getInputStream();
    }

    public static InputStream genPdfCover(InputStream inputStream) throws IOException {
        @Cleanup PDDocument document = PDDocument.load(inputStream);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
//        System.out.println("PDF文件总页数为:" + pageCount);
        BufferedImage image = pdfRenderer.renderImageWithDPI(0, 105, ImageType.ARGB);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(image, "png", imOut);
        return new ByteArrayInputStream(bs.toByteArray());
    }
}
