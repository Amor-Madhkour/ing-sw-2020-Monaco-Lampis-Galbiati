package it.polimi.ingsw.psp40.view.cli;

import it.polimi.ingsw.psp40.commons.Colors;
import it.polimi.ingsw.psp40.commons.Configuration;
import it.polimi.ingsw.psp40.exceptions.OldUserException;
import it.polimi.ingsw.psp40.exceptions.YoungUserException;
import it.polimi.ingsw.psp40.network.client.Client;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * it used to be a "small" library of utilities for the CLI, now is pretty big
 * mainly for input verification, display of tables in ascii art
 *
 * @author TiberioG
 */
public class Utils {

    private PrintWriter out;
    private Scanner in;

    /**
     * constructor for utils
     *
     * @param in  Scanner
     * @param out Printwriter
     */
    public Utils(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    /**
     * just an utility to get longest string in a matrix of strings
     *
     * @param matrix a matrix of strings
     * @return int of chars of longest string
     */
    public static int longestMatrix(String[][] matrix) {
        int maxLength = 0;
        String longestString = "";
        for (String[] strings : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                if (strings[j].length() > maxLength) {
                    maxLength = strings[j].length();
                    longestString = strings[j];
                }
            }
        }
        return longestString.length();
    }


    /**
     * utiliyy to get the maximum length of an array of strings
     *
     * @param array
     * @return the maximum length
     */
    public static int longestArray(String[] array) {
        int maxLength = 0;
        String longestString = "";
        for (String s : array) {
            if (s.length() > maxLength) {
                maxLength = s.length();
                longestString = s;
            }

        }
        return longestString.length();
    }


    /**
     * utils for choices
     *
     * @param min smaller number readable
     * @param max biggest number readable
     * @return number read
     */
    public int readNumbers(int min, int max) {
        int number;
        do {
            out.println("Choose one number:\n");
            while (!in.hasNextInt()) {
                out.println("That's not a number!\n");
                in.next();
            }
            number = in.nextInt();
        } while (number < min || number > max);
        in.nextLine();
        return number;
    }

    /**
     * Manages the insertion of an integer on command line input,
     * asking it again until it not a valid value.
     *
     * @param minValue is the minimum acceptable value of the input
     * @param maxValue is the maximum acceptable value of the input
     * @return the value of the input
     */
    public int validateIntInput(int minValue, int maxValue) {
        int output;
        try {
            output = in.nextInt();
        } catch (InputMismatchException e) {
            output = minValue - 1;
            in.nextLine();
        }
        while (output > maxValue || output < minValue) {
            System.out.println("Value must be between " + minValue + " and " + maxValue + ". Please, try again:");
            try {
                output = in.nextInt();
            } catch (InputMismatchException e) {
                output = minValue - 1;
                in.nextLine();
            }
        }
        in.nextLine(); // handle nextInt()
        return output;
    }

    /**
     * method to read a date and parse it
     *
     * @param kind string used when asking for which kind of date: example birthday
     * @return a {@link Date}
     */
    public Date readDate(String kind) {
        DateFormat dateFormat = new SimpleDateFormat(Configuration.formatDate);
        Date date = new Date();
        Date today = new Date();
        Date oldest = new Date();
        try {
            oldest = dateFormat.parse(Configuration.minDate);
        } catch (ParseException e) {
            //it's impossible to trow excep here ehe
        }

        if (kind == null) {
            kind = "date";
        }

        out.println("Insert " + kind + " in format " + Configuration.formatDate);
        while (true) {
            try {
                date = dateFormat.parse(in.nextLine());
                if (date.before(today) && date.after(oldest)) {
                    break;
                } else {
                    if (date.after(today)) {
                        out.println("you're too young to play this game");
                    }
                    if (date.before(oldest)) {
                        out.println("you're too old to play this game");
                    }

                }
            } catch (ParseException e) {
                out.println("Wrong format of date");
            }
        }

        return date;
    }


    /**
     * Method to parse a date from a string which throws exception if the date is not accepted
     *
     * @param input the string containing the date
     * @return an object {@link Date}
     * @throws ParseException     if the format is wrong or is not a date
     * @throws YoungUserException user too young means the birthdate inserted is after today
     * @throws OldUserException   if the date is before the one defined in {@link Configuration}
     */
    public static Date isValidDate(String input) throws ParseException, YoungUserException, OldUserException {
        DateFormat dateFormat = new SimpleDateFormat(Configuration.formatDate);
        Date date = dateFormat.parse(input);
        Date today = new Date();
        Date oldest = new Date();
        try {
            oldest = dateFormat.parse(Configuration.minDate);
        } catch (ParseException e) {
            //it's impossible to trow excep here
        }
        if (date.before(today) && date.after(oldest)) {
            return date;
        } else {
            if (date.after(today)) {
                throw new YoungUserException();
            }
            if (date.before(oldest)) {
                throw new OldUserException();
            }
        }
        return null;
    }

