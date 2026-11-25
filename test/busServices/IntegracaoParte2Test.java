package busServices;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testes INTERCLASSES dos cenarios D, E e F.
 *
 * Massa de dados: onibus com 2 poltronas LEITO e 3 PADRAO;
 * passagem LEITO = R$ 150,00 e PADRAO = R$ 80,00.
 */
public class IntegracaoParte2Test {

    private static final double DELTA = 0.000001d;

    private Onibus onibus;
    private Viagem viagem;

    @BeforeEach
    void setUp() {
        onibus = new Onibus("ABC1D23", "Paradiso G8", "Marcopolo", 3, 2);
        Calendar data = new GregorianCalendar(2026, Calendar.DECEMBER, 10, 8, 30);
        viagem = new Viagem("Campinas", "Sao Paulo", data, 150.0d, 80.0d, onibus);
    }

    private void ocupar(int leitos, int padrao) {
        for (int i = 1; i <= leitos; i++) {
            Passageiro passageiro = new Passageiro("Passageiro Leito " + i, "L" + i);
            assertTrue(viagem.getOnibus().adicionarPassageiro(passageiro, TipoDePoltrona.LEITO));
        }
        for (int i = 1; i <= padrao; i++) {
            Passageiro passageiro = new Passageiro("Passageiro Padrao " + i, "P" + i);
            assertTrue(viagem.getOnibus().adicionarPassageiro(passageiro, TipoDePoltrona.PADRAO));
        }
    }

    private double faturamentoPorTipo(TipoDePoltrona tipo) {
        Onibus onibusDaViagem = viagem.getOnibus();
        int ocupadas = onibusDaViagem.getCapacidade(tipo) - onibusDaViagem.getLugaresLivres(tipo);
        return ocupadas * viagem.getPrecoPassagem(tipo);
    }

    // Cenario D - quantidade de poltronas disponiveis

    @Test
    void d1_totalDisponivelEmOnibusVazio() {
        assertEquals(5, viagem.getOnibus().getLugaresLivres());
    }

    @Test
    void d2_disponibilidadePorTipoEmOnibusVazio() {
        assertAll(
                () -> assertEquals(2, viagem.getOnibus().getLugaresLivres(TipoDePoltrona.LEITO)),
                () -> assertEquals(3, viagem.getOnibus().getLugaresLivres(TipoDePoltrona.PADRAO)));
    }

    @Test
    void d3_disponibilidadeTotalEPorTipoEmOnibusParcial() {
        ocupar(1, 2);
        assertAll(
                () -> assertEquals(2, viagem.getOnibus().getLugaresLivres()),
                () -> assertEquals(1, viagem.getOnibus().getLugaresLivres(TipoDePoltrona.LEITO)),
                () -> assertEquals(1, viagem.getOnibus().getLugaresLivres(TipoDePoltrona.PADRAO)));
    }

    @Test
    void d4_disponibilidadeEmOnibusLotado() {
        ocupar(2, 3);
        assertAll(
                () -> assertEquals(0, viagem.getOnibus().getLugaresLivres()),
                () -> assertEquals(0, viagem.getOnibus().getLugaresLivres(TipoDePoltrona.LEITO)),
                () -> assertEquals(0, viagem.getOnibus().getLugaresLivres(TipoDePoltrona.PADRAO)));
    }

    // Cenario E - taxa de ocupacao

    @Test
    void e1_taxaGeralEmOnibusVazio() {
        assertEquals(0.0d, viagem.getTaxaOcupacao(), DELTA);
    }

    @Test
    void e2_taxaGeralEmOnibusParcial() {
        ocupar(1, 2);
        assertEquals(0.6d, viagem.getTaxaOcupacao(), DELTA);
    }

    @Test
    void e3_taxaPorTipoEmOnibusVazio() {
        assertAll(
                () -> assertEquals(0.0d, viagem.getTaxaOcupacao(TipoDePoltrona.LEITO), DELTA),
                () -> assertEquals(0.0d, viagem.getTaxaOcupacao(TipoDePoltrona.PADRAO), DELTA));
    }

    @Test
    void e4_taxaPorTipoEmOnibusParcial() {
        ocupar(1, 2);
        assertAll(
                () -> assertEquals(0.5d, viagem.getTaxaOcupacao(TipoDePoltrona.LEITO), DELTA),
                () -> assertEquals(2.0d / 3.0d, viagem.getTaxaOcupacao(TipoDePoltrona.PADRAO), DELTA));
    }

    @Test
    void e5_taxasEmOnibusLotado() {
        ocupar(2, 3);
        assertAll(
                () -> assertEquals(1.0d, viagem.getTaxaOcupacao(), DELTA),
                () -> assertEquals(1.0d, viagem.getTaxaOcupacao(TipoDePoltrona.LEITO), DELTA),
                () -> assertEquals(1.0d, viagem.getTaxaOcupacao(TipoDePoltrona.PADRAO), DELTA));
    }

    // Cenario F - faturamento

    @Test
    void f1_faturamentoEmOnibusVazio() {
        assertEquals(0.0d, viagem.calcularTotalFaturado(), DELTA);
    }

    @Test
    void f2_faturamentoEmOnibusParcial() {
        ocupar(1, 2);
        assertEquals(310.0d, viagem.calcularTotalFaturado(), DELTA);
    }

    @Test
    void f3_faturamentoEmOnibusLotado() {
        ocupar(2, 3);
        assertEquals(540.0d, viagem.calcularTotalFaturado(), DELTA);
    }

    @Test
    void f4_faturamentoPorTipoEmOnibusParcial() {
        ocupar(1, 2);
        double faturamentoLeito = faturamentoPorTipo(TipoDePoltrona.LEITO);
        double faturamentoPadrao = faturamentoPorTipo(TipoDePoltrona.PADRAO);

        assertAll(
                () -> assertEquals(150.0d, faturamentoLeito, DELTA),
                () -> assertEquals(160.0d, faturamentoPadrao, DELTA),
                () -> assertEquals(310.0d, faturamentoLeito + faturamentoPadrao, DELTA),
                () -> assertEquals(310.0d, viagem.calcularTotalFaturado(), DELTA));
    }
}
