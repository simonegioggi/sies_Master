<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.util.SIESSwitch" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="modalita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="reato"       scope="request" class="siap.siep.modulocumulo.model.ReatoCumuloModel"/>

<jsp:useBean id="TipiReato"            scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="PeriodoConsumazione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiPeneDetentive"    scope="request" class="java.lang.String"/>


<html>
<head>
<title>[S.I.E.S.] - Gestione Reato (Cumulo) </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
  var desktop;
  function ListaReatiPredisposti(a_formname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reatopredisposto.action.ActRicercaReatoPredisposto&formname="+a_formname, "Ricerca_Reati_Predisposti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaCopiaReati(a_formname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadRicercaReatiFascicoloFramset&formname="+a_formname, "Ricerca_Reati_Fascicolo","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=400");
  }
       
  //==========================================================================
  // Ritorna alla lista dei Reati per il Titolo
  //==========================================================================
  function eseguiFunzione(action)
  {
    document.LoadInserisciReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.LoadInserisciReatoCumulo.submit();
  }
      
  //****************************************************************************************************
  // Ambrosino 05/2010
  //aggiunta funzione di controllo dei campi
  // come da mev   a9-rr-078 

  function ControlloObbligatorieta()
  {

    // se selezionato un reato (cosa controllata nella ActInserisciReato), deve esserci 
    // obbligatoriamente almeno un articolo e la fonte
    var presenza_articolo=0;
    var ind;
    for (ind=0; ind<5; ind++)
    {
        if (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>[ind].value!="-"
         || document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>[ind].value.length>0)
        {
            presenza_articolo=1;
        }

        if ( (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>[ind].value!="-"
             && document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>[ind].value.length==0)
            || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>[ind].value=="-"
             && document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>[ind].value.length>0) )
        {
            alert("Fonte/Articolo devono essere entrambi presenti");
              document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>[ind].focus();
            return false;
        }

        if (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>[ind].value=="-"
         && document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>[ind].value.length==0)  
        {
            // se viene digitato qualcosa sulle righe di definizione dell'articolo,
            // è obbligatorio riempirle correttamente
            if ((document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_FONTE%>[ind].value.length>0)
             || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_NUMERO_FONTE%>[ind].value.length>0)
             || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_SOTTONUMERAZIONE%>[ind].value!="-")
             || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-")
             || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COMMA%>[ind].value.length>0)
             || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_LETTERA%>[ind].value.length>0)
             || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_NUMERO%>[ind].value.length>0))
            { 
                alert("Fonte/Articolo obbligatori!");
                  document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>[ind].focus();
                return false;
            }
        }

      // controllo obbligatorietà comma 
        if (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COMMA%>[ind].value==""
         && document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-") 
        {
            alert("Comma obbligatorio");
              document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COMMA%>[ind].focus();
            return false;
        }

    } // fine for

    if (presenza_articolo=="0") 
    {
      alert("Fonte/Articolo obbligatori!");
        document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_FONTE%>[0].focus();
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
    if (
      (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="04")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="07")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="09")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="10")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="17")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="23")
       )
    {
        if (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.value.length==0    
         || document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>value.length==0)
        {
	          alert("Data1 e Data2 devono essere entrambe presenti!");
              document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.focus();
            return false;
        }
    } // fine if

    // *********************************************************************************************
    // per i seguenti valori devono essere presente solo data1
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
    if (
      (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="01")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="02")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="03")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="05")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="06")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="08")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="11")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="12")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="15")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="18")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="19")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="20")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="22")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="24")
     || (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="25")
       )
    {
        if (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.value.length==0)   
        {
            alert("Data1 obbligatoria");
              document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.focus();
            return false;
        }
    } // fine if

    // -> i valori 16 = Accertato e 21 = Commesso NON RICHIEDONO DATA COMMESSO REATO
    // -> i valori 13 e 14 NON SONO PREVISTI
    
    // se inserisco il luogo commesso reato è obbligatorio indicare un periodo di consumazione
	if ((document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="-")
	&& (document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_DESC_LUOGO%>.value!=""))
 	{
		alert("indicare un periodo di consumazione se si vuole inserire il luogo commesso reato");
	    document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.focus();
		return false;
 	}

    return true;

  } // fine funzione ControlloObbligatorieta()
  //****************************************************************************************************
      
  	function mettidata()
 	{
 		var valueSel = document.LoadInserisciReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value;

      	var nodedata1 = document.getElementById('divdata1');
      	var nodedata2 = document.getElementById('divdata2');
      
      	if(valueSel == '01' || valueSel == '02' || valueSel == '03' || valueSel == '05' || valueSel == '06' || 
      	   valueSel == '08' || valueSel == '11' || valueSel == '12' || valueSel == '15' || valueSel == '18' || 
      	   valueSel == '19' || valueSel == '20' || valueSel == '22' || valueSel == '24' || valueSel == '25') 
      	{
           	nodedata1.style.visibility='visible';
           	nodedata2.style.visibility='visible';
           	
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>.disabled = false;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>.disabled = false;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.disabled = false;   
			
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.disabled = true;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.disabled = true;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.disabled = true;	
			
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.value = "";
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.value = "";
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.value = "";			        	
      	}
      	
	  	if(valueSel == '04' || valueSel == '07' || valueSel == '09' || valueSel == '10' || 
	  	   valueSel == '17' || valueSel == '23')
		{
            nodedata1.style.visibility='visible';
            nodedata2.style.visibility='visible';

			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>.disabled = false;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>.disabled = false;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.disabled = false;   
            
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.disabled = false;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.disabled = false;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.disabled = false;	
        }
        
        if(valueSel == '-' || valueSel == '16' || valueSel == '21')
		{
            nodedata1.style.visibility='visible';
            nodedata2.style.visibility='visible';

			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>.disabled = true;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>.disabled = true;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.disabled = true;   
            
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.disabled = true;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.disabled = true;
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.disabled = true;	
			
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_INIZIO%>.value = "";
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_INIZIO%>.value = "";
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_INIZIO%>.value = "";   
            
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>.value = "";
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>.value = "";
			document.LoadInserisciReatoCumulo.<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>.value = "";
        }       

	}
           
  </script>
