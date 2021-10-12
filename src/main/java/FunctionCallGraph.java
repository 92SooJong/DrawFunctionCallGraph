import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;


public class FunctionCallGraph {


    public FunctionCallGraph() {
    }

    public void drawGraph(String filePath, String fileName) {

        String textInFile = getFileText(filePath); // 파일내의 Text를 가져온다
        ArrayList<String> functions = getFunctions(textInFile);
        HashMap<String,ArrayList<String>> functionMap = getFunctionMap(functions,textInFile);
        drawNode(functionMap,fileName);

    }




    private String getFileText(String filePath){
        StringBuilder allTextInFile = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();

            while (line != null) {
                // 한줄 주석 제거
                if( line.contains("//")){
                    line = line.substring(0,line.indexOf("//"));
                }

                allTextInFile.append(line);
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return removeNeedlessText(allTextInFile.toString());

    }


    private String removeNeedlessText(String text){



        // remove Multi line comment
        while(true){
            int commentStartIndex = text.indexOf("/*");
            int commentEndIndex = text.indexOf("*/");

            // No more Multi line comment
            if(commentStartIndex == -1 ) break;

            text = text.substring(0,commentStartIndex).concat(text.substring(commentEndIndex+2));


        }

        String result = text;
        result = result.replaceAll("async function" , "function"); // async 함수를 일반함수로
        result = result.replaceAll(" " , "");
        result = result.replaceAll("\t" , "");

        System.out.println("result = " + result);

        return result;
    }

    private ArrayList<String> getFunctions(String text){

        ArrayList<String> functions = new ArrayList<>();

        text = "}" + text;
        while (text.contains("=function(")) {

            // 1. 앞에서부터 "=function"의 위치를 구한다.
            int functionDefinitionIndex = text.indexOf("=function(");
            // 2. 1번에서 구한 "=function"의 위치를 끝으로 하는 문자열을 만든다.
            String textToFunction = text.substring(0, functionDefinitionIndex);
            // 3. 2번에서구한 문자열에서 가장 마지막 "}"의 위치를 구한다.
            int lastBraketIndex = textToFunction.lastIndexOf("}");
            // 4. 3번에서 구한 위치와 1번에서 구한 위치 사이에 있는 문자를 가져온다( 가져온 문자가 함수명이다! )
            textToFunction = textToFunction.substring(lastBraketIndex + 1, functionDefinitionIndex);
            functions.add(textToFunction);
            // 5. 다음 작업을 위해 1번에서 구한 위치의 이전 Text는 제거한다.
            text = text.substring(functionDefinitionIndex + "=function(".length());
        }

        return functions;


    }

    private HashMap<String, ArrayList<String>> getFunctionMap(ArrayList<String> functions,String textInFile) {

        HashMap<String,ArrayList<String>> result = new HashMap<>();

        for (int i=0; i<functions.size(); i++) {
            String functionName = functions.get(i);
            String functionDefinition = functionName.concat("=function(");
            int functionStartIndex = textInFile.indexOf(functionDefinition);
            String functionDefinitionAndBody;

            if(functions.size() > i+1){
                String nextFunctionDefinition = functions.get(i+1);
                int functionEndIndex = textInFile.indexOf(nextFunctionDefinition.concat("=function("));
                functionDefinitionAndBody = textInFile.substring(functionStartIndex,functionEndIndex);
            } else {
                functionDefinitionAndBody = textInFile.substring(functionStartIndex);
            }

            String body = functionDefinitionAndBody.substring(functionDefinitionAndBody.indexOf("{"),functionDefinitionAndBody.lastIndexOf("}")+1);

            System.out.println("body = " + body);
            ArrayList<String> callFunctions = new ArrayList<>();
            for(String function : functions){
                if(body.contains(function+"(")){
                    callFunctions.add(function);
                }
            }
            result.put(functionName , callFunctions);

        }

        System.out.println("result = " + result);
        return result;


    }


    private void drawNode(HashMap<String,ArrayList<String>> functionMap,String fileName){

        ArrayList<Node> nodes = new ArrayList<>();

        functionMap.forEach((key,functionList) ->{

            if(functionList.size() == 0){
                nodes.add(node(key));
            } else {

                for (String function : functionList) {
                    nodes.add(node(key).with(Color.BLACK).link(function));
                }
            }

        });

        Graph g = graph("example1").directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(nodes);

        try {
            Graphviz.fromGraph(g).height(600).render(Format.SVG).toFile(new File("C:/graph/" + fileName +"_graph.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}