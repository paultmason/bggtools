//package main.java;

//import javax.net.ssl.HttpsURLConnection;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

//import java.util.Base64;
/**
 * http://www.sheltonsonline.net/bggtools/getplays
 */
public class BGGUpload {
    public StringBuffer logger = new StringBuffer();
    public Map<String, UserInformation> usernameInfoMap = new HashMap<String,UserInformation>();
    public static String masterFile = "mergeFile-plays.csv";
   // public static String masterFile = "plays.csv";
  //   public static String masterFile = "Hanabi.csv";

    public static String escapeGameName(String gameName){
        return gameName.replace("\"","").replace(",","");
    }
    public static Map<String,UserInformation> ptmUserInfo(){
        Map<String, UserInformation> userInfoMap = new HashMap<String,UserInformation>();
      //  userInfoMap.put("Allen", new UserInformation("a0ashle", "Allen Ashley"));
      //  userInfoMap.put("Amanda X", new UserInformation("", "Amanda"));
      //  userInfoMap.put("Ashley", new UserInformation("seussN10", "Ashley President"));
      //  userInfoMap.put("Blake", new UserInformation("", "Blake Standford"));
      //  userInfoMap.put("Carol", new UserInformation("", "Carol Wilson"));
       // userInfoMap.put("Carl (Ashley)", new UserInformation("", "Carl Alloway"));
     //   userInfoMap.put("Caitlin", new UserInformation("", "Katlyn"));
        //       userInfoMap.put("Chris", new UserInformation("", "Chris Ferraro"));
        userInfoMap.put("Chris F", new UserInformation("M0N0XIDE", "Chris Ferraro"));
      //  userInfoMap.put("Dan Eisenhauer", new UserInformation("Salmonax", "Dan Eisenhauer"));
        userInfoMap.put("David", new UserInformation("Cygnusx1", "David Whitehouse"));
        userInfoMap.put("Gene", new UserInformation("BlueFaceBeast", "Gene Winterscheidt"));
        //userInfoMap.put("Louis", new UserInformation("", "Louis"));
        //userInfoMap.put("Luis", new UserInformation("", "Louis"));

        //userInfoMap.put("Hsiu-Ping", new UserInformation("", "Hsiu-Ping"));
        //userInfoMap.put("Hsiu-ping", new UserInformation("", "Hsiu-Ping"));
        //userInfoMap.put("Conner", new UserInformation("", "Connor"));

        userInfoMap.put("Jason Kai", new UserInformation("Kaiwolf", "Jason Kai"));
        return userInfoMap;
    }
    public void populateUserInformation(){


     //   usernameInfoMap = new HashMap<String,UserInformation>();
     //   usernameInfoMap.put("rmoll",new UserInformation("rmoll", "Russ Moll"));
     //   usernameInfoMap.put("BillsBoardGames", new UserInformation("BillsBoardGames", "Bill"));
        usernameInfoMap.put("Cygnusx1", new UserInformation("Cygnusx1", "David Whitehouse"));
        usernameInfoMap.put("Kaiwolf", new UserInformation("Kaiwolf", "Jason Kai"));
      //  usernameInfoMap.put("ptm_junk", new UserInformation("ptm_junk", "Paul Mason"));
        usernameInfoMap.put("MooseyFate", new UserInformation("MooseyFate", "Mike Welsch"));
      //  usernameInfoMap.put("ChrisAbler2", new UserInformation("ChrisAbler2", "Chris Abler"));

        //usernameInfoMap.put("nazulleptra", new UserInformation("nazulleptra", "Carter Linn"));


    }

    public BGGUpload(  ){
    }

