package busServices;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testes INTRACLASSE da classe Viagem - particionamento por CATEGORIA (de metodos).
 *
 * Categoria A - Dados da viagem (get/set de origem, destino, data, onibus)
 * Categoria B - Precos (getPrecoLeito, getPrecoPassagem, setPrecoPassagem)
 * Categoria C - Calculos (calcularTotalFaturado, getTaxaOcupacao geral e por tipo)
 *
 * Massa de dados: onibus com 3 poltronas PADRAO (ids 03,04,05) e 2 LEITO (ids 01,02);
 * tarifa LEITO = R$ 150,00 e tarifa PADRAO = R$ 80,00.
 */
public class ViagemTest {

    private static final double DELTA = 0.000001d;

    private Onibus onibus;
    private Viagem viagem;
    private Calendar data;

    @BeforeEach
    void setUp() {
        onibus = new Onibus("ABC1D23", "Paradiso G8", "Marcopolo", 3, 2);
        data = new GregorianCalendar(2026, Calendar.DECEMBER, 10, 8, 30);
        // Viagem(origem, destino, data, precoLeito, precoPadrao, onibus)
        viagem = new Viagem("Campinas", "Sao Paulo", data, 150.0d, 80.0d, onibus);
    }

    // ------------------------------------------------------------------
    // CATEGORIA A - Dados da viagem
    // ------------------------------------------------------------------

    @Test
    void v01_construtorEGettersDeDados() { // Seq 01: Viagem-getOrigem()-getDestino()-getDataViagem()-getOnibus()
        assertEquals("Campinas", viagem.getOrigem());
        assertEquals("Sao Paulo", viagem.getDestino());
        assertSame(data, viagem.getDataViagem());
        assertSame(onibus, viagem.getOnibus());
    }

    @Test
    void v02_settersDeDados() { // Seq 02: Viagem-setOrigem()-setDestino()-setDataViagem()-getOrigem()-getDestino()-getDataViagem()
        Calendar novaData = new GregorianCalendar(2027, Calendar.JANUARY, 5, 22, 0);
        viagem.setOrigem("Valinhos");
        viagem.setDestino("Rio de Janeiro");
        viagem.setDataViagem(novaData);
        assertEquals("Valinhos", viagem.getOrigem());
        assertEquals("Rio de Janeiro", viagem.getDestino());
        assertSame(novaData, viagem.getDataViagem());
    }

    @Test
    void v03_trocaDeOnibus() { // Seq 03: Viagem-setOnibus()-getOnibus()-[getCapacidade()]
        Onibus outro = new Onibus("XYZ9W87", "Vissta Buss DD", "Busscar", 10, 4);
        viagem.setOnibus(outro);
        assertSame(outro, viagem.getOnibus());
        assertEquals(14, viagem.getOnibus().getCapacidade());
    }

    // ------------------------------------------------------------------
    // CATEGORIA B - Precos
    // ------------------------------------------------------------------

    @Test
    void v04_consultaPrecosIniciais() { // Seq 04: Viagem-getPrecoLeito()-getPrecoPassagem(LEITO)-getPrecoPassagem(PADRAO)
        assertEquals(150.0d, viagem.getPrecoLeito(), DELTA);
        assertEquals(150.0d, viagem.getPrecoPassagem(TipoDePoltrona.LEITO), DELTA);
        assertEquals(80.0d, viagem.getPrecoPassagem(TipoDePoltrona.PADRAO), DELTA);
    }

    @Test
    void v05_alterarPrecoLeito() { // Seq 05: Viagem-setPrecoPassagem(LEITO)-getPrecoPassagem(LEITO)-getPrecoPassagem(PADRAO)
        viagem.setPrecoPassagem(200.0d, TipoDePoltrona.LEITO);
        assertEquals(200.0d, viagem.getPrecoPassagem(TipoDePoltrona.LEITO), DELTA);
        assertEquals(80.0d, viagem.getPrecoPassagem(TipoDePoltrona.PADRAO), DELTA); // PADRAO nao pode mudar
    }

    @Test
    void v06_alterarPrecoPadrao() { // Seq 06: Viagem-setPrecoPassagem(PADRAO)-getPrecoPassagem(PADRAO)-getPrecoPassagem(LEITO)
        viagem.setPrecoPassagem(90.0d, TipoDePoltrona.PADRAO);
        assertEquals(90.0d, viagem.getPrecoPassagem(TipoDePoltrona.PADRAO), DELTA);
        assertEquals(150.0d, viagem.getPrecoPassagem(TipoDePoltrona.LEITO), DELTA); // LEITO nao pode mudar
    }

