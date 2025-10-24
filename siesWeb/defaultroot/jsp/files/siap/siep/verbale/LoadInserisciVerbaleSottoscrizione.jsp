<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.util.MinorMask"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>

<html>

<jsp:useBean id="tipoAutorita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"        scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="penaRes"       scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<head>
<title>[S.I.E.S.] - Gestione Verbale Sottoscrizione </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
function Verifica() {
    if (document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value.length==1)
      document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value;
    if (document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value.length==1)
      document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value;
    var data_to_verify = document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;
    if (!ControllaData(data_to_verify)) {
      alert('Data Pervenimento non valida');
      return false;
    }
    if (document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
      document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
      document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value;

    var data_to_verify_em = document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>.value;
    if (!ControllaDataPassaVuota(data_to_verify_em)) {
<%
if (evento.getCodMotivo().equals("0001")
             || evento.getCodMotivo().equals("0002") 
             || evento.getCodMotivo().equals("0003")
             || evento.getCodMotivo().equals("2006")
             || evento.getCodMotivo().equals("2008")
     	// 20191120 [SG]: aggiunto codice per gestione ticket
		// Ticket#20191114019 - SIES - mancata registrazione data inizio misura
		// Ticket#20191112019 - 2019/11 Ancona Procura Minori non fa caricare inizio misura
		// Esecuzione presso domicilio della pena detentiva ( TdS )
        || evento.getCodMotivo().equals("0610")
		) {
%>
          alert('Data Sottoscrizione Prescrizioni non valida');
      <%  
}
      // 27/09/2010 Codice 2630 = Espiazione Pena presso Domicilio. 
if (evento.getCodMotivo().equals("0005")
		|| evento.getCodMotivo().equals("0010")
		|| evento.getCodMotivo().equals("0013")
		|| evento.getCodMotivo().equals("2245")
		|| evento.getCodMotivo().equals("2630")
		|| evento.getCodMotivo().equals("0011")
		|| evento.getCodMotivo().equals("2005")
          // || evento.getCodMotivo().equals("2006") 
          // || evento.getCodMotivo().equals("2008")
   		) {
      %>
          alert('Data Sottoposizione agli Obblighi non valida');
<%
}
if (evento.getCodMotivo().equals("0004")) {
%>
          alert('Data Ingresso in Istituto non valida');
<%
}
%>
         return false;
    }
    if(   document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>.value=="" 
       || document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value=="" 
       || document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value==""
      		) {
      <% if (   evento.getCodMotivo().equals("0001") 
             || evento.getCodMotivo().equals("0002") 
             || evento.getCodMotivo().equals("0003")
             || evento.getCodMotivo().equals("2006")
             || evento.getCodMotivo().equals("2008")
          	// 20191120 [SG]: aggiunto codice per gestione ticket
			// Ticket#20191114019 - SIES - mancata registrazione data inizio misura
			// Ticket#20191112019 - 2019/11 Ancona Procura Minori non fa caricare inizio misura
			// Esecuzione presso domicilio della pena detentiva ( TdS )
             || evento.getCodMotivo().equals("0610")
            ) 
      { %>
        alert("La Data Sottoscrizione Prescrizioni è obbligatoria");
      <% } %>
    
      <% //  Codice 2630 = 27/09/2010 Espiazione Pena presso Domicilio.
      if(evento.getCodMotivo().equals("0005") || evento.getCodMotivo().equals("0010") || 
         evento.getCodMotivo().equals("0013") || evento.getCodMotivo().equals("2245") || 
         evento.getCodMotivo().equals("2630") || 
         evento.getCodMotivo().equals("0011") || evento.getCodMotivo().equals("2005") 
         // || evento.getCodMotivo().equals("2006") 
         // || evento.getCodMotivo().equals("2008")
        )
      { %>
        alert("La Data Sottoposizione agli Obblighi è obbligatoria");
      <%}
  
      if (evento.getCodMotivo().equals("0004")) {%>
        alert("La Data Ingresso in Istituto è obbligatoria");        
      <% } %>
      
      return false;
    } 

    <% 
    if( evento.getCodMotivo() != null)
    {
      if(   evento.getCodMotivo().equals("0001") 
         || evento.getCodMotivo().equals("0002") 
         || evento.getCodMotivo().equals("0003")
         || evento.getCodMotivo().equals("2006")
         || evento.getCodMotivo().equals("2008")
   		// 20191120 [SG]: aggiunto codice per gestione ticket
		// Ticket#20191114019 - SIES - mancata registrazione data inizio misura
		// Ticket#20191112019 - 2019/11 Ancona Procura Minori non fa caricare inizio misura
		// Esecuzione presso domicilio della pena detentiva ( TdS )
         || evento.getCodMotivo().equals("0610")
        )
      { %>
        if(document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CSS_ID_CSSA%>.value=="")
        {
            alert("L' UEPE competente è obbligatorio");
            return false;
        }
      <% } %>
  
      <%
      if (evento.getCodMotivo().equals("0005") || evento.getCodMotivo().equals("0010") ||
          evento.getCodMotivo().equals("0013") || evento.getCodMotivo().equals("2245") ||
          evento.getCodMotivo().equals("2630") ||  // 27/09/2010 Espiazione Pena presso Domicilio.
          evento.getCodMotivo().equals("0011") || evento.getCodMotivo().equals("2005") 
          // || evento.getCodMotivo().equals("2006") 
          // || evento.getCodMotivo().equals("2008")
         )
      { %>
        if(document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>.value=="-")
        {
          alert("L'Autorità competente è obbligatorio");
          return false;
        }
        
        if(document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>.value=="")
        {
          alert("La sede Dell'Autorità competente è obbligatorio");
          return false;
        }
       <% }
    
       if(evento.getCodMotivo().equals("0004"))
       {%>
        if(document.LoadInserisciVerbaleSott.<%=ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
        {
          alert("L'Istituto competente è obbligatorio");
          return false;
        }
        <% }
  
      }   // CHIUDE if(evento.getCodMotivo() != null)  
    %>

   <% // AMBROSINO 04/2013 a6-rr-090 - %>   
    if (   document.LoadInserisciVerbaleSott.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value != ""
        || document.LoadInserisciVerbaleSott.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value != ""
        || document.LoadInserisciVerbaleSott.<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE %>.value != "" )
    {
      if (typeof (document.LoadInserisciVerbaleSott.<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>)!="undefined")
      { 
        if (   document.LoadInserisciVerbaleSott.<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value != ""
            || document.LoadInserisciVerbaleSott.<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value != ""
            || document.LoadInserisciVerbaleSott.<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value != "" )
          {
            var dataMisu = document.LoadInserisciVerbaleSott.<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE %>.value+'-'+document.LoadInserisciVerbaleSott.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value+'-'+document.LoadInserisciVerbaleSott.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value;
            var dataPena = document.LoadInserisciVerbaleSott.<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value+'-'+document.LoadInserisciVerbaleSott.<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'-'+document.LoadInserisciVerbaleSott.<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
            if(dataMisu > dataPena)
            {
              alert("ERRORE : E' STATA DIGITATA UNA DATA SOTTOSCRIZIONE PRESCRIZIONI/INIZIO MISURA MAGGIORE DELLA DATA FINE PENA!\n IMMETTERE UNA DATA CORRETTA");
              document.LoadInserisciVerbaleSott.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.focus();
              return false;
            } 
        }
      }
    } 
    <% // FINE AMBROSINO 04/2013 a6-rr-090 - %>
  
    return true;

  }    <% // CHIUDE function Verifica()  %>

  	// Chiamata all'elenco dei CSSA
	function ListaCSSA(a_formname,a_fieldname,a_field2) {
		<%-- MEV10-s3: aggiunto controllo preventivo --%>
	  	var a_typename = document.getElementById('<%=MinorMask.ComboCSSAId%>').value;
		if (a_typename == "-")  {
         alert("Selezionare il Destinatario dell'UEPE/USSM");
    	} else {
    		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+a_typename, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    	}
  	}

  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  function ListaComuni(a_formname,a_fieldname)
  {
   desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
</script>


</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Registrazione Data Inizio Misura</font>
      </td>
    </tr>
  </table>
  
  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciVerbaleSott">
     <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.verbale.action.ActInserisciVerbaleSottoscrizione">
     
     <% // AMBROSINO 04/2013 - mac a6-rr-090   (i 3 campi servonoper un controolo in javascript) %>
     <% if(penaRes.getDataFine()!= null) {  %>
        <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaRes.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
        <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaRes.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
        <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaRes.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
     <% } %>
     <% //FINE AMBROSINO 04/2013 - mac a6-rr-090  %>
     
     <table cellspacing=2 cellpadding=2>

    <tr>
    <%if(evento.getCodMotivo().equals("0001") || evento.getCodMotivo().equals("0002") || evento.getCodMotivo().equals("0003"))
     {
     %>
        <td class="l" colspan=2>Concessione Affidamento in Prova</td>
     <%
       }    
    else
      if(evento.getCodMotivo().equals("2005"))
       {
      %>
        <td class="l" colspan=2>Ammissione Provvisoria a Detenzione Domiciliare</td>
     <%
     }
     else
         if(evento.getCodMotivo().equals("2006") || evento.getCodMotivo().equals("2008"))
         {
        %>
          <td class="l" colspan=2>Ammissione Provvisoria ad Affidamento in Prova</td>
       <%
         }
       else      
      if(evento.getCodMotivo().equals("0005") || evento.getCodMotivo().equals("0010") || evento.getCodMotivo().equals("0013"))
       {
      %>
        <td class="l" colspan=2>Concessione Detenzione Domiciliare</td>
     <%
     }
      else
      if(evento.getCodMotivo().equals("0004"))
       {%>
        <td class="l" colspan=2>Concessione Semilibertà</td>
     <%}else
      if(evento.getCodMotivo().equals("2245"))
       {%>
        <td class="l" colspan=2>Concessione Sospensione Condizionata esecuzione parte finale pena detentiva</td>
     <%}else
       // 27/09/2010
      if(evento.getCodMotivo().equals("2630"))
       {%>
        <td class="l" colspan=2>Concessione Espiazione Pena presso Domicilio</td>
     <%}else
      if(evento.getCodMotivo().equals("0011"))
       {%>
        <td class="l" colspan=2>Concessione Detenzione Domiciliare a Termine</td>
     <%
		// 20191120 [SG]: aggiunto codice per gestione ticket
		// Ticket#20191114019 - SIES - mancata registrazione data inizio misura
		// Ticket#20191112019 - 2019/11 Ancona Procura Minori non fa caricare inizio misura
		// Esecuzione presso domicilio della pena detentiva ( TdS )
       }else
      if(evento.getCodMotivo().equals("0610"))
       {%>
    <td class="l" colspan=2>Esecuzione presso domicilio della pena detentiva ( TdS )</td>
     <%}%>
     
    </tr>

    <tr>
        <td class="l" width="30%">Data pervenimento del verbale</td>
        <td class="l">
        <input title="Giorno Pervenimento" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        -
        <input title="Mese Pervenimento" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input title="Anno Pervenimento" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"onBlur="javascript:value=FillYear(value)">
    </tr>
    
<%
if(evento.getCodMotivo() != null)
{
  if(   evento.getCodMotivo().equals("0001") // Concessione Affidamento
     || evento.getCodMotivo().equals("0002") // Concessione Affidamento
     || evento.getCodMotivo().equals("0003") // Concessione Affidamento
     || evento.getCodMotivo().equals("2006") // Concessione Ammissione Provvisoria Affidamento
     || evento.getCodMotivo().equals("2008") // Concessione Ammissione Provvisoria Affidamento
	// 20191120 [SG]: aggiunto codice per gestione ticket
	// Ticket#20191114019 - SIES - mancata registrazione data inizio misura
	// Ticket#20191112019 - 2019/11 Ancona Procura Minori non fa caricare inizio misura
	// Esecuzione presso domicilio della pena detentiva ( TdS )
     || evento.getCodMotivo().equals("0610")
    )
{%>

    <tr>
       <td class="l" >Data Sottoscrizione Prescrizioni <font class=ob>(*)</font></td>
        <td class="l">

          <input title="Giorno Arresto" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
         -
          <input title="Mese Arresto" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input title="Anno Arresto" size=4 maxlength=4 value="" type="text" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
	</tr>
	<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
	<tr>
		<td class="Titolo" colspan="2">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l">Destinatario <font class=ob>(*)</font></td>
		<td class="l"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
    <tr>
    	<td class="l">Sede</td>
        <td class="l">
          	<input readonly Title="Sede UEPE Competente" name="Indirizzo" value="" size=60 >
          	<input type="hidden" readonly Title="Sede UEPE Competente" name="<%= ICostantiVerbale.CSS_ID_CSSA%>" value="" size=35 >
          	<a href="Javascript:ListaCSSA('LoadInserisciVerbaleSott','<%= ICostantiVerbale.CSS_ID_CSSA %>','Indirizzo');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
		</td>
	</tr>
<%
  }

  if(    evento.getCodMotivo().equals("0005")
      || evento.getCodMotivo().equals("0010")
      || evento.getCodMotivo().equals("0013")
      || evento.getCodMotivo().equals("2245")
      || evento.getCodMotivo().equals("2630") // 27/09/2010 Espiazione Pena presso Domicilio
      || evento.getCodMotivo().equals("0011")
      || evento.getCodMotivo().equals("2005")
      //|| evento.getCodMotivo().equals("2006")
      //|| evento.getCodMotivo().equals("2008") // 24/01/2014 DL 146 2013
    )
  {
%>
      <tr>
        <td class="l" >Data Sottoposizione agli obblighi <font class=ob>(*)</font></td>
        <td class="l">
          <input title="Giorno Arresto" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input title="Mese Arresto" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input title="Anno Arresto" size=4 maxlength=4 value="" type="text" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
        <td class="l">Autorità Competente che ha inviato il verbale <font class=ob>(*)</font></td>
        <td class="L">
          <select Title="Autorita" class="" name="<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>">
            <%=tipoAutorita%>
          </select>
      </tr>
      <tr>
        <td class="l">Sede Autorità <font class=ob>(*)</font> </td>
        <td class="L">
          <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciVerbaleSott','<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
        <td class="l">Indirizzo</td>
        <td class="L">
          <TEXTAREA title="Note" name="<%= ICostantiVerbale.CAMPO_NOTE %>"   cols=30  ></textarea>
        </td>
      </tr>
<%
  }
  
  
  if(evento.getCodMotivo().equals("0004"))
  {
%>
    <tr>
      <td class="l" >Data Ingresso in Istituto <font class=ob>(*)</font></td>
      <td class="l">
        <input title="Giorno Arresto" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input title="Mese Arresto" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input title="Anno Arresto" size=4 maxlength=4 value="" type="text" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Istituto Competente che ha inviato il verbale <font class=ob>(*)</font></td>
      <td class="l">
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciVerbaleSott','<%= ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
<%
  }
}
%>
      <td>&nbsp;</td>
    <tr>
      <td>
      <br><INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
    </td>
  </tr>
</table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciVerbaleSott");

    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","lt=2099");


    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

    frmvalidator.setAddnlValidationFunction("Verifica");
  </script>
</body>
</html>