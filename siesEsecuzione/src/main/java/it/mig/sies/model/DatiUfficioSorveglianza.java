package it.mig.sies.model;

import java.io.Serial;
import java.util.Date;
import java.util.List;

/**
 * SIES FASE 2 - Classe model relativa ai dati dell'Ufficio di Sorveglianza
 * 
 * @author Federico Paparoni
 */
public class DatiUfficioSorveglianza extends BaseModel {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 5609442858351913298L;

	private long chiaveSies;
	private long chiaveNSC;
	private Date dataProvvedimento;
	private String codiceAutorita;
	private String sedeAutoritaPrinDist;
	private String sedeAutoritaPrinc;
	private String codiceUnivocoProvvedimento;
	private String tipoProvvedimento;
	private int numGiorniLibAnticipata;
	private int numGiorniLibAnticipataLs;
	private int numGiorniLibAnticipataLi;
	private Date arrayPeriodoLA;
	private double importoAmmenda;
	private double importoMulta;
	private int anniDurataLibControllata;
	private int mesiDurataLibControllata;
	private int giorniDurataLibControllata;
	private int anniDurataLavoroSost;
	private int mesiDurataLavoroSost;
	private int giorniDurataLavoroSost;
	private String testoLibero;
	private int numeroRate;
	private double importoRate;
	private double importoUltimaRata;
	private Date dataDecorrenzaPrimaRata;
	private int giorniDallaNotifica;
	private String durataDifferimento;
	private Date dataDecorrenzaSospensione;
	private int giorniSospensione;
	private int mesiSospensione;
	private int anniSospensione;
	private Date dataFineSospensioneSS;
	private long idProvvRevocato;
	private List<PeriodoLibertaAnticipata> periodoLibertaAnticipataList;

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
	 * @return the dataProvvedimento
	 */
	public Date getDataProvvedimento() {
		return dataProvvedimento;
	}

	/**
	 * @param dataProvvedimento
	 *            the dataProvvedimento to set
	 */
	public void setDataProvvedimento(Date dataProvvedimento) {
		this.dataProvvedimento = dataProvvedimento;
	}

	/**
	 * @return the codiceAutorita
	 */
	public String getCodiceAutorita() {
		return codiceAutorita;
	}

	/**
	 * @param codiceAutorita
	 *            the codiceAutorita to set
	 */
	public void setCodiceAutorita(String codiceAutorita) {
		this.codiceAutorita = codiceAutorita;
	}

	/**
	 * @return the sedeAutoritaPrinDist
	 */
	public String getSedeAutoritaPrinDist() {
		return sedeAutoritaPrinDist;
	}

	/**
	 * @param sedeAutoritaPrinDist
	 *            the sedeAutoritaPrinDist to set
	 */
	public void setSedeAutoritaPrinDist(String sedeAutoritaPrinDist) {
		this.sedeAutoritaPrinDist = sedeAutoritaPrinDist;
	}

	/**
	 * @return the sedeAutoritaPrinc
	 */
	public String getSedeAutoritaPrinc() {
		return sedeAutoritaPrinc;
	}

	/**
	 * @param sedeAutoritaPrinc
	 *            the sedeAutoritaPrinc to set
	 */
	public void setSedeAutoritaPrinc(String sedeAutoritaPrinc) {
		this.sedeAutoritaPrinc = sedeAutoritaPrinc;
	}

	/**
	 * @return the codiceUnivocoProvvedimento
	 */
	public String getCodiceUnivocoProvvedimento() {
		return codiceUnivocoProvvedimento;
	}

	/**
	 * @param codiceUnivocoProvvedimento
	 *            the codiceUnivocoProvvedimento to set
	 */
	public void setCodiceUnivocoProvvedimento(String codiceUnivocoProvvedimento) {
		this.codiceUnivocoProvvedimento = codiceUnivocoProvvedimento;
	}

	/**
	 * @return the tipoProvvedimento
	 */
	public String getTipoProvvedimento() {
		return tipoProvvedimento;
	}

	/**
	 * @param tipoProvvedimento
	 *            the tipoProvvedimento to set
	 */
	public void setTipoProvvedimento(String tipoProvvedimento) {
		this.tipoProvvedimento = tipoProvvedimento;
	}

	/**
	 * @return the numGiorniLibAnticipata
	 */
	public int getNumGiorniLibAnticipata() {
		return numGiorniLibAnticipata;
	}

	/**
	 * @param numGiorniLibAnticipata
	 *            the numGiorniLibAnticipata to set
	 */
	public void setNumGiorniLibAnticipata(int numGiorniLibAnticipata) {
		this.numGiorniLibAnticipata = numGiorniLibAnticipata;
	}

	/**
	 * @return the numGiorniLibAnticipataLs
	 */
	public int getNumGiorniLibAnticipataLs() {
		return numGiorniLibAnticipataLs;
	}