</head> 
     
  <body class="corpo" onLoad="mettidata()">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
          
<%
          ReatoCumuloModel lReatoCum = new ReatoCumuloModel();
          String lAzione = new String();
          lAzione = "siap.siep.modulocumulo.action.ActInserisciReatoCumulo";
%>
          <font class="campo">Inserimento Reato relativo al Titolo Cumulato</font>
      
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
  
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciReatoCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">

  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="HIDDEN" name=<%= ICostantiReatoCumulo.CAMPO_ID_REATO_CUM%> value="<%=lReatoCum.getIdReatoCum()%>">
  
  

  <input type="Hidden" name="cablati"  value="" >
  <input type="Hidden" name="cablati2" value="" >
  
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo" colspan=9>Reato</td></tr>
  </table>
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Numero Reato</td>
      <td class="c">
        <input size=10 maxlength=10 value="<%=StringUtils.toStringJSP(lReatoCum.getProgrNumeroManuale()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_PROGR_NUMERO_MANUALE %>">
      </td>

      <% if (siap.util.SIESSwitch.isRegeSiesOn()) { %>
      <td class="L"><font class="campo">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Reati Predisposti</font>          
        <a href="Javascript:ListaReatiPredisposti('LoadInserisciReatoCumulo');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>    
      <% } %>   
    </tr>   
  </table>
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="r"><strong>110 CP</strong>&nbsp;<input type="checkbox" name="cablati2" value="110 01"></td>
      <td class="r"><strong>56 CP</strong>&nbsp;<input type="checkbox" name="cablati2" value="56 01"></td>
      <td class="r">
          <strong>81 CP</strong>&nbsp;
          <strong>C1</strong>&nbsp;<input type="checkbox" name="cablati2" value="81 01 C1">
          <strong>C2</strong>&nbsp;<input type="checkbox" name="cablati2" value="81 01 C2">
      </td>
    </tr>
  </table>
  
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr>
        <td class="int">Fonte</td>
        <td class="int">Anno</td>
        <td class="int">Numero</td>
        <td class="int">Articolo</td>
        <td class="int">Art.qualificante</td>
        <td class="int">Comma</td>
        <td class="int">CommaQual</td>
        <td class="int">Lettera</td>
        <td class="int">Numero</td>
    </tr>
    
