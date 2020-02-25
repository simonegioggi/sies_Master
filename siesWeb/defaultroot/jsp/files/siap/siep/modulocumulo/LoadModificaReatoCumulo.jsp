<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="reato"     scope="request" class="siap.siep.modulocumulo.model.ReatoCumuloModel"/>

<jsp:useBean id="TipiReato"             scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato"        scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiCommaQualificante" scope="request" class="java.lang.String"/>
<jsp:useBean id="PeriodoConsumazione"   scope="request" class="java.lang.String"/>


<html>
<head>
  <title>[S.I.E.S.] - Gestione Reato (CUMULO) </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  
  //==========================================================================
  // Ritorna alla lista dei Reati per il Titolo
  //==========================================================================
  function eseguiFunzione(action)
  {
    document.LoadModificaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.LoadModificaReatoCumulo.submit();
  }
    
   //==========================================================================
   // Controlli sui campi alla lista dei Reati per il Titolo
   //==========================================================================     
  function ControlloObbligatorieta()
  {
    // controllo obbligatorietà articolo e fonte
    if (   document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>.value=="-"
        || document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>.value.length==0)
    {
        alert("Fonte/Articolo obbligatori!");
        document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>.focus();
        return false;
    }
      
    if (   (   document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>.value!="-"
            && document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>.value.length==0) 
        || (   document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>.value=="-"
            && document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>.value.length>0)
       )
    {
      alert("Fonte/Articolo devono essere entrambi presenti");
      document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>.focus();
      return false;
    }
        
    // controllo obbligatorietà comma
    if (   document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COMMA%>.value==""
        && document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COMMA_QUALIFICANTE%>.value!="-") 
    {
      alert("Comma obbligatorio");
      document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COMMA%>.focus();
      return false;
    }
  
    // *********************************************************************************************
    //               controllo tra il campo periodo_consumazione e le relative date
    // *********************************************************************************************
    // per i seguenti valori devono essere presenti entrambe le date:
    // valore  4 =  Accertato in Data [data1] e Permanente Sino al [data2]       
    // valore  7 =  Accertato in Data [data1] e in Data [data2]                  
    // valore  9 =  Commesso in Data [data1] e Permanente Sino al [data2]   
    // valore 10 =  Commesso in Data [data1] e in Data [data2] 
    // valore 17 =  Accertato dalla data  [data1] e fino alla data  [data2]   
    // valore 23 =  Commesso dalla data [data1]  e fino alla data  [data2]   
    if (   (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="04")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="07")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="09")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="10")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="17")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="23")
       )
    {
      if (   document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.value.length==0   
          || document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.value.length==0)
      {
        alert("Data1 e Data2 devono essere entrambe presenti!");
        document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.focus();
        return false;
      }
    } // fine if
  
    // *********************************************************************************************
    // per i seguenti valori devono essere presenti entrambe le date:
    // valore  1 =  Commesso in Data [data1]                                    
    // valore  2 =  Accertato in Data [data1]                                   
    // valore  3 =  Accertato in Data [data1] e Tuttora Permanente              
    // valore  5 =  In Epoca Anteriore e Prossima al [data1]                    
    // valore  6 =  In Epoca Successiva e Prossima al [data1]                   
    // valore  8 =  Commesso in Data [data1] e Tuttora Permanente               
    // valore 11 =  Commesso Fino al [data1]                                    
    // valore 12 =  Il [data1]                                                  
    // valore 15 =  Accertato dalla data [data1]                                
    // valore 18 =  Accertato fino alla data  [data1]                    
    // valore 19 =  Accertato in epoca anteriore e prossima alla data  [data1]  
    // valore 20 =  Accertato in epoca successiva e prossima alla data  [data1] 
    // valore 22 =  Commesso dalla data  [data1]                                
    // valore 24 =  Commesso in epoca anteriore e prossima alla data  [data1]   
    // valore 25 =  Commesso in epoca successiva e prossima alla data  [data1]
    if (   (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="01")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="02")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="03")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="05")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="06")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="08")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="11")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="12")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="15")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="18")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="19")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="20")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="22")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="24")
        || (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="25")
       )
    {
      if (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.value.length==0)    
      {
        alert("Data1 obbligatoria");
        document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.focus();
        return false;
      }
    } // fine if
    
  // -> i valori 16 = Accertato e 21 = Commesso NON RICHIEDONO DATA COMMESSO REATO
    // -> i valori 13 e 14 NON SONO PREVISTI
  
  // se inserisco il luogo commesso reato è obbligatorio indicare un periodo di consumazione
  if ((document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="-")
  && (document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_DESC_LUOGO%>.value!=""))
  {
    alert("indicare un periodo di consumazione se si vuole inserire il luogo commesso reato");
      document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.focus();
    return false;
  }
    
    return true;

  } // fine funzione
  //****************************************************************************************************
 
  function mettidata()
  {
  
    <% if(reato.getProgrCircostanza().intValue() == 1) { %>
    
    var valueSel = document.LoadModificaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value;

    var nodedata1 = document.getElementById('divdata1');
    var nodedata2 = document.getElementById('divdata2');
  
    if(valueSel == '01' || valueSel == '02' || valueSel == '03' || valueSel == '05' || valueSel == '06' || 
       valueSel == '08' || valueSel == '11' || valueSel == '12' || valueSel == '15' || valueSel == '18' || 
       valueSel == '19' || valueSel == '20' || valueSel == '22' || valueSel == '24' || valueSel == '25') 
    {
      nodedata1.style.visibility='visible';
      nodedata2.style.visibility='visible';
            
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>.disabled = false;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>.disabled = false;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.disabled = false;   
      
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.disabled = true;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.disabled = true;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.disabled = true; 
      
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.value = "";
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.value = "";
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.value = "";                
    }
        
      if(valueSel == '04' || valueSel == '07' || valueSel == '09' || valueSel == '10' || 
         valueSel == '17' || valueSel == '23')
    {
      nodedata1.style.visibility='visible';
      nodedata2.style.visibility='visible';

      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>.disabled = false;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>.disabled = false;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.disabled = false;   
            
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.disabled = false;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.disabled = false;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.disabled = false;  
    }
        
    if(valueSel == '-' || valueSel == '16' || valueSel == '21')
    {
      nodedata1.style.visibility='visible';
      nodedata2.style.visibility='visible';

      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>.disabled = true;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>.disabled = true;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.disabled = true;   
            
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.disabled = true;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.disabled = true;
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.disabled = true; 
      
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>.value = "";
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>.value = "";
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.value = "";   
            
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.value = "";
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.value = "";
      document.LoadModificaReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.value = "";
    }       
    
    <% } %>

  } <% // Chiude Function mettidata()  %>
  
  </script>