    public StringBuffer getLoggerBuffer(){return logger;}
    public String getLogger(){
        return logger.toString();
    }
    public static void main( String[] args) {
        String dir = null;
        if(args.length>=3 && args[2] != null ) {
            dir=args[2];
        }else{
            dir = "c:\\temp\\";
        }
        BGGUpload u2 = new BGGUpload();
        BufferedWriter loggerFile = null;

        try {
            String username = null;
            String password = null;
            if(args.length>=1&& args[0] != null ) {
                username=args[0];

            }else{
                username = "ptm_junk";
            }

            if(args.length>=2 && args[1] != null ) {
                password=args[1];

            }else{
                password = "junk";
            }

            String csvFile = dir+ masterFile;
            System.out.println("PTM csvFile"+csvFile+"\n");
            FileReader csvFileReader = new FileReader(csvFile);
            u2.processFile( username, password, csvFileReader);
            String logFile = u2.getLogger();
System.out.println("PTM logFileResults: "+ logFile);
            FileWriter fw = new FileWriter(dir+"uploadPlay.log");
            loggerFile = new BufferedWriter(fw);
            loggerFile.append(logFile);
            System.out.println(logFile);
            System.out.println(logFile);
        }catch( Exception e ){
            e.printStackTrace();
        }
        finally{
            if( loggerFile !=null){
                try {
                    loggerFile.close();
                }catch( Throwable t ){
                    t.printStackTrace();
                }
            }
            System.out.println("PTM DONE!");
        }
    }



    public void writeCsvFile(List<LogEntry> logEntries, String fileName, UserInformation ui) throws Exception{
    //    System.out.println("writing csv file"+fileName +" UI: " + ui);
        logger.append("writing number of rows: "+logEntries.size()+"\n");
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter logEntryFile = null;
       // CsvWriter
        logEntryFile = new BufferedWriter(fw);

        //firstcreate a header
        int maxPlayer=0;


        StringBuffer entries= new StringBuffer();
        for(LogEntry currentEntry:logEntries){
            String currentRow = currentEntry.csvWrite(ui);

            if( currentRow != null) {
                entries.append(currentRow+"\n");
            }
            maxPlayer = Math.max(maxPlayer,currentEntry.players.size());
        }

        logEntryFile.append("playid,objectid,gamename,playdate,location,length,incomplete,comments,nowinstats,notes");
        for( int i = 0; i < maxPlayer;i++){
            logEntryFile.append(",players["+(i+1)+"][username],players["+(i+1)+"][name],players["+(i+1)+"][position]");
            logEntryFile.append(",players["+(i+1)+"][color],players["+(i+1)+"][score],players["+(i+1)+"][win],players["+(i+1)+"][rating]");

        }
        logEntryFile.append("\n");
        logEntryFile.append(entries);


        if( logEntryFile !=null){
            try {
                logEntryFile.close();
            }catch( Throwable t ){
                t.printStackTrace();
            }
        }


    }


    public void processFile(String username, String password, FileReader csvFile) throws Exception{
        String encryptedPassword = login(username,password);
        //System.out.println("processFile");
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        //List<Map<String,Object>> rows = parseFile(csvFile);
        List<LogEntry> rows = parseFile(csvFile, ptmUserInfo());
        logPlay(rows,username,encryptedPassword);
    }

