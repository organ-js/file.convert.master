package com.lx.file.convert.master;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.xmlgraphics.io.URIResolverAdapter;
import org.xml.sax.SAXException;

public class XMlToPDF2 {
	public static void main(String[] args) throws SAXException, IOException {

		// Step 1: Construct a FopFactory by specifying a reference to the configuration file
		// (reuse if you plan to render multiple documents!)
		FopFactory fopFactory = null;
		URIResolverAdapter uriResolverAdapter = new URIResolverAdapter(new URIResolver(){
			public Source resolve(String href, String base) throws TransformerException {
				try {
					System.out.println(href+"----------------------");
		            URL url = new URL(href);
		            URLConnection connection = url.openConnection();
		            connection.setRequestProperty("User-Agent", "whatever");
		            return new StreamSource(connection.getInputStream());
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
			}
		});
//		FopFactoryBuilder builder = new FopFactoryBuilder(URI.create("/"), uriResolverAdapter);
//		fopFactory = builder.build();
		fopFactory = FopFactory.newInstance(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\fop-2.3\\fop\\conf\\fop.xconf"));

		// Step 2: Set up output stream.
		// Note: Using BufferedOutputStream for performance reasons (helpful with FileOutputStreams).
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\complete.pdf")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
		    // Step 3: Construct fop with desired output format
		    Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

		    // Step 4: Setup JAXP using identity transformer
		    TransformerFactory factory = TransformerFactory.newInstance();
		    Transformer transformer = factory.newTransformer(); // identity transformer
		    
		    // Step 5: Setup input and output for XSLT transformation
		    // Setup input stream
		    Source src = new StreamSource(new File("F:\\20.vscode\\iWorkP\\temp\\aa.xml"));

		    // Resulting SAX events (the generated FO) must be piped through to FOP
		    Result res = new SAXResult(fop.getDefaultHandler());
		    // Step 6: Start XSLT transformation and FOP processing
		    transformer.transform(src, res);

		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FOPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    //Clean-up
		    try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}}
}
