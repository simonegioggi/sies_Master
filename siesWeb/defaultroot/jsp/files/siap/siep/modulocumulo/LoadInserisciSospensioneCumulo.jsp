<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>



<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="aProvvedimento"     scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="ContenutoDecisione" scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
  
  
<jsp:useBean id="tipoprovvedimento"  scope="request" class="java.lang.String"/>  
<jsp:useBean id="autoritaemittente"  scope="request" class="java.util.ArrayList"/>
  
<jsp:useBean id="contenutodecisione" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="oggettodecisione"   scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="tipologiadecisione" scope="request" class="java.util.ArrayList"/>


<% 
Collection tiporegistroordinanza =(Collection) request.getAttribute("tiporegistroordinanza");

//============================================================================== 
// Form per l'inserimento e la modifica dei Provvedimenti di annotazione 
// Sospensione
//============================================================================== 
ComputiCumuloModel aComputo = new ComputiCumuloModel();

if ( modalita.equals("M") ) {
  aComputo = aProvvedimento.getListaComputi().elementAt(0);
} 

%> 

<html>
<head>
  <title> Gestione Provvedimento Sospensione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

  <script language="JavaScript" >
    var desktop;
    var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
    
    var valOnLoadCbAut = "";
    var valOnLoadCbContenuto = "";
    var valOnLoadCbOggetto = "";
    var valOnLoadCbTipologia = "";

    <% if( modalita.equals("M") ) { %>
    valOnLoadCbAut       = "<%= StringUtils.toStringJSP( aComputo.getCodTipoAutoritaEmittente() ) %>";
    valOnLoadCbContenuto = "<%= StringUtils.toStringJSP( ContenutoDecisione )%>";
    valOnLoadCbOggetto   = "<%= StringUtils.toStringJSP( aProvvedimento.getCodMotivo() )%>";
    valOnLoadCbTipologia = "<%= StringUtils.toStringJSP( aProvvedimento.getCodEsito() )%>";
    <% } %>
    
  
    function eseguiFunzione(action)
    {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.f.submit();
    }

    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  
    // ** GESTIONE COMBO BOX **
    // Matrice di tante righe quanti sono i tipi registro
    // e tante colonne quante sono le combo da gestire
    var lNumTipoReg=<%=tiporegistroordinanza.size()%>;
    var lNumCombo = 3;
  
    var lMatrice=new Array(lNumTipoReg)
    for (i=0; i<lNumTipoReg; i++)
      lMatrice[i]=new Array(lNumCombo);
<%
    //Autorita
    for(int i=0; i<tiporegistroordinanza.size(); i++)
    {
      List lAut = (List)autoritaemittente.get(i);
  
      out.println("\n\n\tvar lAutorita"+i+"=new Array()\n");
  
      Iterator lIterAutorita = lAut.iterator();
      DecodificheModel lDec = null;
      int idx = 0;
      while(lIterAutorita.hasNext())
      {
        lDec = (DecodificheModel) lIterAutorita.next();
        out.println("\tlAutorita"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getCodiceAlternativo()+"\");");
        idx++;
      }
    }
  
    //Contenuto
    for(int i=0; i<tiporegistroordinanza.size(); i++)
    {
      List lCont = (List)contenutodecisione.get(i);
  
      out.println("\n\n\tvar lContenuto"+i+"=new Array()\n");
  
      Iterator lIterContenuto = lCont.iterator();
      DecodificheModel lDec = null;
      int idx = 0;
      while(lIterContenuto.hasNext())
      {
        lDec = (DecodificheModel) lIterContenuto.next();
if (!"G019".equals(lDec.getFiltro())) {
        out.println("\tlContenuto"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getFiltro()+"\");");
}
        idx++;
      }
    }
  
    //Oggetto
    for(int i=0; i<tiporegistroordinanza.size(); i++)
    {
      List lOgg = (List)oggettodecisione.get(i);
  
      out.println("\n\n\tvar lOggetto"+i+"=new Array()\n");
  
      Iterator lIterOggetto = lOgg.iterator();
      DecodificheModel lDec = null;
      int idx = 0;
      while(lIterOggetto.hasNext())
      {
        lDec = (DecodificheModel) lIterOggetto.next();
if (!"0840".equals(lDec.getCode())) {
        out.println("\tlOggetto"+i+"["+idx+"]= new OggettoDecode(\""+lDec.getDescription()+"\",\""+lDec.getCode()+"\", \""+lDec.getFiltro()+"\");");
}
        idx++;
      }
    }
  
    //Tipologia
    for(int i=0; i<(tiporegistroordinanza.size()); i++)
    {
      List lTipo = (List)tipologiadecisione.get(i);
  
      out.println("\n\n\tvar lTipologia"+i+"=new Array()\n");
  
      Iterator lIterTipologia = lTipo.iterator();
      DecodificheModel lDec = null;
      int idx = 0;
      while(lIterTipologia.hasNext())
      {
        lDec = (DecodificheModel) lIterTipologia.next();
        out.println("\tlTipologia"+i+"["+idx+"]= new OggettoDecode(\""+lDec.getDescription()+"\",\""+lDec.getCode()+"\", \""+lDec.getFiltro()+"\");");
        idx++;
      }
    }