    /**
     * method to know if a string contains a valid date
     *
     * @param input the string containing a date
     * @return true if it's a valid date, false otherwise
     */
    public static boolean isValidDateBool(String input) {
        try {
            isValidDate(input);
            return true;
        } catch (ParseException | YoungUserException | OldUserException e) {
            return false;
        }
    }

    /**
     * method used to ask a player to insert some numbers, defined as input, and those cannot be repeated
     *
     * @param min     value of accepted input
     * @param max     value of accepted input
     * @param howmany numbers you want to read
     * @return a list of the number selected by user
     */
    public List<Integer> readNotSameNumbers(int min, int max, int howmany) {
        List<Integer> numbers = new ArrayList<Integer>();

        while (numbers.size() < howmany) {
            int curinput = readNumbers(min, max);
            if (!numbers.contains(curinput)) {
                numbers.add(curinput);
            }
        }
        return numbers;
    }

    /**
     * method used to read coordinates in the format xx,yy
     *
     * @param min value of each coordinate
     * @param max value of each coordinate
     * @return an array of int[2]  containing the coordinates
     */
    public int[] readPosition(int min, int max) {
        int[] coord = new int[2];
        String input;
        do {
            input = in.nextLine();
            while (!input.matches("([" + min + "-" + max + "]),([" + min + "-" + max + "])")) {
                out.println("This is not an allowed coordinate");
                input = in.nextLine();
            }
            String[] ints = input.split(",");
            coord[0] = Integer.parseInt(ints[0]);
            coord[1] = Integer.parseInt(ints[1]);
        } while (coord[0] < min || coord[0] > max || coord[1] < min || coord[1] > max);
        return coord;
    }

    /**
     * Method used to print in asciiArt a single column table
     *
     * @param title     of the table, will be made UPPERCASE
     * @param inputList an array of strings, which are the rows
     * @param delay     millisecond between showing a line and the following
     */
    public void singleTableCool(String title, String[] inputList, int delay) {
        final int SPACEADD = 5;
        int height = inputList.length;

        int width = Math.max(Utils.longestArray(inputList), title.length()) + SPACEADD;
        int innerwidth = width - 4;

        String titleString = centerString(width, title);

        StringBuilder table = new StringBuilder();
        //top line
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        //title line
        table.append("???").append(titleString.replaceAll("\n", " ").toUpperCase()).append("???\n");

        //close tile line
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        //middle item lines
        for (int i = 0; i < height; i++) {
            String nonewline = inputList[i].replaceAll("\n", " ");
            String output = String.format(". %-" + innerwidth + "s", nonewline);

            if (nonewline.length() > Colors.reset().length()) { // if length is less than the colorreset length it means cannot be colored for sure
                if (!Colors.reset().equals(nonewline.substring(nonewline.length() - Colors.reset().length()))) { //then I check if it contains a color reset at the end
                    table.append("??? ").append(i).append(output).append("???\n"); //if not
                } else { // it there is colorreset at the end: FIX needed!
                    table.append("??? ").append(i).append(output).append("         ???\n");  //sorry for magic numbers of spaces but just works

                }
            } else {
                table.append("??? ").append(i).append(output).append("???\n");
            }
        }

        //closeline
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        String[] lines = table.toString().split(System.getProperty("line.separator"));

        for (int i = 0; i < lines.length; i++) {
            out.println(lines[i]);
            doTimeUnitSleep(delay);
        }
    }


    /**
     * Method used to get as a string a single column table in asciiart
     * this method doesn't print!
     *
     * @param title     of the table, will be made UPPERCASE
     * @param inputList an array of strings, which are the rows
     * @return a string containing the table in asciiart
     */
    public String tableString(String title, String[] inputList) {
        final int SPACEADD = 5;
        int height = inputList.length;

        int width = Math.max(Utils.longestArray(inputList), title.length()) + SPACEADD;
        int innerwidth = width - 4;

        String titleString = centerString(width, title);

        StringBuilder table = new StringBuilder();
        //top line
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        //title line
        table.append("???").append(titleString.replaceAll("\n", " ").toUpperCase()).append("???\n");

        //close tile line
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        //middle item lines
        for (int i = 0; i < height; i++) {
            String nonewline = inputList[i].replaceAll("\n", " ");
            String output = String.format(". %-" + innerwidth + "s", nonewline);

            if (nonewline.length() > Colors.reset().length()) { // if length is less than the colorreset length it means cannot be colored for sure
                if (!Colors.reset().equals(nonewline.substring(nonewline.length() - Colors.reset().length()))) { //then I check if it contains a color reset at the end
                    table.append("??? ").append(i).append(output).append("???\n"); //if not
                } else { // it there is colorreset at the end: FIX needed!
                    table.append("??? ").append(i).append(output).append("         ???\n");  //sorry for magic numbers of spaces but just works
                }
            } else {
                table.append("??? ").append(i).append(output).append("???\n");
            }
        }

        //closeline
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        return table.toString();

    }

