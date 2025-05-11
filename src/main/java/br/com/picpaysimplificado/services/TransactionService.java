package br.com.picpaysimplificado.services;

import br.com.picpaysimplificado.domain.transaction.Transaction;
import br.com.picpaysimplificado.domain.transaction.TransactionRepository;
import br.com.picpaysimplificado.domain.user.User;
import br.com.picpaysimplificado.dtos.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;

    public void createTransaction(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        userService.validateTransaction(sender, transactionDTO.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transactionDTO.value());
        if(!isAuthorized) {
            throw new Exception("Transacao nao autorizada");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));
        this.transactionRepository.save(transaction);
    }

    public boolean authorizeTransaction(User sender, BigDecimal value){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> authResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        if(authResponse.getStatusCode().is2xxSuccessful()){
            String message = (String) authResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;

     }
}