    public String login(String username, String password) throws IOException{
        String bggpassword = "";
        try {
            URL url = new URL("http://www.boardgamegeek.com/login");
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("username", username);
            params.put("password", password);
            StringBuilder postData = new StringBuilder();
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            URLConnection connection = new URL("https://www.boardgamegeek.com/login?username="+username+"&password="+password).openConnection();
            List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
            for( String currentCookie : cookies){
                if( currentCookie.contains("bggpassword")){
                    bggpassword= currentCookie.substring(currentCookie.indexOf("=")+1,currentCookie.indexOf(";"));

                }
            }
            //bggpassword = "junk";
        }catch( Throwable t ){
            t.printStackTrace();
        }
        if( bggpassword != null && bggpassword.trim().length()>0) {
            logger.append("\npassword retrieved");
        }

        return bggpassword;
    }
    //coverts the CSVHeader names to names the post understand
    private String[] readHeaderRow( String[] csvHeader) throws IOException{
          logger.append("\nread Header Row\n");
        logger.append(csvHeader);
        logger.append("\nend Header Row\n");

        for( int i = 0 ; i < csvHeader.length;i++){
            String currentHeader = csvHeader[i].trim();
            //System.out.println("currentHeader:"+currentHeader);
            if( currentHeader.contains("playid") ||currentHeader.contains("play ID") ){
                csvHeader[i]="playid";
            }
            else if( currentHeader.contains("objectid") || currentHeader.contains("game ID") ){
                csvHeader[i]="objectid";
            }
            else if( currentHeader.contains("gamename") || currentHeader.contains("game name") ){
                csvHeader[i]="gamename";
            }
            else if( currentHeader.contains("nowinstats") ||currentHeader.contains("nowinstats") ){
                csvHeader[i]="nowinstats";
            }
            else if( currentHeader.contains("userid") ){
                csvHeader[i]="IGNORE";
            }
            else if( currentHeader.contains("playdate") || currentHeader.contains("date") ){
                csvHeader[i]="playdate";
            }
            else if( currentHeader.contains("quantity") ||currentHeader.contains("quantity") ){
                csvHeader[i]="quantity";
            }
            else if( currentHeader.contains("location") ||currentHeader.contains("location") ){
                csvHeader[i]="location";
            }
            else if( currentHeader.contains("length") ||currentHeader.contains("length") ){
                csvHeader[i]="length";
            }
            else if( currentHeader.contains("comments") ||currentHeader.contains("comments") ){
                csvHeader[i]="comments";
            }
            else if( currentHeader.contains("incomplete") ||currentHeader.contains("incomplete") ){
                csvHeader[i]="incomplete";
            }
            else if( currentHeader.contains("players[" ) ) {
                //This is when I save a CSV that this application makes
                csvHeader[i]=currentHeader;

            }
            else if( currentHeader.contains("player" ) ) {
                String[] playerInfo = currentHeader.split(" ");
                String playerDetail = playerInfo[2];
                //sometimes csv gives me "
                if( playerDetail.contains("\"")){
                    playerDetail = playerDetail.substring(0,playerDetail.length()-1);
                }
                if( playerDetail.equalsIgnoreCase("startposition")){
                    playerDetail="position";
                }
                csvHeader[i]="players"+"["+playerInfo[1]+"]["+playerDetail+"]";
            }
            else{
                csvHeader[i]="IGNORE";
            }

        }
        return csvHeader;
    }

    public List<LogEntry> parseFile(String csvFile, Map<String, UserInformation> userInfoMap ) throws Exception {
        System.out.println("Parsing File: "+ csvFile);
        logger.append("Parsing File: "+ csvFile);
        return parseFile( new FileReader(csvFile ), userInfoMap);
    }

