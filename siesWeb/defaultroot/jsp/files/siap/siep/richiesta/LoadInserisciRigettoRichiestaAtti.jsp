<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.competenza.action.ICostantiCompetenza"%>
<%@ page import="siap.siep.competenza.model.CompetenzaModel"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="org.apache.log4j.Logger"%>

<jsp:useBean id="titolo"                scope="request" class="java.lang.String"/>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"         scope="request" class="java.lang.String"/>

<jsp:useBean id="insertManuale"         scope="request" class="java.lang.String" />
<jsp:useBean id="senFascicoloTrovato"   scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="fasFascicoloTrovato"   scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="richiesta"             scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"               scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaN"      scope="request" class="java.lang.String"/>

<jsp:useBean id="MessaggioRichiesta"    scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<jsp:useBean id="soggetto"		    	scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="competenza"   			scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>
<jsp:useBean id="UfficioAccorpatoOrigine" 	scope="request" class="siap.sico.ufficio.model.UfficioAccorpatoModel" />
<jsp:useBean id="UfficioOld" 				scope="request" class="siap.sico.ufficio.model.UfficioModel" />

<%
	//[FT] - 03/08/2016 - MAC_LOG - Logger per SIESLog
	final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  //setto il flag per la verifica dell'inserimento manuale
  boolean insManuale = false;
  if (insertManuale.equalsIgnoreCase("1"))
    insManuale = true;  
  
  
  //====================
    
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
  
//Costruzione della stringa con i dati del Titolo che determina la competenza
String TitoloComp="";
String lLabel="";

if(senFascicoloTrovato.getCodTipoProvvedimento().compareTo("01")==0 ||
	senFascicoloTrovato.getCodTipoProvvedimento().compareTo("03")==0 || 
	senFascicoloTrovato.getCodTipoProvvedimento().compareTo("05")==0 )
{
	lLabel += "pronunciata da ";
}
else
{
	lLabel += "pronunciato da ";
}

TitoloComp += senFascicoloTrovato.getDescrTipoAutoritaEmittente(); 
TitoloComp += "<font class='label'> di </font>";
TitoloComp += senFascicoloTrovato.getDescrLuogoEmittente();

if(senFascicoloTrovato.getNumSezioneAutoritaEmittente()!=null && (!senFascicoloTrovato.getNumSezioneAutoritaEmittente().equalsIgnoreCase("null")) )
{
	TitoloComp += "<font class='label'> Sezione </font>";
	TitoloComp += senFascicoloTrovato.getNumSezioneAutoritaEmittente();
}
  
// Costruzione della stringa con i dati del Titolo Richiesto
 String stringaTitolo="";
 stringaTitolo += competenza.getDescrTipoProvvedimento()+" N. "+competenza.getAnnoSentenza()+"/"+competenza.getNumeroSentenza();
 stringaTitolo += "<font class='label'> del </font>&nbsp;";
 stringaTitolo += DateUtils.getDateToString(competenza.getDataProvvedimento(),"dd-MM-yyyy");
 
 if(competenza.getCodTipoProvvedimento().compareTo("01")==0 ||
	 competenza.getCodTipoProvvedimento().compareTo("03")==0 || 
	 competenza.getCodTipoProvvedimento().compareTo("05")==0 )
 {
	 stringaTitolo += "<font class='label'> emessa da </font>";
 }
 else
 {
	 stringaTitolo += "<font class='label'> emesso da </font>";
 }
	 
 stringaTitolo += competenza.getDescrTipoAutoritaEmittente(); 
 stringaTitolo += "<font class='label'> di </font>";
 stringaTitolo += competenza.getDescrLuogoEmittente();
 
//Costruzione della stringa con i dati del Soggetto a carico del Procedimento che determina la competenza
String stringaSoggetto = "";

stringaSoggetto += "<font class='campo'>"
                   +StringUtils.toStringJSP(soggetto.getCognome())+" "
                   +StringUtils.toStringJSP(soggetto.getNome())+"</font>&nbsp;";

if (soggetto.getSesso().compareTo("F")==0)
  stringaSoggetto += "<font class='label'>nata il :</font>&nbsp;";
else 
  stringaSoggetto += "<font class='label'>nato il :</font>&nbsp;";

// Soggetto: Data Nascita
if (soggetto.getDataNascita()!=null)
  stringaSoggetto += "<font class='campo'>"+StringUtils.toStringJSP (DateUtils.getDateToString (soggetto.getDataNascita(),"dd-MM-yyyy"))+"</font>";
else if (soggetto.getDataNascitaPresunta().equals("S"))
  stringaSoggetto += "<font class='campo'>"+StringUtils.toStringJSP (soggetto.getAnnoNascita())+"</font>";
else 
  stringaSoggetto += "<font class='campo'>***</font>";


// Soggetto: Comune nascita
stringaSoggetto += "<font class='label'>&nbspin: &nbsp;</font>";
stringaSoggetto += "<font class='campo'>";
if (soggetto.getDescrComuneNascita().compareTo("-")==0)
  stringaSoggetto += "&nbsp;"+soggetto.getDescComuneNascitaEstero()+"&nbsp;("+soggetto.getDescrStatoNascita().toUpperCase()+")";
