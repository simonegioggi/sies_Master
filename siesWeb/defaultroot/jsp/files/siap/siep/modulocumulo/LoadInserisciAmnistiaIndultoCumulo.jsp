<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="aProvvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aIdComputo" scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoUfficioEmittente" scope="request" class="java.lang.String"/>

<jsp:useBean id="reati"         scope="request" class="java.util.Vector" /> <% // Reati sul fascicolo %>

<jsp:useBean id="listaDPR"               scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoAnnotazioneManuale" scope="request" class="java.lang.String" />

<% 
//============================================================================== 
// Form per l'inserimento e la modifica dei Provvedimenti di annotazione Amnistia
// Indulto
//============================================================================== 
ComputiCumuloModel aComputo = new ComputiCumuloModel();
Vector <ComputiCumuloModel> lListaComputi = aProvvedimento.getListaComputi();
 if ( modalita.equals("M") )
{
  Iterator itxComputi = lListaComputi.iterator();
  while ( itxComputi.hasNext()) 
  {
    ComputiCumuloModel lComputo = (ComputiCumuloModel) itxComputi.next();
    BigDecimal lIdCompDaModificare = new BigDecimal (aIdComputo);
    if (lComputo.getIdComputiCumulo().compareTo(lIdCompDaModificare)==0){
      aComputo = lComputo;
      break;
    }
  }
} 


int maxNumComputi = 4;

%> 

<html>
<head>
  <title> Gestione Provvedimento Amnistia Indulto </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >

  	var lMaxNumComputi = <%=maxNumComputi%>;
  
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.f.submit();
    }

    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    var data_to_verify;
    var obbligatori;
    var dataesiste;

    //============================================================================
    // funzione per la verifica dei dati imputati in maschera
    //============================================================================
    function Verify()
    {
      //==========================================================================
      // Controlli su dati dell'ordinanza
      //==========================================================================
      if (document.f.<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>[document.f.<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.selectedIndex].value=="-")
      {
        alert("Selezionare computo beneficio");
        return false;
      }

      if (document.f.<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>[document.f.<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>.selectedIndex].value=="-")
      {
        alert("Selezionare DPR");
        return false;
      }

      if (document.f.DaGiArr.value.length>0 || document.f.DaMeArr.value.length>0 || document.f.DaAnArr.value.length>0)
      {
          if (document.f.DaGiArr.value.length<2)
            document.f.DaGiArr.value="0"+document.f.DaGiArr.value;
          if (document.f.DaMeArr.value.length<2)
            document.f.DaMeArr.value="0"+document.f.DaMeArr.value;
    
          data_to_verify = document.f.DaGiArr.value +"/"+document.f.DaMeArr.value+"/"+document.f.DaAnArr.value;
    
          if (! ControllaData(data_to_verify))
          {
            alert('Data di emissione ordinanza non valida');
            return false;
          }

          // La Data di emissione ordinanza deve essere <= SYSDATE
          var sysDate = new Date();
    
          var ggSysDate = sysDate.getDate();
          if(ggSysDate<10)
            ggSysDate = "0"+ggSysDate;
    
          var mmSysDate = (sysDate.getMonth()+1);

          if(mmSysDate<10)
            mmSysDate = "0"+mmSysDate;
    
          var yyyySysDate = sysDate.getYear();
          var strSysDate = ggSysDate + "/" + mmSysDate + "/" + yyyySysDate;

          if (!CompareDate(data_to_verify, strSysDate))
          {
            alert('Data di emissione ordinanza superiore alla data attuale');
            return false;
          }
      
      }  <% // Chiude if  %>

	  /**
	   * CAMPI OBLIGATORI
	   */
      var AAGE     = document.f.DaAnArr.value;
      var MMGE     = document.f.DaMeArr.value;
      var GGGE     = document.f.DaGiArr.value;
      var codTipoUffEmi = document.f.CodTipoUffEmi.value;
      var luogoUffEmi   = document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value;
      
      var AnnoSige  = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>.value;
      var ProgrSige = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>.value;

      if(AnnoSige == '' && ProgrSige == ''
      	  && AAGE == '' && MMGE == '' && GGGE == ''
          && codTipoUffEmi == '-' && luogoUffEmi == '' )
      {
			alert("Dati Declaratoria Obbligatori");
          	document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>.focus();
  
          	return false;
      }
      
      if(AnnoSige == '')
      {
          alert("Dati Declaratoria Incompleti - Indicare Anno Procedimento SIGE");
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>.focus();
  
          return false;
      }
      
      if(ProgrSige == '')
      {
          alert("Dati Declaratoria Incompleti - Indicare Numero Procedimento SIGE");
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>.focus();
  
          return false;
      }
      if(GGGE == '')
      {
          alert("Dati Declaratoria Incompleti. Indicare il giorno data Emissione Ordinanza.");
          document.f.DaGiArr.focus();      
          return false;
      }
      if(MMGE == '')
      {
          alert("Dati Declaratoria Incompleti. Indicare il mese della data Emissione Ordinanza.");
          document.f.DaMeArr.focus();      
          return false;
      }
      if(AAGE == '')
      {
          alert("Dati Declaratoria Incompleti. Indicare l'anno della data Emissione Ordinanza.");
          document.f.DaAnArr.focus();      
          return false;
      }

      if(codTipoUffEmi == '-')
      {
          alert("Dati Declaratoria Incompleti. Tipo Ufficio Emittente obbligatorio");
          document.f.CodTipoUffEmi.focus();
          return false;
      }
      if(luogoUffEmi == '')
	  {
          alert("Dati Declaratoria Incompleti. Sede Ufficio Emittente obbligatoria.");
          document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
          return false;
	  }

      //==========================================================================
      // I quantum sono obbligatori  
      //==========================================================================
      
      // Flag +/- obbligatorio
      if (document.f.PM[document.f.PM.selectedIndex].value == "")
      {
        alert ("Selezionare tra + e -");
        return false;
      }
        
      obbligatori = (document.f.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.value!="" || document.f.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.value!="" || document.f.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.value!="");
      obbligatori = obbligatori || (document.f.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA%>INT.value!="");
      obbligatori = obbligatori || (document.f.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA%>DEC.value!="");
      obbligatori = obbligatori || (document.f.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_ARRESTO%>.value!="" || document.f.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_ARRESTO%>.value!="" || document.f.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_ARRESTO%>.value!="");
      obbligatori = obbligatori || (document.f.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA%>INT.value!="");
      obbligatori = obbligatori || (document.f.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA%>DEC.value!="");
  
      if (!obbligatori)
      {
        alert ("Selezionare la durata del periodo per arresto o reclusione oppure la sanzione");  
        return false;
      }
    
      
      return true;
            
    } <%  // end function verify() %>

    
