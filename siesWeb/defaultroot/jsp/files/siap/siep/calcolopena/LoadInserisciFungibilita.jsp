<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>

<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>

<jsp:useBean id="annotazioneManuale"   scope="request" class="java.util.Vector"/>
<jsp:useBean id="annotazioneManualeGE" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="flagPage"             scope="request" class="java.lang.String" />
<jsp:useBean id="posizioneGiuridica"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="evento"               scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="evento06"             scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="avvocati"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="lAutorita"            scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaAvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaC"     scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="codiceAutoritaC"      scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceUffici"         scope="request" class="java.lang.String"/>

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
      	( 
      	   !"039".equals(lSoggettoAssociato.getCodStatoNascita())
      	 )
     )
  {
    lCasellario = "ROMA";
  }

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
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

<script language="JavaScript">
<%
  String notMagistrato = null;
  String notAutorita = null;
  //FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  String Posizione = posizioneGiuridica.getCodPosizioneGiuridica();
%>
  //============================================================================
  //
  //============================================================================
  function Verify()
  {
  	//Se il "Foglio Complementare" è selezionato, il "Casellario Giudiziale" è obbligatorio
  	if( document.f.<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>.checked == true )
  	{
     		if (   document.f.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '-' 
     		    || document.f.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '' 
     		   )
        {
          alert("Il campo Casellario Giudiziale è obbligatorio!");

          return false;
        }		  	
  	} 
		  	
    if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
      document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
      document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

    var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) )
    {
      alert('Data di Emissione non valida');
      return false;
    }

    if (document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value.length==1)
      document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
    if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length==1)
      document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;

    var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;

    if (!ControllaData(data_to_verify) )
    {
      alert('Data di Trasmissione non valida');
      return false;
    }

    if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
    {
      alert("Il  Magistrato Assegnatario è obbligatorio");
      document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
      return false;
    }

    <%if(((Posizione.equals("07") || Posizione.equals("10") )  && (evento06 != null  && evento06.getIdEvento()!= null)) || (!Posizione.equals("07") && !Posizione.equals("10"))){%>
    if(document.f.<%= ICostantiAnnotazioneManuale.AUTORITA_ESTERNA %>.value=="-")
    {
      alert("L' Autorità di polizia competente per territorio è obbligatorio");
      document.f.<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA %>.focus();
      return false;
    }
    <%}%>
    
    <%
    //if(!Posizione.equals("07") && !Posizione.equals("10") || (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) )
    if(!posizioneGiuridica.isLibero())
    {
    %>
    if(document.f.<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>.value=="")
    {
      alert("L' Istituto di detenzione è obbligatorio");
      return false;
    }
    <% } %>
    
    <%if(Posizione.equals("07") || Posizione.equals("10") ) {%>
    if(document.f.<%= ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_NC %>.value=="-")
    {
      alert("Gli Ufficiali Giudiziari per notifica al condannato sono obbligatori");
      document.f.<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_NC%>.focus();
      return false;
    }
    <%}%>
    
    if(document.f.<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_ND%>.value=="-")
    {
      alert("Gli Ufficiali Giudiziari per notifica al difensore sono obbligatori");
      document.f.<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_ND%>.focus();
      return false;
    }
  }
  
  
  //============================================================================
  //
  //============================================================================
  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
  {
    var desktop;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }
  
  //============================================================================
  //
  //============================================================================
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }
  
  //============================================================================
  //
  //============================================================================
  function ListaComuni(a_formname, a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function toggleComuneCasellario()
	{
		vistaLabel = (document.getElementById("labelCasellario").style.display == 'none') ? 'block' : 'none';
		document.getElementById("labelCasellario").style.display = vistaLabel;

		//vistaInput = (document.getElementById("inputCasellario").style.display == 'none') ? 'block' : 'none';
		//document.getElementById("inputCasellario").style.display = vistaInput;
	}
</script>


    <title>[S.I.E.S.] - Decreto Computo C.C. delle pene espiate senza titolo Fungibilità  art. 657 c.p.p.</title>
</head>


<body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
          <font  class="label">Funzione :&nbsp;</font>
          <font class="campo">Decreto Computo C.C. delle pene espiate senza titolo Fungibilità art. 657 c.p.p. </font>
        </td>
        <td class="LBG">
<!--
     Torna alla pagina "Provvedimenti e Stampe per Rideterminazione Pena"
     per eliminare history.go(-1);
     predisporre un azione di Dettaglio per richiamare la pagina
     che al momento manca 02/04/2004  DL
-->
          <a href="Javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
    
<form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciFungibilita">
  <input type="HIDDEN" name="flagPage" value="<%=flagPage%>">
  <input type="HIDDEN" name="fungibilita" value="S">
  
<% 
		if( evento06.getIdEvento() != null ) 
		{
%>
    	<input type="HIDDEN" name="evento06" value="S">
<% 
		} 
%>

  <table>
    <tr>
      <td class="l">Posizione Giuridica :</td>
      <td class="l">
        <font class="campo">
<% 
					if(lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) 
					{ 
%>
          	DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
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
      <input type="HIDDEN" name="codPosizioneGiu" value="<%=posizioneGiuridica.getCodPosizioneGiuridica()%>">
    </tr>

 		<tr>
    	<td class="l">
    		<input type="checkbox" onClick="toggleComuneCasellario()" name="<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>" checked>
     		Foglio Complementare
     	</td>
     	<td class="l" id="labelCasellario" style="display:block;">Casellario Giudiziale&nbsp;
<!--      	
     	</td>
     	<td class="l" id="inputCasellario" style="display:block;">
 -->
       	<input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
       	<a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>');">
         	<img src="/images/filefolder.gif" border=0>
       	</a>
     	</td>
    </tr>
    
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=evento.getIdEvento()%>">
    <%
    //==========================================================================
    // 
    //==========================================================================
    //if(annotazioneManuale.size()>0)
    if(annotazioneManualeGE.getIdAnnotazioneManuale() != null)
    {
      //AnnotazioneManualeModel lAnnPrimo =  (AnnotazioneManualeModel)annotazioneManuale.get(0);
      AnnotazioneManualeModel lAnnPrimo = annotazioneManualeGE;
%>
        <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=StringUtils.toStringJSP(lAnnPrimo.getIdAnnotazioneManuale())%>">
        <tr>
<%
        if(lAnnPrimo.getAnnoGe() != null || lAnnPrimo.getNumeroGe()!= null)
        {
%>
          <td class="L">Ordinanza GE: </td>
          <td class="L">
            <font class="campo">
              <%=StringUtils.toStringJSP(lAnnPrimo.getAnnoGe())%> / <%=StringUtils.toStringJSP(lAnnPrimo.getNumeroGe())%>
            </font>
          </td>
<%
        }
%>
        </tr>
<%
        if(lAnnPrimo.getDataGE() != null)
        {
%>
          <tr>
            <td class="l">Data Ordinanza GE: </td>
            <td class="L">
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnPrimo.getDataGE(),"dd-MM-yyyy"))%>
              </font>
            </td>
          </tr>
<%
        }

        if(lAnnPrimo.getMotivazioni() != null)
        {
%>
          <tr>
            <td class="l">Motivazioni  </td>
            <td class="campo">
              <%=StringUtils.toStringJSP(lAnnPrimo.getMotivazioni())%>
            </td>
          </tr>
<%
        }
%>
    </table>
    
    
<table>
<%
  int lIdx= 0;
  Iterator lItx = annotazioneManuale.iterator();
  while(lItx.hasNext())
  {
    AnnotazioneManualeModel lAnn =  (AnnotazioneManualeModel)lItx.next();
    
    //=========================
    // Reclusione
    //=========================
    if(lAnn.getDataReclusioneDa() != null){%>
    <tr>
        <td class="Titolo" colspan="5" width="100%">Computo custodia cautelare reclusione</td>
    </tr>

    <tr>
      <td class="l">Dalla data  </td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataReclusioneDa(),"dd-MM-yyyy"))%></font>
      </td>
      <td class="L">Alla data  </td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataReclusioneA(),"dd-MM-yyyy"))%></font>
      </td>
      
      <td class="L">
        Anni
        <font class="campo"><%=StringUtils.toStringJSP(lAnn.getNumAnniReclusione())%></font>
        Mesi
        <font class="campo"><%=StringUtils.toStringJSP(lAnn.getNumMesiReclusione())%></font>
        Giorni
        <font class="campo"> <%=StringUtils.toStringJSP(lAnn.getNumGiorniReclusione())%></font>
      </td>
    </tr>
    <%}%>
    
    <%
    //=========================
    // Arresti
    //=========================
    if(lAnn.getDataArrestoDa() != null){%>
    <tr>
      <td class="Titolo">Computo custodia cautelare Arresto</td>
    </tr>
    <tr>
      <td class="l">Dalla data  </td>
      <td class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataArrestoDa(),"dd-MM-yyyy"))%>
      </td>
      <td class="l">Alla data  </td>
      <td class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataArrestoA(),"dd-MM-yyyy"))%>
      </td>
    </tr>
    <tr>
      <td class="l">
        Anni
        <font class="campo"><%=StringUtils.toStringJSP(lAnn.getNumAnniArresto())%></font>
        Mesi
        <font class="campo"><%=StringUtils.toStringJSP(lAnn.getNumMesiArresto())%></font>
        Giorni
        <font class="campo"> <%=StringUtils.toStringJSP(lAnn.getNumGiorniArresto())%></font>
      </td>
    </tr>
    <%}%>
