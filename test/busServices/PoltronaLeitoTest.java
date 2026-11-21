package busServices;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Testes INTRACLASSE de Poltrona (particionamento por ESTADO: LIVRE / OCUPADA)
 * e de Leito (subclasse de Poltrona; estado LIVRE / OCUPADA + calculo de area especializado).
 */
public class PoltronaLeitoTest {

    private static final double DELTA = 0.000001d;

    // ================= POLTRONA =================

    @Test
    void p01_estadoLivre() { // Poltrona-getOcupante()-calcularArea()-getLargura()
        Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRAO, 2.0d, 3.0d, 6.0d);
        assertNull(p.getOcupante());
        assertEquals(6.0d, p.calcularArea(), DELTA);
        assertEquals(2.0d, p.getLargura(), DELTA);
    }

    @Test
    void p02_estadoOcupada() { // Poltrona-setOcupante()-calcularArea()-getOcupante()
        Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRAO, 1.0d, 3.0d, 6.0d);
        Passageiro pax = new Passageiro("Sr. Madruga", "12345678");
        p.setOcupante(pax);
        assertNotNull(p.getOcupante());
        assertSame(pax, p.getOcupante());
        assertEquals(3.0d, p.calcularArea(), DELTA);
    }

    @Test
    void p03_ocuparELiberar() { // Poltrona-setOcupante()-setOcupante(null)-getOcupante()
        Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRAO, 1.0d, 3.0d, 6.0d);
        p.setOcupante(new Passageiro("Chaves", "1111"));
        p.setOcupante(null);
        assertNull(p.getOcupante());
    }

    @Test
    void p04_trocaDeOcupante() { // Poltrona-setOcupante()-setOcupante()-getOcupante()
        Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRAO, 1.0d, 3.0d, 6.0d);
        Passageiro a = new Passageiro("Chaves", "1111");
        Passageiro b = new Passageiro("Kiko", "2222");
        p.setOcupante(a);
        p.setOcupante(b);
        assertSame(b, p.getOcupante());
    }

    @Test
    void p05_alterarDimensoesRecalculaArea() { // Poltrona-[setComprimento()|setLargura()]^n-calcularArea()-getOcupante()
        Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRAO, 1.0d, 1.0d, 1.0d);
        p.setComprimento(4.0d);
        p.setLargura(2.5d);
        assertEquals(10.0d, p.calcularArea(), DELTA);
        assertNull(p.getOcupante());
    }

    @Test
    void p06_alturaNaoAlteraArea() { // Poltrona-setAltura()-calcularArea()
        Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRAO, 2.0d, 3.0d, 6.0d);
        p.setAltura(10.0d);
        assertEquals(10.0d, p.getAltura(), DELTA);
        assertEquals(6.0d, p.calcularArea(), DELTA);
    }

    @Test
    void p07_identEtipo() { // Poltrona-setIdent()-setTipo()-getIdent()-getTipo()
        Poltrona p = new Poltrona("1A", TipoDePoltrona.PADRAO, 1.0d, 1.0d, 1.0d);
        p.setIdent("2B");
        p.setTipo(TipoDePoltrona.LEITO);
        assertEquals("2B", p.getIdent());
        assertEquals(TipoDePoltrona.LEITO, p.getTipo());
    }

    // ================= LEITO =================

    @Test
    void l01_construtorEHeranca() { // Leito-getIdent()-getTipo()-getLargura()-getComprimento()-getAltura()-getOcupante()
        Leito l = new Leito("01", TipoDePoltrona.LEITO, 0.5d, 2.0d, 1.2d, 45.0d);
        assertTrue(l instanceof Poltrona);
        assertEquals("01", l.getIdent());
        assertEquals(TipoDePoltrona.LEITO, l.getTipo());
        assertEquals(0.5d, l.getLargura(), DELTA);
        assertEquals(2.0d, l.getComprimento(), DELTA);
        assertEquals(1.2d, l.getAltura(), DELTA);
        assertNull(l.getOcupante());
    }

    @Test
    void l02_estadoOcupado() { // Leito-setOcupante()-getOcupante()
        Leito l = new Leito("01", TipoDePoltrona.LEITO, 0.5d, 2.0d, 1.2d, 45.0d);
        Passageiro pax = new Passageiro("Viviane Costa", "123456789");
        l.setOcupante(pax);
        assertSame(pax, l.getOcupante());
        l.setOcupante(null);
        assertNull(l.getOcupante());
    }

    @Test
    void l03_areaDoLeito() {
        // Leito-calcularArea(): area util = comprimento x largura (mesmo criterio de Poltrona).
        // 2.0 x 0.5 = 1.0 m2. O codigo atual multiplica tambem por altura e angulo.
        Leito l = new Leito("01", TipoDePoltrona.LEITO, 0.5d, 2.0d, 1.2d, 45.0d);
        assertEquals(1.0d, l.calcularArea(), DELTA);
    }

    @Test
    void l04_areaDoLeitoComPolimorfismo() { // Poltrona p = new Leito(); p.calcularArea() -> usa a versao do Leito
        Poltrona p = new Leito("02", TipoDePoltrona.LEITO, 0.5d, 0.5d, 1.2d, 180.0d);
        Poltrona comum = new Poltrona("03", TipoDePoltrona.PADRAO, 0.5d, 0.5d, 1.2d);
        assertEquals(comum.calcularArea(), p.calcularArea(), DELTA,
                "Leito e Poltrona com as mesmas dimensoes deveriam ter a mesma area util");
    }

    @Test
    void l05_settersDeDimensoesNoLeito() { // Leito-setComprimento()-setLargura()-getComprimento()-getLargura()
        Leito l = new Leito("01", TipoDePoltrona.LEITO, 0.5d, 2.0d, 1.2d, 45.0d);
        l.setComprimento(2.2d);
        l.setLargura(0.6d);
        assertEquals(2.2d, l.getComprimento(), DELTA);
        assertEquals(0.6d, l.getLargura(), DELTA);
    }
}
