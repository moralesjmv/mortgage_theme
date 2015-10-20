package com.murach.tipcalculator;

public class Mortgage {
    
    private double loanAmount;
    private double annualInterestRate;
    private double monthlyInterestRate;
    private int totalMonthsToRepay;
    private double numberOfPayments;
    private int termInMonths;
    private double downPayment;

    Mortgage(){
        loanAmount = 0;
        annualInterestRate = 0;
        monthlyInterestRate = 0;
        totalMonthsToRepay = 0;
        numberOfPayments = 0;
        termInMonths = 0;
        downPayment = 0;
    }
    Mortgage(double loanAmount, double annualInterestRate){
        setLoanAmount(loanAmount);
        setAnnualInterestRate(annualInterestRate);
        setMonthlyInterestRate(annualInterestRate/12);
    }

    public double getTermInMonths(){
        return termInMonths;
    }

    public double getLoanAmount(){
        return loanAmount;
    }
    
    public double getAnnualInterestRate(){
        return annualInterestRate;
    }
    
    public double getMonthlyInterestRate(){
        return monthlyInterestRate;
    }
    
    public int totalMonthsToRepay(){
        return totalMonthsToRepay;
    }

    public double getNummberOfPayments(){
        return numberOfPayments;
    }
    
    public void setLoanAmount(double loanAmount){
        this.loanAmount = loanAmount;
    }
    
    public void setAnnualInterestRate(double annualInterestRate){
        this.annualInterestRate = annualInterestRate;
    }
    
    public void setMonthlyInterestRate(double monthlyInterestRate){
        this.monthlyInterestRate = monthlyInterestRate;
    }
    
    public void setTotalMonthsToRepay(int monthsToRepay){
        this.totalMonthsToRepay = monthsToRepay;
    }
    
    public void setNumberOfPayments(double numberOfPayments){
        this.numberOfPayments = numberOfPayments;
    }

    public void setTermInMonths(int loanTermInMonths){
        this.termInMonths = loanTermInMonths;
    }

    public void setDownPayment(double downPayment){
        this.downPayment = downPayment;
    }

    public double getDownPayment(){
        return downPayment;
    }
    
    //============END SETTERS AND GETTERS==================
    
    public double calcTerm(){
        return (Math.pow((1 + monthlyInterestRate), numberOfPayments));
    }
    
    public double monthlyPayment(){
	    return (loanAmount * monthlyInterestRate * termInMonths) / (termInMonths - 1);
    }

    public double JoseMethod(){
        double initialCost = loanAmount - downPayment;
        return ((monthlyInterestRate) * initialCost * Math.pow((1+monthlyInterestRate),termInMonths)) / ((Math.pow((1+monthlyInterestRate),termInMonths)) - 1);
    }
    
}
