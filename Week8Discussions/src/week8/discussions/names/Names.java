package week8.discussions.names;

import java.util.HashMap;
import java.util.Map;

public class Names { 

	   private Map<String, String> m =new HashMap<String, String>(); 

	   public Names() { 

	      m.put("Mickey","Mouse"); 

	      m.put("Mickey","Mantle"); 

	   }

	   public int size() {

	      return m.size();

	   }

	   public static void main(String args []) { 

	      Names names =new Names(); 

	      System.out.println(names.size()); 

	   } 

	}