else
  stringaSoggetto += "&nbsp;"+soggetto.getDescrComuneNascita()+"&nbsp;("+soggetto.getCodProvinciaNascita()+")";
stringaSoggetto += "</font>";


stringaSoggetto += "&nbsp;<font class='label'>Codice CUI : </font>"
                   +"<font class='campo'>"+StringUtils.toStringJSP(soggetto.getCodAfis())+"</font>";

%>

<html>
<head>
<title>[S.I.E.S.] - <%=titolo.toUpperCase()%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">

  function Verifica(){
    //CONTROLLO CHE SIA STATO SELEZIONATA LA TIPOLOGIA DELL'ATTO
    if(document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=='-'){
      alert("E' obbligatorio selezionare la tipologia dell'atto");
          return false;   
    }

    if (document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'
        +document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
    if (document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'
        +document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data di Trasmissione non valida');
      return false;
    }
      
    <%	//	if(insManuale)
    	//	{ 
		// 		questa parte è stata eliminata
    	//	} %>
    
    if(document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>
      .options[document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.selectedIndex]
      .text=='-')
    {
      alert("E' obbligatorio selezionare un valore del campo 'Oggetto Atto'");
      document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
      return false;   
    }     
  
    // Magistrato Firnatario
    if(document.LoadInserisciRigetto.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciRigetto.<%=ICostantiMagistrato.CAMPO_NOME %>.value==""){
        alert("Il  Magistrato Firmatario è obbligatorio");  
        document.LoadInserisciRigetto.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus()  ;
        return false;
    } 
    
    // Campo Contenuto
	if(document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_NOTE%>.value=="")
    {
          alert("Il  Contenuto è obbligatorio");
          document.LoadInserisciRigetto.<%=ICostantiNotifica.CAMPO_NOTE%>.focus();
          return false;
    }
    
    // Sede e Luogo Altro destinatario
    if(document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>.value=="-" && 
   		 document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>.value!="" )
    {
          alert("Selezionare un Destinatario (Ufficio)");
          document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>.focus();
          return false;
    }
    
    if(document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>.value!="-" && 
   		 document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>.value=="" )
    {
          alert("Selezionare un Destinatario (Sede)");
          document.LoadInserisciRigetto.<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>.focus();
          return false;
    }
   
  }


  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3){
    var desktop;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }
  
  function ListaComuni(a_formname,a_fieldname){
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo"><%=titolo%></font>
      </td>
    </tr>
  </table>

<br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRigetto" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActInserisciRigettoRichiestaAtti">
  <input type="HIDDEN" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>"  >
  <input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" >
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO %>" value="<%=competenza.getEveIdEvento() %>" >
  <input type="hidden" name="<%=ICostantiCompetenza.CAMPO_CODICE_CUI_SOGGETTO_RICH %>" value="<%=soggetto.getCodAfis() %>">

<%
//==============================================================================
// Visualizzazione - Posizione giuridica/Luogo di detenzione/Pena residua
//==============================================================================
%>
  <table>
      <tr>
          <td class="l">Posizione Giuridica </td>
          <td class="L" colspan=5>
              <font class="campo">
<%
  if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){
%>
              DETENUTO PER ALTRA CAUSA
<%    }
      else
      {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%     }%>
            </font>
      </td>
  </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null)
           {
%>
  <tr>
      <td class="l">Detenuto presso </td>
      <td class="L" colspan=5>
        <font class="campo">
        <%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
        di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
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
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
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
        }%>

<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
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
%>


   <tr>
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
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
%>
</tr>

      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }

if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         </td>
<%
          }else if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }else{
%>
                <td class="l">Data Fine Pena</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
                </td>
<%            }
        }
      }
}
%>


</tr>



  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  
    <td class="l">Data Trasmissione</td>
    <td class="L">
      <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
</table>
<br>
<table width=90%>
  <tr>
    <td class="Titolo" colspan="4">  Titolo Richiesto  </td>
  </tr>
  <tr>
    <td class="l">Titolo</td>
    <td class="L" colspan="3"><font class="campo"><%=stringaTitolo%></font></td>
  </tr>
 
<% if (competenza.getChiaveAnno()!=null && competenza.getChiaveProgr()!=null) 
   {
	      String lAccorpato = "";
	      BigDecimal lProgressOrigine = competenza.getChiaveProgr();
	      if (   UfficioAccorpatoOrigine!=null 
	          && UfficioAccorpatoOrigine.getCodUfficio()!=null
	          && !UfficioAccorpatoOrigine.getCodUfficio().equals("")
	         )
	      {
		        lProgressOrigine = lProgressOrigine.subtract(new BigDecimal (UfficioAccorpatoOrigine.getIncrProgressivo()));
		     
		     
		        lAccorpato += "  <font class=\"cRosso\">(Ex "+StringUtils.toStringJSP(UfficioOld.getDescrTipoUfficio());
		        lAccorpato += " di "+StringUtils.toStringJSP(UfficioOld.getDescrComune());
		        lAccorpato += " ) </font>";
	      }
%>
  <tr>
    <td class="l" width="25%">Iscritto al Procedimento N. </td>
    <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(competenza.getChiaveAnno(),"&nbsp;")%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(lProgressOrigine,"&nbsp;")%></font><%=lAccorpato%></td>
  </tr>
<% }  %> 
  
