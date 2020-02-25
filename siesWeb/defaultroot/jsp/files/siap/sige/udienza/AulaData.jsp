<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="f3b.util.F3BException"%>
<%@ page import="siap.sige.magistrato.controller.IMagistrato"%>
<%@ page import="siap.sige.aula.controller.IAula"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sige.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="siap.sige.util.SIGELookupRemote" %>
<%@ page import="siap.sige.udienza.controller.IUdienzaSige" %>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel" %>
<%@ page import="siap.sige.aula.model.AulaUdienzaModel" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
	// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
	// ufficio differente da quello in cui ha delle udienze poichè trasferito
	UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	String codUfficioAppartenenza = lUteMod.getUfficioUtente().getCodUfficio();
    String idUdienza=request.getParameter("idUdienza"); 
    IUdienzaSige lUdi = SIGELookupRemote.getUdienzaSigeRemote();
	UdienzaSigeModel udienza = lUdi.ExRicercaUdienzaSigeById(new BigDecimal(idUdienza));
	
	JSONObject jsObj = new JSONObject();
	FascicoloSigeEstesoModel fascicoloEsteso  = (FascicoloSigeEstesoModel) request.getSession().getAttribute ("FascicoloSigeEsteso"); 
    AulaUdienzaModel aulaUdienzaModel=getAulaUdienzaModel(udienza, fascicoloEsteso, codUfficioAppartenenza);
    
    String	aula=(aulaUdienzaModel.getDescrizioneStanza()==null?"":aulaUdienzaModel.getDescrizioneAula());
    String	ingresso=(aulaUdienzaModel.getDescrizioneIngresso()==null?"":aulaUdienzaModel.getDescrizioneIngresso());
    String 	piano=(aulaUdienzaModel.getNumeroPiano()==null?"":aulaUdienzaModel.getNumeroPiano().toString());
    String	idAula=(aulaUdienzaModel.getIdAula()==null?"":aulaUdienzaModel.getIdAula().toString());
    String idSezione=aulaUdienzaModel.getIdSezione().toString();

    String oraInizio = (udienza.getOraInizio()== null || udienza.getOraInizio().equalsIgnoreCase("NULL")?"":udienza.getOraInizio());
    String minInizio = (udienza.getMinInizio()== null || udienza.getMinInizio().equalsIgnoreCase("NULL")?"":udienza.getMinInizio());
    String oraFine = (udienza.getOraFine()== null || udienza.getOraFine().equalsIgnoreCase("NULL")?"":udienza.getOraFine());
    String minFine = (udienza.getMinFine()== null || udienza.getMinFine().equalsIgnoreCase("NULL")?"":udienza.getMinFine());
 
    jsObj.put("oraInizio", oraInizio);
    jsObj.put("minInizio", minInizio);
    jsObj.put("oraFine", oraFine);
    jsObj.put("minFine", minFine);
    
    jsObj.put("idSezione", idSezione);
    
    jsObj.put("aula", aula);
    jsObj.put("ingresso", ingresso);
    jsObj.put("piano", piano);
    jsObj.put("idAula", piano);
    out.println (jsObj.toString() );
%>
<%!
private AulaUdienzaModel getAulaUdienzaModel (UdienzaSigeModel udienza, FascicoloSigeEstesoModel fascicoloEsteso, String codUfficioAppartenenza) throws F3BException{
	 AulaUdienzaModel aulaUdienzaModel=udienza.getAulaUdienzaModel();
	 if (aulaUdienzaModel != null) return aulaUdienzaModel;
	 
	 return getAulaPredefinita(fascicoloEsteso, codUfficioAppartenenza);
}

private AulaUdienzaModel getAulaPredefinita (FascicoloSigeEstesoModel fascicoloEsteso, String codUfficioAppartenenza) throws F3BException{
	 String idSezione=this.getIdSezione(fascicoloEsteso, codUfficioAppartenenza);
	 AulaUdienzaModel aula=getAulaUdienzaSige(idSezione);
	 if (aula==null) {
		 aula = new AulaUdienzaModel();
		 aula.setIdSezione(new BigDecimal (idSezione));
	 }
	 
	 return aula;
}

private String getIdSezione (FascicoloSigeEstesoModel mFasEsteso, String codUfficioAppartenenza) throws F3BException {
	String lSezioneUdienza="-"; 
	IMagistratoAssegnatario lMagCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
    MagistratoAssegnatarioModel lMagAss = lMagCtrl.ExRicercaEstesaMagAssCorrenteXFascicolo(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
    if (lMagAss != null) {
	    IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
		// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
		// ufficio differente da quello in cui ha delle udienze poichè trasferito
		MagistratoModel lMagMod = lCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(), codUfficioAppartenenza);
		if (lMagMod.getMagistratoSezioni().length > 0) {
		    BigDecimal idSezMag = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
			if (idSezMag != null) {
			    lSezioneUdienza = idSezMag.toString();
			}
		}
	}
	return lSezioneUdienza;
}

private AulaUdienzaModel getAulaUdienzaSige(String sezione) throws F3BException{
	AulaUdienzaModel aulaUdienza = null;
	IAula lCtrl = SIGELookupRemote.getAulaRemote();
	aulaUdienza = lCtrl.ExRicercaAulaPredefinitaSezione(sezione);
	return aulaUdienza;
}

%>