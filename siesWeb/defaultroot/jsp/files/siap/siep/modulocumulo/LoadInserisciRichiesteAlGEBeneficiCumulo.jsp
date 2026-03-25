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
<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="aProvvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aIdComputo"     scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoUfficioEmittente"  scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoUfficioEmittente" scope="request" class="java.lang.String"/>

<jsp:useBean id="reati"         scope="request" class="java.util.Vector" /> <% // Reati sul fascicolo %>

<jsp:useBean id="listaDPR"               scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoAnnotazioneManuale" scope="request" class="java.lang.String" />
<jsp:useBean id="TipoAnnotazioneManualeIncDep" scope="request" class="java.lang.String" />

<jsp:useBean id="tipoProvvedimento" scope="request" class="java.lang.String" />

<jsp:useBean id="TipiFontiReato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" scope="request" class="java.lang.String" />

<% 
//============================================================================== 
// Form per l'inserimento e la modifica dei Provvedimenti di Richiesta al GE
// Amnistia/Indulto/depenalizzazione/Incostituzionalita' (modulo cumulo)
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

int maxNumComputi = 1;

%> 

<html>
<head>
  <title> Gestione Richieste al GE Applicazione/Revoca Benefici </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
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

    function mettiFonte(str)
    {
        if(str == "004")
        {
            document.f.<%= ICostantiReato.CAMPO_COD_FONTE %>.value="03";
            document.f.<%= ICostantiReato.CAMPO_ANNO_FONTE %>.value="2016";
            document.f.<%= ICostantiReato.CAMPO_NUMERO_FONTE %>.value="7";
        }
        else if(str == "017")
        {
            document.f.<%= ICostantiReato.CAMPO_COD_FONTE %>.value="03";
            document.f.<%= ICostantiReato.CAMPO_ANNO_FONTE %>.value="2016";
            document.f.<%= ICostantiReato.CAMPO_NUMERO_FONTE %>.value="8";
        }
    }
    
    function selTipoRichiesta(){
    	var motivo = $('#<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>').val();
        if (   motivo=='0287' || motivo=='0288' 
            || motivo=='0291' || motivo=='0292'
            || motivo=='0290' || motivo=='0294') 
        {
            $('#tdTipoAnnAmnInd').show();
            $('#tipoAnnAmnInd').prop('disabled',false);
            $('#tipoAnnAmnInd').change();
            
            $('#tdTipoAnnIncDep').hide();
            $('#tipoAnnIncDep').prop('disabled',true);
        }
        else if (motivo=='0293' || motivo=='0289') {
            $('#tdTipoAnnAmnInd').hide();
            $('#tipoAnnAmnInd').prop('disabled',true);
            
            $('#tdTipoAnnIncDep').show();
            $('#tipoAnnIncDep').prop('disabled',false);
            $('#tipoAnnIncDep').change();
        }
        else {
            $('#tdTipoAnnAmnInd').hide();
            $('#tipoAnnAmnInd').prop('disabled',true);
            
            $('#tdTipoAnnIncDep').hide();
            $('#tipoAnnIncDep').prop('disabled',true);
            
            $('#tabIndulto').hide();
            $('#tabDepenalizzazione').hide();
            $('#tabIncostituzionalita').hide();             
        }
    }
    
    function selTipoAnnotazione(obj){
    	var annotazione = $(obj).val();
        if (annotazione=='002' || annotazione=='003' ){
            $('#tabIndulto').show();
            $('#tabDepenalizzazione').hide();
            $('#tabIncostituzionalita').hide();        	
        }
        else if (annotazione=='004' || annotazione=='017' ){
            $('#tabIndulto').hide();
            $('#tabDepenalizzazione').show();
            $('#tabIncostituzionalita').hide();   
            mettiFonte(annotazione);
        }
        else if (annotazione=='013'){
            $('#tabIndulto').hide();
            $('#tabDepenalizzazione').hide();
            $('#tabIncostituzionalita').show();           	
        }
    }
    
    var data_to_verify;
    var obbligatori;
    var dataesiste;

    //============================================================================
    // funzione per la verifica dei dati imputati in maschera
    //============================================================================
    function Verify()
    {
        var motivo = $('#<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>').val();
        if (motivo=='-'){
            alert("Selezionare il tipo di richiesta");
            $('#<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>').focus();
            return false;       
        }
        
        var annotazione = '';
        if (   motivo=='0287' || motivo=='0288' || motivo=='0291' || motivo=='0292' || motivo=='0290' || motivo=='0294') 
        {
            annotazione = $('#tipoAnnAmnInd').val();
            if (annotazione=='-'){
                alert("Selezionare computo beneficio");
                $('#tipoAnnAmnInd').focus();
                return false;
            }
        }
        else {
            annotazione = $('#tipoAnnIncDep').val();
            if (annotazione=='-'){
                alert("Selezionare computo beneficio");
                $('#tipoAnnIncDep').focus();
                return false;
            }       
        }
        
        if (annotazione=='002' || annotazione=='003' ){
            if (document.f.<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>[document.f.<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>.selectedIndex].value=="-")
            {
              alert("Selezionare DPR");
              return false;
            }
        }
        else if (annotazione=='004'){
            if(!(document.f.Radio_Depe_Ammi[0].checked ) &&
               !(document.f.Radio_Depe_Ammi[1].checked ) )
            {
                alert ("Selezionare tra Depenalizzazione e Illecito Amministrativo");
                return false;     
            }
            
            if(document.f.<%=ICostantiReato.CAMPO_COD_FONTE%>[document.f.<%=ICostantiReato.CAMPO_COD_FONTE%>.selectedIndex].value == "-")
            {
              alert ("Selezionare la Fonte");
              document.f.<%=ICostantiReato.CAMPO_COD_FONTE%>.focus();
              return false;     
            }           
        }        
        else if (annotazione=='013'){
            var GGScc     = document.f.ggScc.value;
            var MMScc     = document.f.mmScc.value;
            var AAScc     = document.f.aaScc.value;
            var AnnoScc   = document.f.annoSCC.value;
            var NumScc    = document.f.numeroSCC.value;   
            
            if(AnnoScc == '')
            {
              alert("Anno Sentenza Corte Costituzionale Obbligatorio");
              document.f.annoSCC.focus();
              return false;
            }
          
            if(NumScc == '')
            {
              alert("Numero Sentenza Corte Costituzionale Obbligatorio");
              document.f.annoSCC.focus();
              return false;
            } 
          
            if (GGScc.length==1) GGScc="0"+GGScc;
            
            if (MMScc.length==1) MMScc.value="0"+MMScc.value;
              
            data_to_verify = GGScc +"/"+MMScc+"/"+AAScc;
              
            if (! ControllaData(data_to_verify))
            {
              alert('Data declaratoria non valida');
              document.f.ggScc.focus();
              return false;
            }   
        }    	
        
        
    	//==========================================================================
        // Controlli su dati dell'ordinanza
        //==========================================================================
        if (document.f.DaGiArr.value.length==1)
          document.f.DaGiArr.value="0"+document.f.DaGiArr.value;
        if (document.f.DaMeArr.value.length==1)
          document.f.DaMeArr.value="0"+document.f.DaMeArr.value;
      
        data_to_verify = document.f.DaGiArr.value +"/"+document.f.DaMeArr.value+"/"+document.f.DaAnArr.value;
      
        if (! ControllaData(data_to_verify))
        {
          alert('Data richiesta non valida');
          return false;
        }

        // La Data di richiesta deve essere <= SYSDATE
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
          alert('Data richiesta superiore alla data attuale');
          return false;
        }
          
        var codTipoUffEmi = document.f.CodTipoUffEmi.value;
        var luogoUffEmi   = document.f.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value;

        if(codTipoUffEmi == '-')
        {
            alert("Dati Richiesta Incompleti. Tipo Ufficio Giudice dell'esecuzione obbligatorio");
            document.f.CodTipoUffEmi.focus();
            return false;
        }
        
        if(luogoUffEmi == '')
        {
            alert("Dati Richiesta Incompleti. Sede Giudice dell'esecuzione obbligatoria.");
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

    $(document).ready(function(){
    	<% if( modalita.equals("M") ) { %>
    	var motivoObj = $('#<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>');
    	var motivo = '<%=aProvvedimento.getCodMotivo()%>';
    	var annotaVal = '<%=aComputo.getCodTipoAnnotazione()%>';
    	var annotaObj = null;
        if (   motivo=='0287' || motivo=='0288' 
            || motivo=='0291' || motivo=='0292'
            || motivo=='0290' || motivo=='0294') 
        {
        	annotaObj = $('#tipoAnnAmnInd');
        }
        else if (motivo=='0293' || motivo=='0289') {
            annotaObj = $('#tipoAnnIncDep');
        }
        motivoObj.val(motivo);
        motivoObj.change();
        annotaObj.val(annotaVal);
        annotaObj.change();
    	<% } %>
    });
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
        <font class="campo">Inserimento Richieste al GE Applicazione/Revoca Benefici &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica Richieste al GE Applicazione/Revoca Benefici &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRichBenGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichBenGECumulo">
  <input type="hidden" name="maxNumComputi" value="<%=maxNumComputi%>" >
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  
  <input type="hidden" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiComputiCumulo.CAMPO_FLAG_STATO %>"                              value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">

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
    if ( modalita.equals("M") && aComputo.getReaIdReatoCum()!=null && lReato.getIdReatoCum().compareTo(aComputo.getReaIdReatoCum())==0) 
        checkReato = "checked";
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
  <tr><td colspan=8 class="Titolonocap">Richiesta Al Giudice dell' Esecuzione</td></tr> 
  
  <tr>
    <td class="l" nowrap>Tipo Richiesta <font class="ob">(*)</font> :&nbsp;</td>
    <td class="l">
        <select name=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>
                  id=<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>
                  onChange="javascript: selTipoRichiesta();"> 
            <option value = "-"  />-        
            <%=tipoProvvedimento%>
        </select>
    </td>
    <td class="l" id="tdTipoAnnAmnInd" style="display:none;">
        Amnistia/Indulto <font class="ob">(*)</font>
        <select name="<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>" id="tipoAnnAmnInd"
                onChange="javascript:selTipoAnnotazione(this)"> 
            <%=TipoAnnotazioneManuale%>
        </select>
    </td>
    <td class="l" id="tdTipoAnnIncDep" style="display:none;">
        Depenalizzazione/Incostituzionalita' <font class="ob">(*)</font>
        <select name="<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>" id="tipoAnnIncDep"
                onChange="javascript:selTipoAnnotazione(this)"> 
            <%=TipoAnnotazioneManualeIncDep%>
        </select>
    </td>
  </tr>
</table>

<%-- Amnistia/Indulto [] --%>
<table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabIndulto" style="display:none;">
  <tr><td colspan=8 class="Titolonocap">Amnistia/Indulto</td></tr> 
  <tr>
    <td class="l" nowrap>DPR <font class=ob>(*)</font> :&nbsp;
        <select name=<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>> <%=listaDPR%></select>
    </td>
  </tr>
</table>  
  

<%-- Incostituzionalità [] --%>
<%
//==============================================================================
//                    Sezione Sentenza Corte Costituzionale
//==============================================================================
%>
<table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabIncostituzionalita" style="display:none;">
  <tr>
    <td class="Titolonocap" colspan=8>
      Dichiarazione di illegittimità costituzionale
    </td>
  </tr>
  <tr>
    <td class="l" colspan=2 valign=middle>Sentenza Corte Costituzionale :</td>
    <td class="l" colspan=3> Anno/Numero
      <input type="text" name="annoSCC"  size=4 maxlength=4 
             value="<%=StringUtils.toStringJSP(aComputo.getAnnoSentenza() )%>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      /
      <input type="text" name="numeroSCC" size=8 maxlength=8 value="<%=StringUtils.toStringJSP(aComputo.getNumeroSentenza() )%>">
    </td>
    <td class="c" colspan=3>
      <font class="label">in data </font>
        <input title = "Giorno Sentenza Corte Costituzionale" type="text" name="ggScc" maxlength="2" size="2" 
            value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataSentenza(),"dd"))%>"        
            <%=IWebConstants.UTIL_DATA%>>
        /
        <input title = "Mese Sentenza Corte Costituzionale" type="text" name="mmScc" maxlength="2" size="2" 
            value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataSentenza(),"MM"))%>"        
            <%=IWebConstants.UTIL_DATA%>>
        /
        <input title = "Anno di arrivo documento" type="text" name="aaScc" maxlength="4" size="4" 
            value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataSentenza(),"yyyy"))%>"        
            <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
