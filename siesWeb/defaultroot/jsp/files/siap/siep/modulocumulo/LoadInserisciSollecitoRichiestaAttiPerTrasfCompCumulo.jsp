<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="siap.siep.sollecitoesitotrasmissione.action.ICostantiSollecitoEsitoTrasmissione"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="fascicolo"     scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="UtenteConnesso"  scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="oggettoProvvedimento"  scope="request" class="siap.sico.decodifiche.model.DecodificheModel"/>
<jsp:useBean id="oggettoSollecito"            scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioDestinatarioSollecito"      scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="messaggioRichiesta"          scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<%// [DATI PER ORA NON UTILIZZATI] %>
<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="dataeditabile"         scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="residenza"             scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="penacumulo"            scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel"/>

<%// Ufficio a cui inviare il sollecito %>
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaN"      scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita"      scope="request" class="java.lang.String"/>

<%// In caso di modifica %>
<jsp:useBean id="contenuto"            scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"               scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="autoritaEsternaNSede" scope="request" class="java.lang.String"/>

<!--      LoadInserisciSollecitoRichiestaAttiPerTrasfCompCumulo      -->

<html>
<head>
<title>[S.I.E.S.] - Riscontro Trasmissione Atti per Trasf.Comp. - Sollecito</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
 

  function Verifica(){
 
    //======================
    // Data Emissione
    //======================
    if (document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'
       +document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        
    if (document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'
       +document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
       
    var data_to_verify =     document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
                        +'-'+document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
                        +'-'+document.SollecitoRichiestaAtti.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data di Emissione non valida');
      return false;
    }
    
    //======================
    // Data Trasmissione
    //======================
    if (document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'
       +document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
        
    if (document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'
       +document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
                    +'-'+document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
                    +'-'+document.SollecitoRichiestaAtti.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data di Trasmissione non valida');
      return false;
    }
    
    //======================== 
    if(document.SollecitoRichiestaAtti.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" 
       && document.SollecitoRichiestaAtti.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
    {
      alert("Il  Magistrato Firmatario è obbligatorio");  
      document.SollecitoRichiestaAtti.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus()  ;
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
  
  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
  {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function clearSedeALtroDest(){
    if (document.SollecitoRichiestaAtti.AltroDestinatario.value=="-"){
      document.SollecitoRichiestaAtti.SedeAltroDestinatario.value=""  ;
    }
  }

  </script>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">SOLLECITO RICHIESTA ATTI PER TRASMISSIONE COMPETENZA CUMULO </font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActDettaglioRichiestaAttiCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=messaggioRichiesta.getIdMessaggio()%>">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>
 <%
  //===================================================================
  //  INTESTAZIONE
  //===================================================================
  SoggettoModel soggetto = fascicolo.getSoggetto();
  SentenzaModel sentenza = fascicolo.getSentenza();  
%>    
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento : N.</font>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
            <%=fascicolo.getChiaveAnno()%>
            /
            <%=fascicolo.getChiaveProgr()%>
          </a>
          &nbsp;
          <% if(fascicolo.getFlagCumulante()!=null && fascicolo.getFlagCumulante().equals("S")) { %>
            <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
          <% } %>

          <% if(fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-")) {%>
            <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
          <% } %>

          <% if(   fascicolo.getCodStatoFascicolo() != null && fascicolo.getCodStatoFascicolo().equals("01")) { %>
            <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
          <% } %>

          <%
            if(   (   penaresidua != null
                   && "S".equals(penaresidua.getFlagPenaSospesa())
                  )
               || (    fascicolo!= null && fascicolo.getChiaveProgr() != null
                    && fascicolo.getChiaveProgr().intValue() >= 30000
                    && fascicolo.getChiaveProgr().intValue() < 40000
                  )
              )
            {
              if(   fascicolo!= null && fascicolo.getChiaveProgr() != null
                 && fascicolo.getChiaveProgr().intValue() >= 30000
                 && fascicolo.getChiaveProgr().intValue() < 40000
                )
              {
              %>
              <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
              <% } else { %>
              <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
              <%
              }
            }

            if(   penaresidua != null
               && penaresidua.getFlagPenaSospesa()!= null
               && penaresidua.getFlagPenaSospesa().equals("I"))
            {
            %>
            <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
            <%
            }
        
            if(   penaresidua != null
               && penaresidua.getFlagPenaSospesa()!= null
               && penaresidua.getFlagPenaSospesa().equals("D"))
            {
            %>
            <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
            <% } %>
            
            <% if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))) { %>
              &nbsp;
              <font class="label"><%=fascicolo.getDescrTipoUfficio() + " DI " + fascicolo.getDescrComuneUfficio() %></font>
            <% } %>
      </td>
    </tr>
    
    
    <tr>
      <td class="L" width=100%><font class="label">Soggetto : </font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
      <% if (soggetto.getSesso().compareTo("F")==0) { %>
          <font class="label">nata il :</font>&nbsp;
      <% } else { %>
          <font class="label">nato il :</font>&nbsp;
      <% } %>

      <% 
      if(soggetto.getDataNascita() == null)
      {
          if(soggetto.getDataNascitaPresunta().equals("S")) {%>
          <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
          <% } else {%>
          <font class="campo">***</font>&nbsp;
          <%}
      } else {%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
      <% } %>
      
      <font class="label">in : </font>
      <font class="campo">
      <% if (soggetto.getDescrComuneNascita().compareTo("-")==0) { %>
          <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
      <% } else { %>
          <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
      <% } %>
      </font>
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          <%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%> </a>&nbsp;  
          <font class="label">del</font>&nbsp;
            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
        </font>
        <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> 
        &nbsp;<font class="label"> Emessa da: </font> <% 
        }else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
        <% if (sentenza.getNumSezioneAutoritaEmittente() != null) { %>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
        <% } %>
        <font class="label"> di </font>
        <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
  </table>
  
<br>
<jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/> 

<%
//==============================================================================
%>
    
<br>  
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="SollecitoRichiestaAtti" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciSollecitoRichiestaAttiPerTrasfCompCumulo">
  
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=messaggioRichiesta.getIdMessaggio()%>">
  <input type="HIDDEN" name="<%=ICostantiSollecitoEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_SOLLECITATO%>" value="<%=messaggioRichiesta.getIdMessaggio()%>">
  <input type="HIDDEN" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(messaggioRichiesta.getCodUfficioDestinatario(),"")%>">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(ufficioDestinatarioSollecito.getCodTipoUfficio(), "")%>">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(ufficioDestinatarioSollecito.getCodComune() ,"")%>">
 
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(evento.getIdEvento(),"")%>">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="5203">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=oggettoProvvedimento.getCode()%>">

  <input type="HIDDEN" name="modalita" value="<%=modalita%>">

  <%
  Date dataEmissione = DateUtils.getSysDate();
  Date dataTrasmissione = DateUtils.getSysDate();
  if (evento.getIdEvento()!=null){
    dataEmissione = evento.getDataEmissione();   
    dataTrasmissione = evento.getDataTrasmissioneAtti();
  }
  %>
  <table>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" >
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"dd"),"")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"MM"),"")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"yyyy"),"")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
  
      <td class="l">Data Trasmissione</td>
      <td class="L">
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"dd"),"")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"MM"),"")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"yyyy"),"")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>


