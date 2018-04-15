import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;


public class RegEx {


    public static void main(String[] args) {
        System.out.print("Input name of file: ");
        Scanner in = new Scanner(System.in);
        String filename = in.next();

        readInput("./src/"+filename);
    }

    private static void readInput(String filename) {
        FileInputStream fstream;
        DataInputStream input;
        BufferedReader buff;
        String answer;
        try {
            buff = new BufferedReader(
                   new InputStreamReader(
                   new DataInputStream(
                   new FileInputStream(filename))));


            answer = fileParsing(buff);
        } catch (IOException er) {
            System.err.println(er);
            return;
        }
//        System.out.println("============= Return value =============");
//        System.out.println(answer);
    }

    private static String fileParsing(BufferedReader buff) throws IOException {
        ArrayList<String> expressions_list = new ArrayList<>();
        String strLine = buff.readLine();
        String return_string = "";
        int total_num_of_cases = Integer.parseInt(strLine);

        String current_expression = buff.readLine();

        System.out.println(total_num_of_cases + " " + current_expression);

        while(current_expression != null) {
            String regex_pattern = current_expression;

            int num_of_succeeding_expressions = 0;

            try {
                num_of_succeeding_expressions = Integer.parseInt(buff.readLine().replaceAll("\\s+",""));
            } catch(NumberFormatException er) {
                // handles input that are not numeric
                break;
            } catch(NullPointerException er) {
                // handles end of input stream
                break;
            }

            while(num_of_succeeding_expressions > 0) {
                current_expression = buff.readLine();
                System.out.println(current_expression);

                if(current_expression == null) break;

                if(verify_regex_match(regex_pattern.replaceAll("\\s+",""), current_expression)) {
                    return_string += "yes\n";
                } else {
                    return_string += "no\n";
                }

                num_of_succeeding_expressions--;
            }
            current_expression = buff.readLine();
        }

        return return_string;
    }

    private static boolean verify_regex_match(String pattern, String string_to_check) {
//        System.out.println("Pattern: " + pattern + "; string: " + string_to_check);
        String[] clean_pattern_set = disconfigure(pattern);

//        System.out.println("Pattern set size: " + clean_pattern_set.length);
        for(int i = 0; i < clean_pattern_set.length; i++) {
            System.out.println(clean_pattern_set[i]);
            if(validate_candidate(clean_pattern_set[i], string_to_check)) {
                return true;
            }
        }
        System.out.println();
        return false;
    }



    private static String[] disconfigure(String pattern) {
        char current_element;
        Stack<ArrayList<String>> context = new Stack<>();
        Stack<Integer> group = new Stack<>();
        ArrayList<String> ret_set = new ArrayList<>();
        ArrayList<String> current_context = new ArrayList<>();

        {
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add("");
            context.push(tmp);
        }

        Stack<Integer> edit_context = new Stack<>();
        edit_context.push(0);
        edit_context.push(0);

        int context_level = 0;
        int edit_start = 0, edit_end = 0;
        int group_start = 0, group_end = 0;

        ret_set.add("");

        for(int i = 0; i < pattern.length(); i++) {
            current_element = pattern.charAt(i);

            if(current_element == '*') {
                if(pattern.charAt(i-1) == ')') {
                    String[] sub_res_set = disconfigure(pattern.substring(group_start, group_end));
                    for(int j = 0; j < ret_set.size(); j++) {
                        ret_set.set(j, ret_set.get(j).substring(0, ret_set.get(j).length() - sub_res_set[j].length()) + "(" + sub_res_set[j] + ")*");
                    }
                } else {
                    for(int j = 0; j < current_context.size(); j++) {
                        current_context.set(j, current_context.get(j) + '*');
                    }
                    for(int j = edit_start; j <= edit_end; j++) {
                        ret_set.set(j, ret_set.get(j) + '*');
                    }
                }

            } else if(current_element == 'U') {
                edit_start = ret_set.size();

                current_context = (ArrayList<String>)context.pop().clone();
//                System.out.println("Current context: " + current_context.get(0));
                ret_set.addAll((ArrayList<String>)current_context.clone());

                edit_end = ret_set.size() - 1;

//                System.out.println("ES: " + edit_start + "; EE: " + edit_end);
                edit_context.push(edit_start);
                edit_context.push(edit_end);

            } else if(current_element == '(') {
                context_level++;
                group.push(i);
                context.push((ArrayList<String>)ret_set.clone());
            } else if(current_element == ')') {
                context_level--;
                group_start = group.pop();
                group_end = i;
                if(!edit_context.isEmpty()) {
                    edit_end = edit_context.pop();
                    edit_start = edit_context.pop();
                }

            } else  /*This is a character from alphabet sigma, hopefully*/
                {
                    for(int j = 0; j < current_context.size(); j++) {
                        current_context.set(j, current_context.get(j) + Character.toString(current_element));
                    }

                    for(int j = edit_start; j <= edit_end; j++) {
                        ret_set.set(j, ret_set.get(j) + Character.toString(current_element));
                    }
            }
        }

        String[] ret = new String[ret_set.size()];
        for(int i = 0; i < ret_set.size(); i++) {
            ret[i] = ret_set.get(i);
        }

        return ret;
    }

    private static boolean validate_candidate(String pattern, String string_to_check) {
        int current_position = 0;
        for(int i = 0; current_position < pattern.length() && i < string_to_check.length(); i++) {

        }
        return false;
    }

    private static boolean simple_pattern_matching(String pattern, String string_to_check) {
        if(pattern.length() != string_to_check.length())
            return false;

        for(int i = 0; i < pattern.length() && i < string_to_check.length(); i++) {
            if(pattern.charAt(i) != string_to_check.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    private static boolean indefinite_pattern_matching(String pattern, String string_to_check) /*kleene */{
        return false;
    }
}
