//package main.java;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ptm on 2/5/2016.
 */
public class FilterFile {
      // public static String masterFile = "ehomer666.csv";
    public static String masterFile = "thorodinson.csv";
    public static String resultFile ="filterFiles.csv";
    public static UserInformation filterOnUser = new UserInformation("ptm_junk","Paul Mason");
        public static Map<String,UserInformation> masterUserInfo = mattUserInfo();

    /*
public static String masterFile = "ptm_junk.csv";
    public static String resultFile ="ptm_filterFiles.csv";
    public static UserInformation filterOnUser = new UserInformation("ptm_junk","Paul Mason");
*/
  //  public static Map<String,UserInformation> masterUserInfo = markUserInfo();//BGGUpload.ptmUserInfo();

    public static Map<String,UserInformation> markUserInfo() {
        Map<String, UserInformation> userInfoMap = new HashMap<String, UserInformation>();
        userInfoMap.put("Paul", new UserInformation("ptm_junk", "Paul Mason"));
        userInfoMap.put("Mark", new UserInformation("ehomer666", "Mark Innerebner"));
        userInfoMap.put("Hermes", new UserInformation("hayala","Hermes Ayala"));
        userInfoMap.put("Nathan", new UserInformation("wagnat",	"Nathan Wagner"));
        userInfoMap.put("Travis", new UserInformation("blackwater", "Travis Meredith"));
        userInfoMap.put("Jeff", new UserInformation("junesen", "Jeff Wu"));
        userInfoMap.put("Gene", new UserInformation("BlueFaceBeast", "Gene Winterscheidt"));
        userInfoMap.put("Scott Burns", new UserInformation("SpaceLordOO", "Scott Burns"));
        userInfoMap.put("Eugene", new UserInformation("eugene256", "Eugene Ko"));
        userInfoMap.put("Frank", new UserInformation("DrFrankNFurter", "Frank Hofmann"));
        userInfoMap.put("Jay", new UserInformation("ffyoyo", "Jay Logan"));
        userInfoMap.put("David", new UserInformation("Cygnusx1", "David Whitehouse"));
        userInfoMap.put("Amber", new UserInformation("Gidastra", "Amber Feldman"));

        return userInfoMap;
    }

