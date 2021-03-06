package com.hb.mcfdebugger;

public class SimpleCmdObj {
    public String funNamespace;
    public String funPath;
    public Integer cmdIndex;
    public SimpleCmdObj(String funNamespace, String funPath, Integer cmdIndex){
        this.funNamespace=funNamespace;
        this.funPath=funPath;
        this.cmdIndex=cmdIndex;
    }
    public SimpleCmdObj(){
        this.funNamespace="";
        this.funPath="";
        this.cmdIndex=0;
    }
    public void clear(){
        this.funNamespace="";
        this.funPath="";
        this.cmdIndex=0;
    }
    public boolean equals(SimpleCmdObj compareObj){
        return (this.cmdIndex==compareObj.cmdIndex&&this.funPath.equals(compareObj.funPath)&&this.funNamespace.equals(compareObj.funNamespace));
    }
    public boolean isNext(SimpleCmdObj compareObj){
        return (this.cmdIndex==compareObj.cmdIndex+1&&this.funPath.equals(compareObj.funPath)&&this.funNamespace.equals(compareObj.funNamespace));
    }
}