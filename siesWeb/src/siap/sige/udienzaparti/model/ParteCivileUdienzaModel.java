package siap.sige.udienzaparti.model;



import siap.sico.residenza.model.ResidenzaModel;
import f3b.util.DateUtils;


public class ParteCivileUdienzaModel extends AnagraficaPartiUdienzaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7757178926565223383L;
    
	
	
	public ParteCivileUdienzaModel (AnagraficaPartiUdienzaModel parteCivile) {
    	super (parteCivile);
    }
    
    public ParteCivileUdienzaModel () {
    	super ();
    }

	public String getNome() {
		return super.getNome();
	}
    
	public String getCognome() {
		return super.getCognome();
	}
	
	public String getLabelNascita () {
		String label=" nato ";
		if (super.getSesso().equalsIgnoreCase("F"))
			label =" nata ";
		
		return label;
	}
	
	public String getDataNascitaAsString () {
		super.getDataNascita();
		return DateUtils.getDateToString(super.getDataNascita(), "dd-MM-yyyy");
	}
	
	public String getDescComuneNascita () {
		String luogoNascita=super.getDescComuneNascita();
		return luogoNascita;
	}

	public String getDescComuneNascitaEstero () {
		String luogoNascitaEstero=super.getDescComuneNascitaEstero();
		return luogoNascitaEstero;
	}

	public String getDescrStatoNascita () {
		String luogoStatoNascita=super.getDescrStatoNascita();
		return luogoStatoNascita;
	}

	public String getDescrResidenza () {
		ResidenzaModel residenza=super.getResidenza();
		String indirizzo=residenza.toStringaResidenza();
		return indirizzo;
	}
	
	public String getFlagDomicilioDifensore () {
		ResidenzaModel residenza = super.getResidenza();
		String flgDomicilioDif = residenza.getFlgDomicilioDifensore();
		return flgDomicilioDif;
	}

	public String getCodParte () {
		String codiceParte = super.getCodParte();
		return codiceParte;
	}
	
	public String getDenominazione () {
		String denominazione = super.getDenominazione();
		return denominazione;
	}

	public String getRagSociale () {
		String ragSociale = super.getRagSociale();
		return ragSociale;
	}
	
}
