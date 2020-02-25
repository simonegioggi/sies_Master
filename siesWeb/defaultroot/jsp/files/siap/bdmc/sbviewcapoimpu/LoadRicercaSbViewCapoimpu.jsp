<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel"%>
<%@ page import="siap.bdmc.sbviewcapoimpu.action.ICostantiSbViewCapoimpu"%>
<jsp:useBean id="sbviewcapoimpu" scope="request" class="siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel"/>

<html>
<head>
  <title> Ricerca SbViewCapoimpu </title>
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
      if (document.LoadInserisciSbViewCapoimpu.<%="ICostantiSbViewCapoimpu.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciSbViewCapoimpu.<%="ICostantiSbViewCapoimpu.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo corretteza campo 'Data Reat 0101' 
      //=============================================================
      var data_to_verify = document.LoadRicercaSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0101%>.value+'/'+ 
                           document.LoadRicercaSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0101%>.value+'/'+ 
                           document.LoadRicercaSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0101%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Reat 0101 non corretta'); 
        document.LoadRicercaSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0101%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Reat 0202' 
      //=============================================================
      var data_to_verify = document.LoadRicercaSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0202%>.value+'/'+ 
                           document.LoadRicercaSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0202%>.value+'/'+ 
                           document.LoadRicercaSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0202%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Reat 0202 non corretta'); 
        document.LoadRicercaSbViewCapoimpu.<%=ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0202%>.focus(); 
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
                      <font class="campo">Ricerca SbViewCapoimpu</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaSbViewCapoimpu">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbviewcapoimpu.action.ActRicercaSbViewCapoimpu">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Flag Arti 0056</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0056()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0056 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0061</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0061()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0061 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti 0061 Comm</td>
      <td class="l"> 
        <input type="text" maxlength="35" size="35" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getArti0061Comm()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0061_COMM %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0081</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0081()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0081 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti 0081 Comm</td>
      <td class="l"> 
        <input type="text" maxlength="10" size="10" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getArti0081Comm()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0081_COMM %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Art 0110</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArt0110()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ART_0110 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0112</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0112()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0112 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti 0112 Commi</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getArti0112Commi()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ARTI_0112_COMMI %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0113</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0113()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0113 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0114</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0114()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0114 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0116</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0116()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0116 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0117</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0117()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_ARTI_0117 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Luog Reat</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getLuogReat()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_LUOG_REAT %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Peri Temp</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagPeriTemp()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_FLAG_PERI_TEMP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Reat 0101</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewcapoimpu.getDataReat0101(),"dd")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0101 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewcapoimpu.getDataReat0101(),"MM")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0101 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewcapoimpu.getDataReat0101(),"yyyy")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0101 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Reat 0202</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewcapoimpu.getDataReat0202(),"dd")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_GIORNO_DATA_REAT_0202 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewcapoimpu.getDataReat0202(),"MM")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_MESE_DATA_REAT_0202 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewcapoimpu.getDataReat0202(),"yyyy")) %>" 
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_DATA_REAT_0202 %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Desc Peri Temp</td>
      <td class="l"> 
        <input type="text" maxlength="200" size="200" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getDescPeriTemp()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_DESC_PERI_TEMP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Prog Capo Impu</td>
      <td class="l"> 
        <input type="text" maxlength="10" size="12" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getNumeProgCapoImpu()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_NUME_PROG_CAPO_IMPU %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Pren</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getIdPren()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ID_PREN %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getAnnoFascBdmc()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_ANNO_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getNumeFascBdmc()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_NUME_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Sede Inst</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getCodiSedeInst()) %>"
               name="<%= ICostantiSbViewCapoimpu.CAMPO_CODI_SEDE_INST %>"  
               > 
      </td> 
    </tr>

    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</FORM>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicercaSbViewCapoimpu");

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