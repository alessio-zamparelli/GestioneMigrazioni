package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {

	// Modello -> Stato del sistema ad ogni passo
	private Graph<Country, DefaultEdge> grafo;

	// Tipi di evento/coda prioritaria
	// 1 solo evento, il tipo x che si muove
	private PriorityQueue<Evento> queue;

	// Parametri della simulazione
	private int N_MIGRANTI = 1000;
	private Country partenza;

	// Valori in output
	private int T;
	private Map<Country, Integer> stanziali;

	public void init(Country partenza, Graph<Country, DefaultEdge> grafo) {
		// Ricevo i parametri
		this.grafo = grafo;
		this.partenza = partenza;

		// Impostazione dello stato iniziale
		this.T = 1;
		stanziali = new HashMap<Country, Integer>();
		for (Country c : grafo.vertexSet())
			stanziali.put(c, 0);
		queue = new PriorityQueue<>();

		// Inserisco il primo evento, quello di partenza
		queue.add(new Evento(1, N_MIGRANTI, partenza));
	}

	public void run() {
		// Estraggo un evento per volta dalla coda e lo eseguo
		// finche la coda non si esaurisce
		Evento e;

		while ((e = queue.poll()) != null) {
			// Eseguo l'evento
			this.T = e.getT();
			int nPersone = e.getN();
			Country stato = e.getStato();
			List<Country> confinanti = Graphs.neighborListOf(grafo, stato);
			int migranti = (nPersone / 2) / confinanti.size(); // Fa gia l'approssimazione per difetto

			if (migranti > 0) {
				// vuol dire che posso spostarne in parti uguali
				for (Country c : confinanti) {
					queue.add(new Evento(e.getT() + 1, migranti, c));
				}
				int stanziali = nPersone - migranti * confinanti.size();
				this.stanziali.put(stato, this.stanziali.get(stato)+stanziali);
				
			}

		}

	}

	public int getLastT() {
		return this.T;
	}

	public Map<Country, Integer> getStanziali() {
		return stanziali;
	}

	

}

























