<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaPeneAccessorie"  scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Form per la visualizzazione delle Pene Accessorie legate a un certo Titolo.
//
// La form presenta un elenco delle P.A. presenti con la possibilità di 
// modificarle, cancellarle o inserirne delle nuove 
//==============================================================================
%>
<!-- 		ElencoPeneAccessorieCumulo		 -->
<html>
<head>
  <title> S.I.E.S.] - Gestione Cumulo - Elenco Pene Accessorie</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function visualizzaMotivo(idRecord)
    {
      var riga = document.getElementById(idRecord);  
      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
      }
      else 
      {
        riga.style.display = "none";
      }
    }	
    	
  /*  function visualizzaNota(idRecord)
    {
      var riga = document.getElementById(idRecord);  
      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
      }
      else 
      {
        riga.style.display = "none";
      }
    }
  */  
    //==========================================================================
    // Richiama l'opportuna azione
    //==========================================================================
    function eseguiAzione(aTipoAzione, aIdMisura, aStato, aMotivoModifica)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioPenaAccessoriaCumulo";
        document.ListaPeneAccessorieCumulo.<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO%>.value = aIdMisura;
        document.ListaPeneAccessorieCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaPeneAccessorieCumulo.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciPenaAccessoriaCumulo";
        document.ListaPeneAccessorieCumulo.<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO%>.value = aIdMisura;
        document.ListaPeneAccessorieCumulo.modalita.value="M";
        document.ListaPeneAccessorieCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.ListaPeneAccessorieCumulo.submit();
      }
      else if (aTipoAzione=='Cancella'){
        if (aStato=='E' || aStato=='M'){
          // Cancellazione Logica Richiedo Motivazione
          lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciPenaAccessoriaCumulo";
          document.ListaPeneAccessorieCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.ListaPeneAccessorieCumulo.<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO%>.value = aIdMisura;
          document.ListaPeneAccessorieCumulo.<%=ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_STATO%>.value = aStato;
          document.ListaPeneAccessorieCumulo.modalita.value="C";
          var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"ListaPeneAccessorieCumulo"
                                     + "&" + "<%=ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
          window.parent.close();
          
          // N.B. la submit viene effettuare direttamnete dalla finestra di popup
        }
        else if (aStato=='I'){
          // Cancellazione fisica richiedo conferma
          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
          if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciPenaAccessoriaCumulo";
            document.ListaPeneAccessorieCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;

            document.ListaPeneAccessorieCumulo.<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO%>.value = aIdMisura;
            document.ListaPeneAccessorieCumulo.<%=ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_STATO%>.value = aStato;
            document.ListaPeneAccessorieCumulo.modalita.value="C";
            document.ListaPeneAccessorieCumulo.submit();
          }
        }
      }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaAnnotazione(){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciPenaAccessoriaCumulo";
      document.ListaPeneAccessorieCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.ListaPeneAccessorieCumulo.modalita.value="I";
      document.ListaPeneAccessorieCumulo.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.ListaPeneAccessorieCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ListaPeneAccessorieCumulo.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Pene Accessorie</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      </td>
    </tr>
  </table>

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaPeneAccessorieCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO%>" value="">
  <input type="hidden" name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA%>"            value="">
  <input type="hidden" name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_STATO%>"                 value="">
  <input type="hidden" name="modalita" value = "">

  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int" width="30%">Tipo Pena</td>
      <td class="int" width="10%">Tipo Durata</td>
      <td class="int" width="16%">Durata</td>
      <td class="int" width="7%" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <!--  td class="int" width="25%">Note</td	-->
      <td class="int" width="7%">Azioni</td>
    </tr>
    <%
      int id_record = 0;

      Iterator itx = ListaPeneAccessorie.iterator();
      while ( itx.hasNext()) {
        id_record = id_record +1;
        PenaAccessoriaCumuloModel lPenaAccCumuloMod = (PenaAccessoriaCumuloModel)itx.next();
        
        String lStato = "";
        String lDescStato = "";
        String lFontColor = "";
        
        if      ( lPenaAccCumuloMod.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        else if ( lPenaAccCumuloMod.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        else if ( lPenaAccCumuloMod.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lPenaAccCumuloMod.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
        
        // Preparazione Durata
        String lDurata = "";
        if(lPenaAccCumuloMod.getNumAnni()!= null && lPenaAccCumuloMod.getNumAnni().intValue() > 0)
      	  lDurata +="Anni: "+lPenaAccCumuloMod.getNumAnni().toString()+"  ";
        if(lPenaAccCumuloMod.getNumMesi()!= null && lPenaAccCumuloMod.getNumMesi().intValue() > 0)
      	  lDurata +="Mesi: "+lPenaAccCumuloMod.getNumMesi().toString()+"  ";
        if(lPenaAccCumuloMod.getNumGiorni()!= null && lPenaAccCumuloMod.getNumGiorni().intValue() > 0)
      	  lDurata +="Giorni: "+lPenaAccCumuloMod.getNumGiorni().toString()+"  ";
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lPenaAccCumuloMod.getDescrTipoPenaAccessoria(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lPenaAccCumuloMod.getDescrDurata(),"&nbsp;")%></td>
      <td class="c" <%=lFontColor%> >&nbsp; <%=lDurata%></td>
<% 		if (lPenaAccCumuloMod.getMotivoModifica()!=null && lPenaAccCumuloMod.getMotivoModifica().length()>0)
      	{ %>
      	<td class="c">
      	<a href="javascript:visualizzaMotivo('rec_<%=id_record%>')" title="<%=lDescStato%>">
       		<%=lStato%>
       	</a>
       	</td>
     <% }
    	else
    	{	%>	
    	<td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
   	<% 	} %>
       
      <td class="c" style="text-align:center"> &nbsp;
        <%
          //======================================================================
          // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
         //======================================================================
        %>
        <a href="javascript:eseguiAzione('Dettaglio',<%=lPenaAccCumuloMod.getIdPenaAccessoriaCumulo() %> )">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
        
        <% 
        //======================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //======================================================================
        if(IstruttoriaCumulo.getFlagStato().equals("A") && !lPenaAccCumuloMod.getFlagStato().equals("C")){ %>
        <a href="javascript:eseguiAzione('Modifica',<%=lPenaAccCumuloMod.getIdPenaAccessoriaCumulo() %>)">
          <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',<%=lPenaAccCumuloMod.getIdPenaAccessoriaCumulo() %>,'<%=lPenaAccCumuloMod.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lPenaAccCumuloMod.getMotivoModifica()),"") %>')">
          <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
        <% } %>
      </td>
    </tr>
    <!-- Record Hidden con le note -->
    <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lPenaAccCumuloMod.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr>
    <% } // end while su iterator %>
    
    <% if (ListaPeneAccessorie.size()==0){ %>
    <tr>
      <td>Nessun dato presente</td>
    </tr>
    <% } %>
    
    <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuova Pena Accessoria" onClick="javascript:nuovaAnnotazione();">
      </td>      
    </tr>
    <% } %>
  </table>
</FORM>
</body>
</html>