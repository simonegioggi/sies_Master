<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>

<jsp:useBean id="decretoordinanza"    scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>

<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="avvocati"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaE"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"     scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="CodiceMotivo" scope="request" class="java.lang.String" />

<!--
< jsp:useBean id="sospensione"   scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
-->
<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();

  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

/*
  //Inizializzazione campi se SOSPENSIONE non trovata
  //(per evitare eventuale NullPointerException)
  if(sospensione.getIdSospensione() == null)
    sospensione.setQuantumZero();
*/
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Revoca Sospensione </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function pulisciCodMagistrato()
    {
      document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = '';
    }

    function Verify()
	  {
		  // DATA EMISSIONE
      if (document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_emissione = document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_emissione) )
		  {
        alert('Data di emissione non valida');
        document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();

        return false;
		  }

      // DATA TRASMISSIONE
      if (document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

		  var data_trasmissione = document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

      if (!ControllaData(data_trasmissione) )
		  {
        alert('Data di trasmissione non valida');
        return false;
		  }

      if(document.LoadInserisciOrdineEsecuzioneRevoca.<%= ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il Magistrato competente è obbligatorio");
        document.LoadInserisciOrdineEsecuzioneRevoca.<%= ICostantiMagistrato.CAMPO_COGNOME%>.focus();

        return false;
      }

      if(document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value=="-")
      {
        alert("L' Autorità Destinazione è obligatoria");
        document.LoadInserisciOrdineEsecuzioneRevoca.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();

        return false;
      }
    }

    // magistrato competente
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
  </script>
  <script language="JavaScript1.2">
    <!--
      function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
    -->
  </script>

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Revoca Sospensione</font>

      </td>
     <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
    </td>

    </tr>
  </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciOrdineEsecuzioneRevoca">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.revoca.action.ActInserisciOrdineEsecuzioneRevoca">
    <input type="HIDDEN" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ID_EVENTO_GENERATO%>" value="<%= StringUtils.toStringJSP(decretoordinanza.getIdEventoGenerato()) %>">
    <input type="HIDDEN" name="CodMotivo" value="<%=CodiceMotivo%>">
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=7>
        <font class="campo">
<%
          if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
          {
%>
            DETENUTO PER ALTRA CAUSA
<%
          }
          else
          {
%>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
          }
%>
        </font>
      </td>
    </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
             //  if(lAltraCausa.getDescrLuogoIstituto()!=null)
              // {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
               //}
%>
             </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
             // if(lLuogoDetenzione.getDescrLuogo()!=null)
             // {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
             // }
%>
            </td>
          </tr>
<%
        }
%>
    <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>

<%
/*
       if (penaresidua.getDataInizio() != null)
       {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<tr>
  <td class="l">Data Decorrenza Pena</td>
  <td class="L">
    <font class="campo">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
    </font>
  </td>
</tr>
<%-- <% --%>
<!-- } -->
<!-- if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFinePresunta() != null) -->
<!-- { -->
<!-- %> -->
<%--
        <tr>
          <td class="l">Data Fine Pena Automatica</td>
          <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
        </tr>
--%>
<%-- <% --%>
<!-- } -->
<!-- if ( penaresidua.getFlagErgastolo() != null && penaresidua.getFlagErgastolo().equals("S") ) -->
<!-- { -->
<%-- %> --%>
<!--
<tr>
  <td class="l">Pena Detentiva</td>
  <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
</tr>
-->
<%-- <% --%>
<!-- } -->
<!-- %> -->
<!--
      </tr>
      <tr>
-->
<%-- <% --%>
<!-- if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null) -->
<!-- { -->
<!--   String lClassTd="l"; -->
<!--   String lClassFont="campo"; -->
<!--   if( penaresidua.getDataFine() != null && !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()) ) -->
<!--   { -->
<!--     lClassTd="lRosso"; -->
<!--     lClassFont="lRosso"; -->
<!--   } -->
<%-- %> --%>
<%--
<td class="l">Data Fine Pena</td>
<td class="< %=lClassTd%>">
  <font class="< %=lClassFont%>">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;
  </font>
</td>
--%>
<%-- <% --%>
<!-- } -->
<!-- %> -->
<!--
      <tr>
-->
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
<tr>
          <td class="l">Reclusione</td>
          <td class="l" >
            <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
<%
          if(penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          {
%>
            <td class="l">Multa</td>
            <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          }
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
<%
      if(penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
%>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
  }
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
  <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>"
  	type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
  </tr>
--%>
<%-- <% --%>
<!-- if (  sospensione.getNumAnniPenaEspiata().intValue()!=0 -->
<!-- || sospensione.getNumMesiPenaEspiata().intValue()!=0 -->
<!-- || sospensione.getNumGiorniPenaEspiata().intValue()!=0 ) -->
<!-- { -->
<%-- %> --%>
<%--
<tr>
	<td class="l">
		<font class="label">Pena Espiata</font>
	</td>
	<td class="l">
		<font class="label">Anni</font>
		<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
		<font class="label">Mesi</font>
		<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
		<font class="label">Giorni</font>
		<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
	</td>
</tr>
--%>
<%-- <% --%>
<!-- 	  } -->
<!-- 	  if (flagergastolo.equals("N") -->
<!--        && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0 -->
<!-- 	  	 || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0 -->
<!-- 	  	 || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0 -->
<!--        || sospensione.getNumAnniPenaResiduaArres().intValue()!=0 -->
<!-- 	  	 || sospensione.getNumMesiPenaResiduaArres().intValue()!=0 -->
<!-- 	  	 || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0) -->
<!--       ) -->
<!-- 	  { -->
<!-- %> -->
<!--
<tr>
<td class="l">
	<font class="label">Pena Residua</font>
</td>
<td class="l">
-->
<%-- <% --%>
<!-- if (flagergastolo.equals("N") -->
<!-- && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0 -->
<!-- || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0 -->
<!-- || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0) -->
<!-- ) -->
<!-- { -->
<%-- %> --%>
<%--
<font class="label">Reclusione : </font>
<font class="label">Anni</font>
<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%></font>
<font class="label">Mesi</font>
<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%></font>
<font class="label">Giorni</font>
<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
--%>
<%-- <% --%>
<!-- } -->
<!-- if (  flagergastolo.equals("N") -->
<!--    && (sospensione.getNumAnniPenaResiduaArres().intValue()!=0 -->
<!--    || sospensione.getNumMesiPenaResiduaArres().intValue()!=0 -->
<!--    || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0) ) -->
<!-- { -->
<%-- %> --%>
<%--
<font class="label"> Arresto : </font>
<font class="label">Anni</font>
<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%></font>
<font class="label">Mesi</font>
<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%></font>
<font class="label">Giorni</font>
<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
--%>
<%-- <% --%>
<!-- } -->
<!-- if (  flagergastolo.equals("N") -->
<!--    && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0 -->
<!--    || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0 -->
<!--    || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0 -->
<!--    || sospensione.getNumAnniPenaResiduaArres().intValue()!=0 -->
<!--    || sospensione.getNumMesiPenaResiduaArres().intValue()!=0 -->
<!--    || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0) -->
<!--   ) -->
<!-- { -->
<!-- %> -->
<!--
  </td>
</tr>
-->
<%
/*
          }
    }
*/
    if(flagergastolo.equals("S"))
    {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO</font>
        </td>
      </tr>
<%
    }else if(flagergastolo.equals("D"))
     {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
        </td>
      </tr>


<%
     }
