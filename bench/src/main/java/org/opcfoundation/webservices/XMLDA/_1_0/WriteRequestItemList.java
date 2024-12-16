/**
 * WriteRequestItemList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.opcfoundation.webservices.XMLDA._1_0;

public class WriteRequestItemList  implements java.io.Serializable {
    private org.opcfoundation.webservices.XMLDA._1_0.ItemValue[] items;
    private java.lang.String itemPath;  // attribute

    public WriteRequestItemList() {
    }

    public WriteRequestItemList(
           org.opcfoundation.webservices.XMLDA._1_0.ItemValue[] items,
           java.lang.String itemPath) {
           this.items = items;
           this.itemPath = itemPath;
    }


    /**
     * Gets the items value for this WriteRequestItemList.
     * 
     * @return items
     */
    public org.opcfoundation.webservices.XMLDA._1_0.ItemValue[] getItems() {
        return items;
    }


    /**
     * Sets the items value for this WriteRequestItemList.
     * 
     * @param items
     */
    public void setItems(org.opcfoundation.webservices.XMLDA._1_0.ItemValue[] items) {
        this.items = items;
    }

    
    /** 
     * @param i
     * @return ItemValue
     */
    public org.opcfoundation.webservices.XMLDA._1_0.ItemValue getItems(int i) {
        return this.items[i];
    }

    public void setItems(int i, org.opcfoundation.webservices.XMLDA._1_0.ItemValue _value) {
        this.items[i] = _value;
    }


    /**
     * Gets the itemPath value for this WriteRequestItemList.
     * 
     * @return itemPath
     */
    public java.lang.String getItemPath() {
        return itemPath;
    }


    /**
     * Sets the itemPath value for this WriteRequestItemList.
     * 
     * @param itemPath
     */
    public void setItemPath(java.lang.String itemPath) {
        this.itemPath = itemPath;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WriteRequestItemList)) return false;
        WriteRequestItemList other = (WriteRequestItemList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.items==null && other.getItems()==null) || 
             (this.items!=null &&
              java.util.Arrays.equals(this.items, other.getItems()))) &&
            ((this.itemPath==null && other.getItemPath()==null) || 
             (this.itemPath!=null &&
              this.itemPath.equals(other.getItemPath())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getItems(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getItemPath() != null) {
            _hashCode += getItemPath().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WriteRequestItemList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "WriteRequestItemList"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("itemPath");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ItemPath"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("items");
        elemField.setXmlName(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "Items"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://opcfoundation.org/webservices/XMLDA/1.0/", "ItemValue"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
