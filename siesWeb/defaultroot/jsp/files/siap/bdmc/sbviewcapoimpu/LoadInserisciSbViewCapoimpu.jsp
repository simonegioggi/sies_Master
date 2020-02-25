<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel"%>
<%@ page import="siap.bdmc.sbviewcapoimpu.action.ICostantiSbViewCapoimpu"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbviewcapoimpu" scope="request" class="siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel"/>

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
  <title> Gestione SbViewCapoimpu </title>
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
      if (document.LoadInserisciSbViewCapoimpu.<%="ICostantiSbViewCapoimpu.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciSbViewCapoimpu.<%="ICostantiSbViewCapoimpu.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data Reat 0101' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0101%>.value+'/'+ 
                           document.LoadInserisciSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0101%>.value+'/'+ 
                           document.LoadInserisciSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0101%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Reat 0101 non corretta'); 
        document.LoadInserisciSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0101%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Reat 0202' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0202%>.value+'/'+ 
                           document.LoadInserisciSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0202%>.value+'/'+ 
                           document.LoadInserisciSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0202%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Reat 0202 non corretta'); 
        document.LoadInserisciSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0202%>.focus(); 
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
        SbViewCapoimpuModel lSbViewCapoimpu = new SbViewCapoimpuModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.bdmc.sbviewcapoimpu.action.ActInserisciSbViewCapoimpu"; 
        %>
        <font class="campo">Inserimento SbViewCapoimpu</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.bdmc.sbviewcapoimpu.action.ActModificaSbViewCapoimpu";
          lSbViewCapoimpu = sbviewcapoimpu;
        %>
        <font class="campo">Modifica SbViewCapoimpu</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciSbViewCapoimpu">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Flag Arti 0056 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0056()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0056 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0061 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0061()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0061 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti 0061 Comm</td>
      <td class="l"> 
        <input type="text" maxlength="35" size="35" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getArti0061Comm()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0061_COMM %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0081 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0081()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0081 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti 0081 Comm</td>
      <td class="l"> 
        <input type="text" maxlength="10" size="10" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getArti0081Comm()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0081_COMM %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Art 0110 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArt0110()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ART_0110 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0112 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0112()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0112 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti 0112 Commi</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getArti0112Commi()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0112_COMMI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0113 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0113()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0113 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0114</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0114()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0114 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0116 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0116()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0116 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0117 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagArti0117()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0117 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Luog Reat</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getLuogReat()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_LUOG_REAT %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Peri Temp (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getFlagPeriTemp()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_PERI_TEMP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Reat 0101</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewCapoimpu.getDataReat0101(),"dd")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0101 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewCapoimpu.getDataReat0101(),"MM")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0101 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewCapoimpu.getDataReat0101(),"yyyy")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0101 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Reat 0202</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewCapoimpu.getDataReat0202(),"dd")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0202 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewCapoimpu.getDataReat0202(),"MM")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0202 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewCapoimpu.getDataReat0202(),"yyyy")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0202 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Desc Peri Temp</td>
      <td class="l"> 
        <input type="text" maxlength="200" size="200" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getDescPeriTemp()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_DESC_PERI_TEMP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Prog Capo Impu (*)</td>
      <td class="l"> 
        <input type="text" maxlength="10" size="12" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getNumeProgCapoImpu()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_NUME_PROG_CAPO_IMPU %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Pren (*)</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getIdPren()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ID_PREN %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getAnnoFascBdmc()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getNumeFascBdmc()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_NUME_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Sede Inst (*)</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewCapoimpu.getCodiSedeInst()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_CODI_SEDE_INST %>"  
               <%=readonly%> 
               > 
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
  var frmvalidator  = new Validator("LoadInserisciSbViewCapoimpu");

  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0056 %>","req","Il campo Flag Arti 0056 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0061 %>","req","Il campo Flag Arti 0061 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0081 %>","req","Il campo Flag Arti 0081 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ART_0110 %>","req","Il campo Flag Art 0110 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0112 %>","req","Il campo Flag Arti 0112 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0113 %>","req","Il campo Flag Arti 0113 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0116 %>","req","Il campo Flag Arti 0116 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0117 %>","req","Il campo Flag Arti 0117 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_PERI_TEMP %>","req","Il campo Flag Peri Temp è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_NUME_PROG_CAPO_IMPU %>","req","Il campo Nume Prog Capo Impu è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ID_PREN %>","req","Il campo Id Pren è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_FASC_BDMC %>","req","Il campo Anno Fasc Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_NUME_FASC_BDMC %>","req","Il campo Nume Fasc Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_CODI_SEDE_INST %>","req","Il campo Codi Sede Inst è obbligatorio");

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

  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0056 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0061 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0061_COMM %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0081 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0081_COMM %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ART_0110 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0112 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0112_COMMI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0113 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0114 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0116 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0117 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_LUOG_REAT %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_PERI_TEMP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0101 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0101 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0101 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0202 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0202 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0202 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_DESC_PERI_TEMP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_NUME_PROG_CAPO_IMPU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ID_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_NUME_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewCapoimpu.CAMPO_CODI_SEDE_INST %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>