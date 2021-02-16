package com.hb.mcfdebugger;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SendCmdObj{
    public String funNamespace;
    public String funPath;
    public String cmdContent;
    public Integer cmdIndex;
    public boolean pause;
    public String exception="";
    public Map<String,String> source=new HashMap<String,String>();
    public SendCmdObj(String funNamespace, String funPath, Integer cmdIndex, String cmdContent, Boolean pause, @Nullable Map<String,String> source){
        this.funNamespace=funNamespace;
        this.cmdContent=cmdContent;
        this.funPath=funPath;
        this.cmdIndex=cmdIndex;
        this.pause=pause;
        if(source!=null){this.source=source;}
    }
}