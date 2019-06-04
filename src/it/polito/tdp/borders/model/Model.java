package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Graph<Country, DefaultEdge> graph;
	private List<Country> countries;
	private Map<Integer, Country> countriesMap;

	Simulatore sim;

	public Model() {
		this.countriesMap = new HashMap<>();
		sim = new Simulatore();
	}

	public void creaGrafo(int anno) {

		this.graph = new SimpleGraph<>(DefaultEdge.class);

		BordersDAO dao = new BordersDAO();

		// vertici
		dao.getCountriesFromYear(anno, this.countriesMap);
		Graphs.addAllVertices(graph, this.countriesMap.values());

		// archi
		List<Adiacenza> archi = dao.getCoppieAdiacenti(anno);
		for (Adiacenza c : archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()), this.countriesMap.get(c.getState2no()));

		}
	}

	public List<CountryAndNumber> getCountryAndNumber() {
		List<CountryAndNumber> list = new ArrayList<>();

		for (Country c : graph.vertexSet()) {
			list.add(new CountryAndNumber(c, graph.degreeOf(c)));
		}
		Collections.sort(list);
		return list;
	}

	public List<Country> getCountries() {
		return countriesMap.values().stream().sorted().collect(Collectors.toList());
	}

	public void simula(Country partenza) {
		sim.init(partenza, graph);
		sim.run();
	}

	public int getLastT() {
		return sim.getLastT();
	}

	public List<CountryAndNumber> getStanziali() {
		List<CountryAndNumber> res = new ArrayList<>();
		for (Entry<Country, Integer> entry : sim.getStanziali().entrySet())
			res.add(new CountryAndNumber(entry.getKey(), entry.getValue()));

		Collections.sort(res);

		return res;
	}

}
