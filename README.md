# Draw Function Call Graph With graphviz


I extract functions(mostly using substring) from source file and create function call graph using graphViz. 
When You click "Create Graph", this method called.
```java
  public void drawGraph(String filePath, String fileName) {

        String textInFile = getFileText(filePath); // get text from source file.
        ArrayList<String> functions = getFunctions(textInFile); // get defined function
        HashMap<String,ArrayList<String>> functionMap = getFunctionMap(functions,textInFile);
        drawNode(functionMap,fileName); // draw graph
        
    }
```


This is my sample source file. Syntax is my company's in-house front-end solution. It's based on javascript.
Javascript is interpreter language so I extract function by using only `substring`, `indexof` and `lastindexof`
If you want to change the way of extracting functions and mapping functions, modify `getFunctions` and `getFunctionMap` method.
```javascript


scwin.a = function(a1, a2){
	...
	blah blah
	...
}


/* scwin.b()'s comment */
scwin.b = async function(b1, b2){
	// scwin.b blah blah
}

// scwin.c()'s comment
scwin.c = function(){
	
	var c = scwin.a(); /*scwin.a()'s comment*/
}

scwin.d = 
function(){
	var d = scwin.b(t,d);
	scwin.c();//scwin.c()'s comment
}

scwin.e = 
function(){
	/*scwin.e()'s comment*/
}

scwin.f = function(){
	scwin.a();
	scwin.b();
}

scwin.g = function(){
	scwin.f();
	scwin.i();
}

scwin.h = function(){
	scwin.b();
}

scwin.i = function(){
	scwin.h();
}
```




