package de.robv.android.xposed.mods.irdetofix;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.util.Log;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class IrdetoFix implements IXposedHookZygoteInit {
	private static final String TAG = "IrdetoFix";

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		findAndHookMethod("android.app.ApplicationLoaders", null, "getClassLoader",
				String.class, String.class, ClassLoader.class, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				String dexPath = (String) param.args[0];
				if (dexPath.contains("/com.optimum.unity.mobile-")
				 || dexPath.contains("/com.lgi.orionandroid-")
				 || dexPath.contains("/com.viaplay.android-")) {
					// These apps analyze the stack and crash if they can't load the XposedBridge class.
					// So give them what they are looking for.
					dexPath = XposedBridge.BASE_DIR + "bin/XposedBridge.jar:" + dexPath;
					param.args[0] = dexPath;
					Log.i(TAG, "Set dexPath to " + dexPath);
				}
			}
		});
	}
}
