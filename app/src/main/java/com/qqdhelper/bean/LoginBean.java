package com.qqdhelper.bean;

import android.os.Parcel;

/**
 * Created by sdash on 2016/1/13.
 */
public class LoginBean extends BaseBean {

    //public static final Parcelable.Creator<LoginBean> CREATOR = new LoginBean.1();
    private String a;
    private int b;
    private String c;
    private int d;
    private String e;
    private String f;
    private int g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String l;
    private String m;
    private String n;
    private String o;

    public LoginBean() {}

    protected LoginBean(Parcel paramParcel)
    {
        super(paramParcel);
        this.a = paramParcel.readString();
        this.c = paramParcel.readString();
        this.e = paramParcel.readString();
        this.f = paramParcel.readString();
        this.h = paramParcel.readString();
        this.i = paramParcel.readString();
        this.j = paramParcel.readString();
        this.k = paramParcel.readString();
        this.l = paramParcel.readString();
        this.m = paramParcel.readString();
        this.n = paramParcel.readString();
        this.o = paramParcel.readString();
        this.b = paramParcel.readInt();
        this.d = paramParcel.readInt();
        this.g = paramParcel.readInt();
    }

    public int describeContents()
    {
        return 0;
    }

    public String getA()
    {
        return this.a;
    }

    public int getB()
    {
        return this.b;
    }

    public String getC()
    {
        return this.c;
    }

    public int getD()
    {
        return this.d;
    }

    public String getE()
    {
        return this.e;
    }

    public String getF()
    {
        return this.f;
    }

    public int getG()
    {
        return this.g;
    }

    public String getH()
    {
        return this.h;
    }

    public String getI()
    {
        return this.i;
    }

    public String getJ()
    {
        return this.j;
    }

    public String getK()
    {
        return this.k;
    }

    public String getL()
    {
        return this.l;
    }

    public String getM()
    {
        return this.m;
    }

    public String getN()
    {
        return this.n;
    }

    public String getO()
    {
        return this.o;
    }

    public void setA(String paramString)
    {
        this.a = paramString;
    }

    public void setB(int paramInt)
    {
        this.b = paramInt;
    }

    public void setC(String paramString)
    {
        this.c = paramString;
    }

    public void setD(int paramInt)
    {
        this.d = paramInt;
    }

    public void setE(String paramString)
    {
        this.e = paramString;
    }

    public void setF(String paramString)
    {
        this.f = paramString;
    }

    public void setG(int paramInt)
    {
        this.g = paramInt;
    }

    public void setH(String paramString)
    {
        this.h = paramString;
    }

    public void setI(String paramString)
    {
        this.i = paramString;
    }

    public void setJ(String paramString)
    {
        this.j = paramString;
    }

    public void setK(String paramString)
    {
        this.k = paramString;
    }

    public void setL(String paramString)
    {
        this.l = paramString;
    }

    public void setM(String paramString)
    {
        this.m = paramString;
    }

    public void setN(String paramString)
    {
        this.n = paramString;
    }

    public void setO(String paramString)
    {
        this.o = paramString;
    }

    public String toString()
    {
        return "LoginBean{a='" + this.a + '\'' + ", c='" + this.c + '\'' + ", e='" + this.e + '\'' + ", f='" + this.f + '\'' + ", h='" + this.h + '\'' + ", i='" + this.i + '\'' + ", j='" + this.j + '\'' + ", k='" + this.k + '\'' + ", l='" + this.l + '\'' + ", m='" + this.m + '\'' + ", n='" + this.n + '\'' + ", o='" + this.o + '\'' + ", b=" + this.b + ", d=" + this.d + ", g=" + this.g + "} " + super.toString();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
        super.writeToParcel(paramParcel, paramInt);
        paramParcel.writeString(this.a);
        paramParcel.writeString(this.c);
        paramParcel.writeString(this.e);
        paramParcel.writeString(this.f);
        paramParcel.writeString(this.h);
        paramParcel.writeString(this.i);
        paramParcel.writeString(this.j);
        paramParcel.writeString(this.k);
        paramParcel.writeString(this.l);
        paramParcel.writeString(this.m);
        paramParcel.writeString(this.n);
        paramParcel.writeString(this.o);
        paramParcel.writeInt(this.b);
        paramParcel.writeInt(this.d);
        paramParcel.writeInt(this.g);
    }
}
