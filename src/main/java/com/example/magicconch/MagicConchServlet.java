package com.example.magicconch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

/**
 * Servlet implementation for the MagicConchServlet class.
 * 
 * The doPost() method checks for a /magicconch prefix in the message text and
 * responds with random message by sending a POST request to GroupMe.
 * 
 * The doGet() method returns a default exclamatory string. No messages are sent
 * by this method.
 *
 * @author Ethan Ho
 */
@WebServlet("/")
public class MagicConchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String GROUPME_BOT_POST_URL = 
            "https://api.groupme.com/v3/bots/post";

    private static final String[] CONCH_RESPONSES = {
            "Maybe someday.", "Nothing.", "Neither", "I don\'t think so.",
            "Yes.", "No.", "Try asking again."
    };

    /**
     * Default constructor.
     */
    public MagicConchServlet() {
        super();
    }

    /**
     * Processes a POST request by extracting the given bot ID and message
     * then sending a GroupMe message.
     * 
     * @param request       the request object
     * @param response      the response object
     */
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Extract request information
        final JSONObject REQUEST_BODY_JSON = getRequestBodyJSON(request);

        // Response information
        final String BOT_ID = System.getenv("groupme_bot_id");
        final String MESSAGE = REQUEST_BODY_JSON.getString("text");
        final String SENDER_ID = REQUEST_BODY_JSON.getString("sender_id");

        // Make sure the message did not come from the bot
        if (SENDER_ID.equals(BOT_ID)) return;

        // Send the message (if applicable)
        if (wantsMagicConch(MESSAGE)) {
            String conchResponse = generateRandomResponse();
            HttpResponse httpResponse = sendMessage(BOT_ID, conchResponse);
            System.out.println(httpResponse);
        }
    }

    /**
     * Returns the request body as a JSONObject.
     * 
     * @param request       a HttpServletRequest object
     * @return a JSONObject representing the request content
     * @throws IOException  if an issue occurs while reading in the content
     */
    private static JSONObject getRequestBodyJSON(HttpServletRequest request)
            throws IOException {
        // Storage variables
        StringBuffer jb = new StringBuffer();
        String line = null;

        // Read in the entire content
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            jb.append(line);
        }

        return new JSONObject(jb.toString());
    }

    /**
     * Returns whether or not a message starts with the string /magicconch.
     * 
     * @param message       the input message
     * @return true if the message starts with the substring and false else
     */
    private static boolean wantsMagicConch(String message) {
        return message.startsWith("/magicconch");
    }

    /**
     * Generates a random response from the magic conch.
     * 
     * @return a random magic conch response
     */
    private static String generateRandomResponse() {
        int numResponses = CONCH_RESPONSES.length;
        int randomIndex = (int) (Math.random() * numResponses);
        return CONCH_RESPONSES[randomIndex];
    }

    /**
     * Sends a GroupMe message using a given bot ID and message string.
     * 
     * The message is sent using a POST request.
     * 
     * @param BOT_ID        the ID of the bot
     * @param MESSAGE       the message to send
     * @return an HttpResponse result representing the POSTed GroupMe message.
     * 
     * @throws ClientProtocolException - in case of an http protocol error
     * @throws IOException- in case of a problem or the connection was aborted
     */
    private static HttpResponse sendMessage(
            final String BOT_ID, final String MESSAGE)
            throws ClientProtocolException, IOException {
        // Create a default ClosableHttpClient object
        HttpClient httpClient = HttpClients.createDefault();

        // Create the POST request
        HttpPost httpPost = new HttpPost(GROUPME_BOT_POST_URL);
        httpPost.setHeader("Content-Type", "application/json");
        try {
            httpPost.setURI(new URI(GROUPME_BOT_POST_URL)); // Set the POST URI
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Construct JSON message payload
        JSONObject json = new JSONObject();
        json.put("bot_id", BOT_ID);
        json.put("text", MESSAGE);

        // Add the payload to the POST request
        String responseBody = json.toString();
        StringEntity stringEntity = new StringEntity(responseBody);
        httpPost.setEntity(stringEntity); // Associate entity with the request

        // Execute the POST request
        HttpResponse httpResponse = httpClient.execute(httpPost);
        return httpResponse;
    }

    /**
     * Sets the GET request response content.
     * 
     * The response content says "All hail the Magic Conch!"
     * 
     * @param request       the request object
     * @param response      the response object
     */
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        pw.write("All hail the Magic Conch!");
    }

}