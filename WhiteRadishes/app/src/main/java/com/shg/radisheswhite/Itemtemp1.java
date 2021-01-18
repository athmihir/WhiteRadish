package com.shg.radisheswhite;

public class Itemtemp1 {
    String pname="";
    String qtyval="";
    String priceval="";
    String pid="";

    public Itemtemp1() {

    }

    //         Constructor that is used to create an instance of the Movie object
    public Itemtemp1(String pname, String priceval, String qtyval, String pid) {
        this.pname = pname;
        this.qtyval = qtyval;
        this.priceval = priceval;
        this.pid = pid;

    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getQtyval() {
        return qtyval;
    }

    public String getPid() { return pid; }

    public void setQtyval(String qtyval) {
        this.qtyval = qtyval;
    }

    public String getPriceval() {
        return priceval;
    }

    public void setPriceval(String priceval) {
        this.priceval = priceval;
    }
}
