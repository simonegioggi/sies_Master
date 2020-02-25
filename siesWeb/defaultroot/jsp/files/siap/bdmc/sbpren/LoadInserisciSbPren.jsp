<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbpren.model.SbPrenModel"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbpren" scope="request" class="siap.bdmc.sbpren.model.SbPrenModel"/>

<% // n.b. JSP NON UTILIZZATA generata dal codegenerator (13/03/2009) %>

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
  <title> Gestione SbPren </title>
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
      if (document.LoadInserisciSbPren.<%="ICostantiSbPren.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciSbPren.<%="ICostantiSbPren.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data Pren' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_PREN%>.value+'/'+ 
                           document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_MESE_DATA_PREN%>.value+'/'+ 
                           document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_ANNO_DATA_PREN%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Pren non corretta'); 
        document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_PREN%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Annu' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_ANNU%>.value+'/'+ 
                           document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_MESE_DATA_ANNU%>.value+'/'+ 
                           document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_ANNO_DATA_ANNU%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Annu non corretta'); 
        document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_ANNU%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Nasc' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_NASC%>.value+'/'+ 
                           document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_MESE_DATA_NASC%>.value+'/'+ 
                           document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_ANNO_DATA_NASC%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Nasc non corretta'); 
        document.LoadInserisciSbPren.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_NASC%>.focus(); 
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
        SbPrenModel lSbPren = new SbPrenModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.bdmc.sbpren.action.ActInserisciSbPren"; 
        %>
        <font class="campo">Inserimento SbPren</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.bdmc.sbpren.action.ActModificaSbPren";
          lSbPren = sbpren;
        %>
        <font class="campo">Modifica SbPren</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciSbPren">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Data Pren (*)</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataPren(),"dd")) %>" 
               name="<%= ICostantiSbPren.CAMPO_GIORNO_DATA_PREN %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataPren(),"MM")) %>" 
               name="<%= ICostantiSbPren.CAMPO_MESE_DATA_PREN %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataPren(),"yyyy")) %>" 
               name="<%= ICostantiSbPren.CAMPO_ANNO_DATA_PREN %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Id Pren (*)</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPren.getIdPren()) %>"
               name="<%= ICostantiSbPren.CAMPO_ID_PREN %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Uten Sies (*)</td>
      <td class="l"> 
        <input type="text" maxlength="20" size="20" 
               value="<%=StringUtils.toStringJSP(lSbPren.getUtenSies()) %>"
               name="<%= ICostantiSbPren.CAMPO_UTEN_SIES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="l"> 
        <input type="text" maxlength="250" size="250" 
               value="<%=StringUtils.toStringJSP(lSbPren.getNote()) %>"
               name="<%= ICostantiSbPren.CAMPO_NOTE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Annu</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataAnnu(),"dd")) %>" 
               name="<%= ICostantiSbPren.CAMPO_GIORNO_DATA_ANNU %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataAnnu(),"MM")) %>" 
               name="<%= ICostantiSbPren.CAMPO_MESE_DATA_ANNU %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataAnnu(),"yyyy")) %>" 
               name="<%= ICostantiSbPren.CAMPO_ANNO_DATA_ANNU %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Moti Annu</td>
      <td class="l"> 
        <input type="text" maxlength="250" size="250" 
               value="<%=StringUtils.toStringJSP(lSbPren.getMotiAnnu()) %>"
               name="<%= ICostantiSbPren.CAMPO_MOTI_ANNU %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Pren (*)</td>
      <td class="l"> 
        <input type="text" maxlength="3" size="3" 
               value="<%=StringUtils.toStringJSP(lSbPren.getFlagPrenPres()) %>"
               name="<%= ICostantiSbPren.CAMPO_FLAG_PREN %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Sele Capo Impu (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="3" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPren.getFlagSeleCapoImpu()) %>"
               name="<%= ICostantiSbPren.CAMPO_FLAG_SELE_CAPO_IMPU %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Sele Proc Pena (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="3" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPren.getFlagSeleProcPena()) %>"
               name="<%= ICostantiSbPren.CAMPO_FLAG_SELE_PROC_PENA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>

    <tr>
      <td class="l">Flag Pren Pres (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="3" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPren.getFlagPrenPres()) %>"
               name="<%= ICostantiSbPren.CAMPO_FLAG_PREN_PRES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi Sies (*)</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbPren.getCodiUffiSies()) %>"
               name="<%= ICostantiSbPren.CAMPO_CODI_UFFI_SIES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Sele Peri Comp (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="3" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPren.getFlagSelePeriComp()) %>"
               name="<%= ICostantiSbPren.CAMPO_FLAG_SELE_PERI_COMP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPren.getNumeFascBdmc()) %>"
               name="<%= ICostantiSbPren.CAMPO_NUME_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPren.getAnnoFascBdmc()) %>"
               name="<%= ICostantiSbPren.CAMPO_ANNO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Sede Inst (*)</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbPren.getCodiSedeInst()) %>"
               name="<%= ICostantiSbPren.CAMPO_CODI_SEDE_INST %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cogn Sogg (*)</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(lSbPren.getCognSogg()) %>"
               name="<%= ICostantiSbPren.CAMPO_COGN_SOGG %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nome Sogg (*)</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(lSbPren.getNomeSogg()) %>"
               name="<%= ICostantiSbPren.CAMPO_NOME_SOGG %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Sess (*)</td>
      <td class="l"> 
        <input type="text" maxlength="3" size="3" 
               value="<%=StringUtils.toStringJSP(lSbPren.getFlagSess()) %>"
               name="<%= ICostantiSbPren.CAMPO_FLAG_SESS %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Stat</td>
      <td class="l"> 
        <input type="text" maxlength="3" size="3" 
               value="<%=StringUtils.toStringJSP(lSbPren.getCodiStat()) %>"
               name="<%= ICostantiSbPren.CAMPO_CODI_STAT %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Luog Nasc</td>
      <td class="l"> 
        <input type="text" maxlength="70" size="70" 
               value="<%=StringUtils.toStringJSP(lSbPren.getLuogNasc()) %>"
               name="<%= ICostantiSbPren.CAMPO_LUOG_NASC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Iden Afis</td>
      <td class="l"> 
        <input type="text" maxlength="16" size="16" 
               value="<%=StringUtils.toStringJSP(lSbPren.getCodiIdenAfis()) %>"
               name="<%= ICostantiSbPren.CAMPO_CODI_IDEN_AFIS %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Nasc</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataNasc(),"dd")) %>" 
               name="<%= ICostantiSbPren.CAMPO_GIORNO_DATA_NASC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataNasc(),"MM")) %>" 
               name="<%= ICostantiSbPren.CAMPO_MESE_DATA_NASC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbPren.getDataNasc(),"yyyy")) %>" 
               name="<%= ICostantiSbPren.CAMPO_ANNO_DATA_NASC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Flag Sele Circ Sogg</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbPren.getFlagSeleCircSogg()) %>"
               name="<%= ICostantiSbPren.CAMPO_FLAG_SELE_CIRC_SOGG %>"  
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
  var frmvalidator  = new Validator("LoadInserisciSbPren");

  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_PREN %>","req","Il campo Data Pren è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_PREN %>","req","Il campo Data Pren è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_PREN %>","req","Il campo Data Pren è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ID_PREN %>","req","Il campo Id Pren è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_UTEN_SIES %>","req","Il campo Uten Sies è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_PREN %>","req","Il campo Flag Pren è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_CAPO_IMPU %>","req","Il campo Flag Sele Capo Impu è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_PROC_PENA %>","req","Il campo Flag Sele Proc Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_SENT %>","req","Il campo Flag Sele Sent è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_PREN_PRES %>","req","Il campo Flag Pren Pres è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_CODI_UFFI_SIES %>","req","Il campo Codi Uffi Sies è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_PERI_COMP %>","req","Il campo Flag Sele Peri Comp è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_NUME_FASC_BDMC %>","req","Il campo Nume Fasc Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_FASC_BDMC %>","req","Il campo Anno Fasc Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_CODI_SEDE_INST %>","req","Il campo Codi Sede Inst è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_COGN_SOGG %>","req","Il campo Cogn Sogg è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_NOME_SOGG %>","req","Il campo Nome Sogg è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SESS %>","req","Il campo Flag Sess è obbligatorio");

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

  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ID_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_UTEN_SIES %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_NOTE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_ANNU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_ANNU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_ANNU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MOTI_ANNU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_CAPO_IMPU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_PROC_PENA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_SENT %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_PREN_PRES %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_CODI_UFFI_SIES %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_PERI_COMP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_NUME_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_CODI_SEDE_INST %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_COGN_SOGG %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_NOME_SOGG %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SESS %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_CODI_STAT %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_LUOG_NASC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_CODI_IDEN_AFIS %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_NASC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_NASC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_NASC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_FLAG_SELE_CIRC_SOGG %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>