</script>

</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if( modalita.equals("I") ) { %>
        <font class="campo">Inserimento annotazione Amnistia/Indulto &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica annotazione Amnistia/Indulto &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaAmnistiaIndultoCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciAmnistiaIndultoCumulo">
  <input type="hidden" name="maxNumComputi" value="<%=maxNumComputi%>" >
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiComputiCumulo.CAMPO_FLAG_STATO %>"                       		  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">

  <br>

<%
//==============================================================================
//       Sezione contenente tutti i reati cumulati
//==============================================================================
if (!reati.isEmpty())
{
%>
<table cellspacing="2" cellpadding="2" width="95%" align="center">
  <tr><td colspan=7 class="Titolonocap">Titoli di reato</td></tr>
  <tr>
    <td class="c">Reato</td>
    <td class="c">Durata</td>
    <td class="c">Sanzione</td>
    <td class="c">Sel.</td>
    <td class="c">Ann.Inserita</td>
  </tr>
<%
  ReatoCumuloModel lReato;
  boolean lFlagAnnoNumero;
  for (int i=0;i<reati.size();i++)
  {
   lReato=(ReatoCumuloModel)reati.get(i);
   if (lReato.getProgrCircostanza().equals(new BigDecimal("1") )) {
    
    lFlagAnnoNumero = false;
    
    if(  lReato.getAnnoFonte() != null
       && !lReato.getAnnoFonte().equals("")
       && lReato.getNumeroFonte() != null
       && !lReato.getNumeroFonte().equals("") )
    {
      lFlagAnnoNumero = true;
    }
%>
  <tr>
    <td class="l">
      <font class="label">
<%
            if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
            {
%>
              <font class="campoNoCap">
<%
                out.println("N." + lReato.getProgrNumeroManuale()+": ");
%>
              </font>
<%
            }
            else
            {
              out.println("N." + lReato.getProgrReato()+": ");
            }
%>
          </font>
          <font class="L">
<%
        if(lFlagAnnoNumero)
        {
          if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
            out.println(lReato.getDescrFonte()+" ");
          if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
            out.println(lReato.getAnnoFonte());
          if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
            out.println("/"+lReato.getNumeroFonte());
        }

        if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
          out.println("art."+lReato.getArticolo());
        if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
          out.println(" "+lReato.getDescrSottonumerazione());

        if(!lFlagAnnoNumero)
        {
          if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
            out.println(lReato.getDescrFonte());
        }

        if(lReato.getComma() != null && !lReato.getComma().equals(""))
          out.println(" c. "+lReato.getComma());
        if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
          out.println(" l. "+lReato.getLettera());
        if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
          out.println(" n. "+lReato.getNumero());%>
      </font>
      
      <% if(lReato.getStringaConsumazione()!= null) { %>
      <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
      <% } %>
    
      <%if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
      <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
      <% } %>

      <% if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
      <font class="label">Luogo</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
      <% } %>
    </td>

      <td class="l">
        <table>
          <td class="lnobord"><font class="label">AA</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumAnni(),"0")%></font></td>
          <td class="lnobord"><font class="label">MM</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumMesi(),"0")%></font></td>
          <td class="lnobord"><font class="label">GG</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumGiorni(),"0")%></font></td>
        </table>
    </td>
    <td class="r">
      <font class="campo">
        <%= StringUtils.toEuroFormat(lReato.getSanzionePecuniaria())%>
      </font>
      €
      <% if (lReato.getSanzionePecuniaria() != null && lReato.getSanzionePecuniaria().compareTo(new BigDecimal(0)) != 0) { %>
        di
        <font class="campo">
          <%= StringUtils.toStringJSP(lReato.getDescrTipoSanzione())%>
        </font>&nbsp;
      <% } %>
    </td>
    
    <%
    String checkReato = "";
    if ( modalita.equals("M") && aComputo.getReaIdReatoCum()!=null && lReato.getIdReatoCum().compareTo(aComputo.getReaIdReatoCum())==0) checkReato = "checked";
    %>
    <td class="c"><input type="radio" name="IdReato" value="<%= lReato.getIdReatoCum() %>"  <%=checkReato%> ></td>
    
    <% if(lReato.getFlagVisto()!= null && lReato.getFlagVisto().equals("S")) { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else { %>
    <td class="C"> &nbsp;</td>
    <% } %>
    </tr>
<%
    } // end if = 1
   } // end for
