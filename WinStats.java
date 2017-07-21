import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ptm_j on 12/15/2016.
 */
public class WinStats{

        public static String resultFile = "winStats.csv";
        public static String gamesFile = "gameStats.csv";

        public static String masterFile = "ptm_junk.csv";
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
//            String csvFile = dir+"plays.csv";
                String csvFile = dir+ masterFile;
                //FileReader csvFileReader = new FileReader(csvFile);
                List<LogEntry> masterFile = u2.parseFile(csvFile,u2.ptmUserInfo());

                System.out.println("PTM csvFile:"+ csvFile+" masterFile size: "+masterFile.size());
//Kaiwolf-plays-2015
          //      List<LogEntry> guestFile = u2.parseFile(dir+"plays.csv");
           //     System.out.println("PTM csvFile:"+ dir+""+" guest size: "+guestFile.size());
                WinStats ws = new WinStats();

                java.util.Map winStats = ws.calculateWinStats(masterFile);
                ws.writeGameWinCsvFile(winStats,dir+gamesFile);
                ws.writePlayerWinCsvFile(winStats,dir+resultFile);
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

    public void writeGameWinCsvFile(java.util.Map<UserInformation,WinStats> logEntries, String fileName) throws Exception{
        //    System.out.println("writing csv file"+fileName +" UI: " + ui);
        // logger.append("writing number of rows: "+logEntries.size()+"\n");
        FileWriter fw = new FileWriter(fileName);
        BufferedWriter logEntryFile = null;
        // CsvWriter
        logEntryFile = new BufferedWriter(fw);

        //firstcreate a header
        int maxPlayer=0;


        StringBuffer entries= new StringBuffer();
        Iterator objects = logEntries.values().iterator();
        while( objects.hasNext()){
            WinLosses currentEntry = (WinLosses) objects.next();
            //  objects
            //for(LogEntry currentEntry:logEntries.keySet()){
            String currentRow = currentEntry.csvWriteGames();  //currentEntry.csvWrite(ui);

            if( currentRow != null) {
                entries.append(currentRow);
            }
            //    maxPlayer = Math.max(maxPlayer,currentEntry.players.size());
        }
        logEntryFile.append("displayName,username,game,totalgames,games,win,ptmWins");
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

    public void writePlayerWinCsvFile(java.util.Map<UserInformation,WinStats> logEntries, String fileName) throws Exception{
            //    System.out.println("writing csv file"+fileName +" UI: " + ui);
            // logger.append("writing number of rows: "+logEntries.size()+"\n");
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter logEntryFile = null;
            // CsvWriter
            logEntryFile = new BufferedWriter(fw);

            //firstcreate a header
            int maxPlayer=0;


            StringBuffer entries= new StringBuffer();
            Iterator objects = logEntries.values().iterator();
            while( objects.hasNext()){
                WinLosses currentEntry = (WinLosses) objects.next();
                //  objects
                //for(LogEntry currentEntry:logEntries.keySet()){
                String currentRow = currentEntry.csvWrite();  //currentEntry.csvWrite(ui);

                if( currentRow != null) {
                    entries.append(currentRow+"\n");
                }
                //    maxPlayer = Math.max(maxPlayer,currentEntry.players.size());
            }
            logEntryFile.append("displayName,username,totalgames,wins,games,ptmWins");
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

    public Map<String,WinLosses> calculateWinStats(List<LogEntry> masterFile ){
        Map<String,WinLosses> winStats = new HashMap<String,WinLosses>();
        int i = 0;
        for( LogEntry currentLogEntry : masterFile){
            boolean nowinstats = currentLogEntry.nowinstats.equalsIgnoreCase("1");
            int ptmWins = 0;
            //track if I won
            for( PlayerInfo currentPlayerInfo: currentLogEntry.players ) {
                if( currentPlayerInfo.getUserInformation().username.equalsIgnoreCase("ptm_junk") && currentPlayerInfo.win != null && currentPlayerInfo.win.equalsIgnoreCase("1") ){
                    ptmWins = 1;
                }

            }
            for( PlayerInfo currentPlayerInfo: currentLogEntry.players ){
                if( !winStats.containsKey(currentPlayerInfo.getUserInformation().toString())){
                    winStats.put(currentPlayerInfo.getUserInformation().toString(), new WinLosses(currentPlayerInfo.getUserInformation()));
                }

                //win information about on player
                WinLosses currentWL = winStats.get(currentPlayerInfo.getUserInformation().toString());

                currentWL.winTotals.totalGames++;

                WinLosses.WinTotals gameTotals = currentWL.gamesWins.get(currentLogEntry.gamename);
                if( gameTotals == null){
                    gameTotals = currentWL.new WinTotals();
                    gameTotals.ptmWins = 0;
                    currentWL.gamesWins.put(currentLogEntry.gamename,gameTotals);
                }

                gameTotals.totalGames++;
                if( !nowinstats) {
                    if(currentPlayerInfo.getUserInformation().displayName.equalsIgnoreCase("Alex")){
//                        System.out.println("currentLogEntry.gamename" + currentLogEntry.gamename + " gameTotals.ptmWins: " +gameTotals.ptmWins + " PTMWins:" + ptmWins);
                        if( gameTotals.ptmWins > 0){
 //                           System.out.println("gameWins "+ gameTotals.games);
                        }
                    }
                    currentWL.winTotals.games++;
                    gameTotals.games++;
                    currentWL.winTotals.ptmWins += ptmWins;
                    gameTotals.ptmWins += ptmWins;
                    if (currentPlayerInfo.win != null && currentPlayerInfo.win.equalsIgnoreCase("1")) {
                        currentWL.winTotals.wins++;
                        gameTotals.wins++;
                    }
                }
            }

        }
        return winStats ;
    }


}
