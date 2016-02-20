package com.app.yesi.scal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView2;
    private TextView textView1;
    private Calculate cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        cal = new Calculate();
    }

    public void onClickResultBtn(View view)
    {
        try {
            String str = textView2.getText().toString();
            textView1.setText(str + "=");
            str = cal.changeContent(str);
            textView2.setText(cal.result(str));
        }
        catch (Exception e)
        {
            textView2.setText("Syntext Error");
        }
    }
    public void onClickNegBtn(View view)
    {
        String content = textView2.getText().toString();
        textView2.setText("-(" + content + ")");
    }
    public void onClickDelBtn(View view)
    {
        String str = textView2.getText().toString();
        if(str.equals("0"))
        {
            str = textView1.getText().toString();
            if(str.equals(""))
            {
                return;
            }
            textView1.setText(str.substring(0,str.length()-1));
            return;
        }

        if(str.length()==1)
        {
            str = "0";
        }
        else {
            str = str.substring(0, str.length() - 1);
        }

        textView2.setText(str);
    }
    public void onClickAcBtn(View view)
    {
        textView1.setText("");
        textView2.setText("0");
    }
    public void onClickFuncBtn(View view)
    {
        String str=textView2.getText().toString();

        if(str.equals("0"))
        {
            str = "";
            System.out.println("00");
        }
        if(!textView1.getText().toString().equals(""))
        {
            textView1.setText("");
            str ="";
            System.out.println("11");
        }

        switch (view.getId())
        {
            case R.id.logBtn:
                str += "Log";
                break;
        }

        textView2.setText(str);
    }
    public void onClickOpBtn(View view)
    {
        String str="";

        switch (view.getId())
        {
            case R.id.sumBtn:
                str ="+";
                break;
            case R.id.subBtn:
                str="-";
                break;
            case R.id.multipleBtn:
                str="×";
                break;
            case R.id.divBtn:
                str = "÷";
                break;

        }
        textView2.append(str);
    }
    public void onClickNumBtn(View view)
    {
        String str=textView2.getText().toString();

        if(str.equals("0"))
        {
            str = "";
        }
        if(!textView1.getText().toString().equals(""))
        {
            textView1.setText("");
            str ="";
        }
        switch (view.getId())
        {
            case R.id.openBtn:
                str +="(";
                break;
            case R.id.closeBtn:
                str +=")";
                break;
            case R.id.zeroBtn:
                str += "0";
                break;
            case R.id.oneBtn:
                str += "1";
                break;
            case R.id.twoBtn:
                str += "2";
                break;
            case R.id.threeBtn:
                str += "3";
                break;
            case R.id.fourBtn:
                str += "4";
                break;
            case R.id.fiveBtn:
                str += "5";
                break;
            case R.id.sixBtn:
                str += "6";
                break;
            case R.id.sevenBtn:
                str += "7";
                break;
            case R.id.eightBtn:
                str += "8";
                break;
            case R.id.nineBtn:
                str += "9";
                break;
            case R.id.pointBtn:
                str +=".";
                break;
            default:
                break;
        }

        textView2.setText(str);
    }
}