<%
    for(int i=0; i<5; i++)
    {
%>
    <tr>
      <td class="c">
        <select name="<%= ICostantiReatoCumulo.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="c">
        <input size="4" maxlength="4" title="Anno Fonte" 
               value="<%=StringUtils.toStringJSP(lReatoCum.getAnnoFonte()) %>" type="text" 
               name="<%= ICostantiReatoCumulo.CAMPO_ANNO_FONTE %>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               >
      </td>
      <td class="c">
        <input size=6 maxlength=6 title="Numero Fonte" value="<%=StringUtils.toStringJSP(lReatoCum.getNumeroFonte()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_NUMERO_FONTE %>">
      </td>
      <td class="c">
        <input type="text"  size="5" maxlength="5" title="Articolo Fonte" 
               value="<%=StringUtils.toStringJSP(lReatoCum.getArticolo()) %>" 
               name="<%= ICostantiReatoCumulo.CAMPO_ARTICOLO %>"
               onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="c">
        <select name="<%= ICostantiReatoCumulo.CAMPO_COD_SOTTONUMERAZIONE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      <td class="c">
         <input size=10 maxlength=10 title="Comma" value="<%=StringUtils.toStringJSP(lReatoCum.getComma())%>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_COMMA %>">
      </td>
      
<%
  //Ambrosino 05/2010
  //come da MEV a9-rr-078
%>
      <td class="c">
        <select name="<%= ICostantiReatoCumulo.CAMPO_COMMA_QUALIFICANTE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>      
      
      <td class="c">
        <input size=2 maxlength=2 title="Lettera" value="<%=StringUtils.toStringJSP(lReatoCum.getLettera()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_LETTERA %>">
      </td>
      <td class="c">
       <input size=2 maxlength=2 title="Numero" value="<%=StringUtils.toStringJSP(lReatoCum.getNumero()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_NUMERO %>">
      </td>
    </tr>
<%
   }
%>
  </table>
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="r">
        <strong>61 CP</strong>&nbsp;
        <strong>N1</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N1">
        <strong>N2</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N2">
        <strong>N3</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N3">
        <strong>N4</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N4">
        <strong>N5</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N5">
        <strong>N6</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N6">
        <strong>N7</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N7">
        <strong>N8</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N8">
        <strong>N9</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N9">
        <strong>N10</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N10">
        <strong>N11</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N11">
      </td>
    </tr>
  </table>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="r">
        <strong>112 CP</strong>&nbsp;
        <strong>C1</strong>&nbsp;<input type="checkbox" name="cablati" value="112 01 C1">
        <strong>C2</strong>&nbsp;<input type="checkbox" name="cablati" value="112 01 C2">
        <strong>C3</strong>&nbsp;<input type="checkbox" name="cablati" value="112 01 C3">
        <strong>C4</strong>&nbsp;<input type="checkbox" name="cablati" value="112 01 C4">
      </td>
      <td class="r"><strong>113 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="113 01"></td>
      <td class="r"><strong>114 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="114 01"></td>
      <td class="r"><strong>116 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="116 01"></td>
      <td class="r"><strong>117 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="117 01"></td>
    </tr>
  </table>
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="r">
        <strong>625 CP</strong>&nbsp;
        <strong>N1</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N1">
        <strong>N2</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N2">
        <strong>N3</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N3">
        <strong>N4</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N4">
        <strong>N5</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N5">
        <strong>N6</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N6">
        <strong>N7</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N7">
        <strong>N8</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N8">
        <strong>N9</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N9">
        <strong>N10</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N10">
        <strong>N11</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N11">
      </td>
    </tr>
  </table>
  
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
      <td class="l"><input size=50 maxlength=300 value="<%=StringUtils.toStringJSP(lReatoCum.getDescLuogo()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_DESC_LUOGO %>"  ></td>
    </tr>
    
    <tr>
      <td class="l">Periodo Consumazione</td>
      <td class="l">
        <select name="<%= ICostantiReatoCumulo.CAMPO_COD_PERIODO_CONSUMAZIONE %>" onChange="mettidata()" >
          <%=PeriodoConsumazione%>
        </select>
      </td>
    </tr>
  </table>
  
  <table cellspacing=2 cellpadding=2>
    <tr>
    
    <div id="divdata1" style="visibility:hidden; position:relative; width: 100%;">
    	
      <td class="l">&lt;Data1&gt;</td>
      <td class="l">
