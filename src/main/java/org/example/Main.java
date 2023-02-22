package org.example;

import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, SaxonApiException {
        Processor processor = new Processor(false);

        System.out.println("Example 1:");

        JsonAsXdmValue2XdmValue(processor);

        System.out.println();

        System.out.println("Example 2:");

        Json2Xml2Xml2Json(processor);
    }

    static void JsonAsXdmValue2XdmValue(Processor processor) throws FileNotFoundException, SaxonApiException {
        JsonBuilder jsonBuilder = processor.newJsonBuilder();

        XdmValue input = jsonBuilder.parseJson(new InputStreamReader(new FileInputStream("sample1.json"), StandardCharsets.UTF_8));

        XsltCompiler xsltCompiler = processor.newXsltCompiler();

        XsltExecutable xsltExecutable = xsltCompiler.compile(new StreamSource(new File("sheet1.xsl")));

        Xslt30Transformer xslt30Transformer = xsltExecutable.load30();

        XdmValue result = xslt30Transformer.applyTemplates(input);

        System.out.println(result);
    }

    static void Json2Xml2Xml2Json(Processor processor) throws SaxonApiException {
        XsltCompiler xsltCompiler = processor.newXsltCompiler();

        XsltExecutable xsltExecutable = xsltCompiler.compile(new StreamSource(new File("sheet2.xsl")));

        Xslt30Transformer xslt30Transformer = xsltExecutable.load30();

        xslt30Transformer.setStylesheetParameters(Collections.singletonMap(new QName("json-uri"), new XdmAtomicValue("sample1.json")));

        XdmValue result = xslt30Transformer.callTemplate(null);

        System.out.println(result);
    }
}