<%
//==============================================================================
//      Ufficio Competente all'emissione del Provvedimento
//==============================================================================
%>
<table width=90%>
  <tr>
    <td class="Titolo" colspan="2">Sollecito Richiesta Atti</td>
  </tr>
  
<%--  
      <tr>
      <td class="l">Autorità</td>
      <td class="l">
        <select name="< %= ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO %>" >
          <option value="-">-</option>
          < %=ufficioDestinatarioSollecito%>
        </select>    
      </td>
    </tr>
  
    <tr>
      <td class="l">Luogo</td>
      <td class="L">
        <input type="text" title="Sede Ufficio"  maxlength="35" size="35" name=" %=ICostantiEvento.CAMPO_CODICE_LUOGO_DESTINATARIO %>" 
                  value="< %=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrLuogoEmittenteIV(), "" ) %>">
         <a href="Javascript:ListaUfficiPerTipo('SollecitoRichiestaAtti','< %=ICostantiEvento.CAMPO_CODICE_LUOGO_DESTINATARIO %>',document.SollecitoRichiestaAtti.< %=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>.value);">
          <img src="/images/filefolder.gif" border=0></a>
        </a>
      </td>
    </tr>
--%>  
  
  
  <tr>
    <td class="l">A Ufficio del Pubblico Ministero</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(messaggioRichiesta.getDescrUfficioDestinatario() ,"")%>&nbsp;</font>
    <%--
    <Input type="text" title="Tipo Ufficio" maxlength="70" size="70" 
          name="DescrUfficio" 
          value="<%=StringUtils.toStringJSP(messaggioRichiesta.getDescrUfficioDestinatario() ,"")%>">  
    --%>
    </td>
  </tr>
  <tr>
    <td class="l">Luogo </td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(messaggioRichiesta.getDescrSedeUfficioDestinatario() ,"")%>&nbsp;</font>
    <%--
    <Input type="text" title="Luogo Ufficio" maxlength="70" size="70" 
          name="DescrSedeUfficio"  
          value="<%=StringUtils.toStringJSP(messaggioRichiesta.getDescrSedeUfficioDestinatario() ,"")%>">
    --%>
    </td>
  </tr>       


  <tr>
    <td class="l">Oggetto</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(oggettoProvvedimento.getDescription())%>&nbsp;</font>
      <%--
      <input type="text" title="Oggetto_Sollecito" maxlength="90" size="90" 
      name="Descr_Oggetto_Soll" value="<%=StringUtils.toStringJSP(oggettoProvvedimento.getDescription())%>"></td>
      --%>
  </tr>
  
  <tr>
    <td class="l">Contenuto</td>
    <td  class="L" >
      <TEXTAREA title="Contenuto" name="camponote" cols=90 rows=5 ></TEXTAREA>
    </td> 
  </tr>
 
  <tr>
    <td class="l" >Magistrato Firmatario <font class=ob>(*)</font></td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" 
                      value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" 
                      type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    
                      value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" 
                      type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('SollecitoRichiestaAtti','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>          
    </td>
  </tr>
  
  <tr>
    <td class="l">Altro Destinatario</td>
    <td class="L" colspan="7">    
      <select Title="Altro Destinatario" class="small" name="AltroDestinatario" >
      <%=autoritaEsternaN%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>   
    <td class="L" colspan="3">
      <input title="Sede Altro Destinatario" type="text" name="SedeAltroDestinatario" maxlength="35" size="35">
      <a href="Javascript:ListaComuni('SollecitoRichiestaAtti','SedeAltroDestinatario');">
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

</form>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("SollecitoRichiestaAtti");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");
  
  // DATA TRASMISSIONE
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

</script>
</body>
</html>