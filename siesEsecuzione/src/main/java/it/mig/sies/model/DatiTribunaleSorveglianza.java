package it.mig.sies.model;

import java.util.Date;
import java.util.List;

/**
 * SIES FASE 2 - Classe model relativa ai dati del Tribunale di Sorveglianza
 * 
 * @author Federico Paparoni
 */
public class DatiTribunaleSorveglianza extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4979079497479840580L;

	private long chiaveSies;
	private long chiaveNSC;
	private Date dataProvvedimento;
	private String codiceAutorita;
	private String sedeAutoritaPrinDist;
	private String sedeAutoritaPrinc;
	private String codiceUnivocoProvvedimento;
	private String tipoProvvedimento;
	private int anniDurataMisura;
	private int mesiDurataMisura;
	private int giorniDurataMisura;
	private Date dataTermineMisura;
	private Date dataDecorrenzaRevoca;
	private int anniPenaRidetArresto;
	private int mesiPenaRidetArresto;
	private int giorniPenaRidetArresto;
	private int anniPenaRidetReclusione;
	private int mesiPenaRidetReclusione;
	private int giorniPenaRidetReclusione;
	private Date dataFineBeneficio;
	private int giorniDurataBeneficio;
	private int mesiDurataBeneficio;
	private int anniDurataBeneficio;
	private Date dataInizioNonEspiata;
	private Date dataFineNonEspiata;
	private int giorniPenaRideterminata;
	private int mesiPenaRideterminata;
	private int anniPenaRideterminata;

	private Date dataInizioDifferimentoPena;
	private Date dataFineDifferimentoPena;
	private Date dataInizioRevoca;
	private int giorniPenaDetArresto;
	private int mesiPenaDetArresto;
	private int anniPenaDetArresto;
	private int giorniPenaDetReclusione;
	private int mesiPenaDetReclusione;
	private int anniPenaDetReclusione;
	private int numGiorniLibertaAnticipata;
	private int numGiorniLibertaAnticipataLs;
	private int numGiorniLibertaAnticipataLi;
	private String note;
	private List<PeriodoLibertaAnticipata> periodoLibertaAnticipataList;

	// MEV 23010 - Viene mappato anche l'id SIES per il provv collegato
	private ProvvedimentoCollegato provvedimentoCollegato;

	/**
	 * @return the provvedimentoCollegato
	 */
	public ProvvedimentoCollegato getProvvedimentoCollegato() {
		return provvedimentoCollegato;
	}

	/**
	 * @param provvedimentoCollegato
	 *            the provvedimentoCollegato to set
	 */
	public void setProvvedimentoCollegato(ProvvedimentoCollegato provvedimentoCollegato) {
		this.provvedimentoCollegato = provvedimentoCollegato;
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
	 * @return the anniDurataMisura
	 */
	public int getAnniDurataMisura() {
		return anniDurataMisura;
	}

	/**
	 * @param anniDurataMisura
	 *            the anniDurataMisura to set
	 */
	public void setAnniDurataMisura(int anniDurataMisura) {
		this.anniDurataMisura = anniDurataMisura;
	}

	/**
	 * @return the mesiDurataMisura
	 */
	public int getMesiDurataMisura() {
		return mesiDurataMisura;
	}

	/**
	 * @param mesiDurataMisura
	 *            the mesiDurataMisura to set
	 */
	public void setMesiDurataMisura(int mesiDurataMisura) {
		this.mesiDurataMisura = mesiDurataMisura;
	}

	/**
	 * @return the giorniDurataMisura
	 */
	public int getGiorniDurataMisura() {
		return giorniDurataMisura;
	}

	/**
	 * @param giorniDurataMisura
	 *            the giorniDurataMisura to set
	 */
	public void setGiorniDurataMisura(int giorniDurataMisura) {
		this.giorniDurataMisura = giorniDurataMisura;
	}

	/**
	 * @return the dataTermineMisura
	 */
	public Date getDataTermineMisura() {
		return dataTermineMisura;
	}

	/**
	 * @param dataTermineMisura
	 *            the dataTermineMisura to set
	 */
	public void setDataTermineMisura(Date dataTermineMisura) {
		this.dataTermineMisura = dataTermineMisura;
	}

	/**
	 * @return the dataDecorrenzaRevoca
	 */
	public Date getDataDecorrenzaRevoca() {
		return dataDecorrenzaRevoca;
	}

	/**
	 * @param dataDecorrenzaRevoca
	 *            the dataDecorrenzaRevoca to set
	 */
	public void setDataDecorrenzaRevoca(Date dataDecorrenzaRevoca) {
		this.dataDecorrenzaRevoca = dataDecorrenzaRevoca;
	}

	/**
	 * @return the anniPenaRidetArresto
	 */
	public int getAnniPenaRidetArresto() {
		return anniPenaRidetArresto;
	}

	/**
	 * @param anniPenaRidetArresto
	 *            the anniPenaRidetArresto to set
	 */
	public void setAnniPenaRidetArresto(int anniPenaRidetArresto) {
		this.anniPenaRidetArresto = anniPenaRidetArresto;
	}

	/**
	 * @return the mesiPenaRidetArresto
	 */
	public int getMesiPenaRidetArresto() {
		return mesiPenaRidetArresto;
	}

	/**
	 * @param mesiPenaRidetArresto
	 *            the mesiPenaRidetArresto to set
	 */
	public void setMesiPenaRidetArresto(int mesiPenaRidetArresto) {
		this.mesiPenaRidetArresto = mesiPenaRidetArresto;
	}

	/**
	 * @return the giorniPenaRidetArresto
	 */
	public int getGiorniPenaRidetArresto() {
		return giorniPenaRidetArresto;
	}

	/**
	 * @param giorniPenaRidetArresto
	 *            the giorniPenaRidetArresto to set
	 */
	public void setGiorniPenaRidetArresto(int giorniPenaRidetArresto) {
		this.giorniPenaRidetArresto = giorniPenaRidetArresto;
	}

	/**
	 * @return the anniPenaRidetReclusione
	 */
	public int getAnniPenaRidetReclusione() {
		return anniPenaRidetReclusione;
	}

	/**
	 * @param anniPenaRidetReclusione
	 *            the anniPenaRidetReclusione to set
	 */
	public void setAnniPenaRidetReclusione(int anniPenaRidetReclusione) {
		this.anniPenaRidetReclusione = anniPenaRidetReclusione;
	}

	/**
	 * @return the mesiPenaRidetReclusione
	 */
	public int getMesiPenaRidetReclusione() {
		return mesiPenaRidetReclusione;
	}

	/**
	 * @param mesiPenaRidetReclusione
	 *            the mesiPenaRidetReclusione to set
	 */
	public void setMesiPenaRidetReclusione(int mesiPenaRidetReclusione) {
		this.mesiPenaRidetReclusione = mesiPenaRidetReclusione;
	}

	/**
	 * @return the giorniPenaRidetReclusione
	 */
	public int getGiorniPenaRidetReclusione() {
		return giorniPenaRidetReclusione;
	}

	/**
	 * @param giorniPenaRidetReclusione
	 *            the giorniPenaRidetReclusione to set
	 */
	public void setGiorniPenaRidetReclusione(int giorniPenaRidetReclusione) {
		this.giorniPenaRidetReclusione = giorniPenaRidetReclusione;
	}

	/**
	 * @return the dataFineBeneficio
	 */
	public Date getDataFineBeneficio() {
		return dataFineBeneficio;
	}

	/**
	 * @param dataFineBeneficio
	 *            the dataFineBeneficio to set
	 */
	public void setDataFineBeneficio(Date dataFineBeneficio) {
		this.dataFineBeneficio = dataFineBeneficio;
	}

	/**
	 * @return the giorniDurataBeneficio
	 */
	public int getGiorniDurataBeneficio() {
		return giorniDurataBeneficio;
	}

	/**
	 * @param giorniDurataBeneficio
	 *            the giorniDurataBeneficio to set
	 */
	public void setGiorniDurataBeneficio(int giorniDurataBeneficio) {
		this.giorniDurataBeneficio = giorniDurataBeneficio;
	}

	/**
	 * @return the mesiDurataBeneficio
	 */
	public int getMesiDurataBeneficio() {
		return mesiDurataBeneficio;
	}

	/**
	 * @param mesiDurataBeneficio
	 *            the mesiDurataBeneficio to set
	 */
	public void setMesiDurataBeneficio(int mesiDurataBeneficio) {
		this.mesiDurataBeneficio = mesiDurataBeneficio;
	}

	/**
	 * @return the anniDurataBeneficio
	 */
	public int getAnniDurataBeneficio() {
		return anniDurataBeneficio;
	}

	/**
	 * @param anniDurataBeneficio
	 *            the anniDurataBeneficio to set
	 */
	public void setAnniDurataBeneficio(int anniDurataBeneficio) {
		this.anniDurataBeneficio = anniDurataBeneficio;
	}

	/**
	 * @return the dataInizioNonEspiata
	 */
	public Date getDataInizioNonEspiata() {
		return dataInizioNonEspiata;
	}

	/**
	 * @param dataInizioNonEspiata
	 *            the dataInizioNonEspiata to set
	 */
	public void setDataInizioNonEspiata(Date dataInizioNonEspiata) {
		this.dataInizioNonEspiata = dataInizioNonEspiata;
	}

	/**
	 * @return the dataFineNonEspiata
	 */
	public Date getDataFineNonEspiata() {
		return dataFineNonEspiata;
	}

	/**
	 * @param dataFineNonEspiata
	 *            the dataFineNonEspiata to set
	 */
	public void setDataFineNonEspiata(Date dataFineNonEspiata) {
		this.dataFineNonEspiata = dataFineNonEspiata;
	}

	/**
	 * @return the giorniPenaRideterminata
	 */
	public int getGiorniPenaRideterminata() {
		return giorniPenaRideterminata;
	}

	/**
	 * @param giorniPenaRideterminata
	 *            the giorniPenaRideterminata to set
	 */
	public void setGiorniPenaRideterminata(int giorniPenaRideterminata) {
		this.giorniPenaRideterminata = giorniPenaRideterminata;
	}

	/**
	 * @return the mesiPenaRideterminata
	 */
	public int getMesiPenaRideterminata() {
		return mesiPenaRideterminata;
	}

	/**
	 * @param mesiPenaRideterminata
	 *            the mesiPenaRideterminata to set
	 */
	public void setMesiPenaRideterminata(int mesiPenaRideterminata) {
		this.mesiPenaRideterminata = mesiPenaRideterminata;
	}

	/**
	 * @return the anniPenaRideterminata
	 */
	public int getAnniPenaRideterminata() {
		return anniPenaRideterminata;
	}

	/**
	 * @param anniPenaRideterminata
	 *            the anniPenaRideterminata to set
	 */
	public void setAnniPenaRideterminata(int anniPenaRideterminata) {
		this.anniPenaRideterminata = anniPenaRideterminata;
	}

	/**
	 * @return the dataInizioDifferimentoPena
	 */
	public Date getDataInizioDifferimentoPena() {
		return dataInizioDifferimentoPena;
	}

	/**
	 * @param dataInizioDifferimentoPena
	 *            the dataInizioDifferimentoPena to set
	 */
	public void setDataInizioDifferimentoPena(Date dataInizioDifferimentoPena) {
		this.dataInizioDifferimentoPena = dataInizioDifferimentoPena;
	}

	/**
	 * @return the dataFineDifferimentoPena
	 */
	public Date getDataFineDifferimentoPena() {
		return dataFineDifferimentoPena;
	}

	/**
	 * @param dataFineDifferimentoPena
	 *            the dataFineDifferimentoPena to set
	 */
	public void setDataFineDifferimentoPena(Date dataFineDifferimentoPena) {
		this.dataFineDifferimentoPena = dataFineDifferimentoPena;
	}

	/**
	 * @return the dataInizioRevoca
	 */
	public Date getDataInizioRevoca() {
		return dataInizioRevoca;
	}

	/**
	 * @param dataInizioRevoca
	 *            the dataInizioRevoca to set
	 */
	public void setDataInizioRevoca(Date dataInizioRevoca) {
		this.dataInizioRevoca = dataInizioRevoca;
	}

	/**
	 * @return the giorniPenaDetArresto
	 */
	public int getGiorniPenaDetArresto() {
		return giorniPenaDetArresto;
	}

	/**
	 * @param giorniPenaDetArresto
	 *            the giorniPenaDetArresto to set
	 */
	public void setGiorniPenaDetArresto(int giorniPenaDetArresto) {
		this.giorniPenaDetArresto = giorniPenaDetArresto;
	}

	/**
	 * @return the mesiPenaDetArresto
	 */
	public int getMesiPenaDetArresto() {
		return mesiPenaDetArresto;
	}

	/**
	 * @param mesiPenaDetArresto
	 *            the mesiPenaDetArresto to set
	 */
	public void setMesiPenaDetArresto(int mesiPenaDetArresto) {
		this.mesiPenaDetArresto = mesiPenaDetArresto;
	}

	/**
	 * @return the anniPenaDetArresto
	 */
	public int getAnniPenaDetArresto() {
		return anniPenaDetArresto;
	}

	/**
	 * @param anniPenaDetArresto
	 *            the anniPenaDetArresto to set
	 */
	public void setAnniPenaDetArresto(int anniPenaDetArresto) {
		this.anniPenaDetArresto = anniPenaDetArresto;
	}

	/**
	 * @return the giorniPenaDetReclusione
	 */
	public int getGiorniPenaDetReclusione() {
		return giorniPenaDetReclusione;
	}

	/**
	 * @param giorniPenaDetReclusione
	 *            the giorniPenaDetReclusione to set
	 */
	public void setGiorniPenaDetReclusione(int giorniPenaDetReclusione) {
		this.giorniPenaDetReclusione = giorniPenaDetReclusione;
	}

	/**
	 * @return the mesiPenaDetReclusione
	 */
	public int getMesiPenaDetReclusione() {
		return mesiPenaDetReclusione;
	}

	/**
	 * @param mesiPenaDetReclusione
	 *            the mesiPenaDetReclusione to set
	 */
	public void setMesiPenaDetReclusione(int mesiPenaDetReclusione) {
		this.mesiPenaDetReclusione = mesiPenaDetReclusione;
	}

	/**
	 * @return the anniPenaDetReclusione
	 */
	public int getAnniPenaDetReclusione() {
		return anniPenaDetReclusione;
	}

	/**
	 * @param anniPenaDetReclusione
	 *            the anniPenaDetReclusione to set
	 */
	public void setAnniPenaDetReclusione(int anniPenaDetReclusione) {
		this.anniPenaDetReclusione = anniPenaDetReclusione;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
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

	/**
	 * @return the numGiorniLibertaAnticipata
	 */
	public int getGiorniLibertaAnticipata() {
		return numGiorniLibertaAnticipata;
	}

	/**
	 * @param numGiorniLibertaAnticipata
	 *            the numGiorniLibertaAnticipata to set
	 */
	public void setGiorniLibertaAnticipata(int numGiorniLibertaAnticipata) {
		this.numGiorniLibertaAnticipata = numGiorniLibertaAnticipata;
	}

	/**
	 * @return the numGiorniLibertaAnticipataLs
	 */
	public int getGiorniLibertaAnticipataLs() {
		return numGiorniLibertaAnticipataLs;
	}

	/**
	 * @param numGiorniLibertaAnticipataLs
	 *            the numGiorniLibertaAnticipataLs to set
	 */
	public void setGiorniLibertaAnticipataLs(int numGiorniLibertaAnticipataLs) {
		this.numGiorniLibertaAnticipataLs = numGiorniLibertaAnticipataLs;
	}

	/**
	 * @return the numGiorniLibertaAnticipataLi
	 */
	public int getGiorniLibertaAnticipataLi() {
		return numGiorniLibertaAnticipataLi;
	}

	/**
	 * @param numGiorniLibertaAnticipataLi
	 *            the numGiorniLibertaAnticipataLi to set
	 */
	public void setGiorniLibertaAnticipataLi(int numGiorniLibertaAnticipataLi) {
		this.numGiorniLibertaAnticipataLi = numGiorniLibertaAnticipataLi;
	}

}