%>
    </table>
    <br>
    <table style="width: 95%;">
      <tr>
        <td colspan=5 class="titolo">Dati del provvedimento di sospensione dell'esecuzione</td>
      </tr>
      <tr>
        <td class="l" width="19%">Data ricezione provv.</td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(),"dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
    </tr>
    <tr>
      <td class="l">
        Registro
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrTipoRegistroOrdinanza())%></font>&nbsp;
        Anno/Numero:
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAnnoRegistro())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getNumRegistro())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Provvedimento
      </td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrTipoProvvedimento())%></font>&nbsp;
        Data emissione:
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(),"dd-MM-yyyy"))%>&nbsp;
        </font>
        Anno/Numero:
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAnnoProvvedimento())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getNumProvvedimento())%></font>&nbsp;
      </td>
    </tr>
  </table>
  <table style="width: 95%;">
    <tr>
      <td class="l" width="19%">
        Autorità emittente
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrLuogoEmittente())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Contenuto decisione
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrOggettoDecisione())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Oggetto decisione
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrOggettoProcedimento())%></font>&nbsp;
      </td>
    </tr>
<%
    String lCodTipoRegistro = StringUtils.toStringJSP(decretoordinanza.getCodTipoRegistroOrdinanza());
    if(!lCodTipoRegistro.equals("0001")) // SIUS
    {
%>
      <tr>
        <td class="l">
          Tipologia decisione
        </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrEsito())%></font>&nbsp;
        </td>
      </tr>
<%
    }
%>
    <tr>
      <td class="l">
        Motivazioni
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getMotivazioni())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Data revoca
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRevocaSospensione(),"dd-MM-yyyy"))%>&nbsp;
          &nbsp;
<%
/*
          if( decretoordinanza.getFlagScarcerareScarcerato()!=null
             && decretoordinanza.getFlagScarcerareScarcerato().equals("S")
             )
          {
            out.print("[Già scarcerato]");
          }
          else if(decretoordinanza.getFlagScarcerareScarcerato()!=null
                && decretoordinanza.getFlagScarcerareScarcerato().equals("D"))
          {
            out.print("[Da scarcerare]");
          }
*/
%>
        </font>
      </td>
    </tr>
  </table>
  <table style="width: 95%;">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Data Trasmissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" colspan=6> Magistrato </td>
    </tr>
    <tr>
      <td class="l">Magistrato Competente</td>
      <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25" onChange="pulisciCodMagistrato()">
        <input title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25" onChange="pulisciCodMagistrato()">
        <a href="Javascript:ListaMagistrati('LoadInserisciOrdineEsecuzioneRevoca','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="Titolo" colspan=6>Destinatario per l'esecuzione</td>
    </tr>
    <tr>
<!--autorità di polizia-->
      <td class="l">Autorità Destinazione </td>
      <td class="L">
        <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
          <%=autoritaEsternaE%>
        </select>
      </td>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
      <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=20 rows=5></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede<font class=ob>(*)</font></td>
      <td class="L">
        <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzioneRevoca','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
<%
      int lIdxAvv = 0;

      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>
        <table>
          <tr>
            <td class="l">Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </tr>
        </table>
        <table>
          <tr><td class="l">Autorità Destinazione</td>
          <td class="L">
            <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
              <%=autoritaEsternaN%>
            </select>
         </td>
        <td rowspan=2 class="l">Note</td>
        <td rowspan=2 class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=20 rows=5></textarea>
        </td>
     </tr>
     <tr>
      <td class="l">Sede </td>
      <td class="L">
        <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzioneRevoca','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
  </tr>
  <tr>
    <td class="lNoBord" colspan="2">
    <br>
    <INPUT class="bottone" type="submit" name="I" value="Conferma">
    </td>
  </tr>
</table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciOrdineEsecuzioneRevoca");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

    frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Sede Autorità Destinazione obbligatoria");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
 </body>
</html>