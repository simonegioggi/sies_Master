<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="oggettodefinzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutorita" scope="request" class="java.lang.String"/>

<%-- 
<jsp:useBean id="sedegiudiziaria"     scope="request" class="java.lang.String"/>
<jsp:useBean id="codicecomune"     scope="request" class="java.lang.String"/>
--%>

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
  
  // Per tutti i provvedimenti che lo gestiscono
  // aggiungere campo OBBLIGATORIO editabile CAMPO_CASELLARIO
  //       1. precaricato a '-' se lo stato nascita dell'imputato è blank
  //       2. altrimenti COD_UFFICIO dell'utente collegato
  // ad eccezione dei quattro provvedimento sotto elencati.
  // SOLO per questi 4 provvedimenti e se l'imputato è straniero (stato nascita diverso da ITALIA) --> casellario = ROMA
  //   -- Computo fungibilità
  //   -- Unificazione delle pene concorrenti
  //   -- Rideterminazione della pena
  //   -- Sospensione pena 656 
  SoggettoModel lSoggettoAssociato = lFascicoloAssociato.getSoggetto();
  
  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
   
  String lCasellario = lUfficioUtenteConnesso.getDescrComune();
  
  if( lSoggettoAssociato != null 
      && 
      	(    lSoggettoAssociato.getCodStatoNascita() == null
      	  ||  "".equals(lSoggettoAssociato.getCodStatoNascita()) 
      	  || "-".equals(lSoggettoAssociato.getCodStatoNascita())
      	 )
     )
  {
    lCasellario = "-";
  }
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Definizione Procedimento - Pena Espiata</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      function Verify()
      {
		  	//Il "Casellario Giudiziale" è obbligatorio
     		if (   document.f.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '-' 
     		    || document.f.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '' 
     		   )
        {
           alert("Il campo Casellario Giudiziale è obbligatorio!");

           return false;
        }		  	
		  	
        //DATA EMISSIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data emissione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();

          return false;
        }
        //DATA RICEZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data ricezione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.focus();

          return false;
        }
        //DATA DEFINIZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data definizione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();

          return false;
        }

        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value == '-')
        {
          alert("Il Campo Oggetto definizione è obbligatorio");
          return false;
        }

        if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }

        if(document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }

        return true;
      }

      function ListaMagistrati(a_formname)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }

      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }

     function ListaComuni(a_formname,a_fieldname)
      {
        var desktop;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
     // NGG
      function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
      {
      		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
 
   function tendina()
   {

       var nodeautorita =document.getElementById('divautorita');
       var nodeistituto = document.getElementById('divistituto');
       var nodeistitutoconoscenza = document.getElementById('divistitutoconoscenza');

       if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value =='0096' ||
           document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value =='0098' ||
           document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value =='0479')
       {
          document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.disabled=true;
          document.f.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.disabled=true;
          document.f.<%=ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE%>.disabled=true;
          document.f.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_CONOSCENZA.disabled=true;
          document.f.<%=ICostantiArchiviazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false;
       
          
          nodeistitutoconoscenza.style.display='none';               
          if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value =='0098')
          {
           nodeistitutoconoscenza.style.display='block';
           document.f.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_CONOSCENZA.disabled=false;
          }      
          nodeistituto.style.display='block';
          nodeautorita.style.display='none';
    
       }
       else
       {
          document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.disabled=false;
          document.f.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.disabled=false;
          document.f.<%=ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE%>.disabled=false;
          document.f.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_CONOSCENZA.disabled=true;
          document.f.<%=ICostantiArchiviazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true; 
               
          nodeistitutoconoscenza.style.display='none';  
                
          if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value =='0097')
          {
           nodeistitutoconoscenza.style.display='block';
           document.f.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_CONOSCENZA.disabled=false;
          } 
          nodeistituto.style.display='none';
          nodeautorita.style.display='block';
       }

   }     
    </script>
  </head>
  <body class="corpo" onload="tendina();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Definizione Procedimento - Pena Espiata</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActInserisciPenaEspiata">
<%-- 
    <input type="hidden" name="codicecomcas" value="<%=codicecomune%>">
--%>
    <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=8>
          <font class="campo">
            <%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
          </font>
        </td>
      </tr>
