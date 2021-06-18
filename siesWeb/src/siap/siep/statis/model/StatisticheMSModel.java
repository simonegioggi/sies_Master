package siap.siep.statis.model;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: RiepilogoIscrizioniProcedimentiMisuraModel - MEV_39
 * </p>
 * <p>
 * Description: Classe Model che rappresenta la vista in oggetto
 * </p>
 */
public class StatisticheMSModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7852463639053914364L;

	private Vector<Integer> iscrittiParziali;
	private Vector<Integer> anni;
	private String tipoMS;
	private Vector<BigDecimal> progFasc;
	private BigDecimal totFasc;
	private BigDecimal anno;
	private Vector<String> periodi;
	private Integer conta;
	private String codMotivo;
	private String codMagistrato;
	private String descrMotivo;
	private String codAttivita;

	// COSTRUTTORE DI DEFAULT
	public StatisticheMSModel() {

		this.iscrittiParziali = null;
		this.anni = null;
		this.tipoMS = null;
		this.progFasc = null;
		this.totFasc = null;
		this.anno = null;
		this.periodi = null;
		this.conta = null;
		this.codMotivo = null;
		this.codMagistrato = null;
		this.descrMotivo = null;
		this.codAttivita = null;
	}

	// METODI GET & SET
	public Vector<Integer> getIscrittiParziali() {
		return iscrittiParziali;
	}

	public Vector<Integer> getAnni() {
		return anni;
	}

	public String getTipoMS() {
		return tipoMS;
	}

	public void setTipoMS(String tipoMS) {
		this.tipoMS = tipoMS;
	}

	public void setIscrittiParziali(Vector<Integer> iscrittiParziali) {
		this.iscrittiParziali = iscrittiParziali;
	}

	public void setAnni(Vector<Integer> anni) {
		this.anni = anni;
	}

	public BigDecimal getTotFasc() {
		return totFasc;
	}

	public void setTotFasc(BigDecimal totFasc) {
		this.totFasc = totFasc;
	}

	public Vector<BigDecimal> getProgFasc() {
		return progFasc;
	}

	public void setProgFasc(Vector<BigDecimal> progFasc) {
		this.progFasc = progFasc;
	}

	public BigDecimal getAnno() {
		return anno;
	}

	public void setAnno(BigDecimal anno) {
		this.anno = anno;
	}

	public Vector<String> getPeriodi() {
		return periodi;
	}

	public void setPeriodi(Vector<String> periodi) {
		this.periodi = periodi;
	}

	public Integer getConta() {
		return conta;
	}

	public void setConta(Integer conta) {
		this.conta = conta;
	}

	public String getCodMotivo() {
		return codMotivo;
	}

	public void setCodMotivo(String codMotivo) {
		this.codMotivo = codMotivo;
	}

	public String getCodMagistrato() {
		return codMagistrato;
	}

	public void setCodMagistrato(String codMagistrato) {
		this.codMagistrato = codMagistrato;
	}

	public String getDescrMotivo() {
		return descrMotivo;
	}

	public void setDescrMotivo(String descrMotivo) {
		this.descrMotivo = descrMotivo;
	}

	public String getCodAttivita() {
		return codAttivita;
	}

	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}

	
	public String toString () {
		String s = "";
		s+="StatisticheMSModel = \n"
				+ "[ tipoMS          = " + tipoMS + " ]\n"
				+ "[ anno;           = " + anno+ " ]\n"
				+ "[ totFasc         = " + totFasc + " ]\n"
				+ "[ progFasc <vect> = " + progFasc + " ]";
		
		return s;
	}
}