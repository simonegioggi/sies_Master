<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoFascicoloLicenzeModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoModel"%>

<jsp:useBean id="decreti"            scope="request" class="java.util.Vector"/>

<script language="JavaScript">
<% if (decreti.size() > 0 )
   {
     DepositoDecretoFascicoloLicenzeModel primoElemento = (DepositoDecretoFascicoloLicenzeModel) decreti.get(0);
%>
    // Memorizza id del DIV attivo
    var DivAttivo = <%=primoElemento.getDepositoDecreto().getIdEventoGenerato().toString()%>;
    // 18/06/2007 Definizione degli array contenenti i dati permessi assegnati.
    var numPermessi = <%=decreti.size()%>;   /* numero complessivo esperti  */
    var ggRevoca = new Array(numPermessi);   /* Array dei giorni di revoca  */
    var hhRevoca = new Array(numPermessi);   /* Array delle ore di revoca  */

<%
    for (int i = 0; i < decreti.size() ; i++)
    {
    	DepositoDecretoFascicoloLicenzeModel lDecFasLic = null;
    	lDecFasLic = (DepositoDecretoFascicoloLicenzeModel) decreti.get(i);
    	FascicoloSiusModel fascicolo = lDecFasLic.getFascicolo();
    	DepositoDecretoModel decreto = lDecFasLic.getDepositoDecreto();
    	LicenzaLibAnticipataModel[] licenze = lDecFasLic.getLicenze();
      if(licenze[0].getNumeroGiorni() != null )
      {%>
      	ggRevoca[<%=i%>] = <%=licenze[0].getNumeroGiorni()%>;
    <%}else{%> ggRevoca[<%=i%>] = 0;<%}
      if(licenze[0].getNumeroOre() != null )
      {%>
      	hhRevoca[<%=i%>] = <%=licenze[0].getNumeroOre()%>;
    <%}else{%> hhRevoca[<%=i%>] = 0;<%}
    }%>

    // La funzione attiva il DIV individuato dal parametro id
    function scopri(id)
    {
      node=document.getElementById(id);
      node.style.display= "block";
      DivAttivo = id;
      //alert("attivato DIV  -> " + id);
      // 18/06/2007 gestione indiceComboPermessi.
      indiceComboPermessi = document.getElementById("selezione").options.selectedIndex;
    }

    // La funzione disattiva il DIV individuato dal parametro id
    function copri(id)
    {
      node=document.getElementById(id);
      node.style.display= "none";
      //alert("disattivato DIV -> " + id);
    }

    // La funzione cambia il DIV attivo
    function cambiaDIV(id)
    {
      //alert("nuovo id -> " + id);
      copri(DivAttivo);
      scopri(id);
      DivAttivo = id;
    }

    function init()
    {
     // alert("init")
      node=document.getElementById("selezione");
      var val = node.value;
      indiceComboPermessi = document.getElementById("selezione").options.selectedIndex;
      //alert("indiceComboPermessi = " + indiceComboPermessi);
      cambiaDIV(val);
    }

</script>
<body onLoad="init();">
<div id="ciccio" style="display:block">
<table>
    <tr>
      <td class="Titolo" colspan=6> Dettaglio Del Decreto Permesso di Riferimento </td>
    </tr>

<%
       // Dati del Provvedimento corrente nella lista
       DepositoDecretoFascicoloLicenzeModel lDecFasLic = null;
%>
       <tr>
        <td class="l">Seleziona Provvedimento </td>
        <td class="L">
        <select id="selezione" title="ProvvDiRiferimento" class=small  name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>" onChange="cambiaDIV(value);">
<%
       // Ciclo sui provvedimenti per costruire la combo-box
       for (int i = 0; i < decreti.size() ; i++)
       {
            lDecFasLic = (DepositoDecretoFascicoloLicenzeModel) decreti.get(i);
%>
            <option value="<%=lDecFasLic.getDepositoDecreto().getIdEventoGenerato()%>"/>  decreto del <%=" " + DateUtils.getDateToString(lDecFasLic.getDepositoDecreto().getDataEmissione(),"dd/MM/yyyy")%>
<%
       }
 %>
           </select>
        </td>
      </tr>
</table>
</div>

<%
        String display ="none";
        for (int i = 0; i < decreti.size() ; i++)
       {
         lDecFasLic = (DepositoDecretoFascicoloLicenzeModel) decreti.get(i);
         FascicoloSiusModel fascicolo = lDecFasLic.getFascicolo();
         DepositoDecretoModel decreto = lDecFasLic.getDepositoDecreto();
         LicenzaLibAnticipataModel[] licenze = lDecFasLic.getLicenze();
%>

<div id="<%=decreto.getIdEventoGenerato()%>" style="display:<%=display%>">
<table>
    <tr>

      <td class="l">
        <font class="label">Procedimento N.</font>
      </td>
      <td class="l">
        <font class="campo"><%=fascicolo.getChiaveAnno()%> / <%=fascicolo.getChiaveProgr()%>&nbsp;</font>
      </td>
    </tr>
  <tr>
    <td class="l"> Data Emissione</td>
    <td class="l"><font class="campo"> <%=DateUtils.getDateToString(decreto.getDataEmissione(),"dd/MM/yyyy")%></font></td>
  </tr>
<% if (decreto.getDataDeposito() != null)
{ %>
  <tr>
    <td class="l"> Anno / Numero del Decreto</td>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(decreto.getAnnoS72())%> / <%=StringUtils.toStringJSP(decreto.getNumS72())%></font></td>
  </tr>
  <tr>
    <td class="l"> Data Deposito in Cancelleria</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(decreto.getDataDeposito(),"dd/MM/yyyy")%></font></td>
  </tr>
<% } %>
<%
        for (int j = 0; j< licenze.length; j++)
        {
%>
          <tr>
            <td class="l">Tipo Permesso</td>
            <td class=l><font class="campo"> <%=StringUtils.toStringJSP(licenze[j].getDescrTipoLicenza(),"-")%></font></td>
          </tr>
          <tr>
            <td class="l">Stato Permesso</td>
            <td class=l><font class="campo"> <%=StringUtils.toStringJSP(licenze[j].getDescrStatoPermesso(),"-")%></font></td>
          </tr>
          <tr>
            <td class="l">Durata</td>
            <td class="l"> <% if(licenze[j].getNumeroGiorni() != null) {%> giorni <font class="campo"> <%=" " + StringUtils.toStringJSP(licenze[j].getNumeroGiorni(),"-") + " "%> </font> <% } if(licenze[j].getNumeroOre() != null) {%> ore <font class="campo"> <%=" " + StringUtils.toStringJSP(licenze[j].getNumeroOre(),"-")%> </font> <%}%></td>
          </tr>
          <tr>
            <td class="l">Luogo fruizione / Oggetto permesso</td>
            <td class="l"> <font class="campo"> <%=StringUtils.toStringJSP(licenze[j].getLuogoSvolgimentoProva(),"-")%></font></td>
         </tr>
          <tr>
            <td class="l">Presenza Scorta</td>
<%            if (licenze[j].getFlagScorta().toUpperCase().compareTo( "N") == 0)
                {%><td class="l"><font class="campo"> No</font></td><%}
              else{%><td class="l"><font class="campo"> Si</font></td><%}%>
          </tr>
          <tr>
            <td class="l">Motivazione provvedimento</td>
            <td class="l"><font class="campo">  <%=StringUtils.toStringJSP(decreto.getNote(),"-")%></font></td>
          </tr>
</table>
</div>
<%
        } // endwhile
        display ="none";
      } // endfor
%>
</body>
<%
   } // endif
%>