</table>  



<%
//==============================================================================
//                    Sezione Tipo Depenalizzazione
//==============================================================================
%>
<table cellspacing="2" cellpadding="2" width="95%" align="center" id="tabDepenalizzazione" style="display:none;">
  <tr>
    <td class="Titolo">Fonte</td>
    <td class="Titolo">Anno</td>
    <td class="Titolo">Numero</td>
    <td class="Titolo">Articolo</td>
    <td class="Titolo">Art.qualificante</td>
    <td class="Titolo">Comma</td>
    <td class="Titolo">Lettera</td>
    <td class="Titolo">Numero</td>
  </tr>
  <tr>
    <td class="c">
      <select name="<%=ICostantiReato.CAMPO_COD_FONTE%>">
        <%=TipiFontiReato%>
      </select>
    </td>
    <td class="c">
      <input type="text" size="4" maxlength="4" title="Anno Fonte" 
             value="<%=StringUtils.toStringJSP (aComputo.getAnnoFonte() )%>" 
             name="<%=ICostantiReato.CAMPO_ANNO_FONTE%>"
             <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
    <td class="c">
      <input size=6 maxlength=6 title="Numero Fonte" value="<%=StringUtils.toStringJSP (aComputo.getNumeroFonte() )%>" type="text" name="<%=ICostantiReato.CAMPO_NUMERO_FONTE%>">
    </td>
    <td class="c">
      <input size=5 maxlength=5 title="Articolo Fonte" value="<%=StringUtils.toStringJSP (aComputo.getArticolo() )%>" type="text" name="<%=ICostantiReato.CAMPO_ARTICOLO%>">
    </td>
    <td class="c">
      <select name="<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>">
        <%=TipiSottonumerazione%>
      </select>
    </td>
    <td class="c">
      <input size=10 maxlength=10 title="Comma" value="<%=StringUtils.toStringJSP (aComputo.getComma() )%>" type="text" name="<%=ICostantiReato.CAMPO_COMMA%>">
    </td>
    <td class="c">
      <input size=2 maxlength=2 title="Lettera" value="<%=StringUtils.toStringJSP (aComputo.getLettera() )%>" type="text" name="<%=ICostantiReato.CAMPO_LETTERA%>">
    </td>
    <td class="c">
      <input size=2 maxlength=2 title="Numero" value="<%=StringUtils.toStringJSP (aComputo.getNumero() )%>" type="text" name="<%=ICostantiReato.CAMPO_NUMERO%>">
    </td>
  </tr>
