package com.app.yesi.scal;

import android.widget.TextView;

/**
 * Created by leeyeeun on 2016-02-23.
 */
public class NumberSystem {
    private int numberSystemNum;
    public NumberSystem(){
        numberSystemNum = 10;
    }

    public String changeNumSys(String num, int changeNS)
    {
        if(num.equals("0"))
        {
            numberSystemNum = changeNS;
            return "0";
        }
        int answer = Integer.parseInt(num,numberSystemNum);

        numberSystemNum = changeNS;

        switch (changeNS)
        {
            case 2:
                return Integer.toBinaryString(answer);
            case 8:
                return Integer.toHexString(answer);
            case 10:
                return Integer.toString(answer);
            case 16:
                return Integer.toOctalString(answer);
            default:
                return "";
        }
    }

}
