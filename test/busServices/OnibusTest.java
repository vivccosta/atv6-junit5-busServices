package busServices;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OnibusTest {

    private Onibus onibus;

    @BeforeEach
    void setUp() {
        // Construtor real: placa, modelo, marca, numPoltronasPadrão, numLeitos
        // Id 01 e 02 = LEITO; Id 03, 04 e 05 = PADRÃO
        onibus = new Onibus("ABC1D23", "Paradiso G8", "Marcopolo", 3, 2);
    }

    @Test
    void testEstadoVazio() { // Sequência 01
        assertEquals("ABC1D23", onibus.getPlaca());
        assertEquals("Paradiso G8", onibus.getModelo());
        assertEquals("Marcopolo", onibus.getMarca());

        assertEquals(5, onibus.getCapacidade());
        assertEquals(3, onibus.getCapacidade(TipoDePoltrona.PADRAO));
        assertEquals(2, onibus.getCapacidade(TipoDePoltrona.LEITO));

        assertEquals(5, onibus.getLugaresLivres());
        assertEquals(3, onibus.getLugaresLivres(TipoDePoltrona.PADRAO));
        assertEquals(2, onibus.getLugaresLivres(TipoDePoltrona.LEITO));

        assertNull(onibus.getPassageiroDaPoltrona("01"));
    }

    @Test
    void testEstadoParcialmenteOcupado() { // Sequência 02
        Passageiro p1 = new Passageiro("Viviane Costa", "111222333");
        Passageiro p2 = new Passageiro("Carlos Silva", "444555666");

        assertTrue(onibus.adicionarPassageiro(p1, TipoDePoltrona.LEITO));   // Ocupa poltrona "01"
        assertTrue(onibus.adicionarPassageiro(p2, TipoDePoltrona.PADRAO));  // Ocupa poltrona "03"

        assertEquals(3, onibus.getLugaresLivres());
        assertEquals(1, onibus.getLugaresLivres(TipoDePoltrona.LEITO));
        assertEquals(2, onibus.getLugaresLivres(TipoDePoltrona.PADRAO));

        assertEquals(p1, onibus.getPassageiroDaPoltrona("01"));
        assertEquals(p2, onibus.getPassageiroDaPoltrona("03"));
        assertNull(onibus.getPassageiroDaPoltrona("02")); // Poltrona livre

        // Testes das consultas de poltrona por passageiro (tendem a lançar NullPointerException se houver poltrona vaga antes/depois)
        assertEquals("01", onibus.consultarPoltronaDePassageiro("Viviane Costa").getIdent());
        assertEquals("03", onibus.consultarPoltronaDePassageiro(p2).getIdent());
    }

    @Test
    void testEstadoLotadoETentativaExtra() { // Sequência 03
        // Preenche as 2 poltronas LEITO ("01" e "02")
        assertTrue(onibus.adicionarPassageiro(new Passageiro("Pax Leito 1", "101"), TipoDePoltrona.LEITO));
        assertTrue(onibus.adicionarPassageiro(new Passageiro("Pax Leito 2", "102"), TipoDePoltrona.LEITO));

        // Tenta adicionar outro LEITO quando só os leitos acabaram
        assertFalse(onibus.adicionarPassageiro(new Passageiro("Pax Leito Extra", "103"), TipoDePoltrona.LEITO));

        // Preenche as 3 poltronas PADRÃO ("03", "04" e "05")
        assertTrue(onibus.adicionarPassageiro(new Passageiro("Pax Padrao 1", "201"), TipoDePoltrona.PADRAO));
        assertTrue(onibus.adicionarPassageiro(new Passageiro("Pax Padrao 2", "202"), TipoDePoltrona.PADRAO));
        assertTrue(onibus.adicionarPassageiro(new Passageiro("Pax Padrao 3", "203"), TipoDePoltrona.PADRAO));

        // Verifica estado LOTADO
        assertEquals(0, onibus.getLugaresLivres());
        assertEquals(0, onibus.getLugaresLivres(TipoDePoltrona.LEITO));
        assertEquals(0, onibus.getLugaresLivres(TipoDePoltrona.PADRAO));

        // Tenta adicionar passageiro com ônibus totalmente lotado
        assertFalse(onibus.adicionarPassageiro(new Passageiro("Pax Sem Vaga", "999"), TipoDePoltrona.PADRAO));
    }

    @Test
    void testSettersIdentificacao() { // Sequência 04
        onibus.setPlaca("XYZ9W87");
        onibus.setModelo("Vissta Buss DD");
        onibus.setMarca("Busscar");

        assertEquals("XYZ9W87", onibus.getPlaca());
        assertEquals("Vissta Buss DD", onibus.getModelo());
        assertEquals("Busscar", onibus.getMarca());
    }
}