%>
    // La matrice contiene l'insieme delle opzioni selezionabili
    // strutturate in questo modo:
    // ci sono tante righe quante sono le opzioni della combo 'Registro'
    // e tante colonne quante sono le combo da relazionare
    lMatrice[0][0] = lAutorita0;  // Contiene un array di oggetti Option
    lMatrice[0][1] = lContenuto0; // Contiene un array di oggetti Option
    lMatrice[0][2] = lOggetto0;   // Contiene un array di oggetti OggettoDecode
    lMatrice[0][3] = lTipologia0; // Contiene un array di oggetti OggettoDecode
  
    lMatrice[1][0] = lAutorita1;  // Contiene un array di oggetti Option
    lMatrice[1][1] = lContenuto1; // Contiene un array di oggetti Option
    lMatrice[1][2] = lOggetto1;   // Contiene un array di oggetti OggettoDecode
    lMatrice[1][3] = lTipologia1; // Contiene un array di oggetti OggettoDecode
  
    function initCombo()
    {
      caricamento();
    }
  
    function caricamento()
    {
      var idxSel = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>.options.selectedIndex;
  
      // Carica Combo 'Autorità emittente'
      loadCombo(document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>, idxSel, 0);
      if (valOnLoadCbAut!=""){
        selectCombo (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>,valOnLoadCbAut);
        valOnLoadCbAut = "";
      }
      
      // Carica Combo 'Contenuto decisione'
      loadCombo(document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>, idxSel, 1);
      if (valOnLoadCbContenuto!=""){
        selectCombo (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>,valOnLoadCbContenuto);
        valOnLoadCbContenuto = "";
      }
  
      // Carica Combo 'Oggetto decisione'
      caricamentoComboOggetto();
    }
  
    function caricamentoComboOggetto()
    {
      var idxRiga = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>.options.selectedIndex;
  
      loadComboOggettoDecisione(idxRiga, 2);
      
      if (valOnLoadCbOggetto!=""){
        selectCombo (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>,valOnLoadCbOggetto);
        valOnLoadCbOggetto = "";
      }      
  
      // Carica Combo 'Tipologia Decisione'  
      caricamentoComboTipologia();
    }
  
    function caricamentoComboTipologia()
    {
      var idxRiga = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>.options.selectedIndex;
  
      loadComboTipologiaDecisione(idxRiga, 3);
      
      if (valOnLoadCbTipologia!=""){
        selectCombo (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>,valOnLoadCbTipologia);
        valOnLoadCbTipologia = "";
      }       
    }
  
    function loadCombo(campo, riga, colonna)
    {
      for (m=campo.options.length-1;m>=0;m--)
        campo.options[m]=null;
  
      for (i=0;i<lMatrice[riga][colonna].length;i++)
      {
        campo.options[i]=new Option(lMatrice[riga][colonna][i].text,lMatrice[riga][colonna][i].value)
      }
  
      selectCombo(campo);
    }
  
    // La funzione considera solo le opzioni in funzione della selezione della
    // combo 'Contenuto Decisione'
    function loadComboOggettoDecisione(riga, colonna)
    {
      var campo = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>;
      var campoRiferimento = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>;
  
      var j = 0;
  
      for (m=campo.options.length-1;m>=0;m--)
        campo.options[m]=null;
  
      for (i=0;i<lMatrice[riga][colonna].length;i++)
      {
        var valueSel = campoRiferimento[campoRiferimento.options.selectedIndex].value;
  
        if(valueSel == lMatrice[riga][colonna][i].CodiceAlternativo)
        {
          campo.options[j]=new Option(lMatrice[riga][colonna][i].OptionOggetto.text,lMatrice[riga][colonna][i].OptionOggetto.value)
          j++;
        }
      }
  
      selectCombo(campo);
    }
  
    // La funzione considera solo le opzioni in funzione della selezione della
    // combo 'Tipologia Decisione'
    function loadComboTipologiaDecisione(riga, colonna)
    {
      var campo = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>;
      var campoRiferimento = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>;
  
      var j = 0;
  
      for (m=campo.options.length-1;m>=0;m--)
        campo.options[m]=null;
  
      if(riga == 2)
        return;
  
      for (i=0;i<lMatrice[riga][colonna].length;i++)
      {
        var valueSel = campoRiferimento[campoRiferimento.options.selectedIndex].value;
  
        if(valueSel == lMatrice[riga][colonna][i].CodiceAlternativo)
        {
          campo.options[j]=new Option(lMatrice[riga][colonna][i].OptionOggetto.text,lMatrice[riga][colonna][i].OptionOggetto.value)
          j++;
        }
      }
  
      selectCombo(campo);
    }
  
    function selectCombo (combo, valore)
    {
      //alert("campo: "+campo)
      //alert("valore: "+valore)

      for (m=0;m<combo.options.length;m++)
      {
        if(combo.options[m].value == valore)
        {
          combo.options[m].selected=true;
          return;
        }
      }
    }
  
    // Oggetto Javascript che rappresenta i Dati della CG_REF_CODES
    // utili ala caricamento dinamico delle combo 'Oggetto Decisione' e 'Tipologia Decisione'
    function OggettoDecode( Descrizione, Codice, CodiceAlternativo)
    {
      this.OptionOggetto = new Option(Descrizione, Codice);
      this.CodiceAlternativo = CodiceAlternativo;
    }



    function Verify()
    {
      //DATA RICEZIONE PROVVEDIMENTO (non obbligatoria)
      if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value;
      if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value;

      var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value;

      if (!ControllaDataPassaVuota(data_to_verify) )
      {
        alert('Data ricezione provvedimento non valida');
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO %>.focus();

        return false;
      }
      
      if (data_to_verify!="//"){
        if (!CompareDate(data_to_verify,sysDate)){
          alert('La data ricezione non può essere una data futura');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO %>.focus();
          return false;
        }
      }     

      //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
      if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
      if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value;

      var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data emissione provvedimento non valida');
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>.focus();

        return false;
      }
      
      if (!CompareDate(data_to_verify,sysDate)){
        alert('La data emissione provvedimento non può essere una data futura');
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>.focus();
        return false;
      } 


      //DATA SOSPENSIONE ESECUZIONE (obbligatoria)
      if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>.value.length==1)
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>.value;
      if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>.value.length==1)
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>.value;

      var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>.value;

      if (!ControllaDataPassaVuota(data_to_verify) )
      {
        alert('Data sospensione esecuzione non valida');
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE %>.focus();

        return false;
      }
      if (!CompareDate(data_to_verify,sysDate)){
        alert('La data sospensione esecuzione non può essere una data futura');
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>.focus();
        return false;
      }      


      // Non è possibile specificare solo il numero o solo l'anno per il registro
      if(   document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.value.length != 0
         && document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value.length == 0 )
      {
        alert("Valorizzare Anno Registro");
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO %>.focus();
  
        return false;
      }

      if(   document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.value.length == 0
         && document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value.length != 0 )
      {
        alert("Valorizzare Numero Registro");
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO %>.focus();

        return false;
      }

      // Non è possibile specificare solo il numero o solo l'anno per il provvedimento
      if(  document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>.value.length != 0
        && document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>.value.length == 0 )
      {
        alert("Valorizzare Anno Provvedimento");
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO %>.focus();

        return false;
      }

      if(   document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>.value.length == 0
         && document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>.value.length != 0 )
      {
        alert("Valorizzare Numero Provvedimento");
        document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO %>.focus();

        return false;
      }
        
      return true;
    }
  </script>

