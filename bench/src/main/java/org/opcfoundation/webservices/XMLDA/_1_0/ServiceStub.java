/**
 * ServiceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.opcfoundation.webservices.XMLDA._1_0;

public class ServiceStub extends org.apache.axis.client.Stub implements org.opcfoundation.webservices.XMLDA._1_0.Service {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[8];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "GetStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">GetStatus"), org.opcfoundation.webservices.XMLDA._1_0.GetStatus.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">GetStatusResponse"));
        oper.setReturnClass(org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "GetStatusResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Read");
//Siemens ThH: false generated code
param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Read"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Read"), org.opcfoundation.webservices.XMLDA._1_0.Read.class, false, false);
oper.addParameter(param);
//        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Options"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "RequestOptions"), org.opcfoundation.webservices.XMLDA._1_0.RequestOptions.class, false, false);
//        oper.addParameter(param);
//        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ItemList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReadRequestItemList"), org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList.class, false, false);
//        oper.addParameter(param);
//        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReadResult"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReplyBase"), org.opcfoundation.webservices.XMLDA._1_0.ReplyBase.class, false, false);
//        oper.addParameter(param);
//        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "RItemList"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReplyItemList"), org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList.class, false, false);
//        oper.addParameter(param);
//        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Errors"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "OPCError"), org.opcfoundation.webservices.XMLDA._1_0.OPCError[].class, false, false);
//        oper.addParameter(param);
//        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
oper.setReturnType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">ReadResponse"));
oper.setReturnClass(org.opcfoundation.webservices.XMLDA._1_0.ReadResponse.class);
oper.setReturnQName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReadResponse"));
//End ThH
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Write");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Write"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Write"), org.opcfoundation.webservices.XMLDA._1_0.Write.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">WriteResponse"));
        oper.setReturnClass(org.opcfoundation.webservices.XMLDA._1_0.WriteResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "WriteResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Subscribe");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Subscribe"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Subscribe"), org.opcfoundation.webservices.XMLDA._1_0.Subscribe.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscribeResponse"));
        oper.setReturnClass(org.opcfoundation.webservices.XMLDA._1_0.SubscribeResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscribeResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SubscriptionPolledRefresh");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscriptionPolledRefresh"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscriptionPolledRefresh"), org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefresh.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscriptionPolledRefreshResponse"));
        oper.setReturnClass(org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefreshResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscriptionPolledRefreshResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SubscriptionCancel");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscriptionCancel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscriptionCancel"), org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancel.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscriptionCancelResponse"));
        oper.setReturnClass(org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscriptionCancelResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Browse");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Browse"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Browse"), org.opcfoundation.webservices.XMLDA._1_0.Browse.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">BrowseResponse"));
        oper.setReturnClass(org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "BrowseResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetProperties");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "GetProperties"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">GetProperties"), org.opcfoundation.webservices.XMLDA._1_0.GetProperties.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">GetPropertiesResponse"));
        oper.setReturnClass(org.opcfoundation.webservices.XMLDA._1_0.GetPropertiesResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "GetPropertiesResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

    }

    public ServiceStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ServiceStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ServiceStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Browse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.Browse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">BrowseResponse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">GetProperties");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.GetProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">GetPropertiesResponse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.GetPropertiesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">GetStatus");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.GetStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">GetStatusResponse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Read");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.Read.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">ReadResponse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ReadResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Subscribe");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.Subscribe.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscribeResponse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscribeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscriptionCancel");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancel.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscriptionCancelResponse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscriptionPolledRefresh");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefresh.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">SubscriptionPolledRefreshResponse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefreshResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">Write");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.Write.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", ">WriteResponse");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.WriteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfAnyType");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfAnyType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfBoolean");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfBoolean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfByte");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfByte.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfDateTime");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfDateTime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfDecimal");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfDecimal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfDouble");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfDouble.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfFloat");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfFloat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfInt");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfInt.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfLong");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfLong.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfShort");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfShort.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfString");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfUnsignedInt");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfUnsignedInt.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfUnsignedLong");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfUnsignedLong.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ArrayOfUnsignedShort");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ArrayOfUnsignedShort.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "BrowseElement");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.BrowseElement.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "browseFilter");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.BrowseFilter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "interfaceVersion");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.InterfaceVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ItemIdentifier");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ItemIdentifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ItemProperty");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ItemProperty.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ItemValue");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ItemValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "limitBits");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.LimitBits.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "OPCError");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.OPCError.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "OPCQuality");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.OPCQuality.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "PropertyReplyList");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.PropertyReplyList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "qualityBits");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.QualityBits.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReadRequestItem");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReadRequestItemList");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReplyBase");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ReplyBase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReplyItemList");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "RequestOptions");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.RequestOptions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "serverState");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ServerState.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ServerStatus");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.ServerStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscribeItemValue");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscribeItemValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscribePolledRefreshReplyItemList");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscribePolledRefreshReplyItemList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscribeReplyItemList");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscribeReplyItemList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscribeRequestItem");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "SubscribeRequestItemList");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.SubscribeRequestItemList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "WriteRequestItemList");
            cachedSerQNames.add(qName);
            cls = org.opcfoundation.webservices.XMLDA._1_0.WriteRequestItemList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    
    /** 
     * @return Call
     * @throws java.rmi.RemoteException
     */
    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse getStatus(org.opcfoundation.webservices.XMLDA._1_0.GetStatus parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://opcfoundation.org/webservices/XMLDA/1.0/GetStatus");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.opcfoundation.webservices.XMLDA._1_0.GetStatusResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

//Siemens ThH: false generated code
//    public void read(org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options, org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList itemList, org.opcfoundation.webservices.XMLDA._1_0.holders.ReplyBaseHolder readResult, org.opcfoundation.webservices.XMLDA._1_0.holders.ReplyItemListHolder RItemList, org.opcfoundation.webservices.XMLDA._1_0.holders.OPCErrorArrayHolder errors) throws java.rmi.RemoteException {
    public org.opcfoundation.webservices.XMLDA._1_0.ReadResponse read(org.opcfoundation.webservices.XMLDA._1_0.RequestOptions options, org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList itemList/*, org.opcfoundation.webservices.XMLDA._1_0.holders.ReplyBaseHolder readResult, org.opcfoundation.webservices.XMLDA._1_0.holders.ReplyItemListHolder RItemList, org.opcfoundation.webservices.XMLDA._1_0.holders.OPCErrorArrayHolder errors*/) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://opcfoundation.org/webservices/XMLDA/1.0/Read");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Read"));

//Siemens ThH: false generated code
Read read = new Read(options, itemList);

        setRequestHeaders(_call);
        setAttachments(_call);
//Siemens ThH: false generated code
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {read});//options, itemList});
 //try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {options, itemList});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
