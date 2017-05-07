package tundra;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-05-06 15:54:31 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.IOException;
import java.nio.charset.Charset;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.io.InputStreamHelper;
import permafrost.tundra.lang.CharsetHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.ObjectConvertMode;
import permafrost.tundra.lang.ObjectHelper;
import permafrost.tundra.zip.GzipHelper;
// --- <<IS-END-IMPORTS>> ---

public final class gzip

{
	// ---( internal utility methods )---

	final static gzip _instance = new gzip();

	static gzip _newInstance() { return new gzip(); }

	static gzip _cast(Object o) { return (gzip)o; }

	// ---( server methods )---




	public static final void compress (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(compress)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:optional $content
		// [i] field:0:optional $encoding
		// [i] field:0:optional $mode {"stream","bytes","string","base64"}
		// [o] object:0:optional $content.gzip
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    Object input = IDataHelper.get(cursor, "$content");
		    Charset charset = IDataHelper.get(cursor, "$encoding", Charset.class);
		    ObjectConvertMode mode = IDataHelper.get(cursor, "$mode", ObjectConvertMode.class);
		
		    Object output = ObjectHelper.convert(GzipHelper.compress(InputStreamHelper.normalize(input, charset)), charset, mode);
		
		    IDataHelper.put(cursor, "$content.gzip", output, false);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void decompress (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(decompress)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:optional $content.gzip
		// [i] field:0:optional $encoding
		// [i] field:0:optional $mode {"stream","bytes","string","base64"}
		// [o] object:0:optional $content
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    Object input = IDataHelper.get(cursor, "$content.gzip");
		    Charset charset = IDataHelper.get(cursor, "$encoding", Charset.class);
		    ObjectConvertMode mode = IDataHelper.get(cursor, "$mode", ObjectConvertMode.class);
		
		    Object output = ObjectHelper.convert(GzipHelper.decompress(InputStreamHelper.normalize(input, charset)), charset, mode);
		
		    IDataHelper.put(cursor, "$content", output, false);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

