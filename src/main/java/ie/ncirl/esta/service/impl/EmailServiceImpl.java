package ie.ncirl.esta.service.impl;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import ie.ncirl.esta.service.EmailService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String API_KEY = "f5a77afd90422395d1fcbaaeebf3ab08";
    private static final String API_SECRET = "2c6eeae915521ccf35d1846ace2c07a9";
    private static final String CLIENT_VERSION = "v3.1";

    @Override
    public boolean sendVerificationEmail(String firstName, String lastName, String email, Integer confirmationCode) throws MailjetSocketTimeoutException, MailjetException {

        MailjetClient client = new MailjetClient(API_KEY, API_SECRET, new ClientOptions(CLIENT_VERSION));

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "igushkin.m@gmail.com")
                                        .put("Name", "Mark"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", email)
                                                .put("Name", firstName)))
                                .put(Emailv31.Message.SUBJECT, "Greetings from Mailjet.")
                                .put(Emailv31.Message.TEXTPART, "My first Mailjet email")
                                .put(Emailv31.Message.HTMLPART,
                                        String.format("<h3><a href='http://localhost:8080/therapists/'>Click to confirm</a></h3><br/> Thanks! Your conf code is %s", confirmationCode)))

                );

        MailjetResponse response;

        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());

        return response.getStatus() == 200;
    }
}