	/**
	 * @param numGiorniLibAnticipataLs
	 *            the numGiorniLibAnticipataLs to set
	 */
	public void setNumGiorniLibAnticipataLs(int numGiorniLibAnticipataLs) {
		this.numGiorniLibAnticipataLs = numGiorniLibAnticipataLs;
	}

	/**
	 * @return the numGiorniLibAnticipataLi
	 */
	public int getNumGiorniLibAnticipataLi() {
		return numGiorniLibAnticipataLi;
	}

	/**
	 * @param numGiorniLibAnticipataLi
	 *            the numGiorniLibAnticipataLi to set
	 */
	public void setNumGiorniLibAnticipataLi(int numGiorniLibAnticipataLi) {
		this.numGiorniLibAnticipataLi = numGiorniLibAnticipataLi;
	}

	/**
	 * @return the arrayPeriodoLA
	 */
	public Date getArrayPeriodoLA() {
		return arrayPeriodoLA;
	}

	/**
	 * @param arrayPeriodoLA
	 *            the arrayPeriodoLA to set
	 */
	public void setArrayPeriodoLA(Date arrayPeriodoLA) {
		this.arrayPeriodoLA = arrayPeriodoLA;
	}

	/**
	 * @return the importoAmmenda
	 */
	public double getImportoAmmenda() {
		return importoAmmenda;
	}

	/**
	 * @param importoAmmenda
	 *            the importoAmmenda to set
	 */
	public void setImportoAmmenda(double importoAmmenda) {
		this.importoAmmenda = importoAmmenda;
	}

	/**
	 * @return the importoMulta
	 */
	public double getImportoMulta() {
		return importoMulta;
	}

	/**
	 * @param importoMulta
	 *            the importoMulta to set
	 */
	public void setImportoMulta(double importoMulta) {
		this.importoMulta = importoMulta;
	}

	/**
	 * @return the anniDurataLibControllata
	 */
	public int getAnniDurataLibControllata() {
		return anniDurataLibControllata;
	}

	/**
	 * @param anniDurataLibControllata
	 *            the anniDurataLibControllata to set
	 */
	public void setAnniDurataLibControllata(int anniDurataLibControllata) {
		this.anniDurataLibControllata = anniDurataLibControllata;
	}

	/**
	 * @return the mesiDurataLibControllata
	 */
	public int getMesiDurataLibControllata() {
		return mesiDurataLibControllata;
	}

	/**
	 * @param mesiDurataLibControllata
	 *            the mesiDurataLibControllata to set
	 */
	public void setMesiDurataLibControllata(int mesiDurataLibControllata) {
		this.mesiDurataLibControllata = mesiDurataLibControllata;
	}

	/**
	 * @return the giorniDurataLibControllata
	 */
	public int getGiorniDurataLibControllata() {
		return giorniDurataLibControllata;
	}

	/**
	 * @param giorniDurataLibControllata
	 *            the giorniDurataLibControllata to set
	 */
	public void setGiorniDurataLibControllata(int giorniDurataLibControllata) {
		this.giorniDurataLibControllata = giorniDurataLibControllata;
	}

	/**
	 * @return the anniDurataLavoroSost
	 */
	public int getAnniDurataLavoroSost() {
		return anniDurataLavoroSost;
	}

	/**
	 * @param anniDurataLavoroSost
	 *            the anniDurataLavoroSost to set
	 */
	public void setAnniDurataLavoroSost(int anniDurataLavoroSost) {
		this.anniDurataLavoroSost = anniDurataLavoroSost;
	}

	/**
	 * @return the mesiDurataLavoroSost
	 */
	public int getMesiDurataLavoroSost() {
		return mesiDurataLavoroSost;
	}

	/**
	 * @param mesiDurataLavoroSost
	 *            the mesiDurataLavoroSost to set
	 */
	public void setMesiDurataLavoroSost(int mesiDurataLavoroSost) {
		this.mesiDurataLavoroSost = mesiDurataLavoroSost;
	}

	/**
	 * @return the giorniDurataLavoroSost
	 */
	public int getGiorniDurataLavoroSost() {
		return giorniDurataLavoroSost;
	}

	/**
	 * @param giorniDurataLavoroSost
	 *            the giorniDurataLavoroSost to set
	 */
	public void setGiorniDurataLavoroSost(int giorniDurataLavoroSost) {
		this.giorniDurataLavoroSost = giorniDurataLavoroSost;
	}

	/**
	 * @return the testoLibero
	 */
	public String getTestoLibero() {
		return testoLibero;
	}

	/**
	 * @param testoLibero
	 *            the testoLibero to set
	 */
	public void setTestoLibero(String testoLibero) {
		this.testoLibero = testoLibero;
	}

	/**
	 * @return the numeroRate
	 */
	public int getNumeroRate() {
		return numeroRate;
	}

