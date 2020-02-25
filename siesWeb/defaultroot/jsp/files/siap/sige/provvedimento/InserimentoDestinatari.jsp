<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.sige.sentenza.model.SentenzaSigeModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>

<jsp:useBean id="fascicolo" 			   scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="UtenteConnesso" 		   scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="destDeposito"     		   scope="request" class="java.util.Vector"/>
<jsp:useBean id="Aggiungi"         		   scope="request" class="java.lang.String"/>
<jsp:useBean id="NotifichePresenti"        scope="request" class="java.lang.String"/>
<jsp:useBean id="altreAutoritaGiudiziarie" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			   scope="request" class="java.lang.String"/>
<jsp:useBean id="sentenze"     			   scope="request" class="java.util.Vector"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
 
  String lCompetenza = "N";

  if (request.getParameter("Competenza") != null)
  {
	  lCompetenza = request.getParameter("Competenza");
  }
%>

<script language="JavaScript">
  var desktop;
  function ListaAutorita(a_formname,a_fieldname,codTipoUfficio)
  {
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
</script>

<script language="JavaScript">
        function Lista(tipo, codice)
        {
           var nomeForm = "<%=request.getParameter("NomeForm")%>";
           if(tipo == 'C')
              ListaComuni(nomeForm,codice);
           else if(tipo == 'U')
              ListaUffici(nomeForm,codice);
           else if(tipo == 'PGCAP')
              ListaProcure(nomeForm,codice);
           else if(tipo == 'UDS')
              ListaUDS(nomeForm,codice);
           else if(tipo == 'UEPE')
              ListaCSSA(nomeForm,codice);
           else if(tipo == 'TDS')
              ListaTDS(nomeForm,codice);
           else
              ListaUffici(nomeForm,codice);
        }
</script>

<table cellspacing="2" cellpadding="2" width="95%">

    <tr>
      <td colspan=3 class="Titolo" colspan=2> Nuovi Destinatari</td>
    </tr>

    <tr>
      <td class="l">Data Trasmissione atti</td>
      <td class="L">
       <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
       <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
       <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
		
	   <!-- MEV 15 - Revisione SIGE -->
	   <a href="javascript:calendario('<%=request.getParameter("NomeForm")%>','<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>');">
      	   <img src="/images/calendario.gif" border=0>
       </a>
      </td>
    </tr>
		
<%
    // Contatore destinatari
		int ind = 0;
		int ind_check = 0;
		String sedeCom = "";
		String sedeEsec = "";
		String tipoUffEsec = "";
		String TipoUfficioConnesso = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
		if(UtenteConnesso != null &&  UtenteConnesso.getUfficioUtente() != null)
			sedeCom = UtenteConnesso.getUfficioUtente().getDescrComune();

		if(fascicolo.getDescrComuneUfficio() != null && fascicolo.getDescrComuneUfficio().length()>0)
		{
			// Viene ricavato il Comune dell'ufficio di esecuzione ed il tipo
			sedeEsec = fascicolo.getDescrComuneUfficio();
			if (fascicolo.getCodTipoUfficio() != null)
			tipoUffEsec = fascicolo.getCodTipoUfficio();
		}
%>
    <tr>
      <td colspan=3 class="LBG" colspan=2>Per l'esecuzione :</td>
    </tr>
<%
    Iterator itDeposito = destDeposito.iterator();
    DecodificheModel lDec = null;
    while (itDeposito.hasNext())
    {
			lDec = (DecodificheModel) itDeposito.next();
%>
      <tr>
        <td class="l">
          <%=lDec.getDescription()%>
            <input type="HIDDEN" name="cod_destinatari" value = "<%=lDec.getCode()%>" maxlength="6" size="6">
        </td>

        <td class="l">
            <input Title="Sede <%=lDec.getDescription()%>" name="sede_destinatari" value="" type="text" maxlength="35" size="35">
    				<input name="nota_destinatari" value="Per l'esecuzione" type="HIDDEN" readonly>
    				<input name="dato_destinatari" value="" type="HIDDEN" readonly>
            <a href="Javascript:Lista('<%=lDec.getFiltro()%>','sede_destinatari[<%=ind%>]');">
            	<img src="/images/filefolder.gif" border=0> 
            </a>
     		</td>
   		</tr>
		<%
    	ind++;
  	}%>

    <tr>
      <td colspan=3 class="LBG">Per la comunicazione / visto :</td>
    </tr>
<%
    itDeposito = destDeposito.iterator();
    lDec = null;
    while (itDeposito.hasNext())
    {
			lDec = (DecodificheModel) itDeposito.next();
%>
      <tr>
        <td class="l">
          <%=lDec.getDescription()%>
            <input type="HIDDEN" name="cod_destinatari" value = "<%=lDec.getCode()%>" maxlength="6" size="6">
        </td>

        <td class="l">
            <input Title="Sede <%=lDec.getDescription()%>" name="sede_destinatari" value="" type="text" maxlength="35" size="35">
    				<input name="nota_destinatari" value="Per la comunicazione" type="HIDDEN" readonly>
    				<input name="dato_destinatari" value="" type="HIDDEN" readonly>
            <a href="Javascript:Lista('<%=lDec.getFiltro()%>','sede_destinatari[<%=ind%>]');">
            	<img src="/images/filefolder.gif" border=0> 
            </a>
     		</td>
   		</tr>
		<%ind++;}%>
	</table>
	<input type="HIDDEN" name='num_sentenze' value=<%=sentenze.size()%> >

	<!-- Autorità Giudicanti caricati dalle sentenze afferenti al Fascicolo SIGE -->
	<table cellspacing="2" cellpadding="2" width="95%">
    <tr>
      <td class="LBG" colspan=3>Autorità Giudicanti :</td>
    </tr>
 	</table>

	<table cellspacing="2" cellpadding="2" width="95%">
<% 
	for (int i = 0; i<sentenze.size(); i++ )
	{
		SentenzaSigeModel sentenza = (SentenzaSigeModel)sentenze.get(i);
%>
     <tr>
        <td class="l" colspan='5'>
          <%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%> di <%=sentenza.getDescrLuogoEmittente()%> &nbsp;
        	<font class="label"> (<%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:
<%
	        if (sentenza.getNumeroSentenza()!=null && sentenza.getNumeroSentenza().length()>0)
        	{%>
	        	<font class="label"> N.</font>
        		<font class="campo"> 
          		<%=sentenza.getAnnoSentenza()%>/<%=sentenza.getNumeroSentenza()%></font>
				<%}else{%>
	        	<font class="cRosso">
        			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          			NON NUMERATA 
          		</a>
	        	</font>
				<%}%>
        	<font class="label"> del </font>
        	<font class="campo">
        		<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
        	</font>
<%
        if (sentenza.getAnnoRegeGip() != null) {
%>
          <font class="label"> (N.Reg.Gen. </font>
          <font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGip())%>/<%=StringUtils.toStringJSP(sentenza.getNumeroRegeGip())%></font>
          <font class="label"> GIP)</font>
<%
		} else {
			if (sentenza.getAnnoRegeDib() != null) {
%>
          	&nbsp;<font class="label"> (N.Reg.Gen. </font>
          	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeDib())%>/<%=StringUtils.toStringJSP(sentenza.getNumeroRegeDib())%></font>
          	<font class="label"> DIB)</font>
<%
			// MEV_66: aggiunte quattro nuove proprietà
			} else if (sentenza.getAnnoRegeGup() != null) {
%>
          	&nbsp;<font class="label"> (N.Reg.Gen. </font>
          	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGup())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGup())%></font>
          	<font class="label"> GUP) </font>
<%
          	} else if (sentenza.getAnnoRegeCapsm() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
           	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCapsm())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCapsm())%></font>
           	<font class="label"> CAPSM) </font>
