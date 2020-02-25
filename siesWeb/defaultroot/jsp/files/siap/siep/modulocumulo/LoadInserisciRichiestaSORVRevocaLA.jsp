<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>


<%@page import="siap.siep.modulocumulo.action.ICostantiLibAnticipataCumulo"%>
<%@page import="siap.siep.modulocumulo.model.LibAnticipataCumuloModel"%>
<%@page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaAllaSORV"   scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
  
<jsp:useBean id="VectorTitoli"      scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita"          scope="request" class="java.lang.String"/>

<% 
//============================================================================== 
// Form per l'inserimento e la modifica delle richieste alla SORVEGLIANZA di 
// Revoca LA
//============================================================================== 
%>

<html>
<head>
  <title> Gestione Richieste Revoca LA</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
   
    
    function ElencoTitoliPopup (a_form_name, a_form_type)
    {
      <%
      String lStrParametri = "";
      lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
      %>
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActCaricaElencoTitoliPerSelezione&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametri%>"
                          , "Elenco_Titoli"
                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=800, height=500");
    }     

    function Verify() 
    { 
      // Controllo che sia selezionato almeno un Titolo.
      var Spunta="NO";
      var numTitoli = <%=VectorTitoli.size()%>;
      for (var j = 0; j < numTitoli; j++ )
      {
        if (numTitoli == 1 ) {
            if(document.formName.idTitoloCompleto.checked )
            { 
              Spunta="SI";
            }
          
        } else {
            if(document.formName.idTitoloCompleto[j].checked )
            { 
              Spunta="SI";
            }
        } 
      }
      
      if(Spunta=="NO")
      {
          // Nessun Titolo selezionato. Controllo che sia selezionato almeno un Provvedimento
        if (typeof document.formName.idStatoEsecSelezionato.length == 'undefined') {
          if (document.formName.idStatoEsecSelezionato.checked  )
          {
            Spunta="SI";
          }
        } else {
          for (var k = 0; k < document.formName.idStatoEsecSelezionato.length; k++ )
          {
            if(document.formName.idStatoEsecSelezionato[k].checked )
            { 
              Spunta="SI";
            }
          }
        }
        if(Spunta=="NO")
        {
          alert(" Attenzione selezionare almeno un Titolo o un Provvedimento e dare conferma");
          //document.formName.idTitoloCompleto[0].focus();
          return false;
        }
      }
      
      // Controllo Data Emissione
      if (document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (data_to_verify=='//' )
      {
          alert('Indicare la Data Richiesta');
          document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
      }
      
      if (!ControllaData(data_to_verify) )
      {
          alert('Data Richiesta non valida');
          document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
      } 

      // Controllo numero giorni.
      if (   (   document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV%>.value.length==0
              || parseInt(document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV%>.value)==0)
          && (   document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LS_REV%>.value.length==0
              || parseInt(document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LS_REV%>.value)==0)
          && (   document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LI_REV%>.value.length==0
              || parseInt(document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LI_REV%>.value)==0)
         )
      {
        alert("Valorizzare il Numero di giorni da revocare");        
        document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV%>.focus();
        return false;
      }
      
      return true; 
    } 

  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;

        <% if (modalita.equals("I") ) { %>
        <font class="campo">Inserimento Richiesta Revoca Liberazione Anticipata&nbsp;</font>
        <% } else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Richiesta Revoca Liberazione Anticipata &nbsp;</font>
        <% } %>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
 
  <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaSORVRevocaLA">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">

  
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA%>" value="<%=StringUtils.toStringJSP(RichiestaAllaSORV.getCodTipoRichiesta(),"")%>">

  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>" value="<%=StringUtils.toStringJSP(RichiestaAllaSORV.getIdRichiestePmInCumulo(),"")%>">

  <br>
  
  <%
  //============================================================================
  // Sezione con i dati delle LA concesse
  //============================================================================
  %>
  <div id="idTitolo_"  style="display:block" >
<%  String DescProvv="";
  String DescAuto="";
  Iterator itx = VectorTitoli.iterator();
  while(itx.hasNext())
  { 
    TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)itx.next();
%> 
   <table width="95%" align="center">
    <tr>
    
    <table width="95%" align="center">
      <tr>
        <td class="titolo" width="90%">In relazione al Titolo</td>
        <td class="titolo" width="10%" style="color:red" >tutto il Titolo</td>
      </tr>
      
      <tr>
      <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>" value="<%=StringUtils.toStringJSP(lTitolo.getIdTitoloCumulato()) %>" >
<%    DescProvv = lTitolo.getDescrTipoProvvedimento() +" N. "+lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
    DescAuto = lTitolo.getDescrTipoAutoritaEmittente() +" di "+lTitolo.getDescrLuogoEmittente();
%>        
  <td class="l">
          <font class="label"> Provvedimento </font>
          <font class="campo"><%=DescProvv%> </font>&nbsp;
          <font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
          <font class="label"> Emessa da </font>
          <font class="campo"><%=DescAuto%></font>&nbsp;
          &nbsp;<font class="label"> Irrevocabile il  </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        </td>
        <td class="c">
          <input type="checkbox" name="idTitoloCompleto" value="<%=StringUtils.toStringJSP(lTitolo.getIdTitoloCumulato()) %>">                 
        </td>
      </tr>
    </table>
    
