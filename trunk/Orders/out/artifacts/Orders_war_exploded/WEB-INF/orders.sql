use orders;
select attr.recid, attr.attributeName, type.dataType from orders.ecoresattribute as attr
  join orders.ecoresattributetype as type
  on attr.attributeTypeRef = type.recid;

select val.recid, prval.productRef, attr.attributeName,val.relationType, val.TextValue, val.DateTimeValue, val.FloatValue,val.BooleanValue,
       prval.recid from orders.ecoresvalue  as val
       join orders.ecoresproductattributevalue as prval on val.recid = prval.ecoResValueRef
         join  orders.ecoresattribute as attr on attr.recid = prval.attributeRef;

select * from orders.ecoresvalueenumeration where instancerelationtype = 11152;

select type.recid, type.TypeName, enum.TextValue from orders.ecoresattributetype as type
            join orders.ecoresvalueenumeration as enum on type.recid = enum.InstanceRelationType where type.recid = 11152;

          select attr.recid, attr.attributeName, type.recid, type.TypeName, type.isEnumeration, enum.TextValue from orders.ecoresattribute as attr
            join orders.ecoresattributetype as type on attr.attributeTypeRef = type.recid
              join orders.ecoresvalueenumeration as enum on type.recid = enum.InstanceRelationType
                where attr.recid = 10702;

select * from orders.ecoresproductattributevalue where attributeRef = 10702;
select * from orders.ecoresattributetype