	/**
	 * @param numeroRate
	 *            the numeroRate to set
	 */
	public void setNumeroRate(int numeroRate) {
		this.numeroRate = numeroRate;
	}

	/**
	 * @return the importoRate
	 */
	public double getImportoRate() {
		return importoRate;
	}

	/**
	 * @param importoRate
	 *            the importoRate to set
	 */
	public void setImportoRate(double importoRate) {
		this.importoRate = importoRate;
	}

	/**
	 * @return the importoUltimaRata
	 */
	public double getImportoUltimaRata() {
		return importoUltimaRata;
	}

	/**
	 * @param importoUltimaRata
	 *            the importoUltimaRata to set
	 */
	public void setImportoUltimaRata(double importoUltimaRata) {
		this.importoUltimaRata = importoUltimaRata;
	}

	/**
	 * @return the dataDecorrenzaPrimaRata
	 */
	public Date getDataDecorrenzaPrimaRata() {
		return dataDecorrenzaPrimaRata;
	}

	/**
	 * @param dataDecorrenzaPrimaRata
	 *            the dataDecorrenzaPrimaRata to set
	 */
	public void setDataDecorrenzaPrimaRata(Date dataDecorrenzaPrimaRata) {
		this.dataDecorrenzaPrimaRata = dataDecorrenzaPrimaRata;
	}

	/**
	 * @return the giorniDallaNotifica
	 */
	public int getGiorniDallaNotifica() {
		return giorniDallaNotifica;
	}

	/**
	 * @param giorniDallaNotifica
	 *            the giorniDallaNotifica to set
	 */
	public void setGiorniDallaNotifica(int giorniDallaNotifica) {
		this.giorniDallaNotifica = giorniDallaNotifica;
	}

	/**
	 * @return the durataDifferimento
	 */
	public String getDurataDifferimento() {
		return durataDifferimento;
	}

	/**
	 * @param durataDifferimento
	 *            the durataDifferimento to set
	 */
	public void setDurataDifferimento(String durataDifferimento) {
		this.durataDifferimento = durataDifferimento;
	}

	/**
	 * @return the dataDecorrenzaSospensione
	 */
	public Date getDataDecorrenzaSospensione() {
		return dataDecorrenzaSospensione;
	}

	/**
	 * @param dataDecorrenzaSospensione
	 *            the dataDecorrenzaSospensione to set
	 */
	public void setDataDecorrenzaSospensione(Date dataDecorrenzaSospensione) {
		this.dataDecorrenzaSospensione = dataDecorrenzaSospensione;
	}

	/**
	 * @return the giorniSospensione
	 */
	public int getGiorniSospensione() {
		return giorniSospensione;
	}

	/**
	 * @param giorniSospensione
	 *            the giorniSospensione to set
	 */
	public void setGiorniSospensione(int giorniSospensione) {
		this.giorniSospensione = giorniSospensione;
	}

	/**
	 * @return the mesiSospensione
	 */
	public int getMesiSospensione() {
		return mesiSospensione;
	}

	/**
	 * @param mesiSospensione
	 *            the mesiSospensione to set
	 */
	public void setMesiSospensione(int mesiSospensione) {
		this.mesiSospensione = mesiSospensione;
	}

	/**
	 * @return the anniSospensione
	 */
	public int getAnniSospensione() {
		return anniSospensione;
	}

	/**
	 * @param anniSospensione
	 *            the anniSospensione to set
	 */
	public void setAnniSospensione(int anniSospensione) {
		this.anniSospensione = anniSospensione;
	}

	/**
	 * @return the dataFineSospensioneSS
	 */
	public Date getDataFineSospensioneSS() {
		return dataFineSospensioneSS;
	}

	/**
	 * @param dataFineSospensioneSS
	 *            the dataFineSospensioneSS to set
	 */
	public void setDataFineSospensioneSS(Date dataFineSospensioneSS) {
		this.dataFineSospensioneSS = dataFineSospensioneSS;
	}

	/**
	 * @return the idProvvRevocato
	 */
	public long getIdProvvRevocato() {
		return idProvvRevocato;
	}

	/**
	 * @param idProvvRevocato
	 *            the idProvvRevocato to set
	 */
	public void setIdProvvRevocato(long idProvvRevocato) {
		this.idProvvRevocato = idProvvRevocato;
	}

	/**
	 * @return the periodoLibertaAnticipataList
	 */
	public List<PeriodoLibertaAnticipata> getPeriodoLibertaAnticipataList() {
		return periodoLibertaAnticipataList;
	}

	/**
	 * @param periodoLibertaAnticipataList
	 *            the periodoLibertaAnticipataList to set
	 */
	public void setPeriodoLibertaAnticipataList(List<PeriodoLibertaAnticipata> periodoLibertaAnticipataList) {
		this.periodoLibertaAnticipataList = periodoLibertaAnticipataList;
	}

}