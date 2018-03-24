var XFXml=
{
    useActiveX:(typeof ActiveXObject!="undefined"),
    useDom:document.implementation && document.implementation.createDocument,
    useXmlHttp:(typeof XMLHttpRequest!="undefined")
};
XFXml.ARR_XMLHTTP_VERS=["MSXML2.XmlHttp.5.0",
                       "MSXML2.XmlHttp.4.0",
                       "MSXML2.XmlHttp.3.0",
                       "MSXML2.XmlHttp",
                       "Microsoft.XmlHttp"
                       ];
XFXml.ARR_DOM_VERS=["MSXML2.DOMDocument.5.0",
                   "MSXML2.DOMDocument.4.0",
                   "MSXML2.DOMDocument.3.0",
                   "MSXML2.DOMDocument",
                   "Microsoft.XmlDom"
                   ];
function XFXmlHttp(){}
XFXmlHttp.createRequest=function()
{
    if(XFXml.useXmlHttp)
    {
        return new XMLHttpRequest();
    }
    else if(XFXml.useActiveX)
    {
        if(!XFXml.XMLHTTP_VER)
        {
            for(var i= 0;i < XFXml.ARR_XMLHTTP_VERS.length;i++)
            {
                try
                {
                    new ActiveXObject(XFXml.ARR_XMLHTTP_VERS[i]);
                    XFXml.XMLHTTP_VER = XFXml.ARR_XMLHTTP_VERS[i];
                    break;
                }
                catch(oError)
                {
                    ;
                }
            }
        }
        if(XFXml.XMLHTTP_VER)
        {
            return new ActiveXObject(XFXml.XMLHTTP_VER);
        }
        else
        {
            throw new Error("Could not create XML HTTP Request.");
        }
    }
    else
    {
        throw new Error("Your browser doesn't support an XML HTTP Request.");
    }
};
XFXmlHttp.isSupported=function()
{
    return XFXml.useXmlHttp||XFXml.useActiveX;
};
function XFXmlDom(){}
XFXmlDom.createDocument=function()
{
    if(XFXml.useDom)
    {
        var oXmlDom=document.implementation.createDocument("","",null);
        oXmlDom.parseError=
        {
            valueOf:function()
            {
                return this.errorCode;
            },
            toString:function()
            {
                return this.errorCode.toString()
            }
        };
        oXmlDom.__initError__();
        oXmlDom.addEventListener("load",function()
        {   
            this.__checkForErrors__();
            this.__changeReadyState__(4);
         },false);
         return oXmlDom;;
    }
    else if(XFXml.useActiveX)
    {
        if(!XFXml.DOM_VER)
        {
            for(var i=0;i<XFXml.ARR_DOM_VERS.length;i++)
            {   
                try
                {
                    new ActiveXObject(XFXml.ARR_DOM_VERS[i]);
                    XFXml.DOM_VER=XFXml.ARR_DOM_VERS[i];break;
                 }
                 catch(oError)
                 {
                    ;
                 }
             }
        }
        if(XFXml.DOM_VER)
        {
            return new ActiveXObject(XFXml.DOM_VER);
        }
        else
        {
            throw new Error("Could not create XML DOM document.");
        }
    }
    else
    {
        throw new Error("Your browser doesn't support an XML DOM document.");
    }
};
XFXmlDom.isSupported=function()
{
    return XFXml.useDom||XFXml.useActiveX;
};
var oMozDocument = null;//FireFox Document Object 
if(typeof XMLDocument!="undefined")
{
    oMozDocument = XMLDocument;
}
else if(typeof Document!="undefined")
{
    oMozDocument = Document;
}
if(oMozDocument && !window.opera)
{
    oMozDocument.prototype.readyState = 0;
    oMozDocument.prototype.onreadystatechange = null;
    oMozDocument.prototype.__changeReadyState__= function(iReadyState)
    {
        this.readyState = iReadyState;
        if(typeof this.onreadystatechange == "function")
        {
            this.onreadystatechange();
         }
    };
    oMozDocument.prototype.__initError__=function()
    {
        this.parseError.errorCode=0;
        this.parseError.filepos=-1;
        this.parseError.line=-1;
        this.parseError.linepos=-1;
        this.parseError.reason=null;
        this.parseError.srcText=null;
        this.parseError.url=null;
     };
    oMozDocument.prototype.__checkForErrors__=function()
    {
        if(this.documentElement.tagName=="parsererror")
        {
            var reError=/>([\s\S]*?)Location:([\s\S]*?)Line Number(\d+),Column(\d+):<sourcetext>([\s\S]*?)(?:\-*\^)/;
            reError.test(this.xml);
            this.parseError.errorCode=-999999;
            this.parseError.reason=RegExp.$1;
            this.parseError.url=RegExp.$2;
            this.parseError.line=parseInt(RegExp.$3);
            this.parseError.linepos=parseInt(RegExp.$4);
            this.parseError.srcText=RegExp.$5;
         }
    };
    oMozDocument.prototype.loadXML=function(sXml)
    {
        this.__initError__();
        this.__changeReadyState__(1);
        var oParser = new DOMParser();
        var oXmlDom=oParser.parseFromString(sXml,"text/xml");
        while(this.firstChild){this.removeChild(this.firstChild);}
        for(var i=0;i<oXmlDom.childNodes.length;i++)    
        {
            var oNewNode=this.importNode(oXmlDom.childNodes[i],true);
            this.appendChild(oNewNode);
        }
        this.__checkForErrors__();
        this.__changeReadyState__(4);
    };
    oMozDocument.prototype.__load__= oMozDocument.prototype.load;
    oMozDocument.prototype.load = function(sURL)
    {
        this.__initError__();
        this.__changeReadyState__(1);
        this.__load__(sURL);
     };
    oMozDocument.prototype.selectNodes = function(cXPathString, xNode)
    {
      if( !xNode ) { xNode = this; } 
      var oNSResolver = this.createNSResolver(this.documentElement)
      var aItems = this.evaluate(cXPathString, xNode, oNSResolver, 
                   XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null)
      var aResult = [];
      for( var i = 0; i < aItems.snapshotLength; i++)
      {
         aResult[i] =  aItems.snapshotItem(i);
      }
      return aResult;
    }
    oMozDocument.prototype.selectSingleNode = function(cXPathString, xNode)
    {
      if( !xNode ) { xNode = this; } 
      var xItems = this.selectNodes(cXPathString, xNode);
      if( xItems.length > 0 )
      {
         return xItems[0];
      }
      else
      {
         return null;
      }
    }

    Node.prototype.__defineGetter__("xml",function()
    {
        var oSerializer = new XFXMLSerializer();
        return oSerializer.serializeToString(this,"text/xml");
     });
    Node.prototype.__defineGetter__("text",function()
    {
        var sText="";
        for(var i=0;i<this.childNodes.length;i++)
        {
            if(this.childNodes[i].hasChildNodes())
            {
                sText+=this.childNodes[i].text;
            }
            else
            {
                sText+=this.childNodes[i].nodeValue;
            }
        }return sText;
    });
    Node.prototype.__defineGetter__("value",function()
    {
        return this.nodeValue;
     });
     Node.prototype.__defineSetter__("value",function(XF)
    { 
        return this.nodeValue = XF;
     });
     Node.prototype.__defineGetter__("length",function()
    { 
        return this.childNodes.length;
     }); 
   Element.prototype.selectNodes = function(cXPathString)
   {
      if(this.ownerDocument.selectNodes)
      {
         return this.ownerDocument.selectNodes(cXPathString, this);
      }
      else
      {
        throw "For XML Elements Only";
      }
   }

}
function XFXslt(){}
XFXslt.transformToText=function(oXml,oXslt)
{
    if(typeof XSLTProcessor!="undefined")
    {
        var oProcessor = new XSLTProcessor();
        oProcessor.importStylesheet(oXslt);
        var oResultDom=oProcessor.transformToDocument(oXml);
        var sResult=oResultDom.xml;
        if(sResult.indexOf("<transformiix:result")>-1)
        {
            sResult=sResult.substring(sResult.indexOf(">")+1,sResult.lastIndexOf("<"));
        }
        return sResult;
    }
    else if(zXml.useActiveX)
    {
        return oXml.transformNode(oXslt);
    }
    else
    {
        throw new Error("No XSLT engine found.");
    }
};
function XFXPath(){}
XFXPath.selectNodes=function(oRefNode,sXPath,sXmlNs)
{
    if(typeof XPathEvaluator!="undefined")
    {
        oXmlNs=oXmlNs||{};
        var nsResolver=function(sPrefix)
        {
            return oXmlNs[sPrefix];
        };
        var oEvaluator = new XPathEvaluator();
        var oResult=oEvaluator.evaluate(sXPath,oRefNode,nsResolver,
        XPathResult.ORDERED_NODE_ITERATOR_TYPE,null);
        var aNodes=new Array;
        if(oResult!=null)
        {
            var oElement = oResult.iterateNext();
            while(oElement)
            {
                aNodes.push(oElement);
                oElement = oResult.iterateNext();
            }
         }
         return aNodes;
    }
    else if(zXml.useActiveX)
    {
        if(oXmlNs)
        {
            var sXmlNs="";
            for(var sProp in oXmlNs)
            {
                sXmlNs+="xmlns:"+sProp+"="+oXmlNs[sProp]+" ";
            }
            oRefNode.ownerDocument.setProperty("SelectionNamespaces",sXmlNs);
        };
        return oRefNode.selectNodes(sXPath);
    }
    else
    {
        throw new Error("No XPath engine found.");
    }
};
XFXPath.selectSingleNode=function(oRefNode,sXPath,oXmlNs)
{
    if(typeof XPathEvaluator!="undefined")
    {
        oXmlNs = oXmlNs||{};
        var nsResolver=function(sPrefix)
        {
            return oXmlNs[sPrefix];
        };
        var oEvaluator=new XPathEvaluator();
        var oResult=oEvaluator.evaluate(sXPath,oRefNode,nsResolver,XPathResult.FIRST_ORDERED_NODE_TYPE,null);
        if(oResult!=null)
        {
            return oResult.singleNodeValue;
        }
        else
        {
            return null;
        }
    }
    else if(zXML.useActiveX)
    {
        if(oXmlNs)
        {
            var sXmlNs="";
            for(var sProp in oXmlNs)
            {
                sXmlNs+="xmlns:"+sProp+"="+oXmlNs[sProp]+" ";
            }
            oRefNode.ownerDocument.setProperty("SelectionNamespaces",sXmlNs);
        };
        return oRefNode.selectSingleNode(sXPath);
    }
    else
    {
        throw new Error("No XPath engine found.");
    }
};
function XFXMLSerializer(){}
XFXMLSerializer.prototype.serializeToString=function(oNode)
{
    var sXml="";
    switch(oNode.nodeType)
    {
        case 1:
            sXml="<"+oNode.tagName;
            for(var i=0;i<oNode.attributes.length;i++)
            {
                sXml+=" "+oNode.attributes[i].name+"=\""+oNode.attributes[i].value+"\"";
            }
            sXml+=">";
            for(var i=0;i<oNode.childNodes.length;i++)
            {
                sXml+=this.serializeToString(oNode.childNodes[i]);
             }
            sXml+="</"+oNode.tagName+">";
            break;
        case 3:
            sXml=oNode.nodeValue;
            break;
        case 4:
            sXml="<![CDATA["+oNode.nodeValue+"]]>";
            break;
        case 7:
            sXml="<?"+oNode.nodevalue+"?>";
            break;
        case 8:
            sXml="<!--"+oNode.nodevalue+"-->";
            break;
        case 9:
        for(var i=0;i<oNode.childNodes.length;i++)
        {
            sXml+=this.serializeToString(oNode.childNodes[i]);
        }
        break;
     
   };
   return sXml;
};



