package ie.ncirl.esta;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EstaApplication {
    public static void main(String[] args) throws MailjetSocketTimeoutException, MailjetException {
        SpringApplication.run(EstaApplication.class, args);
    }
}
