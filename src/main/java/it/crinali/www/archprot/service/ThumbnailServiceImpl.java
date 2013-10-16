package it.crinali.www.archprot.service;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Component;

@Component
public class ThumbnailServiceImpl implements ThumbnailService {
	
	private static final Log log = LogFactory.getLog(ThumbnailServiceImpl.class);
	
	private static final int IMG_WIDTH = 800;
	private static final int IMG_HEIGHT = 600;

	public byte[] generateThumbnail(byte [] pdf	){
		ByteArrayInputStream bArray = new ByteArrayInputStream(pdf);
		PDDocument document = null;
		
		
		try {
			document = PDDocument.load( bArray );
			List<PDPage> pages = document.getDocumentCatalog().getAllPages();
			PDPage page=pages.get(0);
//			RenderedImage pageImage=page.convertToImage();
			BufferedImage resizeImageHintPng = resizeImageWithHint(convertRenderedImage(page.convertToImage()),BufferedImage.TYPE_BYTE_GRAY);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ImageIO.write(resizeImageHintPng,"png",baos);
			pages=null;
			page=null;
			byte [] retValue= baos.toByteArray();
			baos.close();
			baos=null;
			return retValue;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}  
		
		
	}
	
    private  BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){
    	 
	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	g.dispose();	
	g.setComposite(AlphaComposite.Src);
 
	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.setRenderingHint(RenderingHints.KEY_RENDERING,
	RenderingHints.VALUE_RENDER_QUALITY);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	RenderingHints.VALUE_ANTIALIAS_ON);
 
	return resizedImage;
    }
    
	private BufferedImage convertRenderedImage(RenderedImage img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage)img;	
		}	
		ColorModel cm = img.getColorModel();
		int width = img.getWidth();
		int height = img.getHeight();
		WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		Hashtable properties = new Hashtable();
		String[] keys = img.getPropertyNames();
		if (keys!=null) {
			for (int i = 0; i < keys.length; i++) {
				properties.put(keys[i], img.getProperty(keys[i]));
			}
		}
		BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
		img.copyData(raster);
		return result;
	}
}
