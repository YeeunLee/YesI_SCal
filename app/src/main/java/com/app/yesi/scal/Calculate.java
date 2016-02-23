package com.app.yesi.scal;


import java.util.ArrayList;
import java.util.Stack;

import static java.lang.Math.*;

/**
 * Created by leeyeeun on 2016-02-05.
 */
public class Calculate {

    public Calculate()
    {

    }

    public String factorial(int n) //n!
    {
        if(n==0)
        {
            return "1";
        }
        double result = 1;
        for(int i = 2;i<=n;i++)
        {
            result *= i;
        }
        return rearrange(result);
    }
    public String combination(int n,int r)
    {
        double result1 = 1, result2 = 1;

        for(int i = n; i > n-r; i-- )
        {
            result1 *= i;
        }
        for(int i = 1; i <= r; i++ ) {
            result2 *= i;
        }

        return rearrange(result1/result2);
    }
    public String permutation(int n,int r)
    {
        double result = n;

        for(int i = n-1; i > n-r; i-- )
        {
            result *= i;
        }

        return rearrange(result);
    }
    public String changeContent(String content)//문자 재배열
    {
        content = content.replace("Log","L");
        content = content.replace("-(","-1×(");

        return content;
    }
    public int makeOpOrder(char c)//우선순위
    {
        switch (c)
        {
            case '+':
            case '-':
                return 1;
            case '×':
            case '÷':
                return 2;
            case 'L'://log
                return 3;
            case '!'://factorial
            case 'C'://combination
            case 'P'://permutation
                return 4;
            default:
                return -1;
        }
    }
    public String compute(String content)//계산
    {
        char[] operationCode = {'+', '-', '×', '÷', '(', ')','L','!','C','P'}; //연산 부호

        ArrayList<String> postfixList = new ArrayList<String>(); //후위표기법으로 변환 후 저장 할 ArrayList
        Stack<Character> opStack = new Stack<Character>(); // 연산 부호 우선순위처리 하며 후위 표기법으로 변경하는 Stack
        Stack<String> calculatorStack = new Stack<String>(); //후위 표기법을 계산하는 Stack

        int index = 0;//content.substring() 인수

        for(int i = 0; i < content.length(); i++)
        {
            for(int j = 0; j < operationCode.length; j++)
            {
                char tmpChar = content.charAt(i);
                if(tmpChar == operationCode[j])//문자열과 연산 부호 비교
                {
                    //postfixList에 연산 부호가 나오기 전까지의 숫자를 담는다(공백제거)
                    postfixList.add(content.substring(index, i).trim().replace("(", "").replace(")", ""));

                     //우 괄호가 나오면 좌 괄호가 나오거나 스택에 비어있을때 까지 pop하여 list에 저장
                    if((tmpChar == '(')&&(tmpChar == ')'))
                    {
                        while (true)
                        {
                            postfixList.add(opStack.pop().toString());

                            if(opStack.isEmpty()||opStack.pop() == '(')
                            {
                                break;
                            }
                        }
                    }

                    if(opStack.isEmpty())//opStack이 비어 있을 경우
                    {
                        opStack.push(operationCode[j]); //연산 부호 저장
                    }
                    else//opStack이 비어 있지 않을 경우
                    {
                        if(makeOpOrder(operationCode[j]) > makeOpOrder(opStack.peek()))//우선 순위 비교
                        {
                            opStack.push(operationCode[j]); //스택에 top 값 보다 높은 우선순위이면 그대로 저장
                        }
                        else if(makeOpOrder(operationCode[j]) <= makeOpOrder(opStack.peek()))//우선 순위 비교
                        {
                            postfixList.add(opStack.peek().toString());//스택에 있는 값이 우선순위가 같거나 작을 경우 list에 저장
                            opStack.pop();//스택 제거
                            opStack.push(operationCode[j]);//높은 우선순위 연산 부호 스택에 저장
                        }
                    }
                    index = i + 1;// 다음 순서 처리
                }
            }
        }
        postfixList.add(content.substring(index, content.length()).trim().replace("(", "").replace(")", "")); //마지막 숫자 처리

        if(!opStack.isEmpty())//Stack에 남아있는 연산 모두 postfixList에 추가
        {
            for(int i = 0; i < opStack.size();)
            {
                postfixList.add(opStack.peek().toString());
                opStack.pop();
            }
        }

        //list에 공백, 괄호 제거
        for (int i = 0; i < postfixList.size(); i++)
        {
            if (postfixList.get(i).equals(""))
            {
                postfixList.remove(i);
                i = i - 1;
            }
            else if (postfixList.get(i).equals("("))
            {
                postfixList.remove(i);
                i = i - 1;
            }
            else if (postfixList.get(i).equals(")"))
            {
                postfixList.remove(i);
                i = i - 1;
            }
        }

        System.out.println(postfixList);

        opStack.clear(); //Stack 비우기

        //postfixList를 calculatorStack에 저장하면서 후위연산 처리
        for (int i = 0; i < postfixList.size(); i++)
        {
            calculatorStack.push(postfixList.get(i));
            for (int j = 0; j < operationCode.length; j++)
            {
                if (postfixList.get(i).charAt(0) == operationCode[j])  //연산 부호 비교
                {
                    calculatorStack.pop(); //stack에 저장된 연산 부호 제거

                    double s2, s1; //stack에서 pop 되는 값들을 저장할 변수
                    String rs; // 연산 처리 후 문자열로 변환 후 stack에 저장할 변수

                    s2 = Double.parseDouble(calculatorStack.pop()); //스택에서 pop하여 문자열을 숫자로 형변환


                    //연산 부호에 해당하는 산술 처리 후 stack에 저장
                    switch (operationCode[j])
                    {
                        case '+':
                            s1 = Double.parseDouble(calculatorStack.pop());
                            rs = String.valueOf(s1 + s2);
                            calculatorStack.push(rs);
                            break;
                        case '-':
                            s1 = Double.parseDouble(calculatorStack.pop());
                            rs = String.valueOf(s1 - s2);
                            calculatorStack.push(rs);
                            break;
                        case '×':
                            s1 = Double.parseDouble(calculatorStack.pop());
                            rs = String.valueOf(s1 * s2);
                            calculatorStack.push(rs);
                            break;
                        case '÷':
                            s1 = Double.parseDouble(calculatorStack.pop());
                            rs = String.valueOf(s1 / s2);
                            calculatorStack.push(rs);
                            break;
                        case '!':
                            rs = String.valueOf(factorial((int) s2));
                            calculatorStack.push(rs);
                            break;
                        case 'L':
                            rs = String.valueOf(log10(s2));
                            calculatorStack.push(rs);
                            break;
                        case 'C':
                            s1 = Double.parseDouble(calculatorStack.pop());
                            rs = String.valueOf(combination((int)s1,(int)s2));
                            calculatorStack.push(rs);
                            break;
                        case 'P':
                            s1 = Double.parseDouble(calculatorStack.pop());
                            rs = String.valueOf(permutation((int)s1,(int)s2));
                            calculatorStack.push(rs);
                            break;
                    }
                }
            }
        }

        double re = Double.parseDouble(calculatorStack.peek()); //Stack Top 데이터


        return rearrange(re);
    }

    public String rearrange(double re)//소수점 정리
    {
      //  String result = String.format("%.10f", re); //소수점 10째짜리

        String result = Double.toString(re);

        //정수 부분 자리 구하기
        int num = 0;

        for (int i = 0; i < result.length(); i++)
        {
            if (result.charAt(i) == '.')
            {
                num = i;
                break;
            }
        }

        //정수부분
        String mok = result.substring(0, num);

        //나머지 연산
        double divde = Double.parseDouble(result) % Double.parseDouble(mok);

        //나머지가 0이면 소수점 자릿 수 안보이게

        if (divde == 0)
        {
            result = String.format("%.0f", re);
        }

        return result;
    }

}