<%
        String lStrGGInizio = StringUtils.toStringJSP( lReatoCum.getGiornoInizio());
        if (lStrGGInizio.length() == 1)
          lStrGGInizio = "0"+lStrGGInizio;

        String lStrMMInizio = StringUtils.toStringJSP( lReatoCum.getMeseInizio());
        if (lStrMMInizio.length() == 1)
          lStrMMInizio = "0"+lStrMMInizio;

        String lStrAAInizio = StringUtils.toStringJSP( lReatoCum.getAnnoInizio());
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
        String lStrGGFine = StringUtils.toStringJSP( lReatoCum.getGiornoFine());
        if (lStrGGFine.length() == 1)
          lStrGGFine = "0"+lStrGGFine;

        String lStrMMFine = StringUtils.toStringJSP( lReatoCum.getMeseFine());
        if (lStrMMFine.length() == 1)
          lStrMMFine = "0"+lStrMMFine;

        String lStrAAFine = StringUtils.toStringJSP( lReatoCum.getAnnoFine());
%>
        <input size=2 maxlength=2 value="<%=lStrGGFine%>" type="text" size="2" maxlength="2" name="<%= ICostantiReatoCumulo.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=2 maxlength=2 value="<%=lStrMMFine%>" type="text" size="2" maxlength="2" name="<%= ICostantiReatoCumulo.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size=4 maxlength=4 value="<%=lStrAAFine%>" type="text" size="4" maxlength="4" name="<%= ICostantiReatoCumulo.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        <br>
        <font class="ob">(ammessa data parziale)</font>
      </td>
      
    </div>
      
    </tr>
  </table>
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Note Reato</td>
      <td class="l"><TextArea cols=80 rows=5 name="<%= ICostantiReatoCumulo.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(lReatoCum.getNote()) %></textarea></td>
    </tr>
    <tr>
        <td class="l">Motivo Inserimento Reato nel titolo cumulato</td>
        <td class="l"><TextArea cols=80 rows=3 name="<%= ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA %>"><%=StringUtils.toStringJSP(lReatoCum.getMotivoModificaNote()) %></textarea></td>
    </tr>     
    <tr>
      <td colspan=2>
        <input type=submit value="Conferma" class=bottone>
      </td>
    </tr>
  </table>

</form>


<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciReatoCumulo");

<%
  for(int i=0;i<5;i++)
  {
%>
     frmvalidator.addValidationWithIdx("<%=ICostantiReatoCumulo.CAMPO_ANNO_FONTE%>","<%=i%>","num");
     frmvalidator.addValidationWithIdx("<%=ICostantiReatoCumulo.CAMPO_ANNO_FONTE%>","<%=i%>","minlength=4");

     frmvalidator.addValidationWithIdx("<%=ICostantiReatoCumulo.CAMPO_NUMERO_FONTE%>","<%=i%>","alphanumeric");
     frmvalidator.addValidationWithIdx("<%=ICostantiReatoCumulo.CAMPO_COMMA%>","<%=i%>","alphanumeric");
     
     frmvalidator.addValidationWithIdx("<%=ICostantiReatoCumulo.CAMPO_ARTICOLO%>","<%=i%>","numeric");

     frmvalidator.addValidationWithIdx("<%=ICostantiReatoCumulo.CAMPO_LETTERA%>","<%=i%>","alphanumeric");
     frmvalidator.addValidationWithIdx("<%=ICostantiReatoCumulo.CAMPO_NUMERO%>","<%=i%>","alphanumeric");
<%
  }
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

 </script>
</body>
</html>