package br.com.picpaysimplificado.services;

import br.com.picpaysimplificado.domain.user.User;
import br.com.picpaysimplificado.domain.user.UserType;
import br.com.picpaysimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuario do tipo lojista nao esta autorizado a realizar transacao");
        }

        if (sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findById(id).orElseThrow(() ->
                new Exception("Usuario nao encontrado")
        );
    }

    public void saveUser(User user) throws Exception {
        this.userRepository.save(user);
    }
}
