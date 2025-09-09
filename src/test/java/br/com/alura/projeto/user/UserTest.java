package br.com.alura.projeto.user;

import br.com.alura.projeto.util.EncryptUtil;
import org.junit.jupiter.api.Test;

import static br.com.alura.projeto.user.Role.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void password__should_be_encrypted_to_md5() {
        User user = new User("Charles", "charles@alura.com.br", STUDENT, "mudar123");
        assertThat(user.getPassword())
                .isEqualTo(EncryptUtil.toMD5("mudar123"));
    }

}