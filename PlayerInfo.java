//package main.java;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 players[2][username]=blackflight4&
 players[2][name]=Steven Black&
 players[2][win]=&
 players[2][score]=88&
 players[2][color]=blue&
 players[2][position]=3&
 * Simple Structure to take player information
 */
public class PlayerInfo {
    public String username;
    public String name;
    public String position;
    public String color;
    public String score;
    public String win;
    public String rating;
  //  public String new;
//    public String new;
    private UserInformation userInformation;

    public UserInformation getUserInformation(){
        if( userInformation== null){
            this.userInformation = new UserInformation(username, name);
        }
        return this.userInformation;
    }


    public String toString(){
        String playInfo="-Player-\n"+
                "username:       "+username+ "\n"+
                "name:           "+name+ "\n"+
                "position       "+position+ "\n"+
                "color:          "+color+ "\n"+
                "score           "+score+ "\n"+
                "win             "+win+ "\n";
        return playInfo;
    }
    public void addValue(String variableName, String value) throws Exception {
        try {
            if( variableName.equalsIgnoreCase("new")){
                //skip
            }
            else if( value != null || !value.equalsIgnoreCase("null")) {
                Field f = this.getClass().getDeclaredField(variableName);
//                f.set(this, value);
//                 if( f.get(this)==null ) {
                if( f.get(this)==null || (f.get(this).toString().length()==0) ) {
                    f.set(this, value);
                }else{
             //       System.out.println("PTM player valuenull:"+(f.get(this)==null) + " variableName:"+variableName);
             //       System.out.println("PTM player value length:"+f.get(this).toString().length() + " variableName:"+variableName);
             //       System.out.println("PTM player value:"+f.get(this) + " variableName:"+variableName);
                }

            }
            else{
                System.out.println("PTM skipping:"+variableName+" value:"+value);
            }
        }catch( Exception e ){
            System.out.println("unable set variableName: " + variableName + " value: " + value + " message: " + e.getMessage());
        }
    }
    public String encodePlay(int playerNumber) throws Exception{
        StringBuilder postData = new StringBuilder();
        Field[] allFields = this.getClass().getFields();
        for(int i = 0; i<allFields.length;i++){
            try {
                Field currentField = allFields[i];
                if( currentField.get(this) != null) {
                    postData.append('&');
                    postData.append(URLEncoder.encode("players[" + (playerNumber) + "][" + currentField.getName() + "]", "UTF-8"));
 //               postData.append("players[" + (playerNumber) + "][" + currentField.getName() + "]");
                    postData.append('=');
                    postData.append(URLEncoder.encode((String) currentField.get(this), "UTF-8"));
  //              postData.append((String) currentField.get(this));
                }
            }catch( Exception e ){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return postData.toString();
    }

    public String csvWrite( int playerNumber){
        StringBuffer sb = new StringBuffer();
        sb.append(username==null?",":username+",");
        sb.append(name==null?",":name+",");
        sb.append(position==null?",":position+",");
        sb.append(color==null?",":color+",");
        sb.append(score==null?",":score+",");
        sb.append(win==null?",":win+",");
        sb.append(rating==null?",":rating+",");

        return sb.toString();
    }


}
