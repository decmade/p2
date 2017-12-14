var last_click = "frac";
function CalcParlay(form)
{
	      var t = new Array(7);
	      var f = new Array(7);
          var ParlayPayout = 1;

	 if (form.unit.value == "") {alert("Enter a wager.");return;}
	if (check(form.team0) && check(form.team1) &&
	    check(form.team2) && check(form.team3) &&
	    check(form.team4) && check(form.team5) &&
	    check(form.team6) && check(form.unit) )
	   {
	      t[0] = parseFloat(form.team0.value);
      	  t[1] = parseFloat(form.team1.value);
	      t[2] = parseFloat(form.team2.value);
	      t[3] = parseFloat(form.team3.value);
  	      t[4] = parseFloat(form.team4.value);
	      t[5] = parseFloat(form.team5.value);
	      t[6] = parseFloat(form.team6.value);
	      

	      var Bet = parseFloat(form.unit.value);

              for (var i = 0; i < 7; i++)
              {
	            if (t[i] < 0) { 	 f[i] = (-t[i] + 100) / -t[i];  }
		 	     else if (t[i] > 0) { 	 f[i] = ( t[i] + 100) / 100;}
	             else f[i] = 1;
                ParlayPayout *= f[i];
              }

	      ParlayPayout -= 1;
	      ParlayPayout *= Bet;
          
	      if (!isNaN(t[0])){form.factor0.value = f[0].toFixed(4);} else {form.factor0.value = "";}
	      if (!isNaN(t[1])){form.factor1.value = f[1].toFixed(4);} else {form.factor1.value = "";}
	      if (!isNaN(t[2])){form.factor2.value = f[2].toFixed(4);} else {form.factor2.value = "";}
	      if (!isNaN(t[3])){form.factor3.value = f[3].toFixed(4);} else {form.factor3.value = "";}
	      if (!isNaN(t[4])){form.factor4.value = f[4].toFixed(4);} else {form.factor4.value = "";}
	      if (!isNaN(t[5])){form.factor5.value = f[5].toFixed(4);} else {form.factor5.value = "";}
          if (!isNaN(t[6])){form.factor6.value = f[6].toFixed(4);} else {form.factor6.value = "";}
          form.payout.value = ParlayPayout.toFixed(2);
	}
	else
	{
		form.payout.value = "ERROR";
	}
}

function last_change(cl)
{
	last_click = cl;
}

function calculate(form)
{
	if (last_click== "frac")      {frac(form);}
	else if (last_click== "dec")  {decicalc (form);}
	else if (last_click== "ml")   {moneylinecalc (form);}
}

function GCD(num1,num2)
{
	var a; var b;
	if      (num1 < num2) {a = num2; b = num1;}
	else if (num1 > num2) {a = num1; b = num2;}
	else if (num1 == num2) {return num1;}
	
     while(1)
     {  // The recursive way gave a stack error
     	if (b == 0)
     	{/*alert ("Done a = "+ a);*/ return a;}
     	else
     	{
     		//alert (a + " a and b " + b);
     		var temp = b;
     		b = a % b;
     		a = temp;
     	}
     }
}



function reduce (a,b)
{   //alert ("line 62" + a + " / " + b );>
	var n  = new Array(2);
	var f = GCD(a,b);	
	//alert ("line 65" + a + " / " + b );>

	n[0] = a/f;
	n[1] = b/f;
	//alert (a + " " + b +" " + n[0] + " / " + n[1] + " GCD = " + f);
	return n;
}
function frac(form)
{
	if (isNaN(form.denominator.value)){alert("Denominator is not a whole number"); return;}
		else if (form.denominator.value == "") {return;}
	if (isNaN(form.numerator.value))  {alert("Numerator is not a whole number"); return;}
		else if (form.numerator.value == "") {return;}

	var mn;
	var dec;
	var num = parseFloat(form.numerator.value);
	var dom = parseFloat(form.denominator.value);

	if (num != Math.round(num)){alert("Numerator must be a whole number"); return;}
	if (dom != Math.round(dom)){alert("Denominator must be a whole number"); return;}

	dec = (num/dom) + 1;
    if (num < dom) 
    {mn = (dom/num) * (-1)*100;}
    else if (dom < num)
    {mn = (num/dom) * 100;}
    else 
    {mn = 100;}
    
   	a = reduce(num,dom)
   	num = a[0];
   	dom = a[1];
   	
   	form.numerator.value = num;
	form.denominator.value = dom;
	
	form.moneyline.value = mn.toFixed(2);
	form.decimalline.value = dec.toFixed(2);
	if (!isNaN(form.bet.value)){form.payout.value = (form.bet.value * (dec-1)).toFixed(2);}
	else{alert(form.bet.value);}
	if (form.payout.value == 0){form.payout.value = "";}
}

function decicalc (form)
{
	if (isNaN(form.decimalline.value))  {alert("Decimal line is not a number"); return;}
		else if (form.decimalline.value == "") {return;}

	var mn;
	var dec = parseFloat(form.decimalline.value);;
	var num = (dec-1) * 10000;
	var dom = 10000;
	
	num = Math.round(num);
	dom = Math.round(dom);
	
	var a = reduce(num,dom);
	num=a[0];
	dom=a[1];

	if (num < dom) 
    {mn = (dom/num) * (-1)*100;}
    else if (dom < num)
    {mn = (num/dom) * 100;}
    else 
    {mn = 100;}


	form.numerator.value = num;
	form.denominator.value = dom;
	form.moneyline.value = mn.toFixed(2);
	if (!isNaN(form.bet.value)){form.payout.value = (form.bet.value * (dec-1)).toFixed(2);}
	else{alert(form.bet.value);}
	if (form.payout.value == 0){form.payout.value = "";}
}

function moneylinecalc (form)
{
	if (isNaN(form.moneyline.value))  {alert("Moneyline is not a number"); return;}
		else if (form.moneyline.value == "") {return;}


	var mn = parseFloat(form.moneyline.value);;
	var dec;
	var num;
	var dom;
	
	if (mn < 0)
	{
		dom = (-1)*(mn);
		num = 100;
	}
	else if (mn > 0)
	{
		dom = 100;
		num = mn;
	}
	var a = reduce (num,dom)
	num = a[0];
	dom = a[1];
	
	dec = (num/dom) + 1;

	form.numerator.value = num;
	form.denominator.value = dom;
	form.decimalline.value = dec.toFixed(2);
	if (!isNaN(form.bet.value)){form.payout.value = (form.bet.value * (dec-1)).toFixed(2);}
	else{alert(form.bet.value);}
	if (form.payout.value == 0){form.payout.value = "";}
}
function clearParlayForm(form)
{   form.team1.value = "";
    form.team2.value = "";
    form.team3.value = "";
    form.team4.value = "";
    form.team5.value = "";
    form.team6.value = "";
    form.team0.value = "";
    
    form.factor1.value = "";
    form.factor2.value = "";
    form.factor3.value = "";
    form.factor4.value = "";
    form.factor5.value = "";
    form.factor6.value = "";
    form.factor0.value = "";
    form.payout.value = "0";

    form.unit.value = "100";
}
function clearConvertForm(form)
{   form.denominator.value = "11";
    form.numerator.value = "10";
    form.moneyline.value = "-110";
    form.decimalline.value = "1.91";
    form.bet.value = "";
    form.payout.value = "";
}
function check(infield)
{   
    if (isNaN(infield.value))
    {
    	infield.value = 'ERROR';
    	return false;
    } 
    return true;
}