//Siemens ThH: false generated code
try {
      return (org.opcfoundation.webservices.XMLDA._1_0.ReadResponse) _resp;
} catch (java.lang.Exception _exception) {
      return (org.opcfoundation.webservices.XMLDA._1_0.ReadResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.opcfoundation.webservices.XMLDA._1_0.ReadResponse.class);
}
//End ThH
/*Siemens ThH: false generated code
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                readResult.value = (org.opcfoundation.webservices.XMLDA._1_0.ReplyBase) _output.get(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReadResult"));
            } catch (java.lang.Exception _exception) {
                readResult.value = (org.opcfoundation.webservices.XMLDA._1_0.ReplyBase) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ReadResult")), org.opcfoundation.webservices.XMLDA._1_0.ReplyBase.class);
            }
            try {
                RItemList.value = (org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList) _output.get(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "RItemList"));
            } catch (java.lang.Exception _exception) {
                RItemList.value = (org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "RItemList")), org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList.class);
            }
            try {
                errors.value = (org.opcfoundation.webservices.XMLDA._1_0.OPCError[]) _output.get(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Errors"));
            } catch (java.lang.Exception _exception) {
                errors.value = (org.opcfoundation.webservices.XMLDA._1_0.OPCError[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Errors")), org.opcfoundation.webservices.XMLDA._1_0.OPCError[].class);
            }
End ThH*/
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.opcfoundation.webservices.XMLDA._1_0.WriteResponse write(org.opcfoundation.webservices.XMLDA._1_0.Write parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://opcfoundation.org/webservices/XMLDA/1.0/Write");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Write"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.opcfoundation.webservices.XMLDA._1_0.WriteResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.opcfoundation.webservices.XMLDA._1_0.WriteResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.opcfoundation.webservices.XMLDA._1_0.WriteResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.opcfoundation.webservices.XMLDA._1_0.SubscribeResponse subscribe(org.opcfoundation.webservices.XMLDA._1_0.Subscribe parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://opcfoundation.org/webservices/XMLDA/1.0/Subscribe");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Subscribe"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.opcfoundation.webservices.XMLDA._1_0.SubscribeResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.opcfoundation.webservices.XMLDA._1_0.SubscribeResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.opcfoundation.webservices.XMLDA._1_0.SubscribeResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefreshResponse subscriptionPolledRefresh(org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefresh parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://opcfoundation.org/webservices/XMLDA/1.0/SubscriptionPolledRefresh");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "SubscriptionPolledRefresh"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefreshResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefreshResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.opcfoundation.webservices.XMLDA._1_0.SubscriptionPolledRefreshResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse subscriptionCancel(org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancel parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://opcfoundation.org/webservices/XMLDA/1.0/SubscriptionCancel");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "SubscriptionCancel"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.opcfoundation.webservices.XMLDA._1_0.SubscriptionCancelResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse browse(org.opcfoundation.webservices.XMLDA._1_0.Browse parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://opcfoundation.org/webservices/XMLDA/1.0/Browse");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Browse"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.opcfoundation.webservices.XMLDA._1_0.BrowseResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.opcfoundation.webservices.XMLDA._1_0.GetPropertiesResponse getProperties(org.opcfoundation.webservices.XMLDA._1_0.GetProperties parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://opcfoundation.org/webservices/XMLDA/1.0/GetProperties");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetProperties"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.opcfoundation.webservices.XMLDA._1_0.GetPropertiesResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.opcfoundation.webservices.XMLDA._1_0.GetPropertiesResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.opcfoundation.webservices.XMLDA._1_0.GetPropertiesResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
