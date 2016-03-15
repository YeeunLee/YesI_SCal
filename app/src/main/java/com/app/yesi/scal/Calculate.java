package com.app.yesi.scal;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.Math.*;

/**
 * Created by leeyeeun on 2016-02-05.
 */
public class Calculate {

    private String ans; //바로전 계산값
    private String X,Y,Z;

    private String[] delim = {"+", "-","×", "÷","%","(",")", "l","!","C","P","s", "c", "t", "^", "$", "b", "n" ,"r" ,"p"};

    private Stack<String> tokenStack;
    private Stack<String> exprStack;
    private List<String> postOrderList;

    public Calculate()
    {
        ans = "0";
        X = "0";
        Y = "0";
        Z = "0";

        tokenStack = new Stack<String>();
        exprStack = new Stack<String>();
        postOrderList = new ArrayList<String>();
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
        content = content.replace("Log","l");
        content = content.replace("-(","0-1×(");
        content = content.replace("Sin","s");
        content = content.replace("Cos","c");
        content = content.replace("Tan","t");
        content = content.replace("^2","$");
        content = content.replace("1/","b");
        content = content.replace("Ln","n");
        content = content.replace("√","r");
        content = content.replace("*10^", "p");

        return content;
    }
    public int exprOrder(String s)//우선순위
    {
        char c = s.charAt(0);
        switch (c)
        {
            case '+':
                return 0;
            case '-':
                return 1;
            case '×':
            case '÷':
            case '%':
            case 'b':
            case 'p':
                return 2;
            case 'l'://log
            case 's':
            case 'c':
            case 't':
            case '^':
            case '$':
            case 'n':
            case 'r':
                return 3;
            case '!'://factorial
            case 'C'://combination
            case 'P'://permutation
                return 4;
            case '=':
                return 5;
            default:
                return -1;
        }
    }
    public void makeTokens(String s) {
        StringBuffer tokenBuf = new StringBuffer();

        int argSize = s.length();
        String token = null;
        for(int i = 0; i < argSize; i++) {
            token = s.substring(i, i + 1);
            if(!isDelim(token)) {
                tokenBuf.append(token);

                if(i == argSize - 1) {
                    tokenStack.push(tokenBuf.toString());
                }
            } else {
                if(tokenBuf.length() > 0) {
                    tokenStack.push(tokenBuf.toString());
                    tokenBuf = new StringBuffer();
                }

                tokenStack.push(token);
            }
        }
    }

    public boolean isDelim(String s) {
        boolean bDelim = false;

        int delimSize = delim.length;
        for(int i = 0; i < delimSize; i++) {
            if(delim[i].equals(s)) {
                bDelim = true;
                break;
            }
        }

        return bDelim;
    }

    private boolean isOpcode(String s) {
        boolean opcode = isDelim(s);

        if("(".equals(s) || ")".equals(s)) {
            opcode = false;
        }

        return opcode;
    }

    private void convertPostOrder(String expr) {
        for(String token : tokenStack) {
            if(isDelim(token)) {
                exprAppend(token);
            } else {
                postOrderList.add(token);
            }
        }

        // exprStack에 자료가 남아 있으면 출력한다
        String item = null;
        while(!exprStack.isEmpty()) {
            item = exprStack.pop();
            postOrderList.add(item);
        }
    }

    private void exprAppend(String token) {
        // '(' 일 경우 스택에 넣는다
        if("(".equals(token)) {
            exprStack.push(token);
            return;
        }

        // ')' 일 경우 '('가 나올 때 까지 출력한다
        if(")".equals(token)) {
            String opcode = null;
            while(true) {
                opcode = exprStack.pop();
                if("(".equals(opcode)) {
                    break;
                }

                postOrderList.add(opcode);
            }

            return;
        }

        // 연산자일 경우 스택에서 pop하여 낮은 우선순위를 만날때 까지 출력하고 자신을 push
        if(isOpcode(token)) {
            String opcode = null;
            while(true) {
                if(exprStack.isEmpty()) {
                    exprStack.push(token);
                    break;
                }

                opcode = exprStack.pop();
                if(exprOrder(opcode) > exprOrder(token)) {
                    postOrderList.add(opcode);
                } else {
                    exprStack.push(opcode);
                    exprStack.push(token);
                    break;
                }
            }

            return;
        }
    }

