import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailService {

    fun sendEmail(to: String, subject: String, message: String, callback: (Boolean) -> Unit) {

        Thread {
            try {
                val props = Properties().apply {
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.socketFactory.port", "465")
                    put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.port", "465")
                }

                val session = Session.getDefaultInstance(props,
                    object : javax.mail.Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(
                                "hrasiya212@rku.ac.in",
                                "rvjs aguo vxgl thuu"   // *** Not Gmail password, use App Password ***
                            )
                        }
                    }
                )

                val mimeMessage = MimeMessage(session)
                mimeMessage.setFrom(InternetAddress("hrasiya212@rku.ac.in"))
                mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(to))
                mimeMessage.subject = subject
                mimeMessage.setText(message)

                Transport.send(mimeMessage)
                callback(true)

            } catch (e: Exception) {
                e.printStackTrace()
                callback(false)
            }
        }.start()
    }
}
