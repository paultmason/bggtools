//package main.java;
//import main.java.*;
/**
 * Created by ptm on 1/27/2016.
 */
public class UserInformation {
    public String username;
    public String displayName;
    public UserInformation(String username, String displayName){
        this.username=username;
        this.displayName=displayName;
    }
    public boolean equals( UserInformation compare){
        return (((username == null && compare.username==null) || username.equalsIgnoreCase(compare.username))
                && ((displayName == null && compare.displayName==null) || displayName.equalsIgnoreCase(compare.displayName)) );
    }
    public String toString(){
        return "username: "+username+" displayName: "+displayName;
    }

    //should only compare not null
    public boolean partialMatch( UserInformation compare ){
        boolean returnValue = false;
        if( Utils.isEmpty(this.username) && Utils.isEmpty(compare.username)) {
           //skip compare if both are null.
        }else{
          returnValue = (this.username != null && this.username.equalsIgnoreCase(compare.username));

        }
        if( Utils.isEmpty(this.displayName) && Utils.isEmpty(compare.displayName)) {
            //skip compare if both are null.
        }else{
            returnValue = returnValue || (this.displayName!= null && this.displayName.equalsIgnoreCase(compare.displayName));

        }

        return returnValue;
    }
}
