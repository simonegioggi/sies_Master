<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbperipren.model.SbPeriprenModel"%>
<%@ page import="siap.bdmc.sbperipren.action.ICostantiSbPeripren"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbperipren" scope="request" class="siap.bdmc.sbperipren.model.SbPeriprenModel"/>

<% // n.b. JSP NON UTILIZZATA (13/03/2009) %>

<% 
//=========================================================================== 
// Inserire la condizione in base alla quale i campi non sono modificabili 
// (se esiste) 
//=========================================================================== 
String readonly = ""; 
//if ( modalita.equals("M" && ?????) ){ 
//  readonly  = "readonly=readonly"; 
//} 
%> 

<html>
<head>
  <title> Gestione SbPeripren </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      /* Esempio:
      if (document.LoadInserisciSbPeripren.<%="ICostantiSbPeripren.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciSbPeripren.<%="ICostantiSbPeripren.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data Iniz Peri' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_GIORNO_DATA_INIZ_PERI%>.value+'/'+ 
                           document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_MESE_DATA_INIZ_PERI%>.value+'/'+ 
                           document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_ANNO_DATA_INIZ_PERI%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Iniz Peri non corretta'); 
        document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_GIORNO_DATA_INIZ_PERI%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Fine Peri' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_GIORNO_DATA_FINE_PERI%>.value+'/'+ 
                           document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_MESE_DATA_FINE_PERI%>.value+'/'+ 
                           document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_ANNO_DATA_FINE_PERI%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Fine Peri non corretta'); 
        document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_GIORNO_DATA_FINE_PERI%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Pren Peri' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_GIORNO_DATA_PREN_PERI%>.value+'/'+ 
                           document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_MESE_DATA_PREN_PERI%>.value+'/'+ 
                           document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_ANNO_DATA_PREN_PERI%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Pren Peri non corretta'); 
        document.LoadInserisciSbPeripren.<%=ICostantiSbPeripren.CAMPO_GIORNO_DATA_PREN_PERI%>.focus(); 
        return false; 
      } 

      <%  if( modalita.equals("I") )  {%> 
      var msgConfirm = "Si vuole procedere con l'inserimento dei dati?"; 
      <%} else if( modalita.equals("M") ) { %> 
      var msgConfirm = "Si vuole procedere con la modifica dei dati?"; 
      <%}%> 
      if (window.confirm(msgConfirm)) 
        return true; 
      else 
        return false; 
    } 
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        SbPeriprenModel lSbPeripren = new SbPeriprenModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.bdmc.sbperipren.action.ActInserisciSbPeripren"; 
        %>
        <font class="campo">Inserimento SbPeripren</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.bdmc.sbperipren.action.ActModificaSbPeripren";
          lSbPeripren = sbperipren;
        %>
        <font class="campo">Modifica SbPeripren</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciSbPeripren">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Data Iniz Peri (*)</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataInizPeri(),"dd")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_GIORNO_DATA_INIZ_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataInizPeri(),"MM")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_MESE_DATA_INIZ_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataInizPeri(),"yyyy")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_ANNO_DATA_INIZ_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Fine Peri (*)</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataFinePeri(),"dd")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_GIORNO_DATA_FINE_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataFinePeri(),"MM")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_MESE_DATA_FINE_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataFinePeri(),"yyyy")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_ANNO_DATA_FINE_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Prog Peri Pres (*)</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getProgPeriPres()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_PROG_PERI_PRES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Pren (*)</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getIdPren()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_ID_PREN %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Uffi Sies</td>
      <td class="l"> 
        <input type="text" maxlength="18" size="18" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getCodiUffiSies()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_COD_UFFI_SIES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Siep</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getAnnoFascSiep()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_ANNO_FASC_SIEP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Siep</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getNumeFascSiep()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_NUME_FASC_SIEP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Sede Inst (*)</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getCodiSedeInst()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_CODI_SEDE_INST %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getAnnoFascBdmc()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_ANNO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getNumeFascBdmc()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_NUME_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Stat Pren Peri</td>
      <td class="l"> 
        <input type="text" maxlength="3" size="3" 
               value="<%=StringUtils.toStringJSP(lSbPeripren.getCodStatPrenPeri()) %>"
               name="<%= ICostantiSbPeripren.CAMPO_COD_STAT_PREN_PERI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>

    <tr>
      <td class="l">Data Pren Peri</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataPrenPeri(),"dd")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_GIORNO_DATA_PREN_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataPrenPeri(),"MM")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_MESE_DATA_PREN_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPeripren.getDataPrenPeri(),"yyyy")) %>" 
               name="<%= ICostantiSbPeripren.CAMPO_ANNO_DATA_PREN_PERI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciSbPeripren");

  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_GIORNO_DATA_INIZ_PERI %>","req","Il campo Data Iniz Peri è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_MESE_DATA_INIZ_PERI %>","req","Il campo Data Iniz Peri è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ANNO_DATA_INIZ_PERI %>","req","Il campo Data Iniz Peri è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_GIORNO_DATA_FINE_PERI %>","req","Il campo Data Fine Peri è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_MESE_DATA_FINE_PERI %>","req","Il campo Data Fine Peri è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ANNO_DATA_FINE_PERI %>","req","Il campo Data Fine Peri è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_PROG_PERI_PRES %>","req","Il campo Prog Peri Pres è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ID_PREN %>","req","Il campo Id Pren è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_CODI_SEDE_INST %>","req","Il campo Codi Sede Inst è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ANNO_FASC_BDMC %>","req","Il campo Anno Fasc Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_NUME_FASC_BDMC %>","req","Il campo Nume Fasc Bdmc è obbligatorio");

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

  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_GIORNO_DATA_INIZ_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_MESE_DATA_INIZ_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ANNO_DATA_INIZ_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_GIORNO_DATA_FINE_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_MESE_DATA_FINE_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ANNO_DATA_FINE_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_PROG_PERI_PRES %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ID_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_COD_UFFI_SIES %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ANNO_FASC_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_NUME_FASC_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_CODI_SEDE_INST %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_NUME_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_COD_STAT_PREN_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_COD_TIPO_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_GIORNO_DATA_PREN_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_MESE_DATA_PREN_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPeripren.CAMPO_ANNO_DATA_PREN_PERI %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>