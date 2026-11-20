package busServices;

import java.util.Calendar;

public class Viagem {

	private String origem, destino;
    private Calendar dataViagem;
    private double precoLeito, precoPADRAO;
    private Onibus onib; //inst�ncia de �nibus que far� a viagem
	
    public Viagem(String origem, String destino, Calendar dataViagem,
			double precoLeito, double precoPADRAO, Onibus on) {
		super();
		this.origem = origem;
		this.destino = destino;
		this.dataViagem = dataViagem;
		this.precoLeito = precoLeito;
		this.precoPADRAO = precoPADRAO;
		this.onib = on;
	}
    
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Calendar getDataViagem() {
		return dataViagem;
	}
	public void setDataViagem(Calendar dataViagem) {
		this.dataViagem = dataViagem;
	}
	public double getPrecoLeito() {
		return precoLeito;
	}

	
	public double getPrecoPassagem(TipoDePoltrona tp) {
		
		switch (tp) {
			case PADRAO : return this.precoPADRAO;
					
			case LEITO  : return this.precoLeito;
				
		}
		return 0;
	
	}
	
	public void setPrecoPassagem(double preco, TipoDePoltrona tp) {
		switch (tp) {
			case PADRAO : this.precoPADRAO = preco;
					
			case LEITO  : this.precoLeito = preco;
		
		}
	}
	
	public Onibus getOnibus() {
		return onib;
	}
	public void setOnibus(Onibus on) {
		this.onib = on;
	}
    
	//calcula total fatura na viagem
    public double calcularTotalFaturado(){
    	double faturaLeito = onib.getCapacidade(TipoDePoltrona.LEITO) - onib.getLugaresLivres(TipoDePoltrona.LEITO) * this.getPrecoPassagem(TipoDePoltrona.LEITO);
    	double faturaComum = onib.getCapacidade(TipoDePoltrona.PADRAO) - onib.getLugaresLivres(TipoDePoltrona.PADRAO) * this.getPrecoPassagem(TipoDePoltrona.LEITO);
        return faturaLeito + faturaComum;
    }
    
    //caclula taxa de Ocupa��o Global do Onibus
    public double getTaxaOcupacao()
    {
    	return (double)  (onib.getCapacidade() - onib.getLugaresLivres()) / onib.getCapacidade();
    }
    
    //sobrecrga calcula taxa de Ocupa��o do Onibus por TipoDePoltrona
    public double getTaxaOcupacao
	
	
	
	
	(TipoDePoltrona tp)
    {
    	return (double) onib.getCapacidade(tp) - onib.getLugaresLivres(tp) / onib.getCapacidade(tp);
    }
}
