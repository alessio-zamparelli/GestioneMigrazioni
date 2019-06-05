/**
 * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryAndNumber;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BordersController {

	
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="boxNazione"
	private ComboBox<Country> boxNazione; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCalcolaConfini(ActionEvent event) {

		String annoS = txtAnno.getText();
		try {
			int anno = Integer.parseInt(annoS);

			model.creaGrafo(anno);
			
			
			
			List<CountryAndNumber> list = model.getCountryAndNumber();

			if (list.size() == 0) {
				txtResult.appendText("Non ci sono stati corrispondenti\n");
			} else {
				txtResult.appendText("Stati nell'anno "+anno+"\n");
				for (CountryAndNumber c : list) {
					txtResult.appendText(String.format("%s %d\n",
							c.getCountry().getStateName(), c.getNumber()));
				}
			}

		} catch (NumberFormatException e) {
			txtResult.appendText("Errore di formattazione dell'anno\n");
			return;
		}
		
		// Aggiorno la tendina degli stati
		boxNazione.getItems().addAll(model.getCountries());

	}

	@FXML
	void doSimula(ActionEvent event) {

		Country partenza = boxNazione.getValue();
		if(partenza==null) {
			txtResult.setText("Devi selezionare uno stato");
			return;
		}
		model.simula(partenza);
		
		txtResult.setText("SIMULAZIONE A PARTIRE DA " + partenza.toString() + "\n\n");
		txtResult.appendText(String.format("N PASSI: %d\n\n", model.getLastT()));
		for(CountryAndNumber c : model.getStanziali()) {
			if(c.getNumber()>0)
			txtResult.appendText(String.format("%s = %d\n", c.getCountry().getStateName(), c.getNumber()));
		}
		txtResult.positionCaret(0);
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
		assert boxNazione != null : "fx:id=\"boxNazione\" was not injected: check your FXML file 'Borders.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";
		txtResult.setStyle("-fx-font-family: monospace");
		txtResult.setFont(Font.font(null, FontWeight.BOLD, 12));
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