</head>

<body class="corpo" onLoad="initCombo();" >
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
        <font class="campo">Inserimento annotazione Sospensione &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica annotazione Sospensione &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaSospensioneCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciSospensioneCumulo">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  
  <input type="HIDDEN" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="HIDDEN" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="HIDDEN" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"             		  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">


  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td colspan="4" class="titolo">Dati del provvedimento di sospensione dell'esecuzione</td>
    </tr>
    <tr>
      <td class="l" width="20%">
        Data ricezione provvedimento
      </td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno ricezione provvedimento" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="2" size="2"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"dd"))%>"  
          <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Mese ricezione provvedimento"   
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="2" size="2"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"MM"))%>"  
          <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Anno ricezione provvedimento" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="4" size="4"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"yyyy"))%>"  
          <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
  </tr>
  <tr>
    <td class="l">
        Anno/Numero registro
    </td>
    <td class="l" colspan="3">
        <input type="text" Title="Anno registro" maxlength=4 size=4
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>" 
          value="<%=StringUtils.toStringJSP( aComputo.getAnnoProc() )%>" 
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
          > 
        /
        <input type="text" Title="Numero registro" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>" size=6 maxlength=6 
          value="<%=StringUtils.toStringJSP( aComputo.getProgrProc() )%>" 
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" > 
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          
        Registro <font class="ob">(*)</font> &nbsp;&nbsp;
        <select Title="Registro" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_REGISTRO_ORDINANZA%>" onChange="caricamento();">
