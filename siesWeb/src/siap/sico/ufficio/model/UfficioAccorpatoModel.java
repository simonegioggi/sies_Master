package siap.sico.ufficio.model;

import f3b.model.GenericModel;

public class UfficioAccorpatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2327509505281228622L;

	private String codUfficio = "";
	private String codUfficioNew = "";
	private String codUfficioCompetente = "";
	private String codTipoUfficio = "";
	private String codTipoUfficioNew = "";
	private String descrizione = "";
	private String descrizioneNewUfficio = "";
	private String codComune = "";
	private String codProvincia = "";
	private String codDistrettoOld = "";
	private String codDistrettoNew = "";
	private String incrProgressivo = "";

	public UfficioAccorpatoModel() {

	}

	public UfficioAccorpatoModel(UfficioAccorpatoModel model) {

		this.codUfficio = model.codUfficio;
		this.codTipoUfficio = model.codTipoUfficio;
		this.codTipoUfficioNew = model.codTipoUfficioNew;
		this.descrizione = model.descrizione;
		this.codComune = model.codComune;
		this.codDistrettoOld = model.codDistrettoOld;
		this.descrizioneNewUfficio = model.descrizioneNewUfficio;
		this.codUfficioCompetente = model.codUfficioCompetente;
		this.codDistrettoNew = model.codDistrettoNew;
		this.codProvincia = model.codProvincia;
		this.codUfficioNew = model.codUfficioNew;
		this.incrProgressivo = model.incrProgressivo;

	}

	public String transCodingTipoUfficioSige() {
		String ret = codTipoUfficio;

		if (codTipoUfficio.equals("DIB")) {
			ret = "Tribunale Ordinario";
		} else if (codTipoUfficio.equals("TRIBSD")) {
			ret = "Sezione Distaccata Tribunale";
		} else if (codTipoUfficio.equals("CAS")) {
			ret = "Corte Assise";
		} else if (codTipoUfficio.equals("GIP")) {
			ret = "Gip presso Tribunale";
		} else if (codTipoUfficio.equals("PM")) {
			ret = "Procura presso Tribunale";
		} else if (codTipoUfficio.equals("PGCAP")) {
			ret = "Procura presso Corte D'appello";
		}
		return ret;
	}

	public String getCodUfficio() {
		return codUfficio;
	}

	public void setCodUfficio(String codUfficio) {
		this.codUfficio = codUfficio;
	}

	public String getCodTipoUfficio() {
		return codTipoUfficio;
	}

	public void setCodTipoUfficio(String codTipoUfficio) {
		this.codTipoUfficio = codTipoUfficio;
	}

	public String getCodTipoUfficioNew() {
		return codTipoUfficioNew;
	}

	public void setCodTipoUfficioNew(String codTipoUfficioNew) {
		this.codTipoUfficioNew = codTipoUfficioNew;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodComune() {
		return codComune;
	}

	public void setCodComune(String codComune) {
		this.codComune = codComune;
	}

	public String getCodDistrettoOld() {
		return codDistrettoOld;
	}

	public void setCodDistrettoOld(String codDistrettoOld) {
		this.codDistrettoOld = codDistrettoOld;
	}

	public String getDescrizioneNewUfficio() {
		return descrizioneNewUfficio;
	}

	public void setDescrizioneNewUfficio(String descrizioneNewUfficio) {
		this.descrizioneNewUfficio = descrizioneNewUfficio;
	}

	public String getCodUfficioCompetente() {
		return codUfficioCompetente;
	}

	public void setCodUfficioCompetente(String codUfficioCompetente) {
		this.codUfficioCompetente = codUfficioCompetente;
	}

	public String getCodDistrettoNew() {
		return codDistrettoNew;
	}

	public void setCodDistrettoNew(String codDistrettoNew) {
		this.codDistrettoNew = codDistrettoNew;
	}

	public String getCodProvincia() {
		return codProvincia;
	}

	public void setCodProvincia(String codProvincia) {
		this.codProvincia = codProvincia;
	}

	public String getCodUfficioNew() {
		return codUfficioNew;
	}

	public void setCodUfficioNew(String codUfficioNew) {
		this.codUfficioNew = codUfficioNew;
	}

	public String getIncrProgressivo() {
		return incrProgressivo;
	}

	public void setIncrProgressivo(String incrProgressivo) {
		this.incrProgressivo = incrProgressivo;
	}

}