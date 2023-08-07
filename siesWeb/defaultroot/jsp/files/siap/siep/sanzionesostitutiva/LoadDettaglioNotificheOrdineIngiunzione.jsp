<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.pagoPA.model.CivilmenteObbligatoModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="ordineIngiunzione"   	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="comboAutNotifica"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="notificaAlCondannato" 	scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="listaNotAvvSiep"      	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="lListaNotObbligati"   	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="notifichePending"   	scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

String listaIdNotAvv = "";
for (int i = 0; i < listaNotAvvSiep.size(); i++) 
	listaIdNotAvv += "," + ((NotificaModel) listaNotAvvSiep.get(i)).getIdNotifica();
if (listaNotAvvSiep.size() > 0)
	listaIdNotAvv = "[" + listaIdNotAvv.substring(1) + "]";
else
	listaIdNotAvv = "[]";

String listaIdNotObbl = "";
for (int i = 0; i < lListaNotObbligati.size(); i++) 
	listaIdNotObbl += "," + ((NotificaModel) lListaNotObbligati.get(i)).getIdNotifica();
if (lListaNotObbligati.size() > 0)
    listaIdNotObbl = "[" + listaIdNotObbl.substring(1) + "]";
else
	listaIdNotObbl = "[]";
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
    
    <script language="JavaScript">
      function modificaNotifiche(){
        document.NotificheForm.submit();
      }      
    </script>
  </head>
  
<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio avvenuta notifica alle parti</font>
      </td>
      <td class="LBG">       
         <a href="javascript:modificaNotifiche()">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Notifiche" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="NotificheForm">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActLoadNotificheOrdineIngiunzione">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=ordineIngiunzione.getEvento().getIdEvento()%>">
  <input type="HIDDEN" name="modifica" value="true"> 
</form>

 
 
   <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan="5">
        <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){%>
          DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
        <%}else{%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        <%}%>
        </font>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
      </td>
    </tr>
  </table> 


   <table>
    <tr>
      <td class="L" colspan="5">
        <font class="campo">
        <%=ordineIngiunzione.getEvento().getDescrTipoProvvedimento()%>
        &nbsp;
        <%=ordineIngiunzione.getEvento().getDescrMotivo()%>
        &nbsp;emesso in data&nbsp;
        <%=DateUtils.getDateToString(ordineIngiunzione.getEvento().getDataEmissione(), "dd-MM-yyyy")%>
        </font>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
      </td>
    </tr>
  </table>   

<%
//=======================================================================
//                     Notifica al condannato
//=======================================================================
%>
<table>
  <tr><td class="Titolo" colspan="6">Notifica al Condannato </td></tr>
  
  <% if (notificaAlCondannato.getAutoritaEsterna() != null) { %>
     <tr>
       <td class="L">Autorita' preposta alla notifica</td>
       <td class="L" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP( notificaAlCondannato.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( notificaAlCondannato.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
       </td>
      </tr>
      <% if(notificaAlCondannato.getNote()!= null){%>
      <tr>
        <td class="L">Indirizzo</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(notificaAlCondannato.getNote())%></font>&nbsp;</td>
      </tr>
      <% } %>
  <% } %>

  <% if (notificaAlCondannato.getIstitutoDetenzione() != null) { 
      String lNotificaIstituto = notificaAlCondannato.getIstitutoDetenzione().getDescrTipoIstituto();
      if (notificaAlCondannato.getIstitutoDetenzione().getDescrComune() != null)
        lNotificaIstituto += " di " + notificaAlCondannato.getIstitutoDetenzione().getDescrComune();
      if (notificaAlCondannato.getIstitutoDetenzione().getIndirizzo() != null)
        lNotificaIstituto += " - " + notificaAlCondannato.getIstitutoDetenzione().getIndirizzo();
  %>
      <tr>
        <td class="L">Istituto Notifica</td>
        <td class="L" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP(lNotificaIstituto)%></font>&nbsp;
        </td>
      </tr>
  <% } %>

  <tr>
    <td class="l">Data Notifica</td>
    <td class="l">
      <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(notificaAlCondannato.getDataAvvenutaNotifica(), "dd/MM/yyyy"))%>
      </font>
    </td>    
  </tr> 

   <% if (notificaAlCondannato.getAutoritaEsternaDelegata()!=null) { %>
      <tr>
        <td class="l">Autorita' che ha effettuato la notifica</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP( notificaAlCondannato.getAutoritaEsternaDelegata().getDescrTipoAutorita())%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( notificaAlCondannato.getAutoritaEsternaDelegata().getDescrSede())%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l">Note</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(notificaAlCondannato.getNote())%></font>&nbsp;</td>
      </tr>      
      <% } %>
<%
//=======================================================================
//                      Notifica al Difensore
//=======================================================================
%>
  <tr>
    <td class="Titolo" colspan="6">Notifica al Difensore</td>
  </tr>
  <%
  // Difensore/i
  for(int i=0; i<listaNotAvvSiep.size(); i++)
  {
    NotificaModel lNotificaDifensore = (NotificaModel) listaNotAvvSiep.get(i);
  %>  
  
  <% if (i==1) {%>  <tr><td>&nbsp;</td></tr> <% } %>
  
      <tr>
       <td class="l">Avvocato per  Notifica</td>
       <td class="L" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(lNotificaDifensore.getAvvSiep().getAvvocato().getCognome()) + " " + StringUtils.toStringJSP(lNotificaDifensore.getAvvSiep().getAvvocato().getNome())%></font>&nbsp;
        &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lNotificaDifensore.getAvvSiep().getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lNotificaDifensore.getAvvSiep().getAvvocato().getDescrTipo())%>
        </font>
       </td>
      </tr>  
      
      <tr>
        <td class="L">Autorita preposta alla Notifica</td>
        <td class="L" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP( lNotificaDifensore.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( lNotificaDifensore.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
        </td>
      </tr>
      <tr>  
        <td class="L">Indirizzo</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lNotificaDifensore.getAutoritaEsterna().getDescrizione())%></font>&nbsp;</td>
      </tr>
      <tr>
        <td class="l">Data Notifica</td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaDifensore.getDataAvvenutaNotifica(), "dd/MM/yyyy") )%>
          </font>
        </td>
      </tr>

      <% if (lNotificaDifensore.getAutoritaEsternaDelegata()!=null) { %>
      <tr>
        <td class="l">Autorita' che ha effettuato la notifica</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP( lNotificaDifensore.getAutoritaEsternaDelegata().getDescrTipoAutorita())%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( lNotificaDifensore.getAutoritaEsternaDelegata().getDescrSede())%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l">Note</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lNotificaDifensore.getNote())%></font>&nbsp;</td>
      </tr>      
      <% } %>