<%
          Iterator lIter = tiporegistroordinanza.iterator();
          while(lIter.hasNext())
          {
            DecodificheModel lDecMod = (DecodificheModel)lIter.next();
            String lSelected = "";
            
            if (lDecMod.getCodiceAlternativo().equals(aComputo.getCodTipoRegistroOrdinanza()))
              lSelected = "selected";
%>
            <option value="<%=lDecMod.getCodiceAlternativo()%>" <%=lSelected%> /><%=lDecMod.getCode()%>
<%
          }
%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l" >
      Data emissione provvedimento <font class="ob">(*)</font>
  </td>
  <td class="l" colspan="3">      
      <input type="text" Title="Giorno emissione provvedimento"
        name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>" maxlength="2" size="2" 
        value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataEmissioneProvv(), "dd") )%>" 
        <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" Title="Mese emissione provvedimento" 
        name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>" maxlength="2" size="2" 
        value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataEmissioneProvv(), "MM") )%>" 
        <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" Title="Anno emissione provvedimento" 
        name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>" maxlength="4" size="4" 
        value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataEmissioneProvv(), "yyyy") )%>" 
        <%=IWebConstants.UTIL_DATA_ANNO%>>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

      <font class="l" >Anno/Numero provvedimento</font>
      <input type="text" Title="Anno Provvedimento" size=4 maxlength=4
        name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO %>" 
        value="<%=StringUtils.toStringJSP( aComputo.getAnnoProvv() )%>" 
        onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> 
      /
      <input type="text" Title="Numero Provvedimento" size=6 maxlength=6
        name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO %>" 
        value="<%=StringUtils.toStringJSP( aComputo.getProgrProvv() )%>"
        onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
        > &nbsp;&nbsp;
    </td>
  </tr>

  <tr>
    <td class="l">
      Tipo provvedimento <font class="ob">(*)</font>
    </td>
    <td class="l" colspan="3">
      <select Title="Tipo Provvedimento" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
        <%=tipoprovvedimento%>
      </select>
    </td>
  </tr>

  <tr>
    <td class="l">
      Autorità emittente <font class="ob">(*)</font>
    </td>
    <td class="l" colspan="3">
      <select Title="Autorità emittente" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
      </select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Sede&nbsp;<font class="ob">(*)</font>
        <input title="Sede Autorita Esterna"  type="text" value="<%=StringUtils.toStringJSP( aComputo.getDescluogoUfficioEmittenteProvv() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
        <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.options.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
    </td>
  </tr>
  <tr>
    <td class="l" nowrap>
      Contenuto decisione <font class="ob">(*)</font>
    </td>
    <td class="l" colspan="3">
      <select Title="Contenuto Decisione" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>" onChange="caricamentoComboOggetto();">
      </select>
    </td>
  </tr>
  <tr>
    <td class="l" nowrap>
      Oggetto decisione <font class="ob">(*)</font>
    </td>
    <td class="l" colspan="3">
      <select Title="Oggetto Decisione" class="small" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>">
      </select>
    </td>
  </tr>
  <tr>
    <td class="l" nowrap>
        Tipologia decisione <font class="ob">(*)</font>
    </td>
    <td class="l" colspan="3">
        <select Title="Tiipologia Decisione" class="small" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>">
        </select>
    </td>
  </tr>
  <tr>
    <td class="l">
      Motivazioni
    </td>
    <td class="l" colspan="3">
      <textarea cols="60" rows="2" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP( aComputo.getNote() )%></textarea>
    </td>
  </tr>
