<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="oggettodefinzione" scope="request" class="java.lang.String"/>

<%--
<jsp:useBean id="codicecomunecasellario" scope="request" class="java.lang.String"/>
--%>

<jsp:useBean id="uffrecrediti"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutorita"   scope="request" class="java.lang.String"/>

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
    <title>[S.I.E.S.] - Definizione Procedimento - Non luogo a provvedere</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      var desktop;

      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    
      function ListaComuniUff(a_formname,a_fieldname,codTipoUfficio)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
  
      function ListaComuni(a_formname,a_fieldname)
      {
       desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      // NGG
      function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
      {
      		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

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
		  	
        //DATA DEFINIZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data definizione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();

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

      function gestisciDest()
      {
        var valueSel = document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.options.value;
        var nodeDest = document.getElementById('divDestinatari');

        if(valueSel == '0006' || valueSel == '0009' || valueSel == '0478')
        {
          nodeDest.style.display='block';
          document.f.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
          document.f.UfficioRecuperCrediti.disabled = false;
          document.f.SedeUfficioRecuperCrediti.disabled = false;
          document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled = false;
          document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled = false;
          document.f.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled = false;
        }
        else
        {
          nodeDest.style.display='none';
          document.f.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
          document.f.UfficioRecuperCrediti.disabled = true;
          document.f.SedeUfficioRecuperCrediti.disabled = true;
          document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled = true;
          document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled = true;
          document.f.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled = true;
        }
      }
    </script>
  </head>
  <body class="corpo" onLoad="gestisciDest()">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Definizione Procedimento - Non Luogo A Provvedere</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActInserisciNonLuogoAProvvedere">
<%-- 
    <input type="hidden"  Title="Casellario" name="codicecomunecasellario" value="<%=codicecomunecasellario%>">
--%>

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

    if( lPosizione.getCodPosizioneGiuridica() != null
      && (!lPosizione.getCodPosizioneGiuridica().equals("07")
      && !lPosizione.getCodPosizioneGiuridica().equals("10")) )
    {
%>
        <tr>
<%
          if(   flagergastolo.equals("N")
             && penaresidua.getDataInizio() != null )
          {
%>
            <td class="l">Data Decorrenza Pena</td>
            <td class="L">
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
              </font>
            </td>
<%
          }

/*
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S"))) && penaresidua.getDataFinePresunta() != null)
        {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
	<td class="l">Data Fine Pena Automatica</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
--%>
<%
/*
       }
*/
         if (  flagergastolo.equals("N")
            && penaresidua.getDataFine()!=null
            )
         {
           String lClassTd="l";
           String lClassFont="campo";
           if(penaresidua.getDataFine() != null
             && !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
           {
             lClassTd="lRosso";
             lClassFont="lRosso";
           }
%>
             <td class="l">Data Fine Pena</td>
             <td class="<%=lClassTd%>">
               <font class="<%=lClassFont%>">
                 <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy") )%>&nbsp;
               </font>
             </td>
<%
         }
    }
%>
    </tr>
<%
  if (flagergastolo.equals("S"))
  {
%>
    <tr>
      <td class="l">Pena Complessiva</td>
      <td class="L">
        <font class="campo">ERGASTOLO&nbsp;</font>
      </td>
    </tr>
<%
  }
  else if(flagergastolo.equals("D"))
  {
%>
    <tr>
      <td class="l">Pena Complessiva</td>
      <td class="L">
        <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font>
      </td>
    </tr>
<%
   }
%>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Data Trasmissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
  <br>
  <table width="100%">
    <tr>
      <td colspan=3 class="titolo">Dati Definizione Procedimento</td>
    </tr>
    <tr>
      <td class="l" width="30%">
        Data Definizione <font class="ob">(*)</font>
      </td>
      <td class="l">
        <input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="30%">Oggetto Definizione <font class="ob">(*)</font></td>
      <td class="l">
        <select Title="Oggetto Definzione" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" onChange="gestisciDest()">
          <%=oggettodefinzione%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">
        Note
      </td>
      <td class="l">
        <textarea cols="60" rows="2" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"></textarea>
      </td>
    </tr>
    <tr>
      <td colspan=3 class="titolo">Magistrato firmatario</td>
    </tr>
    <tr>
      <td class="l" width="30%">Magistrato Firmatario</td>
      <td class="L">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('f');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
       <td>
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
       </td>
    </tr>
    <tr>
      <td colspan="4" class="titolo">Destinatari</td>
    </tr>
    <tr>
      <td class="l" width="30%">Casellario Giudiziale</td>
     	<td class="l" id="inputCasellario">
       	<input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
       	<a href="Javascript:ListaUfficiPerTipo('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>','DIB');">
         	<img src="/images/filefolder.gif" border=0>
       	</a>
     	</td>
    </tr>
  </table>
  <div id="divDestinatari" style="display:none; position:relative; ">
  <table width="100%">
    <tr>
      <td class="l" width="30%">Istituto di Detenzione</td>
      <td class="l" colspan="3">
<%
      if(lLuogoDetenzione.getIstitutoDetenzione() == null)
      {
%>
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0>
        </a>
<%
      }
      else
      {
%>
        <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0>
        </a>
<%
      }
%>
      </td>
    </tr>
   <tr>
      <td class="l" width="30%">Ufficio Recupero Crediti presso</td>
      <td class="L">
        <select  Title="Ufficio Recupero Crediti" name="UfficioRecuperCrediti">
          <%=uffrecrediti%>
         </select>
      </td>
     <td class="l">di</td>
     <td class="L">
          <input title="Sede Ufficio recupero crediti"  type="text" name="SedeUfficioRecuperCrediti"  maxlength="35" size="25">
           <a href="Javascript:ListaComuniUff('f','SedeUfficioRecuperCrediti',document.f.UfficioRecuperCrediti[document.f.UfficioRecuperCrediti.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
   </tr>   
    <tr>
      <td class="l" width='30%'>Altra Autorità</td>
      <td class="L" colspan="3">
         <select  Title="Altra Autorità"  name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
            <%=codiceAutorita%>
         </select>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
      <input title="Sede Autorità"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
          <TEXTAREA title="Indirizzo" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=30 ></textarea>
      </td>
   </tr>    
  </table>
  </div>
  <table>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
    //DATA EMISSIONE
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

    // DATA RIPRISTINO ESECUZIONE
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE %>","req","Il campo Giorno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE %>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE %>","req","Il campo Mese definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE %>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE %>","req","Il campo Anno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE %>","maxlen=4","La lunghezza massima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE %>","minlen=4","La lunghezza minima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE %>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>