<%
    lIdx++;
  }
}
%>
</table>

<%
//==============================================================================
//                      SEZIONE CON LA PENA RIDETERMINATA
//==============================================================================
%>
<table style="width: 95%;" >
<%
  if(penaresidua != null)
  {
%>
    <tr>
      <td class="Titolo" colspan="2">Pena rideterminata</td>
    </tr>
    <tr>
      <td class="l">Reclusione</td>
      <td class="L">Anni
        <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione())%></font>
        Mesi
        <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione())%></font>
        Giorni
        <font class="campo"> <%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione())%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Arresto</td>
      <td class="L">Anni
        <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto())%></font>
        Mesi
        <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto())%></font>
        Giorni
        <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto())%></font>
      </td>
    </tr>
<%
  }
%>
<table style="width: 95%;">
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input value="<%=DateUtils.getSysDate("dd")%>" title="Giorno Emissione" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("MM")%>" title="Mese Emissione" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("yyyy")%>" title="Anno Emissione" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <td class="l">Data Trasmissione</td>
    <td class="L">
      <input value="<%=DateUtils.getSysDate("dd")%>" title="Anno Trasmissione" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getSysDate("MM")%>" title="Mese Trasmissione" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("yyyy")%>" title="Giorno Trasmissione" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
</table>
</table>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" width="100%" colspan=6> Magistrato Assegnatario </td>
    </tr>
    <tr>
      <td class="l" width="20%">Magistrato
      <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
        <input type="hidden" name="notMagistrato" value="<%=notMagistrato%>">
      </td>
      <td>
      </td>
    </tr>
  </table>
