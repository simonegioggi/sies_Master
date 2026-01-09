package it.mig.sies.model;

import java.io.Serial;
import java.util.Date;
import java.util.List;

/**
 * SIES FASE 2 - Classe model per la gestione della risposta
 * 
 * @author Federico Paparoni
 * 
 */
public class ResponseData extends BaseModel {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 7687582982835511468L;

	private long id;
	private String esito;
	private long chiaveSies;
	private long chiaveNSC;
	private byte[] estratto;
	private boolean completed;
	private Date dataOperazione;
	private String operazione;
	private TitoloEsecutivo titoloEsecutivo;
	private Soggetto soggetto;
	private List<TitoloGiudiziario> titoloGiudiziarioList;
	// MEV 23010 - Aggiunte informazioni di dettaglio del provvedimento
	private DescrizioneProvvedimento descrizioneProvvedimento;
	// MEV 23010 - Aggiunte informazioni di dettaglio per l'errore
	private String dettaglioErrore;

	// MEV 16 + MEV 31: aggiunta variabile per gestire la sinonimia
	private List<Sinonimo> elencoSinonimi;

	// MEV 16 CUMULO: aggiunte variabili per gestire l'elenco dei provvedimenti cumulabili
	private List<TitoloGiudiziario> elencoProvvedimentiNSC;
	private String codiceEsito;
	private String descCodiceEsito;
	private String descEsitoCumulo;

	/**
	 * @return the descrizioneProvvedimento
	 */
	public DescrizioneProvvedimento getDescrizioneProvvedimento() {
		return descrizioneProvvedimento;
	}

	/**
	 * @param descrizioneProvvedimento
	 *            the descrizioneProvvedimento to set
	 */
	public void setDescrizioneProvvedimento(DescrizioneProvvedimento descrizioneProvvedimento) {
		this.descrizioneProvvedimento = descrizioneProvvedimento;
	}

	/**
	 * @return the titoloGiudiziarioList
	 */
	public List<TitoloGiudiziario> getTitoloGiudiziarioList() {
		return titoloGiudiziarioList;
	}

	/**
	 * @param titoloGiudiziarioList
	 *            the titoloGiudiziarioList to set
	 */
	public void setTitoloGiudiziarioList(List<TitoloGiudiziario> titoloGiudiziarioList) {
		this.titoloGiudiziarioList = titoloGiudiziarioList;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the esito
	 */
	public String getEsito() {
		return esito;
	}

	/**
	 * @param esito
	 *            the esito to set
	 */
	public void setEsito(String esito) {
		this.esito = esito;
	}

	/**
	 * @return the chiaveSies
	 */
	public long getChiaveSies() {
		return chiaveSies;
	}

	/**
	 * @param chiaveSies
	 *            the chiaveSies to set
	 */
	public void setChiaveSies(long chiaveSies) {
		this.chiaveSies = chiaveSies;
	}

	/**
	 * @return the chiaveNSC
	 */
	public long getChiaveNSC() {
		return chiaveNSC;
	}

	/**
	 * @param chiaveNSC
	 *            the chiaveNSC to set
	 */
	public void setChiaveNSC(long chiaveNSC) {
		this.chiaveNSC = chiaveNSC;
	}

	/**
	 * @return the estratto
	 */
	public byte[] getEstratto() {
		return estratto;
	}

	/**
	 * @param estratto
	 *            the estratto to set
	 */
	public void setEstratto(byte[] estratto) {
		this.estratto = estratto;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed
	 *            the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * @return the dataOperazione
	 */
	public Date getDataOperazione() {
		return dataOperazione;
	}

	/**
	 * @param dataOperazione
	 *            the dataOperazione to set
	 */
	public void setDataOperazione(Date dataOperazione) {
		this.dataOperazione = dataOperazione;
	}

	/**
	 * @return the operazione
	 */
	public String getOperazione() {
		return operazione;
	}

	/**
	 * @param operazione
	 *            the operazione to set
	 */
	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}

	/**
	 * @return the titoloEsecutivo
	 */
	public TitoloEsecutivo getTitoloEsecutivo() {
		return titoloEsecutivo;
	}

	/**
	 * @param titoloEsecutivo
	 *            the titoloEsecutivo to set
	 */
	public void setTitoloEsecutivo(TitoloEsecutivo titoloEsecutivo) {
		this.titoloEsecutivo = titoloEsecutivo;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto
	 *            the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the dettaglioErrore
	 */
	public String getDettaglioErrore() {
		return dettaglioErrore;
	}

	/**
	 * @param dettaglioErrore
	 *            the dettaglioErrore to set
	 */
	public void setDettaglioErrore(String dettaglioErrore) {
		this.dettaglioErrore = dettaglioErrore;
	}

	/**
	 * @return the elencoSinonimi
	 */
	public List<Sinonimo> getElencoSinonimi() {
		return elencoSinonimi;
	}

	/**
	 * @param elencoSinonimi
	 *            the elencoSinonimi to set
	 */
	public void setElencoSinonimi(List<Sinonimo> elencoSinonimi) {
		this.elencoSinonimi = elencoSinonimi;
	}

	/**
	 * @return the elencoProvvedimentiNSC
	 */
	public List<TitoloGiudiziario> getElencoProvvedimentiNSC() {
		return elencoProvvedimentiNSC;
	}

	/**
	 * @param elencoProvvedimentiNSC
	 *            the elencoProvvedimentiNSC to set
	 */
	public void setElencoProvvedimentiNSC(List<TitoloGiudiziario> elencoProvvedimentiNSC) {
		this.elencoProvvedimentiNSC = elencoProvvedimentiNSC;
	}

	/**
	 * @return the codiceEsito
	 */
	public String getCodiceEsito() {
		return codiceEsito;
	}

	/**
	 * @param codiceEsito
	 *            the codiceEsito to set
	 */
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}

	/**
	 * @return the descCodiceEsito
	 */
	public String getDescCodiceEsito() {
		return descCodiceEsito;
	}

	/**
	 * @param descCodiceEsito
	 *            the descCodiceEsito to set
	 */
	public void setDescCodiceEsito(String descCodiceEsito) {
		this.descCodiceEsito = descCodiceEsito;
	}

	/**
	 * @return the descEsitoCumulo
	 */
	public String getDescEsitoCumulo() {
		return descEsitoCumulo;
	}

	/**
	 * @param descEsitoCumulo
	 *            the descEsitoCumulo to set
	 */
	public void setDescEsitoCumulo(String descEsitoCumulo) {
		this.descEsitoCumulo = descEsitoCumulo;
	}

}