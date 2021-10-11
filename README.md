# Draw Function Call Graph With graphviz

----
## What is it? 
I extract functions from source file and create function call graph using graphViz. 
 

When You click "Create Graph", this method called.
```java
  public void drawGraph(String filePath, String fileName) {

        String textInFile = getFileText(filePath); // get text from source file.
        ArrayList<String> functions = getFunctions(textInFile); // get defined function
        HashMap<String,ArrayList<String>> functionMap = getFunctionMap(functions,textInFile);
        drawNode(functionMap,fileName); // draw graph
        
    }
```

This is my sample source file. ( Source code is my company's in-house front-end engine syntax. It's based on javascript. )
```javascript

scwin.a = function(a1, a2){
	...
	// a에대한 내용
	...
}


/* scwin.b()에 대한 주석 */
scwin.b = async function(b1, b2){
	// scwin.b에 대한 내용...
}

// scwin.c()에 대한 주석
scwin.c = function(){
	
	var c = scwin.a();/*scwin.a()에대한 주석*/
}

scwin.d = 
function(){
	var d = scwin.b(t,d);
	scwin.c();//scwin.c()에 대한 주석
}

scwin.e = 
function(){
	/*scwin.e()에 대한 주석*/
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
