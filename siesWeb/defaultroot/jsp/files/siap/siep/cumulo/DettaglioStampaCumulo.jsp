<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.F3BProperties"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sius.documentoallegato.model.DocumentoAllegatoModel"%>
<%@ page import="siap.siep.modulocumulo.util.ModuloCumuloUtils"%>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="f3b.log.LogF3B"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="eventonotifica"       scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="posizione"            scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="cumulo"               scope="request" class="siap.siep.cumulo.model.CumuloModel" />
<jsp:useBean id="tipo"                 scope="request" class="java.lang.String" />
<jsp:useBean id="giorniLA"             scope="request" class="java.lang.String"/>
<jsp:useBean id="fogliocomplementare"  scope="request" class="java.lang.String"/>
<jsp:useBean id="documentoAllegato"    scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel" />
<jsp:useBean id="penaCum"              scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel" />

<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <title>[S.I.E.S.] - Calcolo Pena</title>
</head>

<body class="corpo">
	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    			</a>
    		</td>
			<td class=LBG>
        		<font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Stampa Cumulo</font>
      		</td>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
		|| (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
		&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)) {
%>
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.cumulo.action.ActStampaCumulo&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>&flagStampa=<%=cumulo.getFlagTipoStampa()%>" onclick="javascript:lookUpload();">
            <img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
       <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
         <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.cumulo.action.ActStampaCumulo&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&flagStampa="+cumulo.getFlagTipoStampa()%>"/>
       </jsp:include>
<%
}
%>
<!--  Bottone per il TRASFERIMENTO ATTI PROSECUZIONE/CESSAZIONE MISURA  (Ex art 51 Bis) -->
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
		&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
%> 
			<td class="LBG">
      			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadInserisciTrasmissione&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
        			<img src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasferisci" width="24" height="24" border="0">
      			</a>
     		</td>
  			<!-- Bottone per l'INSERIMENTO DEL FOGLIO COMPLEMENTARE -->
<%
	// MEV 16 CUMULO: gestite casistiche per cui far vedere l'icona del FC
	boolean onOffIconFCCumulo = "true".equalsIgnoreCase(F3BProperties.getProperty("onOffIconFCCumulo")) ? true : false;
	if (ModuloCumuloUtils.isCumulo(eventonotifica.getEvento().getCodMotivo()) && onOffIconFCCumulo) {
		if (documentoAllegato != null && documentoAllegato.getIdDocumentoAllegato() != null
				&& documentoAllegato.getDataAnnullamento() == null) {
		// il foglio complementare esiste ==> azione: dettaglio foglio complementare
%>	
	 		<td class="LBG">  		
				<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&IdDocumentoAllegato=<%=documentoAllegato.getIdDocumentoAllegato()%>&<%=ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE%>=siap.siep.cumulo.action.ActDettaglioCumuloStampa">
					<img src="/images/fcNsc.gif" width="30" height="30" alt="Modifica Foglio Complementare" border="0"> 
				</a>
	 		</td> 
<%
	} else {
	// il foglio complementare non esiste ==> azione: inserimento foglio complementare
%>
	 		<td class="LBG">  		
				<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE%>=siap.siep.cumulo.action.ActDettaglioCumuloStampa">
					<img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0"> 
				</a>
	 		</td> 
<%		
		}
	}
}
%>  
		</tr>
	</table>
  	<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  	<br>
	<table style="width: 90%;">
		<tr>
			<td class=l width="30%">Posizione Giuridica</td>
			<td class=l colspan=5><font class="campo"><%=posizione.getDescrPosizioneGiuridica()%></font></td>
 		</tr>
   		<tr>
