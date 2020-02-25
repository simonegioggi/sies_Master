<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.security.model.ProfileModel"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="soggetto"  scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="TornaQui"  scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lDataNascita = "DataNascita";
  String DataNascita = DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy");
%>

<table cellspacing="0" cellpadding="0" style="width: 95%;">
    <tr>
		<td class="L" width=100%>
			<font class="label">Soggetto:</font>&nbsp;
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&<%=ICostantiSoggetto.CAMPO_COGNOME%>=<%=soggetto.getCognome()%>&<%=ICostantiSoggetto.CAMPO_NOME%>=<%=soggetto.getNome()%>&<%=lDataNascita%>=<%=DataNascita%><%=retParam%>">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
// MERGE v10 COLLAUDO: modifica alla gestione del codice
// Si ricava il profilo dell'utente connesso
ProfileModel lProfilo = (ProfileModel)UtenteConnesso.getUserProfile();
UfficioModel lUfficio = UtenteConnesso.getUfficioUtente();
String codTipoUfficio = lUfficio.getCodTipoUfficio();

if (soggetto.getDataNascita() == null) {
	// 20190226 [SG]: invertito controllo su nascita presunta
   	if ("S".equals(soggetto.getDataNascitaPresunta())) {
    	if (soggetto.getSesso().compareTo("F")==0) {
%>
      		<font class="label">nata il :</font>&nbsp;
<%
    	} else {
%>
      		<font class="label">nato il :</font>&nbsp;
<%
    	}
    	
    	if(soggetto.getAnnoNascita() != null){
%>
        	<font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%
    	} else {
%>
			<font class="campo">**-**-****</font>&nbsp;
<%    	
   		}	
	// MERGE v10 COLLAUDO: modifica alla gestione del codice
	} else if (!lProfilo.isSius() && (soggetto.getEtaPresuntaAnni() != null || soggetto.getEtaPresuntaMesi() != null)) {
%>
      	<font class="label">Età Presunta: </font>
<%
		if (soggetto.getEtaPresuntaAnni() != null){
%>
			anni <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font>
<%
		}
		if (soggetto.getEtaPresuntaMesi() != null){
%>
			mesi <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font>
<%			
		}
%>

<%
	} else {
%>
      <font class="campo">**-**-****</font>&nbsp;
<%
	}
}else { // chiude if(soggetto.getDataNascita() == null){
	if (soggetto.getSesso().compareTo("F") == 0) {
%>
          <font class="label">nata il :</font>&nbsp;
<%
	} else {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }
%>
      <font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;

<%
} // chiude else presenza data nascita
if (soggetto.getDescrComuneNascita().compareTo("-") == 0) {
	if (Utils.isPresentNotTrattino(soggetto.getDescrStatoNascita())) {
%>

      <font class="label">in : </font>
   			<font class="campo"><%=soggetto.getDescrStatoNascita()%></font>
<%
      }
} else {
%>
			<font class="label">in : </font>
   			<font class="campo">
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
       		</font>
<%
      }
%>
		</td>
    </tr>
  </table>