package com.app.yesi.scal;

/**
 * Created by leeyeeun on 2016-03-12.
 */
public class History {
    private String[] question;
    private String[] answer;
    private int count;

    public History()
    {
        question = new String[7];
        answer = new String[7];
        count = 0;
    }
    public void set(String q,String a)
    {
        if(count==7) {
            for (int i = 1; i < count; i++) {
                question[i - 1] = question[i];
                answer[i - 1] = answer[i];
            }
            question[count] = q;
            answer[count] = a;
        }
        else
        {
            question[count] = q;
            answer[count] = a;
            count++;
        }
    }
}