</table>  
<br>
<% 
//==============================================================================
//Titolo proveniente dalla Ricerca o da Richiesta Atti
//==============================================================================
if(!insManuale) 
{ %>
<input type="hidden" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=StringUtils.toStringJSP(MessaggioRichiesta.getIdMessaggio(),"")%>">

<table width=90%>
  <tr>
    <td class="Titolo" colspan="4">Titolo Che Determina La Competenza</td>
  </tr>
  <tr>
    <td class="l" width="25%" >Provvedimento </td>
    <td class="L" colspan="3">
     <font class="campo"><%=senFascicoloTrovato.getDescrTipoProvvedimento()%></font>&nbsp;
     N.&nbsp; 
     <font class="campo"><%=StringUtils.toStringJSP(senFascicoloTrovato.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(senFascicoloTrovato.getNumeroSentenza())%></font>&nbsp;
	 DEL&nbsp;
	 <font class="campo"><%=DateUtils.getDateToString(senFascicoloTrovato.getDataProvvedimento(), "dd-MM-yyyy")%></font></td>
  </tr>
  <tr>
    <td class="l" width="25%"><%=lLabel %></td>
    <td class="L" colspan="3"><font class="campo"><%=TitoloComp%></font></td>
  </tr>
  <tr>
    <td class="l">A carico di:</td>
    <td class="L" colspan="3"><font class="campo"><%=stringaSoggetto%></font></td>
  </tr>
</table>
<br>
<table width=90%>
  <tr>
    <td class="Titolo" colspan="4">Ufficio Competente all'emissione del Provvedimento</td>
  </tr>
  <tr>
    <td class="l">Ufficio del Pubblico Ministero</td>
    <td class="L" colspan="3"><font class="campo"><%=fasFascicoloTrovato.getDescrTipoUfficio() %>&nbsp;</font></td>
  </tr>
  <tr>  
    <td class="l">Luogo</td>
    <td class="L" colspan="3"><font class="campo"><%=fasFascicoloTrovato.getDescrComuneUfficio() %>&nbsp;</font></td>
  </tr>
  <tr>
    <td class="l" width="25%">Relativa al Procedimento numero</td>
    <td class="L" colspan="3"><font class="campo"><%=fasFascicoloTrovato.getChiaveAnno()%> / <%=fasFascicoloTrovato.getChiaveProgr()%>&nbsp;</font></td>
  </tr> 
</table>
<% 
} else { 
//==============================================================================
// Inserimento Manuale o titolo caricato dalla Richiesta
//==============================================================================

siesLogger.debug("--XXXXX------>>>>>>> LoadInserisciRigettoRichiesta - ERRORE , Richiesta inserita a mano !!! <<<<<<--------");

 } %>


<%
//==============================================================================
//
//==============================================================================
%>
<table width=90%>
  <tr>
    <td class="Titolo" colspan='8'> Dati Atto </td>
  </tr>
  <tr>
    <td class="l" width="25%">Tipologia Atto <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>"  Title="Tipologia Atto" >
      <%=richiesta%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Oggetto Atto <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  Title="Oggetto Atto"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
      <%=oggetto%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Contenuto</td>
    <td  class="L" colspan="3">
     <TEXTAREA title="Contenuto" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=90 rows=5 ></textarea>
    </td>
  </tr>
 
  <tr>
    <td class="l" width="15%">Magistrato Firmatario <font class=ob>(*)</font></td>
    <td class="L" colspan="7">
      <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" 
             value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" 
             type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"
             value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" 
             type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciRigetto','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="l">Altro Destinatario</td>
    <td class="L" colspan="7">    
      <select Title="Altro Destinatario" class="small" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>" >
      <%=autoritaEsternaN%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>   
    <td class="L" colspan="3">
      <input title="Sede Altro Destinatario" type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>" maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciRigetto','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verifica();">
    </td>
  </tr>
</table>

<%if(!insManuale){
	
	%>
  <input type=hidden name="<%=ICostantiCompetenza.CAMPO_SENTENZA_SIEP_TROVATO%>" value="<%=senFascicoloTrovato.getIdSentenza()%>">
  <input type=hidden name="<%=ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO%>" value="<%=fasFascicoloTrovato.getIdFascicoloSiep()%>">
<%}%>

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRigetto");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione della Comunicazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione della Comunicazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione della Comunicazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");
  
<%  if(insManuale) {%>
    frmvalidator.addValidation("<%= ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>","numeric");             
<%  }%>


</script>
</body>
</html>