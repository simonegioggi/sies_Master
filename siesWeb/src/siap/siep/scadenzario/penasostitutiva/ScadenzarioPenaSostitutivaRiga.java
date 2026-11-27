package siap.siep.scadenzario.penasostitutiva;

import java.io.Serializable;
import java.util.Date;

// 20261124-MEV_2026-124_SG - riga dello scadenzario delle pene sostitutive (semilibertà / detenzione domiciliare sostitutive)
public class ScadenzarioPenaSostitutivaRiga implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idFascicoloSiep;
	private String chiaveAnno;
	private String chiaveProgr;
	private String cognome;
	private String nome;
	private String luogoNascita;
	private Date dataNascita;
	private String codTipoSanzione;
	private Date dataInizio;
	private Date dataFine;
	private long giorniResidui;

	public String getIdFascicoloSiep() { return idFascicoloSiep; }
	public void setIdFascicoloSiep(String v) { idFascicoloSiep = v; }
	public String getChiaveAnno() { return chiaveAnno; }
	public void setChiaveAnno(String v) { chiaveAnno = v; }
	public String getChiaveProgr() { return chiaveProgr; }
	public void setChiaveProgr(String v) { chiaveProgr = v; }
	public String getCognome() { return cognome; }
	public void setCognome(String v) { cognome = v; }
	public String getNome() { return nome; }
	public void setNome(String v) { nome = v; }
	public String getLuogoNascita() { return luogoNascita; }
	public void setLuogoNascita(String v) { luogoNascita = v; }
	public Date getDataNascita() { return dataNascita; }
	public void setDataNascita(Date v) { dataNascita = v; }
	public String getCodTipoSanzione() { return codTipoSanzione; }
	public void setCodTipoSanzione(String v) { codTipoSanzione = v; }
	public Date getDataInizio() { return dataInizio; }
	public void setDataInizio(Date v) { dataInizio = v; }
	public Date getDataFine() { return dataFine; }
	public void setDataFine(Date v) { dataFine = v; }
	public long getGiorniResidui() { return giorniResidui; }
	public void setGiorniResidui(long v) { giorniResidui = v; }
}
