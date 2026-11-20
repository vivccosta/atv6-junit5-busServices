package busServices;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Onibus {

	private List<Poltrona> config;
	private int lotMaxima, nPoltronasPADRAO, nLeito;
	private String placa, modelo, marca;
	
	//constroi o objeto Onibus contendo placa, modelo e marca
	//configura o ?nibus com poltronas comuns especificadas no par?mtero numPoltronas
	//e o numero de leitos especificados no par?mntero numLeitos
	public Onibus(String pl, String md, String mar, int numPoltronasPADRAO, int numLeitos) {
	
	//atribui parametros dos construtor aos atributos correspondnetes
	this.placa = pl;
	this.modelo = md;
	this.marca = mar;
	this.nPoltronasPADRAO = numPoltronasPADRAO;
	this.nLeito = numLeitos;
	this.lotMaxima = numPoltronasPADRAO+numLeitos;
	config = new ArrayList<Poltrona>(lotMaxima);	
	
	//seta a configura??o do Onibus
	//constroi as poltonas do tipo lEITO e atribui a elas as primeiras numera??es (id)
	int i=0;
	for (i=0; i<numLeitos; i++)
		{   String id = "";
		    if (i < 9) id = id + "0"; //insere 0 na frente da numera??o  1-9
		    id = id + Integer.toString(i+1);
			config.add (new Leito (id,TipoDePoltrona.LEITO,0.5d,0.5d,1.2,180) );
		}
	
	//constroi as poltonas restantes como tipo PADRAO, continuando a numera??o (id)
	for (int j=0; j<numPoltronasPADRAO; j++, i++)
		{   String id = "";
		    if (i < 9) id = id + "0"; //insere 0 na frente da anumera??o 1-9
		    id = id + Integer.toString(i+1);
		    config.add ( new Poltrona (id,TipoDePoltrona.PADRAO,0.5d,0.5d,1.2) );
		}
				
	} //fim do construtor
	
	//getters e setter
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	
	
	//m?todo que retona a quantidade total de lugares do onibus
	public int getCapacidade() {
		return lotMaxima;
	}

	//sobrecarga do m?todo que retona a quantidade total de lugares do onibus
	//por tipo de poltorn passada como par?metro
	public int getCapacidade(TipoDePoltrona tp) {
		switch (tp) {
			case PADRAO : return this.nPoltronasPADRAO;
					
			case LEITO  : return this.nLeito;
				
		}
		return 0;
	}
	
	//m?todo que retorna a quantidade de leitos Disponiveis
	//do tipo de Poltorna passado como par?mtro
	public int getLugaresLivres(TipoDePoltrona tp) {
		
        Iterator <Poltrona> iter = config.iterator();
        int cont = 0;
        
        while (iter.hasNext()) {
        	Poltrona lugar = iter.next();
        	if ( (lugar.getTipo() == tp) && (lugar.getOcupante() == null) )
        	   cont++; //se for tipo leito e n?o houver ocupante, conta mais um
        	        	
        }
        
        return cont;
	}
	
	//sobrecarga do m?todo que retorna a quantidade de leitos Disponiveis
	//independente do tipo de Poltorna passado como parametro
	public int getLugaresLivres() {
		
        Iterator <Poltrona> iter = config.iterator();
        int cont = 0;
        
        while (iter.hasNext()) {
        	Poltrona lugar = iter.next();
        	if ( (lugar.getOcupante() == null) )
        	   cont++; //se for tipo leito e n?o houver ocupante, conta mais um
        	        	
        }
        
        return cont;
	}
	

	
	//m?todo que retorna a inst?ncia da pr?xima poltrona livre
	//coforme tipo de Plotrona. se n?o houver disponibilidade, retorna null.
	private Poltrona getProxPoltornaLivre(TipoDePoltrona tp){
		
		Iterator <Poltrona> iter = config.iterator();
                
        while (iter.hasNext()) {
        	Poltrona lugar = iter.next();
        	if ( (lugar.getTipo() == tp) && (lugar.getOcupante() == null) )
        	   return lugar;
        	        	
        }
		
        return null;
		
	}
		
	//m?todo que aloca um passageiro, cuja int?ncia ? recebida como par?metro
	//na proxima poltrona livre do tipo passado como 2o par?netro e retorna true se bem sucedido; 
	//se n?o houver mais poltronas livres retorna falso
	public boolean adicionarPassageiro(Passageiro ocp, TipoDePoltrona tp){				

		Poltrona p = this.getProxPoltornaLivre(tp);
		if (p==null) 
			return false;
		p.setOcupante(ocp);
		return true;
	}	
	
	//retona a inst?ncia de passageiro que ocupa a poltrona passada como par?metro
	//null indica se passageiro n?o foi localizado
	public Passageiro getPassageiroDaPoltrona(String ident){
		
        Iterator <Poltrona> iter = config.iterator();
                
        while (iter.hasNext()) {
        	Poltrona lugar = iter.next();
        	if ( (lugar.getIdent().compareTo(ident) == 0) )
        	        	return lugar.getOcupante();
        }
        return null;
		
	}
	
	//retorna Poltorna de Passageiro passado como par?metro (nome)
	//null indica se passageiro n?o foi localizado
	public Poltrona consultarPoltronaDePassageiro (String nome){

        Iterator <Poltrona> iter = config.iterator();
        
        while (iter.hasNext()) {
        	Poltrona lugar = iter.next();
        	if ( (lugar.getOcupante().getNome().compareToIgnoreCase(nome) == 0) )
        	        	return lugar;
        }
        return null;
	}
	
	//sobrecarga que retorna Poltorna de int?ncia de Passageiro passada como par?metri
	//null indica se passageiro n?o foi localizado
	public Poltrona consultarPoltronaDePassageiro (Passageiro pax){

        Iterator <Poltrona> iter = config.iterator();
        
        while (iter.hasNext()) {
        	Poltrona lugar = iter.next();
        	if ( (lugar.getOcupante().equals(pax)) )
        	        	return lugar;
        }
        return null;
	}
	
	
	
	
	public String toString(){

		String S = "\n\n\t\t*** MAPA DE LUGARES ***";
		
        Iterator <Poltrona> iter = config.iterator();
        
        while (iter.hasNext()) {
        	Poltrona lugar = iter.next();
			S=S+"\n Poltrona "+lugar.getIdent()+", tipo "+lugar.getTipo()+" de ?rea ?til "+lugar.calcularArea()+" m2"; //agrega poltrona usando ToString
			
			if (lugar.getOcupante()==null)
			  	 S = S + ("\tVAGA");
			else S = S + ("\tOcupada por "+lugar.getOcupante().getNome()+", RG: "+lugar.getOcupante().getRG());					
		}
		
		//retorna a String montada
		return S;
	}
}
