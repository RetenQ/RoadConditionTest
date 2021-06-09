package com.example.cs183;

public class Hole {
    //创建一个代表坑洞的类
    private static int staticHoleNum =  0 ; //坑洞的个数

    private  boolean isnull = true ; //判断是否是空

    private int rank ; //坑洞的等级，进而用来测量路面平整度   0是默认状态，随后从1-4增加等级
    private long time ; //穿过该洞的时间
    private float value ; //坑洞前后的加速度差值
    private double Latitude ; //经度
    private double Longitude ; //纬度

    public  Hole(){
        //默认构造器，不管怎么样先HoleNum++
        staticHoleNum++ ;
        this.isnull = false ;
        this.rank = 0 ;
        this.time = 0 ;
        this.value = 0 ;
        this.Latitude = 0 ;
        this.Longitude = 0 ;
    }

    //第一种构造方法，传入时间和差值
    public  Hole(long time , float value){
        staticHoleNum++ ;
        this.isnull = false ;
        this.rank = 0 ;
        this.time = time ;
        this.value = value ;
        this.Latitude = 0 ;
        this.Longitude = 0 ;
    }

    //第二种构造方法，传入等级，时间，差值
    public  Hole(int rank ,long time , float value){
        staticHoleNum++ ;
        this.isnull = false ;
        this.rank = rank ;
        this.time = time ;
        this.value = value ;
        this.Latitude = 0 ;
        this.Longitude = 0 ;
    }

    //第三种，也是我们存储用的，传入所有我们需要的数据
    public Hole( long time , float value , double Latitude , double Longitude){
        staticHoleNum++ ;
        this.isnull = false ;
        this.rank = 0 ;
        this.time = time ;
        this.value = value ;
        this.Latitude = Latitude ;
        this.Longitude = Longitude ;

    }


    //Getter------------------------------------------
    public  boolean getIsNull(){
        return  this.isnull ;
    }

    public int getHoleNum(){
        return this.staticHoleNum;
    }

    public  int getRank(){
        return  this.rank ;
    }

    public long getTime(){
        return this.time ;
    }

    public float getValue(){
        return this.value ;
    }

    public double getLatitude(){
        return this.Latitude ;
    }

    public double getLongitude(){
        return this.Longitude ;
    }

    //Setter------------------------------------------
    public  void  setHoleNum(int HoleNum){
        this.staticHoleNum = HoleNum ;
    }

    public  void  setRank(int rank){
        this.rank = rank ;
    }

    public  void  setTime(long time){
        this.time = time ;
    }

    public  void setValue(float value){
        this.value = value ;
    }

    public void setLatitude(double Latitude){
        this.Latitude = Latitude ;
    }

    public void setLongitude(double Longitude){
        this.Longitude = Longitude  ;
    }

    //Function-----------------------------------------
    public  void  RankHole(){
        //评估坑洞等级
        if(this.value > 0 && this.value < 1.5){
            this.rank = 1 ;
        }else if(this.value >= 1.5 && this.value < 2.5 ){
            this.rank = 2 ;
        }else if(this.value >= 2.5 && this.value <4){
            this.rank = 3 ;
        }else if(this.value >=4){
            this.rank = 4 ;
        }
    }

}
