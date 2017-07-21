import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WinLosses{
    public class WinTotals {
        public int wins;
        public int games;
        public int totalGames;
        public int ptmWins;
    }
    public WinTotals winTotals = new WinTotals();
    //for each game, log how much they win
    public Map<String,WinTotals> gamesWins = new HashMap<String,WinTotals>();
    public UserInformation userInformation;
    public WinLosses( UserInformation ui ){
        userInformation =ui;
    }
    public String csvWrite(){
        return userInformation.displayName+","+userInformation.username+","+winTotals.totalGames+","+winTotals.wins+","+winTotals.games+","+winTotals.ptmWins
        ;
    }

    public String csvWriteGames(){
        StringBuffer sb = new StringBuffer();
        Iterator gameNames = gamesWins.keySet().iterator();
        while( gameNames.hasNext()){
            String gameName = (String)gameNames.next();
            WinTotals totals = gamesWins.get(gameName);
            sb.append(userInformation.displayName+","+userInformation.username+",\""+BGGUpload.escapeGameName(gameName)+"\","+totals.totalGames+","+totals.games+","+totals.wins+","+totals.ptmWins+"\n");
        }
        return sb.toString();
    }
}