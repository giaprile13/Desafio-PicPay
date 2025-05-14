package br.com.picpaysimplificado.services;

import br.com.picpaysimplificado.domain.user.User;
import br.com.picpaysimplificado.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificationRequest, String.class);

        if(!notificationResponse.getStatusCode().is2xxSuccessful()) {
            System.out.println("Erro ao enviar notificacao");
            throw new Exception("Servico de notificacao fora do ar");
        }

    }

}