    public static Map<String,UserInformation> mattUserInfo(){
        Map<String, UserInformation> userInfoMap = new HashMap<String,UserInformation>();
        userInfoMap.put("Allen", new UserInformation("a0ashle", "Allen Ashley"));

//        userInfoMap.put("Amanda", new UserInformation("Fireyaztce", "Amanda Lacy"));
        userInfoMap.put("Amanda X", new UserInformation("", "Amanda"));
//        userInfoMap.put("Amanda (Fiery)", new UserInformation("Fireyaztce", "Amanda Lacy"));

//        userInfoMap.put("Anthony", new UserInformation("Quizoid", "Anthony Martins"));

        userInfoMap.put("Ashley", new UserInformation("seussN10", "Ashley President"));
//        userInfoMap.put("Aron", new UserInformation("eranel", "Eran Eldar"));
        userInfoMap.put("Blake", new UserInformation("", "Blake Standford"));

//        userInfoMap.put("Billy", new UserInformation("b414213562", "Billy"));
        //       userInfoMap.put("bill", new UserInformation("", "Bill"));

//        userInfoMap.put("Carter", new UserInformation("nazulleptra", "Carter Linn"));
        userInfoMap.put("Carl", new UserInformation("", "Carl Alloway"));
        userInfoMap.put("Carl (Ashley)", new UserInformation("", "Carl Alloway"));
        userInfoMap.put("Caitlin", new UserInformation("", "Katlyn"));
        //       userInfoMap.put("Chris", new UserInformation("", "Chris Ferraro"));
        userInfoMap.put("Chris F", new UserInformation("", "Chris Ferraro"));
        userInfoMap.put("Chris (Lawyer)", new UserInformation("", "Chris Ferraro"));
        userInfoMap.put("Chris x", new UserInformation("cbergstrom", "Chris Bergstrom"));
//        userInfoMap.put("Chris Watson", new UserInformation("Christimwatson", "Christopher Watson"));
        userInfoMap.put("Chris", new UserInformation("Christimwatson", "Christopher Watson"));
        userInfoMap.put("Chris (New Guy)", new UserInformation("claggie", "Chris Lane"));


//       userInfoMap.put("Danaan", new UserInformation("", "Danin Jackson"));
        userInfoMap.put("Dan", new UserInformation("Salmonax", "Dan Eisenhauer"));
        userInfoMap.put("David", new UserInformation("Cygnusx1", "David Whitehouse"));
        userInfoMap.put("Derek", new UserInformation("TheDreadPirate", "Derek Bassett"));
        userInfoMap.put("Derek @P's", new UserInformation("TheDreadPirate", "Derek Bassett"));

//       userInfoMap.put("Derrick", new UserInformation("TheDreadPirate", "Derek Bassett"));

//        userInfoMap.put("Eric L", new UserInformation("Ash1138", "Eric Lucero"));

        //       userInfoMap.put("Eugene", new UserInformation("eugene256", "Eugene Ko"));

        userInfoMap.put("Gene", new UserInformation("BlueFaceBeast", "Gene Winterscheidt"));
        userInfoMap.put("Louis", new UserInformation("", "Louis"));
        userInfoMap.put("Luis", new UserInformation("", "Louis"));

        userInfoMap.put("Hsiu-Ping", new UserInformation("", "Hsiu-Ping"));
        userInfoMap.put("Hsiu-ping", new UserInformation("", "Hsiu-Ping"));

        userInfoMap.put("Jacci", new UserInformation("vibrant goo", "Jacci Ziebert"));
//        userInfoMap.put("Jason", new UserInformation("Kaiwolf", "Jason Kai"));
//        userInfoMap.put("Jason", new UserInformation("Kaiwolf", "Jason Kai"));
        userInfoMap.put("Jason Kai", new UserInformation("Kaiwolf", "Jason Kai"));
//        userInfoMap.put("Jeremiah", new UserInformation("Rimjack", "Jeremiah Dwyer"));
//        userInfoMap.put("Jesh", new UserInformation("Hat and Watch", "Jeshua Siplak"));
//        userInfoMap.put("Jennifer N.", new UserInformation("lacy2408", "Jennifer Nicodem"));
        userInfoMap.put("Jennifer N", new UserInformation("lacy2408", "Jennifer Nicodem"));
//        userInfoMap.put("Jennifer", new UserInformation("jprice72", "Jennifer Price"));
//       userInfoMap.put("Jeff", new UserInformation("", "Jeff Wilson"));
//        userInfoMap.put("Jenna", new UserInformation("", "Jenna Black"));//may have to keep
//        userInfoMap.put("Jenna", new UserInformation("", "Jenna Black"));//may have to keep
        userInfoMap.put("John", new UserInformation("jreakins", "John Eakins"));
        //      userInfoMap.put("Jen", new UserInformation("lacy2408", "Jennifer Nicodem"));
        //      userInfoMap.put("Jacob", new UserInformation("", "Jacob Wagner"));


        userInfoMap.put("Justin", new UserInformation("", "Justin Wu"));

//       userInfoMap.put("Mark B", new UserInformation("SirShark33", "Mark Benesh"));
        userInfoMap.put("Mark", new UserInformation("SirShark33", "Mark Benesh"));
        //   userInfoMap.put("Mark", new UserInformation("ehomer666", "Mark Innerebner"));
        userInfoMap.put("Mark (Saturday)", new UserInformation("ehomer666", "Mark Innerebner"));
//
        userInfoMap.put("Martin", new UserInformation("mnoreke",	"Martin Noreke"));

        userInfoMap.put("Matthew Tanous", new UserInformation("thorodinson", "Matthew Tanous"));
        userInfoMap.put("Matt Tanous", new UserInformation("thorodinson", "Matthew Tanous"));
        userInfoMap.put("Matt T", new UserInformation("thorodinson", "Matthew Tanous"));

        //enigmakairos	Melinda Carbonell
//        userInfoMap.put("Melinda", new UserInformation("enigmakairos","Melinda Carbonell"));
//        userInfoMap.put("Melinda Carbonell", new UserInformation("enigmakairos","Melinda Carbonell"));
        userInfoMap.put("Hermes", new UserInformation("hayala","Hermes Ayala"));

        //      userInfoMap.put("Nathan", new UserInformation("wagnat",	"Nathan Wagner"));
        userInfoMap.put("Nathan", new UserInformation("wagnat",	"Nathan Wagner"));
        // userInfoMap.put("Nathan", new UserInformation("SludgeBeard", "Nathan Addison"));

        userInfoMap.put("Paul Mason", new UserInformation("ptm_junk", "Paul Mason"));
//        userInfoMap.put("Paul", new UserInformation("ptm_junk", "Paul Mason"));
        userInfoMap.put("Paul Stein", new UserInformation("", "Paul Steen"));
        userInfoMap.put("Scott", new UserInformation("", "Scott Teske"));

        userInfoMap.put("Sean", new UserInformation("igwanna", "Shawn Hazelwood"));
        userInfoMap.put("Shawn", new UserInformation("igwanna", "Shawn Hazelwood"));
        userInfoMap.put("Carol", new UserInformation("", "Carol Wilson"));

        userInfoMap.put("Stephen Black", new UserInformation("blackflight4", "Steven Black"));
        userInfoMap.put("Steven @P's", new UserInformation("blackflight4", "Steven Black"));
        //      userInfoMap.put("Stephen", new UserInformation("blackflight4", "Steven Black"));
        //      userInfoMap.put("Steven", new UserInformation("blackflight4", "Steven Black"));

        //        userInfoMap.put("Todd", new UserInformation("toddpark75", "Todd Parker"));
        //      userInfoMap.put("Travis", new UserInformation("blackwater", "Travis Meredith"));
        userInfoMap.put("Will", new UserInformation("", "William"));
        userInfoMap.put("Jacob", new UserInformation("", "Jacob Wagner"));
        userInfoMap.put("Marisol", new UserInformation("", "Mirasol Black"));
        userInfoMap.put("Jenna", new UserInformation("", "Jenna Black"));

        userInfoMap.put("Mike (Saturday)", new UserInformation("MooseyFate", "Mike Welsch"));
        userInfoMap.put("Frank", new UserInformation("DrFrankNFurter", "Frank Hofmann"));


//        userInfoMap.put("Werewolf Kai Jason", new UserInformation("Kaiwolf", "Jason Kai"));
        userInfoMap.put("Yomper", new UserInformation("", "Mr. Black"));
        return userInfoMap;
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
        System.out.println("maaster File"+ masterFile);
        List<LogEntry> guestFile = u2.parseFile(dir+masterFile, masterUserInfo);
        System.out.println("PTM csvFile:"+ " guest size: "+guestFile.size());
        System.out.println("guestFile Count"+ guestFile.size());

        //first shrink the compare file to only had logged plays with UserInfo
      //  UserInformation ui = new UserInformation("Kaiwolf",	"Jason Kai");

        List<LogEntry> filteredComparedFile = new ArrayList<LogEntry>();
        for( LogEntry currentCompareEntry: guestFile ) {
            if( currentCompareEntry.hasUser(filterOnUser) ){
                filteredComparedFile.add(currentCompareEntry);
            }
        }
        System.out.println("LogEntry Count"+ filteredComparedFile.size());

        u2.writeCsvFile(filteredComparedFile,  dir+resultFile, null);

        String logFile = u2.getLogger();
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

}