</head> 

<body class="corpo" onLoad="mettidata()">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Modifica Reato relativo al Titolo Cumulato</font>
      </td>

      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaReatoCumulo')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
      
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaReatoCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActModificaReatoCumulo">

  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">


  <input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_ID_REATO_CUM%>"      value="<%=reato.getIdReatoCum()%>">
  <input type="HIDDEN" name="<%=ICostantiReatoCumulo.CAMPO_PROGR_REATO%>"       value="<%=reato.getProgrReato().toString()%>">
  <input type="HIDDEN" name="<%=ICostantiReatoCumulo.CAMPO_PROGR_CIRCOSTANZA%>" value="<%=reato.getProgrCircostanza().toString()%>" >
  

  <input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO%>" value="<%=reato.getTitIdTitoloCumulato()%>">
  <input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_FLAG_STATO%>"             value="<%=reato.getFlagStato()%>">
  
<%
  //Se il reato è quello principale
  if(reato.getProgrCircostanza().intValue() == 1)
  {
%>
    <table cellspacing=2 cellpadding=2>
      <tr>
          <td class="l">Numero Reato</td>
          <td class="c">
            <input size=10 maxlength=10 value="<%=StringUtils.toStringJSP(reato.getProgrNumeroManuale()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_PROGR_NUMERO_MANUALE %>">
          </td>
          <td>&nbsp;</td>
          
    <%
    // Descrizione dello stato 

        String lDescStato = "";
        String lFlag = "";
        lFlag = reato.getFlagStato();
      
        if      ( reato.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
        else if ( reato.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
        else if ( reato.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
        else if ( reato.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
    %>

          <td class="l"><%=lDescStato %></td> 
      
        </tr> 
    </table>
<%
  }
%>

  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art.qualificante</td>
      <td class="int">Comma</td>
      <td class="int">Comma qualificante</td> 
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
    </tr>
    <tr>
      <td class="l">
        <select name="<%= ICostantiReatoCumulo.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="l">
        <input type="text" size=4 maxlength=4 title="Anno" 
               value="<%=StringUtils.toStringJSP(reato.getAnnoFonte()) %>"                 
               name="<%= ICostantiReatoCumulo.CAMPO_ANNO_FONTE %>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               >
      </td>
      <td class="l">
        <input size=6 maxlength=6 title="Numero" value="<%=StringUtils.toStringJSP(reato.getNumeroFonte()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_NUMERO_FONTE %>">
      </td>
      <td class="l">
        <input type="text" size=5 maxlength=5 title="Articolo" 
               value="<%=StringUtils.toStringJSP(reato.getArticolo()) %>" 
               name="<%= ICostantiReatoCumulo.CAMPO_ARTICOLO %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="l">
      <select name="<%= ICostantiReatoCumulo.CAMPO_COD_SOTTONUMERAZIONE %>">
        <%=TipiSottonumerazione %>
      </select>
      </td>
      <td class="l">
        <strong>C</strong>&nbsp;<input size=10 maxlength=10 title="Comma" value="<%=StringUtils.toStringJSP(reato.getComma()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_COMMA %>">
      </td>
      <td class="c">
        <select name="<%= ICostantiReatoCumulo.CAMPO_COMMA_QUALIFICANTE %>">
          <%=TipiCommaQualificante%>
        </select>
      </td>      
      <td class="l">
        <strong>L</strong>&nbsp;<input size=2 maxlength=2 title="Lettera" value="<%=StringUtils.toStringJSP(reato.getLettera()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_LETTERA %>">
      </td>
      <td class="l">
        <strong>N</strong>&nbsp;<input size=2 maxlength=2 title="Numero" value="<%=StringUtils.toStringJSP(reato.getNumero()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_NUMERO %>">
      </td>
    </tr>
  </table>
<%
  //Se il reato è quello principale
  if(reato.getProgrCircostanza().intValue() == 1)
  {
%>
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo" colspan=4>Reato</td></tr>
    <tr>
        <td class="l">Tipo Reato</td>
        <td class="l">
        <select name="<%= ICostantiReatoCumulo.CAMPO_COD_TIPO_REATO %>"  >
          <%=TipiReato%>
        </select>
        </td>
    </tr>
    <tr>
        <td class="l">Luogo Reato</td>
        <td class="l"><input size=50 maxlength=300 value="<%=StringUtils.toStringJSP(reato.getDescLuogo()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_DESC_LUOGO %>"  ></td>
    </tr>
    <tr>
      <td class="l">Periodo Consumazione</td>
      <td class="l">
        <select name="<%= ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE %>" onChange="mettidata()">
          <%=PeriodoConsumazione%>
        </select>
      </td>
    </tr>
  </table>
  <table cellspacing=2 cellpadding=2>
    <tr>
    
    <div id="divdata1" style="visibility:hidden; position:relative; width: 100%;" >
    
      <td class="l">&lt;Data1&gt;</td>
      <td class="l">
<%
        String lStrGGInizio = StringUtils.toStringJSP( reato.getGiornoInizio());
        if (lStrGGInizio.length() == 1)
          lStrGGInizio = "0"+lStrGGInizio;

        String lStrMMInizio = StringUtils.toStringJSP( reato.getMeseInizio());
        if (lStrMMInizio.length() == 1)
          lStrMMInizio = "0"+lStrMMInizio;

        String lStrAAInizio = StringUtils.toStringJSP( reato.getAnnoInizio());
%>
        <input size=2 maxlength=2 value="<%=lStrGGInizio%>" type="text" size="2" maxlength="2" name="<%=ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=2 maxlength=2 value="<%=lStrMMInizio%>" type="text" size="2" maxlength="2" name="<%=ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=4 maxlength=4 value="<%=lStrAAInizio%>" type="text" size="4" maxlength="4" name="<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        <br>
        <font class="ob">(ammessa data parziale)</font>
      </td>
      
    </div>
      
    <div id="divdata2" style="visibility:hidden; position:relative; width: 100%;">
      <td class="l">&lt;Data2&gt;</td>
      <td class="l">
<%
        String lStrGGFine = StringUtils.toStringJSP( reato.getGiornoFine());
        if (lStrGGFine.length() == 1)
          lStrGGFine = "0"+lStrGGFine;

        String lStrMMFine = StringUtils.toStringJSP( reato.getMeseFine());
        if (lStrMMFine.length() == 1)
          lStrMMFine = "0"+lStrMMFine;

        String lStrAAFine = StringUtils.toStringJSP( reato.getAnnoFine());
%>
        <input size=2 maxlength=2 value="<%=lStrGGFine%>" type="text" size="2" maxlength="2" name="<%=ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=2 maxlength=2 value="<%=lStrMMFine%>" type="text" size="2" maxlength="2" name="<%=ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=4 maxlength=4 value="<%=lStrAAFine%>" type="text" size="4" maxlength="4" name="<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        <br>
        <font class="ob">(ammessa data parziale)</font>
      </td>
      
    </div>  
      
    </tr>
  </table>
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Note Reato</td>
      <td class="l"><TextArea cols=80 rows=5 name="<%= ICostantiReatoCumulo.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(reato.getNote()) %></textarea></td>
    </tr> 
    <tr>
      <td class="l">Motivo Modifica Reato nel titolo cumulato</td>
      <td class="l"><TextArea cols=80 rows=3 name="<%=ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA%>"><%=StringUtils.toStringJSP(reato.getMotivoModificaNote()) %></textarea></td>
    </tr>
  </table>
<%
  }
%>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td colspan=2>
        <input type=submit value="Conferma" class=bottone>
      </td>
    </tr>
  </table>
  


</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadModificaReatoCumulo");

  frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNO_FONTE%>", "num");
  frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNO_FONTE%>", "minlength=4");
  frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>", "numeric");
  frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_NUMERO_FONTE%>", "alphanumeric");

  frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_COMMA%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_LETTERA%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_NUMERO%>", "alphanumeric");

<%
  //Se il reato è quello principale
  if(reato.getProgrCircostanza().intValue() == 1)
  {
%>
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>","lt=31");

    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>","lt=12");

    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>","lt=2050");

    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>","lt=31");

    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>","lt=12");

    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>","lt=2050");
    
    frmvalidator.setAddnlValidationFunction("ControlloObbligatorieta");
<%
  }
%>
</script>

 </body>
</html>