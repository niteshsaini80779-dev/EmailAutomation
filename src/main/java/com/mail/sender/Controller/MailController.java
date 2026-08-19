package com.mail.sender.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mail.sender.Services.EmailService;
import com.mail.sender.dto.Email;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/email")
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(
            @RequestBody Email request) {

        // --------------------------------------------------
        // Validate request body
        // --------------------------------------------------

        if (request == null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Request body cannot be empty.");
        }

        // --------------------------------------------------
        // Validate receiver email
        // --------------------------------------------------

        String receiverEmail = request.getReceiverEmail();

        if (receiverEmail == null || receiverEmail.isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Receiver email is required.");
        }

        // Basic email format validation
        if (!isValidEmail(receiverEmail)) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Please provide a valid receiver email address.");
        }

        // --------------------------------------------------
        // Company name is OPTIONAL
        //
        // If companyName is provided:
        //     Company-specific email will be sent.
        //
        // If companyName is empty/null:
        //     Generic recruiter email will be sent.
        // --------------------------------------------------

        try {

            emailService.sendJobInquiryEmail(request);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                        "Email sent successfully to "
                        + receiverEmail
                    );

        } catch (MessagingException e) {

            /*
             * Email provider / SMTP related problem.
             *
             * Do not expose the actual exception message
             * to the frontend.
             */
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(
                        "Unable to send the email at the moment. "
                        + "Please try again later."
                    );

        } catch (Exception e) {

            /*
             * Unexpected server-side error.
             *
             * Do not expose internal exception details
             * to the client.
             */
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        "An unexpected error occurred while "
                        + "processing your request."
                    );
        }
    }

    /**
     * Basic email format validation.
     */
    private boolean isValidEmail(String email) {

        return email.matches(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }
}