<%
			// MEV_66: aggiunti anche CAS, CAP e CASAP
			} else if (sentenza.getAnnoRegeCap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCap())%></font>
			<font class="label"> CAP) </font>
<%
			} else if (sentenza.getAnnoRegeCas() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCas())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCas())%></font>
			<font class="label"> CAS) </font>
<%
			} else if (sentenza.getAnnoRegeCasap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCasap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCasap())%></font>
			<font class="label"> CASAP) </font>
<%
			}
		}
%>
       		<font class="label">)</font>
        </td>
        <td class=l width="1%"><input type='checkbox' name='lCheck_<%=ind_check%>' value='0'>
			<input type="HIDDEN" name='cod_destinatari' value ='<%=sentenza.getCodTipoAutoritaEmittente()%>' >
			<input type="HIDDEN" name='sede_destinatari' value="<%=sentenza.getDescrLuogoEmittente()%>" >
			<input type="HIDDEN" name='nota_destinatari' value="Autorita' Giudicante" readonly>
			<input type="HIDDEN" name='dato_destinatari' value="Check" readonly>
		</td>
	</tr>
<%
	ind++;
	ind_check++;
} //endfor %>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="95%">
    <tr>
        <td class="l">Altra Autorità giudicante</td>
        <td class="l">
          <select title="Destinatario" name="cod_destinatari">
            <%=altreAutoritaGiudiziarie%>
          </select>
        </td>
    </tr>
    <tr>
        <td class="l">Sede </td>
        <td class="l">
           <input Title="Sede " name="sede_destinatari" value="" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaAutorita('<%=request.getParameter("NomeForm")%>','sede_destinatari[<%=ind%>]', document.<%=request.getParameter("NomeForm")%>.cod_destinatari[<%=ind%>][document.<%=request.getParameter("NomeForm")%>.cod_destinatari[<%=ind%>].selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
 				 <input name="nota_destinatari" value="Autorita' Giudicante" type="HIDDEN" readonly>
 				 <input name="dato_destinatari" value="" type="HIDDEN" readonly>
    </tr>
 </table>
 <%ind++;%>
	<!-- Uffici Recupero Crediti presso destinatari caricati dalle sentenze afferenti al Fascicolo SIGE -->
	<table cellspacing="2" cellpadding="2" width="95%">
    <tr>
      <td class="LBG">Uffici Recupero Crediti presso :</td>
    </tr>
 	</table>
 	
	<table cellspacing="2" cellpadding="2" width="95%">
<% 
	for (int i = 0; i<sentenze.size(); i++ )
	{
		SentenzaSigeModel sentenza = (SentenzaSigeModel)sentenze.get(i);
%>
     <tr>
        <td class="l" colspan='5'>
          <%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%> di <%=sentenza.getDescrLuogoEmittente()%>
        	<font class="label">&nbsp;(<%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:
<%
	        if (sentenza.getNumeroSentenza()!=null && sentenza.getNumeroSentenza().length()>0)
        	{%>
	        	<font class="label"> N.</font>
        		<font class="campo"> 
          		<%=sentenza.getAnnoSentenza()%>/<%=sentenza.getNumeroSentenza()%></font>&nbsp;
				<%}else{%>
	        	<font class="cRosso">
        			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          			NON NUMERATA 
          		</a>
	        	</font>&nbsp;
				<%}%>
        	<font class="label">del</font>&nbsp;
        	<font class="campo">
        		<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>&nbsp;
        	</font>
				<%
        if (sentenza.getAnnoRegeGip() != null)
        {%>
          &nbsp;<font class="label">(N.Reg.Gen.</font>
          <font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGip())%>/<%=StringUtils.toStringJSP(sentenza.getNumeroRegeGip())%></font>
          <font class="label"> GIP)</font>
			<%}else{
          if (sentenza.getAnnoRegeDib() != null) 
          {%>
          	&nbsp;<font class="label">(N.Reg.Gen.</font>
          	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeDib())%>/<%=StringUtils.toStringJSP(sentenza.getNumeroRegeDib())%></font>
          	<font class="label"> DIB)</font>
<%
			// MEV_66: aggiunte quattro nuove proprietà
			} else if (sentenza.getAnnoRegeGup() != null) {
%>
          	&nbsp;<font class="label"> (N.Reg.Gen. </font>
          	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGup())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGup())%></font>
          	<font class="label"> GUP) </font>
<%	
          	} else if (sentenza.getAnnoRegeCapsm() != null) {
%>
           	&nbsp;<font class="label"> (N.Reg.Gen. </font>
           	<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCapsm())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCapsm())%></font>
           	<font class="label"> CAPSM) </font>
  <%	
			// MEV_66: aggiunti anche CAS, CAP e CASAP
			} else if (sentenza.getAnnoRegeCap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCap())%></font>
			<font class="label"> CAP) </font>
