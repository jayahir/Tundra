package tundra;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2020-09-10T05:09:41.617
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Map;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.io.CloseableHelper;
import permafrost.tundra.io.FileHelper;
import permafrost.tundra.io.InputStreamHelper;
import permafrost.tundra.io.MarkableFileInputStream;
import permafrost.tundra.io.OutputStreamHelper;
import permafrost.tundra.io.filter.FilenameFilterType;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.CharsetHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.ObjectConvertMode;
import permafrost.tundra.lang.ObjectHelper;
import permafrost.tundra.math.LongHelper;
import permafrost.tundra.security.MessageDigestHelper;
import permafrost.tundra.server.ServiceHelper;
import permafrost.tundra.time.DurationHelper;
// --- <<IS-END-IMPORTS>> ---

public final class file

{
	// ---( internal utility methods )---

	final static file _instance = new file();

	static file _newInstance() { return new file(); }

	static file _cast(Object o) { return (file)o; }

	// ---( server methods )---




	public static final void copy (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(copy)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file.source
		// [i] field:0:required $file.target
		// [i] field:0:optional $file.mode {"append","write"}
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String source = IDataHelper.get(cursor, "$file.source", String.class);
		    String target = IDataHelper.get(cursor, "$file.target", String.class);
		    String mode = IDataHelper.firstOrDefault(cursor, String.class, "create", "$file.mode", "$mode");

		    if (mode.equalsIgnoreCase("create") && FileHelper.exists(target)) {
		        throw new IOException("file already exists and will not be overwritten or appended to: " + target);
		    } else {
		        FileHelper.copy(source, target, mode.equalsIgnoreCase("append"));
		    }
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void create (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(create)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $file
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    IDataHelper.put(cursor, "$file", FileHelper.create(file));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void digest (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(digest)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [i] field:0:optional $digest.algorithm {"SHA-512","SHA-384","SHA-256","SHA","MD5","MD2"}
		// [i] field:0:optional $digest.mode {"stream","bytes","base64","hex"}
		// [o] object:0:optional $digest
		IDataCursor cursor = pipeline.getCursor();
		InputStream stream = null;

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    MessageDigest algorithm = IDataHelper.getOrDefault(cursor, "$digest.algorithm", MessageDigest.class, MessageDigestHelper.DEFAULT_ALGORITHM);
		    ObjectConvertMode mode = IDataHelper.get(cursor, "$digest.mode", ObjectConvertMode.class);

		    if (file != null) {
		        stream = new MarkableFileInputStream(file);
		        Map.Entry<? extends InputStream, byte[]> digest = MessageDigestHelper.digest(algorithm, stream);
		        stream = digest.getKey();
		        IDataHelper.put(cursor, "$digest", ObjectHelper.convert(digest.getValue(), mode));
		    }
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(NoSuchAlgorithmException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    CloseableHelper.close(stream);
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void equal (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(equal)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file.source
		// [i] field:0:required $file.target
		// [i] field:0:optional $raise? {"false","true"}
		// [o] field:0:required $file.equal? {"true","false"}
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String source = IDataHelper.get(cursor, "$file.source", String.class);
		    String target = IDataHelper.get(cursor, "$file.target", String.class);
		    boolean raise = IDataHelper.getOrDefault(cursor, "$raise?", Boolean.class, false);

		    boolean equal = FileHelper.equal(source, target, raise);

		    IDataHelper.put(cursor, "$file.equal?", equal, String.class);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(NoSuchAlgorithmException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void executable (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(executable)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [o] field:0:required $executable?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    IDataHelper.put(cursor, "$executable?", FileHelper.isExecutable(file), String.class);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void exists (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(exists)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [o] field:0:required $exists?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    IDataHelper.put(cursor, "$exists?", FileHelper.exists(file), String.class);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void gzip (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(gzip)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [i] field:0:optional $file.gzip
		// [i] field:0:optional $file.remove? {"false","true"}
		// [o] field:0:required $file.gzip
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String source = IDataHelper.get(cursor, "$file", String.class);
		    String target = IDataHelper.get(cursor, "$file.gzip", String.class);
		    boolean replace = IDataHelper.firstOrDefault(cursor, Boolean.class, false, "$file.remove?", "$replace?");

		    IDataHelper.put(cursor, "$file.gzip", FileHelper.gzip(source, target, replace));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void length (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(length)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [o] field:0:required $length
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    IDataHelper.put(cursor, "$length", FileHelper.length(file), String.class);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void match (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(match)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [i] field:0:required $pattern
		// [i] field:0:optional $mode {"regular expression","wildcard"}
		// [o] field:0:required $match?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    String pattern = IDataHelper.get(cursor, "$pattern", String.class);
		    String mode = IDataHelper.firstOrDefault(cursor, String.class, "regular expression", "$pattern.mode", "$mode");

		    IDataHelper.put(cursor, "$match?", FileHelper.match(file, pattern, mode.equalsIgnoreCase("regular expression") || mode.equalsIgnoreCase("regex")), String.class);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void normalize (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(normalize)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [o] field:0:required $file
		IDataCursor cursor = pipeline.getCursor();

		try {
		    IDataHelper.put(cursor, "$file", FileHelper.normalize(IDataHelper.get(cursor, "$file", String.class)));
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void process (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(process)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [i] field:0:optional $file.mode {"read","append","write"}
		// [i] field:0:required $service
		// [i] record:0:optional $pipeline
		// [i] field:0:optional $service.input
		// [i] field:0:optional $raise? {"true","false"}
		// [o] record:0:optional $pipeline
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    String mode = IDataHelper.firstOrDefault(cursor, String.class, "read", "$file.mode", "$mode");
		    String service = IDataHelper.get(cursor, "$service", String.class);
		    String input = IDataHelper.get(cursor, "$service.input", String.class);
		    IData scope = IDataHelper.getOrDefault(cursor, "$pipeline", IData.class, pipeline);
		    boolean raise = IDataHelper.getOrDefault(cursor, "$raise?", Boolean.class, true);

		    scope = FileHelper.process(FileHelper.construct(file), mode, service, input, scope, raise, true);

		    if (scope != pipeline) IDataHelper.put(cursor, "$pipeline", scope);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void purge (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(purge)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [i] field:0:optional $duration
		// [i] field:0:optional $duration.pattern {"xml","milliseconds","seconds","minutes","hours","days","weeks","months","years"}
		// [i] field:0:optional $filter.type {"regular expression","wildcard","literal"}
		// [o] field:0:required $count
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    String duration = IDataHelper.get(cursor, "$duration", String.class);
		    String pattern = IDataHelper.get(cursor, "$duration.pattern", String.class);
		    FilenameFilterType filterType = IDataHelper.get(cursor, "$filter.type", FilenameFilterType.class);

		    long count = FileHelper.purge(file, filterType, DurationHelper.parse(duration, pattern));

		    IDataHelper.put(cursor, "$count", count, String.class);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void read (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(read)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [i] field:0:optional $content.mode {"stream","bytes","string"}
		// [i] field:0:optional $content.encoding
		// [o] object:0:required $content
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    ObjectConvertMode mode = IDataHelper.first(cursor, ObjectConvertMode.class, "$content.mode", "$mode");
		    Charset charset = IDataHelper.first(cursor, Charset.class, "$content.encoding", "$encoding");

		    IDataHelper.put(cursor, "$content", ObjectHelper.convert(FileHelper.readToBytes(file), charset, mode));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void readable (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(readable)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [o] field:0:required $readable?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    IDataHelper.put(cursor, "$readable?", FileHelper.isReadable(file), String.class);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void reflect (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(reflect)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [o] record:0:required $file.properties
		// [o] - field:0:required exists?
		// [o] - field:0:optional parent
		// [o] - field:0:optional name
		// [o] - field:0:optional base
		// [o] - field:0:optional extension
		// [o] - field:0:optional type
		// [o] - field:0:optional length
		// [o] - field:0:optional modified
		// [o] - field:0:optional executable?
		// [o] - field:0:optional readable?
		// [o] - field:0:optional writable?
		// [o] - field:0:required uri
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    IDataHelper.put(cursor, "$file.properties", FileHelper.getPropertiesAsIData(file));
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void remove (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(remove)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $file
		IDataCursor cursor = pipeline.getCursor();

		try {
		    FileHelper.remove(IDataHelper.get(cursor, "$file", String.class));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void rename (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(rename)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file.source
		// [i] field:0:required $file.target
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String source = IDataHelper.get(cursor, "$file.source", String.class);
		    String target = IDataHelper.get(cursor, "$file.target", String.class);

		    FileHelper.rename(source, target);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void touch (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(touch)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [i] field:0:optional $file.create? {"true","false"}
		// [i] field:0:optional $file.updated
		IDataCursor cursor = pipeline.getCursor();

		try {
		    File file = IDataHelper.get(cursor, "$file", File.class);
		    boolean create = IDataHelper.getOrDefault(cursor, "$file.create?", Boolean.class, true);
		    Calendar updated = IDataHelper.get(cursor, "$file.updated", Calendar.class);

		    FileHelper.touch(file, create, updated);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void type (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(type)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [o] field:0:required $type
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    IDataHelper.put(cursor, "$type", FileHelper.getMIMEType(file));
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void writable (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(writable)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [o] field:0:required $writable?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    IDataHelper.put(cursor, "$writable?", FileHelper.isWritable(file), String.class);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void write (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(write)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $file
		// [i] field:0:optional $file.mode {"append","write","create"}
		// [i] object:0:optional $content
		// [i] field:0:optional $content.encoding
		// [o] field:0:required $file
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String file = IDataHelper.get(cursor, "$file", String.class);
		    String mode = IDataHelper.firstOrDefault(cursor, String.class, "create", "$file.mode", "$mode");
		    Object content = IDataHelper.get(cursor, "$content");
		    Charset charset = IDataHelper.first(cursor, Charset.class, "$content.encoding", "$encoding");

		    if (mode.equalsIgnoreCase("create") && FileHelper.exists(file)) {
		        throw new IOException("file already exists and will not be overwritten or appended to: " + file);
		    } else {
		        file = FileHelper.writeFromStream(file, InputStreamHelper.normalize(content, charset), mode.equalsIgnoreCase("append"));
		        IDataHelper.put(cursor, "$file", file);
		    }
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void zip (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(zip)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $file
		// [i] field:0:optional $file.zip
		// [i] field:0:optional $file.remove? {"false","true"}
		// [o] field:0:required $file.zip
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String source = IDataHelper.get(cursor, "$file", String.class);
		    String target = IDataHelper.get(cursor, "$file.zip", String.class);
		    boolean replace = IDataHelper.firstOrDefault(cursor, Boolean.class, false, "$file.remove?", "$replace?");

		    IDataHelper.put(cursor, "$file.zip", FileHelper.zip(source, target, replace));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

