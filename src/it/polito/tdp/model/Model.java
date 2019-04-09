package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.dao.EsameDAO;

public class Model {

	//Esami letti dal BataBase
	private List<Esame> esami;
	
	//gestione della ricorsione ( soluzione migliore fino a quel livello )
	private List<Esame> best;
	private double media_best;
	
	//Costruttore
	public Model() {
		EsameDAO dao = new EsameDAO();
	    this.esami = dao.getTuttiEsami();
	}
	
	/**
	 * Trova la combinazione di corsi avente somma crediti richiesta, che abbia la media voti max
	 * @param numeroCrediti
	 * @return elenco lista corsi ottimale, oppure NULL se non esiste la soluzione ottimale per quel numero crediti
	 */
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		//Inizializzo variabili
		best = null;
		media_best = 0.0;
		
		Set<Esame> parziale = new HashSet<>();
		
		//avvio ricorsione
		cerca (parziale,0,numeroCrediti);
		
		//alla fine, ritorno i risultati migliori
		return best;
	}
	
	private void cerca(Set<Esame> parziale, int livello, int m ) {
		
		//Caso terminale??
		int crediti = sommaCrediti(parziale);
		
		//Verifico se la somma crediti è corretta
		if (crediti > m) return;
		
		if (crediti == m) {
			double media = calcolaMedia(parziale);
			
			//Se la media è migliore, aggiorno soluzione parziale
			if (media > media_best) {
				//Lo assegno creando un nuovo oggetto
				best = new ArrayList<Esame>(parziale);
				media_best = media;
				
				//chiudo questo ramo
				return;
			}
			//chiudo lo stesso il ramo, perchè non vi possono essere miglioramenti al di sotto
			else return;
		}
		
		//(crediti<m) - gestisco il caso in cui siamo all'ultimo esame della lista
		if(livello==esami.size())
			return;
		
		//Generiamo sotto-problemi
		//esame [L] è da aggiungere o no?
		
		//Provo a non aggiungerlo
		cerca(parziale,livello+1,m);
		
		//Provo ad aggiungerlo
		parziale.add(esami.get(livello)) ; 
		cerca (parziale,livello+1,m);
		//se lo aggiungo, backtracking
		parziale.remove(esami.get(livello));	
	}

	private double calcolaMedia(Set<Esame> parziale) {
		double media = 0.0;
		int crediti = 0;
		
		for (Esame e : parziale) {
			media += e.getVoto()*e.getCrediti();
			crediti += e.getCrediti();
		}
		
		return media/crediti;
	}
	
	private int sommaCrediti(Set<Esame> parziale) {
		
		int somma = 0;
		for (Esame e : parziale) {
			somma += e.getCrediti();
		}
		return somma;
	}
	
}