</table>
<%
  //=============================================================================
  // Inserimento dei campi dell'ordinanza
  //=============================================================================
%>
<br>
<table cellspacing="2" cellpadding="2" width="95%" align="center">
  <tr>
    <td class="l">
      <font class="label">Data richiesta al GE </font><font class="ob">(*)</font>
    </td>
    <td class="l">
        <input title = "Giorno" type="text" name="DaGiArr" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"dd"))%>"        
               <%=IWebConstants.UTIL_DATA%>>
        /
        <input title = "Mese" type="text" name="DaMeArr" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"MM"))%>"        
               <%=IWebConstants.UTIL_DATA%>>
        /
        <input title = "Anno" type="text" name="DaAnArr" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"yyyy"))%>"        
               <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>

  <tr>
    <td class="l">Giudice dell'esecuzione <font class="ob">(*)</font> :</td>
    <td class="l" colspan=2>
        <select Title="Ufficio Emittente" name="CodTipoUffEmi" >
          <option value = "-"  />-
          <%=tipoUfficioEmittente%>
        </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Sede  <font class="ob">(*)</font> :</td>
    <td class="l" colspan=2>
      <font class="campo">
          <input Title="Luogo Ufficio GE" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>" 
                 value="<%=luogoUfficioEmittente%>"  size=35 type="text">
          <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.CodTipoUffEmi[document.f.CodTipoUffEmi.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>          
      </font>
    </td>
  </tr>
</table>  
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
  </table>

<br>
  <table width="95%" align="center">
    <tr>
      <td class="l">
        Anticipazione degli effetti&nbsp;&nbsp;
      
        <input type="checkbox" name="<%=ICostantiComputiCumulo.CAMPO_FLAG_APP_PROVVISORIA %>" value="A"  
               <%="A".equals(aComputo.getFlagAppProvvisoria())?"checked":""%>>
      </td>
    </tr>
  </table>


  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>


</form>
</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  // Controllo Data di Arrivo Documento
  frmvalidator.addValidation("DaGiArr","numeric");
  frmvalidator.addValidation("DaGiArr","gt=1");
  frmvalidator.addValidation("DaGiArr","lt=31");

  frmvalidator.addValidation("DaMeArr","numeric");
  frmvalidator.addValidation("DaMeArr","gt=1");
  frmvalidator.addValidation("DaMeArr","lt=12");

  frmvalidator.addValidation("DaAnArr","numeric");
  frmvalidator.addValidation("DaAnArr","gt=1900");

  frmvalidator.setAddnlValidationFunction("Verify_Quantum");

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>