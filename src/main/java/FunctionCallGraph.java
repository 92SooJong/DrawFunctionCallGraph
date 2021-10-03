import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *  Singleton
 */
public class FunctionCallGraph {

    private static FunctionCallGraph single_instance = null;

    private FunctionCallGraph(){

    }

    public static FunctionCallGraph getInstance() {
        if (single_instance == null)
            single_instance = new FunctionCallGraph();
        return single_instance;
    }

    public void drawGraph(String filePath){

        String allText = getFileText(filePath);
        System.out.println(allText);
        getFunctions(allText);


    }



    private String getFileText(String filePath){
        String allTextInFile = "";
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                allTextInFile += data;

            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return removeAllSpace(allTextInFile);

    }

    private String removeAllSpace(String text){

        text = text.replaceAll("\t" , "");
        text = text.replaceAll(" " , "");
        return text;
    }


    private HashMap<String, ArrayList<String>> getFunctions(String text){

        // 함수관계 추출
        // scwin.으로 시작하는 문자의 위치가져오기
        String[] texts = text.split("scwin.");
        for (String s : texts) {

            if (s.contains("=function")) {
                String[] functionNames = s.split("=function");
                System.out.println(functionNames[0]);

            }


        }








        return null;
    }


}
