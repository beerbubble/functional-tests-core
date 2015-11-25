package common.Device.iOS;

import common.Enums.OSType;
import common.Log.Log;
import common.OSUtils.OSUtils;
import common.Settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Dimitar on 10/9/2015.
 */
public class Simctl {

    private static List<String> getSimulatorsIdsByName(String deviceName) {
        String rowData = OSUtils.runProcess(true, "xcrun simctl list devices");
        String[] rowList = rowData.split("\\r?\\n");

        List<String> list = new ArrayList<String>();

        for (String rowLine : rowList) {
            if ((rowLine.contains(deviceName)) && (rowLine.contains("(")) && (!rowLine.contains("unavailable"))) {
                String line = rowLine.substring(rowLine.indexOf('(') + 1, rowLine.indexOf(')')).replaceAll("\\s+", "");
                list.add(line);
            }
        }

        return list;
    }

    public static void deleteSimulator(String deviceName) {
        List<String> simulators = getSimulatorsIdsByName(deviceName);

        for (String sim : simulators) {
            Log.info("Delete " + deviceName + " simulator with id: " + sim);
            OSUtils.runProcess(true, "xcrun simctl delete " + sim);
        }
    }

    public static String createSimulator(String simulatorName, String deviceType, String iOSVersion) {

        Log.info("Create simulator with following command:");
        Log.info("xcrun simctl create \"" + simulatorName + "\" \"" + deviceType + "\" \"" + iOSVersion + "\"");
        String output = OSUtils.runProcess(true, "xcrun simctl create \"" + simulatorName + "\" \"" + deviceType + "\" \"" + iOSVersion + "\"");
        return output;
    }
}