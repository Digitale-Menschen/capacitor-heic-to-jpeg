package ch.digitalemenschen.capacitorheictojpeg;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "CapacitorHeicToJpeg")
public class CapacitorHeicToJpegPlugin extends Plugin {

    private CapacitorHeicToJpeg implementation = new CapacitorHeicToJpeg();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }
}
