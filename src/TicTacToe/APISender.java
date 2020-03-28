package TicTacToe;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.Response;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;

public class APISender {
	public static final String HOST = "http://www.notexponential.com/aip2pgaming/api/index.php";
    public static final String TEAMID = "1205";
    public static final String APIKEY = "9b392e42cf6813dafbed";
    public static final String USERID = "868";
    
    public static String GAMEID;
    
    protected final OkHttpClient httpClient = new OkHttpClient();
 
    
    public APISender(int GameId) {
    	this.GAMEID = Integer.toString(GameId);
    }
    
    public int[] getMove() throws IOException {
        Request request = new Request.Builder()
                .url(String.format("%s?%s%s", HOST, "type=moves&count=1&gameId=", GAMEID))
                .addHeader("x-api-key", APIKEY)
                .addHeader("userid", USERID)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Gson gson = new Gson();
            Move move = gson.fromJson(response.body().string(), Move.class);
            return move.getMove();
        }
    }
    
	 public boolean requestMove(int x, int y) throws IOException {

	        RequestBody body = new FormBody.Builder()
	                .add("gameId", GAMEID)
	                .add("type", "move")
	                .add("teamId", TEAMID)
	                .add("move", x + "," + y)
	                .build();

	        Request request = new Request.Builder()
	                .url(HOST)
	                .addHeader("x-api-key", APIKEY)
	                .addHeader("userid", USERID)
	                .post(body)
	                .build();

	        try (Response response = httpClient.newCall(request).execute()) {

	            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

	            // Get response body
	            System.out.println(response.body().string());
	        }

	        return true;
    }
    
    
}
