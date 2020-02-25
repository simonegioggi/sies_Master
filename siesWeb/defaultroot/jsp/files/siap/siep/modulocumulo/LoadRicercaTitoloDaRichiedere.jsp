<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>



<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="tipoAutorita"    scope="request" class="java.lang.String" />
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />




<html>
<head>
  <title> [S.I.E.S.] - Ricerca Procedimento - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>  
  <script language="JavaScript"> 
  
  var ufficiAccorpatiArray = new Array();

  <%
  Iterator uaIter = ufficiAccorpati.iterator();
  int uaIndice = 0;
  while (uaIter.hasNext())
  {
    UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
    %>
    ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>","<%=uaModel.getIncrProgressivo()%>","<%=uaModel.getCodUfficioNew()%>","<%=uaModel.getDescrizioneNewUfficio()%>"); 
    <%
    uaIndice ++;
  }
  %>
    
    

  function selSedeUfficio()
  {
    if (document.f.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value.length==0){
      ChoosePopup('f','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
    }
  }
          
  
  var flagVerify = true;    
 
  function ChoosePopup()
  {
    var selectTipoUfficio = document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>;
    var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
    var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;
      
    if (codTipoUfficio=='' || codTipoUfficio=='-'){
      alert("Selezionare prima l'autorità");
      document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.focus();      
    }
    else if (codTipoUfficio == 'PM'){
      ListaUffici('f','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
    } 
    else if (codTipoUfficio == 'PGCAP'){
      ListaDistretti('f','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
    }
    else {
      ListaUfficiPerTipo('f','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>'
                         ,document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value);
    }
  } 
 
  function tornaIndietro(action)
  {
    document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.f.submit();
  }
  
  function ListaUffici(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }
  
  function ListaDistretti(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }
  
  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }


  // Funzione richiamata dalla POPUP deglla selezione sede ufficio 
  // codUfficio = codUfficio accorpante
  function loadUfficiAccorpati(codUfficio){
    var i=0;
    var ufficioAccorpatoSelect = document.f.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
    ufficioAccorpatoSelect.options.length = 0;
    ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
    while(i<ufficiAccorpatiArray.length){
      var ufficio = ufficiAccorpatiArray[i];
      if (ufficio[2]==codUfficio){
        ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0], ufficio[1]);
      }
      i++;
    }
  }
  
  function resetSede(){
    document.f.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value="";
    var ufficioAccorpatoSelect = document.f.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
    ufficioAccorpatoSelect.options.length = 0;
    ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
  }
  
  //=======================================
  //
  //=======================================
  function Verify(){  
    if(flagVerify==false){
      return true
    }
    else{ 
      var campoAnno = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value;
      if (campoAnno.length==0){
        alert('Il campo Anno è obbligatorio');
        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
        return false;
      }
      else if(campoAnno<1900 || campoAnno><%=DateUtils.getSysDate("yyyy")%> ){
        alert('Digitare correttamente il campo Anno. Maggiore di 1900, minore di '+<%=DateUtils.getSysDate("yyyy")%>);
        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
        return false;
      }
      
      if (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value.length==0){
        alert('Il campo Numero SIEP è obbligatorio');
        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
        return false;
      }
    
      if (   document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value.length==0
          || document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value=="-")
      {
        alert('Il campo Autorità è obbligatorio');
        document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.focus();
        return false;
      }
      
      if (   document.f.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value.length==0
          || document.f.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value=="-")
      {
        alert('Il campo Luogo è obbligatorio');
        document.f.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.focus();
        return false;
      } 

      return true;
    } 
  }
  
  </script>


</head>

<body class="corpo" onLoad="document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus()">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>      
      <td class=LBG>
        <font class="label">Funzione:</font>&nbsp;<font class="campo">Ricerca Procedimento da Richiedere</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Lista dei fascicoli coinvolti -->
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>      
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<% // INCLUDE DEL DETTAGLIO DELL'ISTRUTTORIA %>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
  </table>
  
  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadRichiestaTrasmissioneTitolo">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">



    <table cellpadding=2 cellspacing=2 width="95%" align="center">
      <tr><td width="90%" class="Titolo" colspan="2">Estremi del titolo da richiedere</td></tr>
      <tr>
        <td class="l">Anno/Numero SIEP <font class=ob>(*)</font></td>
        <td class="L">
          <input type="text" title="Anno"  maxlength="4" size="4"   
                 name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>"
                 value=""
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillYear(value)">
          /
          <input type="text" title="Numero SIEP"  maxlength="14" size="14" 
                 name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>"
                 onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>
      
      <tr>
        <td class="L">Autorità <font class=ob>(*)</font></td>
        <td class="l">
          <select name="<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>" onchange="resetSede()">
            <option value="-">-</option>
            <%=tipoAutorita%>
          </select>
        </td>
      </tr> 
      
      <tr>
        <td class="l">
          Luogo <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        <td class="L">
          <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>" 
                 value="" maxlength="35" size="35" readonly="readonly" 
                 onclick="javascript:selSedeUfficio();"
                 onfocus="javascript:selSedeUfficio();"
                 >
          <a href="Javascript:ChoosePopup('f','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      
      <tr>
         <td class="L">Ufficio Accorpato</td>
         <td class="l">
          <select name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>"  >
            <option value="0" >-</option>
          </select>
         </td>
      </tr>      
  
      <tr>
        <td colspan="2">
          <input type="submit" class="bottone" name="Carica" value="Carica" onclick="return Verify();" title="Ricerca in banca dari del titolo indicato">
          &nbsp;
          <input type="submit" class="bottone" name="Avanti" value="Avanti" title="Inserimento manuale dati titolo da richiedere">
        </td>
      </tr>
    </table>    
  
  </form>


  <script language="JavaScript" type="text/javascript">  
    var frmvalidator  = new Validator("f");   
  </script>
</body>
</html>