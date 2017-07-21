//package main.java;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ptm on 2/1/2016.
 */
public class MergeFile {

    public static String masterFile = "ptm_junk.csv";
    public static String slaveFile = "filterFiles.csv";
    StringBuffer logger = null;
    public void setLogger( StringBuffer logger){
        this.logger = logger;
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
            //FileReader csvFileReader = new FileReader(csvFile);
            List<LogEntry> masterFile = u2.parseFile(csvFile,u2.ptmUserInfo());

            System.out.println("PTM csvFile:"+ csvFile+" masterFile size: "+masterFile.size());

            List<LogEntry> slaveEntries = u2.parseFile(dir+slaveFile,u2.ptmUserInfo());
            System.out.println("PTM csvFile:"+ dir+""+" guest size: "+slaveEntries.size());
            MergeFile mf = new MergeFile();
            mf.setLogger(u2.getLoggerBuffer());
            List<LogEntry> mergeResults = mf.mergeFiles(masterFile,slaveEntries, new UserInformation("ptm_junk","Paul Mason"));
            u2.writeCsvFile(mergeResults,  dir+"mergeFile-plays.csv", null);

            String logFile = u2.getLogger();
            FileWriter fw = new FileWriter(dir+"uploadPlay.log");
            loggerFile = new BufferedWriter(fw);
            loggerFile.append(logFile);
            System.out.println(logFile);
        }catch( Exception e ){
            e.printStackTrace();
            System.out.println("!!!!!!!!!!!!!ERROR");
        }
        finally{
            if( loggerFile !=null){
                try {
                    loggerFile.close();
                }catch( Throwable t ){
                    t.printStackTrace();
                }
            }
            System.out.println("done processing");
        }
    }
    public List<LogEntry> mergeFiles( List<LogEntry> masterFile, List<LogEntry> slaveEntries, UserInformation ui){
        logger.append("Merging Files"+"\n");
        //first shrink the compare file to only had logged plays with UserInfo
        List<LogEntry> filteredComparedFile = new ArrayList<LogEntry>();
        for( LogEntry slaveCompareEntry: slaveEntries) {
            if( slaveCompareEntry.hasUser(ui) ){
                filteredComparedFile.add(slaveCompareEntry);
            }
        }



        logger.append("Number of Games the Compare File has of the user: " + filteredComparedFile.size() + "\n");
        logger.append("masterFile list"+masterFile.size()+"\n");

        HashSet alreadyProcessed = new HashSet();
        List<LogEntry> resultFile = new ArrayList<LogEntry>();
        List<String> alreadyMerged = new ArrayList<String>();
        for( LogEntry currentMasterEntry: masterFile){
            boolean foundCompareMatch = false;
            for( LogEntry currentCompareEntry : filteredComparedFile){
                //  System.out.println("PTM currentMasterEntry:"+currentMasterEntry);
                if(alreadyProcessed.contains(currentCompareEntry.playid)){
                    //skip if we already processed the entry
//                    logger.append("skipping already used compareEntry: "+currentCompareEntry.playid+"\n");

                }
                else if( currentMasterEntry.playdate.equalsIgnoreCase(currentCompareEntry.playdate)) {
                    boolean gameFound=false;
                    System.out.println("currentMasterEntry "+currentMasterEntry.gamename);
                    System.out.println("currentCompareEntry "+currentCompareEntry.gamename);
                    if (currentMasterEntry.objectid.equalsIgnoreCase(currentCompareEntry.objectid)) {
                        gameFound=true;
                    }
                    else if( ( currentMasterEntry.gamename != null && currentCompareEntry.gamename != null&& currentMasterEntry.gamename.contains(currentCompareEntry.gamename) ) ||
                            ( currentMasterEntry.gamename !=null && currentCompareEntry.gamename != null && currentCompareEntry.gamename.contains(currentMasterEntry.gamename) )
                            ){
                        gameFound=true;
                        logger.append("Plays have similar names, "+
                                currentMasterEntry.playid +" "+ currentMasterEntry.gamename + " and "
                                +currentCompareEntry.playid + " " +currentCompareEntry.gamename+"\n");
                    }

                    if (gameFound) {
                        //right date and game.  now check the users
                        boolean hasAllUsers = comparePlayers( currentMasterEntry, currentCompareEntry );

                        if (hasAllUsers) {
                            alreadyProcessed.add(currentCompareEntry.playid);
                            //great name date, game, and players. update entry.
                            //updateEntry.
                            LogEntry resultEntry = mergeEntries(currentMasterEntry, currentCompareEntry);
                            if( resultEntry != null) {
                                resultFile.add(resultEntry);
                            }else{
                                alreadyMerged.add( currentCompareEntry.playid);
                            }
                            break;
                        } else {
                            currentMasterEntry.notes = "Users are different than " + currentCompareEntry.playid;
                        }


                    }
                }
            }
        }
        ArrayList<String> notProcessed = new ArrayList<String>();
        for( LogEntry currentCompareEntry: filteredComparedFile) {
            if(alreadyProcessed.contains(currentCompareEntry.playid) || alreadyMerged.contains(currentCompareEntry.playid) ){

            }
            else{
                notProcessed.add(currentCompareEntry.playid);
            }
        }

        logger.append("Already Merged: "+ alreadyMerged.size() + "  "+alreadyMerged+"\n");
        logger.append("Not Pocessed: "+ notProcessed.size() + "  "+notProcessed+"\n");
        return resultFile;
    }
    //compare the players ( also add if needed )
    public boolean comparePlayers( LogEntry currentMasterEntry, LogEntry currentCompareEntry ){
        boolean hasAllUsers = true;


        for (PlayerInfo currentMasterPlayer : currentMasterEntry.players) {
            if (currentCompareEntry.hasUser(currentMasterPlayer.getUserInformation())) {
//                               logger.append(" found ");
            } else {
                hasAllUsers = false;
                logger.append("merging entries:" + currentMasterEntry.playid + " compare:" + currentCompareEntry.playid + "\n");
                logger.append("doesn't have all users  " + currentMasterPlayer.getUserInformation() + "\n");
                logger.append( "Mastr: "+currentMasterEntry.playid + " " + currentMasterEntry.names + "\n");
                logger.append( "Slave: "+currentCompareEntry.playid + " " + currentCompareEntry.names + "\n");
                break;
            }
        }

        if( hasAllUsers ){
            boolean differentPlayerCount = false;
            if( currentMasterEntry.players.size() != currentCompareEntry.players.size()){
                differentPlayerCount = true;
                logger.append("Different PlayerCount "+ currentMasterEntry.playid + " " + currentMasterEntry.names+"\n");
                logger.append("                      "+ currentCompareEntry.playid + " " + currentCompareEntry.names+"\n");
            }

            if( differentPlayerCount ) {
                //The compareFile has more users... add them
                for (PlayerInfo currentComparePlayer : currentCompareEntry.players) {
                    if (currentMasterEntry.hasUser(currentComparePlayer.getUserInformation())) {
                    } else {
                        logger.append("adding User: " + currentComparePlayer.name+"\n");
                        currentMasterEntry.addPlayer(currentComparePlayer);
                        currentMasterEntry.notes += "adding User: " + currentComparePlayer.name;
                    }

                }
            }
        }
        return hasAllUsers;
    }


    public LogEntry mergeEntries( LogEntry currentMasterEntry, LogEntry currentCompareEntry ){
        if( !Utils.isEmpty(currentMasterEntry.comments) &&currentMasterEntry.comments.contains("Merging Entries ")){
            logger.append("Already Merged:"+ currentMasterEntry.playid+"\n");
            // entry has already been merged... so short circuit the thing
            return null;
        }
        currentMasterEntry.comments = " Merging Entries "+ currentMasterEntry.playid + " and "+currentCompareEntry.playid;
        LogEntry resultMergeEntry = new LogEntry();
        if( currentMasterEntry.comments.equalsIgnoreCase( currentCompareEntry.comments) ){
            //all cool with comments
        }
        else{
            currentMasterEntry.comments += currentCompareEntry.comments;
            currentMasterEntry.notes += "Appending comments " +currentCompareEntry.comments;

        }
        if( currentMasterEntry.incomplete== null || currentMasterEntry.incomplete.equalsIgnoreCase(currentCompareEntry.incomplete)){
            //sameincomplete status
        }
        else{
            if( currentCompareEntry.incomplete ==null || currentCompareEntry.incomplete.trim().length()==0 ){
                //skip value
            }else {
                currentMasterEntry.incomplete = "1";
                currentMasterEntry.notes += "Changing incomplete from " + currentMasterEntry.incomplete + " to 1";
            }
        }

        if( currentMasterEntry.length != null && currentMasterEntry.length.equalsIgnoreCase(currentCompareEntry.length) ){
            //same length
        }else{
            if( Utils.isEmpty(currentCompareEntry.length)&& Utils.isEmpty(currentCompareEntry.length)){
                //both are null
            }
            else if(currentMasterEntry.length.equalsIgnoreCase("0") || currentMasterEntry.length.trim().length()==0 ){
                //master is blank... populate
                currentMasterEntry.length = currentCompareEntry.length;
                currentMasterEntry.notes += "Changing length from " + currentMasterEntry.length +" to "+ currentCompareEntry.length;
            }
        }
        resultMergeEntry.playid = currentMasterEntry.playid;
        resultMergeEntry.objectid= currentMasterEntry.objectid;
        resultMergeEntry.gamename= currentMasterEntry.gamename;
        resultMergeEntry.playdate= currentMasterEntry.playdate;
        resultMergeEntry.location= currentMasterEntry.location;
        resultMergeEntry.incomplete =currentMasterEntry.incomplete;
        resultMergeEntry.length= currentMasterEntry.length;
        resultMergeEntry.comments = currentMasterEntry.comments;
        resultMergeEntry.players = mergePlayerInfo(currentMasterEntry, currentCompareEntry, resultMergeEntry);



        return resultMergeEntry;

    }

    //need to pass in everything to append notes
    public List<PlayerInfo> mergePlayerInfo( LogEntry currentMasterEntry, LogEntry currentCompareEntry, LogEntry resultMergeEntry ){
        List<PlayerInfo> currentMasterPlayerInfoList=currentMasterEntry.players;
        List<PlayerInfo> currentComparePlayerInfoList=  currentCompareEntry.players;
        List<PlayerInfo>  resultPlayerInfoList = new ArrayList<PlayerInfo>();

        if( currentMasterPlayerInfoList.size() != currentComparePlayerInfoList.size()){
            //shouldn't happen but just in case.
            resultMergeEntry.notes += " player sizes are not the same";
        }

        for( PlayerInfo currentMasterPlayerInfo: currentMasterPlayerInfoList){
            for( PlayerInfo currentComparePlayerInfo: currentComparePlayerInfoList){
                if( currentMasterPlayerInfo.getUserInformation().partialMatch(currentComparePlayerInfo.getUserInformation()) ){
                    //matchfound
                    PlayerInfo resultPlayerInfo = new PlayerInfo();
                    resultPlayerInfo.username= mergePlayerEntry(currentMasterPlayerInfo.username, currentComparePlayerInfo.username, "username", resultMergeEntry);
                    resultPlayerInfo.name = mergePlayerEntry(currentMasterPlayerInfo.name, currentComparePlayerInfo.name, "name", resultMergeEntry);
                    resultPlayerInfo.color= mergePlayerEntry(currentMasterPlayerInfo.color, currentComparePlayerInfo.color, "color", resultMergeEntry);
                    resultPlayerInfo.position= mergePlayerEntry(currentMasterPlayerInfo.position, currentComparePlayerInfo.position, "position", resultMergeEntry);
                    resultPlayerInfo.rating= mergePlayerEntry(currentMasterPlayerInfo.rating, currentComparePlayerInfo.rating, "rating", resultMergeEntry);
                    resultPlayerInfo.score= mergePlayerEntry(currentMasterPlayerInfo.score, currentComparePlayerInfo.score, "score", resultMergeEntry);
                    resultPlayerInfo.win= mergePlayerEntry(currentMasterPlayerInfo.win, currentComparePlayerInfo.win, "win", resultMergeEntry);
                    resultPlayerInfoList.add(resultPlayerInfo);
                    break;
                }
            }
        }

        return resultPlayerInfoList;
    }
    public String mergePlayerEntry(String masterField, String compareField, String fieldName, LogEntry resultMergeEntry){
        if( Utils.isEmpty(masterField) || masterField.equalsIgnoreCase("0") ){
            if( Utils.isEmpty(compareField ) || compareField.equalsIgnoreCase("0") ){
                //both are blank
            }
            else{
                resultMergeEntry.notes += "   changing "+ fieldName+ "  to: " + compareField;
                return compareField;
            }
        }
        else if(masterField.equalsIgnoreCase(compareField)){

        }
        else{
            if(Utils.isEmpty(compareField ) || compareField.equalsIgnoreCase("0")){
                // no data in compareFile
            }else {
                resultMergeEntry.notes += "  " +fieldName + " has different Values " +
                        masterField + " and " + compareField;
                return compareField;

            }
        }
        return masterField;


    }

}