<table style="width: 95%;">
<%
//==============================================================================
//             S E Z I O N E    C O N     I    D E S T I N A T A R I
//==============================================================================
// Se libero con OE o non libero:
// - Autorità giudiziaria competente per l'esecuzione
  if(   (    (Posizione.equals("07") || Posizione.equals("10") )  
          && (evento06 != null  && evento06.getIdEvento()!= null)
        ) 
     || (!Posizione.equals("07") && !Posizione.equals("10") )
    )
  {
%>
    <tr>
      <td class="Titolo" colspan=6>Autorità giudiziaria</td>
    </tr>
    <tr>
      <td class="l" width="20%">Destinatario</td>
      <td class="L">
        <input type="hidden" name="notAutorita" value="<%=notAutorita%>">
        <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA%>">
          <%=codiceUffici%>
        </select>
      </td>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%= ICostantiAnnotazioneManuale.NOTE_AUTORITA_ESTERNA %>"  cols=20 rows=5 ></textarea>
        <input type="hidden" name="notificaPolizia" value="C">
      </td>
    </tr>
    <tr>
      <td class="l">Sede<font class=ob>(*)</font> </td>
      <td class="L">
<%
      if(autoritaEsternaC.getDescrSede()!= null)
      {
%>
        <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lAutorita.getDescrSede()) %>" type="text" name="<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA %>"  maxlength="35" size="35">
<%
      }
      else
      {
%>
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA %>"  maxlength="35" size="35">
<%
      }