    public List<LogEntry> parseFile(FileReader csvFile,Map<String, UserInformation> userInfoMap) throws Exception {
        populateUserInformation();
        BufferedReader br = null;
        String line = "";
        List<LogEntry> logEntries = new ArrayList<LogEntry>();
logger.append(csvFile.toString());
        try {
              br = new BufferedReader(csvFile);
            line = br.readLine();
            System.out.println("PTM Line"+ line);
            //CSV issue ( games with commas mess me up so I need the regex )...although this is really the
            // header so not needed
            String[] splitted = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            String[] headerRow = readHeaderRow(splitted);
            System.out.println("header:"+ headerRow);
            int currentCount = 0;
            while ((line = br.readLine()) != null) {
                HashMap<String,Object> currentRow = new HashMap<String,Object>();
                currentCount++;
                System.out.println("PTM line"+ line);
                if( currentCount%100==0){
//                    System.out.println("parsing row: "+currentCount);
                    logger.append("parsing row: "+currentCount+"\n");
                }
                //CSV issue ( games with commas mess me up so I need the regex )
                String[] currentLine  = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                //blank line
                //blank linemergeFiles
                System.out.println("PTM currentLine:"+ currentLine.length);
                if( currentLine.length==0 || currentLine.length==1){
                    break;
                }
                StringBuffer buf = new StringBuffer();

                System.out.println("PTM more");
                //!!!! I really just use this app to change names.... not to add play.
                //!!!! so only update when I find a name to replace
                boolean namesFound = false;
                LogEntry currentLogEntry =new LogEntry();
                boolean foundusername=false;
                for( int i=0; i< currentLine.length;i++){
                   //remove the quotes

                  if( currentLine[i].endsWith("\"") && currentLine[i].startsWith("\"") ){
                      currentLine[i]=currentLine[i].substring(1,currentLine[i].length()-1);
                   }
                    if( i< headerRow.length && !headerRow[i].equalsIgnoreCase("IGNORE")) {

                        //if we are at a players[X][name] and a name to swap
                        // then replace the name and the username
                        if( headerRow[i].contains("players")&&
                                //not to confuse with username, so the space is added
                                headerRow[i].contains("[username]")&&
                                !Utils.isEmpty(currentLine[i])
                                )
                        {
                            currentLogEntry.addValue(headerRow[i], currentLine[i]);

                            if( this.usernameInfoMap.get(currentLine[i]) != null ) {
                              //  System.out.println("currentLine:"+(currentLine[i]));

                                foundusername=true;
                                //changing the game... so we will update the row
                                namesFound = true;
                                String userNameColumn = headerRow[i].replace("username", "name");
                                currentLogEntry.addValue(userNameColumn, usernameInfoMap.get(currentLine[i]).displayName);
                                //change the username as well.
                                currentLogEntry.addValue(headerRow[i], usernameInfoMap.get(currentLine[i]).username);

                            }else{
                                currentLogEntry.addValue(headerRow[i], currentLine[i]);
                            }

                        }
                        else if( headerRow[i].contains("players")&&
                                headerRow[i].contains("[name]")&&!foundusername&&
                                userInfoMap.get(currentLine[i]) != null
                                )
                        {
                          //  System.out.println("currentLine:"+(currentLine[i]));

                            //if( !currentLine[i].equalsIgnoreCase(userInfoMap.get(currentLine[i]).displayName)){
                                namesFound=true;

                            //}
                            //changing the game... so we will update the row
                            String userNameColumn = headerRow[i].replace("name","username");
                            currentLogEntry.addValue(headerRow[i],userInfoMap.get(currentLine[i]).displayName);
                            //change the username as well.
                            currentLogEntry.addValue(userNameColumn, userInfoMap.get(currentLine[i]).username);
                        }
                        else {
                            currentLogEntry.addValue(headerRow[i], currentLine[i]);
                        }

                    }
                    foundusername=false;
                }
                //only add if we changed the code
               // if(namesFound) {
                    logEntries.add(currentLogEntry);
               // }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.append("total change rowCount:"+logEntries.size()+"\n");
        //return rows;
        return logEntries;
    }

    //Makes the actual call to the BGG and updates the play.
    public void logPlay( List<LogEntry> rows, String username, String encryptedPassword  ){

    int successCount = 0;
        int failureCount = 0;
        int currentElement =0;
        StringBuffer failureString = new StringBuffer();
        StringBuffer resultResponse = new StringBuffer();


        for( LogEntry currentLogEntry: rows){
            try{
                currentElement++;
                if( currentElement %100 ==0){
                    System.out.println("Processing " + currentElement + " row");
                }
                URL url = new URL("https://www.boardgamegeek.com/geekplay.php");//switched to https
                Map<String,Object> params = new HashMap<String, Object>();
                params.put("ajax", "1");
                params.put("action", "save");
                params.put("quantity", "1");
                params.put("version", "2");
                params.put("objecttype","thing");
              //  params.put("bggusername","ptm_junk");
             //   params.put("bggpassword","junk");


                //   conn.setRequestProperty("Cookie", "bggusername="+username+";bggpassword="+encryptedPassword);

                StringBuilder postData = new StringBuilder();
                //all the columns of the row ( player[x]name, player[x]win, etx...
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                postData.append(currentLogEntry.encodePlay());
                System.out.println("PTM postData"+URLDecoder.decode(postData.toString(), "UTF-8"));

                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                //Actually make the call to bgg
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setRequestProperty("Cookie", "bggusername="+username+";bggpassword="+encryptedPassword);

                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                resultResponse = new StringBuffer();
                for (int c; (c = in.read()) >= 0; resultResponse.append((char) c)) ;

                if( resultResponse.toString().contains("Plays")){
                    successCount+=1;
                }else{
                    failureCount+=1;
                    failureString.append("Failure on row:" + currentElement+" paramList\n");
                    failureString.append(postData+"\n");
                    failureString.append(resultResponse+"\n");
                }

            } catch(Exception e) {
                failureString.append("Failure on row:" + currentElement+" paramList\n");
                failureString.append(currentLogEntry+"\n");
                failureString.append(resultResponse+"\n");
                System.out.println(failureString.toString());

                e.printStackTrace();
            }

        }
        try {
            logger.append("\n"+"SuccessCount" + successCount);
            logger.append("\n"+"FailureCount" + failureCount);
            logger.append("\n"+failureString.toString());

        }catch( Exception e){
            e.printStackTrace();
        }
        System.out.println(logger);
    }
}
