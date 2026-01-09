package it.mig.sies.model;

import java.util.Date;
import java.util.List;

/**
 * MEV 16 - Classe model relativa ai dati del Pubblico Ministero
 * 
 * @author SIMONE GIOGGI
 *
 */
public class DatiPubblicoMinistero extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4663662798557067436L;

	private long chiaveSies;
	private long chiaveNSC;
	private Date dataProvvedimento;
	private String codiceAutorita;
	private String sedeAutoritaPrinDist;
	private String sedeAutoritaPrinc;
	private String codiceUnivocoProvvedimento;
	private String tipoProvvedimento;
	private int giorniArresto;
	private int mesiArresto;
	private int anniArresto;
	private int giorniReclusione;
	private int mesiReclusione;
	private int anniReclusione;
	private double importoAmmenda;
	private double importoMulta;
	private Date dataFinePena;
	private Date dataFinePenaDal;
	private Date dataFinePenaAl;
	private ProvvedimentoCollegato datiProvvCollegato;
	private String ergastolo;
	private int giorniIsolamentoDiurno;
	private int mesiIsolamentoDiurno;
	private int anniIsolamentoDiurno;
	// MEV 16 CUMULO: aggiunte liste MS & PA + altri dati
	private List<MisuraSicurezzaCumulo> listaMisureSicurezzaCumulo;
	private List<PenaAccessoriaCumulo> listaPeneAccessorieCumulo;
	private SanzioniSostitutiveCumulo sanzioniSostitutiveCumulo;
	private LiberazioneAnticipataCumulo liberazioneAnticipataCumulo;
	private ConversionePPCumulo conversionePPCumulo;
	private SanzioniGPCumulo sanzioniGPCumulo;
	private List<RichiesteGECumulo> listaRichiesteGECumulo;

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
	 * @return the giorniArresto
	 */
	public int getGiorniArresto() {
		return giorniArresto;
	}

	/**
	 * @param giorniArresto
	 *            the giorniArresto to set
	 */
	public void setGiorniArresto(int giorniArresto) {
		this.giorniArresto = giorniArresto;
	}

	/**
	 * @return the mesiArresto
	 */
	public int getMesiArresto() {
		return mesiArresto;
	}

	/**
	 * @param mesiArresto
	 *            the mesiArresto to set
	 */
	public void setMesiArresto(int mesiArresto) {
		this.mesiArresto = mesiArresto;
	}

	/**
	 * @return the anniArresto
	 */
	public int getAnniArresto() {
		return anniArresto;
	}

	/**
	 * @param anniArresto
	 *            the anniArresto to set
	 */
	public void setAnniArresto(int anniArresto) {
		this.anniArresto = anniArresto;
	}

	/**
	 * @return the giorniReclusione
	 */
	public int getGiorniReclusione() {
		return giorniReclusione;
	}

	/**
	 * @param giorniReclusione
	 *            the giorniReclusione to set
	 */
	public void setGiorniReclusione(int giorniReclusione) {
		this.giorniReclusione = giorniReclusione;
	}

	/**
	 * @return the mesiReclusione
	 */
	public int getMesiReclusione() {
		return mesiReclusione;
	}

	/**
	 * @param mesiReclusione
	 *            the mesiReclusione to set
	 */
	public void setMesiReclusione(int mesiReclusione) {
		this.mesiReclusione = mesiReclusione;
	}

	/**
	 * @return the anniReclusione
	 */
	public int getAnniReclusione() {
		return anniReclusione;
	}

	/**
	 * @param anniReclusione
	 *            the anniReclusione to set
	 */
	public void setAnniReclusione(int anniReclusione) {
		this.anniReclusione = anniReclusione;
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
	 * @return the datiProvvCollegato
	 */
	public ProvvedimentoCollegato getDatiProvvCollegato() {
		return datiProvvCollegato;
	}

	/**
	 * @param datiProvvCollegato
	 *            the datiProvvCollegato to set
	 */
	public void setDatiProvvCollegato(ProvvedimentoCollegato datiProvvCollegato) {
		this.datiProvvCollegato = datiProvvCollegato;
	}

	/**
	 * @return the dataFinePena
	 */
	public Date getDataFinePena() {
		return dataFinePena;
	}

	/**
	 * @param dataFinePena
	 *            the dataFinePena to set
	 */
	public void setDataFinePena(Date dataFinePena) {
		this.dataFinePena = dataFinePena;
	}

	/**
	 * @return the dataFinePenaDal
	 */
	public Date getDataFinePenaDal() {
		return dataFinePenaDal;
	}

	/**
	 * @param dataFinePenaDal
	 *            the dataFinePenaDal to set
	 */
	public void setDataFinePenaDal(Date dataFinePenaDal) {
		this.dataFinePenaDal = dataFinePenaDal;
	}

	/**
	 * @return the dataFinePenaAl
	 */
	public Date getDataFinePenaAl() {
		return dataFinePenaAl;
	}

	/**
	 * @param dataFinePenaAl
	 *            the dataFinePenaAl to set
	 */
	public void setDataFinePenaAl(Date dataFinePenaAl) {
		this.dataFinePenaAl = dataFinePenaAl;
	}

	/**
	 * @return the ergastolo
	 */
	public String getErgastolo() {
		return ergastolo;
	}

	/**
	 * @param ergastolo
	 *            the ergastolo to set
	 */
	public void setErgastolo(String ergastolo) {
		this.ergastolo = ergastolo;
	}

	/**
	 * @return the giorniIsolamentoDiurno
	 */
	public int getGiorniIsolamentoDiurno() {
		return giorniIsolamentoDiurno;
	}

	/**
	 * @param giorniIsolamentoDiurno
	 *            the giorniIsolamentoDiurno to set
	 */
	public void setGiorniIsolamentoDiurno(int giorniIsolamentoDiurno) {
		this.giorniIsolamentoDiurno = giorniIsolamentoDiurno;
	}

	/**
	 * @return the mesiIsolamentoDiurno
	 */
	public int getMesiIsolamentoDiurno() {
		return mesiIsolamentoDiurno;
	}

	/**
	 * @param mesiIsolamentoDiurno
	 *            the mesiIsolamentoDiurno to set
	 */
	public void setMesiIsolamentoDiurno(int mesiIsolamentoDiurno) {
		this.mesiIsolamentoDiurno = mesiIsolamentoDiurno;
	}

	/**
	 * @return the anniIsolamentoDiurno
	 */
	public int getAnniIsolamentoDiurno() {
		return anniIsolamentoDiurno;
	}

	/**
	 * @param anniIsolamentoDiurno
	 *            the anniIsolamentoDiurno to set
	 */
	public void setAnniIsolamentoDiurno(int anniIsolamentoDiurno) {
		this.anniIsolamentoDiurno = anniIsolamentoDiurno;
	}

	/**
	 * @return the listaMisureSicurezzaCumulo
	 */
	public List<MisuraSicurezzaCumulo> getListaMisureSicurezzaCumulo() {
		return listaMisureSicurezzaCumulo;
	}

	/**
	 * @param listaMisureSicurezzaCumulo
	 *            the listaMisureSicurezzaCumulo to set
	 */
	public void setListaMisureSicurezzaCumulo(List<MisuraSicurezzaCumulo> listaMisureSicurezzaCumulo) {
		this.listaMisureSicurezzaCumulo = listaMisureSicurezzaCumulo;
	}

	/**
	 * @return the listaPeneAccessorieCumulo
	 */
	public List<PenaAccessoriaCumulo> getListaPeneAccessorieCumulo() {
		return listaPeneAccessorieCumulo;
	}

	/**
	 * @param listaPeneAccessorieCumulo
	 *            the listaPeneAccessorieCumulo to set
	 */
	public void setListaPeneAccessorieCumulo(List<PenaAccessoriaCumulo> listaPeneAccessorieCumulo) {
		this.listaPeneAccessorieCumulo = listaPeneAccessorieCumulo;
	}

	/**
	 * @return the sanzioniSostitutiveCumulo
	 */
	public SanzioniSostitutiveCumulo getSanzioniSostitutiveCumulo() {
		return sanzioniSostitutiveCumulo;
	}

	/**
	 * @param sanzioniSostitutiveCumulo
	 *            the sanzioniSostitutiveCumulo to set
	 */
	public void setSanzioniSostitutiveCumulo(SanzioniSostitutiveCumulo sanzioniSostitutiveCumulo) {
		this.sanzioniSostitutiveCumulo = sanzioniSostitutiveCumulo;
	}

	/**
	 * @return the liberazioneAnticipataCumulo
	 */
	public LiberazioneAnticipataCumulo getLiberazioneAnticipataCumulo() {
		return liberazioneAnticipataCumulo;
	}

	/**
	 * @param liberazioneAnticipataCumulo
	 *            the liberazioneAnticipataCumulo to set
	 */
	public void setLiberazioneAnticipataCumulo(LiberazioneAnticipataCumulo liberazioneAnticipataCumulo) {
		this.liberazioneAnticipataCumulo = liberazioneAnticipataCumulo;
	}

	/**
	 * @return the conversionePPCumulo
	 */
	public ConversionePPCumulo getConversionePPCumulo() {
		return conversionePPCumulo;
	}

	/**
	 * @param conversionePPCumulo
	 *            the conversionePPCumulo to set
	 */
	public void setConversionePPCumulo(ConversionePPCumulo conversionePPCumulo) {
		this.conversionePPCumulo = conversionePPCumulo;
	}

	/**
	 * @return the sanzioniGPCumulo
	 */
	public SanzioniGPCumulo getSanzioniGPCumulo() {
		return sanzioniGPCumulo;
	}

	/**
	 * @param sanzioniGPCumulo
	 *            the sanzioniGPCumulo to set
	 */
	public void setSanzioniGPCumulo(SanzioniGPCumulo sanzioniGPCumulo) {
		this.sanzioniGPCumulo = sanzioniGPCumulo;
	}

	/**
	 * @return the listaRichiesteGECumulo
	 */
	public List<RichiesteGECumulo> getListaRichiesteGECumulo() {
		return listaRichiesteGECumulo;
	}

	/**
	 * @param listaRichiesteGECumulo
	 *            the listaRichiesteGECumulo to set
	 */
	public void setListaRichiesteGECumulo(List<RichiesteGECumulo> listaRichiesteGECumulo) {
		this.listaRichiesteGECumulo = listaRichiesteGECumulo;
	}

}