%>
        <a href="Javascript:ListaComuni('f','<%=ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
<%
  }

//==============================================================================
// Se Libero senza OE
// - Autorità Giudiziaria competente per l'Esecuzione
//==============================================================================
  if((Posizione.equals("07") || Posizione.equals("10") ) && (evento06.getIdEvento() == null ) )
  {
%>
    <tr>
      <td class="Titolo" colspan=6>Autorità giudiziaria </td>
    </tr>
    <tr>
      <td class="l" width="20%">Destinatario  </td>
      <td class="L">
        <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_E%>">
          <%=codiceUffici%>
        </select>
      </td>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%= ICostantiAnnotazioneManuale.NOTE_AUTORITA_ESTERNA_E %>"  cols=20 rows=5 ></textarea>
        <input type="hidden" name="notificaPolizia" value="C">
      </td>
    </tr>
    <tr>
      <td class="l">Sede<font class=ob>(*)</font> </td>
      <td class="L">
<%
      if(autoritaEsternaC.getDescrSede()!= null)
      {
%>
        <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lAutorita.getDescrSede()) %>" type="text" name="<%=  ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_E %>"  maxlength="35" size="35">
<%
      }
      else
      {
%>
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_E %>"  maxlength="35" size="35">
<%
      }
%>
        <a href="Javascript:ListaComuni('f','<%=ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_E%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
<%
  }

//==============================================================================
// Libero con OE
//==============================================================================
  if((Posizione.equals("07") || Posizione.equals("10") ) && (evento06 != null && evento06.getIdEvento() != null))
  {
%>
    <tr>
      <td class="Titolo" colspan=6>Autorità di polizia per la restituzione dell'ordine di esecuzione</td>
    </tr>
    <tr>
      <td class="l" width="20%">Destinatario  </td>
      <td class="L">
        <input type="hidden" name="notAutorita" value="<%=notAutorita%>">
        <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_E%>">
          <%=codiceAutoritaC%>
        </select>
      </td>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%= ICostantiAnnotazioneManuale.NOTE_AUTORITA_ESTERNA_E %>"  cols=20 rows=5 ></textarea>
          <input type="hidden" name="notificaPolizia" value="C">
        </td>
      </tr>
    <tr>
      <td class="l">Sede<font class=ob>(*)</font> </td>
      <td class="L">
<%
        if(autoritaEsternaC.getDescrSede()!= null)
        {
%>
          <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lAutorita.getDescrSede()) %>" type="text" name="<%= ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_E%>"  maxlength="35" size="35">
<%
        }
        else
        {
%>
          <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_E %>"  maxlength="35" size="35">
<%
        }
%>
        <a href="Javascript:ListaComuni('f','<%=ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_E%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
<%
  }