<%
  } // end for Notifiche avvocati
%>


<%
//=======================================================================
//                  Notifica al Civilmente Obbligato
//=======================================================================
%>
<% if (lListaNotObbligati.size()>0) {%>

  <tr><td class="Titolo" colspan="4">Notifica al Civilmente Obbligato </td></tr>

  <%
  // Civilmente Obbligati
  for(int i=0; i<lListaNotObbligati.size(); i++)
  {
    NotificaModel lNotificaObbligato = (NotificaModel) lListaNotObbligati.get(i);
    if(lNotificaObbligato.getCivilmenteObbligato() == null)
    {
      lNotificaObbligato.setCivilmenteObbligato( new CivilmenteObbligatoModel());
    }
    CivilmenteObbligatoModel lObbligatoModel = lNotificaObbligato.getCivilmenteObbligato();
%>

<%
		if (i==1) {
%>
	<tr><td>&nbsp;</td></tr>
<%
		}
%>
	<tr>
		<td class="l">Civilmente Obbligato</td>
        <td class="L" colspan="3">
          <font class="campo"><%=lObbligatoModel.getCognome()%></font>&nbsp;
          <font class="campo"><%=lObbligatoModel.getNome()%></font>&nbsp;
          <font class="label">nato a</font>&nbsp;<font class="campo"><%=lObbligatoModel.getDescComuneNascita()%></font>&nbsp;
          <font class="label">il</font>&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lObbligatoModel.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
          <% if ("G".equals(lObbligatoModel.getCodPersona())) { %>
          <font class="label"> in qualita' di Legale Rappresentante di </font>
          <font class="campo"><%=lObbligatoModel.getDenominazione()%></font>
          <% } %>
        </td>
      </tr>
      <tr>
        <td class="l">Autorita Delegata alla Notifica</td>
        <td class="L" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP( lNotificaObbligato.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( lNotificaObbligato.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l">Note</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lNotificaObbligato.getNote())%></font>&nbsp;</td>
      </tr>
      <tr>
        <td class="l">Data Notifica</td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaObbligato.getDataAvvenutaNotifica(), "dd/MM/yyyy") )%>
          </font>
        </td>
      </tr>

<%
		if (lNotificaObbligato.getAutoritaEsternaDelegata() != null) {
%>
	<tr>
	  	<td class="l">Autorita' che ha effettuato la notifica</td>
	  	<td class="L">
	    	<font class="campo"><%=StringUtils.toStringJSP( lNotificaObbligato.getAutoritaEsternaDelegata().getDescrTipoAutorita())%></font>
			&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP( lNotificaObbligato.getAutoritaEsternaDelegata().getDescrSede())%></font>
	  	</td>
	</tr>
	<tr>
	  	<td class="l">Note</td>
	  	<td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lNotificaObbligato.getNote())%></font></td>
	</tr>
<%
		}
	} // end obbligati
}
%>  
</table>
</body>
</html>