    /**
     * This method returns a box with a title and an empty space
     * used to create forms
     *
     * @param title of the form, will be displayed up
     * @param width of the box
     * @return the string containing the form in asciiart
     */
    public String form(String title, int width) {
        String titleString = centerString(width, title);

        StringBuilder table = new StringBuilder();
        //top line
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        //title line
        table.append("???").append(titleString.replaceAll("\n", " ").toUpperCase()).append("???\n");

        //close tile line
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        //formarea
        int centerwidth = width - 2;
        String empty = String.format("%0" + centerwidth + "d", 0).replace('0', ' ');
        table.append("??? ").append(empty).append(" ???\n");

        //closeline
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");
        return table.toString();
    }

    /**
     * This method returns a box with a title and a prefill
     * used to create the form with inside the regexp for the date
     *
     * @param title   of the form, will be displayed up
     * @param width   of the box
     * @param prefill which is put inside the form
     * @return the string containing the form in asciiart
     */
    public String formPrefilled(String title, int width, String prefill) {
        String titleString = centerString(width, title);

        StringBuilder table = new StringBuilder();
        //top line
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        //title line
        table.append("???").append(titleString.replaceAll("\n", " ").toUpperCase()).append("???\n");

        //close tile line
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");

        //formarea
        int centerwidth = width - 3;
        String output = String.format("%-" + centerwidth + "s", prefill);
        table.append("???  ").append(output).append(" ???\n");

        //closeline
        table.append("???");
        for (int i = 0; i < (width); i++) {
            table.append("???");
        }
        table.append("???\n");
        return table.toString();
    }

    /**
     * Method used in {@link CLI} to print a map as a table
     *
     * @param stringIsland a matrix containing the strings to be displayed
     */
    public void printMap(String[][] stringIsland) {
        String lineSplit = "";
        StringJoiner splitJoiner = new StringJoiner("???", "|", "|");
        for (int i = 0; i < 5; i++) {
            splitJoiner.add(String.format("%14s", "").replace(" ", "-"));
        }
        lineSplit = splitJoiner.toString();
        for (String[] row : stringIsland) {
            StringJoiner sj = new StringJoiner(" | ", "| ", " |");
            for (String col : row) {
                sj.add(col);
            }
            System.out.println(lineSplit);
            System.out.println(sj.toString());
        }
        System.out.println(lineSplit);
    }


    /**
     * Static method used to center a string using blanks
     *
     * @param width int number of the width of output string
     * @param s     the string to center with spaces
     * @return a string with added spaces with the original string in the middle
     */
    public static String centerString(int width, String s) {
        return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }


    /**
     * method used to validate an ip
     *
     * @return a string containing a valid ip
     */
    public String readIp() {
        String ip;
        ip = in.nextLine();
        while (!isValidIp(ip)) {
            System.out.println("This is not a valid IPv4 address. Please, try again:");
            ip = in.nextLine();
        }
        return ip;
    }

    /**
     * method used to know if the string is a valid uername
     *
     * @param input the string
     * @return true if is valid, false otherwise
     */
    public static boolean isValidUsername(String input) {
        return !(input.equalsIgnoreCase("ALL") || input.isEmpty() || input.matches("^\\s*$") || input.equalsIgnoreCase("Hitler"));
    }

    /**
     * used to know if the input string is a valid ip, or a url
     *
     * @param input string
     * @return true if is valid ip or url
     */
    public static boolean isValidIp(String input) {
        Pattern p = Pattern.compile("^"
                + "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain name
                + "|"
                + "localhost" // localhost
                + "|"
                + "((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?))$"); // Ip

        return p.matcher(input).matches();
    }

    /**
     * used to validate the input port
     *
     * @param input string
     * @return true if the number is in the range specified in {@link Configuration}
     */
    public static boolean isValidPort(Integer input) {
        return (input >= Client.MIN_PORT && input <= Client.MAX_PORT);
    }

    /**
     * Method to call MILLISECONDS.sleep of {@link TimeUnit} and for manage his InterruptedException
     *
     * @param timeout milliseconds value requested for sleep
     */
    public static void doTimeUnitSleep(int timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout); //show user message 1 sec before wiping out
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

}
