<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="datiFinaliAggregatoModel"   scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="ElencoPeneAccessorieInIstruttoria" scope="request" class="java.util.Vector"/>


<%
//==============================================================================
//     FORM di Selezione delle Pene Accessorie da includere nel cumulo
//==============================================================================
DatiFinaliCumuloModel lDatiFinaliCumulo = new DatiFinaliCumuloModel(); 
if( modalita.equals("M")){
  lDatiFinaliCumulo = datiFinaliAggregatoModel.getDatiFinaliCumulo();
}
%>

<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">    
   
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formIndietro.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formIndietro.submit();
    }
    
    
    function Verify()
    {
      return true;
    }
    
    
    //================================================
    // Funzione richiamata al caricamento della form
    //================================================
    $(document).ready(function(){

    });    
  </script>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Elenco Pene Accessorie Sui Titoli Coinvolti</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia Della Gestione Cumulo -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActDettaglioAltreSanzioni')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>


  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formIndietro">
    <%//FORM per richiamare i dettagli in POST ma evitare di inviare inutilmente i dati della FORM principale%>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  </form>
  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActSelezionaPenaAccessoriaDatiFinali">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    
    <input type="hidden" name="<%=ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO%>" value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getIdDatiFinaliCumulo()) %>">
    
    <input type="hidden" name="modalita" value="<%=modalita%>">

<%
//==============================================================================
// 
//==============================================================================
%>
<!--div id="divPosizionamento" align="left" style="padding-left: 25px;"-->
<div id="divPosizionamento" align="center" >
  <!--table cellspacing=2 cellpadding=4 width="95%"-->
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
  
    <tr>
      <td class="titolo" colspan="11">Pene Accessorie sui titoli caricati in cumulo</td>
    </tr>


    <%
      BigDecimal lastIdTitolo = new BigDecimal (0);
      boolean isNuovoTitolo = true ;

      Iterator itx = ElencoPeneAccessorieInIstruttoria.iterator();
      while ( itx.hasNext()) {
        PenaAccessoriaCumuloModel lPenaAccessoriaCumulo = (PenaAccessoriaCumuloModel)itx.next();
        TitoloCumulatoModel lTitolo = lPenaAccessoriaCumulo.getTitoloCumulato();

        if (lTitolo.getIdTitoloCumulato().compareTo(lastIdTitolo)!=0) {
          isNuovoTitolo = true;
        }
        else {
          isNuovoTitolo = false;
        }

        lastIdTitolo = lTitolo.getIdTitoloCumulato();

        
        // Compongo la descizione del Titolo
        String lDescrTitolo = lTitolo.getDescrTipoProvvedimento();
        if ("02".equals (lTitolo.getCodTipoProvvedimento())) {
          String [] lUfficiSorv = new String[] {"UDS","TDS","UDSM"};
          if (!Arrays.asList(lUfficiSorv).contains(lTitolo.getCodTipoAutoritaEmittente())){
            // Rimappo il codice per poterlo gestire nella jsp
            lDescrTitolo = "Decreto Penale";
          }
        }
        
        lDescrTitolo += " N. <font class=\"campo\">"+lTitolo.getAnnoSentenza()+" / "+lTitolo.getNumeroSentenza()+"</font>";
        
        lDescrTitolo += " Del <font class=\"campo\">"+DateUtils.getDateToString(lTitolo.getDataProvvedimento(), "dd-MM-yyyy")+"</font>";
        
        if(!lTitolo.getCodTipoProvvedimento().equals("02")) { 
          lDescrTitolo += " Emessa da: <font class=\"campo\">"+lTitolo.getDescrTipoAutoritaEmittente()+"</font>";
        } else {
          lDescrTitolo += " Emesso da: <font class=\"campo\">"+lTitolo.getDescrTipoAutoritaEmittente()+"</font>";
        }
        
        if (lTitolo.getNumSezioneAutoritaEmittente() != null) {
          lDescrTitolo += " (Sez. <font class=\"campo\">"+lTitolo.getNumSezioneAutoritaEmittente()+"</font>)";
        }
        
        lDescrTitolo += " di <font class=\"campo\">"+lTitolo.getDescrLuogoEmittente()+"</font>";
        
        if (lTitolo.getDataIrrevocabilita()!=null) {
          lDescrTitolo += " Data irrevocabilità : <font class=\"campo\">"+DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(), "dd-MM-yyyy")+"</font>";
        }


        String lStato = "";
        String lDescStato = "";
        String lFontColor = "";
        String lAnnullata="";
        
        if      ( lPenaAccessoriaCumulo.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        else if ( lPenaAccessoriaCumulo.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        else if ( lPenaAccessoriaCumulo.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lPenaAccessoriaCumulo.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
        
        // Preparazione Durata
        String lDurata = "";
        if(lPenaAccessoriaCumulo.getNumAnni()!= null && lPenaAccessoriaCumulo.getNumAnni().intValue() > 0)
          lDurata +="Anni: "+lPenaAccessoriaCumulo.getNumAnni().toString()+"  ";
        if(lPenaAccessoriaCumulo.getNumMesi()!= null && lPenaAccessoriaCumulo.getNumMesi().intValue() > 0)
          lDurata +="Mesi: "+lPenaAccessoriaCumulo.getNumMesi().toString()+"  ";
        if(lPenaAccessoriaCumulo.getNumGiorni()!= null && lPenaAccessoriaCumulo.getNumGiorni().intValue() > 0)
          lDurata +="Giorni: "+lPenaAccessoriaCumulo.getNumGiorni().toString()+"  ";        
        
      %>

    <% if (isNuovoTitolo) { %>
    <tr>
      <td colspan="11">&nbsp;</td>
    </tr>
    <tr style="background-color: rgb(255,255,153);">
      <td class="L" colspan="11"><font class="cassmpo"><%=lDescrTitolo%></font></td>
    </tr>
    
    <tr>
      <td class="titolo" width="30%" nowrap>Tipo Pena</td>
      <td class="titolo" width="10%">Tipo Durata</td>
      <td class="titolo" width="16%">Durata</td>
      
      <td class="titolo" width="8%">Includi</td>
    </tr>    
    <% } %>
      
    <tr>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getDescrTipoPenaAccessoria(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getDescrDurata(),"&nbsp;")%></td>
      
      <td class="c" <%=lFontColor%> >&nbsp; <%=lDurata%></td>

      <td class="c" <%=lFontColor%> >
        <input type="checkbox" 
               <%="S".equals(lPenaAccessoriaCumulo.getFlagDatiFinali())?"checked":""%>
               name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_DATI_FINALI%>" 
               value="<%=lPenaAccessoriaCumulo.getIdPenaAccessoriaCumulo()%>"
               >
      </td>
    </tr>
      <%
      }  // End While
    %>
    
  </table>
  
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td>
        <input type="submit" name="Conferma" value="Conferma">
      </td>
    </tr>
  </table>
  
</div>
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>

</html>

