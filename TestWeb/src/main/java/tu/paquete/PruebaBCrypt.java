package tu.paquete;

import org.mindrot.jbcrypt.BCrypt;

public class PruebaBCrypt {
    public static void main(String[] args) {
        String password = "123456";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Contraseña: " + password);
        System.out.println("Hash: " + hash);
    }
}