</table>

<table cellspacing="2" cellpadding="2" width="95%" align="center">
  <tr>
    <td class="l">
      Data sospensione esecuzione <font class="ob">(*)</font>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="text" Title="Giorno sospensione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataSospensioneInterruzione(), "dd") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" Title="Mese sospensione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataSospensioneInterruzione(), "MM") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" Title="Anno sospensione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aComputo.getDataSospensioneInterruzione(), "yyyy") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
      &nbsp;&nbsp;&nbsp;
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
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
    var frmvalidator = new Validator("f");
  
  // Data ricezione provvedimento
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=31");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=12");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=3000");
  
  // Sede
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Sede è obbligatorio");
  
  // Anno registro
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","maxlen=4","La lunghezza massima per l'anno registro è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","minlen=4","La lunghezza minima per l'anno registro è di 4 caratteri");
  // Numero registro
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>","numeric");
  
  // Anno provvedimento
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno provvedimento è di 4 caratteri");
  
  // Numero provvedimento
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>","numeric");
  
  // Data emissione provvedimento
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Giorno emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=31")
  
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Mese emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=12");
  
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Anno emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=3000");

  // Data sospensione Esecuzione
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>","req","Il campo Giorno sospensione esecuzione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_SOSPENSIONE_ESECUZIONE%>","lt=31");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>","req","Il campo Mese sospensione esecuzione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_SOSPENSIONE_ESECUZIONE%>","lt=12");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","req","Il campo Anno sospensione esecuzione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","maxlen=4","La lunghezza massima per l'anno sospensione esecuzione è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","minlen=4","La lunghezza minima per l'anno sospensione esecuzione è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_SOSPENSIONE_ESECUZIONE%>","lt=3000");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>