%>
    </table>
<%
  }
%>


<table cellspacing="2" cellpadding="2" width="95%" align="center">
  <tr><td colspan=8 class="Titolonocap">Decisione del Giudice dell' Esecuzione</td></tr>
  <tr>
    <td class="l" nowrap>Computo Beneficio <font class="ob">(*)</font> :&nbsp;</td>
    <td class="l"><select name=<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>> <%=TipoAnnotazioneManuale%></select></td>
    <td class="l" nowrap>DPR <font class=ob>(*)</font> :&nbsp;<select name=<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>> <%=listaDPR%></select></td>
  </tr>
<%
  //i dati dell'ordinanza dovranno essere prelevati se esistenti da una tabella x
  //=============================================================================
  // Inserimento dei campi dell'ordinanza
  //=============================================================================
%>
  <tr>
    <td class="l">Ordinanza <font class="ob">(*)</font> :</td>
    <td class="l">
      Anno/Numero
      <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>" size=4 maxlength=4 
		value="<%=StringUtils.toStringJSP (aComputo.getAnnoProvv() )%>"      
      <%=IWebConstants.UTIL_DATA_ANNO%> >
      /
      <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>" size=8 maxlength=38 
		value="<%=StringUtils.toStringJSP (aComputo.getProgrProvv() )%>"      
      onkeypress="return TicTabNumField(this,event)" >
      Procedimento SIGE
    </td>
    <td class="l">
      <font class="label">in data </font>
      &nbsp;&nbsp;
        <input title = "Giorno di arrivo documento" type="text" name="DaGiArr" maxlength="2" size="2" 
			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"dd"))%>"        
        	<%=IWebConstants.UTIL_DATA%>>
        /
        <input title = "Mese di arrivo documento" type="text" name="DaMeArr" maxlength="2" size="2" 
			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"MM"))%>"        
        	<%=IWebConstants.UTIL_DATA%>>
        /
        <input title = "Anno di arrivo documento" type="text" name="DaAnArr" maxlength="4" size="4" 
			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"yyyy"))%>"        
        	<%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>

  <tr>
    <td class="l">Ufficio  <font class="ob">(*)</font> :</td>
    <td class="l" colspan=2>
        <select Title="Ufficio Emittente" name="CodTipoUffEmi" >
          <%=tipoUfficioEmittente%>
        </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede  <font class="ob">(*)</font> :</td>
    <td class="l" colspan=2>
        <font class="campo">
          <input Title="Luogo Ufficio Emittente" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>" 
         	value="<%=luogoUfficioEmittente%>"  size=35 type="text">
          <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.CodTipoUffEmi[document.f.CodTipoUffEmi.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
          
        </font>
    </td>
  </tr>
  <tr>
    <td class="l">Motivazioni :</td>
    <td class="l" colspan=3>
        <textarea cols="58" rows="2" name="<%=ICostantiComputiCumulo.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(aComputo.getNote())%></textarea>
    </td>
  </tr>
  
<%
//==============================================================================
//                    Sezione con i quantum da imputare
//==============================================================================
%>

<table cellspacing="2" cellpadding="2" width="97%" align="center">
  <tr><td colspan=6><hr width="100%"></td></tr>
  <tr>
    <td valign="middle" class=c rowspan=3>+/- <font class="ob">(*)</font><br>
      <select name="PM">
        <option value=""></option>
        <option value="+" <%= "+".equals(aComputo.getFlagPiuMeno())?"selected":""%> >+</option>
        <option value="-" <%= "-".equals(aComputo.getFlagPiuMeno())?"selected":""%> >-</option>
      </select>
  </td>
  <td class=titolo colspan=2>Reclusione</td>
  <td width=25>&nbsp;</td>
  <td class="titolo" colspan=2>Arresto</td>
  </tr>
  <tr>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
		<input type="text" maxlength="2" size="2"
             value="<%=StringUtils.toStringJSP(aComputo.getNumAnniReclusione()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE %>" 
             ONKEYPRESS="return TicTabNumField(this,event)">&nbsp;
		<input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.toStringJSP(aComputo.getNumMesiReclusione()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE %>"  
             ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
		<input type="text" maxlength="4" size="4"
             value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniReclusione()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE %>"        
             ONKEYPRESS="return TicTabNumField(this,event)">
      </td>

      <td class="c">
        <font class="label">Multa</font><br>
		<input type="text" maxlength="7" size="7" style="text-align:right"
		       value="<%=StringUtils.getParteIntera(aComputo.getImportoMulta()) %>"
		       name="<%= ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA%>INT" 
		       ONKEYPRESS="return TicTabNumField(this,event)" >
		,
		<input type="text" maxlength="2" size="2" 
		       value="<%=StringUtils.getParteDecimale(aComputo.getImportoMulta()) %>"
		       name="<%= ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA %>DEC"  
		       ONKEYPRESS="return TicTabNumField(this,event)" >
		</td>
      </td>
      <td width=25>&nbsp;</td>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
		<input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.toStringJSP(aComputo.getNumAnniArresto()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_ANNI_ARRESTO %>" 
             ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
		<input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.toStringJSP(aComputo.getNumMesiArresto()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_MESI_ARRESTO %>" 
             ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
		<input type="text" maxlength="4" size="4" 
             value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniArresto()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_GIORNI_ARRESTO %>"
             ONKEYPRESS="return TicTabNumField(this,event)" >
      </td>
      <td class="c">
        <font class="label">Ammenda</font><br>
		<input type="text" maxlength="7" size="7"  style="text-align:right"
             value="<%=StringUtils.getParteIntera(aComputo.getImportoAmmenda()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA%>INT" 
             ONKEYPRESS="return TicTabNumField(this,event)" >
		,
		<input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.getParteDecimale(aComputo.getImportoAmmenda()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA %>DEC"        
             ONKEYPRESS="return TicTabNumField(this,event)" >
      </td>
    </tr>
    <%-- tr>
      <td class="c" colspan=5><font class="label" style="vertical-align: top;">Note</font>
      <textarea cols="60" rows="2" name="noteRec"></textarea></td>
      <td width=20>&nbsp;</td>
    </tr--%>
  </table>
  
  


  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>

  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">

</form>
</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  // Controllo su Anno Numero Procedimento SIGE
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>","numeric","Il campo Anno Procedimento SIGE può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento SIGE è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE%>","minlen=4","La lunghezza minima per l'Anno Procedimento SIGE è di 4 caratteri");

  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>","numeric","Il campo Numero Procedimento SIGE può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE%>","maxlen=38","La lunghezza massima per il Numero Procedimento SIGE è di 38 caratteri");
  
  // Controllo Data di Arrivo Documento
  frmvalidator.addValidation("DaGiArr","numeric");
  frmvalidator.addValidation("DaGiArr","gt=1");
  frmvalidator.addValidation("DaGiArr","lt=31");

  frmvalidator.addValidation("DaMeArr","numeric");
  frmvalidator.addValidation("DaMeArr","gt=1");
  frmvalidator.addValidation("DaMeArr","lt=12");

  frmvalidator.addValidation("DaAnArr","numeric");
  frmvalidator.addValidation("DaAnArr","gt=1900");

  // Controllo campi Reclusione
  frmvalidator.addValidation("<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>","numeric","Il campo Anni Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>","numeric","Il campo Mesi Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>","numeric","Il campo Giorni Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA %>INT","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA %>DEC","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");

  // Controllo campi Arresto
  frmvalidator.addValidation("<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_ARRESTO%>","numeric","Il campo Anni Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_ARRESTO%>","numeric","Il campo Mesi Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_ARRESTO%>","numeric","Il campo Giorni Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA %>INT","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA %>DEC","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");

  frmvalidator.setAddnlValidationFunction("Verify_Quantum");

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>