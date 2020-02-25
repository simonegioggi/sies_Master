package siap.siep.statistiche.model;

import java.util.Iterator;
import java.util.Vector;

import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import f3b.model.GenericModel;

public class StatisticheFogliComplementariContainerModel extends GenericModel{
   
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9219961180249944307L;
	private Vector<StatisticheFogliComplementariModel> fcIscrittiManualmente=null;
    private Vector<StatisticheFogliComplementariModel> fcAnnullati=null;
    private Vector<StatisticheFogliComplementariModel> provvedimentiPriviFc=null;
    private Vector<StatisticheFogliComplementariModel> provvedimentiConFc=null;
    private Vector<StatisticheFogliComplementariModel> fcTrasmessi=null;
    private Vector<StatisticheFogliComplementariModel> fcNonTrasmessi=null;
    private Vector<StatisticheFogliComplementariModel> fcTrasmessiConErrore=null;
    
	private UfficioModel uffUteConnesso=null;
    private UtenteModel utenteConnesso=null;
    private RicercaFogliCompModel filtro=null;
    private Vector <RiepilogoStatisticheFogliComplementari> riepilogoAnnullati=null;
    private Vector <RiepilogoStatisticheFogliComplementari> riepilogoProvvedimentiPriviFC=null;
    private Vector <RiepilogoStatisticheFogliComplementari> riepilogFCIscrittiManualmente=null;
    private Vector <RiepilogoStatisticheFogliComplementari> riepilogoProvvedimentiConFC=null;
    private Vector <RiepilogoStatisticheFogliComplementari> riepilogoFCNonTrasmessi=null;
    private Vector <RiepilogoStatisticheFogliComplementari> riepilogoFCTrasmessiConErrore=null;
    
	private Vector <StatisticheFogliComplementariModel> elenco=null;
    
    private Vector <String> testataRiepilogo=null;   
    
	
    public Vector<StatisticheFogliComplementariModel> getFcIscrittiManualmente() {
		return fcIscrittiManualmente;
	}
	public void setFcIscrittiManualmente(
			Vector<StatisticheFogliComplementariModel> fcIscrittiManualmente) {
		this.fcIscrittiManualmente = fcIscrittiManualmente;
	}
	public Vector<StatisticheFogliComplementariModel> getFcAnnullati() {
		return fcAnnullati;
	}
	public void setFcAnnullati(
			Vector<StatisticheFogliComplementariModel> fcAnnullati) {
		this.fcAnnullati = fcAnnullati;
	}
	public Vector<StatisticheFogliComplementariModel> getProvvedimentiPriviFc() {
		return provvedimentiPriviFc;
	}
	public void setProvvedimentiPriviFc(
			Vector<StatisticheFogliComplementariModel> provvedimentiPriviFc) {
		this.provvedimentiPriviFc = provvedimentiPriviFc;
	}
	public UfficioModel getUffUteConnesso() {
		return uffUteConnesso;
	}
	public void setUffUteConnesso(UfficioModel uffUteConnesso) {
		this.uffUteConnesso = uffUteConnesso;
	}
	public RicercaFogliCompModel getFiltro() {
		return filtro;
	}
	public void setFiltro(RicercaFogliCompModel filtro) {
		this.filtro = filtro;
	}
	public Vector<RiepilogoStatisticheFogliComplementari> getRiepilogoAnnullati() {
		if (riepilogoAnnullati == null)
			riepilogoAnnullati=new Vector <RiepilogoStatisticheFogliComplementari> ();
			
		return riepilogoAnnullati;
	}
	public void setRiepilogoAnnullati(
			Vector<RiepilogoStatisticheFogliComplementari> riepilogoAnnullati) {
		this.riepilogoAnnullati = riepilogoAnnullati;
	}
	public Vector<String> getTestataRiepilogo() {
		if (this.testataRiepilogo != null)
		    return this.testataRiepilogo;
		
		this.testataRiepilogo=new Vector <String> ();
		Vector <RiepilogoStatisticheFogliComplementari> appo=null;
		if (this.riepilogoAnnullati != null) {
			appo=this.riepilogoAnnullati;
		}
		
		if (this.riepilogoProvvedimentiPriviFC != null) {
			appo=this.riepilogoProvvedimentiPriviFC;
		}
		
		if (this.riepilogFCIscrittiManualmente != null) {
			appo=this.riepilogFCIscrittiManualmente;
		}
		
		if (this.riepilogoProvvedimentiConFC != null) {
			appo=this.riepilogoProvvedimentiConFC;
		}
		
		if (this.riepilogoFCNonTrasmessi != null) {
			appo=this.riepilogoFCNonTrasmessi;
		}
		
		if (this.riepilogoFCTrasmessiConErrore != null) {
			appo=this.riepilogoFCTrasmessiConErrore;
		}

		this.testataRiepilogo.add("Riepilogo Fogli Complementari");
		Iterator<RiepilogoStatisticheFogliComplementari> it=appo.iterator();
		while (it.hasNext()) {
			RiepilogoStatisticheFogliComplementari riepilogo=it.next();
			testataRiepilogo.add(riepilogo.getAnno().toString());
		}

		return testataRiepilogo;
	}
	public void setTestataRiepilogo(Vector<String> testataRiepilogo) {
		this.testataRiepilogo = testataRiepilogo;
	}
	public Vector<RiepilogoStatisticheFogliComplementari> getRiepilogFCIscrittiManualmente() {
		if (riepilogFCIscrittiManualmente == null)
			riepilogFCIscrittiManualmente = new Vector <RiepilogoStatisticheFogliComplementari>();
		
		return riepilogFCIscrittiManualmente;
	}
	