<%  // -----------------------  STATI ESECUZIONE di Lib. Anticipata -----------------------------------------  
  if(lTitolo.getStatoEsecuzioneTitoloCumulato()!=null && lTitolo.getStatoEsecuzioneTitoloCumulato().size() > 0 )
  { %>
    <table width="95%" align="center">
      <tr>
      <td class="L"  width="95%">
      <font class="label" style="color: red;">Provvedimenti di concessione L.A.</font>
    </td>
        <td class="L" width="5%">sel.</td>
        
      </tr>          
<%    for(int kse = 0; kse < lTitolo.getStatoEsecuzioneTitoloCumulato().size(); kse++ )
    { 
      StatoEsecTitoloCumulatoModel lSETCMod = (StatoEsecTitoloCumulatoModel)lTitolo.getStatoEsecuzioneTitoloCumulato().get(kse); %>
          <tr>
          <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(lSETCMod.getIdStatoEsecTitoloCumulato()) %>" >
            <td class="l">
              &nbsp;&nbsp;<font class="label"> - </font>
              &nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lSETCMod.getDescrTipoProvvedimento(),"") %></font>&nbsp;
              <font class="label"> N.</font>
              <font class="campoLow"><%=StringUtils.toStringJSP(lSETCMod.getAnnoProvvedimento(), "") %></font>/
              <font class="campoLow"><%=StringUtils.toStringJSP(lSETCMod.getProgrProvvedimento(), "") %></font>&nbsp;del&nbsp;
              <font class="campoLow"><%=DateUtils.getDateToString(lSETCMod.getDataEmissione(),"dd/MM/yyyy" ) %></font>&nbsp;&nbsp;
                

emesso da <font class="campoLow"><%=StringUtils.toStringJSP(lSETCMod.getDescrUfficioEmittente(),"") %></font> di 
<font class="campoLow"><%=StringUtils.toStringJSP(lSETCMod.getDescrLuogoEmittente(),"") %></font>

concessi gg.&nbsp;
<%          if (lSETCMod.getListaLiberazioniAnticipate()!=null) {

              for(int lp = 0; lp < lSETCMod.getListaLiberazioniAnticipate().size(); lp++ ) {
                LibAnticipataCumuloModel lLibAntCum = lSETCMod.getListaLiberazioniAnticipate().elementAt(lp);
                if(lLibAntCum.getTipoLa() != null) {
                  if(lLibAntCum.getTipoLa().equals("LA") ) { %> 
                        <font class="campoLow"><%=StringUtils.toStringJSP(lLibAntCum.getNumeroGiorni(), "") %></font>&nbsp;L.A.&nbsp;-&nbsp;
                    <%}else if(lLibAntCum.getTipoLa().equals("LS") ) { %>
                        <font class="campoLow"><%=StringUtils.toStringJSP(lLibAntCum.getNumeroGiorni(), "") %></font>&nbsp;L.A. Spec.&nbsp;-&nbsp;
                    <%}else if(lLibAntCum.getTipoLa().equals("LI") ) { %>
                        <font class="campoLow"><%=StringUtils.toStringJSP(lLibAntCum.getNumeroGiorni(), "") %></font>&nbsp;Int. L.A.&nbsp;-&nbsp;
                    <%}
              }
            }
          }%>
            </td>
            <td class="c">
                <input type="checkbox" name="idStatoEsecSelezionato" value="<%=StringUtils.toStringJSP(lSETCMod.getIdStatoEsecTitoloCumulato()) %>">                 
            </td>
          </tr>
<%    }   %>

   </table>

<%  } // Chiude lo STATO ESECUZIONE   %> 
  
   </tr>  
  </table>   <%// CHIUDE la TABELLA relativa ad 1 Titolo %>
  <br>
<% } // Chiude ciclo while principale %>    
    
  </div>


  <table width="95%" align="center">
    <tr>
      <td colspan="2" class="Titolonocap">Richiesta al Giudice della Sorveglianza</td>
    </tr>
    <tr>
      <td class="l" colspan="1" width="300px">Data Richiesta </td>
      <td class="L" colspan="1">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>  
    <tr>
      <td class="l" colspan="1" width="300px">Si richiede la revoca nella misura di giorni</td>
      <td class="l" colspan="1">
        <table>
          <tr>
            <td class="L">Liberazione Anticipata</td>
            <td class="L">
              <input type="text" size="5" maxlength="4"
                     name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV%>" 
                     value="<%=StringUtils.toStringJSP(RichiestaAllaSORV.getNumGiorniRevocaLA(),"") %>"
                     onkeypress="return TicTabNumField(this,event)">
            </td>
          </tr>
          <tr>
            <td class="L">Liberazione Anticipata Speciale</td>
            <td class="L">
              <input type="text" size="5" maxlength="4"
                     name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LS_REV%>" 
                     value="<%=StringUtils.toStringJSP(RichiestaAllaSORV.getNumGiorniRevocaLS(),"") %>"
                     onkeypress="return TicTabNumField(this,event)">
            </td>
          </tr>
          <tr>
            <td class="L">Integrazione Liberazione Anticipata</td>
            <td class="L">
              <input type="text" size="5" maxlength="4"
                     name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LI_REV%>" 
                     value="<%=StringUtils.toStringJSP(RichiestaAllaSORV.getNumGiorniRevocaLI(),"") %>"
                     onkeypress="return TicTabNumField(this,event)">                              
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="l" colspan="1">Motivazioni</td>
      <td class="l" colspan="1">
        <textarea cols="90" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>"><%=StringUtils.toStringJSP(RichiestaAllaSORV.getMotivazioni()) %></textarea>
      </td>
    </tr>
  </table>

  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>


</FORM>

</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 