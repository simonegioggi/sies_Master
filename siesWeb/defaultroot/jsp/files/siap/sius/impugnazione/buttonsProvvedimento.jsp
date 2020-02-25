<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<%@page import="siap.siep.provvedimentopm.action.ICostantiProvvedimento"%>
<%@page import="siap.sius.impugnazione.action.ICostantiImpugnazione"%>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>


<%
//==============================================================================
//
// 
//==============================================================================
%>
<script language="JavaScript">

  function confermaInserimento(a_action, a_entityname1, a_entityvalue1, a_entityname2, a_entityvalue2, a_destnname, a_destvalue, a_message )
  {
      str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2 + "&" + a_destnname + "=" +a_destvalue;
      // alert("Stringa di conferma1 ->" + str);
      if (window.confirm("" + a_message ))
      {
        window.location.href=str;
      }
  }

</script>

  <table>
    <tr>
<%
      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
      String strTitolo="";
      if (tipoUfficio.compareTo("TDS")==0 )
        strTitolo = "Elenco Ricorsi per Provvedimento";
      else
        strTitolo = "Elenco Impugnazioni per Provvedimento";
    
      //Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
      {
        Iterator lIterBottoni = lFunFiglie.iterator();
        FunctionModel lFun = null;
        while(lIterBottoni.hasNext())
        {
          lFun = (FunctionModel)lIterBottoni.next();

          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
            // STUB 12/09/2003 Al momento il test è realizzato controllando il Campo FLAG_PIU_MENO, ma in futuro sarà gestito COD_STATO_EVENTO
            // n.b. il tipo funzione è R ma è la funzione di aggiornamento
            if(    lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RICERCA)
                && request.getParameter("FlagPiuMeno").compareTo("R")==0 
              )
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (  !(   request.getParameter("CodTipoProvvedimento").compareTo("02")==0 
                      && (   request.getParameter("CodMotivo").compareTo("0600")==0  
                          || request.getParameter("CodMotivo").compareTo("0601")==0 
                         )
                     )
                  && request.getParameter("numeroImpugnazioni").compareTo("1")==0 
                 )
              {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&tipoOper=A&TornaQui=<%=TornaQui%>">
                  <img src="/images/aggiorna24.gif" width="12" height="12" alt="Aggiorna Ricorso" border="0">
                </a>
              </td>
<%            }
            }
            
            
            
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO))
            //01/10/2007 && (request.getParameter("FlagPiuMeno").compareTo("R")!=0 ))
            {
              // Non è possibile inserire un nuovo ricorso se ne esiste già uno attivo.
              //if (request.getParameter("flagImpugnato").compareTo("N")==0 )
              //{
                // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
                if (! ( request.getParameter("CodTipoProvvedimento").compareTo("02")==0 && (request.getParameter("CodMotivo").compareTo("0600")==0  || request.getParameter("CodMotivo").compareTo("0601")==0 )  ))
                {
                  if (request.getParameter("numeroImpugnazioni").compareTo("0")==0 )
                  {
 %>
                  <td>
                    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&tipoOper=I&TornaQui=<%=TornaQui%>">
                      <img src="/images/new24.gif" alt="Inserisci Ricorso" width="12" height="12" border="0">
                    </a>
                  </td>
<%
                  }
                  else
                  {
%>
                <td>
                <a href="Javascript:confermaInserimento('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>','<%=request.getParameter("CodTipoProvvedimento")%>','TornaQui','<%=TornaQui%>', 'Per il provvedimento è già presente una impugnazione, si vuole inserirne una nuova ?' );">
                <img src="/images/new24.gif" width="12" height="12" alt="Inserisci nuovo Ricorso" border="0">
                  </a>
                </td>
<%
                  }
               }
            }
            
            
            
            // La modifica è operativa solo se esiste il ricorso associato al provvedimento.
            // STUB 12/09/2003 Al momento il test è realizzato controllando il Campo FLAG_PIU_MENO, ma in futuro sarà gestito COD_STATO_EVENTO
            if((lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
              && (request.getParameter("FlagPiuMeno").compareTo("R")==0 ))
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (! ( request.getParameter("CodTipoProvvedimento").compareTo("02")==0 && (request.getParameter("CodMotivo").compareTo("0600")==0  || request.getParameter("CodMotivo").compareTo("0601")==0 )  )
                  && request.getParameter("numeroImpugnazioni").compareTo("1")==0 )
              {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/modifica.gif" width="12" height="12" alt="Modifica Ricorso" border="0">
                </a>
              </td>
<%            }
            }
            
            // 29/10/2007 Il dettaglio del ricorso è operativo solo se esiste un solo ricorso associato al provvedimento.
            if(   (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
               && (request.getParameter("FlagPiuMeno").compareTo("R")==0 )
               && (request.getParameter("numeroImpugnazioni").compareTo("1")==0 )
               && (lFun.getNameAction().compareTo("siap.sius.impugnazione.action.ActLoadDettaglioImpugnazione")==0 ) 
              )
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (! ( request.getParameter("CodTipoProvvedimento").compareTo("02")==0 && (request.getParameter("CodMotivo").compareTo("0600")==0  || request.getParameter("CodMotivo").compareTo("0601")==0 )  ))
              {
%>
                <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazione.CAMPO_NUMERO_IMPUGNAZIONI%>=<%=request.getParameter("numeroImpugnazioni")%>&TornaQui=<%=TornaQui%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Ricorso" border="0">
                  </a>
               </td>
<%            }
            }

            // 29/10/2007 Nel caso di più impugnazioni si punta all'elenco ricorsi associati al provvedimento.
            if(   lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO)
               && request.getParameter("FlagPiuMeno").compareTo("R")==0 
               && (   request.getParameter("numeroImpugnazioni").length()>0
                   && Integer.parseInt(request.getParameter("numeroImpugnazioni"))>1
                  ) 
               && ( lFun.getNameAction().compareTo("siap.sius.impugnazione.action.ActRicercaImpugnazioniDelProvvedimento")==0 ) 
               )
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (! ( request.getParameter("CodTipoProvvedimento").compareTo("02")==0 && (request.getParameter("CodMotivo").compareTo("0600")==0  || request.getParameter("CodMotivo").compareTo("0601")==0 )  ))
              {
%>
                <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazione.CAMPO_NUMERO_IMPUGNAZIONI%>=<%=request.getParameter("numeroImpugnazioni")%>&TornaQui=<%=TornaQui%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="<%=strTitolo%>" border="0">
                  </a>
               </td>
<%            }
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) && (request.getParameter("Stampa").compareTo("SI")==0))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/transfer.gif" alt="Trasferisci" width="12" height="12" border="0">
                </a>
              </td>
<%
            }

      //   ANNULLAMENTO
            if((lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)) && (request.getParameter("FlagPiuMeno").compareTo("R")==0 )
            && request.getParameter("numeroImpugnazioni").compareTo("1")==0 )

            {
%>
          <td>
            <a href="Javascript:confermaAnnullamento('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>','<%=request.getParameter("CodTipoProvvedimento")%>');">
          <img src="/images/delete.gif" width="12" height="12" alt="Annulla" border="0">
            </a>
          </td>
<%
            }
          }
        }
      }
%>
    </tr>
  </table>