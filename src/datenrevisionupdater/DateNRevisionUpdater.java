/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datenrevisionupdater;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.io.File;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author NeilStandard
 */
public class DateNRevisionUpdater {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String majRev = "3", minRev = "0";
        String COMPILEDATE = "20120119";
        String REVISION = "11";
        String BRANCHID = "dnrSeperateNP", stability = "b";
        String version = BRANCHID + "-" + majRev + "." + minRev + "." + stability + REVISION;
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        if (args.length == 0) {
            System.out.println("\nNo Arguments!  Please Specify a File.");
            System.out.println("java -jar DateNRevisionUpdater.java /? for info\n");
        } else if (args[0].equalsIgnoreCase("/a") || args[0].equalsIgnoreCase("-a") ||
                args[0].equalsIgnoreCase("/v") || args[0].equalsIgnoreCase("-v")) {
            System.out.println("");
            System.out.println("DateNRevisionUpdater v. " + version);
            System.out.println("Compiled On: " + COMPILEDATE);
            System.out.println("Compiled By: Neil A. Patel");
            System.out.println("Created By: Neil A. Patel(neil92@bu.edu)" + "\r\n");
        } else if (args[0].equalsIgnoreCase("/?") || args[0].equalsIgnoreCase("-?") || 
                args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("--info") ||
                args[0].equalsIgnoreCase("--man")) {
                System.out.println(about());
        } else {
            System.out.println("***In DateNRevisionUpdater " + version + "***");
            System.out.println(args[0]);
            try {
                inputStream = new BufferedReader(new FileReader(args[0]));
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found Exception." + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Exception1" + e.getMessage());
            }
            try {
                outputStream = new PrintWriter(new FileWriter(args[0]+".temp"));
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found Exception." + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Exception2" + e.getMessage());
            }
            try {
                String l; boolean foundRevision = false, foundDate = false;
                while ((l = inputStream.readLine()) != null) {
                    String new1 = new String(), new2 = new String();
                    if (!foundDate) {
                        new1 = processDate(l);
                        if (!new1.equalsIgnoreCase(l)) {
                            if (!new1.equalsIgnoreCase(CONSTANT.strFlagEqual)) {
                                l = new1;
                            }
                            foundDate = true;
                        }
                    }
                    if (!foundRevision) {
                        new2 = processRevision(l);
                        if (!new2.equalsIgnoreCase(l)) {
                            l = new2;
                            foundRevision = true;
                        }
                    }
                    outputStream.println(l);
                }
            } catch (IOException e) {
                System.out.println("IOException3" + e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        System.out.println("IOException4" + e.getMessage());
                    }
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }

        //delete regular file
        File oldFile = new File(args[0]);
        oldFile.delete();
        //remove .temp ext
        File newFile = new File(args[0]+".temp");
        String newName = "newName";
        newName = newFile.getPath();
        //System.out.println("getPath()=" + newFile.getPath());
        //System.out.println("getName()=" + newFile.getName());
        //System.out.println("getAbsPath()=" + newFile.getAbsolutePath());
        //System.out.println("getConPath()=" + newFile.getCanonicalPath());
        newName = newName.replace(".temp", "");
        newFile.renameTo(new File(newName));
    }
    
    private static String processRevision(String l) {
        if (l.contains(CharBuffer.wrap(CONSTANT.strRevision.toCharArray()))) {
            int firstSavedPos = 0;
            int lastSavedPos = 0;
            boolean fFirstTime = true;
            //skip other letters & look for number
            char[] chrL = l.toCharArray();
            System.out.println(chrL[0]);
            for (int i = 0; i< l.length(); i++) {
                for (int j = 0; j < CONSTANT.numbers.length; j++) {
                    if (chrL[i] == CONSTANT.numbers[j]) {
                        //found a number!
                        //save number location for rewrite
                        if (fFirstTime) {
                            firstSavedPos = i;
                        }
                        fFirstTime = false;
                        lastSavedPos = i;
                    }
                }
             }
            System.out.println(chrL[firstSavedPos]);
            System.out.println(chrL[lastSavedPos]);
            //get target char sequence
            String target = "";
            String replacement = "";
            //find replacement
            int newNum = 0;
            target = CharBuffer.wrap(chrL).subSequence(firstSavedPos, lastSavedPos+1).toString();
            newNum = Integer.parseInt(target);
            newNum++;
            System.out.println("New Num:  " + newNum);
            replacement = Integer.toString(newNum); 
            //replace
            l = l.replaceAll(target, replacement);
            System.out.println("Replaced String on next line:  \n"+l);
       }
        return l;
    }
    
    private static String processDate(String l) {
        if (l.contains(CharBuffer.wrap(CONSTANT.strCompDate.toCharArray()))) {
            Calendar now = Calendar.getInstance();
            int firstSavedPos = 0;
            int lastSavedPos = 0;
            boolean fFirstTime = true;
            //skip other letters & look for number
            char[] chrL = l.toCharArray();
            System.out.println(chrL[0]);
            for (int i = 0; i< l.length(); i++) {
                for (int j = 0; j < CONSTANT.numbers.length; j++) {
                    if (chrL[i] == CONSTANT.numbers[j]) {
                        //found a number!
                        //save number location for rewrite
                        if (fFirstTime) {
                            firstSavedPos = i;
                        }
                        fFirstTime = false;
                        lastSavedPos = i;
                    }
                }
             }
            System.out.println(chrL[firstSavedPos]);
            System.out.println(chrL[lastSavedPos]);
            //get target char sequence
            String target = "";
            String replacement = "";
            //find replacement
            int olNum = 0, nNum = 0;
            target = CharBuffer.wrap(chrL).subSequence(firstSavedPos, lastSavedPos+1).toString();
            olNum = Integer.parseInt(target);  
            replacement = String.valueOf(now.get(Calendar.YEAR));
            if (now.get(Calendar.MONTH) < 10) replacement += "0"+String.valueOf(now.get(Calendar.MONTH)+1);
            else replacement += String.valueOf(now.get(Calendar.MONTH)+1);
            if (now.get(Calendar.DATE) < 10)replacement += "0"+String.valueOf(now.get(Calendar.DATE));
            else replacement += String.valueOf(now.get(Calendar.DATE));
            nNum = Integer.parseInt(replacement);
            System.out.println("Old:"+olNum + "\t" + "New:"+nNum);
            //replace
            l = l.replaceAll(target, replacement);
            System.out.println("Replaced String on next line:  \n"+l);
            if (target.equalsIgnoreCase(replacement)) return CONSTANT.strFlagEqual;
        }
        return l;
    }
    
    private static String about() {
        return (
            "\r\nNAME\r\n" +
                "\tjava -jar DateNRevisionUpdater.jar\r\n" +
            "\r\nSYNOPSIS\r\n" +
                "\tjava -jar DateNRevisionUpdater.jar [file]\r\n" +
                "\tjava -jar DateNRevisionUpdater.jar /?\r\n" +
                "\tjava -jar DateNRevisionUpdater.jar /a\r\n" +
            "\r\nDESCRIPTION\r\n" +
                "\tDateNRevisionUpdater is designed to update the version of a program\r\n" +
                "\tevery time the program is compiled. It updates the \"last compiled date\"\r\n" +
                "\tand iterates a new \"revision\" every time the program is compiled.\r\n" +
                "\tThese are specified by the REVISION and COMPILEDATE variables in the\r\n" +
                "\tcode of the program that is updated. REVISION must be declared after\r\n" +
                "\tCOMPILEDATE. COMPILEDATE and REVISION are updated separately so that\r\n" +
                "\tthe revision is NOT reset to zero if the date changes.\r\n" +
                "\r\n\tType in the following to a netbeans build.xml file to use this:\r\n" +
                    "\t\t<target name=\"-pre-compile\">\r\n" +
                        "\t\t\t<java\r\n" +
                        "\t\t\t\tjar=\"DateNRevisionUpdater.jar\r\n" +
                        "\t\t\t\tfailonerror=\"true\"\r\n" +
                        "\t\t\t\t>\r\n" +
                        "\t\t\t\t<arg value=\"${src.dir}\\[dir]\\[srcFile.java]\"/>\r\n" +
                        "\t\t\t</java>\r\n" +
                    "\t\t</target>\r\n" +
                "\r\n\tDo the following to use in Microsoft Visual C++ 2010 Express:\r\n" +
                    "\t\t1) Right Click your project in Solutions Explorer and\r\n" +
                    "\t\t   then click properties\r\n" +
                    "\t\t2) Go to Configuration Properties->Build Events->Prebuild Event\r\n" +
                    "\t\t3) Type in \"java -jar ...\" into the \"Command Line\"\r\n" +
                "\r\n\tExample Declarations:\r\n" +
                    "\t\tString COMPILEDATE = \"20111212\";\r\n" +
                    "\t\tString REVISION = \"0\";\r\n" +
                    "\r\n\t\tconst wchar_t * REVISION = _T(\"9\");\r\n" +
            "\r\nOPTIONS\r\n" +
                "\t/? -? --info --man --help\r\n" + 
                    "\t\tprints out full summary of help information\r\n" +
                "\t/a -a /v -v\r\n" +
                    "\t\t for version information\r\n" +
                "\t[file]\r\n" +
                    "\t\tto execute a revision update on a file specified\r\n" +
            "\r\nSEE ALSO\r\n" +
                "\tjava -?\r\n"
                );
    }
}

