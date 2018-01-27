package com.example.brady.bpomerle_subbook;

import java.util.ArrayList;

/**
 * Created by Brady on 2018-01-26.
 */

public class AccountBook {
    private ArrayList<Subscription> subList;

    public AccountBook(){
        subList = new ArrayList<>();
    }
    public AccountBook(Subscription sub){
        subList = new ArrayList<>();
        subList.add(sub);
    }
}
