import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public interface Constants {
	
		
	//used by langListener to determine highlighting language
		String[] STYLECODES={
				"text/plain","text/actionscript",
				"text/asm","text/bbcode","text/c","text/clojure",
				"text/cpp","text/cs","text/css","text/delphi",
				"text/dtd","text/fortran","text/groovy","text/html",
				"text/java","text/javascript","text/jsp","text/latex",
				"text/lisp","text/lua","text/makefile","text/mxml",
				"text/perl","text/php","text/properties","text/python",
				"text/ruby","text/sas","text/scala","text/sql","text/tcl",
				"text/unix","text/bat","text/xml"
		};

		//used for supplying strings when creating JCheckBoxMenuItem
		 String[] CHECKMENUCODES= {
				"NONE",	"ACTIONSCRIPT",	"ASSEMBLER_X86","BBCODE","C",
				"CLOJURE","C++","C#",	"CSS","DELPHI","DTD",
				"FORTRAN","GROOVY",	"HTML",	"JAVA",	"JAVASCRIPT","JSP",
				"LATEX","LISP",	"LUA",	"MAKEFILE",	"MXML",	"PERL",
				"PHP","PROPERTIES_FILE","PYTHON","RUBY","SAS","SCALA","SQL",	
				"TCL",	"UNIX_SHELL","WINDOWS_BATCH","XML"
		};
		 
		 //there are only 33 extensions, nothing for plain
		 String[] EXTENSIONS={
				 "as","asm","bb","c","clj","cpp","cs","css","pas","dtd","f","groovy","html","java","js","jsp",
				 "latex","lisp","lua","makefile","mxml","perl","php","properties","python","ruby","sas","scala",
				 "sql","tcl","sh","bat","xml"
		 };
		 
		 
		 @SuppressWarnings("serial")
		Map<String, String> EXTCODES = 
		            Collections.unmodifiableMap(
		                    new HashMap<String, String>() {
		                        {
		                            for(int i=0;i<33;i++)
		                            	put(EXTENSIONS[i], STYLECODES[i+1]);
		                        }

		                    });

		
}
