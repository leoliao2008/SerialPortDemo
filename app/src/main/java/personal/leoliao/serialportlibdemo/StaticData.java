package personal.leoliao.serialportlibdemo;

/**
 * Created by 廖华凯 on 2017/11/20.
 */

public interface StaticData {
    String SHARED_PREFERENCES_NAME="Serial_Port_Test_Config";
    String CURRENT_BAUD_RATE = "CURRENT_BAUD_RATE";
    String CURRENT_SERIAL_PORT="CURRENT_SERIAL_PORT";
    String GPIO_POW_CTRL="GPIO_POW_CTRL";
    String GPIO_SP_CTRL="GPIO_SP_CTRL";
    String GPIO_POW_CTRL_PATH="/sys/devices/platform/soten/uhf_power";
    String GPIO_SP_CTRL_PATH="/sys/devices/platform/soten/uhf_pdn";
}
