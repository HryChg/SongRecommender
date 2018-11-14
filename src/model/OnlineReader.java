package model;

import Observer.Observer;

public class OnlineReader implements Observer{

    @Override
    public void update(){
        System.out.println("Just saw the string \"210\"");
        System.out.println("Oh boy. Can't wait for the finals!");
    }




}
