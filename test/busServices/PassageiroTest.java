package busServices;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PassageiroTest {

    @Test
    void testConstrutorEGetters() {
        Passageiro p = new Passageiro("Viviane Costa", "123456789");

        assertEquals("Viviane Costa", p.getNome());
        assertEquals("123456789", p.getRG());
    }

    @Test
    void testSetters() {
        Passageiro p = new Passageiro("Nome Inicial", "000000000");

        p.setNome("Carlos Silva");
        p.setRG("987654321");

        assertEquals("Carlos Silva", p.getNome());
        assertEquals("987654321", p.getRG());
    }
}