<%
			} else if (sentenza.getAnnoRegeCas() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCas())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCas())%></font>
			<font class="label"> CAS) </font>
<%
			} else if (sentenza.getAnnoRegeCasap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCasap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCasap())%></font>
			<font class="label"> CASAP) </font>
<%
			}
		}
%>
        	<font class="label">)</font>
        </td>

        <td class=l width="1%"><input type='checkbox' name='lCheck_<%=ind_check%>' value='0' > </td>
          <input type="HIDDEN" name='cod_destinatari' value ='<%=sentenza.getCodTipoAutoritaEmittente()%>' >
          <input type="HIDDEN" name='sede_destinatari' value="<%=sentenza.getDescrLuogoEmittente()%>" >
   		  <input type="HIDDEN" name='nota_destinatari' value="Ufficio Recupero Crediti" readonly>
   		  <input type="HIDDEN" name='dato_destinatari' value="Check" readonly>
        </td>
<%	ind++;
		ind_check++;
	} //endfor %>
 	</table>
 	<br>
	<table cellspacing="2" cellpadding="2" width="95%">
	    <tr>
	        <td class="l" width="20%">Altro Ufficio Recupero Crediti presso :</td>
	        <td class="l" colspan= '5'>
	          <select title="Destinatario" name="cod_destinatari">
	            <%=altreAutoritaGiudiziarie%>
	          </select>
	        </td>
	    </tr>
	    <tr>
	        <td class="l">Sede </td>
	        <td class="l"  colspan= '5'>
	           <input Title="Sede " name="sede_destinatari" value="" type="text" maxlength="35" size="35">
	              <a href="Javascript:ListaAutorita('<%=request.getParameter("NomeForm")%>','sede_destinatari[<%=ind%>]', document.<%=request.getParameter("NomeForm")%>.cod_destinatari[<%=ind%>][document.<%=request.getParameter("NomeForm")%>.cod_destinatari[<%=ind%>].selectedIndex].value);">
	              <img src="/images/filefolder.gif" border=0> </a>
	        </td>
	   				 <input name="nota_destinatari" value="Ufficio Recupero Crediti" type="HIDDEN" readonly>
	   				 <input name="dato_destinatari" value="" type="HIDDEN" readonly>
	    </tr>
	<%ind++; %>
 </table>