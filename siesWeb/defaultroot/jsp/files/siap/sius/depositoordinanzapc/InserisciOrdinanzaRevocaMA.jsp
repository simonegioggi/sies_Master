<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoModel"%>
<%@ page import="siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"   	scope="request" class="java.util.Date"/>
<jsp:useBean id="decreto"          	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="esecuzioneMA"     	scope="request" class="siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
	
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelTribunale = "Tribunale di Sorveglianza";
	String labelUfficio = "Ufficio di Sorveglianza";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelTribunale = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
		labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
	}
%>


<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza di Revoca Misura Alternativa</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaRevocaMA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      if (!VerifyCombo(lEsiti,"Esito") )
        return false;

      var ritorno = true;
      ritorno = ControlliDate();
      return ritorno;
    }

    function ControlliDate()
    {
     var ritorno = true;
     var data_emissione = '<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>';
     var data_emissione_ordinanza = document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;
     var data_emissione_decreto = document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value;
     var data_decorrenzae_revoca = document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA%>.value+'/'+document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DECORRENZA%>.value+'/'+document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA%>.value;

     // Controlli su Data Emissione Ordinanza
     if (data_emissione_ordinanza.length > 2 )
     {
       if (! ControllaData(data_emissione_ordinanza))
       {
        alert('Data Emissione Ordinanza non valida');
        ritorno =  false;
       }
       else if ( !CompareDate( data_emissione_ordinanza, data_emissione ) )
       {
        alert("Data Emissione Ordinanza deve precedere " + data_emissione + " !");
        ritorno =  false;
       }
     }
     // Controlli su Data Emissione Decreto
     if (ritorno && (data_emissione_decreto.length > 2 ))
     {
       if (! ControllaData(data_emissione_decreto))
       {
         alert('Data Emissione Decreto non valida');
         ritorno =  false;
       }
       else if ( !CompareDate( data_emissione_decreto, data_emissione) )
       {
         alert("Data Emissione Decreto deve precedere " + data_emissione + " !");
         ritorno =  false;
       }
       // STUB Controllo commentato il 02/03/2006
       //else if (data_emissione_ordinanza.length > 2 && ( !CompareDate( data_emissione_ordinanza, data_emissione_decreto)) )
       //{
       //  alert("Data Emissione Ordinanza deve precedere Data Emissione Decreto");
       //  ritorno =  false;
       //}
       // Conservo la data maggiore
       data_emissione_ordinanza = data_emissione_decreto;
     }
     // Controlli su Data Decorrenza Revoca
     if (ritorno && (data_decorrenzae_revoca.length > 2 ))
     {
       if (! ControllaData(data_decorrenzae_revoca))
       {
         alert('Data Decorrenza Revoca non valida');
         ritorno =  false;
       }
       //STUB Controllo commentato il 02/03/2006
       //else if ( data_emissione_ordinanza.length > 2 && (!CompareDate( data_emissione_ordinanza, data_decorrenzae_revoca) ))
       //{
       //  alert("Data Decorrenza Revoca non può precedere " + data_emissione_ordinanza + " !");
       //  ritorno =  false;
       //}
    }
    else if (ritorno)
    {
     ritorno = ControlloPena();
    }

     return ritorno;
    }
    function ControlloPena()
    {
     var ritorno = true;
     var NumDetenzione = 0;
     var NumArresto = 0;
     NumDetenzione += document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>.value + document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>.value + document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>.value;
     NumArresto += document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_ARRESTO_REV%>.value + document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV%>.value + document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_ARRESTO_REV%>.value;
     var Oggetto = 0;
     var Esito = 0;
     Oggetto += document.InserisciOrdinanzaRevocaMA.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.value ;
     Esito += document.InserisciOrdinanzaRevocaMA.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.value ;
     //alert(" Detenzione ->" + NumDetenzione);
     //alert(" Arresto ->" + NumArresto);
     //alert(" Oggetto & Esito ->" + Oggetto+" & "+Esito );
     // 28/08/2006 A6/RR/292
     if(( Oggetto == '00014' || Oggetto == '00015' ||
          Oggetto == '00086' || Oggetto == '00196' ) &&
        ( Esito == '00006' || Esito == '00041' ) )
     {
     		if (NumDetenzione < 1 && NumArresto < 1)
     		{
        	alert("Occorre valorizzare la Pena Rideterminata");
        	ritorno = false;
     		}
     }
     return ritorno;
    }

  </script>
  <script language="JavaScript">
  	var desktop;
    // Chiamata lista Procure ovvero Lista TDS
    function ListaProcure(a_formname,a_fieldname)
    {
    	// Selezionato radio corrispondente al TDS ( Questa condizione evita che per <> da TDS 
    	// venga chiamata la popup ).
    	if(	document.InserisciOrdinanzaRevocaMA.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>[0].checked )  
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Elenco Tribunali di Sorveglianza", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    // Chiamata all'elenco degli UDS
    function ListaUDS(a_formname,a_fieldname, a_typename)
    {
    	// Selezionato radio corrispondente al UDS ( Questa condizione evita che per <> da UDS 
    	// venga chiamata la popup ). Inoltre la pop up viene sempre chiamata se la funzione
    	// è invocata per la compilazione del CAMPO_COD_UFFICIO_MAGISTRATO_COMP
    	if(	document.InserisciOrdinanzaRevocaMA.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>[1].checked  || 
    	    a_fieldname == '<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>' )
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
  </script>
    
  <script language="JavaScript">
    function Init()
    {
    <% if (decreto != null && decreto.getIdDepositoDecreto() != null)
       { %>
          InitDecreto();
    <% } %>
    <% if (esecuzioneMA != null && esecuzioneMA.getIdEsecuzioneMisuraAlternati() != null)
       { %>
          InitOrdinanza();
    <% } %>
       return;
    }

    function InitDecreto()
    {
       document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione() ,"dd")%>";
       document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(),"MM")%>";
       document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(),"yyyy")%>";
       document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "<%=decreto.getDescrUfficioInserimento()%>";
    }

