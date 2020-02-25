<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel"%>
<%@ page import="siap.bdmc.statoprenotazionibdmc.action.ICostantiStatoPrenotazioniBdmc"%>
<jsp:useBean id="statoprenotazionibdmc" scope="request" class="siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel"/>

<html>
<head>
  <title> Ricerca Stato Trasmissioni verso Bdmc </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      /* Esempio:
      if (document.LoadInserisciStatoPrenotazioniBdmc.<%="ICostantiStatoPrenotazioniBdmc.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciStatoPrenotazioniBdmc.<%="ICostantiStatoPrenotazioniBdmc.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo corretteza campo 'Data Trasmissione' 
      //=============================================================
      var data_to_verify = document.LoadRicercaStatoPrenotazioniBdmc.<%=ICostantiStatoPrenotazioniBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+ 
                           document.LoadRicercaStatoPrenotazioniBdmc.<%=ICostantiStatoPrenotazioniBdmc.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+ 
                           document.LoadRicercaStatoPrenotazioniBdmc.<%=ICostantiStatoPrenotazioniBdmc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Trasmissione non corretta'); 
        document.LoadRicercaStatoPrenotazioniBdmc.<%=ICostantiStatoPrenotazioniBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.focus(); 
        return false; 
      } 

    } 
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Ricerca Stato Trasmissioni verso Bdmc</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaStatoPrenotazioniBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.statoprenotazionibdmc.action.ActRicercaStatoPrenotazioniBdmc">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
      <tr>
      <td class="l">Data Trasmissione</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(statoprenotazionibdmc.getDataTrasmissione(),"dd")) %>" 
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;-&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(statoprenotazionibdmc.getDataTrasmissione(),"MM")) %>" 
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_MESE_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;-&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(statoprenotazionibdmc.getDataTrasmissione(),"yyyy")) %>" 
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ANNO_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Esito</td>
      <td class="l"> 
  
         <select Title="Tipo Trasmissione" name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ESITO_ID%>" >
 
		 <option value = "-" selected />Tutti
		 <option value = "0"  />Positivo
		 <option value = "1"  />Negativo
		

      </select>
    </td>
    
    </tr>
    <tr>
      <td class="l">Numero Prenotazione</td>
      <td class="l"> 
        <input type="text"  size="15" 
               value="<%=StringUtils.toStringJSP(statoprenotazionibdmc.getIdPrenotazione()) %>"
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ID_PRENOTAZIONE %>"  
               > 
      </td> 
    </tr>
    
    <tr>
      <td class="l">Tipo Trasmissione</td>
      <td class="l"> 
      
      
         <select Title="Tipo Trasmissione" name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_TIPO_TRASMISSIONE %>" >
 
		 <option value = "-" selected />Tutti
		 <option value = "C"  />Annullamento Provvedimenti
		 <option value = "A"  />Associazione Provvedimenti
		 <option value = "I"  />Iscrizione Periodi
		 <option value = "V"  />Trasmissione Provvedimenti

      </select>
    </td>
    </tr>
<tr><td> <br> </td></tr>
    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Ricerca">
      </td>
    </tr>

  </table>
</FORM>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicercaStatoPrenotazioniBdmc");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");

  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_STATO_PRENOTAZIONI_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ID_MISURA_CAUTELARE_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_MESE_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ANNO_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ESITO_ID %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ESITO_MSG %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ID_PRENOTAZIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_PROG_PERI_PRES %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_TIPO_TRASMISSIONE %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>