	public void setRiepilogFCIscrittiManualmente(Vector<RiepilogoStatisticheFogliComplementari> riepilogFCIscrittiManualmente) {
		this.riepilogFCIscrittiManualmente = riepilogFCIscrittiManualmente;
	}
	public Vector<RiepilogoStatisticheFogliComplementari> getRiepilogoProvvedimentiPriviFC() {
		if (riepilogoProvvedimentiPriviFC == null)
			riepilogoProvvedimentiPriviFC=new Vector <RiepilogoStatisticheFogliComplementari>();
		
		return riepilogoProvvedimentiPriviFC;
	}
	
	public void setRiepilogoProvvedimentiPriviFC(Vector<RiepilogoStatisticheFogliComplementari> riepilogoProvvedimentiPriviFC) {
		this.riepilogoProvvedimentiPriviFC = riepilogoProvvedimentiPriviFC;
	}
	public Vector<StatisticheFogliComplementariModel> getProvvedimentiConFc() {
		//if (provvedimentiConFc == null)
			//provvedimentiConFc = new Vector <StatisticheFogliComplementariModel>();
		return provvedimentiConFc;
	}
	public void setProvvedimentiConFc(
			Vector<StatisticheFogliComplementariModel> provvedimentiConFc) {
		this.provvedimentiConFc = provvedimentiConFc;
	}
	
	public Vector<StatisticheFogliComplementariModel> getFcTrasmessi() {
		return fcTrasmessi;
	}
	public void setFcTrasmessi(
			Vector<StatisticheFogliComplementariModel> fcTrasmessi) {
		this.fcTrasmessi = fcTrasmessi;
	}
	public Vector<RiepilogoStatisticheFogliComplementari> getRiepilogoProvvedimentiConFC() {
		if (riepilogoProvvedimentiConFC == null)
			riepilogoProvvedimentiConFC = new Vector <RiepilogoStatisticheFogliComplementari>();
		
		return riepilogoProvvedimentiConFC;
	}
	public void setRiepilogoProvvedimentiConFC(
			Vector<RiepilogoStatisticheFogliComplementari> riepilogoProvvedimentiConFC) {
		this.riepilogoProvvedimentiConFC = riepilogoProvvedimentiConFC;
	}
	public UtenteModel getUtenteConnesso() {
		return utenteConnesso;
	}
	public void setUtenteConnesso(UtenteModel utenteConnesso) {
		this.utenteConnesso = utenteConnesso;
	}
	public Vector<StatisticheFogliComplementariModel> getElenco() {
		return elenco;
	}
	public void setElenco(Vector<StatisticheFogliComplementariModel> elenco) {
		this.elenco = elenco;
	}
	public Vector<StatisticheFogliComplementariModel> getFcNonTrasmessi() {
		return fcNonTrasmessi;
	}
	public void setFcNonTrasmessi(
			Vector<StatisticheFogliComplementariModel> fcNonTrasmessi) {
		this.fcNonTrasmessi = fcNonTrasmessi;
	}
	public Vector<RiepilogoStatisticheFogliComplementari> getRiepilogoFCNonTrasmessi() {
		if (riepilogoFCNonTrasmessi == null)
			riepilogoFCNonTrasmessi=new Vector <RiepilogoStatisticheFogliComplementari>();
		return riepilogoFCNonTrasmessi;
	}
	public void setRiepilogoFCNonTrasmessi(
			Vector<RiepilogoStatisticheFogliComplementari> riepilogoFCNonTrasmessi) {
		this.riepilogoFCNonTrasmessi = riepilogoFCNonTrasmessi;
	}
	public Vector<StatisticheFogliComplementariModel> getFcTrasmessiConErrore() {
		return fcTrasmessiConErrore;
	}
	public void setFcTrasmessiConErrore(
			Vector<StatisticheFogliComplementariModel> fcTrasmessiConErrore) {
		this.fcTrasmessiConErrore = fcTrasmessiConErrore;
	}
	public Vector<RiepilogoStatisticheFogliComplementari> getRiepilogoFCTrasmessiConErrore() {
		if (riepilogoFCTrasmessiConErrore==null)
			riepilogoFCTrasmessiConErrore = new Vector <RiepilogoStatisticheFogliComplementari>();
		
		return riepilogoFCTrasmessiConErrore;
	}
	public void setRiepilogoFCTrasmessiConErrore(
			Vector<RiepilogoStatisticheFogliComplementari> riepilogoFCTrasmessiConErrore) {
		this.riepilogoFCTrasmessiConErrore = riepilogoFCTrasmessiConErrore;
	}
}
