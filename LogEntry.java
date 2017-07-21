//package main.java;

import javafx.collections.transformation.SortedList;

import java.net.URLEncoder;
import java.util.*;
import java.lang.reflect.Field;

/**
    PTM postData.toString()
    players[2][username]=blackflight4&
    players[2][name]=Steven Black&
    players[2][win]=&
    players[2][score]=88&
    players[2][color]=blue&
    players[2][position]=3&

    players[3][position]=2&
    players[3][color]=green&
    players[3][name]=Dan Eisenhauer&
    players[3][username]=Salmonax&
    players[3][score]=44&

    players[1][score]=99&
    players[1][color]=red&
    players[1][win]=1&
    players[1][position]=1&
    players[1][username]=ptm_junk&
    players[1][name]=Paul Mason&

    ajax=1&
    action=save&
    quantity=1&
    version=2&
    objecttype=thing&

    incomplete=1&
    playdate=2016-01-24&
    length=300&
    playid=&
    location=PTMLocation&
    objectid=1115
*/
public class LogEntry {
    public List<PlayerInfo> players = new ArrayList<PlayerInfo>();
    public String playid = null;
    //gameid
    public String objectid= null;
    public String gamename;
    public String playdate= null;
    public String location= null;
    public String length= null;
    public String incomplete= null;
    public String comments= null;
    public String nowinstats= null;
    public String quantity=null;
    //helperMethod to speed up search
    public List<String> usernames = new ArrayList<String>();
    public List<String> names = new ArrayList<String>();
    //when an entry is merged this is what changed.
    public  String notes= new String();
    public String toString(){
        String playInfo="------LogPlay--------"+
                        "playId:     "+playid+ "\n"+
                        "objectid:     "+objectid+ "\n"+
                        "gameName    "+gamename+ "\n"+
                        "playDate:   "+playdate+ "\n"+
                        "Location    "+location+ "\n"+
                        "Length      "+length+ "\n"+
                        "incomplete  "+incomplete+ "\n"+
                        "comments    "+comments+ "\n"+
                        "nowinstats  "+nowinstats+"\n"+
                        "notes       "+notes+"\n"+
                        "quantity    "+quantity+"\n"+
                        "-PLAYERS-\n";

        if( players !=null ){
            for (PlayerInfo currentInfo : players) {
                playInfo += currentInfo.toString();
            }
        }

        return playInfo;
    }

    public boolean hasUser(UserInformation ui ){
        if( ( !Utils.isEmpty(ui.displayName )  && names.contains(ui.displayName)) ||
            ( !Utils.isEmpty(ui.username) && usernames.contains(ui.username) )) {
           return true;
        }
        return false;
    }

    public void addPlayer( PlayerInfo newPlayer){
        players.add(newPlayer);
        if( !Utils.isEmpty(newPlayer.username)){
            usernames.add(newPlayer.username);
        }else if( !Utils.isEmpty(newPlayer.name)){
            names.add(newPlayer.name);
        }

    }
    public void addValue(String variableName, String value) throws Exception {
        try {
//            System.out.println("variableName"+ variableName + " value:" +value);
            if( variableName.contains("players")){
                StringTokenizer st = new StringTokenizer(variableName,"[");
                String number =null;
                String element =null;
                int numberInt=0;
                if( st.hasMoreElements() ){
                    //skip the first element it is just player
                    st.nextToken();
                }
                if( st.hasMoreElements() ){
                    //need to remove ]
                    number = st.nextToken();
                    numberInt = new Integer( number.substring(0,number.length()-1)).intValue();
                }
                if( st.hasMoreElements() ){
                    element = st.nextToken();
                    element = element.substring(0,element.length()-1);
                }
                //System.out.println("element"+element);

                if( element.equalsIgnoreCase("username")){
                    if( !usernames.contains(value)) {
                        usernames.add(value);
                    }
                }else if( element.equalsIgnoreCase("name")){
                    //System.out.println("Adding Name:"+ value);
                    if( !names.contains(value)) {
                        names.add(value);
                    }
                }
                //create a new player if we need to.
                while( players.size() < numberInt){
                    players.add(new PlayerInfo());
                }
                //add player information
                players.get((numberInt-1)).addValue(element,value);
            }else {
                //not at player, just add information
                if( value != null || !value.equalsIgnoreCase("null")) {
                    Field f = this.getClass().getDeclaredField(variableName);
                    if( f.get(this)==null) {
                        f.set(this, value);
                    }else{
                     //   System.out.println("PTM value:"+f.get(this));
                    }
                }
                else{
                    System.out.println("PTM skipping:"+variableName+" value:"+value);
                }
            }
        }catch( Exception e ){
           System.out.println("unable set variableName: " + variableName + " value: " + value + " message: " + e.getMessage());
        }
    }

    public String encodePlay() throws Exception {
        StringBuilder postData = new StringBuilder();
        Field[] allFields = this.getClass().getFields();
        for(Field currentField:allFields){
            if( currentField.get(this) != null) {

                if (currentField.getName().equalsIgnoreCase("players")) {

                } else if (currentField.getName().equalsIgnoreCase("gamename")
                    || currentField.getName().equalsIgnoreCase("usernames")
                    || currentField.getName().equalsIgnoreCase("names")
                    || currentField.getName().equalsIgnoreCase("notes")
                        ) {
//                    System.out.println("PTM skipping " + currentField.getName());
                } else {
                    postData.append('&');
                    postData.append(URLEncoder.encode(currentField.getName(), "UTF-8"));
   //             postData.append(currentField.getName());
                    postData.append('=');
                    postData.append(URLEncoder.encode((String) currentField.get(this), "UTF-8"));
  //              postData.append((String) currentField.get(this));
                }
            }
        }
        for( int i = 0; i <players.size();i++ ){
            postData.append(players.get(i).encodePlay(i + 1));
        }
        return postData.toString();
    }

    //ui will filter only plays that have this user information
    public String csvWrite(UserInformation ui){
        String cn = new String();
        for( String currentName:names){
          cn+= currentName;
        }
//        System.out.println("CurrentNames: "+cn);
        if( ui != null ) {
//            System.out.println("displayName:" + ui.displayName);
        }
        if( ui==null || names.contains(ui.displayName) || usernames.contains(ui.username) ) {
            StringBuffer sb = new StringBuffer();
            sb.append(playid == null ? "," : playid + ",");
            sb.append(objectid == null ? "," : objectid + ",");
            sb.append(gamename == null ? "," : "\""+BGGUpload.escapeGameName(gamename) + "\",");
            sb.append(playdate == null ? "," : playdate + ",");
            sb.append(location == null ? "," : location + ",");
            sb.append(length == null ? "," : length + ",");
            sb.append(incomplete == null ? "," : incomplete + ",");
            sb.append(comments == null ? "," : comments + ",");
            sb.append(nowinstats == null ? "," : nowinstats + ",");
            sb.append(notes== null ? "," : notes+ ",");
            for (int i = 0; i < players.size(); i++) {
                sb.append(players.get(i).csvWrite(i + 1));
            }

            return sb.toString();

        }
        else{
            return null;
        }
    }

}
