package com.amcglynn.myenergi;

public class ZappiResponse {

    public static String getErrorResponse() {
        return "{\"status\":\"-14\",\"statustext\":\"\"}";
    }

    public static String getGenericResponse() {
        return "{\"status\":0,\"statustext\":\"\",\"asn\":\"s18.myenergi.net\"}";
    }

    public static String getExampleResponse() {
        return "{\n" +
                "    \"zappi\": [\n" +
                "        {\n" +
                "            \"sno\": 12345678,\n" +
                "            \"dat\": \"16-02-2023\",\n" +
                "            \"tim\": \"11:47:14\",\n" +
                "            \"ectp1\": 0,\n" +
                "            \"ectp2\": 54,\n" +
                "            \"ectp3\": 0,\n" +
                "            \"ectt1\": \"Internal Load\",\n" +
                "            \"ectt2\": \"Grid\",\n" +
                "            \"ectt3\": \"None\",\n" +
                "            \"bsm\": 0,\n" +
                "            \"bst\": 0,\n" +
                "            \"cmt\": 254,\n" +
                "            \"dst\": 1,\n" +
                "            \"div\": 0,\n" +
                "            \"frq\": 50.01,\n" +
                "            \"fwv\": \"3562S4.525\",\n" +
                "            \"gen\": 594,\n" +
                "            \"grd\": 64,\n" +
                "            \"pha\": 1,\n" +
                "            \"pri\": 1,\n" +
                "            \"sta\": 1,\n" +
                "            \"tz\": 0,\n" +
                "            \"vol\": 2389,\n" +
                "            \"che\": 21.39,\n" +
                "            \"bss\": 0,\n" +
                "            \"lck\": 7,\n" +
                "            \"pst\": \"A\",\n" +
                "            \"zmo\": 3,\n" +
                "            \"pwm\": 1200,\n" +
                "            \"zs\": 256,\n" +
                "            \"rdc\": -1,\n" +
                "            \"rac\": 1,\n" +
                "            \"rrac\": -3,\n" +
                "            \"zsh\": 1,\n" +
                "            \"ectt4\": \"None\",\n" +
                "            \"ectt5\": \"None\",\n" +
                "            \"ectt6\": \"None\",\n" +
                "            \"newAppAvailable\": false,\n" +
                "            \"newBootloaderAvailable\": false,\n" +
                "            \"beingTamperedWith\": false,\n" +
                "            \"batteryDischargeEnabled\": false,\n" +
                "            \"mgl\": 30,\n" +
                "            \"sbh\": 17,\n" +
                "            \"sbk\": 5\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
