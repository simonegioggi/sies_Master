package siap.sige.statistiche.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class StatisticheFogliComplementariModel extends GenericModel{

	
	private static final long serialVersionUID = 8221163619945799228L;
	
	private BigDecimal idFascicolo=null;
	private String descrFascicolo=null;
	private Date dataProvvedimento=null;
	private String dataFoglioComplementare=null;
	private String descrEsito=null;
	private String descrProvvedimento=null;
	private String dataProvvedimentoAsString=null;
	
	// Ticket#20230202011 - Aggiunte informazioni 
	private String annoNumeroProvvedimento=null;
	private String annoNumeroFoglioComplementare=null;
	public String getAnnoNumeroProvvedimento() {
		return annoNumeroProvvedimento;
	}
	public void setAnnoNumeroProvvedimento(String annoNumeroProvvedimento) {
		this.annoNumeroProvvedimento = annoNumeroProvvedimento;
	}
	public String getAnnoNumeroFoglioComplementare() {
		return annoNumeroFoglioComplementare;
	}
	public void setAnnoNumeroFoglioComplementare(String annoNumeroFoglioComplementare) {
		this.annoNumeroFoglioComplementare = annoNumeroFoglioComplementare;
	}
	// Ticket#20230202011 -	FINE
	
	
	public BigDecimal getIdFascicolo() {
		return idFascicolo;
	}
	public void setIdFascicolo(BigDecimal idFascicolo) {
		this.idFascicolo = idFascicolo;
	}
	public String getDescrFascicolo() {
		return descrFascicolo;
	}
	public void setDescrFascicolo(String descrFascicolo) {
		this.descrFascicolo = descrFascicolo;
	}
	public Date getDataProvvedimento() {
		return dataProvvedimento;
	}
	public void setDataProvvedimento(Date dataProvvedimento) {
		this.dataProvvedimento = dataProvvedimento;
	}
	public String getDataFoglioComplementare() {
		return dataFoglioComplementare;
	}
	public void setDataFoglioComplementare(String dataFoglioComplementare) {
		this.dataFoglioComplementare = dataFoglioComplementare;
	}
	public String getDescrEsito() {
		return descrEsito;
	}
	public void setDescrEsito(String descrEsito) {
		this.descrEsito = descrEsito;
	}
	public String getDescrProvvedimento() {
		return descrProvvedimento;
	}
	public void setDescrProvvedimento(String descrProvvedimento) {
		this.descrProvvedimento = descrProvvedimento;
	}
	public String getDataProvvedimentoAsString() {
		if (this.dataProvvedimento==null)
			return "-";
					
		dataProvvedimentoAsString=DateUtils.getDateToString(this.dataProvvedimento, "dd-MM-yyyy");			
		return dataProvvedimentoAsString;
	}
	public void setDataProvvedimentoAsString(String dataProvvedimentoAsString) {
		this.dataProvvedimentoAsString = dataProvvedimentoAsString;
	}

}
