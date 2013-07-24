package com.dianping.polestar.lang;

import java.io.InputStream;
import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClassUtils {
	private static final Log LOG = LogFactory.getLog(ClassUtils.class);

	private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
		@Override
		protected ClassLoader doGetClassLoader() throws Throwable {
			return Thread.currentThread().getContextClassLoader();
		}
	};

	private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
		@Override
		protected ClassLoader doGetClassLoader() throws Throwable {
			return ClassUtils.class.getClassLoader();
		}
	};

	private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
		@Override
		protected ClassLoader doGetClassLoader() throws Throwable {
			return ClassLoader.getSystemClassLoader();
		}
	};

	public static InputStream getResourceAsStream(String name) {

		InputStream is = THREAD_CL_ACCESSOR.getResourceStream(name);

		if (is == null) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("Resource ["
						+ name
						+ "] was not found via the thread context ClassLoader.  Trying the "
						+ "current ClassLoader...");
			}
			is = CLASS_CL_ACCESSOR.getResourceStream(name);
		}

		if (is == null) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("Resource ["
						+ name
						+ "] was not found via the current class loader.  Trying the "
						+ "system/application ClassLoader...");
			}
			is = SYSTEM_CL_ACCESSOR.getResourceStream(name);
		}

		if (is == null && LOG.isTraceEnabled()) {
			LOG.trace("Resource ["
					+ name
					+ "] was not found via the thread context, current, or "
					+ "system/application ClassLoaders.  All heuristics have been exhausted.  Returning null.");
		}

		return is;
	}

	public static Class<?> forName(String fqcn) throws RuntimeException {

		Class<?> clazz = THREAD_CL_ACCESSOR.loadClass(fqcn);

		if (clazz == null) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("Unable to load class named ["
						+ fqcn
						+ "] from the thread context ClassLoader.  Trying the current ClassLoader...");
			}
			clazz = CLASS_CL_ACCESSOR.loadClass(fqcn);
		}

		if (clazz == null) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("Unable to load class named [" + fqcn
						+ "] from the current ClassLoader.  "
						+ "Trying the system/application ClassLoader...");
			}
			clazz = SYSTEM_CL_ACCESSOR.loadClass(fqcn);
		}

		if (clazz == null) {
			String msg = "Unable to load class named ["
					+ fqcn
					+ "] from the thread context, current, or "
					+ "system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.";
			throw new RuntimeException(msg);
		}

		return clazz;
	}

	public static boolean isAvailable(String fullyQualifiedClassName) {
		try {
			forName(fullyQualifiedClassName);
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}

	public static Object newInstance(String fqcn) {
		return newInstance(forName(fqcn));
	}

	public static Object newInstance(String fqcn, Object... args) {
		return newInstance(forName(fqcn), args);
	}

	public static Object newInstance(Class<?> clazz) {
		if (clazz == null) {
			String msg = "Class method parameter cannot be null.";
			throw new IllegalArgumentException(msg);
		}
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate class ["
					+ clazz.getName() + "]", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Object newInstance(Class<?> clazz, Object... args) {
		Class[] argTypes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		Constructor<?> ctor = getConstructor(clazz, argTypes);
		return instantiate(ctor, args);
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... argTypes) {
		try {
			return clazz.getConstructor(argTypes);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		}

	}

	public static Object instantiate(Constructor<?> ctor, Object... args) {
		try {
			return ctor.newInstance(args);
		} catch (Exception e) {
			String msg = "Unable to instantiate Permission instance with constructor ["
					+ ctor + "]";
			throw new RuntimeException(msg, e);
		}
	}

	private static interface ClassLoaderAccessor {
		Class<?> loadClass(String fqcn);

		InputStream getResourceStream(String name);
	}

	private static abstract class ExceptionIgnoringAccessor implements
			ClassLoaderAccessor {

		public Class<?> loadClass(String fqcn) {
			Class<?> clazz = null;
			ClassLoader cl = getClassLoader();
			if (cl != null) {
				try {
					clazz = cl.loadClass(fqcn);
				} catch (ClassNotFoundException e) {
					if (LOG.isTraceEnabled()) {
						LOG.trace("Unable to load clazz named [" + fqcn
								+ "] from class loader [" + cl + "]");
					}
				}
			}
			return clazz;
		}

		public InputStream getResourceStream(String name) {
			InputStream is = null;
			ClassLoader cl = getClassLoader();
			if (cl != null) {
				is = cl.getResourceAsStream(name);
			}
			return is;
		}

		protected final ClassLoader getClassLoader() {
			try {
				return doGetClassLoader();
			} catch (Throwable t) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Unable to acquire ClassLoader.", t);
				}
			}
			return null;
		}

		protected abstract ClassLoader doGetClassLoader() throws Throwable;
	}

}
