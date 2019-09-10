package com.example.version5;

public class ListItem {
    private int imageId;
    private String itemTitle;
    private String itemText;

    public ListItem(int imageId, String itemTitle, String itemText){
        this.imageId = imageId;
        this.itemTitle = itemTitle;
        this.itemText = itemText;
    }

    //for adapter 
    public int getImageId(){
        return imageId;
    }

    public String getItemTitle(){
        return itemTitle;
    }

    public String getItemText(){
        return itemText;
    }
}
