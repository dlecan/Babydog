package com.dlecan.phenochiot.framework;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
/**
 * @author dam
 * 
 * @revision $Revision: 1.2 $
 * @version $Name: $ @date $Date: 2004/01/10 21:17:06 $ @id $Id: Util.java,v
 *          1.2 2004/01/10 21:17:06 fris Exp $
 */
public class Util {
	/**
	 *  
	 */
	private static ImageRegistry image_registry;
	/**
	 * @param url_name
	 * @return
	 */
	public static URL newURL(String url_name) {
		try {
			return new URL(url_name);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL " + url_name, e);
		}
	}
	/**
	 * @return
	 */
	public static ImageRegistry getImageRegistry() {
		if (image_registry == null) {
			image_registry = new ImageRegistry();
			image_registry.put("logo_petit", ImageDescriptor
					.createFromURL(Util.class.getResource("/logo.png")));
		}
		return image_registry;
	}
}