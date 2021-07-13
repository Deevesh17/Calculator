package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var resultString : String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        one.setOnClickListener(){
            resultString += one.text
            result.setText(resultString)
        }
        two.setOnClickListener{
            resultString += two.text
            result.setText(resultString)
        }
        three.setOnClickListener{
            resultString += three.text
            result.setText(resultString)
        }
        four.setOnClickListener{
            resultString += four.text
            result.setText(resultString)
        }
        five.setOnClickListener{
            resultString += five.text
            result.setText(resultString)
        }
        six.setOnClickListener{
            resultString += six.text
            result.setText(resultString)
        }
        seven.setOnClickListener{
            resultString += seven.text
            result.setText(resultString)
        }
        eight.setOnClickListener{
            resultString += eight.text
            result.setText(resultString)
        }
        nine.setOnClickListener{
            resultString += nine.text
            result.setText(resultString)
        }
        zero.setOnClickListener{
            resultString += zero.text
            result.setText(resultString)
        }
        zerozero.setOnClickListener{
            resultString += zerozero.text
            result.setText(resultString)
        }
        pluse.setOnClickListener{
            resultString = result.text.toString()
            if(resultString.length == 0){
                resultString = "0"
            }
            if(checkOperator(resultString[resultString.length - 1])){
                resultString += pluse.text
                result.setText(resultString)
            }
        }
        minus.setOnClickListener{
            resultString = result.text.toString()
            if(resultString.length == 0){
                resultString = "0"
            }
            if(checkOperator(resultString[resultString.length - 1])) {
                resultString += minus.text
                result.setText(resultString)
            }
        }
        divide.setOnClickListener{
            resultString = result.text.toString()
            if(resultString.length == 0){
                resultString = "0"
            }
            if(checkOperator(resultString[resultString.length - 1])) {
                resultString += divide.text
                result.setText(resultString)
            }
        }
        multiply.setOnClickListener{
            resultString = result.text.toString()
            if(resultString.length == 0){
                resultString = "0"
            }
            if(checkOperator(resultString[resultString.length - 1])) {
                resultString += "*"
                result.setText(resultString)
            }
        }
        modulu.setOnClickListener{
            resultString = result.text.toString()
            if(resultString.length > 0) {
                if (checkOperator(resultString[resultString.length - 1])) {
                    if (checkDigitAndDot(result.text.toString())) {
                        val expr = resultString + modulu.text.toString()
                        result.setText((resultString.toDouble() * 0.01).toString())
                        expression?.setText(expr)
                        resultString = result.text.toString()
                    } else {
                        val modluValue = checkModlu(resultString)
                        resultString = resultString.dropLast(modluValue.length)
                        resultString += (modluValue.toDouble() * 0.01)
                        result.setText(resultString)
                        resultString = result.text.toString()
                    }
                }
            }
            else{
                result.setText("0.0")
            }
        }
        acbtn.setOnClickListener{
            result.setText("0")
            expression?.setText("")
            resultString = ""
        }
        equalto.setOnClickListener{
            val resultexpression = result.text.toString()
            if(resultexpression.length > 0 ){
                if(checkOperator(resultexpression[resultexpression.length - 1]))
                {
                    expression?.setText(resultexpression)
                    result.setText(calculateExpression(resultexpression))
                    resultString = ""
                }
            }
        }
        dot.setOnClickListener{
            resultString += dot.text
            result.setText(resultString)
        }
        backspace.setOnClickListener{
            resultString = resultString.dropLast(1)
            result.setText(resultString)
        }
        backspace.setOnLongClickListener{
            result.setText("0")
            resultString = ""
            true
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("result",result.text.toString())
        outState.putString("expression",expression?.text.toString())

    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null){
            result.setText(savedInstanceState.getString("result").toString())
            expression?.setText(savedInstanceState.getString("expression").toString())
        }
    }
    fun checkOperator(operator: Char) : Boolean{
        if (operator.isDigit())
            return true
        return false
    }
    fun checkDigitAndDot(resultExpression : String):Boolean{
        for (token in resultExpression ){
            if ( !(token.isDigit() || token == '.' )){
                return false
            }
        }
        return true
    }
    fun checkModlu(resultExpression : String):String{
        val resultExpression = resultExpression.reversed()
        var number = ""
        for (token in resultExpression ){
            if (token.isDigit() || token == '.' ){
                number += token
            }
            else{
                return number.reversed()
            }
        }
        return ""
    }
    fun calculateExpression(expression : String): String{
        val resultexpression = expression.toCharArray()
        val numaricValue = Stack<Double>()
        val operator = Stack<String>()
        var number = ""
        val count = expression.length
        var tokenCount = 1
        var symbolCount = 0
        for( token in resultexpression){
            if (token.isDigit() || token == '.' ){
                number += token
            }
            if((!token.isDigit() && token != '.') || tokenCount == count){
                symbolCount = 1
                numaricValue.push(number.toDouble())
                if(!token.isDigit()){
                    if(!operator.empty()){
                        while(!operator.empty() && precidence(token,operator.peek()))
                        {
                            numaricValue.push(calculate(operator.pop(),numaricValue.pop(),numaricValue.pop()))
                        }
                    }
                    operator.push(token.toString())
                }
                number = ""
            }
            tokenCount++
        }
        if(symbolCount == 0)
        {
            resultString = numaricValue.pop().toString()
        }
        else if(!operator.empty() && !numaricValue.empty()){
            while(!operator.empty()) {
                numaricValue.push(calculate(operator.pop(),numaricValue.pop(),numaricValue.pop()))
            }
            if(!numaricValue.empty()){
                resultString = numaricValue.pop().toString()
            }
        }
        return resultString
    }
    fun precidence(newToken:Char , peekValue :String) : Boolean{
        if ((newToken == '*' || newToken == '/') && (peekValue == "+" || peekValue == "-"))
            return false
        else
            return true
    }
    fun calculate(operator : String,number2 : Double,number1 : Double) : Double{
        when(operator){
            "+" -> return (number1 + number2)
            "-" -> return (number1 - number2)
            "/" ->
                if (number2 != 0.0 ) {
                    return (number1 / number2)
                }
            "*" -> return (number1 * number2)
            else -> return (0.0)
        }
        return (0.0)
    }
}