//==============================================================================
// Diverso da Libero (e assimilati)
// - Istituto di detenzione
//==============================================================================
  if(!posizioneGiuridica.isLibero())
  {
%>
    <tr>
      <td class="Titolo" colspan=6>Istituto / luogo della detenzione per la notifica al condannato e le annotazioni in matricola </td>
    </tr>
    <tr>
      <td class="l" width="20%">Istituto di Detenzione</td>
<%
      if(!Posizione.equals("07") && !Posizione.equals("10") || (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) )
      {
%>
        <td class="l">
          <input readonly Title="Istituto" name="Comune" value="" size=50>
          <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
          <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0></a>
          <input type="HIDDEN" name="istituto" value="E">
        </td>
<%
      }
      else
      {
%>
        <td class="l">
          <input readonly Title="Istituto" name="Comune" value="" size=50>
          <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
          <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0></a>
          <input type="HIDDEN" name="istituto" value="C">
        </td>
<%
      }
%>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%= ICostantiLuogoDetenzione.CAMPO_NOTE%>"  cols=20 rows=5 ></textarea>
      </td>
    </tr>
<%
  }

//==============================================================================
// Se Libero
//==============================================================================
    if(Posizione.equals("07") || Posizione.equals("10") )
    {
%>
      <tr>
        <td class="Titolo" colspan=6>Ufficiali Giudiziari per notifica al condannato</td>
      </tr>
      <tr>
        <td class="l" width="20%">Destinatario  </td>
        <td class="L">
          <input type="hidden" name="notAutorita" value="<%=notAutorita%>">
          <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_NC%>">
            <%=codiceAutoritaC%>
          </select>
        </td>
        <td rowspan=2 class="l">Note</td>
        <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%= ICostantiAnnotazioneManuale.NOTE_AUTORITA_ESTERNA_NC %>"  cols=20 rows=5 ></textarea>
          <input type="hidden" name="notificaPolizia" value="C">
        </td>
        </tr>
    <tr>
      <td class="l">Sede<font class=ob>(*)</font> </td>
     <td class="L">
<%
      if(autoritaEsternaC.getDescrSede()!= null)
      {
%>
        <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lAutorita.getDescrSede()) %>" type="text" name="<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_NC %>"  maxlength="35" size="35">
<%
      }
      else
      {
%>
        <input title="Sede Autorita Esterna" value="" type="text" name="<%=ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_NC %>"  maxlength="35" size="35">
<%
      }
%>
        <a href="Javascript:ListaComuni('f','<%=ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_NC%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
<%
  }
%>

<%
//==============================================================================
//  S E Z I O N E    C O N    L A    N O T I F I C A   A G L I   A V V O C A T I
//==============================================================================
%>
    <tr>
      <td class="Titolo" colspan=6>Ufficiali Giudiziari per notifica al difensore</td>
    </tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>
        <table style="width: 95%;">
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
        <table style="width: 95%;">
          <tr><td class="l">Autorità Destinazione </td >
          <td class="L">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_ND%>" >
               <%=autoritaEsternaAvv%>
             </select>
         </td>
        <td rowspan=2 class="l">Note</td>
       <td rowspan=2 class="L">
          <textarea title="Note" name="<%=ICostantiAnnotazioneManuale.NOTE_AVVOCATI%>"  cols=20 rows=5 ></textarea>
       </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_ND%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('f','<%=ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_ND %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

<%
    lIdxAvv++;
  }
%>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Giorno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","req","Il campo Mese Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Anno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2050");

<%
  if(((Posizione.equals("07") || Posizione.equals("10") )  && (evento06 != null  && evento06.getIdEvento()!= null)) || (!Posizione.equals("07") && !Posizione.equals("10")))
  {
%>
    frmvalidator.addValidation("<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA  %>","req","Luogo Autorità di polizia competente per territorio obbligatoria");
    frmvalidator.addValidation("<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA %>","alphabetic");
<%
  }

  if(Posizione.equals("07") || Posizione.equals("10") )
  {
%>
    frmvalidator.addValidation("<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_NC  %>","req","Luogo Ufficiali Giudiziari per notifica al condannato obbligatoria");
    frmvalidator.addValidation("<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_NC %>","alphabetic");
<%
  }
%>

  frmvalidator.addValidation("<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_ND  %>","req","Luogo Ufficiali Giudiziari per notifica al difensore obbligatoria");
  frmvalidator.addValidation("<%= ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_ND %>","alphabetic");

</script>
</body>
</html>