    // ------------------------------------------------------------------
    // CATEGORIA C - Calculos: taxa de ocupacao
    // ------------------------------------------------------------------

    private void ocupar(int leitos, int padrao) {
        for (int i = 0; i < leitos; i++)
            assertTrue(onibus.adicionarPassageiro(new Passageiro("Leito " + i, "L" + i), TipoDePoltrona.LEITO));
        for (int i = 0; i < padrao; i++)
            assertTrue(onibus.adicionarPassageiro(new Passageiro("Padrao " + i, "P" + i), TipoDePoltrona.PADRAO));
    }

    @Test
    void v07_taxaGeralVazio() { // Seq 07: Viagem-getTaxaOcupacao()
        assertEquals(0.0d, viagem.getTaxaOcupacao(), DELTA);
    }

    @Test
    void v08_taxaGeralParcial() { // Seq 08: Viagem-[adicionarPassageiro()]^n-getTaxaOcupacao()
        ocupar(1, 2);
        assertEquals(0.6d, viagem.getTaxaOcupacao(), DELTA);
    }

    @Test
    void v09_taxaGeralLotado() { // Seq 09: Viagem-[adicionarPassageiro()]^capacidade-getTaxaOcupacao()
        ocupar(2, 3);
        assertEquals(1.0d, viagem.getTaxaOcupacao(), DELTA);
    }

    @Test
    void v10_taxaPorTipoVazio() { // Seq 10: Viagem-getTaxaOcupacao(LEITO)-getTaxaOcupacao(PADRAO)
        assertEquals(0.0d, viagem.getTaxaOcupacao(TipoDePoltrona.LEITO), DELTA);
        assertEquals(0.0d, viagem.getTaxaOcupacao(TipoDePoltrona.PADRAO), DELTA);
    }

    @Test
    void v11_taxaPorTipoParcial() { // Seq 11: Viagem-[adicionarPassageiro()]^n-getTaxaOcupacao(tipo)
        ocupar(1, 2);
        assertEquals(0.5d, viagem.getTaxaOcupacao(TipoDePoltrona.LEITO), DELTA);
        assertEquals(2.0d / 3.0d, viagem.getTaxaOcupacao(TipoDePoltrona.PADRAO), DELTA);
    }

    @Test
    void v12_taxaPorTipoLotado() { // Seq 12: Viagem-[adicionarPassageiro()]^capacidade-getTaxaOcupacao(tipo)
        ocupar(2, 3);
        assertEquals(1.0d, viagem.getTaxaOcupacao(TipoDePoltrona.LEITO), DELTA);
        assertEquals(1.0d, viagem.getTaxaOcupacao(TipoDePoltrona.PADRAO), DELTA);
    }

    @Test
    void v13_taxaComOnibusSemPoltronas() { // Seq 13 (limite): Viagem-setOnibus(vazio)-getTaxaOcupacao()
        viagem.setOnibus(new Onibus("SEM0000", "Teste", "Teste", 0, 0));
        double taxa = viagem.getTaxaOcupacao();
        assertFalse(Double.isNaN(taxa), "Taxa de ocupacao nao deve ser NaN para onibus sem poltronas");
    }

    // ------------------------------------------------------------------
    // CATEGORIA C - Calculos: faturamento
    // ------------------------------------------------------------------

    @Test
    void v14_faturamentoVazio() { // Seq 14: Viagem-calcularTotalFaturado()
        assertEquals(0.0d, viagem.calcularTotalFaturado(), DELTA);
    }

    @Test
    void v15_faturamentoParcial() { // Seq 15: Viagem-[adicionarPassageiro()]^n-calcularTotalFaturado()
        ocupar(1, 2); // 1x150 + 2x80
        assertEquals(310.0d, viagem.calcularTotalFaturado(), DELTA);
    }

    @Test
    void v16_faturamentoLotado() { // Seq 16: Viagem-[adicionarPassageiro()]^capacidade-calcularTotalFaturado()
        ocupar(2, 3); // 2x150 + 3x80
        assertEquals(540.0d, viagem.calcularTotalFaturado(), DELTA);
    }

    @Test
    void v17_faturamentoAposAlterarPreco() { // Seq 17: Viagem-setPrecoPassagem()-[adicionarPassageiro()]^n-calcularTotalFaturado()
        viagem.setPrecoPassagem(100.0d, TipoDePoltrona.LEITO);
        ocupar(1, 1); // 1x100 + 1x80
        assertEquals(180.0d, viagem.calcularTotalFaturado(), DELTA);
    }
}
