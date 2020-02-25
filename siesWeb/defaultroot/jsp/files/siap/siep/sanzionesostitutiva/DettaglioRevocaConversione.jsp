<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="scambiosanzione" scope="request" class="siap.siep.scambiosanzione.model.ScambioSanzioneModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="annotazione"         scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="evento"         scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
// 20191002 [SG]: intervento post collaudo 11.3 -- aggiunto controllo preventivo
if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();
if (lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();
if (lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>"> 

</head>

<body class="corpo">
	<table>
	    <tr>
		    <td class="LBG">
		    	<a href="Javascript:window.print();">
		    		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
		    	</a>
		    </td>
		    <td class="LBG">
		    	<font class="label">Funzione :</font>&nbsp;&nbsp;
		    	<font class="campo">Dettaglio Annotazione Revoca/Conversione Sanzione Sostitutiva</font>
			</td>
		</tr>
	</table>
	<br>
		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	<br>    

<% //======================= BLOCCO POSIZIONE GIURIDICA ========================= %>
<table>
	<tr>
		<td class="l">Posizione Giuridica </td>
		<td class="L" colspan=5>
			<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA <%=lAltraCausa.getDescrTipoPosGiuridica()%>
<%
} else {
%>
				<%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
		</td>
	</tr>
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	if (lAltraCausa.getIstitutoDetenzione() != null) {
%>
	<tr>
	  	<td class="l">Detenuto presso </td>
	  	<td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font></td>
	</tr>
<%
		if (lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
	  	<td class="l">Altro Luogo </td >
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
	  	</td>
	</tr>
<%
		}            
	} else if (lLuogoDetenzione.getIstitutoDetenzione() != null && !"-".equals(lLuogoDetenzione.getIstitutoDetenzione().getCodTipoIstituto())) {
	%>
	<tr>
	 	<td class="l">Detenuto presso </td>
	 	<td class="L" colspan=5>
	  		<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
	  	</td>
	</tr>
<%
	}
}
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02")
		|| lPosizione.getCodPosizioneGiuridica().equals("04"))) {
	if (lLuogoDetenzione.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Indirizzo</td>
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
	 	</td>
	</tr>
<%
	}
}
if (penaresidua != null && penaresidua.getFlagSanzioneSostitutiva() != null) {
%>
     <tr>
      <td class="l">Sanzione sostitutiva da espiare</td>  
      <td class="L">
           <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
				 if(   (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
            || (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)
           )
         {
%>
          <font class="label"> Sanz.Pec.&nbsp;</font>
          <% if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) { %>
          <font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
          <% } %>
          <% if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) { %>
          <font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
          <% } %>
<%
         }
%> 
      </td>
    </tr>

<%} %>
 
<% //======================= FINE BLOCCO POSIZIONE GIURIDICA ========================= %>	   
 
     <tr>
      <td class="l">Data Ricezione Provvedimento</td>
      <td class="L" colspan="4">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataRicezioneAtti(),"dd/MM/yyyy") )%></font>
      </td>
    </tr>
     <tr>
      <td class="l">Data emissione Provvedimento</td>
		<td class="l" colspan="4">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataEmissione(),"dd-MM-yyyy"))%>
			&nbsp;</font>
		</td>
    </tr>    
    <tr>
      <td class="l"> Anno/Numero Provvedimento</td>
      <td class="l" colspan="5">
			<font class="campo">
        		<%=StringUtils.toStringJSP(scambiosanzione.getAnnoRegistro())%>
        		/
        		<%=StringUtils.toStringJSP(scambiosanzione.getNumeroRegistro())%>
        	&nbsp;</font>
      </td>

    </tr>
    <tr>
        <td class="l">Tipo Provvedimento </td>
		<td class="l" colspan="4">
			<font class="campo">
				<%=StringUtils.toStringJSP(scambiosanzione.getDescrTipoDecisione())%>
			&nbsp;</font>
		</td>
    </tr>


    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="l" colspan="5">
			<font class="campo">
	          <%=StringUtils.toStringJSP(scambiosanzione.getDescrUfficioEmittente())%>
				&nbsp;di&nbsp;
	          <%=StringUtils.toStringJSP(scambiosanzione.getComuneUfficioEmittente())%>
	        &nbsp;</font>
      </td>
    </tr> 
    <tr>
      <td class="l">Tipo Pena da Convertire</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%></font></td>
     
      <td class="l" colspan="3">Quantum di Pena
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>

        </td>
    </tr>
    <tr>
      <td class="l" colspan="4">Pena Convertita:</td>
    </tr>  
    <tr>
      <td class="l" colspan="4">   RECLUSIONE
          <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumAnniReclusione(), "0")%>&nbsp;</font>
          <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumMesiReclusione(), "0")%>&nbsp;</font>
          <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumGiorniReclusione(), "0")%></font>
        ARRESTO
          <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumAnniArresto(), "0")%>&nbsp;</font>
          <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumMesiArresto(), "0")%>&nbsp;</font>
          <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(annotazione.getNumGiorniArresto(), "0")%></font>

      </td>
    </tr>


<tr>
<td class="lNoBord">
<FORM  method="POST" name="OEsecuzione" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadInserisciOrdineEsecuzioneSanSos&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=evento.getEvento().getIdEvento()%>">
      <br><INPUT class="bottone" type="submit" name="OE" value="Ordine Esecuzione">
 </FORM>
</td>

<td class="lNoBord">
<FORM  method="POST" name="OEsecuzioneS" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadInserisciOrdineEsecuzioneSimeoneSanSos&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=evento.getEvento().getIdEvento()%>">
      <br><INPUT class="bottone" type="submit" name="OES" value="Ordine Esecuzione con Sospensione">
 </FORM>
</td>

<td class="lNoBord">
<FORM  method="POST" name="RPena" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRideterminazionePenaRevocaSS&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=evento.getEvento().getIdEvento()%>">
      <br><INPUT class="bottone" type="submit" name="RP" value="Rideterminazione Pena">
 </FORM>
</td>


<td class="lNoBord">
<FORM  method="POST" name="Exit" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>">
      <br><INPUT class="bottone" type="submit" name="EX" value="Esci">
 </FORM>
</td>
</tr> 

  
    
    
</table>

</body>
</html>