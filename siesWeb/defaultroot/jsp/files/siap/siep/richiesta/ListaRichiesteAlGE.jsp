<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<jsp:useBean id="formname"  scope="request" class="java.lang.String"/>
<jsp:useBean id="beneficio"  scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiesteAlGE" scope="request" class="java.util.Vector"/>

<% 
// Valore di default 
if (formname.trim().length() == 0)
 	formname = "f"; 
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Richieste al GE Amnistia/Indulto</title>

    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">

      function controlla()
      {
<%
        boolean esistonoDati = true;
        if( RichiesteAlGE.size()==0 )
          esistonoDati = false;
%>
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");

          window.parent.close();
        }
      }

      //========================================================================
      // Abilita la visualizzazione del relativo record sulla form chiamante
      //========================================================================
      function insertIT(id_record, selbeneficio, codTipoAnnotazione)
      {

    	richiestaScelta = window.parent.opener.document.getElementById(id_record);
    	  
  		if (window.parent.opener.document.getElementById('titolo_richieste').style.display=='none')
        window.parent.opener.document.getElementById('titolo_richieste').style.display='block';
        window.parent.opener.document.getElementById(id_record).style.display='block';
        window.parent.opener.document.getElementById('cb_'+id_record).checked=true;
        
        //== Se sono selezionate solo richieste senza anticipazione e la decisione 
        //== è conforme, aggiorno i quantum in base ai dati selezionati
        //if (window.parent.opener.document.<%=formname%>.TipoOrd[1].checked && tipoRichiestaSelezionata()=='senzaAnticipazione')
        if (window.parent.opener.document.<%=formname%>.TipoOrd[1].checked )
           aggiornaQuantum('Conferma');
        if (window.parent.opener.document.<%=formname%>.TipoOrd[3].checked && tipoRichiestaSelezionata()=='conAnticipazione')
           aggiornaQuantum('Annulla');
        if (window.parent.opener.document.<%=formname%>.TipoOrd[4].checked && tipoRichiestaSelezionata()=='conAnticipazione')
           aggiornaQuantum('Annulla');

        if (selbeneficio == 'DEPEN') {
        	// Aggiornamento istantaneo dei campi specifici della Depenalizzazione
        	window.parent.opener.document.<%=formname%>.<%=ICostantiReato.CAMPO_COD_FONTE%>.value = richiestaScelta.codFonte;
          	window.parent.opener.document.<%=formname%>.<%=ICostantiReato.CAMPO_ANNO_FONTE%>.value = richiestaScelta.annoFonte;
       	    window.parent.opener.document.<%=formname%>.<%=ICostantiReato.CAMPO_NUMERO_FONTE%>.value = richiestaScelta.numeroFonte;
       	    window.parent.opener.document.<%=formname%>.<%=ICostantiReato.CAMPO_ARTICOLO%>.value = richiestaScelta.articolo;
       	    window.parent.opener.document.<%=formname%>.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>.value = richiestaScelta.codSottonumerazione;
       	    window.parent.opener.document.<%=formname%>.<%=ICostantiReato.CAMPO_COMMA%>.value = richiestaScelta.comma;
       	    window.parent.opener.document.<%=formname%>.<%=ICostantiReato.CAMPO_LETTERA%>.value = richiestaScelta.lettera;
       	    window.parent.opener.document.<%=formname%>.<%=ICostantiReato.CAMPO_NUMERO%>.value = richiestaScelta.numero;
        }
        if (selbeneficio == 'INCOST') {
            // Aggiornamento istantaneo dei campi specifici della Incostituzionalità
        	window.parent.opener.document.<%=formname%>.<%="annoSCC"%>.value = richiestaScelta.annoCC;
       	    window.parent.opener.document.<%=formname%>.<%="numeroSCC"%>.value = richiestaScelta.numeroCC;
       	 	window.parent.opener.document.<%=formname%>.<%="ggScc"%>.value = richiestaScelta.giornoScc;
       		window.parent.opener.document.<%=formname%>.<%="mmScc"%>.value = richiestaScelta.meseScc;
       		window.parent.opener.document.<%=formname%>.<%="aaScc"%>.value = richiestaScelta.annoScc;
        }
        
        //alert('codTipoAnnotazione = '+codTipoAnnotazione);
        window.parent.opener.document.<%=formname%>.<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>.value = codTipoAnnotazione;
        
        var nodeRadio = window.parent.opener.document.getElementById('divRadio');
        window.parent.opener.document.<%=formname%>.Radio_Depe_Ammi[0].checked = false;
        window.parent.opener.document.<%=formname%>.Radio_Depe_Ammi[1].checked = false;
   	  	nodeRadio.style.display = 'none';
   	  	
        window.parent.close();        
      }

     //============================================================================
      // Aggiorna i campi Reclusione e Arresto sommando i dati delle richieste 
      // selezionate
      //============================================================================
      function aggiornaQuantum (tipoAggiornamento){
        //
        righe_tabella = window.parent.opener.document.getElementById('tabella_richieste').all;
        aaRec=0;
        mmRec=0;
        ggRec=0;
        multa = 0.00;
        
        aaArr = 0;
        mmArr = 0;
        ggArr = 0;
        ammenda = 0.00;
        
    
        for(i = 0; i < righe_tabella.length; i++){
          if (righe_tabella(i).tagName=='TR'){
            if (   righe_tabella(i).style.display=='block'
                && righe_tabella(i).id!='titolo_richieste'
               )
            {
              if (righe_tabella(i).segno=='+')
              { //alert("revocati");
                aaRec   = aaRec   + parseInt(righe_tabella(i).aaRec);
                mmRec   = mmRec   + parseInt(righe_tabella(i).mmRec);
                ggRec   = ggRec   + parseInt(righe_tabella(i).ggRec);
                multa   = multa   + parseFloat(righe_tabella(i).multa);
                
                aaArr   = aaArr   + parseInt(righe_tabella(i).aaArr);
                mmArr   = mmArr   + parseInt(righe_tabella(i).mmArr);
                ggArr   = ggArr   + parseInt(righe_tabella(i).ggArr);
                ammenda = ammenda + parseFloat(righe_tabella(i).ammenda);
              }
              else
              { //alert("concessi");
                aaRec   = aaRec   - parseInt(righe_tabella(i).aaRec);
                mmRec   = mmRec   - parseInt(righe_tabella(i).mmRec);
                ggRec   = ggRec   - parseInt(righe_tabella(i).ggRec);
                multa   = multa   - parseFloat(righe_tabella(i).multa);
                
                aaArr   = aaArr   - parseInt(righe_tabella(i).aaArr);
                mmArr   = mmArr   - parseInt(righe_tabella(i).mmArr);
                ggArr   = ggArr   - parseInt(righe_tabella(i).ggArr);
                ammenda = ammenda - parseFloat(righe_tabella(i).ammenda);
              }
            }
          }
        }
        recNorm = normalizzaQuantum(aaRec,mmRec,ggRec);
        annNorm = normalizzaQuantum(aaArr,mmArr,ggArr);
        
        if (recNorm[0]=='-' || annNorm[0]=='-'){
          if (tipoAggiornamento=='Annulla')
            window.parent.opener.document.<%=formname%>.PM[1].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
          else
            window.parent.opener.document.<%=formname%>.PM[2].selected=true; // segno -
        }
        else if (recNorm[1]!='0' || recNorm[2]!='0' || recNorm[3]!='0' || annNorm[1]!='0' || annNorm[2]!='0' || annNorm[3]!='0')
        {
          if (tipoAggiornamento=='Annulla')
            window.parent.opener.document.<%=formname%>.PM[2].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
          else
            window.parent.opener.document.<%=formname%>.PM[1].selected=true; // segno +
        }
        else {
          // quantum nulli ho solo la pena pecuniaria devo utilizzare il segno della
          // multa o ammenda
//          alert ('quantum nulli');
          if (multa<0 || ammenda<0){
            if (tipoAggiornamento=='Annulla')
              window.parent.opener.document.<%=formname%>.PM[1].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
            else
              window.parent.opener.document.<%=formname%>.PM[2].selected=true; // segno -
          }
          else{
            if (tipoAggiornamento=='Annulla')
              window.parent.opener.document.<%=formname%>.PM[2].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
            else
              window.parent.opener.document.<%=formname%>.PM[1].selected=true; // segno +
          }
        }
        
        if (multa<0)   multa   = multa*(-1);
        if (ammenda<0) ammenda = ammenda*(-1);
    
        // Reclusione    
        window.parent.opener.document.<%=formname%>.ARec.value  = recNorm[1];
        window.parent.opener.document.<%=formname%>.MRec.value  = recNorm[2];
        window.parent.opener.document.<%=formname%>.GRec.value  = recNorm[3];
        window.parent.opener.document.<%=formname%>.Multa.value = parseInt(multa);
        window.parent.opener.document.<%=formname%>.Mul_dec.value = getParteDecimale(multa);
        
        // Arresti    
        window.parent.opener.document.<%=formname%>.AArr.value = annNorm[1];
        window.parent.opener.document.<%=formname%>.MArr.value = annNorm[2];
        window.parent.opener.document.<%=formname%>.GArr.value = annNorm[3];
        window.parent.opener.document.<%=formname%>.Ammenda.value=parseInt(ammenda); // recupera la parte intera
        window.parent.opener.document.<%=formname%>.Amm_dec.value=getParteDecimale(ammenda);
      }
      
      //============================================================================
      // Estrae la parte decimale da un number
      //============================================================================
      function getParteDecimale(valore){
        //alert("valore = "+valore);
        valoreStr = String(valore);
        //alert("valoreStr = "+valoreStr);
        posVirgola=-1;
        parteDecimale = "00";
        for (i=0;i<valoreStr.length;i++){
          //alert(valoreStr.substr(i,1));
          if (valoreStr.substr(i,1)=='.')
            posVirgola = i;
        }
        if (posVirgola<0)
          parteDecimale = "00";
        else
          parteDecimale = valoreStr.substr(posVirgola+1,valoreStr.length-(posVirgola+1));
          
    //    alert ("parteDecimale = "+parteDecimale);
        return parteDecimale;
      }
      
      //========================================================================
      // Restituisce il tipo di richiesta selezionata
      // - nessuna
      // - conAnticipazione
      // - senzaAnticipazione
      // - misto
      //========================================================================
      function tipoRichiestaSelezionata (){
        righe_tabella = window.parent.opener.document.getElementById('tabella_richieste').all;
        isRichiestaSelezionata = 'false';
        isAnticipazione='false';
        isSenzaAnticipazione='false';
        for(i = 0; i < righe_tabella.length; i++){
          if (righe_tabella(i).tagName=='TR'){
            if (   righe_tabella(i).style.display=='block'
                && righe_tabella(i).id!='titolo_richieste'
               )
            {
              //alert(righe_tabella(i).tagName);
              isRichiestaSelezionata='true';
              if (righe_tabella(i).tipoRich=='A')
                isAnticipazione='true';
              if (righe_tabella(i).tipoRich=='R')
                isSenzaAnticipazione='true';
            }
          }
        }
        
        if (isRichiestaSelezionata=='false')
          return 'nessuna';
        if (isAnticipazione=='true' && isSenzaAnticipazione=='false')
          return 'conAnticipazione';
        if (isAnticipazione=='false' && isSenzaAnticipazione=='true')
          return 'senzaAnticipazione';
        if (isAnticipazione=='true' && isSenzaAnticipazione=='true')
          return 'misto';
      }
      
      //========================================================================
      // 
      //========================================================================
      function normalizzaQuantum(anni,mesi,giorni)
      {
        tot_giorni=anni*30*12 + mesi*30 + giorni;
        segno='+';
        //alert("tot_giorni = "+tot_giorni);
        if (tot_giorni<0){
          tot_giorni=tot_giorni*(-1);
          segno='-';
        }
        giorni = tot_giorni % 30; // restituisce il resto dell'operazione
        tot_giorni = tot_giorni-giorni;
        tot_mesi = parseInt(tot_giorni/30); // es 55 mesi
        mesi = tot_mesi % 12;
        tot_mesi = tot_mesi - mesi;
        anni = parseInt(tot_mesi/12);
        //alert("anni = "+anni);
        //alert("mesi = "+mesi);
        //alert("giorni = "+giorni);
        var quantumNormalizzati = new Array(segno,anni ,mesi ,giorni );
        return quantumNormalizzati;
      }      
  	</script>
  </head>