<%
if (penaresidua.getIdPenaResidua() != null && ((penaresidua.getFlagErgastolo() == null)
		|| (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S")
		&& !penaresidua.getFlagErgastolo().equals("D")))) {
	if (!(penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
%>
          <td class="l">Reclusione</td>
          	<td class="l" colspan="2">
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          	<td class="l" colspan="2">
          		<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%>
          		</font>&nbsp;<font class="l">Euro</font>
          	</td>
<%
        }
%>
   </tr>
   <tr>
<%
	if (!(penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
%>
      <td class="l" >Arresto</td>
      		<td class="l" colspan="2">
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      		<td class="l" colspan="2">
      			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
      		</td>
<%
      }
    }
%>
  </tr>
</table>  
<%
if (   (penaCum.getNumGiorniLibAnticipataLA()!=null  && penaCum.getNumGiorniLibAnticipataLA().intValue()>0)
    || (penaCum.getNumGiorniLibAnticipataSPE()!=null && penaCum.getNumGiorniLibAnticipataSPE().intValue()>0)
    || (penaCum.getNumGiorniLibAnticipataINT()!=null && penaCum.getNumGiorniLibAnticipataINT().intValue()>0)
   		|| (penaCum.getNumGiorniRiduzionePena() != null    && penaCum.getNumGiorniRiduzionePena().intValue() > 0)) {
  String lLADetratta = "";
	if (penaresidua != null && penaresidua.getDataFine() != null) {
    lLADetratta = "già detratta";
  } else {
    lLADetratta = "da detrarre";
  }
  
  
%>
	<table style="width: 90%;">
       <!--  20/05/2014  Nuova L.A. - DL 146/2013 -->
<%
	if (penaCum.getNumGiorniLibAnticipataLA() != null && !"0".equals(penaCum.getNumGiorniLibAnticipataLA().toString())) {
%>
        <tr>
          <td class="L" width="40%" >
            <font class="label">Liberazione Anticipata Concessa <%=lLADetratta%> in giorni</font>
          </td>
          <td class="L" width="10%" >
            <font class="campo"><%=StringUtils.toStringJSP(penaCum.getNumGiorniLibAnticipataLA(), "0") %></font>
          </td>
          <td width="40%" > </td>
        </tr>
<%
	}
   	if (penaCum.getNumGiorniLibAnticipataSPE() != null && !"0".equals(penaCum.getNumGiorniLibAnticipataSPE().toString())) {
%> 
        <tr>
          <td class="L" width="40%" >
            <font class="label">Liberazione Anticipata Speciale Concessa <%=lLADetratta%> in giorni</font>
          </td>
          <td class="L" width="10%" >
            <font class="campo"><%=StringUtils.toStringJSP(penaCum.getNumGiorniLibAnticipataSPE(), "0") %></font>
          </td>
          <td width="40%" > </td>
        </tr>
<%
	}
   	if (penaCum.getNumGiorniLibAnticipataINT() != null && !"0".equals(penaCum.getNumGiorniLibAnticipataINT().toString())) {
%>      
        <tr>
          <td class="L" width="40%" >
            <font class="label">Integrazione Liberazione Anticipata Concessa <%=lLADetratta%> in giorni</font>
          </td>
          <td class="L" width="10%" >
            <font class="campo"><%=StringUtils.toStringJSP(penaCum.getNumGiorniLibAnticipataINT(), "0") %></font>
          </td>
          <td width="40%" > </td>
        </tr>
<%
	}
   	if (penaCum.getNumGiorniRiduzionePena() != null && !"0".equals(penaCum.getNumGiorniRiduzionePena().toString())) {
%>
        <tr>
          <td class="L" width="40%" >
            <font class="label">Riduzione pena per risarcimento danni Concessa <%=lLADetratta%> in giorni</font>
          </td>
          <td class="L" width="10%" >
            <font class="campo"><%=StringUtils.toStringJSP(penaCum.getNumGiorniRiduzionePena(), "0") %></font>
          </td>
          <td width="40%" > </td>
        </tr>        
<%
	}
%>
  </table>
        <!--  End Nuova L.A. --> 
<%
}
%>
	<table style="width: 90%;">
  <tr>
<%
if (penaresidua.getDataInizio() != null) {
%>
         <td class="l" width="30%">Data Decorrenza Pena</td>
			<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%></font></td>
<%
       }
if (penaresidua.getFlagErgastolo() != null) {
	if (penaresidua.getFlagErgastolo().equals("S")) {
%>
          <td class="l">Pena Detentiva</td>
          	<td class="L"><font class="campo">ERGASTOLO</font></td>
<%
	} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
          <td class="l">Pena Detentiva</td>
          	<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
<%
        }
       }
if (penaresidua.getFlagErgastolo() != null && (penaresidua.getFlagErgastolo().equals("S") || penaresidua.getFlagErgastolo().equals("D"))) {
%>
			<td class="l">Data Fine Pena</td>
           	<td class="lRosso"> <font class="lRosso">MAI</font></td>
<%
} else if (penaresidua.getDataFine() != null) {
%>
           <td class="l">Data Fine Pena</td>
			<td class="L" colspan="2">
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy"))%></font>
            </td>
<%
}
%>
</tr>

<tr>
  <td class="l">Data Emissione</td>
        <td class="L" >
          <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy"))%></font>
        </td>

        <td class="l">Data Trasmissione</td>
        <td class="L" colspan="2">
          <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(), "dd-MM-yyyy"))%></font>
      </td>
      </tr>
    <tr>
      		<td class="l">Foglio Complementare</td>
      <td class="c">
        <font class="campo">&nbsp;
<%
if (fogliocomplementare.equals("1")) {
%>
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
<%
          }