    public String calculate(String expr) {

        expr = changeContent(expr);
        // 토근을 만든다
        makeTokens(expr);
        // postOrder로 변환
        convertPostOrder(expr);

        Stack<String> calcStack = new Stack<String>();

        System.out.println(postOrderList);
        // postOrder 리스트의 값을 calcStack에 저장
        for(String s : postOrderList) {
            calcStack.push(s);
            calcPostOrder(calcStack);
        }

        postOrderList.clear();
        tokenStack.clear();
        exprStack.clear();
        // 스택의 최종 값을 반환한다
        double result = Double.parseDouble(calcStack.pop());

        System.out.println(result);
        ans = rearrange(result);
        return ans;
    }

    private void calcPostOrder(Stack<String> calcStack) {
        // 연산자가 아니면 계산을 하지 않는다
        if(!isOpcode(calcStack.lastElement())) {
            return;
        }

        if(calcStack.size() >= 2) {
            // 값을 계산하여 스택에 넣는다
            calcStack.push(calculateByOpCode(calcStack));
        }
    }

    private String calculateByOpCode(Stack<String> calculatorStack) {
        char opcode = calculatorStack.pop().charAt(0);
        double s2 = Double.parseDouble(calculatorStack.pop());
        double s1;

        String rs="";
        switch (opcode)
        {
            case '+':
                s1 = Double.parseDouble(calculatorStack.pop());
                rs = String.valueOf(s1 + s2);
                break;
            case '-':
                if(!calculatorStack.isEmpty()) {
                    s1 = Double.parseDouble(calculatorStack.pop());
                    rs = String.valueOf(s1 - s2);
                }
                else{
                    rs = String.valueOf(-1*s2);
                }
                break;
            case '×':
                s1 = Double.parseDouble(calculatorStack.pop());
                rs = String.valueOf(s1 * s2);
                break;
            case '÷':
                s1 = Double.parseDouble(calculatorStack.pop());
                rs = String.valueOf(s1 / s2);
                break;
            case '%':
                s1 = Double.parseDouble(calculatorStack.pop());
                rs = String.valueOf(s1%s2);
                break;
            case '!':
                rs = String.valueOf(factorial((int) s2));
                break;
            case 'l':
                if(!calculatorStack.isEmpty()) {
                    s1 = Double.parseDouble(calculatorStack.pop());
                    rs = String.valueOf(s1*log10(s2));
                }
                else {
                    rs = String.valueOf(log10(s2));
                }
                break;
            case 'n':
                if(!calculatorStack.isEmpty()) {
                    s1 = Double.parseDouble(calculatorStack.pop());
                    rs=String.valueOf(s1* log(s2));
                }
                else {
                    rs = String.valueOf(log(s2));
                }
                break;
            case 'C':
                s1 = Double.parseDouble(calculatorStack.pop());
                rs = String.valueOf(combination((int)s1,(int)s2));
                break;
            case 'P':
                s1 = Double.parseDouble(calculatorStack.pop());
                rs = String.valueOf(permutation((int)s1,(int)s2));
                break;
            case 's':
                if(!calculatorStack.isEmpty()) {
                    s1 = Double.parseDouble(calculatorStack.pop());
                    rs = String.valueOf(s1 * sin(s2));
                }
                else{
                    rs=String.valueOf(sin(s2));
                }
                break;
            case 'c':
                if(!calculatorStack.isEmpty()) {
                    s1 = Double.parseDouble(calculatorStack.pop());
                    rs = String.valueOf(s1 * cos(s2));
                }
                else{
                    rs=String.valueOf(cos(s2));
                }
                break;
            case 't':
                if(!calculatorStack.isEmpty()) {
                    s1 = Double.parseDouble(calculatorStack.pop());
                    rs = String.valueOf(s1 * tan(s2));
                }
                else{
                    rs=String.valueOf(tan(s2));
                }
                break;
            case '^':
                s1 = Double.parseDouble(calculatorStack.pop());
                rs = String.valueOf(pow(s1,s2));
                break;
            case '$':
                rs = String.valueOf(pow(s2,2));
                break;
            case 'b':
                rs = String.valueOf(pow(s2,-1));
                break;
            case 'r':
                if(!calculatorStack.isEmpty()) {
                    s1 = Double.parseDouble(calculatorStack.pop());
                    rs = String.valueOf(s1 * sqrt(s2));
                }
                else{
                    rs=String.valueOf(sqrt(s2));
                }
                break;
            case 'p':
                if(!calculatorStack.isEmpty()) {
                    s1 = Double.parseDouble(calculatorStack.pop());
                    rs = String.valueOf(s1 * pow(10,s2));
                }
                else{
                    //Toast.makeText(.this, "수식이 정확한지 확인하세요", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return rs;
    }

/*
    public String compute(String content)//계산
    {
        char[] operationCode = {'+', '-', '×', '÷','%', '(', ')','l','!','C','P','=', 's', 'c', 't', '^', '$', 'b', 'n' ,'r' ,'p'}; //연산 부호

        ArrayList<String> postfixList = new ArrayList<String>(); //후위표기법으로 변환 후 저장 할 ArrayList
        Stack<Character> opStack = new Stack<Character>(); // 연산 부호 우선순위처리 하며 후위 표기법으로 변경하는 Stack
        Stack<String> calculatorStack = new Stack<String>(); //후위 표기법을 계산하는 Stack

        int index = 0;//content.substring() 인수
        content = changeContent(content);

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
                            if(!calculatorStack.isEmpty()) {
                                s1 = Double.parseDouble(calculatorStack.pop());
                                rs = String.valueOf(s1 - s2);
                                calculatorStack.push(rs);
                            }
                            else{
                                calculatorStack.push(String.valueOf(-1*s2));
                            }
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
                        case '%':
                            s1 = Double.parseDouble(calculatorStack.pop());
                            rs = String.valueOf(s1%s2);
                            calculatorStack.push(rs);
                            break;
                        case '!':
                            rs = String.valueOf(factorial((int) s2));
                            calculatorStack.push(rs);
                            break;
                        case 'l':
                            if(!calculatorStack.isEmpty()) {
                                s1 = Double.parseDouble(calculatorStack.pop());
                                rs = String.valueOf(s1*log10(s2));
                                calculatorStack.push(rs);
                            }
                            else {
                                rs = String.valueOf(log10(s2));
                                calculatorStack.push(rs);
                            }
                            break;
                        case 'n':
                            if(!calculatorStack.isEmpty()) {
                                s1 = Double.parseDouble(calculatorStack.pop());
                                rs=String.valueOf(s1* log(s2));

                                calculatorStack.push(rs);
                            }
                            else {
                                rs = String.valueOf(log(s2));
                                calculatorStack.push(rs);
                            }
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
                        case 's':
                            if(!calculatorStack.isEmpty()) {
                                s1 = Double.parseDouble(calculatorStack.pop());
                                rs = String.valueOf(s1 * sin(s2));
                                calculatorStack.push(rs);
                            }
                            else{
                                rs=String.valueOf(sin(s2));
                                calculatorStack.push(rs);
                            }
                            break;
                        case 'c':
                            if(!calculatorStack.isEmpty()) {
                                s1 = Double.parseDouble(calculatorStack.pop());
                                rs = String.valueOf(s1 * cos(s2));
                                calculatorStack.push(rs);
                            }
                            else{
                                rs=String.valueOf(cos(s2));
                                calculatorStack.push(rs);
                            }
                            break;
                        case 't':
                            if(!calculatorStack.isEmpty()) {
                                s1 = Double.parseDouble(calculatorStack.pop());
                                rs = String.valueOf(s1 * tan(s2));
                                calculatorStack.push(rs);
                            }
                            else{
                                rs=String.valueOf(tan(s2));
                                calculatorStack.push(rs);
                            }
                            break;
                        case '^':
                            s1 = Double.parseDouble(calculatorStack.pop());
                            rs = String.valueOf(pow(s1,s2));
                            calculatorStack.push(rs);
                            break;
                        case '$':
                            rs = String.valueOf(pow(s2,2));
                            calculatorStack.push(rs);
                            break;
                        case 'b':
                            rs = String.valueOf(pow(s2,-1));
                            calculatorStack.push(rs);
                            break;
                        case 'r':
                            if(!calculatorStack.isEmpty()) {
                                s1 = Double.parseDouble(calculatorStack.pop());
                                rs = String.valueOf(s1 * sqrt(s2));
                                calculatorStack.push(rs);
                            }
                            else{
                                rs=String.valueOf(sqrt(s2));
                                calculatorStack.push(rs);
                            }
                            break;
                        case 'p':
                            if(!calculatorStack.isEmpty()) {
                                s1 = Double.parseDouble(calculatorStack.pop());
                                rs = String.valueOf(s1 * pow(10,s2));
                                calculatorStack.push(rs);
                            }
                            else{
                                //Toast.makeText(.this, "수식이 정확한지 확인하세요", Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                }
            }
        }

        double re = Double.parseDouble(calculatorStack.peek()); //Stack Top 데이터

        ans = rearrange(re);
        return ans;
    }
    */
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

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    public String getZ() {
        return Z;
    }

    public void setZ(String z) {
        Z = z;
    }
}