<%
    if( flagergastolo.equals("N") )
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
            <td class="l" colspan=2>
              <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
              <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
              <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
            </td>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          </tr>
<%
      }

      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}
      else
      {
%>
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
<%
      }
    }
%>

  </table>
  <br>
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Autorità emittente</td>
    </tr>
    <tr>
      <td class="l" width="25%">Numero Protocollo Nota</td>
      <td class="l" colspan="3">
         <input Title="Numero Nota" value="" name="<%=ICostantiArchiviazione.CAMPO_NUM_NOTA%>" type="text" size="35" maxlength="35">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Emissione</td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Emissione" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione" value="<%=DateUtils.getSysDate("MM")%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Ricezione</td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Ricezione" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Ricezione" value="<%=DateUtils.getSysDate("MM")%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
</table>
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Dati Definizione Procedimento</td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Definizione <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Oggetto Definizione <font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Oggetto Definzione" class="small" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" onchange="tendina();">
            <%=oggettodefinzione%>
          </select>
        </td>
    </tr>
  </table>
<div id="divIstituto" style="width: 100%; display:none; position:relative; ">
<table width="100%">  
 <tr>
     <td class="l" width="25%">Istituto di Detenzione che ha inviato la nota</td>
  <td class="l">
<%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
 {%>



              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiArchiviazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiArchiviazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>

<%}else {%>


              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiArchiviazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiArchiviazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>

       <%}%>
</td>
</tr>
</table>
 </div>
  <div id="divAutorita" style="width: 100%; display:none; position:relative; ">
   <table width="100%">  
    <tr>
<!--autorità di polizia-->
      <td class="l" width="25%">Autorità che ha inviato la nota</td>
      <td class="L" colspan="3">
        <select  Title="Autorita"  class="small" name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
         <%=codiceAutorita%>
         </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
     <td class="L">
          <input title="Sede Autorita"  type="text" name="<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
           <td class="l">Indirizzo</td>
           <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE%>"  cols=30 ></textarea>
            </td>
    </tr>
   </table>
  </div>  
  <table width="100%">
<%if(penaresidua != null)
{%>
      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l" width="25%">Pena Espiata dal</td>
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
    if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">al</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
            </td>
<%
          }
          else
          {
%>
                <td class="l">al</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                </td>
<%            }
        }
      }
	}
%>
   </tr>
<%
}
%>
  </table>
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Magistrato Firmatario</td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario</td>
      <td class="L" colspan="3">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('f');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
       <td>
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
       </td>
    </tr>
    <tr>
      <td colspan=4 class="titolo">Destinatari</td>
    </tr>
    <tr>
      <td class="l" width="25%">Casellario Giudiziale</td>
     	<td class="l" id="inputCasellario">
       	<input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
       	<a href="Javascript:ListaUfficiPerTipo('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>','DIB');">
       	   	<img src="/images/filefolder.gif" border=0>
       	</a>
     	</td>
    </tr>
 </table>
<div id="divistitutoconoscenza" style="display:none; position:relative; width:100%;">
<table width="100%">  
 <tr>
     <td class="l" width="25%">Istituto di Detenzione (per conoscenza)</td>
  <td class="l">
<%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
 {%>
              <input readonly Title="Istituto" name="Comune_CONOSCENZA" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_CONOSCENZA" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>_CONOSCENZA','Comune_CONOSCENZA');">
              <img src="/images/filefolder.gif" border=0></a>
<%}else {%>
              <input readonly Title="Istituto" name="Comune_CONOSCENZA" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>_CONOSCENZA" value="" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>_CONOSCENZA','Comune_CONOSCENZA');">
              <img src="/images/filefolder.gif" border=0></a>
       <%}%>
</td>
</tr>
</table>
</div> 
<table width="100%">   
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");


//data emissione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","maxlen=4","La lunghezza massima per l'Anno emissione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza minima per l'Anno emissione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");

//data ricezione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","maxlen=4","La lunghezza massima per l'Anno ricezione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","minlen=4","La lunghezza minima per l'Anno ricezione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","numeric");

//data definizione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","maxlen=4","La lunghezza massima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","minlen=4","La lunghezza minima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>