%>
        </font>
      </td>
<%
if (fogliocomplementare.equals("1")) {
%>
      <td class="l">Casellario Giudiziale</td>
      <td class="l">
<%
	for (int i = 0; i < eventonotifica.getNotifiche().length; i++) {
          // La notifica corrispondente al Casellario Giudiziale corrisponde al tipo "C"
          if(    eventonotifica.getNotifiche()[i] != null 
              && "FC".equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica()) 
           		&& eventonotifica.getNotifiche()[i].getAutoritaEsterna() != null) {
%>
            	<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getAutoritaEsterna().getDescrSede())%></font>
<%
          }
        }
%>
      </td>
<%
    }
%>    
   </tr>
  <tr>
   <td class="l">Tipologia
   <td class="L" colspan="3">
	     		<font class="campo"><%=StringUtils.toStringJSP(cumulo.getDescrTipoStampa())%></font>
   </td>
  </tr>


  <tr>
   <td class="l">Magistrato Competente
   <td class="L" colspan="3">
	     		<font class="campo"><%=StringUtils.toStringJSP(magistratocompetente.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(magistratocompetente.getNome())%></font>
   </td>
  </tr>

<%
int cont=0;
while (cont < eventonotifica.getNotifiche().length) {
	if ("E".equals(eventonotifica.getNotifiche()[cont].getCodTipoNotifica())
			&& eventonotifica.getNotifiche()[cont].getIstitutoDetenzione() != null) {
%>

  <tr>
     <td class="l">Istituto Detenzione</td>
      <td class="l" colspan="3">
				<font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrComune())%></font>
       </td>
 </tr>
<%
	}
	if ("NC".equals(eventonotifica.getNotifiche()[cont].getCodTipoNotifica())
			&& eventonotifica.getNotifiche()[cont].getCSSA() != null) {
%>
  <tr>
     <td class="l">UEPE</td>
      <td class="l" colspan="3">
				<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getCSSA().getComune())%>&nbsp;-&nbsp;<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getCSSA().getIndirizzo())%></font>
       </td>
 </tr>
<%
	}
	if ("T".equals(eventonotifica.getNotifiche()[cont].getCodTipoNotifica())
			&& eventonotifica.getNotifiche()[cont].getUffCodUfficio() != null) {
%>
  <tr>
     <td class="l">Tribunale di Sorveglianza di</td>
      <td class="l" colspan="3">
         <font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getUfficio().getDescrComune())%></font>
       </td>
 </tr>
<%
	}
	if ("MS".equals(eventonotifica.getNotifiche()[cont].getCodTipoNotifica())
			&& eventonotifica.getNotifiche()[cont].getUffCodUfficio() != null) {
%>
  <tr>
     <td class="l">Magistrato di Sorveglianza di</td>
      <td class="l" colspan="3">
         <font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getUfficio().getDescrComune())%></font>
       </td>
 </tr>
<%
	}
    if (("N".equals(eventonotifica.getNotifiche()[cont].getCodTipoNotifica())
    		|| "C".equals(eventonotifica.getNotifiche()[cont].getCodTipoNotifica()))
      && eventonotifica.getNotifiche()[cont].getAutoritaEsterna() != null
    		&& eventonotifica.getNotifiche()[cont].getAvvIdAvvocatoFascicoloSiep() == null) {
%>
  <tr>
     <td class="l">Autorità di destinazione</td>
      <td class="l" colspan="3">
				<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getAutoritaEsterna().getDescrTipoAutorita())%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getAutoritaEsterna().getDescrSede())%></font>
       </td>
 </tr>
<%
	}
cont++;
}
int count = 0;
while (count < eventonotifica.getNotifiche().length) {
  NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
 	if (lNotMod.getAutEstIdAutoritaEsterna() != null && lNotMod.getAvvIdAvvocatoFascicoloSiep() != null) {
     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Avvocato per  Notifica</td>
       <td class="L" colspan="3">
	        	<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) + " " + StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;Foro di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%></font>&nbsp;Difensore&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%></font>
       </td>
      </tr>
   <tr>
      <td class="l">Autorita Notifica</td>
            <td class="L" colspan=3>
	        	<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita())%></font>&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lAuMod.getDescrSede())%></font>
      </td>
     </tr>

<%
}
count++;
}
%>

</table>
<br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActUploadCumulo">
            <input type="HIDDEN" name="tipologia" value="<%=tipo%>">


            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiCumulo.CAMPO_AZIONE_CHIAMANTE%>" value="siap.siep.cumulo.action.ActDettaglioCumuloStampa">
          </td>
        </tr>
      </table>
</form>
</div>
</body>

</html>