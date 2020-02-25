<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoprovvedimento"    scope="request" class="java.lang.String"/>
<jsp:useBean id="oggettoProvvedimento"  scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"             scope="request" class="java.lang.String"/>

<jsp:useBean id="aCodOggetto"          scope="request" class="java.lang.String"/>
<jsp:useBean id="aDescOggetto"         scope="request" class="java.lang.String"/>

<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// form per l'inserimento del Provvedimento di Rideterminazione Pena 'Altro'
// menu: 'Decisioni Sorveglianza - Scomputo'
//
//==============================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel     lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel  lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel            lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>

<!-- ======================================================================= -->
<!-- LoadInsRidetPenaAltroSORV.jsp                                           -->
<!-- Form per l'inserimento della rideterminazione pena da decisioni della   -->
<!-- Sorveglianza 'Ridimensionamento LA' e 'Scomputo Permesso'               -->
<!-- ======================================================================= -->

<html>

<head>
  <title> [S.I.E.S.] - Rideterminazione Pena - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
    //============================================================================
    // Funzione per il controllo dei dati prima della submit
    //============================================================================
    
    function Verify()
    {
      //===========================================
      // Controllo sui campi altra autorità
      //===========================================
      // Data Ricezione Provv AA
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value;
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value;

      var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>_AA.value;
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di ricezione Provvedimento Altra Autorità non valida');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.focus();
        return false;
      }
      
      // Data Emissione
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value;
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value;

      var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA.value;
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione Provvedimento Altra Autorità non valida');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.focus();
        return false;
      }
      
      // Tipo provvedimento
      if (document.f.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.selectedIndex==0)
      {        
        alert("Selezionare Tipo Provvedimento");
        document.f.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.focus();
        return false;
      }
      
      // Autorità emittente 
      if (document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.selectedIndex==0)
      {        
        alert("Selezionare Autorità Emittente");
        document.f.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.focus();
        return false;
      }
      
      // Sede 
      if (document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.value=="")
      {        
        alert("Selezionare Sede Autorità Emittente");
        document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.focus();
        return false;
      }

      // Oggetto 
      if (document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.selectedIndex==0)
      {        
        alert("Selezionare Oggetto del Provvedimento");
        document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.focus();
        return false;
      }

      // Giorni 
      if (document.f.GiorniComputo.value=="")
      {        
        alert("Inserire Giorni");
        document.f.GiorniComputo.focus();
        return false;
      }
      
      //==================
      //
      //==================
      if (document.f.GiorniComputo.value=="")
      {        
        alert("Indicare i giorni");
        document.f.GiorniComputo.focus();
        return false;
      }
      return true;
    }

    //==========================================================================
    //
    //==========================================================================
    function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
    {
       var desktop;
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    //==========================================================================
    //
    //==========================================================================
    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
    
    //==========================================================================
    //
    //==========================================================================
    function ListaLicenzeAnticipate(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaLiberazioneAnticipata&flaglicenza=Scomputo&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lPosizione.getFasSieIdFascicoloSiep()%>", "Lista_Licenze_Anticipate", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=700, height=500");
    }

    //==========================================================================
    //
    //==========================================================================
    function scegliProvv()
    {
	      if (document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.value=='2250'){
			for(var k=0;k<document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options.length;k++){
			    if(document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options[k].value=="0958"){
			    	document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options[k].selected=true;
			      	break;
			  	}
			}
		  } else {		
		      if (document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA.value=='0039'){
				for(var k=0;k<document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options.length;k++){
				    if(document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options[k].value=="0994"){
				    	document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB.options[k].selected=true;
				      	break;
				  	}
				}
			  }
		 }
    }
  </script>
<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>  
</head>

<body class="corpo" >

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">

  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciRidetPenaScomputiSorv">


  
   	<input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="">
	<input type="hidden" name="<%=ICostantiLicenzaLibanticipata.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>_AA" value="">
	<input type="hidden" name="TipoOrd" value="altroUfficio">


  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Rideterminazione della Pena - Scomputo Permesso</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<!-- ======================================================================== -->
