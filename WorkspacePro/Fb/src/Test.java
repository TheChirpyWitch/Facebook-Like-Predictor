import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.json.JsonObject;
import com.restfb.types.Post;
import com.restfb.types.Post.Likes;
import com.restfb.types.User;
import com.restfb.Connection;

/**
 * Servlet implementation class Test
 */

public class Test  {
	public static String APP_ID = "198371123832208";
    public static String APP_SECRET = "7ce0a20324dea3e5165d2938da3fed92";
    private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("shvsjskj");
		//String code = request.getParameter("code");
		// System.out.println("shvskshwvffwje");
		//  String URLEncodedRedirectURI = URLEncoder.encode("http://localhost:8080/Potato/Test");
		  String MY_ACCESS_TOKEN = "CAACEdEose0cBALfX7V2Gmmy87Fh19UsJEVZCv6TOj9jfZBXFFK1wZACFiUwl7ReIwFldCaNseeBOHo6tXJnWhF2phWcMQlf6N5aWZBcVH1F1uXZB2pRTyCZCjFoBG9Q7TZAEb3YoHhBPzF27iMlefGoqtaHthsZA6ncbZAGZBfOzpMXlm85e0uwENcDZBsckMnUvtJtiYaZCRfhT03bDd2frdxAM";
		 
//		  String authURL = "https://graph.facebook.com/oauth/access_token?" +
//		                "client_id=" + Test.APP_ID + "&" +
//		                "redirect_uri=" + URLEncodedRedirectURI + "&" +
//		                "client_secret=" + Test.APP_SECRET + "&" +
//		                "code=" + code;
//		 
//		  URL url = new URL(authURL);
//		 
//		  String result = readURL(url);
//		  String[] pairs = result.split("&");
//		 
//		  for (String pair : pairs) {
//		 
//		   String[] kv = pair.split("=");
//		   if (kv[0].equals("access_token")) {
//		    MY_ACCESS_TOKEN = kv[1];
//		   }
//		  }
		  HashMap<Post, Integer> postVsLikeList = new HashMap<Post, Integer>();
		  HashMap<Post, ArrayList<String>> postVsFriendsLikeList = new HashMap<Post, ArrayList<String>>();
		  HashMap<String, Integer> friendsVsLikeCountList = new HashMap<String, Integer>();
		  HashMap<String, ArrayList<String>> friendsVsWordsList = new HashMap<String, ArrayList<String>>();
		  HashMap<Post, ArrayList<String>> postVsProminentWords = new HashMap<Post, ArrayList<String>>();
		  ArrayList<String> stopwords = new ArrayList();
	        FileInputStream fstream;
	        try {
	            fstream = new FileInputStream("src/stop-words_english_1_en.txt");
	            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	            String strLine;
	            while ((strLine = br.readLine()) != null)   {
	                  // Print the content on the console
	                  stopwords.add(strLine);
	                }
	            //System.out.println(stopwords);
	            br.close();
	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		 int j;
		  
		  Connection<Post> myFeed;
		  
		  try {
			  FacebookClient facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
		            User loginUser = facebookClient.fetchObject("me", User.class);
		            //request.setAttribute("loginUser", loginUser);
		            //friends = facebookClient.fetchConnection("/me/friends", User.class);
		            myFeed = facebookClient.fetchConnection(loginUser.getId() + "/feed", Post.class, Parameter.with("fields", "likes.limit(150),comments.summary(true),type,with_tags,updated_time,shares,picture,message_tags,name,description,message,link"));
		            
		            //JsonObject p = facebookClient.fetchObject("me/photos", JsonObject.class);
		            JsonObject mj = facebookClient.fetchObject("me/posts", JsonObject.class);
		            String firstPhotoUrl = mj.getJsonArray("data").getJsonObject(0).toString();
		            //System.out.println(firstPhotoUrl);
		            //System.out.println(mj);
		            //response.getWriter().append("Served at: ").append(facebookClient.fetchObject("me",User.class).getName());
		            ArrayList<String> Words = new ArrayList<String>();
		            //ArrayList<String> words = new ArrayList<String>();
		            for(List<Post> plist: myFeed)
		            {
		            	//System.out.println(plist.size());
		  			  for(Post post: plist){
		  				  
		  				//System.out.println("------------------------------------------------------------------");
		  				  if(post.getLikes()!=null){
		  					String s = post.getLikes().getData().toString();
		  					List<String> friendslike= new ArrayList<String>(Arrays.asList(s.split(",")));
		  					for(String friend: friendslike) {
		  						if(!friendsVsLikeCountList.containsKey(friend)) {
		  							friendsVsLikeCountList.put(friend, 1);
		  						}
		  						else {
		  							int val = friendsVsLikeCountList.get(friend);
		  							friendsVsLikeCountList.put(friend, val+1);

		  						}
		  					}
		  					postVsFriendsLikeList.put(post, (ArrayList<String>) friendslike);
		  					if(post.getMessage()!=null) {
		  					//System.out.println((post.getLikes().getData()));
		  					String des = post.getMessage().toString().toLowerCase();
		  					//System.out.println(des);
		  					
		  		            List<String> parts= new ArrayList<String>(Arrays.asList(des.split(" ")));
		  		          List<String> words= new ArrayList<String>(Arrays.asList(des.split(" ")));
		  		            for(String p:(ArrayList<String>)parts){
		  		            	//System.out.println(post.getMessage());
		  		                for(String s1:stopwords){
		  		                	//System.out.println(s1);
		  		                    if(p.equals(s1)){
		  		                    	//System.out.println(p+"yuy");
		  		                        words.remove(p);
		  		                    }
		  		                }
		  		            }
		  					
		  					
		  					for(String friend: friendslike) {
		  						if(!friendsVsWordsList.containsKey(friend)) {
		  							Words = (ArrayList<String>)words;
		  							friendsVsWordsList.put(friend, Words);
		  						}
		  						else {
		  							//int val = friendsVsLikeCountList.get(friend);
		  							Words = friendsVsWordsList.get(friend);
		  							Words.addAll(words);
		  							friendsVsWordsList.put(friend, Words);

		  						}
		  					}
		  					int count=0; 						//LikeCount
		  					int i=0;
		  					while(i<s.length()) {
		  						if(s.charAt(i) == ',') {
		  							count++;
		  						}
		  						i++;
		  					}
		  					 
		  		            postVsProminentWords.put(post, (ArrayList<String>) Words);
		  					//String[] parts = s.split(",");
		  					//Connection<Likes> like;
		  					//like = facebookClient.fetchConnection(post.getId(), Likes.class, Parameter.with("fields", "total_count"));
		  					//System.out.println(postVsProminentWords);
		  					postVsLikeList.put(post, count);
		  					//postVsFriendsLikeList.put(post, (ArrayList<String>) friendslike);
		  					//Long x = post.getLikesCount();
		  					  System.out.println(postVsLikeList);
		  					 
		  					  }
		  				  }
		  				  
		  			  }
		  			  }
		  		  
		  } catch (FacebookException e) {
		    e.printStackTrace();
		  }
		  double p1=0,p2=0,p3=0,p4=0;
		  for(Post p: postVsLikeList.keySet()){
			  p4=p4+(postVsLikeList.get(p)*postVsLikeList.get(p));
		  }
		  double prec = Math.sqrt(p4/postVsLikeList.size());
		  System.out.println("------------"+prec+"-------------");
		  int c,x,z;
		  
		  for(Post p: postVsProminentWords.keySet()){
			  p3=0;
			  for(String s4: postVsProminentWords.get(p)){
				  p2=1;
				  for(String f: friendsVsWordsList.keySet()){
					  c=0;
					  for(String w: friendsVsWordsList.get(f)){
						  if(s4.equals(w)){
							  //p1=0.9;
							  c++;
						  }
					  }
					  if((c<=2)&&(c>=1)){
						  p2=(0.7)*p2;
					  }
					  else if((c>=3)){
						  p2=0.8*p2;
					  }
					  
				  }
				  p3=p3+p2;
			  }
			  
			 double val3 = postVsLikeList.get(p);
			 double val = prec;
			 double val2 = 0;
			 val2 = prec*p3;
			
			 //System.out.println("******"+val2);
				 for(String f1: friendsVsLikeCountList.keySet()){
					 //System.out.println(f1+"   "+friendsVsLikeCountList.get(f1));
					 if((friendsVsLikeCountList.get(f1)>2)&&(friendsVsLikeCountList.get(f1)<5)){
						 val=0.7*val;
					 }
					 else if(friendsVsLikeCountList.get(f1)>12){
						 val=1*val;
					 }
					 else if(friendsVsLikeCountList.get(f1)>5){
						 val=0.8*val;
					 }
				 }
				 val = val*100000;
				 val = (val+val2)/2;
				 
			// System.out.println("Type is         "+p.getType());
			 
			 if(val>200){
				 val/=val/100;
			 }
			 while(val<1){
				 val=val*10;
			 }
			 if(p.getType().equals("photo")){
					// System.out.println("Prediction in pic:   "+val);
					  val=val*15;
			      }
			 System.out.println("Predicted likes are: "+val+"\n"+"Actual Likes are "+val3+"\n");

			/**
			 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
			 */
		  }
		 
		 // getServletConfig().getServletContext().getRequestDispatcher("/FriendsList.jsp").forward(request, response);
		//response.getWriter().append("Served at: ").append("sdhdu");
	}
}