package ie.ncirl.esta.service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

public interface EmailService {
    boolean sendVerificationEmail(String firstName, String lastName, String email, Integer confirmationCode) throws MailjetSocketTimeoutException, MailjetException;
}