<body class="corpo" onload="controlla();">
<form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">
          Decisioni del GE
        </font>
      </td>
    </tr>
  </table>
  <br>

<%
//==============================================================================
// Tabella con la lista delle richieste con e senza anticipazione
//==============================================================================
int id_record = 0;
if (RichiesteAlGE.size()!=0)
{
%>  
<table style="width: 95%;">
        <% if (beneficio.compareTo("AMNI") == 0) { %>
      	<tr><td colspan="100%" class="Titolo">Richieste al GE per applicazione benefici Amnistia/Indulto</td></tr>
      <% } else if (beneficio.compareTo("DEPEN") == 0) { %>
      	<tr><td colspan="100%" class="Titolo">Richieste al GE per applicazione benefici Depenalizzazione</td></tr>
      <% } else if (beneficio.compareTo("INCOST") == 0) { %>
      	<tr><td colspan="100%" class="Titolo">Richieste al GE per applicazione benefici Incostituzionalità</td></tr>
      <% } %>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="Titolo">Data Richiesta</td>
    <td class="Titolo">Tipo</td>
    <td class="Titolo" colspan="4" >Reclusione</td>
    <td class="Titolo" colspan="4" >Arresto</td>
    <td class="Titolo">Anticipazione</td>
    <td class="Titolo">&nbsp;</td>
    <td class="Titolo">Già Elaborata</td>
  </tr>
  <tr>
    <td class="c">&nbsp;</td>
    <td class="c" style="text-align:center">+/-</td>
    <td class="c">Anni</td>
    <td class="c">Mesi</td>
    <td class="c">Giorni</td>
    <td class="c">Multa</td>
    <td class="c">Anni</td>
    <td class="c">Mesi</td>
    <td class="c">Giorni</td>
    <td class="c">Ammenda</td>
    <td class="c">&nbsp;</td>
    <td class="c">&nbsp;</td>
    <td class="c">&nbsp;</td>
  </tr>
<%

  Iterator itx = RichiesteAlGE.iterator();
  for (int i = 0; itx.hasNext(); i++)
  {
    id_record++;
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)itx.next();
%>
  <tr>
    <td class="l" style="text-align:center"><font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMod.getDataRichiesta(),"dd-MM-yyyy"),"&nbsp;")%></font></td>
    <td class="l" style="text-align:center"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno(),"")%></font></td>
    <td class="l" style="text-align:right"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione(),"0")%></font></td>
    <td class="l" style="text-align:right"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione(),"0")%></font></td>
    <td class="l" style="text-align:right"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione(),"0")%></font></td>
    <td class="l" style="text-align:right"><font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoMulta())%></font></td>
    <td class="l" style="text-align:right"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumAnniArresto(),"0")%></font></td>
    <td class="l" style="text-align:right"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumMesiArresto(),"0")%></font></td>
    <td class="l" style="text-align:right"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniArresto(),"0")%></font></td>
    <td class="l" style="text-align:right"><font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda())%></font></td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--  
    <td class="l">
       Anni   <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione(),"0")%></font>
       Mesi   <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione(),"0")%></font>
       Giorni <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione(),"0")%></font>
       Multa  <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoMulta())%></font>
    </td>
    <td class="l">
       Anni <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumAnniArresto(),"0")%></font>
       Mesi <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumMesiArresto(),"0")%></font>
       Giorni <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniArresto(),"0")%></font>
       Ammenda <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda())%></font>
    </td>
--%>
 
    <% if (   lAnnMod.getFlagAppProvvisoria()!=null 
           && lAnnMod.getFlagAppProvvisoria().equals("A")
          )
       { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else {%>
    <td class="l">&nbsp;</td>
    <% }%>
    <td class="c">
      <a href="Javascript:insertIT('record_<%=id_record%>', '<%=beneficio%>', '<%=StringUtils.toStringJSP(lAnnMod.getCodTipoAnnotazione() )%>');">Seleziona</a>
    </td>
    <% if (lAnnMod.getAnnoIdAnnotazioneManuale()!=null) {%>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else {%>
    <td class="l">&nbsp;</td>
    <% }%>
  </tr>
<% } %>
</table>
<%
}
%>

</form>
</body>
</html>