// Ordinanza di riferimento
    function InitOrdinanza()
    {
       document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value = "<%=DateUtils.getDateToString(esecuzioneMA.getDataOrdinanza() ,"dd")%>";
       document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value = "<%=DateUtils.getDateToString(esecuzioneMA.getDataOrdinanza(),"MM")%>";
       document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value = "<%=DateUtils.getDateToString(esecuzioneMA.getDataOrdinanza(),"yyyy")%>";
       document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>.value = "<%=esecuzioneMA.getDescrLuogoAutoritaEmittOrd()%>";
   }
 </script>
  <script language="JavaScript">
  // La funzione gestisce l'abilitazione dei campi in funzione dello stato del radio button
  function abilitaTipoUfficio()
  {
  	if (document.InserisciOrdinanzaRevocaMA.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>[0].checked) // Scelta di tipo ufficio TDS
  	{
			document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>[1].value  = "";
  		document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>[1].disabled  = true;
  		document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>[0].disabled  = false;
  	}
  	else // Scelta tipo ufficio <> da TDS ossia UDS
  	{
  		document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>[0].value  = "";
  		document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>[0].disabled  = true;
  		document.InserisciOrdinanzaRevocaMA.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>[1].disabled  = false;
   	}
  }
  </script>
 </head>
 <%
  String lAction = new String();
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
 %>
  <body class="corpo" onLoad="Javascript:Init();abilitaTipoUfficio();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Misura Alternativa</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaRevocaMA">
    <table width=35%>
   		<tr>
    		<td class="l" width==30%> Data Emissione</td>
     		<td class="l" width==70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   		</tr>
    </table>

    <tr> 
    	<td>&nbsp;</td> 
    </tr>
    
    <table cellspacing="2" cellpadding="2" style="width: 90%;">
    	<tr>
        <td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    	</tr>
    	<tr>
        <td class="l" colspan=2 > Oggetto </td>
        <td class="l" colspan=2 > Esito </td>
    	</tr>
   <%
   for (int i=0; i< tenori.length;i++)
   {
   %>
       <tr>
        <td class="l"colspan=2 >
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2 >
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>
		  <tr>
		  	<td>&nbsp;</td> 
		  </tr>

    	<tr>
				<td class="l" colspan="2">Ulteriore descrizione della decisione</td>
    		<td class="l" colspan="2"><TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="65" rows="4" ></textarea></td>
			</tr>

    </table>
	<br>
 	<table cellspacing="2" cellpadding="2" style="width: 90%;">
  	<tr> </tr>
  	<tr>
    	<td class="Titolo" colspan=6> Estremi ordinanza revocata: <td>
  	</tr>
    <tr>
      <td class="l">Data Emissione <br> (gg/mm/aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l"><input value="TDS" type="radio" name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>" onClick=abilitaTipoUfficio(); checked><%=labelTribunale%></td>
      <td class="l">
        <input Title="Magistrato" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>" value="" size=35 >
        <a href="Javascript:ListaProcure('InserisciOrdinanzaRevocaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>[0]');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>

    <tr>
      <td class="l"> </td>
      <td class="L"> </td>
      <td class="l"><input value="UDS" type="radio" name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>" onClick="abilitaTipoUfficio();"><%=labelUfficio%></td>
      <td class="l">
        <input Title="<%=labelUfficio%>" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciOrdinanzaRevocaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>[1]','<%=CodUff%>');">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

  	<tr> </tr>
  	<tr>
    	<td class="Titolo" colspan=6> Estremi decreto Magistrato Sorveglianza: <td>
  	</tr>
    <tr>
      <td class="l">Data Emissione <br> (gg/mm/aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l"><%=labelUfficio%>  </td>
      <td class="l">
        <input Title="<%=labelUfficio%>" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciOrdinanzaRevocaMA','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=CodUff%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>
  	<tr>
  		<td class="Titolo" colspan=6> Indicare obbligatoriamente almeno uno dei seguenti campi (*): <td> 
  	</tr>
    <tr>
      <td class="l">Data decorrenza revoca <br> (gg/mm/aaaa)</td>
      <td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Pena rideterminata <br> (AA-MM-GG)</td>
      <td class="L"> Reclusione <br>
        <input value="" title="Numero Anni Detenzione" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM %>"  > -
        <input value="" title="Numero Mesi Detenzione" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM %>"  > -
        <input value="" title="Numero Giorni Detenzione" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM %>"  >
      </td>
      <td class="L"> Arresto <br>
        <input value="" title="Numero Anni Arresto Revocato" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_ARRESTO_REV %>"  > -
        <input value="" title="Numero Mesi Arresto Revocato" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV %>"  > -
        <input value="" title="Numero Giorni Arresto Revocato" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_ARRESTO_REV %>"  >
      </td>
    </tr>
  	<tr> </tr>
  	<tr> 
  		<td>&nbsp;</td> 
  	</tr>
    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 	</table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaRevocaMA");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_ARRESTO_REV%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_ARRESTO_REV%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_ARRESTO_REV%>","numeric");
   frmvalidator.setAddnlValidationFunction("Verify");
  </script>
 </body>
</html>