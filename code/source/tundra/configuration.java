package tundra;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-11-27 14:46:04 GMT+10:00
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.IOException;
import com.wm.app.b2b.server.Package;
import permafrost.tundra.configuration.ConfigurationManager;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.server.PackageHelper;
// --- <<IS-END-IMPORTS>> ---

public final class configuration

{
	// ---( internal utility methods )---

	final static configuration _instance = new configuration();

	static configuration _newInstance() { return new configuration(); }

	static configuration _cast(Object o) { return (configuration)o; }

	// ---( server methods )---




	public static final void all (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(all)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $refresh? {"false","true"}
		// [o] record:0:required $configurations
		IDataCursor cursor = pipeline.getCursor();

		try {
		    boolean refresh = IDataHelper.getOrDefault(cursor, "$refresh?", Boolean.class, false);
		    IData configurations = ConfigurationManager.all(refresh);
		    IDataHelper.put(cursor, "$configurations", configurations, false);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void clear (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(clear)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		ConfigurationManager.clear();
		// --- <<IS-END>> ---


	}



	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $package
		// [i] field:0:optional $refresh? {"false","true"}
		// [o] field:0:required $package
		// [o] record:0:required $configuration
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String packageName = IDataHelper.get(cursor, "$package", String.class);
		    boolean refresh = IDataHelper.getOrDefault(cursor, "$refresh?", Boolean.class, false);

		    if (packageName == null) {
		        // infer package name from invoking service
		        Package invokingPackage = PackageHelper.self();
		        if (invokingPackage != null) packageName = invokingPackage.getName();
		    }

		    IData configuration = ConfigurationManager.get(packageName, refresh);

		    IDataHelper.put(cursor, "$package", packageName);
		    IDataHelper.put(cursor, "$configuration", configuration);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void list (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(list)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $refresh? {"false","true"}
		// [o] record:1:required $configurations
		// [o] - field:0:required package
		// [o] - record:0:required configuration
		// [o] - object:0:required configuration.length
		IDataCursor cursor = pipeline.getCursor();

		try {
		    boolean refresh = IDataHelper.getOrDefault(cursor, "$refresh?", Boolean.class, false);
		    IData[] configurations = ConfigurationManager.list(refresh);
		    IDataHelper.put(cursor, "$configurations", configurations, false);
		    IDataHelper.put(cursor, "$configurations.length", configurations == null ? 0 : configurations.length);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void peek (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(peek)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $package
		// [o] record:0:required $configuration
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String packageName = IDataHelper.get(cursor, "$package", String.class);
		    IDataHelper.put(cursor, "$configuration", ConfigurationManager.peek(packageName));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void refresh (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(refresh)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		ConfigurationManager.refresh();
		// --- <<IS-END>> ---


	}
}

