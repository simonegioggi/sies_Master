<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>

<jsp:useBean id="formname"  scope="request" class="java.lang.String"/>
<jsp:useBean id="beneficio"  scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiesteAlGE1" scope="request" class="java.util.Vector"/>

<% 
// Valore di default 
if (formname.trim().length() == 0)
 	formname = "f";
%>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
      //========================================================================
      // Abilita la visualizzazione del relativo record sulla form chiamante
      //========================================================================
      function insertIT(id_record, selbeneficio)
      {

    	  var addedRecord = document.getElementById(id_record);
    	  
    	  //alert (addedRecord.idAnnotazione);
    	  
    	  
    	  selBenefici.push(addedRecord.idAnnotazione);
    	  removeFromTheArray (notSelBenefici, addedRecord.idAnnotazione);  
    	  
    	richiestaScelta = document.getElementById(id_record);
        document.getElementById('titolo_richieste').style.display='block';
        document.getElementById('sel'+id_record).style.display='none';
        document.getElementById(id_record).style.display='block';
        document.getElementById('cb_'+id_record).checked=true;
        
        //== Se sono selezionate solo richieste senza anticipazione e la decisione 
        //== è conforme, aggiorno i quantum in base ai dati selezionati
        //if (document.<%=formname%>.TipoOrd[1].checked && tipoRichiestaSelezionata()=='senzaAnticipazione')
        if (document.<%=formname%>.TipoOrd[1].checked )
           aggiornaQuantum('Conferma');
        if (document.<%=formname%>.TipoOrd[3].checked && tipoRichiestaSelezionata()=='conAnticipazione')
           aggiornaQuantum('Annulla');
        if (document.<%=formname%>.TipoOrd[4].checked && tipoRichiestaSelezionata()=='conAnticipazione')
           aggiornaQuantum('Annulla');

        if (selbeneficio == 'DEPEN') {
        	// Aggiornamento istantaneo dei campi specifici della Depenalizzazione
        	document.<%=formname%>.<%=ICostantiReato.CAMPO_COD_FONTE%>.value = richiestaScelta.codFonte;
          	document.<%=formname%>.<%=ICostantiReato.CAMPO_ANNO_FONTE%>.value = richiestaScelta.annoFonte;
       	    document.<%=formname%>.<%=ICostantiReato.CAMPO_NUMERO_FONTE%>.value = richiestaScelta.numeroFonte;
       	    document.<%=formname%>.<%=ICostantiReato.CAMPO_ARTICOLO%>.value = richiestaScelta.articolo;
       	    document.<%=formname%>.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>.value = richiestaScelta.codSottonumerazione;
       	    document.<%=formname%>.<%=ICostantiReato.CAMPO_COMMA%>.value = richiestaScelta.comma;
       	    document.<%=formname%>.<%=ICostantiReato.CAMPO_LETTERA%>.value = richiestaScelta.lettera;
       	    document.<%=formname%>.<%=ICostantiReato.CAMPO_NUMERO%>.value = richiestaScelta.numero;
        }
        if (selbeneficio == 'INCOST') {
            // Aggiornamento istantaneo dei campi specifici della Incostituzionalità
        	document.<%=formname%>.<%="annoSCC"%>.value = richiestaScelta.annoCC;
       	    document.<%=formname%>.<%="numeroSCC"%>.value = richiestaScelta.numeroCC;
       	 	document.<%=formname%>.<%="ggScc"%>.value = richiestaScelta.giornoScc;
       		document.<%=formname%>.<%="mmScc"%>.value = richiestaScelta.meseScc;
       		document.<%=formname%>.<%="aaScc"%>.value = richiestaScelta.annoScc;
        }
        
                
      }

     //============================================================================
      // Aggiorna i campi Reclusione e Arresto sommando i dati delle richieste 
      // selezionate
      //============================================================================
      function aggiornaQuantum (tipoAggiornamento){
        //
        righe_tabella = document.getElementById('tabella_richieste').all;
        //aaRec=document.inserisciEsitiTenore.ARec.value;
        //mmRec=document.inserisciEsitiTenore.MRec.value;
        //ggRec=document.inserisciEsitiTenore.GRec.value;
        //multa = document.inserisciEsitiTenore.Multa.value+'.'+document.inserisciEsitiTenore.Mul_dec.value;
        //aaArr = document.inserisciEsitiTenore.AArr.value;
        //mmArr = document.inserisciEsitiTenore.MArr.value;
        //ggArr = document.inserisciEsitiTenore.GArr.value;
        //ammenda = document.inserisciEsitiTenore.Ammenda.value+'.'+document.inserisciEsitiTenore.Amm_dec.value;
        
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
              
            	
            	var idRiga=	righe_tabella(i).id;
                if (idRiga.substring(0,9) == "selrecord")
                	continue;
              
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
                
                aaRec   = document.inserisciEsitiTenore.ARec.value   - parseInt(righe_tabella(i).aaRec);
                
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
            document.<%=formname%>.PM[1].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
          else
            document.<%=formname%>.PM[2].selected=true; // segno -
        }
        else if (recNorm[1]!='0' || recNorm[2]!='0' || recNorm[3]!='0' || annNorm[1]!='0' || annNorm[2]!='0' || annNorm[3]!='0')
        {
          if (tipoAggiornamento=='Annulla')
            document.<%=formname%>.PM[2].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
          else
            document.<%=formname%>.PM[1].selected=true; // segno +
        }
        else {
          // quantum nulli ho solo la pena pecuniaria devo utilizzare il segno della
          // multa o ammenda
//          alert ('quantum nulli');
          if (multa<0 || ammenda<0){
            if (tipoAggiornamento=='Annulla')
              document.<%=formname%>.PM[1].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
            else
              document.<%=formname%>.PM[2].selected=true; // segno -
          }
          else{
            if (tipoAggiornamento=='Annulla')
              document.<%=formname%>.PM[2].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
            else
              document.<%=formname%>.PM[1].selected=true; // segno +
          }
        }
        
        if (multa<0)   multa   = multa*(-1);
        if (ammenda<0) ammenda = ammenda*(-1);
    
        // Reclusione
        
        document.<%=formname%>.ARec.value  = recNorm[1];
        document.<%=formname%>.MRec.value  = recNorm[2];
        document.<%=formname%>.GRec.value  = recNorm[3];
        document.<%=formname%>.Multa.value = parseInt(multa);
        document.<%=formname%>.Mul_dec.value = getParteDecimale(multa);
        
        // Arresti    
        document.<%=formname%>.AArr.value = annNorm[1];
        document.<%=formname%>.MArr.value = annNorm[2];
        document.<%=formname%>.GArr.value = annNorm[3];
        document.<%=formname%>.Ammenda.value=parseInt(ammenda); // recupera la parte intera
        document.<%=formname%>.Amm_dec.value=getParteDecimale(ammenda);
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
      function tipoRichiestaSelezionata (idRecord){  
    	var record=  document.getElementById(idRecord);	
    	if (record.tipoRich == 'A')
    		return 'conAnticipazione';
    	if (record.tipoRich == 'R')
    		return 'senzaAnticipazione';
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





<%
//==============================================================================
// Tabella con la lista delle richieste con e senza anticipazione
//==============================================================================
int id_record = 0;
if (RichiesteAlGE1.size()!=0)
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
  </tr>
<%

  Iterator itx = RichiesteAlGE1.iterator();
  for (int i = 0; itx.hasNext(); i++)
  {
    id_record++;
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)itx.next();
%>
  <tr style="display:none;" id="selrecord_<%=id_record %>">
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

    <% if (   lAnnMod.getFlagAppProvvisoria()!=null 
           && lAnnMod.getFlagAppProvvisoria().equals("A")
          )
       { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else {%>
    <td class="l">&nbsp;</td>
    <% }%>
    <td class="c">
      <a href="Javascript:insertIT('record_<%=id_record%>', '<%=beneficio%>');">Seleziona</a>
    </td>
  </tr>
<% } %>
</table>
<%
}
%>