<!--   Sezione con Posizione Giuridica, Luogo di detenzione e Pena da Espiare -->
<!-- ======================================================================== -->
<table>
   	<tr>
   		<td class="l">Posizione Giuridica </td>
   		<td class="L" colspan=5>
      		<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
				<%-- 20181113: prevenzione nullpointer --%>
				DETENUTO PER ALTRA CAUSA <%=posizioneluogoaltra.getAltraCausa() != null ? StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getDescrTipoPosGiuridica()) : ""%>
<%
} else {
%>
				<%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
<%
}
%>
			</font>
   		</td>
   	</tr>
<%
//======================
// Luogo di detenzione
//======================
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	if (lAltraCausa.getIstitutoDetenzione() != null) {
%>
      <tr>
        <td class="l">Detenuto presso </td>
        <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto())%></font>
           di<font class="campo"> <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrComune())%></font>
        </td>
      </tr>
      <%
      }

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
    else if(lLuogoDetenzione.getIstitutoDetenzione() != null)
    {
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

    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI (02, 04)
    if(    lPosizione.getCodPosizioneGiuridica() != null 
        && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) 
        && lLuogoDetenzione.getAltroLuogo() != null
      )
    {
    %>
      <tr>
        <td class="l">Altro Luogo </td>
        <td class="L" colspan=5>
          <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
        </td>
      </tr>
    <%
    }
    %>
    
    <%
    //==========================================================
    // Sezione con la pena da espiare se diversa da ergastolo
    //==========================================================
    if(   penaresidua.getIdPenaResidua() != null 
       && (   penaresidua.getFlagErgastolo() == null
           || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) 
          ) 
      )
    {
      // Reclusione se presente
      if ( !penaresidua.isQuantumReclusioneZero() )  {
      %>
      <tr>
        <td class="l">Reclusione</td>
        <td class="l" colspan=2>
          <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
        </td>
        <td class="l">Multa</td>
        <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>
      <% } %>
 
      <% if ( !penaresidua.isQuantumArrestoZero()) { %>
      <tr>
        <td class="l" >Arresto</td>
        <td class="l" colspan=2>
          <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
        </td>
        <td class="l">Ammenda</td>
        <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>
      <% } %>
    <% } %>



    <tr>
    <%
    //========================================================================
    // Visualizzazione delle date di decorrenza e scadenza.
    // n.b. Le date potrebbero essere presenti se OE contro detenuto agli
    //      arresti domiciliari o Altra Causa
    //========================================================================
    
    // Inizio Pena
    if(   (   !lPosizione.getCodPosizioneGiuridica().equals("07") 
           && !lPosizione.getCodPosizioneGiuridica().equals("10")
           && !lPosizione.getCodPosizioneGiuridica().equals("16") 
           && !lPosizione.getCodPosizioneGiuridica().equals("20") 
           && !lPosizione.getCodPosizioneGiuridica().equals("46")
           && !lPosizione.getCodPosizioneGiuridica().equals("47")
          ) 
       || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) 
      )
    {
      if (penaresidua.getDataInizio() != null)
      {
      %>
        <td class="l">Data Decorrenza Pena</td>
        <td class="L">
           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "yyyy") )%></font>
        </td>
        
      <%
      }

      // ERGASTOLO
      if (penaresidua.getFlagErgastolo() != null)
      {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
        %>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
        <%
        }
        else if(penaresidua.getFlagErgastolo().equals("D"))
        {
        %>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
        <%
        }
      }
    }



    // Fine Pena
    if(   (   !lPosizione.getCodPosizioneGiuridica().equals("07")
           && !lPosizione.getCodPosizioneGiuridica().equals("10")
           && !lPosizione.getCodPosizioneGiuridica().equals("16")
           && !lPosizione.getCodPosizioneGiuridica().equals("20")
           && !lPosizione.getCodPosizioneGiuridica().equals("46")
           && !lPosizione.getCodPosizioneGiuridica().equals("47")
          )
       || (   lFascicoloAssociato.getFlagAltraCausa() != null
           && lFascicoloAssociato.getFlagAltraCausa().equals("S") 
          )
      )
    {
      if(    (    penaresidua.getFlagErgastolo() == null)
          || (   penaresidua.getFlagErgastolo() != null
              && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")
             )
        )
      {
        if( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null )
        {
        %>
        <td class="l">Data Fine Pena</td>
        <td class="L" colspan=2>
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <%
        }
        else if( penaresidua.getDataFine() != null)
        {
        %>
          <td class="l">Data Fine Pena</td>
          <% if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) { %>
          <td class="L" colspan=2>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
          <% } else { %>
          <td class="lRosso" colspan=2>
            <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
          <% } %>  
            <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
            <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
            <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
          </td>
        <%
        }
      }
    }
    %>
    </tr>
  </table>
