

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

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
        HashMap<String, ArrayList<String>> functionMap = getFunctions(allText);
        drawNode(functionMap);


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

        System.out.println("allTextInFile = " + allTextInFile);
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
        ArrayList<String> functionList = new ArrayList<>();
        for (String s : texts) {
            // 함수 목록을 가져온다
            if (s.contains("=function")) {
                String[] functionNames = s.split("=function");
                functionList.add(functionNames[0]);
            }
        }

        // 해당 함수의 내용을 가져오기
        System.out.println("functionList = " + functionList);
        HashMap<String,String> bodyByFunction = new HashMap<>();
        for(int i=0; i<functionList.size(); i++){

            String definitionFunction = "scwin." + functionList.get(i) + "=function(";
            String body ="";
            if(i < functionList.size()-1){
                String nextFunction = "scwin." + functionList.get(i+1) + "=function(";
                int start = text.indexOf(definitionFunction);
                int end = text.indexOf(nextFunction);
                body = text.substring(start,end);
            } else {

                int start = text.indexOf(definitionFunction);
                body = text.substring(start);
            }

            body = body.replaceAll( "scwin." + functionList.get(i) , "");
            bodyByFunction.put(functionList.get(i) , body);

        }

        HashMap<String,ArrayList<String>> result = new HashMap<>();
        bodyByFunction.forEach((key,value) ->{
            result.put(key, new ArrayList<>());
            System.out.println("value = " + value);
            for(String functionName : functionList){
                if(value.contains("scwin."+functionName)){
                    System.out.println("functionName = " + functionName);
                    result.get(key).add(functionName);
                }
            }

        });

        System.out.println(result);

        return result;
    }

    private void drawNode(HashMap<String,ArrayList<String>> functionMap){

        ArrayList<Node> aaa = new ArrayList<>();

        functionMap.forEach((key,functionList) ->{

            if(functionList.size() == 0){
                aaa.add(node(key));
            } else {
                for (String function : functionList) {
                    aaa.add(node(key).with(Color.RED).link(function));
                }
            }

        });


//        aaa.add(node("a").with(Color.RED).link(node("b")));
//        aaa.add(node("b").link(
//                to(node("c")).with(attr("weight", 5), Style.DASHED)
//        ));


        Graph g = graph("example1").directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(aaa);

        try {
            Graphviz.fromGraph(g).height(100).render(Format.SVG).toFile(new File("C:/graph/ex1.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