<!-- ======================================================================= -->
<!--  FINE sezione con la pena residua                                       -->
<!-- ======================================================================= -->



  <table style="width: 100%;">
    <tr>
      <td colspan=4 class="titolo">Dati Altra Autorità</td>
    </tr>
    
    <%
    //========================
    // Dati della Sorveglianza
    //========================
	%> 
    <tr>
      <td class="l" colspan="2">
      	<input type="hidden" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>">
        <a href="Javascript:ListaLicenzeAnticipate('f');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
    </tr>
    <tr>
      <td class="l">Data emissione provvedimento</td>
      <td class="l">
        <input type="text" Title="Giorno Emissione provvedimento" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=DateUtils.getSysDate("MM")%>"   name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA"   maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Anno / Numero Provvedimento</td>
      <td class="l">
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      	<%-- 
         <input Title="Anno Provvedimento"   value="" name="<%= ICostantiLicenzaLibanticipata.CAMPO_ANNO_ORDINANZA%>"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORDINANZA%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
         --%>
         <input Title="Anno Provvedimento"   value="" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>
    

    <tr>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      <td class="l">
        <select Title="Tipo Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA">
          <%=tipoprovvedimento%>
        </select>
      </td>
      <td class="l">Oggetto</td>
      <td class="l">
        <select Title="Oggetto" class="small" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AA" 
        onchange="scegliProvv()">
          <%
			// < %= oggettoProvvedimento % >
          %>
          	<option value = "-" selected>-</option>
			<option value = "0039">Reclamo Avverso Scomputo Periodo Permesso</option>
			<option value = "2250">Esclusione Computo Permesso</option>
        </select>
      </td>
    </tr>

    <tr>
		<td class="l">Anno / Numero SIUS</td>
		<td class="l">
			<input Title="Anno Fascicolo Sius" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
			/
			<input Title="Numero Sius" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>" type="text" size="6" maxlength="6">
    </tr>    


    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="l">
        <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "", ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE+"_AA", autorita)%>
      </td>

      <td class="l" colspan="2">Sede <font class="ob">(*)</font> &nbsp;
        <input title="Sede Autorita"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA"  maxlength="35" size="35">
        <a href="Javascript:ListaComuniEmitUTMinor('f','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
    <tr>
      <td class="l">Data ricezione provvedimento</td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Ricezione" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA" maxlength="2" size="2"  
        onFocus="javascript:textboxSelect(this)" 
        onkeypress="return TicTabNumField(this,event)" 
        onBlur="javascript:value=FillDM(value)"
        >
        /
        <input type="text" Title="Mese Ricezione" value="<%=DateUtils.getSysDate("MM")%>"   name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>_AA" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
  

<%
//==============================================================================
//        Sezione per specificare i giorni da computare 
//==============================================================================
%>
<table style="width: 95%; border: 0;">
  <tr>
    <td colspan=4 class="titolo">Dati del Provvedimento</td>
  </tr>
  
    <tr>
      <td class="l">Oggetto Provvedimento <font class=ob>(*)</font>

        <select Title="Oggetto" class="small" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>_AB">
			 <%=oggettoProvvedimento%>
		</select>
	</td>
    </tr>
  <tr>
    <td class="l">
      <font class="label">Giorni da scomputare <font class=ob>(*)</font>&nbsp;</font>
      <input type="text" name="GiorniComputo" maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
</table>  

<!-- 
================================================================================
      Sezione con data Emissione, data trasmissione e magistrato
================================================================================
-->

<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan="6"> Magistrato Firmatario </td>
  </tr>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <tr>
    <td class="l">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="l">Note :&nbsp;</td>
    <td class="l" colspan="4"><textarea cols="100" rows="2" name="noteComputo"></textarea></td>
  </tr>
</table>

<table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" name="bottConferma" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>

</form>
<script language="JavaScript" type="text/javascript">
	var frmvalidator  = new Validator("f");
    frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>